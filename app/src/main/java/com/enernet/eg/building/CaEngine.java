package com.enernet.eg.building;

import android.content.Context;

public class CaEngine {

    public static final int CB_NONE = 0;
    /*
    public static final int AUTH_TYPE_UNKNOWN = 100;

    //API 요청
	public static final int KS_CHECK_MEMBER_LOGIN = 1000;
	public static final int KS_GET_MEMBER_INFO = 1001;
	public static final int KS_GET_PROJECT_LIST_FOR_MEMBER = 1002;
	public static final int KS_GET_PROJECT_INFO = 1003;
	public static final int KS_GET_NOTICE_LIST = 1004;
     */

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
    /*

    public void CheckMemberLogin(final String Id, final String Password, Context Ctx, IaResultHandler ResultHandler){
        Log.i("ENGINE", "Id=" + Id + ", Password=" + Password);

        CaArg Arg = new CaArg("CheckMemberLogin", NO_CMD_ARGS, null);
        Arg.addArg("Id", Id);
        Arg.addArg("Password", Password);
        Arg.addArg("DeviceId", CaApplication.m_Info.m_strPushId);
        Arg.addArg("Os", "ANDROID");
        Arg.addArg("Version", 1910231);

        executeCommand(Arg, KS_CHECK_MEMBER_LOGIN, false, true, Ctx, ResultHandler);
    }

     */
}
