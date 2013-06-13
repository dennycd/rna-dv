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


package rnaedit.ct;

/**
 * A class representing individual bases
 */
public class CT_base{
    public int index = 0; //index of the base
    public String neo = ""; //RNA nucleotide over { A | C | G | U }
    public int pair = 0; // the index of the nucleotide paired with it. 0 indicates non-paired base.
    
    /**
     * Touch graph initial position of the base
     */
    public double pos_X = -1; 
    public double pos_Y = -1;
    
    /**
     * Set initial touch graph position of X and Y
     * 
     * @param x  X position in the drawing pane
     * @param y  Y position
     */
    public void set_pos(int x,int y){
        
        pos_X = x;
        pos_Y = y;
    }
    
    /*create a new base instance*/
    public CT_base(int idx, String value, int par){
        index = idx;
        neo = value;
        pair = par; 
    }
    
}