package com.qaqa.mybatis.test;

import com.qaqa.mybatis.bean.Employee;
import com.qaqa.mybatis.dao.EmployeeMapper;
import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.junit.Test;

import java.io.IOException;
import java.io.InputStream;

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

    private SqlSessionFactory getSqlSessionFactory() throws IOException {
        String resource = "mybatis-config.xml";
        InputStream inputStream = Resources.getResourceAsStream(resource);
        return new SqlSessionFactoryBuilder().build(inputStream);
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
}
