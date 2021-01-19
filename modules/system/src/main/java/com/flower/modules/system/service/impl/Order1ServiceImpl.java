package com.flower.modules.system.service.impl;

import com.flower.common.data.PageSort;
import com.flower.common.enums.StatusEnum;
import com.flower.modules.system.domain.Order1;
import com.flower.modules.system.repository.Order1Repository;
import com.flower.modules.system.service.Order1Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @author 小懒虫
 * @date 2021/01/12
 */
@Service
public class Order1ServiceImpl implements Order1Service {

    @Autowired
    private Order1Repository order1Repository;

    /**
     * 根据ID查询数据
     * @param id 主键ID
     */
    @Override
    @Transactional
    public Order1 getById(Long id) {
        return order1Repository.findById(id).orElse(null);
    }

    /**
     * 获取分页列表数据
     * @param example 查询实例
     * @return 返回分页数据
     */
    @Override
    public Page<Order1> getPageList(Example<Order1> example) {
        // 创建分页对象
        PageRequest page = PageSort.pageRequest();
        return order1Repository.findAll(example, page);
    }

    /**
     * 保存数据
     * @param order1 实体对象
     */
    @Override
    public Order1 save(Order1 order1) {
        return order1Repository.save(order1);
    }

    /**
     * 状态(启用，冻结，删除)/批量状态处理
     */
    @Override
    @Transactional
    public Boolean updateStatus(StatusEnum statusEnum, List<Long> idList) {
        return order1Repository.updateStatus(statusEnum.getCode(), idList) > 0;
    }
}