package com.enernet.eg.building;

import android.graphics.Bitmap;
import android.util.Pair;

import java.util.ArrayList;
import java.util.Formatter;
import java.util.List;

public class CaArg {

    public String command;
    public List<Pair<String, String>> args = null;
    public Bitmap bitmap = null;
    public ArrayList<Bitmap> bitmapList = new ArrayList<>();
    public byte[] fileData = null;

    public CaArg(final String strCommand, final String[] cmdArgs, final byte[] fileData) {
        this.command=new Formatter().format(strCommand, (Object[])cmdArgs).toString();

        this.args = new ArrayList<>();
        this.fileData = fileData;
    }

    public void addArg(String strKey, String strValue)
    {
        args.add(new Pair<>(strKey, strValue));
    }

    public void addArg(String strKey, int nValue)
    {
        args.add(new Pair<>(strKey, Integer.toString(nValue)));
    }

    public void addArg(String strKey, double dValue)
    {
        args.add(new Pair<>(strKey, Double.toString(dValue)));
    }

    public void addArg(String strKey, boolean bValue)
    {
        args.add(new Pair<>(strKey, bValue ? "1" : "0"));
    }

    public void addArg(String strKey, ArrayList<Integer> alValue){
        args.add(new Pair<>(strKey, alValue.toString().replace("[","").replace("]","")));
    }
}
