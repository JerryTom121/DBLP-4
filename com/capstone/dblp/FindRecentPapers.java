/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.capstone.dblp;

import java.util.*;
import java.io.*;
/**
 *
 * @author jhyeh
 */
public class FindRecentPapers {
    private HashMap dblpmap;
    private int year_start, year_end;

    public static void main(String[] args) {
        // arguments: index map, year_start, year_end
        FindRecentPapers fra = new FindRecentPapers(args[0], args[1], args[2]);
        fra.doFindTitle();
    }

    public FindRecentPapers(String str, int ys, int ye) {
        this.year_start = ys;
        this.year_end = ye;
        try {
            ObjectInputStream ois = new ObjectInputStream(new FileInputStream(str));
            this.dblpmap = (HashMap)ois.readObject();
            ois.close();
        } catch (Exception e) {
            e.printStackTrace();
        }       
    }

    public FindRecentPapers(String str, String str2, String str3) {
        this(str, Integer.parseInt(str2), Integer.parseInt(str3));
    }

    public Object[] doFindKey() {
        TreeSet papers = new TreeSet();
        for (Iterator it=dblpmap.keySet().iterator(); it.hasNext(); ) {
            String key = (String)it.next();
            DBLPObject obj = (DBLPObject)dblpmap.get(key);
            if (obj != null) {
                int year_pub = obj.getYear();
                if ((year_pub >= year_start) && (year_pub <= year_end)) {
                    papers.add(key);
                }
            }
        }
        return papers.toArray();
    }

    public Object[] doFindTitle() {
        TreeSet papers = new TreeSet();
        for (Iterator it=dblpmap.keySet().iterator(); it.hasNext(); ) {
            String key = (String)it.next();
            DBLPObject obj = (DBLPObject)dblpmap.get(key);
            if (obj != null) {
                int year_pub = obj.getYear();
                if ((year_pub >= year_start) && (year_pub <= year_end)) {
                    papers.add(obj.getTitle());
                }
            }
        }
        int count = 0;
        for (Iterator it2=papers.iterator(); it2.hasNext(); ) {
            System.out.println("Paper: "+it2.next());
            count++;
        }
        System.out.println("Total "+count+" papers found between "+
                           this.year_start+" and "+this.year_end);
        return papers.toArray();
    }
}
