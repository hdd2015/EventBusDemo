package com.hdd.eventbusdemo;

/**
 * 事件是纯Java对象，没有其他的特殊要求
 */
public class UserEvent {
    public final String name;
    public final String password;

    public UserEvent(String name, String password) {
        this.name = name;
        this.password = password;
    }
}
