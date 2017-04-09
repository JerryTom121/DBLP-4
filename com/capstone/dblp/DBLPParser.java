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
public class DBLPParser {
    private String fname;
    private HashMap map;

    public static void main(String[] args) {
        DBLPParser dblp = new DBLPParser(args[0]);
        dblp.doParse();
    }

    public DBLPParser(String str) {
        this.fname = str;
        this.map = new HashMap();
    }

    private String extractValue(String line, String tag) {
        String tagStart = "<"+tag+">";
        String tagEnd = "</"+tag+">";
        int begin = tagStart.length();
        int end = line.indexOf(tagEnd);
        return line.substring(begin, end);
    }

    private DBLPObject parseRecord(ArrayList al) {
        DBLPObject rec = new DBLPObject();
        for (Iterator it=al.iterator(); it.hasNext(); ) {
            String line = (String)it.next();
            if (line.startsWith("<author>")) {
                String str = extractValue(line, "author");
                rec.addAuthor(str);
            }
            else if (line.startsWith("<title>")) {
                String str = extractValue(line, "title");
                rec.setTitle(str);
            }
            else if (line.startsWith("<year>")) {
                String str = extractValue(line, "year");
                rec.setYear(Integer.parseInt(str));
            }
            else if (line.startsWith("<booktitle>")) {
                String str = extractValue(line, "booktitle");
                rec.setBookTitle(str);
            }
            else if (line.startsWith("<cite>")) {
                String str = extractValue(line, "cite");
                rec.addCitation(str);
            }
        }
        return rec;
    }

    private String extractKey(String line) {
        String tagStart = "key=\"";
        String tagEnd = "\">";
        int begin = line.indexOf(tagStart)+tagStart.length();
        int end = line.indexOf(tagEnd);
        String key = line.substring(begin, end).replace('/', '-');
        return key;
    }

    private void saveRecords(HashMap map) {
        String outfname = System.currentTimeMillis()+".dblp.map";
        try {
            ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(outfname));
            oos.writeObject(map);
            oos.close();
        } catch(Exception e) {
            e.printStackTrace();
        }
        System.out.println("Map file "+fname+" saved.");
    }

    public void doParse() {
        try {
            BufferedReader br = new BufferedReader(new FileReader(this.fname));
            String line="";
            //int count = 0;
            boolean inrecord = false;
            ArrayList al = null;
            String key = "";
            while ((line=br.readLine()) != null) {
                //count++;
                if (line.startsWith("<incollection") ||
                        line.startsWith("<inproceedings")) {
                    key = extractKey(line);
                    al = new ArrayList();
                    inrecord = true;
                }
                else if (line.startsWith("</incollection>") ||
                        line.startsWith("</inproceedings>")) {
                    inrecord = false;
                    DBLPObject dbo = parseRecord(al);
                    dbo.setKey(key);
                    System.out.println("Record "+dbo.getKey()+" processed.");
                    map.put(dbo.getKey(), dbo);
                }
                else {
                    if (inrecord)
                        al.add(line);
                }
            }
            br.close();
            System.out.println("Total "+map.size()+" incollection records read.");
            saveRecords(map);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
