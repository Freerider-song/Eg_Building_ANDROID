package com.enernet.eg.building.model;

import android.content.Context;
import android.telephony.PhoneNumberUtils;
import android.telephony.TelephonyManager;
import android.util.Log;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

public class CaInfo {

    public static final int DEFAULT_REQUEST_NOTICE_COUNT = 10;

    public SimpleDateFormat m_dfStd=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    public SimpleDateFormat m_dfyyyyMMddhhmmStd=new SimpleDateFormat("yyyy-MM-dd HH:mm");
    public SimpleDateFormat m_dfyyyyMMddhhmm=new SimpleDateFormat("yyyyMMddHHmm");
    public SimpleDateFormat m_dfyyyyMMddhhmmss=new SimpleDateFormat("yyyyMMddHHmmss");
    public SimpleDateFormat m_dfyyyyMMdd=new SimpleDateFormat("yyyyMMdd");
    public SimpleDateFormat m_dfyyyyMMddhhmm_ampm=new SimpleDateFormat("yyyy-MM-dd hh:mm a");


    public int m_nSeqMember=0;
    public int m_nSeqProjectSelected=0;
    public int m_nMemberType=0;
    public String m_strMemberId="";
    public String m_strPassword="";
    public String m_strMemberName="";
    public String m_strMemberPhone="";
    public String m_strMemberMail="";
    public String m_strMemberCompany="";
    public String m_strMemberRank="";

    public boolean m_bNotiAll=true;
    public boolean m_bNotiKwh=true;
    public boolean m_bNotiWon=true;
    public boolean m_bNotiSavingStandard=true;
    public boolean m_bNotiSavingGoal=true;
    public boolean m_bNotiUsageAtTime=true;

    public boolean m_bShowPush=true;

    public String m_strMmsTarget="01094569304";

    public int m_nCountNoticeTop=0;
    public int m_nCountNoticeTotal=0;
    public int m_nCountNoticeUnread=0;

    public int m_nCountSafetyEduTop=0;
    public int m_nCountSafetyEduTotal=0;
    public int m_nCountSafetyEduUnread=0;

    public Date m_dtCreated=null;
    public Date m_dtModified=null;
    public Date m_dtLasLogin=null;

    public Date m_dtNoticeCreatedMaxForNextRequest=null;

    public Date m_dtPicSendCreatedMaxForNextRequest=null;

    public String m_strPushId="";

    public String m_strMemberNameSubscribing="";
    public String m_strPhoneSubscribing="";

    /*
    public int m_nAuthType=CaEngine.AUTH_TYPE_UNKNOWN;

    public CaProject m_caProject=new CaProject();

    public ArrayList<CaNotice> m_alNotice=new ArrayList<>();
    public ArrayList<CaSafety> m_alSafety=new ArrayList<>();
    public ArrayList<CaProject> m_alProject=new ArrayList<>();
    public ArrayList<CaMms> m_alMms=new ArrayList<>();
    public ArrayList<CaReport> m_alReport=new ArrayList<>();
    public ArrayList<CaSurvey> m_alSurvey=new ArrayList<>();
    public ArrayList<CaJobCode> m_alJobCode=new ArrayList<>();

     */

    public int m_nAuthType = CaEngine.AUTH_TYPE_UNKNOWN;

    public String getPhoneNumber() {
        TelephonyManager telephony = (TelephonyManager) CaApplication.m_Context.getSystemService(Context.TELEPHONY_SERVICE);
        String phoneNumber ="";

        try {
            if (telephony.getLine1Number() != null) {
                phoneNumber = telephony.getLine1Number();
            }
            else {
                if (telephony.getSimSerialNumber() != null) {
                    phoneNumber = telephony.getSimSerialNumber();
                }
            }
        }
        catch (SecurityException e) {
            e.printStackTrace();
            Log.e("CaInfo", "getPhoneNumber failed due to SecurityException = "+e.getCause());
        }

        if (phoneNumber.startsWith("+82")) {
            phoneNumber = phoneNumber.replace("+82", "0"); // +8210xxxxyyyy 로 시작되는 번호
        }

        phoneNumber = PhoneNumberUtils.formatNumber(phoneNumber);

        Log.e("CaInfo", "PhoneNumber = "+phoneNumber);

        return phoneNumber;
    }


    public Date parseDate(String str) {
        Calendar cal=new GregorianCalendar(1970, 1, 1, 0, 0, 0);

        Date dt;

        try {
            dt = CaApplication.m_Info.m_dfStd.parse(str);
        }
        catch (ParseException e) {
            // e.printStackTrace();
            dt=cal.getTime();
        }

        return dt;
    }
}
