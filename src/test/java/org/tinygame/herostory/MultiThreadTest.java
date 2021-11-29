package org.tinygame.herostory;

import org.junit.Assert;
import org.junit.Test;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * 多线程减血测试
 * @author hugangquan
 * @date 2021/11/19 22:13
 */
public class MultiThreadTest {

    @Test
    public void test01(){

        /**
         * 多线程并发情况下操作同一个变量，会存在线程安全问题
         *
         * 原因：变量对于不同的线程来说不可见，当一个线程对变量执行了减法操作，该变量最新的值并不能被另一个线程立即看到
         */

        /*for(int i=0;i<10000;i++){
            System.out.println("循环:"+(i+1)+"次");
            testThread();
        }*/

    }

    @Test
    public void test02(){

        /**
         * 使用volatile保证变量在线程间的可见性，但不能保证原子性,因此两个线程可以通过对变量进行减法操作，
         * 当两个线程拿到100，尝试减10操作，然后把修改后的值90同步到主存，虽然可以被另一个线程立即看到最新的值，
         * 但是另一个线程也同样拿到100尝试减10操作，然后把修改后的值90同步到主存，所以最后结果是90
         */

      /*  for(int i=0;i<10000;i++){
            System.out.println("循环:"+(i+1)+"次");
            testThread();
        }*/

    }

    @Test
    public void test03(){

        /**
         *
         * 使用synchonized同步,可以解决减血同步问题
         */

       /* for(int i=0;i<100000;i++){
            System.out.println("循环:"+(i+1)+"次");
            testThread2();
        }
*/
    }

    @Test
    public void test04(){

        /**
         *
         * 使用AtomicInteger,可以解决多线程并发操作同一变量的问题
         */

        /*for(int i=0;i<100000;i++){
            System.out.println("循环:"+(i+1)+"次");
            testThread3();
        }*/

    }

    @Test
    public void test05(){

        /**
         *
         * 两个用户互砍使用synchonized同步,会发生死锁
         * 原因：user1先锁定自己执行attkUser，再锁定user2执行减血
         * user2先锁定自己执行attkUser，再锁定user1执行减血
         * 有可能出现user1持有锁，等待user2释放锁才能继续执行
         * user2持有锁，等待user1释放锁，造成相互无限等待
         */

       /* for(int i=0;i<100000;i++){
            System.out.println("循环:"+(i+1)+"次");
            testThread4();
        }*/

    }

    public void testThread4(){

        User2 user1 = new User2();
        User2 user2 = new User2();
        user1.setCurrentHp(100);
        user2.setCurrentHp(100);

        Thread[] threads = new Thread[2];
        threads[0] = new Thread(()->{
            user1.attkUser(user2);
        });
        threads[1] = new Thread(()->{
            user2.attkUser(user1);
        });

        threads[0].start();
        threads[1].start();

        try {
            threads[0].join();
            threads[1].join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void testThread3(){

        User1 user = new User1();
        user.setCurrentHp(new AtomicInteger(100));

        Thread[] threads = new Thread[2];
        threads[0] = new Thread(()->{
            user.getCurrentHp().addAndGet(-10);
        });
        threads[1] = new Thread(()->{
            user.getCurrentHp().addAndGet(-10);
        });

        threads[0].start();
        threads[1].start();

        try {
            threads[0].join();
            threads[1].join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        Assert.assertEquals(80,user.getCurrentHp().get());

    }

    public void testThread2(){

        User user = new User();
        user.setCurrentHp(100);

        Thread[] threads = new Thread[2];
        threads[0] = new Thread(()->{
            user.substractHp(10);
        });
        threads[1] = new Thread(()->{
            user.substractHp(10);
        });

        threads[0].start();
        threads[1].start();

        try {
            threads[0].join();
            threads[1].join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        Assert.assertEquals(80,user.getCurrentHp());

    }

    public void testThread(){

        User user = new User();
        user.setCurrentHp(100);

        Thread[] threads = new Thread[2];
        threads[0] = new Thread(()->{
            user.setCurrentHp(user.getCurrentHp()-10);
        });
        threads[1] = new Thread(()->{
            user.setCurrentHp(user.getCurrentHp()-10);
        });

        threads[0].start();
        threads[1].start();

        try {
            threads[0].join();
            threads[1].join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        Assert.assertEquals(80,user.getCurrentHp());

    }

}
