package com.zrulin.ftcommunity.util;

import com.zrulin.ftcommunity.pojo.User;
import org.springframework.stereotype.Component;

/**
 * 持有用户的信息，用于代替session对象的。如果是session的话，可以直接持有用户信息，并且是线程隔离的。
 * 这个是用这个类，体验隔离的实现。
 * @author zrulin
 * @create 2022-03-13 9:35
 */
@Component
public class HostHolder {
    private ThreadLocal<User> users = new ThreadLocal<>();
    /**ThreadLocal源码解析
     * set()方法，存值
     *     public void set(T value) {
     *         Thread t = Thread.currentThread();  获取当前线程。
     *         ThreadLocalMap map = getMap(t);    根据当前线程去获取一个map对象
     *         if (map != null)                   然后把值存到线程里面。
     *             map.set(this, value);          存是通过线程来存的，每个线程得到的map对象不一样。
     *         else
     *             createMap(t, value);
     *     }
     *
     * get()方法：取值
     *  public T get() {
     *         Thread t = Thread.currentThread();               获取当前线程
     *         ThreadLocalMap map = getMap(t);                  通过当前线程获取一个map对象。
     *         if (map != null) {                               然后再取值
     *             ThreadLocalMap.Entry e = map.getEntry(this);
     *             if (e != null) {
     *                 @SuppressWarnings("unchecked")
     *                 T result = (T)e.value;
     *                 return result;
     *             }
     *         }
     *         return setInitialValue();
     *     }
     */

    public void setUser(User user){
        users.set(user);
    }
    public User getUser(){
        return users.get();
    }
    public void clear(){
        users.remove();
    }
}
