package com.flower.admin.business.validator;

import lombok.Data;

import java.io.Serializable;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

/**
 * @author cyy
 * @date 2021/01/17
 */
@Data
public class FlowerValid implements Serializable {
    @NotEmpty(message = "花卉名称不能为空")
    private String flowerName;
    @NotNull(message = "花卉分类不能为空")
    private Long flowerKind;
}