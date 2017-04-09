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
public class TitleDocument {
    private HashMap dblpmap;

    public static void main(String[] args) {
        TitleDocument td = new TitleDocument(args[0]);
        td.doGenerateFiles();
    }

    public TitleDocument(String str) {
        try {
            ObjectInputStream ois = new ObjectInputStream(new FileInputStream(str));
            dblpmap = (HashMap)ois.readObject();
            ois.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void doGenerateFiles() {
        for (Iterator it=dblpmap.keySet().iterator(); it.hasNext(); ) {
            String key = (String)it.next();
            DBLPObject obj = (DBLPObject)dblpmap.get(key);
            String title = obj.getTitle();
            String booktitle = obj.getBookTitle();
            //String fname = "titles/"+key.replace('/', '-')+".txt";
            String fname = "titles/"+key+".txt";
            try {
                PrintWriter pw = new PrintWriter(new FileWriter(fname));
                pw.println(title);
                //pw.println(booktitle);
                pw.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
