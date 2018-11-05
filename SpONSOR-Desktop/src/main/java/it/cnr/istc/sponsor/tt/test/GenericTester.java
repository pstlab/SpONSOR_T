/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.cnr.istc.sponsor.tt.test;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

/**
 *
 * @author Luca Coraci <luca.coraci@istc.cnr.it> ISTC-CNR
 */
public class GenericTester {

    public static void main(String[] args) {
        GregorianCalendar gc = new GregorianCalendar();
        gc.setTime(new Date());
        
        System.out.println("START DATE: "+gc);
        for (int i = 0; i < 12; i++) {
            gc.add(GregorianCalendar.WEEK_OF_YEAR, 1);
            System.out.println("NEW DATE: "+gc.getTime());
        }
    }

}
