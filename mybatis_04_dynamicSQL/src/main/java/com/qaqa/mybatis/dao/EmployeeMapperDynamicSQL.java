package com.qaqa.mybatis.dao;

import com.qaqa.mybatis.bean.Employee;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * Created by thinkpad on 2018/7/18.
 */
public interface EmployeeMapperDynamicSQL {

    public List<Employee> getEmpsTestInnerParameter(Employee employee);


    //携带了哪个字段 查询条件就带上这个字段的值
    public List<Employee> getEmpsByConditionIf(Employee employee);

    public List<Employee> getEmpsByConditionTrim(Employee employee);

    public List<Employee> getEmpsByConditionChoose(Employee employee);

    public void updateEmp(Employee employee);

    public List<Employee> getEmpsByConditionForeach(@Param("ids") List<Integer> list);

    public void addEmps(@Param("emps") List<Employee> emps);
    public void addEmps2(@Param("emps2") List<Employee> emps);
    public void addEmps3(@Param("emps3") List<Employee> emps);

}


