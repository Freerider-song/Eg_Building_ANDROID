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
import android.widget.Toast;

import androidx.core.app.NotificationCompat;

import com.enernet.eg.building.activity.ActivityAlarm;
import com.enernet.eg.building.activity.ActivityAlarmList;
import com.enernet.eg.building.activity.ActivityHome;
import com.enernet.eg.building.activity.PreferenceUtil;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

public class ServicePush extends FirebaseMessagingService implements IaResultHandler {

    Context m_Context;
    CaPref m_Pref;

    long now = System.currentTimeMillis();
    Date date = new Date(now);
    SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
    SimpleDateFormat sdf2 = new SimpleDateFormat("yyyyMMddhhmmss");
    String getTime = sdf.format(date);
    String getTime2 = sdf2.format(date);
    String strTitle;
    String strBody;
    String strPushType;
    String strSeqPlanElem;
    int nPushType;
    int nSeqPlanElem;


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

        strTitle=data.get("title");
        strBody=data.get("body");
        strPushType=data.get("push_type");
        strSeqPlanElem = data.get("seq_plan_elem");

        nPushType=Integer.parseInt(strPushType==null? "0" : strPushType);
        nSeqPlanElem=Integer.parseInt(strSeqPlanElem==null? "0" : strSeqPlanElem);


        Log.d("ServicePush", "all data : " + data);
        Log.d("ServicePush", "from : " + strFrom);
       //Log.d("ServicePush", "seq_member_ack_requester : " + nSeqMemberAckRequester);
        Log.d("ServicePush", "title : " + strTitle);
        Log.d("ServicePush", "body : " + strBody);
        Log.d("ServicePush", "push_type : " + nPushType);
        m_Context=getApplicationContext();

        m_Pref = new CaPref(m_Context);
        //int nSeqAdmin=Integer.parseInt(m_Pref.getValue(CaPref.PREF_SEQ_ADMIN, "0"));
        //int nSeqSavePlanActive = Integer.parseInt(m_Pref.getValue(CaPref.PREF_SEQ_SAVE_PLAN_ACTIVE, "0"));
        int nSeqAdmin= PreferenceUtil.getPreferences(m_Context, "SeqAdmin");
        int nSeqSavePlanActive = PreferenceUtil.getPreferences(m_Context, "SeqSavePlanActive");
        //CaApplication.m_Engine.GetBldAlarmList(CaApplication.m_Info.m_nSeqAdmin, 20, this, this);
        //CaApplication.m_Engine.GetSaveResultDaily(CaApplication.m_Info.m_nSeqSavePlanActive, getTime, this, this);
        CaApplication.m_Engine.GetBldAlarmList(nSeqAdmin, getTime2,20, this, this);
        CaApplication.m_Engine.GetSaveResultDaily(nSeqSavePlanActive, getTime, this, this);



    }

    private void notifyNotImplemented(String strTitle, String strBody, int nSeqPlanElem) {
        Log.d("ServicePush", "notifyRequestAckMember called...");

        final int nNotiId=3186;

        Context ctx=getApplicationContext();
        Intent it;

        //
        if(CaApplication.m_Info.m_alPlan.isEmpty()){
            it=new Intent(ctx, com.enernet.eg.building.ActivityLogin.class);
        }
        else {
            it = new Intent(ctx, ActivityAlarmList.class);
        }
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
                            .addAction(R.drawable.speaker_icon, "조치하기", pitAccept)
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
                            .addAction(R.drawable.speaker_icon, "조치하기", pitAccept)
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
        Intent it;

        //Tap 할 시 이동할 activity 설정
        Log.i("ServicePush", "notifyAlarm 실시 alAlarm은 ? " + CaApplication.m_Info.m_alAlarm);
        if(CaApplication.m_Info.m_alAlarm.isEmpty()){
            it=new Intent(ctx, com.enernet.eg.building.ActivityLogin.class);
        }
        else {
            it = new Intent(ctx, ActivityAlarmList.class);
        }

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
            if (Result.object==null) {
                Toast.makeText(getApplicationContext(), "Network Error", Toast.LENGTH_SHORT).show();
                return;
            }

            switch (Result.m_nCallback) {
                case CaEngine.CB_GET_BLD_ALARM_LIST: {
                    Log.i("ServicePush", "Result of GetAlarmList received...");

                    try {
                        JSONObject jo = Result.object;
                        JSONArray jaAlarm = jo.getJSONArray("list_alarm");
                        CaApplication.m_Info.setAlarmList(jaAlarm);
                    }
                    catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                break;

                case CaEngine.CB_GET_SAVE_RESULT_DAILY: {
                    Log.i("Home", "Result of GetSaveResultDaily received...");

                    try {
                        JSONObject jo = Result.object;
                        JSONObject joSave = jo.getJSONObject("save_result_daily");
                        JSONArray jaPlan = joSave.getJSONArray("list_plan_elem");

                        CaApplication.m_Info.m_nSeqSaveRef = joSave.getInt("seq_save_ref");
                        CaApplication.m_Info.m_nSeqSite = joSave.getInt("seq_site");
                        CaApplication.m_Info.m_strSavePlanName = joSave.getString("save_plan_name");
                        CaApplication.m_Info.m_strSaveRefName = joSave.getString("save_ref_name");
                        CaApplication.m_Info.m_dSaveKwhTotalFromElem = joSave.getDouble("save_kwh_total_from_elem");
                        CaApplication.m_Info.m_dSaveWonTotalFromElem = joSave.getDouble("save_won_total_from_elem");
                        CaApplication.m_Info.m_dSaveKwhTotalFromMeter = joSave.getDouble("save_kwh_total_from_meter");
                        CaApplication.m_Info.m_dSaveWonTotalFromMeter = joSave.getDouble("save_kwh_total_from_meter");
                        CaApplication.m_Info.m_dKwhPlanForAllMeter = joSave.getDouble("kwh_plan_for_all_meter");
                        CaApplication.m_Info.m_dKwhRealForAllMeter = joSave.getDouble("kwh_real_for_all_meter");
                        CaApplication.m_Info.m_dKwhRefForAllMeter = joSave.getDouble("kwh_ref_for_all_meter");
                        CaApplication.m_Info.m_dWonPlanForAllMeter = joSave.getDouble("won_plan_for_all_meter");
                        CaApplication.m_Info.m_dWonRealForAllMeter = joSave.getDouble("won_real_for_all_meter");
                        CaApplication.m_Info.m_dWonRefForAllMeter = joSave.getDouble("won_ref_for_all_meter");
                        //CaApplication.m_Info.m_dtSavePlanEnded = parseDate(joSave.getString("time_ended"));
                        //CaApplication.m_Info.m_dtSavePlanCreated = parseDate(joSave.getString("time_created"));
                        CaApplication.m_Info.m_nActCount = joSave.getInt("act_count");
                        CaApplication.m_Info.m_nActCountWithHistory = joSave.getInt("act_count_with_history");

                        CaApplication.m_Info.setPlanList(jaPlan);

                        switch (nPushType){

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

                            case CaEngine.ALARM_PLAN_ELEM_END:
                            case CaEngine.ALARM_PLAN_ELEM_BEGIN:
                            case CaEngine.ALARM_SAVE_ACT_MISSED: {
                                Log.d("ServicePush", "절감조치 알림");
                                notifyNotImplemented(strTitle,strBody, nSeqPlanElem);
                            }
                            break;

                            default: {
                                Log.i("ServicePush", "Unknown push type : " + nPushType);
                                notifyAlarm(strTitle,strBody);
                            }
                            break;
                        }

                    }
                    catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                break;

                default: {
                    Log.i("ServicePush", "Unknown type result received : " + Result.m_nCallback);
                }
                break;

            } // end of switch
        }


}

