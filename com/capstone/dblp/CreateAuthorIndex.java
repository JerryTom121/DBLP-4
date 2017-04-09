/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.capstone.dblp;

import java.io.*;
import java.util.*;
/**
 *
 * @author jhyeh
 */
public class CreateAuthorIndex {
    private int year_start, year_end;
    private String mapname;
    private HashMap titlemap;
    private HashMap dblpmap;
    private HashMap authormap;
    private String outfname;

    public static void main(String[] args) {
        int ys, ye;
        ys = Integer.parseInt(args[2]);
        if (args[3] != null) ye = Integer.parseInt(args[3]);
        else ye = ys+3;
        CreateAuthorIndex frc = new CreateAuthorIndex(args[0],
                                                           args[1], ys, ye);
        frc.doCreate();
    }

    public CreateAuthorIndex(String map, String titleidx, int ys, int ye) {
        this.year_start = ys;
        this.year_end = ye;
        this.mapname = map;
        this.authormap = new HashMap();
        this.outfname = ""+ys+"-"+ye+".authors.map";
        try {
            ObjectInputStream ois = new ObjectInputStream(
                                             new FileInputStream(titleidx));
            this.titlemap = (HashMap)ois.readObject();
            ois.close();
            ois = new ObjectInputStream(new FileInputStream(mapname));
            this.dblpmap = (HashMap)ois.readObject();
            ois.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private double[] addVec(double[] v1, double[] v2) {
        if (v1 == null) {
            if (v2 != null) return v2;
            else return null;
        }
        if (v2 == null) {
            if (v1 != null) return v1;
            else return null;
        }
        int length = (v1.length<v2.length)?v1.length:v2.length;
        double[] result = new double[length];
        for (int i=0; i<length; i++) {
            result[i] = v1[i] + v2[i];
        }
        return result;
    }

    public void doCreate() {
        FindRecentPapers frp = new FindRecentPapers(mapname, year_start, year_end);
        Object[] keylist = frp.doFindKey();
        for (int i=0; i<keylist.length; i++) {
            if ((i%100 == 0) && (i>0)) {
                System.out.println("Document "+i);
            }
            String key2 = (String)keylist[i]+".txt";
            System.out.println("Fetching title: "+key2);
            double[] vec = (double[])titlemap.get(key2);
            if (vec == null) System.out.println(key2+" not exists in title map.");
            DBLPObject dblpobj = (DBLPObject)dblpmap.get(keylist[i]);
            ArrayList aal = dblpobj.getAuthors();
            for (Iterator it=aal.iterator(); it.hasNext(); ) {
                String author = (String)it.next();
                double[] vec2 = (double[])authormap.get(author);
                double[] newvec = addVec(vec, vec2);
                if (newvec != null) authormap.put(author, newvec);
            }
        }
        try {
            ObjectOutputStream oos = new ObjectOutputStream(
                                           new FileOutputStream(outfname));
            oos.writeObject(authormap);
            oos.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
