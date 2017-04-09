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
public class FindBooks {
    private String name;
    private HashMap authormap, dblpmap, bookmap;
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
        double d = Double.parseDouble(args[4]);
        FindBooks fc = new FindBooks(args0, args[1], args[2], args[3], d);
        fc.doFind();
    }

    public FindBooks(String name, String afname, String bookname,
                     String dblpname, double d) {
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
            ObjectInputStream ois3 = new ObjectInputStream(
                                             new FileInputStream(bookname));
            this.bookmap = (HashMap)ois3.readObject();
            ois3.close();
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
        for (Iterator it=bookmap.keySet().iterator(); it.hasNext(); ) {
            String bname = (String)it.next();
            double[] vec2 = (double[])bookmap.get(bname);
            if (vec2 == null) {
                System.out.println("No topic similarity for book = "+bname);
                continue;
            }
            double d = cosine(vec1, vec2);
            ASObject as = new ASObject(bname, d);
            al.add(as);
        }
        Object[] array = al.toArray();
        Arrays.sort(array);
        outputResult(array);
    }

    public void outputResult(Object[] array) {
        System.out.println("Find target: "+this.name);
        int count = 0;
        for (int i=0; i<array.length; i++) {
            ASObject as = (ASObject)array[i];
            if (as.cosine < threshold)
                break;
            //if (isCoAuthor(name, as.label)) {
                if (checkPublished(name, as.label))
                    System.out.println("Book = "+as.label+
                            "(published), similarity = "+as.cosine);
                else
                    System.out.println("Book = "+as.label+
                            "(not yet published), similarity = "+as.cosine);
                count++;
            //}
        }
        System.out.println("Total "+count+" possible books exists."+
                "(threshold = "+threshold+")");

    }

    private boolean checkPublished(String author, String book) {
        for (Iterator it=dblpmap.keySet().iterator(); it.hasNext(); ) {
            String key = (String)it.next();
            DBLPObject dblp = (DBLPObject)dblpmap.get(key);
            if (dblp.getAuthors().contains(author) &&
                    dblp.getBookTitle().equals(book)) {
                return true;
            }
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
