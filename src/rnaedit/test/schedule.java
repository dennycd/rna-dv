/****************************************************************************
 * Module    : schedule.java
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

import java.io.Serializable;

class schedule implements Serializable
{
  float  totalLoanAmt;
  float  usrAmmount;
  float  interestRate;
  int    loanDuration;

  schedule( float rate, float ammount, int duration )
  {
     interestRate = rate;
     usrAmmount   = ammount;
     loanDuration = duration;
     totalLoanAmt = ammount + (ammount / rate);
  }

  void print()
  {
     System.out.println("Schedule Created.");
     System.out.println("Calculation information based on:");
     System.out.println("            Rate        [%" + interestRate + "]" );
     System.out.println("            Ammount     [$" + usrAmmount + "]" );
     System.out.println("            Duration    [ " + loanDuration + "]" );
     System.out.println("            Total Loan  [$" + totalLoanAmt + "]" );

     int   couponNum        = 0;
     float balanceRemaining = totalLoanAmt;
     float monthlyPayment   = 0;

     System.out.println();
     System.out.println( "Payment Monthly Payment Ammount   Balance Remaining"  );
     System.out.println( "------- -----------------------   -----------------" );
     while( balanceRemaining > 0 )
     {
        couponNum++;
        monthlyPayment = totalLoanAmt/loanDuration;
        if( balanceRemaining < monthlyPayment )
        {
          monthlyPayment   = balanceRemaining;
          balanceRemaining = 0;
        } 
        else
        {
          balanceRemaining = balanceRemaining - monthlyPayment;
        }   
    
        System.out.println( couponNum + " " + monthlyPayment + " " + balanceRemaining );
     }
  }
}

