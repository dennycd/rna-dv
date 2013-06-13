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

/**
 * Generator of structural components
 * 
 * Future extesion include specifying the actual sequence content 
 *
 * @author cda18
 */
public class ETGen {

    
   
    /**
     * 
     * Generate multiploop component 
     * 
     * parse in a multiloop definition string ()....()......().....()....
     * 
     * () - the branch 
     * . - one free base
     * 
     * left to right ordering w.r.t. the parent node above the multiloop node
     * 
     * @return
     */
    public ETNode multiloop(String def){
        
        ETNode node = new ETNode(3, ETNode.MULTILOOP,0);
        node.def = def;
      
        return node;
    }
    
 
    /**
     * Generate a stacks strucural component node of size (# of bases)
     * 
     * @param size
     * @return The stacks strucutal component node generated
     */
    public ETNode stacks(int size){
       
        ETNode node = new ETNode(3, ETNode.STACK, size);
        /**
         * 
         * Furture extension includes specifying sequence content e.g. GC ratio
         * 
         */
        return node;
    }
    
    /**
     * Genereate hairpin structure of size
     * 
     * @param size
     * @return
     */
    public ETNode hairpin(int size){
        
        ETNode node = new ETNode(3, ETNode.HAIRPIN, size);
        return node;
    }
    
    /**
     * 
     * Generate innerloop 
     * 
     * @param lsize - size of left chain
     * @param rsize - size of right chain 
     * @return
     */
    public ETNode innerloop(int lsize, int rsize){
        
        //must both not empty
        if( (lsize-2) == 0 || (rsize-2) == 0){
            System.err.println("invalid innerloop specified");
            return null;
        }
        
        ETNode node = new ETNode(3, ETNode.INNERLOOP, 0);
        node.lsize = lsize; node.rsize = rsize;
        
        return node;
    }
 
    /**
     * 
     * Generate bulge node
     * 
     * @param lsize
     * @param rsize
     * @return
     */
    public ETNode bulge(int lsize, int rsize){
        
        //cannot not both emtpy, cannot both have free base
        if( ((lsize-2) == 0 && (rsize-2) == 0 ) ||
                ((lsize-2) != 0 && (rsize-2) != 0)){
            System.err.println("invalid bulge specification");
            return null;
        }
        
        ETNode node = new ETNode(3, ETNode.BULGE,0);
        node.lsize = lsize; node.rsize = rsize;
 
        return node;
    }
    
    /**
     * 
     * Generate free end component node
     * 
     * @param size
     * @return
     */
    public ETNode freeend(int size){
        
        ETNode node = new ETNode(3, ETNode.FREEEND, size);
        return node;
    }
    
    /**
     * 
     * Generate joing component node
     * 
     * @param size
     * @return
     */
    public ETNode joint(int size){
        
        ETNode node = new ETNode(3, ETNode.JOINTS, size);
        return node;
    }
    
    /**
     * Generate free base compoents (within part of the multiloop)
     * @return
     */
    public ETNode freebase(int size){
        
               
        ETNode node = new ETNode(3, ETNode.FREEBASE, size);
        return node;
    }
}




















