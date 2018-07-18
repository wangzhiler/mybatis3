package com.qaqa.mybatis.dao;

import com.qaqa.mybatis.bean.Employee;
import org.apache.ibatis.annotations.MapKey;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * Created by thinkpad on 2018/7/13.
 */
public interface EmployeeMapper {

    //多条记录封装一个map; Map<Integer, Employee>: 键是这条记录的主键,值时记录封装后的javaBean
    //告诉mybatis封装这个map的时候使用哪个属性作为map的key
    @MapKey("id")
    public Map<Integer, Employee> getEmpByLastNameLikeReturnMap(String lastName);

    //返回一条记录的map: key就是列名,值就是对应的值
    public Map<String, Object> getEmpByIdReturnMap(Integer id);

    public List<Employee> getEmpsByLastNameLike(String lastName);

    public Employee getEmpByMap(Map<String, Object> map);

    public Employee getEmpByIdAndLastName(@Param("id") Integer id, @Param("lastName") String lastName);

    //优点: 明确的传入参数类型，明确的返回对象
    public Employee getEmpById(Integer id);

    public void addEmp(Employee employee);

    public boolean updateEmp(Employee employee);

    public void deleteEmpById(Integer id);

}
