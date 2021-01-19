package com.flower.modules.system.service;

import com.flower.common.enums.StatusEnum;
import com.flower.modules.system.domain.Order1;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @author 小懒虫
 * @date 2021/01/12
 */
public interface Order1Service {

    /**
     * 获取分页列表数据
     * @param example 查询实例
     * @return 返回分页数据
     */
    Page<Order1> getPageList(Example<Order1> example);

    /**
     * 根据ID查询数据
     * @param id 主键ID
     */
    Order1 getById(Long id);

    /**
     * 保存数据
     * @param order1 实体对象
     */
    Order1 save(Order1 order1);

    /**
     * 状态(启用，冻结，删除)/批量状态处理
     */
    @Transactional
    Boolean updateStatus(StatusEnum statusEnum, List<Long> idList);
}