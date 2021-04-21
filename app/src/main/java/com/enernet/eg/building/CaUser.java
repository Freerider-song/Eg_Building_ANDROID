package com.enernet.eg.building;

import android.content.Context;
import android.content.SharedPreferences;

public class CaUser {

    private static final String MDL_APP_PREFERENCES		= "mdlAppPreferences";
    private static final String MDL_C2DM 				= "mdlC2dm";
    private static final String MDL_REGISTRATION_ID		= "mdlRegistrationId";
    private static final String MDL_LOGIN_ID			= "loginId";
    private static final String MDL_ROLE_INFO			= "roleInfo";

    private int nSeqAdmin		= 0;
    private int nSeqSavePlanActive	     = 0;





    public CaUser() {

    }

    public void load() {
        Context context 		= CaApplication.getContext();
        //기본 SharedPreferences 환경과 관련된 객체를 얻어옵니다.
        SharedPreferences pref 	= context.getSharedPreferences(MDL_APP_PREFERENCES, Context.MODE_PRIVATE);

        //	loginid			= pref.getString(MDL_LOGIN_ID, "");
        //	roleInfo		= pref.getString(MDL_ROLE_INFO, "");
        //	c2dm 			= pref.getString(MDL_C2DM, "N");
        //	registration_id	= pref.getString(MDL_REGISTRATION_ID, "");
    }

    public void save() {
        Context context = CaApplication.getContext();
        SharedPreferences pref = context.getSharedPreferences(MDL_APP_PREFERENCES, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();

        //	editor.putString(MDL_LOGIN_ID, loginid);
        //	editor.putString(MDL_ROLE_INFO, roleInfo);
        //	editor.putString(MDL_C2DM, c2dm);
        //	editor.putString(MDL_REGISTRATION_ID, registration_id);
        editor.commit();
    }

	/*
	public String getC2dm() {
		return c2dm;
	}

	public void setC2dm(String c2dm) {
		this.c2dm = c2dm;
	}

	public String getRegistration_id() {
		return registration_id;
	}

	public void setRegistration_id(String registration_id) {
		this.registration_id = registration_id;
	}
	public String getLoginid() {
		return loginid;
	}

	public void setLoginid(String loginid) {
		this.loginid = loginid;
	}
	public String getRoleInfo() {
		return roleInfo;
	}
	public void setRoleInfo(String roleInfo) {
		this.roleInfo = roleInfo;
	}

*/
}
