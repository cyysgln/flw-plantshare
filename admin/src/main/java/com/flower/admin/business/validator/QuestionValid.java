package com.flower.admin.business.validator;

import lombok.Data;
import org.hibernate.validator.constraints.URL;

import java.io.Serializable;
import javax.validation.constraints.NotEmpty;

/**
 * @author cyy
 * @date 2021/01/17
 */
@Data
public class QuestionValid implements Serializable {
    @NotEmpty(message = "问题描述不能为空")
    private String content;
    @NotEmpty(message = "图片1不能为空")
   // @URL(message = "URL地址格式不对")
    private String url1;
    @NotEmpty(message = "图片2不能为空")
  //  @URL(message = "URL地址格式不对")
    private String url2;
   // @URL(message = "URL地址格式不对")
    private String url3;
}