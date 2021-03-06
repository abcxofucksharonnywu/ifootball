package com.abcxo.android.push;

import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.umeng.commonsdk.UMConfigure;
import com.umeng.message.IUmengRegisterCallback;
import com.umeng.message.PushAgent;
import com.umeng.message.UmengMessageHandler;
import com.umeng.message.UmengNotificationClickHandler;
import com.umeng.message.entity.UMessage;

import org.json.JSONObject;


/**
 * Created by shadow on 15/12/6.
 */
public class PushUtil {
    private static String messageId;
    public final static String key="565465fce0f55a849e003de9";
    public final static String secret="4f1fdf9b34b64b58ffb14925e56bf9e9";
    public static void enable(Context context) {
        UMConfigure.init(context, key, "Umeng", UMConfigure.DEVICE_TYPE_PHONE, secret);
        PushAgent mPushAgent = PushAgent.getInstance(context);
        UmengMessageHandler messageHandler = new UmengMessageHandler() {
            @Override
            public void dealWithNotificationMessage(Context context, UMessage uMessage) {
                super.dealWithNotificationMessage(context, uMessage);
                if (!uMessage.message_id.equals(messageId)) {//为了排除推送两次的bug
                    messageId = uMessage.message_id;
                    Intent intent = new Intent(PushConstants.ACTION_PUSH);
                    JSONObject object = new JSONObject(uMessage.extra);
                    Log.d("PushUtil","message::"+object.toString());
                    intent.putExtra(PushConstants.ACTION_PUSH_OBJECT, object.toString());
                    context.sendBroadcast(intent);
                }

            }
        };
        UmengNotificationClickHandler notificationClickHandler = new UmengNotificationClickHandler() {
            @Override
            public void launchApp(Context context, UMessage uMessage) {
//                super.launchApp(context, uMessage);
                Intent intent = new Intent(PushConstants.ACTION_PUSH_CLICK);
                JSONObject object = new JSONObject(uMessage.extra);
                Log.d("PushUtil","notification::"+object.toString());
                intent.putExtra(PushConstants.ACTION_PUSH_OBJECT, object.toString());
                context.sendBroadcast(intent);
            }

        };
        mPushAgent.setNotificationClickHandler(notificationClickHandler);
        mPushAgent.setMessageHandler(messageHandler);
        mPushAgent.register(new IUmengRegisterCallback() {

            @Override
            public void onSuccess(String deviceToken) {
                //注册成功会返回device token
                Log.d("PushUtil","device:"+deviceToken);
            }

            @Override
            public void onFailure(String s, String s1) {
                Log.d("PushUtil","error:"+s);
            }
        });

    }

}
