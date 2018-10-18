package com.hdd.eventbusdemo;

/**
 * 事件是纯Java对象，没有其他的特殊要求
 */
public class MessageEvent {
    public final String message;

    public MessageEvent(String message) {
        this.message = message;
    }
}
