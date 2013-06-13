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


package rnaEditTree;

import java.util.Vector;

/**
 * Node representation of the Secondary structure edit distance tree
 * 
 * @author cda18
 */
public class ETNode {
    
    /*tree layout position*/
    public double X_pos = -1;
    public double Y_pos = -1;
    
    
        
    /*
     * the label of the node indicating its substructure type
     * 
     * Using one of the following 8 types
     */
    public int label = -1;
    
    /* (for node type {leaf | internal})
     * structural element label - indicating which component this node, together
     * with its direct child node will form
     *
     * 1 - stack
     * 2 - hairpin
     * 3 - inner loop
     * 4 - bulge
     * 5 - multi-loop
     * 6 - free end
     * 7 - joints 
     */ 
    public static int STACK = 1; 
    public static int HAIRPIN = 2;
    public static int INNERLOOP = 3;
    public static int BULGE = 4;
    public static int MULTILOOP = 5;
    public static int FREEEND = 6;
    public static int JOINTS = 7;    
    /*extra type - for structureal component node*/
    public static int FREEBASE = 8; /*unpaired bases within multiloop*/
    
    
    /*tree structure information*/
    public Vector child = new Vector(); /*child ETNode array (from left to right in order)*/
    public ETNode parent = null; /*reference to parent ETNode*/
    public int KEY = -1; /*unique ID for this tree node*/
    
    /*ETNode content*/
    int type = -1; /*ETNode type   1: internal ETNode for base pairs  
                    *              2: leaf ETNode for unpaired base 
                    *              3: structural component node 
                    */   
 
    
    
    /*content for type 2 - leaf node content*/
    public char content = 'N'; /* ACGU content for unpaired base*/
    int pos = -1; /*base position in the sequence (for unpaired base)*/
    
    /*content for type 1 - internal node content*/
    public char[] pairs = null; /*base pairs content [0] left base [1] is right base*/
    int[] pair_pos = null; /*base pairs position in the original sequence*/
    
    /*content for type 3 - structual component node*/
    /*multiloop definition string if the node is multiloop component type
     *
     * ()....()......().....()....
     * 
     * () - branch
     * . - one free base
     *  
     */
    public String def = "";
    
    /**
     * the base numbers of the particular structure (for STEM, HAIRPIN, FREEEND, JOINTS, FREEBASE)
     * 
     * for stems - # of pairs is half of its size
     * for hairpin -  size-2 is the number of freebase inside
     * 
     * 
     */
    public int size = 0; 
    public int lsize = 0, rsize = 0; //left chain & right chain size (for INNERLOOP & BULGE)
     
    /**
     * display the node information
     * 
     * @return
     */
    public String print(){
        String out = "";      
            switch(label){
                case 1:
                    out = " stack:" + size + " & "; break;
                case 2: 
                    out = " hairpin:" + size + " & "; break;
                case 3: 
                    out = " innerloop:" + lsize + ":" + rsize + " & "; break;
                case 4: 
                    out = " bulge:" + lsize + ":" + rsize + " & "; break;
                case 5:
                    
                    int branch_count = 0, loop_size=0;
                    for(int i=0;i<child.size();i++){
                        ETNode tmp = this.child(i);
                        if(tmp.label != ETNode.FREEBASE){
                            branch_count++;
                            loop_size += 2; //a base pair attached to the loop
                        }
                        else
                            loop_size += tmp.size;   //freeend                                         
                    }
                    out = " multiloop: " + loop_size + ":" + branch_count + " & "; 
                    break;
                case 6:
                    out = " freeend:" + size + " & "; break;
                case 7:
                    out = " joints:" + size + " & "; break;
                case 8:
                    out = " freebase:" + size + " & "; break;
            }
        return out;
    }
    
    public boolean isInternal(){
        
        return (child.size()!=0);
        //return (type == 1);
    }
    
    public boolean isLeaf(){
        
        return (child.size()==0);
        //return (type == 2);
    }
   
   
    /**
     * Link current ETNode to its uplink parent 
     * 
     * @param pr the reference to the parent
     */
    public void link_parent(ETNode pr){
        parent = pr;
    }
    
    /*append child to the current child array (as right most child)
     *
     * @return total number of childs in current ETNode
     */
    public int append_child(ETNode ch){
        child.add(ch);
        return child.size();
    }
    
    
    /*return the ith child ETNode of this ETNode*/
    public ETNode child(int index){
    
        if(index >= child.size() || index < 0)
            return null;
        
        return (ETNode)child.get(index);
        
    }
    
    
    /*defaulted to be an empty ETNode*/
    public ETNode(){
     
    }
    
    /**
     * initialize a component node
     * 
     * @param tp  Component type = 3
     * @param label The component type label 
     * @param size the size of the component (# of bases)
     */
    public ETNode(int tp, int lbl, int sz){
        
        if(tp != 3)
            System.err.println("error node type");
        
        type = tp;
        label = lbl;
        size = sz;
        
    }
    
    /*initialize leaf ETNode     
     * 
     * @param type - ETNode type should be 2 for leaf ETNode
     * @param data - base content
     * @param index - base index in the original sequence
     */
    public ETNode(int tp, char data, int index){
    
        if(tp != 2)
            System.err.println("error node type");
        
        type = tp;
        content = data;
        pos = index; 
        
    }
    
    /*initialize internal ETNode
     *
     * 
     * @param type - ndoe type should be 1 for pairing ETNode 
     * @param leftc, rightc  - pairing base content 
     * @param posl, posr - pairing base position in the original sequence 
     */
    public ETNode(int tp, char leftc, char rightc, int posl, int posr){
        
        if(tp != 1)
            System.err.println("error node type");
        
        type = tp;
        pairs = new char[2]; 
        pairs[0] = leftc; pairs[1] = rightc;
        pair_pos = new int[2]; 
        pair_pos[0] = posl; pair_pos[1] = posr; 
        
        
    }


}












