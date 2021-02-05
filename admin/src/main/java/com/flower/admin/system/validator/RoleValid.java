package com.flower.admin.system.validator;

import lombok.Data;

import javax.validation.constraints.NotEmpty;
import java.io.Serializable;

/**
 * @author 小懒虫
 * @date 2018/8/14
 */
@Data
public class RoleValid implements Serializable {
//    @NotEmpty(message = "角色编号不能为空")
//    private String id;
    @NotEmpty(message = "角色名称不能为空")
    private String title;
}
