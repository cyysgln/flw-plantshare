package com.flower.modules.business.service.impl;

import com.flower.common.data.PageSort;
import com.flower.common.enums.StatusEnum;
import com.flower.modules.business.domain.Essay;
import com.flower.modules.business.repository.EssayRepository;
import com.flower.modules.business.service.EssayService;
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
public class EssayServiceImpl implements EssayService {

    @Autowired
    private EssayRepository essayRepository;

    /**
     * 根据ID查询数据
     * @param id 主键ID
     */
    @Override
    @Transactional
    public Essay getById(Long id) {
        return essayRepository.findById(id).orElse(null);
    }

    /**
     * 获取分页列表数据
     * @param example 查询实例
     * @return 返回分页数据
     */
    @Override
    public Page<Essay> getPageList(Example<Essay> example) {
        // 创建分页对象
        PageRequest page = PageSort.pageRequest();
        return essayRepository.findAll(example, page);
    }

    /**
     * 保存数据
     * @param essay 实体对象
     */
    @Override
    public Essay save(Essay essay) {
        return essayRepository.save(essay);
    }

    /**
     * 状态(启用，冻结，删除)/批量状态处理
     */
    @Override
    @Transactional
    public Boolean updateStatus(StatusEnum statusEnum, List<Long> idList) {
        return essayRepository.updateStatus(statusEnum.getCode(), idList) > 0;
    }
}