package com.flower.modules.business.service.impl;

import com.flower.common.data.PageSort;
import com.flower.common.enums.StatusEnum;
import com.flower.modules.business.domain.EssayCategory;
import com.flower.modules.business.repository.EssayCategoryRepository;
import com.flower.modules.business.service.EssayCategoryService;
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
public class EssayCategoryServiceImpl implements EssayCategoryService {

    @Autowired
    private EssayCategoryRepository essayCategoryRepository;

    /**
     * 根据ID查询数据
     * @param id 主键ID
     */
    @Override
    @Transactional
    public EssayCategory getById(Long id) {
        return essayCategoryRepository.findById(id).orElse(null);
    }

    /**
     * 获取分页列表数据
     * @param example 查询实例
     * @return 返回分页数据
     */
    @Override
    public Page<EssayCategory> getPageList(Example<EssayCategory> example) {
        // 创建分页对象
        PageRequest page = PageSort.pageRequest();
        return essayCategoryRepository.findAll(example, page);
    }

    /**
     * 保存数据
     * @param essayCategory 实体对象
     */
    @Override
    public EssayCategory save(EssayCategory essayCategory) {
        return essayCategoryRepository.save(essayCategory);
    }

    /**
     * 状态(启用，冻结，删除)/批量状态处理
     */
    @Override
    @Transactional
    public Boolean updateStatus(StatusEnum statusEnum, List<Long> idList) {
        return essayCategoryRepository.updateStatus(statusEnum.getCode(), idList) > 0;
    }
}