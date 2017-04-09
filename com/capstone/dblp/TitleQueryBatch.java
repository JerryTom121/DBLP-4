/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.capstone.dblp;

import java.io.*;
/**
 *
 * @author jhyeh
 */
public class TitleQueryBatch {
    private String path;

    public static void main(String[] args) {
        TitleQueryBatch tqb = new TitleQueryBatch(args[0]);
        tqb.doGenerate();
    }

    public TitleQueryBatch(String str) {
        this.path = str;
    }

    public void doGenerate() {
        String template = "java -classpath SORP.jar com.capstone.sorp.SORPQuery " +
                "sorp.model/ settings.txt sorpvoc.txt ";
        try {
            File dir = new File(this.path);
            String[] flist = dir.list();
            for (int i=0; i<flist.length; i++) {
                if (flist[i].endsWith(".txt")) {
                    System.out.println(template+this.path+"/"+flist[i]);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
