package com.flower.admin.business.controller;

import com.flower.admin.business.validator.EssayCategoryValid;
import com.flower.common.enums.StatusEnum;
import com.flower.common.utils.EntityBeanUtil;
import com.flower.common.utils.ResultVoUtil;
import com.flower.common.utils.StatusUtil;
import com.flower.common.vo.ResultVo;
import com.flower.modules.business.domain.EssayCategory;
import com.flower.modules.business.service.EssayCategoryService;
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
@RequestMapping("/business/essayCategory")
public class EssayCategoryController {

    @Autowired
    private EssayCategoryService essayCategoryService;

    /**
     * 列表页面
     */
    @GetMapping("/index")
    @RequiresPermissions("business:essayCategory:index")
    public String index(Model model, EssayCategory essayCategory) {

        // 创建匹配器，进行动态查询匹配
        ExampleMatcher matcher = ExampleMatcher.matching()
                .withMatcher("parentCategoryId", match -> match.contains());

        // 获取数据列表
        Example<EssayCategory> example = Example.of(essayCategory, matcher);
        Page<EssayCategory> list = essayCategoryService.getPageList(example);

        // 封装数据
        model.addAttribute("list", list.getContent());
        model.addAttribute("page", list);
        return "/business/essayCategory/index";
    }

    /**
     * 跳转到添加页面
     */
    @GetMapping("/add")
    @RequiresPermissions("business:essayCategory:add")
    public String toAdd() {
        return "/business/essayCategory/add";
    }

    /**
     * 跳转到编辑页面
     */
    @GetMapping("/edit/{id}")
    @RequiresPermissions("business:essayCategory:edit")
    public String toEdit(@PathVariable("id") EssayCategory essayCategory, Model model) {
        model.addAttribute("essayCategory", essayCategory);
        return "/business/essayCategory/add";
    }

    /**
     * 保存添加/修改的数据
     * @param valid 验证对象
     */
    @PostMapping("/save")
    @RequiresPermissions({"business:essayCategory:add", "business:essayCategory:edit"})
    @ResponseBody
    public ResultVo save(@Validated EssayCategoryValid valid, EssayCategory essayCategory) {
        // 复制保留无需修改的数据
        if (essayCategory.getId() != null) {
            EssayCategory beEssayCategory = essayCategoryService.getById(essayCategory.getId());
            EntityBeanUtil.copyProperties(beEssayCategory, essayCategory);
        }

        // 保存数据
        essayCategoryService.save(essayCategory);
        return ResultVoUtil.SAVE_SUCCESS;
    }

    /**
     * 跳转到详细页面
     */
    @GetMapping("/detail/{id}")
    @RequiresPermissions("business:essayCategory:detail")
    public String toDetail(@PathVariable("id") EssayCategory essayCategory, Model model) {
        model.addAttribute("essayCategory",essayCategory);
        return "/business/essayCategory/detail";
    }

    /**
     * 设置一条或者多条数据的状态
     */
    @RequestMapping("/status/{param}")
    @RequiresPermissions("business:essayCategory:status")
    @ResponseBody
    public ResultVo status(
            @PathVariable("param") String param,
            @RequestParam(value = "ids", required = false) List<Long> ids) {
        // 更新状态
        StatusEnum statusEnum = StatusUtil.getStatusEnum(param);
        if (essayCategoryService.updateStatus(statusEnum, ids)) {
            return ResultVoUtil.success(statusEnum.getMessage() + "成功");
        } else {
            return ResultVoUtil.error(statusEnum.getMessage() + "失败，请重新操作");
        }
    }
}