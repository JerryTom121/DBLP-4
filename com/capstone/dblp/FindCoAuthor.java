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
public class FindCoAuthor {
    private String name;
    private HashMap authormap, dblpmap;
    private double threshold;

    public class ASObject implements Comparable, java.io.Serializable {
        public String label;
        public double cosine;

        public ASObject(String s, double d) {
            this.label = s;
            this.cosine = d;
        }

        public int compareTo(Object obj) {
            ASObject other = (ASObject)obj;
            if (this.cosine > other.cosine) return -1;
            else if (this.cosine < other.cosine) return 1;
            else return 0;
        }
    }

    public static void main(String[] args) {
        String args0 = args[0].replace('_', ' ');
        double d = Double.parseDouble(args[3]);
        FindCoAuthor fc = new FindCoAuthor(args0, args[1], args[2], d);
        fc.doFind();
    }

    public FindCoAuthor(String name, String afname, String dblpname, double d) {
        this.name = name;
        this.threshold = d;
        try {
            ObjectInputStream ois = new ObjectInputStream(
                                             new FileInputStream(afname));
            this.authormap = (HashMap)ois.readObject();
            ois.close();
            ObjectInputStream ois2 = new ObjectInputStream(
                                             new FileInputStream(dblpname));
            this.dblpmap = (HashMap)ois2.readObject();
            ois2.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void doFind() {
        double[] vec1 = (double[])authormap.get(name);
        if (vec1 == null) {
            System.out.println("No topic similarity for primary author = "+name);
            return;
        }
        ArrayList al = new ArrayList();
        for (Iterator it=authormap.keySet().iterator(); it.hasNext(); ) {
            String name2 = (String)it.next();
            if (name.equals(name2))
                continue;
            double[] vec2 = (double[])authormap.get(name2);
            if (vec2 == null) {
                System.out.println("No topic similarity for name = "+name);
                continue;
            }
            double d = cosine(vec1, vec2);
            ASObject as = new ASObject(name2, d);
            al.add(as);
        }
        Object[] array = al.toArray();
        Arrays.sort(array);
        outputResult(array);
    }

    public void outputResult(Object[] array) {
        int[] range = getPublicationRange(name);
        System.out.println("Find target: "+this.name+"("+range[0]+"-"+range[1]+
                        ")");
        int count = 0;
        for (int i=0; i<array.length; i++) {
            ASObject as = (ASObject)array[i];
            if (as.cosine < threshold)
                break;
            if (isCoAuthor(name, as.label)) {
                range = getPublicationRange(as.label);
                System.out.println("Name = "+as.label+"("+range[0]+"-"+range[1]+
                        "), similarity = "+as.cosine);
                count++;
            }
        }
        System.out.println("Total "+count+" possible community member exists."+
                "(threshold = "+threshold+")");

    }

    private int[] getPublicationRange(String name) {
        int min = 3000;
        int max = -1;
        for (Iterator it=dblpmap.keySet().iterator(); it.hasNext(); ) {
            String key = (String)it.next();
            DBLPObject dblp = (DBLPObject)dblpmap.get(key);
            ArrayList authors = dblp.getAuthors();
            if (authors.contains(name)) {
                int year = dblp.getYear();
                if (year > max) max = year;
                if (year < min) min = year;
            }
        }
        int[] range = new int[2];
        range[0] = min; range[1] = max;
        return range;
    }

    private boolean isCoAuthor(String name1, String name2) {
        for (Iterator it=dblpmap.keySet().iterator(); it.hasNext(); ) {
            String key = (String)it.next();
            DBLPObject dblp = (DBLPObject)dblpmap.get(key);
            ArrayList authors = dblp.getAuthors();
            if (authors.contains(name1) && authors.contains(name2))
                return true;
        }
        return false;
    }

    private double cosine(double[] d1, double[] d2) {
        double ip;
        double norm1, norm2;
        norm1 = norm2 = ip = 0.0;
        for (int i=0; i<d1.length; i++) {
            ip += d1[i]*d2[i];
            norm1 += d1[i]*d1[i];
            norm2 += d2[i]*d2[i];
        }
        norm1 = Math.sqrt(norm1);
        norm2 = Math.sqrt(norm2);
        return ip/(norm1*norm2);
    }





    
}
