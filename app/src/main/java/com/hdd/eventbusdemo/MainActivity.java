package com.hdd.eventbusdemo;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

/**
 * https://blog.csdn.net/lmj623565791/article/details/40794879
 */
public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        /**
         * 注册订阅者
         * 使用EventBus.getDefault()是一种从应用程序的任何位置获取共享EventBus实例的简单方法。
         */
        /**
         * 让EventBus扫描当前类，把所有onEvent开头的方法记录下来，如何记录呢？
         * 使用Map，Key为方法的参数类型，Value中包含我们的方法。
         * 这样在onCreate执行完成以后，我们的onEventMainThread就已经以键值对的方式被存储到EventBus中了
         * (键为Item.class(post中实参的类型) ，值为包含该方法的对象)
         *
         * 调用EventBus.getDefault().post()时，
         * EventBus会根据post中实参的类型，去Map中查找对应的方法，于是找到了我们的onEventMainThread
         * （参数中的实例来决定调用哪个onEvent）
         */
        EventBus.getDefault().register(this);
    }

    /**
     * 订阅方法:用@Subscribe注释定义(使用EventBus 3可以自由选择方法名,没有像EventBus 2中的命名约定)
     *
     * 接收消息：可以有四种线程模型选择
     * ThreadMode: POSTING 订阅者将会在事件发布的线程中被调用。这是默认模式
     *
     * ThreadMode: MAIN 订阅者将在Android的主线程（有时称为UI线程）中调用。
     *
     * ThreadMode: BACKGROUND 如果在非UI线程发布的事件，则直接执行，和发布在同一个线程中。
     * 如果在UI线程发布的事件，则加入后台任务队列，使用线程池一个接一个调用。
     *
     * ThreadMode: ASYNC 加入后台任务队列，使用线程池调用，注意没有BackgroundThread中的一个接一个。
     */
    // This method will be called when a MessageEvent is posted (in the UI thread for Toast)
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(MessageEvent event) {
        Toast.makeText(this, event.message, Toast.LENGTH_SHORT).show();
    }

    // This method will be called when a SomeOtherEvent is posted
    @Subscribe
    public void handleSomethingElse(UserEvent event) {
//        doSomethingWith(event);
    }

    //-------------------------------粘性事件----------------------------

    /**
     *之前说的使用方法，都是需要先注册(register)，再post,才能接受到事件；
     * 如果你使用postSticky发送事件，那么可以【不需要先注册，要接收时开始注册】，也能接受到事件
     * 发送事件之后再订阅该事件也能收到该事件
     *
     * 通过 postSticky 发送粘性事件，这个事件不会只被消费一次就消失，
     * 而是一直存在系统中，直到被 removeStickyEvent 删除掉
     * （EventBus会存储所有的Sticky事件，如果某个事件在不需要再存储则需要手动进行移除）
     *
     * 那么只要订阅了该粘性事件的方法，只要被register 的时候，就会被检测到，并且执行。
     * 订阅的方法需要添加 sticky = true 属性。
     */

    /**
     * 应用场景：
     * 发布者发了消息,但订阅者还未产生（主界面还未创建,用于接收的EventBus还未注册）
     * 把一个Event发送到一个还没有初始化的Activity/Fragment，即尚未订阅事件
     */

    /**
     * 接收消息：和之前的方法一样，只是多了一个 sticky = true 的属性。
     */
    @Subscribe(threadMode = ThreadMode.MAIN, sticky = true)
    public void onEvent(MessageEvent event) {
//        mTvReceive.setText(messageEvent.toString());
        EventBus.getDefault().removeStickyEvent(event);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //解注册
        EventBus.getDefault().removeAllStickyEvents();
        EventBus.getDefault().unregister(this);
    }
}
