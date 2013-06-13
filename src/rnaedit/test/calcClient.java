/****************************************************************************
 * Module    : calcClient.java
 * Written By: Chris Matthews
 *
 * Disclaimer:
 * THERE ARE NO REPRESENTATIONS OR WARRANTIES ABOUT THE SUITABILITY OF THE
 * SOFTWARE, EITHER EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR
 * PURPOSE, OR NON-INFRINGEMENT. THE DEVELOPER SHALL NOT BE LIABLE FOR
 * ANY DAMAGES SUFFERED BY LICENSEE AS A RESULT OF USING, MODIFYING OR
 * DISTRIBUTING THIS SOFTWARE OR ITS DERIVATIVES.
 ***************************************************************************/
package rnaedit.test;

import java.rmi.Naming;
import java.rmi.RMISecurityManager;

public class calcClient {

    public static void main(String args[]) {
        
        /*on client side, define only abstract class!!*/
        mathCalc cm = null;
        int i = 0;
        System.setSecurityManager(new RMISecurityManager());

        
        try {
        
            /**
             *  "/calcMath" is the binded object name on Server!!!
             * 
             */
            System.out.println("Starting calcClient");
            String url = new String("//" + args[0] + "/calcMath");
            System.out.println("Calc Server Lookup: url =" + url);
            
            
            /**
             * look up for the server - returned a predefined object referenece
             * on the server
             */
            cm = (mathCalc) Naming.lookup(url);
            if (cm != null) {
                String testStr = "Requesting Current Interest Rate...";

                // Print Current Interest Rate from the server
                cm.printRate();

                // Amortize a schedule using the server interest 
                // rate.

                float amount = (float) 10000.50;
                int duration = 36;
                /*call server side code - object returned from server*/
                schedule curschd = cm.amortizeSchedule(amount, duration);

                // Print the schedule
                curschd.print();
            } else {
                System.out.println("Requested Remote object is null.");
            }
        } catch (Exception e) {
            System.out.println("An error occured");
            e.printStackTrace();
            System.out.println(e.getMessage());
        }
    }
}
