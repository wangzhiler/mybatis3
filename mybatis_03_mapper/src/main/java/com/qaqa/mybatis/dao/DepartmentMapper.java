package com.qaqa.mybatis.dao;

import com.qaqa.mybatis.bean.Department;

/**
 * Created by thinkpad on 2018/7/17.
 */
public interface DepartmentMapper {
    public Department getDeptById(Integer id);

    public Department getDeptByIdPlus(Integer id);

    public Department getDeptByIdStep(Integer id);
}
