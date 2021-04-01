package com.enernet.eg.building.model;

import java.text.SimpleDateFormat;
import java.util.Date;

public class CaNotice {

    public int m_nSeqNotice=0;
    public String m_strTitle="";
    public String m_strContent="";
    public boolean m_bTop=false;
    public int m_nWriterType=1;
    public int m_nTargetType=1;

    public Date m_dtCreated=null;
    public Date m_dtRead=null;
    public boolean m_bRead=true;

    public boolean m_bReadStateChanged=false;

    public String getTimeCreated() {
        SimpleDateFormat df=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return df.format(m_dtCreated);
    }
}
