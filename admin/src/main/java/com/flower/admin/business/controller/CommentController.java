package com.flower.admin.business.controller;

import com.flower.admin.business.validator.CommentValid;
import com.flower.common.enums.StatusEnum;
import com.flower.common.utils.EntityBeanUtil;
import com.flower.common.utils.ResultVoUtil;
import com.flower.common.utils.StatusUtil;
import com.flower.common.vo.ResultVo;
import com.flower.modules.business.domain.Comment;
import com.flower.modules.business.service.CommentService;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author cyy
 * @date 2021/01/17
 */
@Controller
@RequestMapping("/business/comment")
public class CommentController {

    @Autowired
    private CommentService commentService;

    /**
     * 列表页面
     */
    @GetMapping("/index")
    @RequiresPermissions("business:comment:index")
    public String index(Model model, Comment comment) {

        // 创建匹配器，进行动态查询匹配
        ExampleMatcher matcher = ExampleMatcher.matching();

        // 获取数据列表
        Example<Comment> example = Example.of(comment, matcher);
        Page<Comment> list = commentService.getPageList(example);

        // 封装数据
        model.addAttribute("list", list.getContent());
        model.addAttribute("page", list);
        return "/business/comment/index";
    }

    /**
     * 跳转到添加页面
     */
    @GetMapping("/add")
    @RequiresPermissions("business:comment:add")
    public String toAdd() {
        return "/business/comment/add";
    }

    /**
     * 跳转到编辑页面
     */
    @GetMapping("/edit/{id}")
    @RequiresPermissions("business:comment:edit")
    public String toEdit(@PathVariable("id") Comment comment, Model model) {
        model.addAttribute("comment", comment);
        return "/business/comment/add";
    }

    /**
     * 保存添加/修改的数据
     * @param valid 验证对象
     */
    @PostMapping("/save")
    @RequiresPermissions({"business:comment:add", "business:comment:edit"})
    @ResponseBody
    public ResultVo save(@Validated CommentValid valid, Comment comment) {
        // 复制保留无需修改的数据
        if (comment.getId() != null) {
            Comment beComment = commentService.getById(comment.getId());
            EntityBeanUtil.copyProperties(beComment, comment);
        }

        // 保存数据
        commentService.save(comment);
        return ResultVoUtil.SAVE_SUCCESS;
    }

    /**
     * 跳转到详细页面
     */
    @GetMapping("/detail/{id}")
    @RequiresPermissions("business:comment:detail")
    public String toDetail(@PathVariable("id") Comment comment, Model model) {
        model.addAttribute("comment",comment);
        return "/business/comment/detail";
    }

    /**
     * 设置一条或者多条数据的状态
     */
    @RequestMapping("/status/{param}")
    @RequiresPermissions("business:comment:status")
    @ResponseBody
    public ResultVo status(
            @PathVariable("param") String param,
            @RequestParam(value = "ids", required = false) List<Long> ids) {
        // 更新状态
        StatusEnum statusEnum = StatusUtil.getStatusEnum(param);
        if (commentService.updateStatus(statusEnum, ids)) {
            return ResultVoUtil.success(statusEnum.getMessage() + "成功");
        } else {
            return ResultVoUtil.error(statusEnum.getMessage() + "失败，请重新操作");
        }
    }
}