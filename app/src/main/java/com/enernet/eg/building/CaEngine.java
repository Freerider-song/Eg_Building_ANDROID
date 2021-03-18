package com.enernet.eg.building;

import android.content.Context;
import android.util.Log;

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

    public static final String[] NO_CMD_ARGS = new String[]{};

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
