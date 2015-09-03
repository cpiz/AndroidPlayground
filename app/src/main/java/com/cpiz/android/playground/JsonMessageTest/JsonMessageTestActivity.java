package com.cpiz.android.playground.JsonMessageTest;

import android.os.Bundle;
import android.view.View;

import com.cpiz.android.playground.BaseTestActivity;
import com.cpiz.android.utils.RxBus;

import rx.functions.Action1;

/**
 * Created by caijw on 2015/9/1.
 */
public class JsonMessageTestActivity extends BaseTestActivity {
    private static final String TAG = "JsonMessageTestActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        RxBus.getDefault().register(OrderCreatedMsg.class)
                .subscribe(new Action1<OrderCreatedMsg>() {
                    @Override
                    public void call(OrderCreatedMsg orderCreatedMsg) {
                        String destJson = orderCreatedMsg.toJson();
                        appendLine(String.format("received a message[%s]", orderCreatedMsg.getClass().getSimpleName()));
                        appendLine(destJson);
                        appendLine();
                    }
                });
    }

    @Override
    public void onClick(View v) {
        clearOutput();

        final String srcJson = "{\"id\":\"ididididid\",\"state\":0,\"type\":1,\"isPersistent\":true,\"fromUid\":50013856,\"validity\":1440413154,\"createTime\":1440413154,\"payload\":{\"orderId\":\"ooooooorderiiiiid\",\"y\":456}}";
        Message msg = Message.fromJson(srcJson);
        RxBus.getDefault().post(msg);
        appendLine(String.format("post a message[%s]", msg.getClass().getSimpleName()));

        final int times = 1000;
        long startTime, stopTime;

        // serialize performance test
        startTime = System.currentTimeMillis();
        for (int i = 0; i < times; ++i) {
            Message.fromJson(srcJson);
        }
        stopTime = System.currentTimeMillis();
        appendLine(String.format("serialize %d messages in %dms", times, stopTime - startTime));

        // deserialize performance test
        startTime = System.currentTimeMillis();
        for (int i = 0; i < times; ++i) {
            msg.toJson();
        }
        stopTime = System.currentTimeMillis();
        appendLine(String.format("deserialize %d messages in %dms", times, stopTime - startTime));
    }
}