package com.flower.admin.business.validator;

import lombok.Data;

import java.io.Serializable;
import javax.validation.constraints.NotEmpty;

/**
 * @author cyy
 * @date 2021/01/17
 */
@Data
public class FlowerKindValid implements Serializable {
    @NotEmpty(message = "种类名称不能为空")
    private String kindName;
}