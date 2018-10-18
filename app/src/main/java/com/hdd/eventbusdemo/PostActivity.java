package com.hdd.eventbusdemo;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import org.greenrobot.eventbus.EventBus;

/**
 * 发送EventBus事件
 */
public class PostActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //发送事件
        EventBus.getDefault().post(new UserEvent("Mr.sorrow", "123456"));

        //粘性事件
        EventBus.getDefault().postSticky(new UserEvent("粘性事件", "urgent"));
    }
}
