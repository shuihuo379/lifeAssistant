package com.itheima.getui;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;

import com.igexin.sdk.PushConsts;
import com.igexin.sdk.PushManager;
import com.itheima.life.MsgNotificationActivity;

public class GeituiReceiver extends BroadcastReceiver {
    public static StringBuilder payloadData = new StringBuilder();

    @Override
    public void onReceive(Context context, Intent intent) {
        Bundle bundle = intent.getExtras();
        Log.i("test", "onReceive() action=" + bundle.getInt("action"));

        switch (bundle.getInt(PushConsts.CMD_ACTION)) {
            case PushConsts.GET_MSG_DATA:
                // 获取透传数据
                // String appid = bundle.getString("appid");
                byte[] payload = bundle.getByteArray("payload");

                String taskid = bundle.getString("taskid");
                String messageid = bundle.getString("messageid");

                // smartPush第三方回执调用接口，actionid范围为90000-90999，可根据业务场景执行
                boolean result = PushManager.getInstance().sendFeedbackMessage(context, taskid, messageid, 90001);
                System.out.println("第三方回执接口调用" + (result ? "成功" : "失败"));

                if (payload != null) {
                    String data = new String(payload);
                    Log.i("test", "receiver payload : " + data);
                    payloadData.append(data);
                    payloadData.append("\n");
                    //改动之处
                    if(MsgNotificationActivity.tv_content !=null){
                    	MsgNotificationActivity.tv_content.setText(data);
                    }
                }
                break;
            case PushConsts.GET_CLIENTID:
                // 获取ClientID(CID)
                // 第三方应用需要将CID上传到第三方服务器，并且将当前用户帐号和CID进行关联，以便日后通过用户帐号查找CID进行消息推送
                String cid = bundle.getString("clientid");
                Log.i("test","clientId=====>"+cid);
                //增加(改动的代码)
                SharedPreferences sp = context.getSharedPreferences("myPush",context.MODE_PRIVATE);
                sp.edit().putString("clientId",cid).commit();
                break;
            default:
                break;
        }
    }
}
