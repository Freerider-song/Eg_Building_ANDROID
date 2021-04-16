package com.enernet.eg.building.model;

import java.text.SimpleDateFormat;
import java.util.Date;

public class CaAlarm {

    public int m_nSeqAlarm=0;
    public int m_nSeqSavePlanElem=0;
    public int m_nAlarmType=0;
    public String m_strTitle="";
    public String m_strContent="";
    public Date m_dtCreated=null;
    public Date m_dtRead=null;
    public boolean m_bRead=false;

    public boolean m_bReadStateChanged=false;

    public String getTimeCreated() {
        SimpleDateFormat df=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return df.format(m_dtCreated);
    }
}
