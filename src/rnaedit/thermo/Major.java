/********************************************************************************************
*  RNA-DV Version 1.0
*  Copyright (c)  2008 September  Denny Chen Dai, Herbert H. Tsang.
*  Permission is granted to copy, distribute and/or modify this document
*  under the terms of the GNU Free Documentation License, Version 1.2
*  or any later version published by the Free Software Foundation;
*  with no Invariant Sections, no Front-Cover Texts, and no Back-Cover
*  Texts.  A copy of the license is included in the section entitled "GNU
*  Free Documentation License".
*********************************************************************************************/


/*
 * Major.java
 *
 * Hydrogen bond model - Major model 
 *
 *  the energy value attributed to each base pairs can be made proportional
 *  to the approximate relative strength of the canonical base pairs
 *  
 * E(s) = sum of { e(r_i, r_j )  }  
 * 
 * e(G,C) = -3 kcal/mol
 * e(A,U) = -2
 * e(G,U) = -1 
 * e(x, x) = 0 
 *
 * Assuming temperature at 37 degree
 */
package rnaedit.thermo;
import rnaedit.ct.CT;
import rnaedit.ct.CT_base;

/**
 * @author Denny Chen Dai
 * A Thermodynamic function model 
 * Read in a CT class then returns the energy under Major model 
 */
public class Major {
    
    /*pairing energy*/
    private static int E_GC = -3; /*GC pairing energy*/
    private static int E_AU = -2; /*AU pairing energy*/
    private static int E_GU = -1; /*GU pairing energy*/
    private static int E_XX = 0;  /*others*/ 
    
    public Major() {
    }
    
    /*
     * load in CT file and then evaluate the fitness value under Major model 
     */
    public int fitness(CT rna){
        int fitness = 0;
        /*scan through each base and compute the accumulated fitness*/
        for(int i=0;i<rna.sequence.size();i++)
        {
            CT_base base = (CT_base)rna.sequence.get(i);
            /*load the paired base*/
            CT_base pair_base = ( base.pair == 0 ? null : (CT_base)rna.sequence.get(base.pair - 1));
            /*add pairing energy*/
            if(pair_base != null)
            {
                if(base.neo.equals("G") && pair_base.neo.equals("C") )
                    fitness += E_GC; 
                else
                if(base.neo.equals("A") && pair_base.neo.equals("U") )
                    fitness += E_AU; 
                else
                if(base.neo.equals("G") && pair_base.neo.equals("U") )
                    fitness += E_GU; 
                else
                    fitness += E_XX; 
            }
        }
        return fitness; 
    }
    
}



