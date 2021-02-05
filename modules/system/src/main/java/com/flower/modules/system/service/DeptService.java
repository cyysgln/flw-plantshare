package com.flower.modules.system.service;

import com.flower.common.enums.StatusEnum;
import com.flower.modules.system.domain.Dept;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Sort;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @author 小懒虫
 * @date 2018/12/02
 */
public interface DeptService {

    /**
     * 获取角色列表数据
     * @param example 查询实例
     * @param sort 排序对象
     * @return 角色列表
     */
    List<Dept> getListByExample(Example<Dept> example, Sort sort);

    /**
     * 获取排序最大值
     * @param pid 父菜单ID
     * @return 最大值
     */
    Integer getSortMax(Long pid);

    /**
     * 根据父级角色ID获取本级全部角色
     * @param pid 父角色ID
     * @param notId 需要排除的角色ID
     * @return 角色列表
     */
    List<Dept> getListByPid(Long pid, Long notId);

    /**
     * 保存多个角色
     * @param deptList 角色实体类列表
     * @return 角色列表
     */
    List<Dept> save(List<Dept> deptList);

    /**
     * 根据角色管理ID查询角色管理数据
     * @param id 角色管理ID
     * @return 角色信息
     */
    Dept getById(Long id);

    /**
     * 根据ID查找子孙角色
     * @param id [id]形式
     * @return 角色列表
     */
    List<Dept> getListByPidLikeOk(Long id);

    /**
     * 保存角色管理
     * @param dept 角色管理实体类
     * @return 角色信息
     */
    Dept save(Dept dept);

    /**
     * 状态(启用，冻结，删除)/批量状态处理
     * @param statusEnum 数据状态
     * @param idList 数据ID列表
     * @return 操作结果
     */
    @Transactional
    Boolean updateStatus(StatusEnum statusEnum, List<Long> idList);
}

