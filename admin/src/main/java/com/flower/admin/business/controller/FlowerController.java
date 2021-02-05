package com.flower.admin.business.controller;

import com.flower.admin.business.validator.FlowerValid;
import com.flower.admin.system.controller.UploadController;
import com.flower.common.enums.ResultEnum;
import com.flower.common.enums.StatusEnum;
import com.flower.common.utils.EntityBeanUtil;
import com.flower.common.utils.ResultVoUtil;
import com.flower.common.utils.SpringContextUtil;
import com.flower.common.utils.StatusUtil;
import com.flower.common.vo.ResultVo;
import com.flower.component.fileUpload.config.properties.UploadProjectProperties;
import com.flower.component.shiro.ShiroUtil;
import com.flower.modules.business.domain.Flower;
import com.flower.modules.business.domain.FlowerKind;
import com.flower.modules.business.service.FlowerService;
import com.flower.modules.system.domain.Upload;
import com.flower.modules.system.domain.User;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.FileCopyUtils;
import org.springframework.util.StringUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.List;

/**
 * @author cyy
 * @date 2021/01/17
 */
@Controller
@RequestMapping("/business/flower")
public class FlowerController {

    @Autowired
    private FlowerService flowerService;

    @RequestMapping("/httprequest")
    public String hello(){
        return "cyy hello";
    }
    /**
     * 列表页面
     */
    @GetMapping("/index")
    @RequiresPermissions("business:flower:index")
    public String index(Model model, Flower flower) {

        // 创建匹配器，进行动态查询匹配
        ExampleMatcher matcher = ExampleMatcher.matching()
                .withMatcher("flowerName", match -> match.contains())
                .withMatcher("alias", match -> match.contains())
                .withMatcher("cycle", match -> match.contains())
                .withMatcher("area", match -> match.contains());

        // 获取数据列表
        Example<Flower> example = Example.of(flower, matcher);
        Page<Flower> list = flowerService.getPageList(example);

        // 封装数据
        model.addAttribute("list", list.getContent());
        model.addAttribute("page", list);
        return "/business/flower/index";
    }

    /**
     * 跳转到添加页面
     */
    @GetMapping("/add")
    @RequiresPermissions("business:flower:add")
    public String toAdd() {
        return "/business/flower/add";
    }

    /**
     * 跳转到编辑页面
     */
    @GetMapping("/edit/{id}")
    @RequiresPermissions("business:flower:edit")
    public String toEdit(@PathVariable("id") Flower flower, Model model) {
        model.addAttribute("flower", flower);

        return "/business/flower/add";
    }


    /**
     * 保存添加/修改的数据
     * @param valid 验证对象
     */
    @PostMapping("/save")
    @RequiresPermissions({"business:flower:add", "business:flower:edit"})
    @ResponseBody
    public ResultVo save(@Validated FlowerValid valid, Flower flower) {
        // 复制保留无需修改的数据
        if (flower.getId() != null) {
            Flower beFlower = flowerService.getById(flower.getId());
            EntityBeanUtil.copyProperties(beFlower, flower);
        }

        // 保存数据
        flowerService.save(flower);
        return ResultVoUtil.SAVE_SUCCESS;
    }

    /**
     * 跳转到详细页面
     */
    @GetMapping("/detail/{id}")
    @RequiresPermissions("business:flower:detail")
    public String toDetail(@PathVariable("id") Flower flower, Model model) {
        model.addAttribute("flower",flower);
        return "/business/flower/detail";
    }

    /**
     * 设置一条或者多条数据的状态
     */
    @RequestMapping("/status/{param}")
    @RequiresPermissions("business:flower:status")
    @ResponseBody
    public ResultVo status(
            @PathVariable("param") String param,
            @RequestParam(value = "ids", required = false) List<Long> ids) {
        // 更新状态
        StatusEnum statusEnum = StatusUtil.getStatusEnum(param);
        if (flowerService.updateStatus(statusEnum, ids)) {
            return ResultVoUtil.success(statusEnum.getMessage() + "成功");
        } else {
            return ResultVoUtil.error(statusEnum.getMessage() + "失败，请重新操作");
        }
    }

    /**
     * 获取花卉圖片
     */
    @GetMapping("/picture")
    public void picture(String p, HttpServletResponse response) throws IOException {
        String defaultPath = "/images/user-picture.jpg";
        if (!(StringUtils.isEmpty(p) || p.equals(defaultPath))) {
            UploadProjectProperties properties = SpringContextUtil.getBean(UploadProjectProperties.class);
            String fuPath = properties.getFilePath();
            String spPath = properties.getStaticPath().replace("*", "");
            File file = new File(fuPath + p.replace(spPath, ""));
            if (file.exists()) {
                FileCopyUtils.copy(new FileInputStream(file), response.getOutputStream());
                return;
            }
        }
        Resource resource = new ClassPathResource("static" + defaultPath);
        FileCopyUtils.copy(resource.getInputStream(), response.getOutputStream());
    }



}