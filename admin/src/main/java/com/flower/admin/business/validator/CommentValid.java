package com.flower.admin.business.validator;

import lombok.Data;

import java.io.Serializable;
import javax.validation.constraints.NotNull;

/**
 * @author cyy
 * @date 2021/01/17
 */
@Data
public class CommentValid implements Serializable {
    @NotNull(message = "文章ID不能为空")
    private Long essayId;
}