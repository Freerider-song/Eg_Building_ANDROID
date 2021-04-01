package com.enernet.eg.building;

import android.content.Context;
import android.util.Log;

import java.sql.Time;

public class CaEngine {

    public static final int CB_NONE = 0;

    //API 요청
    public static final int CB_CHECK_ADMIN_LOGIN = 1001;
    public static final int CB_GET_BLD_ADMIN_INFO = 1002;
    public static final int CB_CHANGE_ADMIN_PASSWORD = 1003;
    public static final int CB_REQUEST_AUTH_CODE = 1004;
    public static final int CB_CHECK_AUTH_CODE = 1005;
    public static final int CB_CHECK_ADMIN_CANDIDATE = 1006;
    public static final int CB_GET_ADMIN_USAGE_CURRENT = 1007;
    public static final int CB_GET_ADMIN_ALARM_LIST = 1008;
    public static final int CB_GET_BLD_NOTICE_LIST = 1009;
    public static final int CB_SET_BLD_NOTICE_AS_READ = 1010;


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
    public static final int ALARM_TYPE_REQUEST_ACK_MEMBER = 1001;
    public static final int ALARM_TYPE_RESPONSE_ACK_MEMBER_ACCEPTED = 1002;
    public static final int ALARM_TYPE_RESPONSE_ACK_MEMBER_REJECTED = 1003;
    public static final int ALARM_TYPE_RESPONSE_ACK_MEMBER_CANCELED = 1004;
    public static final int ALARM_TYPE_NOTI_KWH = 1101;
    public static final int ALARM_TYPE_NOTI_WON = 1102;
    public static final int ALARM_TYPE_NOTI_PRICE_LEVEL = 1103;
    public static final int ALARM_TYPE_NOTI_USAGE = 1104;
    public static final int ALARM_TYPE_NOTI_TRANS = 1110;
    public static final int ALARM_TEST = 2;

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

    public void CheckAdminLogin(final String AdminId, final String Password, Context Ctx, IaResultHandler ResultHandler){
        Log.i("ENGINE", "Id=" + AdminId + ", Password=" + Password);

        CaArg Arg = new CaArg("CheckAdminLogin", NO_CMD_ARGS, null);
        Arg.addArg("AdminId", AdminId);
        Arg.addArg("Password", Password);

        executeCommand(Arg, CB_CHECK_ADMIN_LOGIN, false, true, Ctx, ResultHandler);
    }

    public void GetBldAdminInfo(final int SeqAdmin, Context Ctx, IaResultHandler ResultHandler){
        Log.i("ENGINE", "-");

        CaArg Arg = new CaArg("GetBldAdminInfo", NO_CMD_ARGS, null);
        Arg.addArg("SeqAdmin", SeqAdmin);

        executeCommand(Arg, CB_GET_BLD_ADMIN_INFO, false, true, Ctx, ResultHandler);
    }

    public void ChangeAdminPassword(final int SeqAdmin, final String PasswordNew, Context Ctx, IaResultHandler ResultHandler){
        Log.i("ENGINE", "-");

        CaArg Arg = new CaArg("ChangeAdminPassword", NO_CMD_ARGS, null);
        Arg.addArg("SeqAdmin", SeqAdmin);
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

        CaArg Arg = new CaArg("GetBldNoticeList", NO_CMD_ARGS, null);
        Arg.addArg("SeqAdmin", SeqAdmin);
        Arg.addArg("SeqNoticeList", strSeqNoticeList);

        executeCommand(Arg, CB_SET_BLD_NOTICE_AS_READ, false, true, Ctx, ResultHandler);
    }




}
