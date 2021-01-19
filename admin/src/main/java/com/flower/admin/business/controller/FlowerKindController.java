package com.flower.admin.business.controller;

import com.flower.admin.business.validator.FlowerKindValid;
import com.flower.common.enums.StatusEnum;
import com.flower.common.utils.EntityBeanUtil;
import com.flower.common.utils.ResultVoUtil;
import com.flower.common.utils.StatusUtil;
import com.flower.common.vo.ResultVo;
import com.flower.modules.business.domain.FlowerKind;
import com.flower.modules.business.service.FlowerKindService;
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
@RequestMapping("/business/flowerKind")
public class FlowerKindController {

    @Autowired
    private FlowerKindService flowerKindService;

    /**
     * 列表页面
     */
    @GetMapping("/index")
    @RequiresPermissions("business:flowerKind:index")
    public String index(Model model, FlowerKind flowerKind) {

        // 创建匹配器，进行动态查询匹配
        ExampleMatcher matcher = ExampleMatcher.matching()
                .withMatcher("kindName", match -> match.contains());

        // 获取数据列表
        Example<FlowerKind> example = Example.of(flowerKind, matcher);
        Page<FlowerKind> list = flowerKindService.getPageList(example);

        // 封装数据
        model.addAttribute("list", list.getContent());
        model.addAttribute("page", list);
        return "/business/flowerKind/index";
    }

    /**
     * 跳转到添加页面
     */
    @GetMapping("/add")
    @RequiresPermissions("business:flowerKind:add")
    public String toAdd() {
        return "/business/flowerKind/add";
    }

    /**
     * 跳转到编辑页面
     */
    @GetMapping("/edit/{id}")
    @RequiresPermissions("business:flowerKind:edit")
    public String toEdit(@PathVariable("id") FlowerKind flowerKind, Model model) {
        model.addAttribute("flowerKind", flowerKind);
        return "/business/flowerKind/add";
    }

    /**
     * 保存添加/修改的数据
     * @param valid 验证对象
     */
    @PostMapping("/save")
    @RequiresPermissions({"business:flowerKind:add", "business:flowerKind:edit"})
    @ResponseBody
    public ResultVo save(@Validated FlowerKindValid valid, FlowerKind flowerKind) {
        // 复制保留无需修改的数据
        if (flowerKind.getId() != null) {
            FlowerKind beFlowerKind = flowerKindService.getById(flowerKind.getId());
            EntityBeanUtil.copyProperties(beFlowerKind, flowerKind);
        }

        // 保存数据
        flowerKindService.save(flowerKind);
        return ResultVoUtil.SAVE_SUCCESS;
    }

    /**
     * 跳转到详细页面
     */
    @GetMapping("/detail/{id}")
    @RequiresPermissions("business:flowerKind:detail")
    public String toDetail(@PathVariable("id") FlowerKind flowerKind, Model model) {
        model.addAttribute("flowerKind",flowerKind);
        return "/business/flowerKind/detail";
    }

    /**
     * 设置一条或者多条数据的状态
     */
    @RequestMapping("/status/{param}")
    @RequiresPermissions("business:flowerKind:status")
    @ResponseBody
    public ResultVo status(
            @PathVariable("param") String param,
            @RequestParam(value = "ids", required = false) List<Long> ids) {
        // 更新状态
        StatusEnum statusEnum = StatusUtil.getStatusEnum(param);
        if (flowerKindService.updateStatus(statusEnum, ids)) {
            return ResultVoUtil.success(statusEnum.getMessage() + "成功");
        } else {
            return ResultVoUtil.error(statusEnum.getMessage() + "失败，请重新操作");
        }
    }
}