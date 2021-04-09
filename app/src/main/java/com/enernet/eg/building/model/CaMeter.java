package com.enernet.eg.building.model;

import java.util.ArrayList;

public class CaMeter {

    public int m_nSeqMeter=0;
    public String m_strMid="";
    public String m_strDescr="";
    public double m_dKwhRef=0.0;
    public double m_dKwhPlan=0.0;
    public double m_dKwhReal=0.0;
    public double m_dWonRef=0.0;
    public double m_dWonPlan=0.0;
    public double m_dWonReal=0.0;

    public ArrayList<CaMeterUsage> m_alMeterUsage;
}
