package org.tinygame.herostory.model;

import java.util.Collection;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 用户管理
 * @author hugangquan
 * @date 2021/11/09 22:22
 */
public class UserManager {

    private UserManager(){}

    private static final Map<Integer, User> userMap = new ConcurrentHashMap<Integer, User>();

    public static void addUser(User user){
        if(user == null){
            return;
        }
        userMap.put(user.getId(),user);
    }

    public static void removeUser(Integer userId){
        if(null == null){
            return;
        }
        userMap.remove(userId);
    }

    public static Collection<User> userList(){
        return userMap.values();
    }

}
