package com.qaqa.mybatis.dao;

import com.qaqa.mybatis.bean.Employee;
import org.apache.ibatis.annotations.Select;

/**
 * Created by thinkpad on 2018/7/14.
 */
public interface EmployeeMapperAnnotation {

    @Select("select * from tb1_employee where id = #{id}")
    public Employee getEmpById(Integer id);
}
