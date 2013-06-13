/****************************************************************************
 * Module    : mathCalc.java
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

public interface mathCalc extends java.rmi.Remote
{
   public schedule amortizeSchedule( float ammount, int duration ) throws java.rmi.RemoteException;
   public void     printRate() throws java.rmi.RemoteException;
}
