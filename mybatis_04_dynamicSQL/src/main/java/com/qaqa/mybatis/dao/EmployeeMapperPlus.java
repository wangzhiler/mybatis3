package com.qaqa.mybatis.dao;

import com.qaqa.mybatis.bean.Employee;

import java.util.List;

/**
 * Created by thinkpad on 2018/7/15.
 */
public interface EmployeeMapperPlus {

    public Employee getEmpById(Integer id);

    public Employee getEmpAndDept(Integer id);

    public Employee getEmpByIdStep(Integer id);

    public List<Employee> getEmpsByDeptId(Integer deptId);

    public Employee getEmpByDeptIdDis(Integer deptId);



}
