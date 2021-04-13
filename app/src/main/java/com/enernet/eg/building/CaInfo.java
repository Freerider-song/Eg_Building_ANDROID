package com.enernet.eg.building;

import android.content.Context;
import android.telephony.PhoneNumberUtils;
import android.telephony.TelephonyManager;
import android.util.Log;

import com.enernet.eg.building.model.CaAct;
import com.enernet.eg.building.model.CaActHistory;
import com.enernet.eg.building.model.CaMeter;
import com.enernet.eg.building.model.CaMeterUsage;
import com.enernet.eg.building.model.CaNotice;
import com.enernet.eg.building.model.CaPlan;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;
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

    public DecimalFormat m_dfKwh = new DecimalFormat("0.#"); // 12345.7
    public DecimalFormat m_dfPercent = new DecimalFormat("0.##"); // 12345.7
    public DecimalFormat m_dfWon = new DecimalFormat("#,##0"); // 87,654


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
    public int m_nSeqSavePlanActive=0;

    public int m_nSeqSavePlan=0;
    public int m_nSeqSaveRef=0;
    public String m_strSavePlanName ="";
    public String m_strSaveRefName = "";
    public double m_dSaveKwhTotalFromElem=0.0;
    public double m_dSaveWonTotalFromElem=0.0;
    public double m_dSaveKwhTotalFromMeter=0.0;
    public double m_dSaveWonTotalFromMeter=0.0;
    public double m_dKwhRefForAllMeter=0.0;
    public double m_dKwhPlanForAllMeter=0.0;
    public double m_dKwhRealForAllMeter=0.0;
    public double m_dWonRefForAllMeter=0.0;
    public double m_dWonPlanForAllMeter=0.0;
    public double m_dWonRealForAllMeter=0.0;
    public Date m_dtSavePlanCreated= null;
    public Date m_dtSavePlanEnded= null;
    public int m_nActCount = 0;
    public int m_nActCountWithHistory=0;

    //GetSaveResult
    public int m_nTotalSaveActCount = 0;
    public int m_nTotalSaveActWithHistoryCount=0;
    public double m_dAvgKwhForAllMeter=0.0;
    public double m_dAvgWonForAllMeter=0.0;





    public int m_nSeqMember=0;
    public int m_nSeqProjectSelected=0;
    public int m_nMemberType=0;
    public String m_strAdminId="";
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
    public ArrayList<CaPlan> m_alPlan = new ArrayList<>();
    public ArrayList<CaMeter> m_alMeter = new ArrayList<>();



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


    public void setMeterList(JSONArray jaMeter) {
        Log.i("CaInfo", "SetMeterList is activated...");

        m_alMeter.clear();
        try{
            for(int i=0;i<jaMeter.length();i++){
                JSONObject joMeter = jaMeter.getJSONObject(i);
                CaMeter meter = new CaMeter();

                meter.m_nSeqMeter=joMeter.getInt("seq_meter");
                meter.m_strMid=joMeter.getString("mid");
                meter.m_strDescr=joMeter.getString("descr");
                meter.m_dKwhRef=joMeter.getDouble("kwh_ref");
                meter.m_dWonRef=joMeter.getDouble("won_ref");
                meter.m_dKwhPlan=joMeter.getDouble("kwh_plan");
                meter.m_dWonPlan=joMeter.getDouble("kwh_plan");

                JSONArray jaUsage = joMeter.getJSONArray("list_usage");
                meter.m_alMeterUsage = new ArrayList<>();
                for(int j=0; j<jaUsage.length();j++){
                    JSONObject joUsage = jaUsage.getJSONObject(i);
                    CaMeterUsage usage = new CaMeterUsage();
                    usage.m_nYear=joUsage.getInt("year");
                    usage.m_nMonth=joUsage.getInt("month");
                    usage.m_nDay=joUsage.getInt("day");
                    usage.m_bHoliday=joUsage.getBoolean("is_holiday");
                    usage.m_dKwh=joUsage.getDouble("kwh");
                    usage.m_dWon=joUsage.getDouble("won");

                    meter.m_alMeterUsage.add(usage);

                }
                m_alMeter.add(meter);
            }

        } catch (JSONException e){
            e.printStackTrace();
        }
    }

    public void setPlanList(JSONArray jaPlan) {

        Log.i("CaInfo", "SetPlanList is activated...");

        m_alPlan.clear();

        try{
            for(int i =0; i<jaPlan.length(); i++){
                JSONObject joPlan=jaPlan.getJSONObject(i);

                CaPlan plan = new CaPlan();
                plan.m_nSeqPlanElem=joPlan.getInt("seq_plan_elem");
                plan.m_nSeqMeter=joPlan.getInt("seq_meter");
                plan.m_strMid=joPlan.getString("mid");
                plan.m_strMeterDescr=joPlan.getString("meter_descr");
                plan.m_nHourFrom=joPlan.getInt("hour_from");
                plan.m_nHourTo=joPlan.getInt("hour_to");
                plan.m_nPercentToSave=joPlan.getInt("percent_to_save");
                plan.m_dSaveKwh=joPlan.getDouble("save_kwh");
                plan.m_dSaveWon=joPlan.getDouble("save_won");
                plan.m_dKwhRef=joPlan.getDouble("kwh_ref");
                plan.m_dKwhPlan=joPlan.getDouble("kwh_plan");
                plan.m_dKwhReal=joPlan.getDouble("kwh_real");
                plan.m_dWonRef=joPlan.getDouble("won_ref");
                plan.m_dWonPlan=joPlan.getDouble("won_plan");
                plan.m_dWonReal=joPlan.getDouble("won_real");

                JSONArray jaAct = joPlan.getJSONArray("list_act");

                plan.m_alAct = new ArrayList<>();
                for(int j=0; j<jaAct.length(); j++){
                    JSONObject joAct = jaAct.getJSONObject(j);

                    CaAct act = new CaAct();
                    act.m_nSeqAct=joAct.getInt("seq_act");
                    act.m_strActContent=joAct.getString("act_content");

                    JSONArray jaActHistory = joAct.getJSONArray("list_act_history");

                    act.m_alActHistory = new ArrayList<>();
                    for(int k=0; k<jaActHistory.length();k++){
                        JSONObject joActHistory = jaActHistory.getJSONObject(k);

                        CaActHistory actHistory = new CaActHistory();
                        actHistory.m_nSeqActHistory = joActHistory.getInt("seq_act_history");
                        actHistory.m_nSeqAdminBegin = joActHistory.getInt("seq_admin_begin");
                        actHistory.m_nSeqAdminEnd = joActHistory.getInt("seq_admin_end");
                        actHistory.m_dtBegin = parseDate(joActHistory.getString("time_begin"));
                        actHistory.m_dtEnd = parseDate(joActHistory.getString("time_end"));

                        act.m_alActHistory.add(actHistory);
                    }
                    plan.m_alAct.add(act);

                }

                m_alPlan.add(plan);
                Log.i("CaInfo", "alPlan is" + m_alPlan);

            }

        }catch (JSONException e){
            e.printStackTrace();
        }
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

        //m_alNotice.clear();

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

    public static String getDecoPhoneNumber(String src) {
        if (src == null) {
            return "";
        }
        if (src.length() == 8) {
            return src.replaceFirst("^([0-9]{4})([0-9]{4})$", "$1-$2");
        } else if (src.length() == 12) {
            return src.replaceFirst("(^[0-9]{4})([0-9]{4})([0-9]{4})$", "$1-$2-$3");
        }
        return src.replaceFirst("(^02|[0-9]{3})([0-9]{3,4})([0-9]{4})$", "$1-$2-$3");
    }
}
