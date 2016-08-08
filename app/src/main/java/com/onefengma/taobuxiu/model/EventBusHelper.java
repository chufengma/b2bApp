package com.onefengma.taobuxiu.model;

import com.onefengma.taobuxiu.utils.ThreadUtils;

import org.greenrobot.eventbus.EventBus;


/**
 * @author yfchu
 * @date 2016/3/21
 * @doc http://greenrobot.org/
 */
public class EventBusHelper {

    private static class EventBusHolder {
        private static EventBus eventBus = EventBus.builder()
                .eventInheritance(false)
                .installDefaultEventBus();
    }

    public static void register(Object subscriber) {
        if (subscriber == null || EventBusHolder.eventBus.isRegistered(subscriber)) {
            return;
        }
        EventBusHolder.eventBus.register(subscriber);
    }

    public static void unregister(Object subscriber) {
        if (subscriber == null) {
            return;
        }
        if (EventBusHolder.eventBus.isRegistered(subscriber)) {
            EventBusHolder.eventBus.unregister(subscriber);
        }
    }

    /**
     * 在UI线程发送事件
     */
    public static void postOnUiThread(final Object event) {
        if (event == null) {
            return;
        }
        ThreadUtils.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                post(event);
            }
        });
    }

    /**
     * EventBus 默认行为是事件接收线程和发送线程在同一线程。
     */
    public static void post(Object event) {
        if (event == null) {
            return;
        }
        EventBusHolder.eventBus.post(event);
    }

}
