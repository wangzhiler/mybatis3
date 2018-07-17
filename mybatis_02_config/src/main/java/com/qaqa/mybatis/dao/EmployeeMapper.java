package com.qaqa.mybatis.dao;

import com.qaqa.mybatis.bean.Employee;

/**
 * Created by thinkpad on 2018/7/13.
 */
public interface EmployeeMapper {

    //优点: 明确的传入参数类型，明确的返回对象
    public Employee getEmpById(Integer id);
}
