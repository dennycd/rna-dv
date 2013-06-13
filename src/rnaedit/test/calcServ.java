/****************************************************************************
 * Module    : calcServ.java
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


public class calcServ
{
    public static void main( String args[] )
    {
       System.setSecurityManager( new RMISecurityManager());

       try
       {
          System.out.println("Starting calcServer");
          mathCalcImp cm = new mathCalcImp();

          
          /*
           * binding the object with a name
           * client will use the name to fetch the object 
           * 
           *
           * Client will invoke specific methods on this object
           */
          System.out.println("Binding Server");
          Naming.rebind("calcMath", cm );
          System.out.println("Server is waiting");
       }
       catch( Exception e )
       {
          System.out.println("An error occured");
          e.printStackTrace();
          System.out.println(e.getMessage());
       }
    }
}




