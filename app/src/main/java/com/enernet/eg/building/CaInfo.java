package com.enernet.eg.building;

import android.content.Context;
import android.telephony.PhoneNumberUtils;
import android.telephony.TelephonyManager;
import android.util.Log;

import com.enernet.eg.building.model.CaNotice;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
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


    public int m_nResultCode =0;
    public int m_nSeqAdmin=0;
    public int m_nTeamType=0;

    public String m_strAdminName="";
    public String m_strAdminPhone="";
    public int m_nUnreadNoticeCount=0;
    public int m_nUnreadAlarmCount=0;
    public Date m_dtLastLogin=null;
    public Date m_dtCreated=null;
    public Date m_dtModified=null;
    public Date m_dtChangePassword =null;
    public int m_nSeqTeam=0;
    public String m_strTeamName="";
    public boolean m_bNotiAll=true;
    public boolean m_bNotiKwh=true;
    public boolean m_bNotiWon=true;
    public boolean m_bNotiSavingStandard=true;
    public boolean m_bNotiSavingGoal=true;
    public boolean m_bNotiUsageAtTime=true;
    public double m_dThresholdThisMonthKwh=0.0;
    public double m_dThresholdThisMonthWon=0.0;
    public int m_nHourNotiThisMonthUsage=0;

    public int m_nSeqSite = 0;
    public int m_nSiteType=0;
    public String m_strSiteName="";
    public int m_nBuiltYear=0;
    public int m_nBuiltMonth=0;
    public String m_strFloorInfo="";
    public String m_strHomePage="";
    public String m_strSiteFax="";
    public String m_strSitePhone="";
    public String m_strSiteAddress="";
    public double m_dSiteDx=0.0;
    public double m_dSiteDy=0.0;
    public double m_dKwContracted=0.0;
    public int m_nReadDay=0;




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



    public boolean m_bShowPush=true;

    public String m_strMmsTarget="01094569304";

    public int m_nCountNoticeTop=0;
    public int m_nCountNoticeTotal=0;

    public Date m_dtNoticeCreatedMaxForNextRequest=null;

    public Date m_dtPicSendCreatedMaxForNextRequest=null;

    public String m_strPushId="";

    public String m_strMemberNameSubscribing="";
    public String m_strPhoneSubscribing="";

    public ArrayList<CaNotice> m_alNotice=new ArrayList<>();

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

    public String getNoticeReadListString() {
        String strResult="";

        for (CaNotice notice : m_alNotice) {
            if (notice.m_bReadStateChanged) {
                strResult = (strResult + Integer.toString(notice.m_nSeqNotice)+",");
            }
        }

        if (strResult.isEmpty()) return strResult;

        strResult = strResult.substring(0, strResult.length() - 1);
        return strResult;
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

    public CaNotice findNotice(int nSeqNotice) {

        for (CaNotice notice : m_alNotice) {
            if (notice.m_nSeqNotice==nSeqNotice) return notice;
        }

        return null;
    }

    public CaNotice registerNotice(int nSeqNotice) {
        CaNotice notice=findNotice(nSeqNotice);
        if (notice==null) {
            notice=new CaNotice();
            notice.m_nSeqNotice=nSeqNotice;
            m_alNotice.add(notice);
        }

        return notice;
    }


    public void setNoticeList(JSONArray jaTop, JSONArray jaNormal) {

        try {

            ArrayList<CaNotice> alTop=new ArrayList<>();

            for (int i=0; i<jaTop.length(); i++) {
                JSONObject joNotice=jaTop.getJSONObject(i);

                CaNotice notice=new CaNotice();
                notice.m_nSeqNotice=joNotice.getInt("seq_notice");
                notice.m_nWriterType=joNotice.getInt("writer_type");
                notice.m_nTargetType=joNotice.getInt("target_type");
                notice.m_strTitle=joNotice.getString("title");
                notice.m_strContent=joNotice.getString("content");
                notice.m_bTop=true;
                notice.m_dtCreated=parseDate(joNotice.getString("time_created"));

                String strTimeRead=joNotice.getString("time_read");
                Log.i("NoticeTopList", "strTimeRead="+strTimeRead);

                if (strTimeRead.equals("null")) {
                    notice.m_bRead=false;
                    notice.m_dtRead=null;
                }
                else {
                    notice.m_bRead=true;
                    notice.m_dtRead=parseDate(strTimeRead);
                }

                alTop.add(notice);
            }

            ArrayList<CaNotice> alNormal=new ArrayList<>();

            for (CaNotice notice : m_alNotice) {
                if (notice.m_bTop) continue;
                alNormal.add(notice);
            }

            m_alNotice.clear();

            for (CaNotice notice : alTop) {
                m_alNotice.add(notice);
            }

            for (CaNotice notice : alNormal) {
                m_alNotice.add(notice);
            }

            for (int i=0; i<jaNormal.length(); i++) {
                JSONObject joNotice=jaNormal.getJSONObject(i);

                int nSeqNotice=joNotice.getInt("seq_notice");
                CaNotice notice=registerNotice(nSeqNotice);

                notice.m_nWriterType=joNotice.getInt("writer_type");
                notice.m_nTargetType=joNotice.getInt("target_type");
                notice.m_strTitle=joNotice.getString("title");
                notice.m_strContent=joNotice.getString("content");
                notice.m_bTop=false;
                notice.m_dtCreated=parseDate(joNotice.getString("time_created"));

                String strTimeRead=joNotice.getString("time_read");
                Log.i("NoticeList", "strTimeRead="+strTimeRead);

                if (strTimeRead.equals("null")) {
                    notice.m_bRead=false;
                    notice.m_dtRead=null;
                }
                else {
                    notice.m_bRead=true;
                    notice.m_dtRead=parseDate(strTimeRead);
                }

                m_dtNoticeCreatedMaxForNextRequest=notice.m_dtCreated;
            }

        }
        catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
