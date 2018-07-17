package com.qaqa.mybatis.bean;

import java.util.List;

/**
 * Created by thinkpad on 2018/7/16.
 */
public class Department {
    private Integer id;
    private String departmentName;

    private List<Employee> emps;

/*
create table tb1_dept{
id int(11) primary key auto_increment,
dept_name varchar(255)
}

alter table tb1_employee add column d_id int(11);

alter table tb1_employee add CONSTRAINT fk_emp_dept
FOREIGN key (d_id) REFERENCES tb1_dept(id)



*
* */

    @Override
    public String toString() {
        return "Department{" +
                "id=" + id +
                ", departmentName='" + departmentName + '\'' +
                '}';
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getDepartmentName() {
        return departmentName;
    }

    public void setDepartmentName(String departmentName) {
        this.departmentName = departmentName;
    }

    public List<Employee> getEmps() {
        return emps;
    }

    public void setEmps(List<Employee> emps) {
        this.emps = emps;
    }
}
