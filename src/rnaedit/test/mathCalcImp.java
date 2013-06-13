/****************************************************************************
 * Module    : mathCalcImp.java
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

import java.rmi.server.UnicastRemoteObject;

class mathCalcImp extends UnicastRemoteObject implements mathCalc
{
   float interestRate = (float)7.5;

   mathCalcImp() throws java.rmi.RemoteException
   {
   }

   public schedule amortizeSchedule( float ammount, int duration ) throws java.rmi.RemoteException
   {
      System.out.println("Amortizeing Schedule.");
      return( new schedule( interestRate, ammount, duration ) );
   }    

   public void printRate() throws java.rmi.RemoteException
   {
      System.out.println("Current Interest Rate is " + interestRate );
   }
}




