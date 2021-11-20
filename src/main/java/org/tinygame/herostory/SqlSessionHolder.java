package org.tinygame.herostory;

import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;

import java.io.IOException;

/**
 * @author hugangquan
 * @date 2021/11/20 22:36
 */
public class SqlSessionHolder {

    private static final SqlSessionHolder instance = new SqlSessionHolder();

    private static  SqlSessionFactory sqlSessionFactory;

    private SqlSessionHolder(){
    }

    public static SqlSessionHolder getInstance(){
        return instance;
    }


    public static void init(){
        try {
            SqlSessionFactoryBuilder sqlSessionFactoryBuilder = new SqlSessionFactoryBuilder();
            sqlSessionFactory = sqlSessionFactoryBuilder.build(Resources.getResourceAsStream("SqlMapConfig.xml"));

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public SqlSession openSession(){
        if(sqlSessionFactory == null){
            throw new RuntimeException("sqlSessionFactory未初始化成功");
        }

        //打开session，自动提交事务
        return sqlSessionFactory.openSession(true);
    }

}
