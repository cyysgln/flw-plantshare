package com.flower.modules.business.service;

import com.flower.common.enums.StatusEnum;
import com.flower.modules.business.domain.EssayCategory;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @author cyy
 * @date 2021/01/17
 */
public interface EssayCategoryService {

    /**
     * 获取分页列表数据
     * @param example 查询实例
     * @return 返回分页数据
     */
    Page<EssayCategory> getPageList(Example<EssayCategory> example);

    /**
     * 根据ID查询数据
     * @param id 主键ID
     */
    EssayCategory getById(Long id);

    /**
     * 保存数据
     * @param essayCategory 实体对象
     */
    EssayCategory save(EssayCategory essayCategory);

    /**
     * 状态(启用，冻结，删除)/批量状态处理
     */
    @Transactional
    Boolean updateStatus(StatusEnum statusEnum, List<Long> idList);
}