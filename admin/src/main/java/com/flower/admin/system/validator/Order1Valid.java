package com.flower.admin.system.validator;

import lombok.Data;

import java.io.Serializable;
import javax.validation.constraints.NotEmpty;

/**
 * @author 小懒虫
 * @date 2021/01/12
 */
@Data
public class Order1Valid implements Serializable {
    @NotEmpty(message = "标题不能为空")
    private String title;
}