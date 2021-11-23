package org.tinygame.herostory;

/**
 * 异步操作
 * @author hugangquan
 * @date 2021/11/23 22:27
 */
@FunctionalInterface
public interface AsyncOperation {

    /**
     * 异步处理
     */
    void doAsync();

    /**
     * 进行异步执行之后的处理
     */
    default void doFinish(){

    }

}
