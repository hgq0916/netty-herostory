package org.tinygame.herostory;

import org.junit.Test;
import org.tinygame.herostory.cmdHandler.ICmdHandler;
import org.tinygame.herostory.util.PackageUtil;

import java.io.File;
import java.util.Set;

/**
 * @author hugangquan
 * @date 2021/11/29 22:22
 */
public class JarClassLoadTest {


    @Test
    public void test(){

        /*File file = new File("H:\\herostory\\target\\classes\\org\\tinygame\\herostory\\cmdHandler\\herostory.jar");

        System.out.println(file.getName());

        Set<Class<?>> classes = PackageUtil.listClazzFromJar(file, "org.tinygame.herostory.cmdHandler", true, (clazz) -> {
            return ICmdHandler.class.isAssignableFrom(clazz);
        });*/

    }

}
