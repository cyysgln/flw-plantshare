package com.flower.admin.business.controller;

import com.flower.admin.business.validator.EssayValid;
import com.flower.common.enums.StatusEnum;
import com.flower.common.utils.EntityBeanUtil;
import com.flower.common.utils.ResultVoUtil;
import com.flower.common.utils.StatusUtil;
import com.flower.common.vo.ResultVo;
import com.flower.modules.business.domain.Essay;
import com.flower.modules.business.service.EssayService;
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
@RequestMapping("/business/essay")
public class EssayController {

    @Autowired
    private EssayService essayService;

    /**
     * 列表页面
     */
    @GetMapping("/index")
    @RequiresPermissions("business:essay:index")
    public String index(Model model, Essay essay) {

        // 创建匹配器，进行动态查询匹配
        ExampleMatcher matcher = ExampleMatcher.matching()
                .withMatcher("title", match -> match.contains());

        // 获取数据列表
        Example<Essay> example = Example.of(essay, matcher);
        Page<Essay> list = essayService.getPageList(example);

        // 封装数据
        model.addAttribute("list", list.getContent());
        model.addAttribute("page", list);
        return "/business/essay/index";
    }

    /**
     * 跳转到添加页面
     */
    @GetMapping("/add")
    @RequiresPermissions("business:essay:add")
    public String toAdd() {
        return "/business/essay/add";
    }

    /**
     * 跳转到编辑页面
     */
    @GetMapping("/edit/{id}")
    @RequiresPermissions("business:essay:edit")
    public String toEdit(@PathVariable("id") Essay essay, Model model) {
        model.addAttribute("essay", essay);
        return "/business/essay/add";
    }

    /**
     * 保存添加/修改的数据
     * @param valid 验证对象
     */
    @PostMapping("/save")
    @RequiresPermissions({"business:essay:add", "business:essay:edit"})
    @ResponseBody
    public ResultVo save(@Validated EssayValid valid, Essay essay) {
        // 复制保留无需修改的数据
        if (essay.getId() != null) {
            Essay beEssay = essayService.getById(essay.getId());
            EntityBeanUtil.copyProperties(beEssay, essay);
        }

        // 保存数据
        essayService.save(essay);
        return ResultVoUtil.SAVE_SUCCESS;
    }

    /**
     * 跳转到详细页面
     */
    @GetMapping("/detail/{id}")
    @RequiresPermissions("business:essay:detail")
    public String toDetail(@PathVariable("id") Essay essay, Model model) {
        model.addAttribute("essay",essay);
        return "/business/essay/detail";
    }

    /**
     * 设置一条或者多条数据的状态
     */
    @RequestMapping("/status/{param}")
    @RequiresPermissions("business:essay:status")
    @ResponseBody
    public ResultVo status(
            @PathVariable("param") String param,
            @RequestParam(value = "ids", required = false) List<Long> ids) {
        // 更新状态
        StatusEnum statusEnum = StatusUtil.getStatusEnum(param);
        if (essayService.updateStatus(statusEnum, ids)) {
            return ResultVoUtil.success(statusEnum.getMessage() + "成功");
        } else {
            return ResultVoUtil.error(statusEnum.getMessage() + "失败，请重新操作");
        }
    }
}