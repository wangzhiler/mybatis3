package com.qaqa.mybatis.test;

import com.qaqa.mybatis.bean.Department;
import com.qaqa.mybatis.bean.Employee;
import com.qaqa.mybatis.dao.*;
import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.junit.Test;

import java.io.IOException;
import java.io.InputStream;
import java.util.*;

/**
 * 1. 接口式编程
 *  原生:     Dao  ====> DaoImpl
 *  mybatis: Mapper====>xxMapper.xml
 *
 * 2. SqlSession代表和数据库的一次会话: 用完必须关闭
 * 3. SqlSession和connection一样都是非线程安全。每次使用都应该获取新的对象,不能作为共享的成员变量
 * 4. mapper接口没有实现类,但是mybatis会为这个接口生成一个代理对象
 *  (将接口和xml进行绑定)
 *  EmployeeMapper empMapper = sqlSession.getMapper(EmployeeMapper.class);
 * 5. 两个重要的配置文件
 *  mybatis的全局配置文件: 包含数据库连接池信息,事务管理器信息等...系统运行环境信息
 *  sql映射文件: 保存了每一个sql语句的映射信息,将sql抽取出来
 *
 */
public class MyBatisTest {

    private SqlSessionFactory getSqlSessionFactory() throws IOException {
        String resource = "mybatis-config.xml";
        InputStream inputStream = Resources.getResourceAsStream(resource);
        return new SqlSessionFactoryBuilder().build(inputStream);
    }

    @Test
    public void testDynamicSQL() throws IOException {
        SqlSessionFactory sqlSessionFactory = getSqlSessionFactory();
        SqlSession openSession = sqlSessionFactory.openSession();

        try {
            EmployeeMapperDynamicSQL mapper = openSession.getMapper(EmployeeMapperDynamicSQL.class);
            //测试if/where
            Employee employee = new Employee(null, "%e%", "jerry@123.com", null);
            List<Employee> emps = mapper.getEmpsByConditionIf(employee);
            for (Employee emp : emps) {
                System.out.println(emp);
            }

            //查询的如果某些条件没带可能sql拼装可能会有问题
            //1. 给where后面加上1=1, 以后的条件都and xxx
            //2. mybatis使用where标签来讲所有的查询条件包括在内.mybatis就会将where标签中拼装的sql,多出来的and或者or去掉
                //where只会去掉第一个多出来的and或者or

            //测试trim
            List<Employee> emps2 = mapper.getEmpsByConditionTrim(employee);
            for (Employee emp : emps2) {
                System.out.println(emps2);
            }
        } finally {
            openSession.close();
        }
    }

    @Test
    public void testDynamicSQL_trim() throws IOException {
        SqlSessionFactory sqlSessionFactory = getSqlSessionFactory();
        SqlSession openSession = sqlSessionFactory.openSession();

        try {
            EmployeeMapperDynamicSQL mapper = openSession.getMapper(EmployeeMapperDynamicSQL.class);
            Employee employee = new Employee(null, "%e%", "jerry@123.com", null);
            //测试trim
            List<Employee> emps2 = mapper.getEmpsByConditionTrim(employee);
            for (Employee emp : emps2) {
                System.out.println(emps2);
            }
        } finally {
            openSession.close();
        }
    }

    @Test
    public void testDynamicSQL_choose() throws IOException {
        SqlSessionFactory sqlSessionFactory = getSqlSessionFactory();
        SqlSession openSession = sqlSessionFactory.openSession();

        try {
            EmployeeMapperDynamicSQL mapper = openSession.getMapper(EmployeeMapperDynamicSQL.class);
//            Employee employee = new Employee(1, "%a%", "null", null);
            Employee employee = new Employee(null, null, null, null);
            //测试choose

            List<Employee> emps = mapper.getEmpsByConditionChoose(employee);
            for (Employee emp : emps) {
                System.out.println(emp);
            }
        } finally {
            openSession.close();
        }
    }


    @Test
    public void testDynamicSQL_update() throws IOException {
        SqlSessionFactory sqlSessionFactory = getSqlSessionFactory();
        SqlSession openSession = sqlSessionFactory.openSession();

        try {
            EmployeeMapperDynamicSQL mapper = openSession.getMapper(EmployeeMapperDynamicSQL.class);
            Employee employee = new Employee(1, "Admin", "admin@123.com", null);
            //测试set标签

            mapper.updateEmp(employee);
            openSession.commit();
        } finally {
            openSession.close();
        }
    }

    @Test
    public void testDynamicSQL_foreach() throws IOException {
        SqlSessionFactory sqlSessionFactory = getSqlSessionFactory();
        SqlSession openSession = sqlSessionFactory.openSession();

        try {
            EmployeeMapperDynamicSQL mapper = openSession.getMapper(EmployeeMapperDynamicSQL.class);
            Employee employee = new Employee(1, "Admin", "admin@123.com", null);
            //测试set标签

            List<Employee> list = mapper.getEmpsByConditionForeach(Arrays.asList(1, 2));
            for (Employee emp : list) {
                System.out.println(emp);
            }

            openSession.commit();
        } finally {
            openSession.close();
        }
    }

    @Test
    public void testDynamicSQL_batchsave() throws IOException {
        SqlSessionFactory sqlSessionFactory = getSqlSessionFactory();
        SqlSession openSession = sqlSessionFactory.openSession();

        try {
            EmployeeMapperDynamicSQL mapper = openSession.getMapper(EmployeeMapperDynamicSQL.class);

            List<Employee> emps = new ArrayList<Employee>();
            emps.add(new Employee(null, "smith", "smith@123.com", "0", new Department(1)));
            emps.add(new Employee(null, "ellen", "ellen@123.com", "1", new Department(1)));
            mapper.addEmps2(emps);

            openSession.commit();
        } finally {
            openSession.close();
        }
    }


    @Test
    public void testInnerParam() throws IOException {
        SqlSessionFactory sqlSessionFactory = getSqlSessionFactory();
        SqlSession openSession = sqlSessionFactory.openSession();

        try {
            EmployeeMapperDynamicSQL mapper = openSession.getMapper(EmployeeMapperDynamicSQL.class);
            Employee employee2 = new Employee();
//            employee2.setLastName("%e%");
            employee2.setLastName("e");

            List<Employee> list = mapper.getEmpsTestInnerParameter(employee2);
            for (Employee employee : list) {
                System.out.println(employee);
            }

            openSession.commit();
        } finally {
            openSession.close();
        }
    }

//    addEmps


    /**
     * 1. 根据xml配置文件(全局配置文件)创建一个SqlSessionFactory对象,有数据源一些运行环境信息
     * 2. sql映射文件: 配置了每一个sql,以及sql的封装规则等
     * 3. 将sql映射文件注册在全局配置文件中
     * 4. 写代码:
     *  1) 根据全局配置文件得到SqlSessionFactory
     *  2) 使用sqlSession工厂,获取到sqlSession对象使用他来执行增删改查
     *      一个sqlSession就是代表和数据库的一次会话,用完关闭
     *  3) 使用sql的唯一标识来告诉MyBatis执行哪个sql sql都保存在映射文件中
     *
     * @throws IOException
     */
    @Test
    public void test() throws IOException {
        SqlSessionFactory sqlSessionFactory = getSqlSessionFactory();

        //2. 获取sqlSession实例,能直接执行已经映射的sql语句
        //sql唯一标识: statement Unique identifier matching the statement to use
        //执行sql要用的参数: parameter A parameter object to pass to the statement
        SqlSession openSession = sqlSessionFactory.openSession();
        try{
            Employee employee = openSession.selectOne("selectEmp", 1);
            System.out.println(employee);
        }finally {
            openSession.close();
        }
    }

    @Test
    public void test01() throws IOException {
        //1. 获取sqlSessionFactory对象
        SqlSessionFactory sqlSessionFactory = getSqlSessionFactory();

        //2. 获取sqlSession对象
        SqlSession openSession = sqlSessionFactory.openSession();

        try{
            //3. 获取接口的实现类对象
            //会为接口自动创建一个代理对象,代理对象去执行增删改查方法
            EmployeeMapper mapper = openSession.getMapper(EmployeeMapper.class);
            Employee employee = mapper.getEmpById(1);
            System.out.println(mapper.getClass()); //$Proxy4
            System.out.println(employee);
        }finally {
            openSession.close();
        }
    }

    @Test
    public void test02() throws IOException {
        SqlSessionFactory sqlSessionFactory = getSqlSessionFactory();
        SqlSession openSession = sqlSessionFactory.openSession();

        try {
            EmployeeMapperAnnotation mapper = openSession.getMapper(EmployeeMapperAnnotation.class);
            Employee empById = mapper.getEmpById(1);
            System.out.println(empById);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            openSession.close();
        }
    }

    /*
    * 测试crud
    * 1. mybatis允许增删改查直接定义一下类型返回值
    *       Integer, Long, Boolean
    * 2. 我们需要提交数据
    *       sqlSessionFactory.openSession(); ==>手动提交
    *       sqlSessionFactory.openSession(true); ==>自动提交
    */
    @Test
    public void test03() throws IOException {
        SqlSessionFactory sqlSessionFactory = getSqlSessionFactory();
        //1. 获取到的SqlSession不会自动提交数据
        SqlSession openSession = sqlSessionFactory.openSession();

        try{
            EmployeeMapper mapper = openSession.getMapper(EmployeeMapper.class);
            //测试添加
            Employee employee = new Employee("paul2", null, "1");
            mapper.addEmp(employee);
            System.out.println(employee.getId());

            //测试修改
//            Employee employee = new Employee(1, "qqq", "qqq@123.com", "0");
//            boolean updateEmp = mapper.updateEmp(employee);
//            System.out.println(updateEmp);

            //测试删除
//            mapper.deleteEmpById(3);

            //2. 手动提交数据
            openSession.commit();
        }finally {
            openSession.close();
        }
    }


    @Test
    public void test04() throws IOException {
        SqlSessionFactory sqlSessionFactory = getSqlSessionFactory();
        //1. 获取到的SqlSession不会自动提交数据
        SqlSession openSession = sqlSessionFactory.openSession();
        try{
            EmployeeMapper mapper = openSession.getMapper(EmployeeMapper.class);
            Employee employee = mapper.getEmpByIdAndLastName(1, "aaaaa1");
            System.out.println(employee);

            //2. 手动提交数据
            openSession.commit();
        }finally {
            openSession.close();
        }
    }

    @Test
    public void test04_map() throws IOException {
        SqlSessionFactory sqlSessionFactory = getSqlSessionFactory();
        //1. 获取到的SqlSession不会自动提交数据
        SqlSession openSession = sqlSessionFactory.openSession();
        try{
            EmployeeMapper mapper = openSession.getMapper(EmployeeMapper.class);
            Map<String, Object> map = new HashMap<String, Object>();
            map.put("id", 1);
            map.put("lastName", "aaaaa1");
            map.put("tableName", "USERS");
            Employee employee = mapper.getEmpByMap(map);

            System.out.println(employee);

            //2. 手动提交数据
            openSession.commit();
        }finally {
            openSession.close();
        }
    }

    @Test
    public void test04_list() throws IOException {
        SqlSessionFactory sqlSessionFactory = getSqlSessionFactory();
        //1. 获取到的SqlSession不会自动提交数据
        SqlSession openSession = sqlSessionFactory.openSession();
        try{
            EmployeeMapper mapper = openSession.getMapper(EmployeeMapper.class);
            List<Employee> like = mapper.getEmpsByLastNameLike("%a%");
            for (Employee employee : like) {
                System.out.println(employee);
            }

//            Map<String, Object> map = mapper.getEmpByIdReturnMap(1);
//            System.out.println(map);

            Map<Integer, Employee> map = mapper.getEmpByLastNameLikeReturnMap("%a%");
            System.out.println(map);

            //2. 手动提交数据
            openSession.commit();
        }finally {
            openSession.close();
        }
    }



    @Test
    public void test05() throws IOException {
        SqlSessionFactory sqlSessionFactory = getSqlSessionFactory();
        //1. 获取到的SqlSession不会自动提交数据
        SqlSession openSession = sqlSessionFactory.openSession();
        try{
            EmployeeMapperPlus mapper = openSession.getMapper(EmployeeMapperPlus.class);
//            Employee empById = mapper.getEmpById(1);
//            System.out.println(empById);

            Employee empAndDept = mapper.getEmpAndDept(2);
            System.out.println(empAndDept);
            System.out.println(empAndDept.getDept());

            //2. 手动提交数据
            openSession.commit();
        }finally {
            openSession.close();
        }
    }

    @Test
    public void test05_step() throws IOException {
        SqlSessionFactory sqlSessionFactory = getSqlSessionFactory();
        //1. 获取到的SqlSession不会自动提交数据
        SqlSession openSession = sqlSessionFactory.openSession();
        try{
            EmployeeMapperPlus mapper = openSession.getMapper(EmployeeMapperPlus.class);

            Employee empByIdStep = mapper.getEmpByIdStep(1);
            System.out.println(empByIdStep);
            System.out.println(empByIdStep.getDept());

            //2. 手动提交数据
            openSession.commit();
        }finally {
            openSession.close();
        }
    }

    @Test
    public void test06() throws IOException {
        SqlSessionFactory sqlSessionFactory = getSqlSessionFactory();
        SqlSession openSession = sqlSessionFactory.openSession();

        try{
            DepartmentMapper mapper = openSession.getMapper(DepartmentMapper.class);
//            Department deptByIdPlus = mapper.getDeptByIdPlus(1);
            Department deptByIdStep = mapper.getDeptByIdStep(1);

            System.out.println(deptByIdStep);
            System.out.println(deptByIdStep.getEmps());

        }finally {
            openSession.close();
        }
    }

}






