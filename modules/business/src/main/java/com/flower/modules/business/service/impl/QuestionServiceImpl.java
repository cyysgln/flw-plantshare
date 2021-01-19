package com.flower.modules.business.service.impl;

import com.flower.common.data.PageSort;
import com.flower.common.enums.StatusEnum;
import com.flower.modules.business.domain.Question;
import com.flower.modules.business.repository.QuestionRepository;
import com.flower.modules.business.service.QuestionService;
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
public class QuestionServiceImpl implements QuestionService {

    @Autowired
    private QuestionRepository questionRepository;

    /**
     * 根据ID查询数据
     * @param id 主键ID
     */
    @Override
    @Transactional
    public Question getById(Long id) {
        return questionRepository.findById(id).orElse(null);
    }

    /**
     * 获取分页列表数据
     * @param example 查询实例
     * @return 返回分页数据
     */
    @Override
    public Page<Question> getPageList(Example<Question> example) {
        // 创建分页对象
        PageRequest page = PageSort.pageRequest();
        return questionRepository.findAll(example, page);
    }

    /**
     * 保存数据
     * @param question 实体对象
     */
    @Override
    public Question save(Question question) {
        return questionRepository.save(question);
    }

    /**
     * 状态(启用，冻结，删除)/批量状态处理
     */
    @Override
    @Transactional
    public Boolean updateStatus(StatusEnum statusEnum, List<Long> idList) {
        return questionRepository.updateStatus(statusEnum.getCode(), idList) > 0;
    }
}