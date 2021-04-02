package com.enernet.eg.building.model;

import java.sql.Array;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class CaPlan {

    public int m_nSeqPlanElem=0;
    public int m_nSeqMeter=0;
    public String m_strMid="";
    public String m_strMeterDescr="";
    public boolean m_bTop=false;
    public int m_nHourFrom=1;
    public int m_nHourTo=1;
    public int m_nPercentToSave=0;
    public double m_dSaveKwh=0.0;
    public double m_dSaveWon=0.0;
    public double m_dKwhRef=0.0;
    public double m_dKwhPlan=0.0;
    public double m_dKwhReal=0.0;
    public double m_dWonRef=0.0;
    public double m_dWonPlan=0.0;
    public double m_dWonReal=0.0;

    public ArrayList<CaAct> m_alAct;
}
