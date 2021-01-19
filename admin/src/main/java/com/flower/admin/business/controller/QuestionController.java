package com.flower.admin.business.controller;

import com.flower.admin.business.validator.QuestionValid;
import com.flower.common.enums.StatusEnum;
import com.flower.common.utils.EntityBeanUtil;
import com.flower.common.utils.ResultVoUtil;
import com.flower.common.utils.StatusUtil;
import com.flower.common.vo.ResultVo;
import com.flower.modules.business.domain.Question;
import com.flower.modules.business.service.QuestionService;
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
@RequestMapping("/business/question")
public class QuestionController {

    @Autowired
    private QuestionService questionService;

    /**
     * 列表页面
     */
    @GetMapping("/index")
    @RequiresPermissions("business:question:index")
    public String index(Model model, Question question) {

        // 创建匹配器，进行动态查询匹配
        ExampleMatcher matcher = ExampleMatcher.matching()
                .withMatcher("content", match -> match.contains());

        // 获取数据列表
        Example<Question> example = Example.of(question, matcher);
        Page<Question> list = questionService.getPageList(example);

        // 封装数据
        model.addAttribute("list", list.getContent());
        model.addAttribute("page", list);
        return "/business/question/index";
    }

    /**
     * 跳转到添加页面
     */
    @GetMapping("/add")
    @RequiresPermissions("business:question:add")
    public String toAdd() {
        return "/business/question/add";
    }

    /**
     * 跳转到编辑页面
     */
    @GetMapping("/edit/{id}")
    @RequiresPermissions("business:question:edit")
    public String toEdit(@PathVariable("id") Question question, Model model) {
        model.addAttribute("question", question);
        return "/business/question/add";
    }

    /**
     * 保存添加/修改的数据
     * @param valid 验证对象
     */
    @PostMapping("/save")
    @RequiresPermissions({"business:question:add", "business:question:edit"})
    @ResponseBody
    public ResultVo save(@Validated QuestionValid valid, Question question) {
        // 复制保留无需修改的数据
        if (question.getId() != null) {
            Question beQuestion = questionService.getById(question.getId());
            EntityBeanUtil.copyProperties(beQuestion, question);
        }

        // 保存数据
        questionService.save(question);
        return ResultVoUtil.SAVE_SUCCESS;
    }

    /**
     * 跳转到详细页面
     */
    @GetMapping("/detail/{id}")
    @RequiresPermissions("business:question:detail")
    public String toDetail(@PathVariable("id") Question question, Model model) {
        model.addAttribute("question",question);
        return "/business/question/detail";
    }

    /**
     * 设置一条或者多条数据的状态
     */
    @RequestMapping("/status/{param}")
    @RequiresPermissions("business:question:status")
    @ResponseBody
    public ResultVo status(
            @PathVariable("param") String param,
            @RequestParam(value = "ids", required = false) List<Long> ids) {
        // 更新状态
        StatusEnum statusEnum = StatusUtil.getStatusEnum(param);
        if (questionService.updateStatus(statusEnum, ids)) {
            return ResultVoUtil.success(statusEnum.getMessage() + "成功");
        } else {
            return ResultVoUtil.error(statusEnum.getMessage() + "失败，请重新操作");
        }
    }
}