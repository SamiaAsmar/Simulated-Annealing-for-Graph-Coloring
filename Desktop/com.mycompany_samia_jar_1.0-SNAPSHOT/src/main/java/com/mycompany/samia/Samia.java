/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 */

package com.mycompany.samia;

import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author samiaasmar
 */
public class Samia {

    public static void main(String[] args) {
        shahd s = new shahd();
        loading l = new loading();
        l.setVisible(true);
        
        for (int i = 0 ; i <= 100 ; ++i){
            try{
                Thread.sleep(80);
                l.progressBar.setValue(i);
                if (i%2 == 0){
                    l.pleaseWait.setText("Please Wait..");
                }
                else{
                    l.pleaseWait.setText("Please Wait...");
                }
                if (i == 100){
                    l.setVisible(false);
                    s.setVisible(true);
                }
            } catch (InterruptedException ex) {
                Logger.getLogger(Samia.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        s.setLocationRelativeTo(null);
    }
}
