package com.enernet.eg.building;

import android.content.Context;
import android.util.Log;

import java.sql.Time;

public class CaEngine {

    public static final int CB_NONE = 0;

    //API 요청
    public static final int CB_CHECK_BLD_LOGIN = 1001;
    public static final int CB_GET_BLD_ADMIN_INFO = 1002;
    public static final int CB_CHANGE_ADMIN_PASSWORD = 1003;
    public static final int CB_REQUEST_AUTH_CODE = 1004;
    public static final int CB_CHECK_AUTH_CODE = 1005;
    public static final int CB_CHECK_ADMIN_CANDIDATE = 1006;
    public static final int CB_GET_ADMIN_USAGE_CURRENT = 1007;
    public static final int CB_SET_BLD_ALARM_AS_READ = 1008;
    public static final int CB_GET_BLD_NOTICE_LIST = 1009;
    public static final int CB_SET_BLD_NOTICE_AS_READ = 1010;
    public static final int CB_GET_SAVE_RESULT_DAILY = 1011;
    public static final int CB_GET_SAVE_RESULT = 1012;
    public static final int CB_GET_USAGE_FOR_ALL_METER_DAY = 1013;
    public static final int CB_GET_USAGE_FOR_ALL_METER_MONTH = 1014;
    public static final int CB_GET_USAGE_FOR_ALL_METER_YEAR = 1015;
    public static final int CB_CHANGE_ADMIN_BLD_SETTINGS= 1016;
    public static final int CB_GET_BLD_ALARM_LIST=1017;
    public static final int CB_SET_SAVE_ACT_BEGIN = 1018;
    public static final int CB_SET_SAVE_ACT_END = 1019;
    public static final int CB_GET_UNREAD_BLD_NOTICE_COUNT = 1020;
    public static final int CB_GET_UNREAD_BLD_ALARM_COUNT = 1021;



    public static final int AUTH_TYPE_UNKNOWN = 1000;
    public static final int AUTH_TYPE_SUBSCRIBE = 1001;
    public static final int AUTH_TYPE_CHANGE_PASSWORD = 1002;

    public static final int MENU_USAGE = 100;
    public static final int MENU_USAGE_DAILY = 101;
    public static final int MENU_USAGE_MONTHLY = 102;
    public static final int MENU_USAGE_YEARLY = 103;
    public static final int MENU_HOME = 200;
    public static final int MENU_SAVING = 300;
    public static final int MENU_ALARM = 500;
    public static final int MENU_NOTICE = 600;
    public static final int MENU_SETTING = 800;
    public static final int MENU_LOGOUT = 900;

    public static final String[] NO_CMD_ARGS = new String[]{};

    public static final int ALARM_TYPE_UNKNOWN = 0;
    public static final int ALARM_TEST = 2;
    public static final int ALARM_NEW_NOTICE = 3001; // 새 공지사항발생
    public static final int ALARM_PLAN_ELEM_BEGIN = 3002; //절감항목 시작
    public static final int ALARM_PLAN_ELEM_END = 3003; // 절감항목종료
    public static final int ALARM_SAVE_ACT_MISSED = 3004; // 미시행절감조치 있음 알림
    public static final int ALARM_THIS_MONTH_USAGE_AT = 3005; //정해진 시간에 사용량과 사용요금 알림
    public static final int ALARM_THIS_MONTH_KWH_OVER = 3006; //설정한 사용량 임계치 초과 알림
    public static final int ALARM_THIS_MONTH_WON_OVER = 3007; //설정한 사용요금 임계치 초과 알림
    public static final int ALARM_METER_KWH_OVER_SAVE_REF = 3008; //계측기별 사용량이 절감기준 사용량 초과
    public static final int ALARM_METER_KWH_OVER_SAVE_PLAN = 3009;// 계측기별 사용량이 절감목표 사용량 초과

    public CaEngine() {

    }

    public final CaResult executeCommand(CaArg Arg, final int nCallMethod, final boolean bSync, final boolean bShowWaitDialog, Context Ctx, IaResultHandler ResultHandler) {

        CaTask Task = new CaTask(nCallMethod, bShowWaitDialog, Ctx, ResultHandler);
        Task = (CaTask) Task.execute(Arg);

        CaResult Result = null;

        if (bSync) {
            try {
                Result = Task.get();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }

        return Result;
    }

    public void CheckBldLogin(final String AdminId, final String Password, final String Os, final String DeviceId,final String Version,Context Ctx, IaResultHandler ResultHandler){
        Log.i("ENGINE", "Id=" + AdminId + ", Password=" + Password);

        CaArg Arg = new CaArg("CheckBldLogin", NO_CMD_ARGS, null);
        Arg.addArg("AdminId", AdminId);
        Arg.addArg("Password", Password);
        Arg.addArg("Os", Os);
        Arg.addArg("DeviceId", DeviceId);
        Arg.addArg("Version", Version);

        executeCommand(Arg, CB_CHECK_BLD_LOGIN, false, true, Ctx, ResultHandler);
    }

    public void GetBldAdminInfo(final int SeqAdmin, Context Ctx, IaResultHandler ResultHandler){
        Log.i("ENGINE", "-");

        CaArg Arg = new CaArg("GetBldAdminInfo", NO_CMD_ARGS, null);
        Arg.addArg("SeqAdmin", SeqAdmin);

        executeCommand(Arg, CB_GET_BLD_ADMIN_INFO, false, true, Ctx, ResultHandler);
    }

    public void ChangeAdminPassword(final String Id, final String PasswordNew, Context Ctx, IaResultHandler ResultHandler){
        Log.i("ENGINE", "-");

        CaArg Arg = new CaArg("ChangeAdminPassword", NO_CMD_ARGS, null);
        Arg.addArg("Id", Id);
        Arg.addArg("PasswordNew", PasswordNew);

        executeCommand(Arg, CB_CHANGE_ADMIN_PASSWORD, false, true, Ctx, ResultHandler);
    }

    public void RequestAuthCode(final String Id, final String Phone, Context Ctx, IaResultHandler ResultHandler){
        Log.i("ENGINE", "-");

        CaArg Arg = new CaArg("RequestAuthCode", NO_CMD_ARGS, null);
        Arg.addArg("Id", Id);
        Arg.addArg("Phone", Phone);

        executeCommand(Arg, CB_REQUEST_AUTH_CODE, false, true, Ctx, ResultHandler);
    }

    public void CheckAuthCode(final String Phone, final String AuthCode, final int SecTimeLimit, Context Ctx, IaResultHandler ResultHandler){
        Log.i("ENGINE", "-");

        CaArg Arg = new CaArg("CheckAuthCode", NO_CMD_ARGS, null);
        Arg.addArg("Phone", Phone);
        Arg.addArg("AuthCode", AuthCode);
        Arg.addArg("SecTimeLimit", SecTimeLimit);

        executeCommand(Arg, CB_CHECK_AUTH_CODE, false, true, Ctx, ResultHandler);
    }

    public void GetBldNoticeList(final int SeqAdmin, final String TimeCreatedMax, final int CountNotice, Context Ctx, IaResultHandler ResultHandler){
        Log.i("ENGINE", "-");

        CaArg Arg = new CaArg("GetBldNoticeList", NO_CMD_ARGS, null);
        Arg.addArg("SeqAdmin", SeqAdmin);
        Arg.addArg("TimeCreatedMax", TimeCreatedMax);
        Arg.addArg("CountNotice", CountNotice);

        executeCommand(Arg, CB_GET_BLD_NOTICE_LIST, false, true, Ctx, ResultHandler);
    }

    public void SetBldNoticeListAsRead(final int SeqAdmin, final String strSeqNoticeList, Context Ctx, IaResultHandler ResultHandler){
        Log.i("ENGINE", "-");

        CaArg Arg = new CaArg("SetBldNoticeListAsRead", NO_CMD_ARGS, null);
        Arg.addArg("SeqAdmin", SeqAdmin);
        Arg.addArg("SeqNoticeList", strSeqNoticeList);

        executeCommand(Arg, CB_SET_BLD_NOTICE_AS_READ, false, true, Ctx, ResultHandler);
    }

    public void SetBldAlarmListAsRead(final int SeqAdmin, final String strSeqAlarmList, Context Ctx, IaResultHandler ResultHandler){
        Log.i("ENGINE", "-");

        CaArg Arg = new CaArg("SetBldAlarmListAsRead", NO_CMD_ARGS, null);
        Arg.addArg("SeqAdmin", SeqAdmin);
        Arg.addArg("SeqAlarmList", strSeqAlarmList);

        executeCommand(Arg, CB_SET_BLD_ALARM_AS_READ, false, true, Ctx, ResultHandler);
    }

    public void GetSaveResultDaily(final int SeqSavePlanActive, final String DateTarget, Context Ctx, IaResultHandler ResultHandler){
        Log.i("ENGINE", "-");

        CaArg Arg = new CaArg("GetSaveResultDaily", NO_CMD_ARGS, null);
        Arg.addArg("SeqSavePlan", SeqSavePlanActive);
        Arg.addArg("DateTarget", DateTarget);

        executeCommand(Arg, CB_GET_SAVE_RESULT_DAILY, false, true, Ctx, ResultHandler);
    }

    public void GetSaveResult(final int SeqSavePlanActive, final String DateFrom, final String DateTo, Context Ctx, IaResultHandler ResultHandler){
        Log.i("ENGINE", "-");

        CaArg Arg = new CaArg("GetSaveResult", NO_CMD_ARGS, null);
        Arg.addArg("SeqSavePlan", SeqSavePlanActive);
        Arg.addArg("DateFrom", DateFrom);
        Arg.addArg("DateTo", DateTo);

        executeCommand(Arg, CB_GET_SAVE_RESULT, false, true, Ctx, ResultHandler);
    }

    public void GetUsageForAllMeterDay(final int SeqSite, final int Year, final int Month, final int Day, Context Ctx, IaResultHandler ResultHandler){
        Log.i("ENGINE", "-");

        CaArg Arg = new CaArg("GetUsageForAllMeterDay", NO_CMD_ARGS, null);
        Arg.addArg("SeqSite", SeqSite);
        Arg.addArg("Year", Year);
        Arg.addArg("Month", Month);
        Arg.addArg("Day", Day);

        executeCommand(Arg, CB_GET_USAGE_FOR_ALL_METER_DAY, false, true, Ctx, ResultHandler);
    }

    public void GetUsageForAllMeterMonth(final int SeqSite, final int Year, final int Month, Context Ctx, IaResultHandler ResultHandler){
        Log.i("ENGINE", "-");

        CaArg Arg = new CaArg("GetUsageForAllMeterMonth", NO_CMD_ARGS, null);
        Arg.addArg("SeqSite", SeqSite);
        Arg.addArg("Year", Year);
        Arg.addArg("Month", Month);


        executeCommand(Arg, CB_GET_USAGE_FOR_ALL_METER_MONTH, false, true, Ctx, ResultHandler);
    }

    public void GetUsageForAllMeterYear(final int SeqSite, final int Year, Context Ctx, IaResultHandler ResultHandler){
        Log.i("ENGINE", "-");

        CaArg Arg = new CaArg("GetUsageForAllMeterYear", NO_CMD_ARGS, null);
        Arg.addArg("SeqSite", SeqSite);
        Arg.addArg("Year", Year);

        executeCommand(Arg, CB_GET_USAGE_FOR_ALL_METER_YEAR, false, true, Ctx, ResultHandler);
    }

    public void ChangeAdminBldSettings(final int SeqAdmin, final boolean NotiAll, final boolean NotiThisMonthKwh, final boolean NotiThisMonthWon,final boolean NotiThisMonthUsageAtTime,
                                       final boolean NotiMeterKwhOverSaveRef,final boolean NotiMeterKwhOverSavePlan,final double ThresholdThisMonthKwh,
                                       final double ThresholdThisMonthWon, final int HourNotiThisMonthUsage, Context Ctx, IaResultHandler ResultHandler){
        Log.i("ENGINE", "-");

        CaArg Arg = new CaArg("ChangeAdminBldSettings", NO_CMD_ARGS, null);
        Arg.addArg("SeqAdmin", SeqAdmin);
        Arg.addArg("NotiAll", NotiAll ? 1 : 0);
        Arg.addArg("NotiThisMonthKwh", NotiThisMonthKwh ? 1 : 0);
        Arg.addArg("NotiThisMonthWon", NotiThisMonthWon ? 1 : 0);
        Arg.addArg("NotiThisMonthUsageAtTime", NotiThisMonthUsageAtTime ? 1 : 0);
        Arg.addArg("NotiMeterKwhOverSaveRef", NotiMeterKwhOverSaveRef ? 1 : 0);
        Arg.addArg("NotiMeterKwhOverSavePlan", NotiMeterKwhOverSavePlan ? 1 : 0);
        Arg.addArg("ThresholdThisMonthKwh", ThresholdThisMonthKwh);
        Arg.addArg("ThresholdThisMonthWon", ThresholdThisMonthWon);
        Arg.addArg("HourNotiThisMonthUsage", HourNotiThisMonthUsage);

        executeCommand(Arg, CB_CHANGE_ADMIN_BLD_SETTINGS, false, true, Ctx, ResultHandler);
    }

    public void GetBldAlarmList(final int SeqAdmin, final String TimeCreatedMax, final int CountMax, Context Ctx, IaResultHandler ResultHandler){
        Log.i("ENGINE", "-");

        CaArg Arg = new CaArg("GetBldAlarmList", NO_CMD_ARGS, null);
        Arg.addArg("TimeCreatedMax", TimeCreatedMax);
        Arg.addArg("SeqAdmin", SeqAdmin);
        Arg.addArg("CountMax", CountMax);

        executeCommand(Arg, CB_GET_BLD_ALARM_LIST, false, true, Ctx, ResultHandler);
    }

    public void SetSaveActBegin(final int SeqSaveAct, final int SeqAdmin, final String DateTarget,Context Ctx, IaResultHandler ResultHandler){
        Log.i("ENGINE", "-");

        CaArg Arg = new CaArg("SetSaveActBegin", NO_CMD_ARGS, null);
        Arg.addArg("SeqAdmin", SeqAdmin);
        Arg.addArg("SeqSaveAct", SeqSaveAct);
        Arg.addArg("DateTarget", DateTarget);

        executeCommand(Arg, CB_SET_SAVE_ACT_BEGIN, false, true, Ctx, ResultHandler);
    }

    public void SetSaveActEnd(final int SeqSaveActHistory, final int SeqAdmin, final String yyyyMMdd,Context Ctx, IaResultHandler ResultHandler){
        Log.i("ENGINE", "-");

        CaArg Arg = new CaArg("SetSaveActEnd", NO_CMD_ARGS, null);
        Arg.addArg("SeqAdmin", SeqAdmin);
        Arg.addArg("SeqSaveActHistory", SeqSaveActHistory);
        Arg.addArg("yyyyMMdd", yyyyMMdd);

        executeCommand(Arg, CB_SET_SAVE_ACT_END, false, true, Ctx, ResultHandler);
    }

    public void GetUnreadBldNoticeCount(final int SeqAdmin,Context Ctx, IaResultHandler ResultHandler){
        Log.i("ENGINE", "-");

        CaArg Arg = new CaArg("GetUnreadBldNoticeCount", NO_CMD_ARGS, null);
        Arg.addArg("SeqAdmin", SeqAdmin);


        executeCommand(Arg, CB_GET_UNREAD_BLD_NOTICE_COUNT, false, true, Ctx, ResultHandler);
    }

    public void GetUnreadBldAlarmCount(final int SeqAdmin,Context Ctx, IaResultHandler ResultHandler){
        Log.i("ENGINE", "-");

        CaArg Arg = new CaArg("GetUnreadBldAlarmCount", NO_CMD_ARGS, null);
        Arg.addArg("SeqAdmin", SeqAdmin);


        executeCommand(Arg, CB_GET_UNREAD_BLD_ALARM_COUNT, false, true, Ctx, ResultHandler);
    }

}
