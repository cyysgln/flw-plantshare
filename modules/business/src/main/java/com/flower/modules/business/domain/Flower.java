package com.flower.modules.business.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.flower.common.enums.StatusEnum;
import com.flower.common.utils.StatusUtil;
import com.flower.modules.system.domain.User;
import lombok.Data;
import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;
import org.hibernate.annotations.Where;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.*;

/**
 * @author cyy
 * @date 2021/01/17
 */
@Data
@Entity
@Table(name="flw_flower")
@EntityListeners(AuditingEntityListener.class)
@Where(clause = StatusUtil.NOT_DELETE)
public class Flower implements Serializable {
    // 主键ID
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonIgnore
    private Long id;
    // 花卉名称
    private String flowerName;
    // 别名
    private String alias;
    // 花卉分类
    @ManyToOne(fetch=FetchType.LAZY)
    @NotFound(action=NotFoundAction.IGNORE)
    @JoinColumn(name="flower_type_id")
    @JsonIgnore
    private FlowerKind flowerTypeId;
    // 生长周期
    private String cycle;
    // 描述
    private String introduce;
    // 图片
    private String url;
    // 地区
    private String area;
    // 创建时间
    @CreatedDate
    private Date createDate;
    // 更新时间
    @LastModifiedDate
    private Date updateDate;
    // 创建者
    @CreatedBy
    @ManyToOne(fetch=FetchType.LAZY)
    @NotFound(action=NotFoundAction.IGNORE)
    @JoinColumn(name="create_by")
    @JsonIgnore
    private User createBy;
    // 更新者
    @LastModifiedBy
    @ManyToOne(fetch=FetchType.LAZY)
    @NotFound(action=NotFoundAction.IGNORE)
    @JoinColumn(name="update_by")
    @JsonIgnore
    private User updateBy;
    // 数据状态
    private Byte status = StatusEnum.OK.getCode();
}