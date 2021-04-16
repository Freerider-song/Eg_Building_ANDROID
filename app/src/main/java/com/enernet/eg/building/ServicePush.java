package com.enernet.eg.building;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.IBinder;
import android.util.Log;

import androidx.core.app.NotificationCompat;

import com.enernet.eg.building.activity.ActivityAlarm;
import com.enernet.eg.building.activity.ActivityAlarmList;
import com.enernet.eg.building.activity.ActivityHome;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import java.util.Map;

public class ServicePush extends FirebaseMessagingService implements IaResultHandler {

    @Override
    public void onNewToken(String s) {
        super.onNewToken(s);

        Log.e("Firebase", "FirebaseInstanceIDService : " + s);
    }
    @Override
    public void onMessageReceived(RemoteMessage rm) {
        Log.d("ServicePush", "onMessageReceived called...");

        if (rm == null || rm.getData().size() == 0) {
            Log.i("ServicePush", "no data available....");
            return;
        }

        String strFrom = rm.getFrom();
        Map<String, String> data = rm.getData();

        String strTitle=data.get("title");
        String strBody=data.get("body");
        String strPushType=data.get("push_type");
        int nPushType=Integer.parseInt(strPushType==null? "0" : strPushType);

        Log.d("ServicePush", "all data : " + data);
        Log.d("ServicePush", "from : " + strFrom);
       //Log.d("ServicePush", "seq_member_ack_requester : " + nSeqMemberAckRequester);
        Log.d("ServicePush", "title : " + strTitle);
        Log.d("ServicePush", "body : " + strBody);
        Log.d("ServicePush", "push_type : " + nPushType);

        switch (nPushType){
            case CaEngine.ALARM_PLAN_ELEM_END:
            case CaEngine.ALARM_PLAN_ELEM_BEGIN:
            case CaEngine.ALARM_THIS_MONTH_WON_OVER:
            case CaEngine.ALARM_METER_KWH_OVER_SAVE_REF:
            case CaEngine.ALARM_METER_KWH_OVER_SAVE_PLAN:
            case CaEngine.ALARM_THIS_MONTH_KWH_OVER:
            case CaEngine.ALARM_THIS_MONTH_USAGE_AT:
            case CaEngine.ALARM_NEW_NOTICE: {
                Log.d("ServicePush", "단순 Push received...");
                notifyAlarm(strTitle, strBody);
            }
            break;

            case CaEngine.ALARM_SAVE_ACT_MISSED: {
                Log.d("ServicePush", "미시행절감조치 알림");
                notifyAlarm(strTitle,strBody);
            }
            break;

            default: {
                Log.i("ServicePush", "Unknown push type : " + nPushType);
                notifyAlarm(strTitle,strBody);
            }
            break;
        }

    }

    private void notifyNotImplemented(String strTitle, String strBody, int nSeqPlanElem) {
        Log.d("ServicePush", "notifyRequestAckMember called...");

        final int nNotiId=3186;

        Context ctx=getApplicationContext();

        // Intent it=new Intent(ctx, ActivityLogin.class);
        Intent it=new Intent(ctx, ActivityAlarm.class);
        it.putExtra("seq_plan_elem", nSeqPlanElem);
        it.setAction(Intent.ACTION_MAIN);
        it.addCategory(Intent.CATEGORY_LAUNCHER);

        PendingIntent pit=PendingIntent.getActivity(this, 0, it, 0);

        Intent itActionAccept=new Intent(ctx, ActionReceiver.class);
        itActionAccept.putExtra("NotiId", nNotiId);
        itActionAccept.putExtra("action", "accept");
        itActionAccept.putExtra("seq_plan_elem", nSeqPlanElem);
        PendingIntent pitAccept=PendingIntent.getBroadcast(ctx, 1, itActionAccept, PendingIntent.FLAG_UPDATE_CURRENT);

        Intent itActionReject=new Intent(ctx, ActionReceiver.class);
        itActionReject.putExtra("NotiId", nNotiId);
        itActionReject.putExtra("action", "reject");
        itActionReject.putExtra("seq_plan_elem", nSeqPlanElem);
        PendingIntent pitReject=PendingIntent.getBroadcast(ctx, 2, itActionReject, PendingIntent.FLAG_UPDATE_CURRENT);

        NotificationManager notificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        String strChannelId="1234";

        if (Build.VERSION.SDK_INT >=Build.VERSION_CODES.O) {

            NotificationChannel mChannel = notificationManager.getNotificationChannel(strChannelId);
            if (mChannel == null) {
                mChannel = new NotificationChannel(strChannelId, strTitle, NotificationManager.IMPORTANCE_HIGH);
                // mChannel.enableVibration(true);
                // mChannel.setVibrationPattern(new long[]{100, 200, 300, 400, 500, 400, 300, 200, 400});
                notificationManager.createNotificationChannel(mChannel);
            }

            NotificationCompat.Builder notificationBuilder =
                    new NotificationCompat.Builder(this, strChannelId)
                            .setSmallIcon(R.drawable.push_icon)
                            .setContentTitle(strTitle)
                            .setContentText(strBody)
                            .setContentIntent(pit)
                            .setAutoCancel(true)
                            .addAction(R.drawable.speaker_icon, "실행", pitAccept)
                            .addAction(R.drawable.speaker_icon, "취소", pitReject)
                            .setOngoing(false)
                            .setStyle(new NotificationCompat.BigTextStyle()
                                    .bigText(strBody))
                            .setDefaults(Notification.DEFAULT_SOUND | Notification.DEFAULT_VIBRATE)
                    ;

            notificationManager.notify(nNotiId, notificationBuilder.build());
        }
        else {
            NotificationCompat.Builder notificationBuilder =
                    new NotificationCompat.Builder(this, strChannelId)
                            .setSmallIcon(R.drawable.push_icon)
                            .setContentTitle(strTitle)
                            .setContentText(strBody)
                            .setContentIntent(pit)
                            .setAutoCancel(true)
                            .addAction(R.drawable.speaker_icon, "실행", pitAccept)
                            .addAction(R.drawable.speaker_icon, "취소", pitReject)
                            .setOngoing(false)
                            .setStyle(new NotificationCompat.BigTextStyle()
                                    .bigText(strBody))
                            .setDefaults(Notification.DEFAULT_SOUND | Notification.DEFAULT_VIBRATE)
                    ;

            notificationManager.notify(nNotiId, notificationBuilder.build());
        }


    }



    private void notifyAlarm(String strTitle, String strBody){
        Log.d("ServicePush", "notifyTest called...");

        final int nNotiId=3186;

        Context ctx=getApplicationContext();

        //Tap 할 시 이동할 activity 설정
        Intent it=new Intent(ctx, ActivityAlarmList.class);
        it.setAction(Intent.ACTION_MAIN);
        it.addCategory(Intent.CATEGORY_LAUNCHER);

        PendingIntent pit=PendingIntent.getActivity(this, 0, it, 0);

        NotificationManager notificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        String strChannelId="1234";

        if (Build.VERSION.SDK_INT >=Build.VERSION_CODES.O) {

            NotificationChannel mChannel = notificationManager.getNotificationChannel(strChannelId);
            if (mChannel == null) {
                mChannel = new NotificationChannel(strChannelId, strTitle, NotificationManager.IMPORTANCE_HIGH);
                notificationManager.createNotificationChannel(mChannel);
            }

            NotificationCompat.Builder notificationBuilder =
                    new NotificationCompat.Builder(this, strChannelId)
                            .setSmallIcon(R.drawable.push_icon)
                            .setContentTitle(strTitle)
                            .setContentText(strBody)
                            .setContentIntent(pit)
                            .setAutoCancel(true)
                            .setOngoing(false)
                            .setStyle(new NotificationCompat.BigTextStyle()
                                        .bigText(strBody))
                            .setDefaults(Notification.DEFAULT_SOUND | Notification.DEFAULT_VIBRATE)
                    ;

            notificationManager.notify(nNotiId, notificationBuilder.build());
        }
        else {
            NotificationCompat.Builder notificationBuilder =
                    new NotificationCompat.Builder(this, strChannelId)
                            .setSmallIcon(R.drawable.push_icon)
                            .setContentTitle(strTitle)
                            .setContentText(strBody)
                            .setContentIntent(pit)
                            .setAutoCancel(true)
                            .setOngoing(false)
                            .setStyle(new NotificationCompat.BigTextStyle()
                                    .bigText(strBody))
                            .setDefaults(Notification.DEFAULT_SOUND | Notification.DEFAULT_VIBRATE)
                    ;

            notificationManager.notify(nNotiId, notificationBuilder.build());
        }


    }

        @Override
    public void onResult(CaResult Result) {

    }
}
