package com.enernet.eg.building;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Paint;
import android.nfc.Tag;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.enernet.eg.building.activity.ActivityChangePasswordAuth;
import com.enernet.eg.building.activity.ActivityHome;
import com.enernet.eg.building.activity.ActivityHome_2;
import com.enernet.eg.building.activity.BaseActivity;
import com.enernet.eg.building.activity.PreferenceUtil;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdReceiver;
import com.google.firebase.iid.InstanceIdResult;
import com.google.firebase.messaging.FirebaseMessaging;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class ActivityLogin extends BaseActivity implements IaResultHandler {

    private EditText m_etUserId;
    private EditText m_etPassword;

    private Context m_Context;
    private CaPref m_Pref;

    String m_strMemberId;
    String m_strPassword;
    String m_strDeviceId;

    private SharedPreferences preferences;
    private SharedPreferences.Editor editor;

    //String m_Os = "ANDROID";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        m_Context = getApplicationContext();
        m_Pref = new CaPref(m_Context);

        m_etUserId = findViewById(R.id.input_id);
        m_etPassword = findViewById(R.id.input_pw);

        TextView basicVer = findViewById(R.id.tv_version) ;
        basicVer.setText(getVersion());


        String savedLoginId = m_Pref.getValue(CaPref.PREF_MEMBER_ID, "");
        if (!savedLoginId.equals("")) {
            m_etUserId.setText(savedLoginId);
        }

        String savedPassword = m_Pref.getValue(CaPref.PREF_PASSWORD, "");
        if (!savedPassword.equals("")) {
            m_etPassword.setText(savedPassword);
        }

        getPushId();

        //기본 SharedPreferences 환경과 관련된 객체를 얻어옵니다.
        preferences = PreferenceManager.getDefaultSharedPreferences(this);
        // SharedPreferences 수정을 위한 Editor 객체를 얻어옵니다.
        editor = preferences.edit();
    }

    private String getVersion()
    {
        String version = "";
        try {
            PackageInfo packageInfo = this.getPackageManager().getPackageInfo(this.getPackageName(), 0);
            version = packageInfo.versionName;
            version += " ";
        }
        catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }

        System.out.println("version : " + version);
        return version;
    }

    public void getPushId() {

        FirebaseMessaging.getInstance().getToken()
                .addOnCompleteListener(new OnCompleteListener<String>() {
                    @Override
                    public void onComplete(@NonNull Task<String> task) {
                        if (!task.isSuccessful()) {
                            return;
                        }
                        // Get new FCM registration token
                        String token = task.getResult();

                        // Log a
                        Log.e("newToken", token);
                        CaApplication.m_Info.m_strPushId=token;
                    }
                });
    }


    @Override
    public void onBackPressed() {
        Log.i("Login", "onBackPressed called...");

        promptAppExit();

    }

    public void promptAppExit(){
        AlertDialog.Builder dlg = new AlertDialog.Builder(ActivityLogin.this);
        dlg.setTitle("경고"); //제목
        dlg.setMessage("앱을 종료하시겠습니까?"); // 메시지
        //dlg.setIcon(R.drawable.deum); // 아이콘 설정
//                버튼 클릭시 동작

        dlg.setNegativeButton("취소",new DialogInterface.OnClickListener(){
            public void onClick(DialogInterface dialog, int which) {
            }
        });

        dlg.setPositiveButton("확인",new DialogInterface.OnClickListener(){
            public void onClick(DialogInterface dialog, int which) {
                // kill app
                finishAffinity();
                System.runFinalization();
                System.exit(0);
            }
        });

        dlg.show();

    }

    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_login: {

                m_strMemberId = m_etUserId.getText().toString();
                m_strPassword = m_etPassword.getText().toString();

                if (m_strMemberId.isEmpty() || m_strPassword.isEmpty()) {
                    AlertDialog.Builder dlg = new AlertDialog.Builder(ActivityLogin.this);
                    //dlg.setTitle("경고"); //제목
                    dlg.setMessage("아이디와 비밀번호를 입력해주세요"); // 메시지
                    //dlg.setIcon(R.drawable.deum); // 아이콘 설정
//                버튼 클릭시 동작

                    dlg.setPositiveButton("확인",new DialogInterface.OnClickListener(){
                        public void onClick(DialogInterface dialog, int which) {
                        }
                    });
                    dlg.show();
                }
                else {
                    Calendar calToday = Calendar.getInstance();
                    calToday.add(Calendar.DATE, 0);
                    SimpleDateFormat myyyyMMddFormat = new SimpleDateFormat("yyMMdd");
                    String m_dtToday = myyyyMMddFormat.format(calToday.getTime())+"1";

                    CaApplication.m_Engine.CheckBldLogin(m_strMemberId, m_strPassword, "android", CaApplication.m_Info.m_strPushId, m_dtToday, this, this);
                }
            }
            break;

            case R.id.btn_change_password: {
                Intent it = new Intent(this, ActivityChangePasswordAuth.class);
                startActivity(it);
            }


        }
    }

    @Override
    public void onResult(CaResult Result) {
        if (Result.object == null) {
            Toast.makeText(m_Context, "Check Network", Toast.LENGTH_SHORT).show();
            return;
        }

        switch (Result.m_nCallback) {
            case CaEngine.CB_CHECK_BLD_LOGIN: {

                try {
                    JSONObject jo = Result.object;
                    int nResultCode = jo.getInt("result_code");

                    if (nResultCode == 1) {
                        m_Pref.setValue(CaPref.PREF_MEMBER_ID, m_strMemberId);
                        m_Pref.setValue(CaPref.PREF_PASSWORD, m_strPassword);


                        CaApplication.m_Info.m_nSeqAdmin = jo.getInt("seq_admin");
                        CaApplication.m_Info.m_nTeamType = jo.getInt("team_type");
                        m_Pref.setValue(CaPref.PREF_SEQ_ADMIN, CaApplication.m_Info.m_nSeqAdmin);
                        PreferenceUtil.setPreferences(m_Context, "SeqAdmin", CaApplication.m_Info.m_nSeqAdmin);


                        CaApplication.m_Info.m_strAdminId = m_strMemberId;
                        CaApplication.m_Info.m_strPassword = m_strPassword;

                        CaApplication.m_Engine.GetBldAdminInfo(CaApplication.m_Info.m_nSeqAdmin, this, this);
                    } else {
                        AlertDialog.Builder dlg = new AlertDialog.Builder(ActivityLogin.this);
                        dlg.setMessage("아이디와 비밀번호를 확인해주세요");
                        dlg.setPositiveButton("확인", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                            }
                        });
                        dlg.show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            break;

            case CaEngine.CB_GET_BLD_ADMIN_INFO: {
                //Log.i{TAG, "Result of GetBldAdminInfo received...");
                try {
                    JSONObject jo = Result.object;
                    JSONArray jaListSite = jo.getJSONArray("list_site");

                    CaApplication.m_Info.m_strAdminName = jo.getString("admin_name");
                    CaApplication.m_Info.m_strAdminPhone = jo.getString("admin_phone");
                    CaApplication.m_Info.m_nUnreadNoticeCount = jo.getInt("unread_notice_count");
                    CaApplication.m_Info.m_nUnreadAlarmCount = jo.getInt("unread_alarm_count");
                    CaApplication.m_Info.m_dtCreated = parseDate(jo.getString("time_created"));
                    CaApplication.m_Info.m_dtModified = parseDate(jo.getString("time_modified"));
                    CaApplication.m_Info.m_dtLastLogin = parseDate(jo.getString("time_last_login"));
                    CaApplication.m_Info.m_dtChangePassword = parseDate(jo.getString("time_change_password"));
                    CaApplication.m_Info.m_nSeqTeam = jo.getInt("seq_team");
                    CaApplication.m_Info.m_nTeamType = jo.getInt("team_type");

                    CaApplication.m_Info.m_strTeamName = jo.getString("team_name");
                    CaApplication.m_Info.m_bNotiAll = jo.getBoolean("noti_all");
                    CaApplication.m_Info.m_bNotiKwh = jo.getBoolean("noti_this_month_kwh");
                    CaApplication.m_Info.m_bNotiWon = jo.getBoolean("noti_this_month_won");
                    CaApplication.m_Info.m_bNotiUsageAtTime = jo.getBoolean("noti_this_month_usage_at_time");
                    CaApplication.m_Info.m_bNotiSavingStandard = jo.getBoolean("noti_meter_kwh_over_save_ref");
                    CaApplication.m_Info.m_bNotiSavingGoal = jo.getBoolean("noti_meter_kwh_over_save_plan");
                    CaApplication.m_Info.m_dThresholdThisMonthKwh = jo.getDouble("threshold_this_month_kwh");
                    CaApplication.m_Info.m_dThresholdThisMonthWon = jo.getDouble("threshold_this_month_won");
                    CaApplication.m_Info.m_nHourNotiThisMonthUsage = jo.getInt("hour_noti_this_month_usage");

                    for(int i =0; i<jaListSite.length();i++){
                        JSONObject joListSite = jaListSite.getJSONObject(i);
                        CaApplication.m_Info.m_nSeqSite = joListSite.getInt("seq_site");
                        CaApplication.m_Info.m_nSiteType = joListSite.getInt("site_type");
                        CaApplication.m_Info.m_strSiteName = joListSite.getString("site_name");
                        CaApplication.m_Info.m_nBuiltYear = joListSite.getInt("built_year");
                        CaApplication.m_Info.m_nBuiltMonth = joListSite.getInt("built_month");
                        CaApplication.m_Info.m_strFloorInfo = joListSite.getString("floor_info");
                        CaApplication.m_Info.m_strHomePage = joListSite.getString("home_page");
                        CaApplication.m_Info.m_strSitePhone = joListSite.getString("site_phone");
                        CaApplication.m_Info.m_strSiteFax = joListSite.getString("site_fax");
                        CaApplication.m_Info.m_strSiteAddress = joListSite.getString("site_address");
                        CaApplication.m_Info.m_dSiteDx = joListSite.getDouble("site_dx");
                        CaApplication.m_Info.m_dSiteDy = joListSite.getDouble("site_dy");
                        CaApplication.m_Info.m_dKwContracted = joListSite.getDouble("kw_contracted");
                        CaApplication.m_Info.m_nReadDay = joListSite.getInt("read_day");
                        CaApplication.m_Info.m_nSeqSavePlanActive= joListSite.getInt("seq_save_plan_active");
                    }

                    Log.i("eee", "성공적으로 불려졌습니다."+CaApplication.m_Info.m_nReadDay);

                    long now = System.currentTimeMillis();
                    Date date = new Date(now);
                    SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
                    String getTime = sdf.format(date);

                    Log.i("get time is ", "is "+ getTime + "and saveplan is" + CaApplication.m_Info.m_nSeqSavePlanActive);

                    m_Pref.setValue(CaPref.PREF_SEQ_SAVE_PLAN_ACTIVE, CaApplication.m_Info.m_nSeqSavePlanActive);
                    PreferenceUtil.setPreferences(m_Context, "SeqSavePlanActive", CaApplication.m_Info.m_nSeqSavePlanActive);

                    //CaApplication.m_Engine.GetSaveResultDaily(CaApplication.m_Info.m_nSeqSavePlanActive, getTime, this, this);

                    finish();

                    Intent it = new Intent(this, ActivityHome.class);
                    startActivity(it);

                } catch (JSONException e) {
                    e.printStackTrace();
                }



            }
            break;

            /*
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
                    CaApplication.m_Info.m_dtSavePlanEnded = parseDate(joSave.getString("time_ended"));
                    CaApplication.m_Info.m_dtSavePlanCreated = parseDate(joSave.getString("time_created"));
                    CaApplication.m_Info.m_nActCount = joSave.getInt("act_count");
                    CaApplication.m_Info.m_nActCountWithHistory = joSave.getInt("act_count_with_history");


                    CaApplication.m_Info.setPlanList(jaPlan);

                    finish();

                    Intent it = new Intent(this, ActivityHome.class);
                    startActivity(it);
                }
                catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            break;

             */


            default: {
                //Log.i(TAG, "Unknown type result received");
            }
            break;

        }
    }
}