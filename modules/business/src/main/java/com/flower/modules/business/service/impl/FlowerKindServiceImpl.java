package com.flower.modules.business.service.impl;

import com.flower.common.data.PageSort;
import com.flower.common.enums.StatusEnum;
import com.flower.modules.business.domain.FlowerKind;
import com.flower.modules.business.repository.FlowerKindRepository;
import com.flower.modules.business.service.FlowerKindService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @author cyy
 * @date 2021/01/17
 */
@Service
public class FlowerKindServiceImpl implements FlowerKindService {

    @Autowired
    private FlowerKindRepository flowerKindRepository;

    /**
     * 根据ID查询数据
     * @param id 主键ID
     */
    @Override
    @Transactional
    public FlowerKind getById(Long id) {
        return flowerKindRepository.findById(id).orElse(null);
    }

    /**
     * 获取分页列表数据
     * @param example 查询实例
     * @return 返回分页数据
     */
    @Override
    public Page<FlowerKind> getPageList(Example<FlowerKind> example) {
        // 创建分页对象
        PageRequest page = PageSort.pageRequest();
        return flowerKindRepository.findAll(example, page);
    }

    /**
     * 保存数据
     * @param flowerKind 实体对象
     */
    @Override
    public FlowerKind save(FlowerKind flowerKind) {
        return flowerKindRepository.save(flowerKind);
    }

    /**
     * 状态(启用，冻结，删除)/批量状态处理
     */
    @Override
    @Transactional
    public Boolean updateStatus(StatusEnum statusEnum, List<Long> idList) {
        return flowerKindRepository.updateStatus(statusEnum.getCode(), idList) > 0;
    }
}