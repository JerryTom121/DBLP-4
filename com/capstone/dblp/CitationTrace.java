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
public class CitationTrace {
    private HashMap dblpmap;

    public static void main(String[] args) {
        CitationTrace ct = new CitationTrace(args[0]);
        ct.doTrace();
    }

    public CitationTrace(String str) {
        try {
            ObjectInputStream ois = new ObjectInputStream(new FileInputStream(str));
            dblpmap = (HashMap)ois.readObject();
            ois.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void doTrace() {
        for (Iterator it=dblpmap.keySet().iterator(); it.hasNext(); ) {
            String key = (String)it.next();
            doTraceRecursion(key, 0);
        }
    }

    public void doTraceRecursion(String key, int level) {
        DBLPObject dbo = (DBLPObject)dblpmap.get(key);
        for (int i=0; i<level; i++) System.out.print(" ");
        if (dbo == null) {
            System.out.println(key+" is a deadend");
            return;
        }
        else {
            System.out.println(key);
        }
        for (Iterator it=dbo.getCitations().iterator(); it.hasNext(); ) {
            String citeid = (String)it.next();
            if (citeid.equals("...")) continue;
            doTraceRecursion(citeid, level+1);
        }        
    }
}
