package com.enernet.eg.building;

import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.enernet.eg.building.activity.ActivityAlarm;

public class ActionReceiver extends BroadcastReceiver implements com.enernet.eg.building.IaResultHandler {

    int m_nSeqPlanElem;

    @Override
    public void onReceive(Context context, Intent intent) {

        Log.i("ActionReceiver", "onReceive called...");

        String action=intent.getStringExtra("action");
        m_nSeqPlanElem=intent.getIntExtra("seq_plan_elem", 0);
        int nNotiId=intent.getIntExtra("NotiId", 0);

        if (action.equals("accept")) {
            performAccept(context);
        }
        else if (action.equals("reject")){
            performReject(context);
        }

        NotificationManager manager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        manager.cancel(nNotiId);

        //This is used to close the notification tray
        Intent it = new Intent(Intent.ACTION_CLOSE_SYSTEM_DIALOGS);
        context.sendBroadcast(it);

    }

    public void performAccept(Context context) {
        Log.i("ActionReceiver", "performAccept called...");

        //Intent it = new Intent(this, ActivityAlarm.class);
        //com.enernet.eg.building.CaApplication.m_Engine.ResponseAckMember(m_nSeqMemberAckRequester, 1, false, context, this);
    }

    public void performReject(Context context) {
        Log.i("ActionReceiver", "performReject called...");
        //com.enernet.eg.building.CaApplication.m_Engine.ResponseAckMember(m_nSeqMemberAckRequester, 2, false, context, this);
    }

    @Override
    public void onResult(com.enernet.eg.building.CaResult Result) {

        if (Result.object==null) {
            Log.i("ActionReceiver", "object is null");
            return;
        }

        switch (Result.m_nCallback) {

            default: {
                Log.i("ActionReceiver", "Unknown type result received : " + Result.m_nCallback);
            }
            break;

        } // end of switch
    }


}
