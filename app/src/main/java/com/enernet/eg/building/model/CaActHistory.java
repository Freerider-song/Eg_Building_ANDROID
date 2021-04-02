package com.enernet.eg.building.model;

import java.text.SimpleDateFormat;
import java.util.Date;

public class CaActHistory {

    public int m_nSeqActHistory=0;
    public int m_nSeqAdminBegin=0;
    public int m_nSeqAdminEnd=0;
    public Date m_dtBegin = null;
    public Date m_dtEnd = null;

    public String getTimeBegin() {
        SimpleDateFormat df=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return df.format(m_dtBegin);
    }

    public String getTimeEnd() {
        SimpleDateFormat df=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return df.format(m_dtEnd);
    }
}
