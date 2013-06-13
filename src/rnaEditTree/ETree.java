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
import rnaedit.ct.CT;
import rnaedit.ct.CT_base;

/**
 * 
 * The edit tree representation for RNA secondary structure 
 *
 * @author cda18
 */
public class ETree {

    private ETNode root = new ETNode(); /*the root of the ETree (has defaulted content 'N')*/
    private int base_key = 0;   /*the key seed*/
    
    
    /**
     * Produce secondary structure feature vector 
     * 
     * [structre]  [size]  [pair-un ratio] [# of stem] [avg stem size] ...
     * 
     * ((..))...  n 
     * 
     * @return feature vector in string format
     */
    public String feature_vector(String ct_name){
        String feature = "";
        
        /**
         * Raw features
         * 
         * may include   max_stack_size , max_multiloop_branch
         */
        
        float stack_n=0,stack_avg_size=0,stack_max_size=0;
        float bulge_n=0,bulge_avg_size=0;
        float inner_n=0,inner_avg_size=0;
        float freeend_n=0,freeend_avg_size=0;
        float hairpin_n=0, hairpin_avg_size=0;
        float multiloop_n=0,multiloop_avg_size=0,multiloop_avg_branch=0,multiloop_max_branch=0;
        
 
        String data = this.recover_structural_component();
        
        String[] data_set = data.split("&");
        for(int i=0;i<data_set.length;i++){
            if(data_set[i].trim().equals(""))
                continue;
            String[]tmp = data_set[i].split(":");
                     
            /*collect statistics*/
            if(tmp[0].trim().equals("stack")){
                stack_n++;
                stack_avg_size += Integer.parseInt(tmp[1].trim());
                /*max stack size*/
                if(Integer.parseInt(tmp[1].trim()) > stack_max_size)
                    stack_max_size = Integer.parseInt(tmp[1].trim());
            }
            else
            if(tmp[0].trim().equals("hairpin")){
                hairpin_n++;
                hairpin_avg_size += Integer.parseInt(tmp[1].trim()); 
            }
            else
            if(tmp[0].trim().equals("innerloop")){
                inner_n++;
                inner_avg_size += Integer.parseInt(tmp[1].trim()) 
                        + Integer.parseInt(tmp[2].trim()); //left & right
            }
            else
            if(tmp[0].trim().equals("bulge")){
                bulge_n++;
                bulge_avg_size += Integer.parseInt(tmp[1].trim()) 
                        + Integer.parseInt(tmp[2].trim());
            }
            else
            if(tmp[0].trim().equals("multiloop")){
                multiloop_n++;
                multiloop_avg_size += Integer.parseInt(tmp[1].trim());
                multiloop_avg_branch += Integer.parseInt(tmp[2].trim());
                /*max branching degree*/
                if(Integer.parseInt(tmp[2].trim()) > multiloop_max_branch)
                    multiloop_max_branch = Integer.parseInt(tmp[2].trim());
            }
            else
            if(tmp[0].trim().equals("freeend")){
                ;
            }
            else
            if(tmp[0].trim().equals("joints")){
                ;
            }
            else
            if(tmp[0].trim().equals("freebase")){
                ;
            }
            else{
                System.err.println("error structural component type...");
            }

        }
        
        /*compute average*/
        if(stack_n>0)
            stack_avg_size = stack_avg_size / stack_n;
        
        if(hairpin_n>0)
            hairpin_avg_size = hairpin_avg_size / hairpin_n;
        
        if(inner_n>0)
            inner_avg_size = inner_avg_size / inner_n; 
        else
            inner_avg_size = 0;
        
        if(bulge_n>0)
            bulge_avg_size = bulge_avg_size / bulge_n;
        else
            bulge_avg_size = 0;
        
        if(multiloop_n>0){
            multiloop_avg_size = multiloop_avg_size / multiloop_n;
            multiloop_avg_branch = multiloop_avg_branch / multiloop_n;
        }
        else{
            multiloop_avg_size = 0;
            multiloop_avg_branch = 0;
        }
            
        /*compute pair/unpair ratio*/
        
        /*have to first reexpand the tree into edit distance tree*/
        this.expand();
        /*obtain SS in dot bracket format*/
        String tmp = this.recover_structure();
        int unpair = 0, pair = 0;
        for(int i=0;i<tmp.length();i++){
            if(tmp.charAt(i)=='.')
                unpair++;
            else
            if(tmp.charAt(i)=='(' || tmp.charAt(i)==')')
                pair++;
            else
                System.err.println("warning...unknow symbol in structure bracket string...");
        }
        float pair_ratio = (float)pair/(float)unpair;
        float structure_size = tmp.length();
        
        /*assemble the feature vector for this strucutre instance*/
        feature = 
                ct_name + "\t" +
                String.format("%.1f", structure_size) + "\t" +
                String.format("%.1f", pair_ratio) + "\t" +
                
                String.format("%.1f", stack_n) + "\t" +
                String.format("%.1f", stack_avg_size) + "\t" +
                String.format("%.1f", stack_max_size) + "\t" +
                
                String.format("%.1f", hairpin_n) + "\t" +
                String.format("%.1f", hairpin_avg_size) + "\t" +
                
                String.format("%.1f", inner_n) + "\t" +
                String.format("%.1f", inner_avg_size) + "\t" +
                
                String.format("%.1f", bulge_n) + "\t" +
                String.format("%.1f", bulge_avg_size) + "\t" +
                
                String.format("%.1f", multiloop_n) + "\t" +
                String.format("%.1f", multiloop_avg_size) + "\t" +
                String.format("%.1f", multiloop_avg_branch) + "\t" +
                String.format("%.1f", multiloop_max_branch) + "\t" +
                tmp
                ;
                
        return feature;
    }
    
    
    /**
     * Generate random secondary structure component tree
     * 
     * @param n - Size of the structure w.r.t. number of nucleotide bases
     */
    public void random_build_tree(int n){
        
        
    }
    
    /**
     * 
     * Recursively build the subtree under parent node
     * 
     * @param parent
     */
    private void recursive_build_tree(ETNode parent){
        
    }
    
    
    /**
     * Produce next unique key
     * 
     * @return
     */
    private int next_key(){
        
        return base_key++;
    }
    
    /**
     * Reset the key 
     * @return
     */
    private int reset_key(){
        
        base_key = 0;
        return base_key;
    }
    
    /**
     * Given key value, search for corresponding node in the tree
     * 
     * @param key
     * @return The tree node having the corresponding key value
     */
    public ETNode search_node(int key){
        
        return recursive_search(root,key);
        
    }
    
    /**
     * check parent node whether it matches the key, otherwise descent
     * 
     * @param parent
     * @param key
     * @return The node found that matches the key - null otherwise;
     */
    private ETNode recursive_search(ETNode parent,int key){
        
        if(parent.KEY == key)
            return parent;
        
        for(int i=0;i<parent.child.size();i++){
            ETNode tmp = recursive_search(parent.child(i),key);
            if(tmp!=null)
                return tmp;
        }
        
        return null;
    }
    
    
    
    /**
     * Delete the corresponding substructure in the tree
     * 
     * @param key
     * @return
     */
    public ETNode delete(int key){
        
        return null;
    }
    
    
    
    /*
     * Parse in secondary structure in CT format and populate the whole 
     * ETree
     * 
     * @param structure The RNA secondary structure in CT format
     */
    public void parse(CT seq){    
        /*a depth first recursion throug the bases*/
        this.recursive_parse(seq, root, 0, seq.sequence.size()-1);
    }
        
    /**
     * Recursive parsing the CT and build up the subtree under root
     * 
     * @param seq - the whole sequence 
     * @param parent - The parent ETNode
     * @param start - The start base position in the parsing
     * @param end - The ending base position in the parsing
     * @return The current base position during the parsing
     */
    private void recursive_parse(CT seq, ETNode parent, int start, int end){
        int cur = start; 
               
        while(cur <= end){
            CT_base tmp = (CT_base)seq.sequence.get(cur);
            
            /*unpaired base - create leaf child and continue to parse*/
            if(tmp.pair==0){
                /*create child ETNode*/
                ETNode child = new ETNode(2, tmp.neo.charAt(0), tmp.index);
                /*associate to parent*/
                parent.append_child(child); child.link_parent(parent);
                cur++;
            }
            
            /*pairing base - create pair child and invoke recursion, continue upon the other end of the pairs*/
            else
            {
                CT_base tmp_r = (CT_base)seq.sequence.get(tmp.pair-1); /*get the pairing CT*/
                
                /*check pseudoknot condition*/
                if(tmp.index > tmp_r.index){
                    System.out.println("pseudoknot detected...ignoring");
                    return;
                }
                
                /*create paring child ETNode*/
                ETNode child = new ETNode(1, tmp.neo.charAt(0), tmp_r.neo.charAt(0), 
                        tmp.index, tmp_r.index);
                /*associate to parent ETNode*/
                parent.append_child(child); child.link_parent(parent);
                /*recursive building for child*/
                this.recursive_parse(seq, child, cur+1, tmp_r.index-2);
                /*update the position pointer*/
                cur = tmp_r.index;                
            }
        }
     
        /*now all child nodes are created, appended to parent and also properly built*/
    }
    
    /**
     * Automatic layout of the secondary structure 
     * in a plane graph
     * 
     */
    public String automatic_layout(){
        
        /*determine root position*/
        double delta_d = 10;      
        root.X_pos = 0; root.Y_pos = root.child.size() * delta_d;
        
        /*recursion*/
        return "("+root.X_pos+","+root.Y_pos+")" + this.recursive_layout(root, 0,0);
    }
    
    /**
     * Recursive travers the tree and compute touchgraph layout position for 
     * each individual base
     * 
     * @param parent  The parent et node.
     * @param center_X,center_Y  The reference centre of the current circle 
     * @return Produce a string chain of positions (X_pos,Y_pos)(X_pos,Y_pos)... 
     */
    private String recursive_layout(ETNode parent, double center_X, double center_Y){
        
        double delta_d = 10;
        String result = "";
        /**
         * all child node will line up, left to right, in a circle round 
         * beginning at the position of the root node 
         */
                
        /*number of childs*/
        int n = parent.child.size();
        /*degree interval for each node*/
        double interval = 360/(n+1);   
        double radius = Math.sqrt(
                Math.pow((double)Math.abs(parent.X_pos-center_X), 2) 
                + Math.pow((double)Math.abs(parent.Y_pos-center_Y), 2)
                );
        
        /*travese through each child and build up the ETree*/
        for(int i=0;i<n;i++){
            
            ETNode child = (ETNode)parent.child.get(i);
            
            
            if(child.isLeaf()){
                /*centered at center_X,center_Y,with angle decided by i & interval */           
                double angle = (i+1) * interval;
                child.X_pos = parent.X_pos - Math.sin(angle)*radius;
                child.Y_pos = parent.Y_pos - (radius - child.X_pos/Math.tan(angle));
                result += "(" + (int)child.X_pos + "," + (int)child.Y_pos +")";
            }
            else
            if(child.isInternal()){
               
                /*produce two pairing position*/ 
                double angle = (i+1)*interval - interval/2;
                double leftX = child.X_pos = parent.X_pos - Math.sin(angle)*radius;
                double leftY = child.Y_pos = parent.Y_pos - (radius - leftX/Math.tan(angle));
                
                angle = (i+1)*interval + interval/2;
                double rightX = parent.X_pos - Math.sin(angle)*radius;
                double rightY = parent.Y_pos - (radius - rightX/Math.tan(angle));
                
                /*compute a center for the next layer*/
                angle = (i+1) * interval;
                int m = child.child.size();
                double new_radius = radius + m * delta_d; 
                double new_center_X = center_X - Math.sin(angle)*new_radius;
                double new_center_Y = center_Y - (new_radius - new_center_X/Math.tan(angle));
                
                /*recurse down to next layer*/
                result +=
                        "(" + (int)leftX + "," + (int)leftY + ")" +
                        recursive_layout(child,new_center_X,new_center_Y) +
                        "(" + (int)rightX + "," + (int)rightY + ")";
            }
        }
        
        return result;
    }
    
    
    
        /**
     * Recursive travese the ETree to recover structure and sequence
     * 
     * @param parent - The parent ETNode
     * @type - Data type  1 - sequence 2 - structure 3 - substructure label 4 - structural component data
                        5 - key value
     * @return the substring represented by the forest under the parent ETNode
     */
    private String recursive_traverse(ETNode parent, int type){
       
        String seq_data = "";/*the subsequence data*/
        String struct_data = "";/*the secondary structure data*/
        String label_data = ""; /*the substructure label*/
        String component_data = ""; /*the structural component data*/
        String key_data = ""; /*the unique component key data*/
        
        /*number of childs*/
        int n = parent.child.size();
        
        /*travese through each child and build up the ETree*/
        for(int i=0;i<n;i++){
            
            ETNode child = (ETNode)parent.child.get(i);
            if(child.isLeaf()){
                seq_data += String.valueOf(child.content);
                struct_data += ".";
                label_data += String.valueOf(child.label);
                component_data += child.print(); //System.out.print(child.print());
                key_data += " " + String.valueOf(child.KEY) + " ";
            }
            else
            if(child.isInternal()){
               
                switch(type){
                    case 1:
                        seq_data += String.valueOf(child.pairs[0])
                            + recursive_traverse(child,type) 
                            + String.valueOf(child.pairs[1]);  
                        break;
                    case 2:
                        struct_data += "(" + recursive_traverse(child,type) + ")";
                        break;
                    case 3:
                        label_data += String.valueOf(child.label) 
                            + recursive_traverse(child,type)
                            + String.valueOf(child.label);
                        break;      
                    case 4:
                        component_data += child.print()
                                + recursive_traverse(child,type);
                        //System.out.print(child.print());
                        break;
                    case 5:
                        key_data += " " + String.valueOf(child.KEY) + " "
                                + recursive_traverse(child, type);
                        break;
                }
                             
                
                
            }
            else{
                System.err.println("invalid tree node ....");
                return "";
            }
            
        }
 
        /*now the forest under the current parent are recovered */
        if(type == 1)
            return seq_data;
        else
        if(type == 2)
            return struct_data;
        else
        if(type == 3)
            return label_data;
        else
        if(type == 4)
            return component_data;
        else
            return key_data;
    }
    
    /**
     * Recover the sequence stored in ordered ETree 
     * 
     * @return
     */
    public String recover_sequence(){
        /*recursive depth first loop to print out sequence*/
        return this.recursive_traverse(root,1);
    }
    
    /**
     * Recover the secondary structure in dot bracket format 
     * 
     * @return 
     */
    public String recover_structure(){      
        /*recursive depth first loop to print out structure*/
        return this.recursive_traverse(root,2);   
    }
    
    /**
     * Recover the secondary structure in dot bracket format 
     * 
     * @return 
     */
    public String recover_component_label(){      
        /*recursive depth first loop to print out component label signs*/
        return this.recursive_traverse(root,3);   
    }
    
    /**
     * Recover the structure component chain after tree collapse
     * 
     * @return
     */
    public String recover_structural_component(){
        
        return this.recursive_traverse(root, 4);
    }
         
    
    public String recover_key_chain(){
        
        return this.recursive_traverse(root, 5);
    }
         
    /**
     * Decompose the tree and detect structure components
     * 
     * A two steps processing 
     * 
     * 1. identify the components and label all the nodes
     * 2. collapse the tree into component trees by collapse components into
     *    single component node 
     * 
     */
    public void decompose(){
        
        recursive_detection(root);
        
    }
    
    /**
     * Collapse the tree into component tree
     * 
     */
    public void collapse(){
        
        reset_key(); //initialize key generator
        recursive_collapse(root); //transform into component tree
        
    }
    
    /**
     * Reexpand the component tree into a full edit structure tree
     * 
     */
    public void expand(){
 
        recursive_expand(root);
    }
    
    
    
    /**
     * This method is still kind of messy to use, though
     * 
     * 
     * recursive traverse through the component tree and expand at each 
     * component node into a full node set representing that component
     * 
     * 
     * @param parent - The current component node to expand upon
     */
    private void recursive_expand(ETNode parent){
        
        /**
         * Expand under root node - careful operation here
         * 
         */
        if(parent.parent == null){
            Vector children = (Vector)parent.child.clone();
            for(int i=0;i<children.size();i++){
                ETNode child = (ETNode)children.get(i);
                recursive_expand(child);
            }
        }
        /**
         * Expand the stack node by links a stacks of new intenal nodes
         * 
         */
        else
        if(parent.label == ETNode.STACK){
            ETNode child = parent.child(0); //hold on the child component node
            int n = (parent.size -2)/2; //number of pairs (stacks)
            parent.child.clear();
            
            ETNode cur = parent;
            for(int i=0;i<n-1;i++){
                //internal ET node
                ETNode tmp = new ETNode(1,'1','1',0,0);
                cur.child.add(tmp); tmp.parent = cur;
                cur = tmp;
            }
            /*attach back the next level child component*/
            cur.child.add(child); child.parent = cur;
            /*recursive descend on the child component*/
            recursive_expand(child);
        }
        /**
         * 
         * Expand on the hairpin component
         * 
         */
        else
        if(parent.label == ETNode.HAIRPIN){           
            int n = parent.size - 2; //number of free base (leaf childs) under parent
            parent.child.clear();;
            for(int i=0;i<n;i++){
                //add leaf node as hairpin free base
                ETNode tmp = new ETNode(2,'2',0);
                parent.child.add(tmp); tmp.parent = parent;
            }
        }
        /**
         * Expand on the inner loop component 
         * 
         * 
         */
        else
        if(parent.label == ETNode.INNERLOOP || parent.label == ETNode.BULGE){

            //hold the exit child node pointer
            ETNode exit_child = parent.child(0);
            //0 position is the exit node, insert leaf node before and after it 
            //left side leaf node
            for(int i=0;i<parent.lsize-2;i++){
                ETNode tmp = new ETNode(2,'3',0);
                parent.child.add(0, tmp); tmp.parent = parent;
            }
            //right side leaf node
            for(int i=0;i<parent.rsize-2;i++){
                ETNode tmp = new ETNode(2,'3',0); tmp.parent = parent;
                parent.child.add(tmp);
            }
            /*recursive descend on exit child node*/
            recursive_expand(exit_child);
        }
        /**
         * Expand on free end
         * 
         */
        else
        if(parent.label == ETNode.FREEEND || parent.label == ETNode.JOINTS){
            ETNode root = parent.parent; assert(root == this.root);  //obtain root node
            int n = root.child.size();
            int parent_pos = -1;
            //locate the position of current parent node
            for(int i=0;i<n;i++){
                ETNode tmp = root.child(i);
                if(parent == tmp){
                    parent_pos = i;
                    break;
                }
            }
            //expand the free ends (add at current component pos)
            for(int i=0;i<parent.size-1;i++){
                ETNode tmp = new ETNode(2,'6',0);
                root.child.add(parent_pos, tmp); tmp.parent = root;
            }
        }
        /**
         * 
         * 
         */
        else
        if(parent.label == ETNode.FREEBASE){
            ETNode ancester = parent.parent; //obtain up level parent
            int n = ancester.child.size();
            int parent_pos = -1;
            //locate the position of current parent node
            for(int i=0;i<n;i++){
                ETNode tmp = ancester.child(i);
                if(parent == tmp){
                    parent_pos = i;
                    break;
                }
            }
            //expand the free ends (add at current component pos)
            for(int i=0;i<parent.size-1;i++){
                ETNode tmp = new ETNode(2,'8',0);
                ancester.child.add(parent_pos, tmp); tmp.parent = ancester;
            }           
        }       
        /**
         * 
         * Expand on multiple loops
         * 
         * already properly joint, do nothing
         * 
         */
        else
        if(parent.label == ETNode.MULTILOOP){
            /*recursion on each child node*/
            Vector children = (Vector)parent.child.clone();//maintain the original child reference
            for(int i=0;i<children.size();i++){
                ETNode child = (ETNode)children.get(i);
                recursive_expand(child);
            }
        }
        
    }
    
    
    /**
     * recursive traverse through the tree and collapse tree nodes into 
     * component nodes - populate component node with appropriate key value
     * 
     * 
     * we preserve the original tree structure, but only cluster nodes belonging
     * to the sample component
     * 
     * original sequence content is discarded
     * 
     * @param parent
     */
    private void recursive_collapse(ETNode parent){
        
        /*
         * base on current parent label, collapse parent node
         * together with child node into one component
         *
         */       

        /*id the parent node*/
        parent.KEY = next_key();
        
        /**
         * Stacks parent, join all consecutive stack childs to form a single stem
         * 
         */
        if(parent.label == ETNode.STACK){
            ETNode child = parent.child(0);   
            /*recursion into the one single child node*/
            recursive_collapse(child);
            /**
             * if child also stack, shall remove the child
             * and join the parent node 
             */
            if(child.label == ETNode.STACK){
                parent.child.clear();
                parent.child = child.child; 
                parent.size = 2 + child.size;
                
                /*redirect the child's uplink to parent ()*/
                ETNode tmp = parent.child(0);
                tmp.parent = parent;
            }
            /**
             * if child not stack, the stem ends here, count 
             * as 1 stack containing two bases
             */
            else
                parent.size = 4;             
        }
        
        /**
         * 
         * Hairpin, join all leaf children to form a hairpin component on parent
         * 
         */
        else
        if(parent.label == ETNode.HAIRPIN){
            /*closing pairs + inner free bases*/
            parent.size = parent.child.size() + 2;
            /*clear all leaf free base nodes*/
            parent.child.clear();
        }
        
        /**
         * Innerloop and/or Bulge, record left & right chain length
         * 
         */
        else
        if(parent.label == ETNode.INNERLOOP || parent.label == ETNode.BULGE){
            int n = parent.child.size();
            int lsize = 0, rsize = 0;
            int exit_pos = -1;
            for(int i=0;i<n;i++){
                ETNode child = parent.child(i);
                if(child.isInternal())
                    exit_pos = i;   //exit pairs position
                else
                if(child.isLeaf()){
                    if(exit_pos == -1)
                        lsize++;    //still left of exit
                    else
                        rsize++;    //right of exit pairs
                }
            }
            /*left & chain + the enter & exit pair base itself*/
            parent.lsize = lsize + 2; parent.rsize = rsize + 2;
            
            /*clean all leaf node, descend on exit child*/
            ETNode child = parent.child(exit_pos);
            parent.child.clear();
            /*recursive descend on exit_child node*/
            recursive_collapse(child);
            parent.child.add(child); child.parent = parent;           
        }
        /**
         * 
         * Multiloop, collapse consecutive free bases into FREEBASE component
         *            remain other internal node braches
         * 
         */
        else
        if(parent.label == ETNode.MULTILOOP){
            Vector children = parent.child;
            parent.child = new Vector();
            int n = children.size();
            int count = 0;
            for(int i=0;i<n;i++){
                ETNode child = (ETNode)children.get(i);
                if(child.isLeaf())
                    count++;
                else
                if(child.isInternal()){
                    /*add a new FREEBASE node to parent*/
                    if(count>0){
                        ETNode tmp = new ETNode(3, ETNode.FREEBASE,count);
                        tmp.KEY = next_key();
                        parent.child.add(tmp); tmp.parent = parent;
                    }
                    
                    /*recursion descend on the internal child node*/
                    recursive_collapse(child);
                    /*re-add the internal child node to parent*/
                    parent.child.add(child); child.parent = parent;
                    /*reset freebase counter*/
                    count = 0; 
                }
            }
            /*add a new FREEBASE node to parent*/
            if(count>0){
                ETNode tmp = new ETNode(3, ETNode.FREEBASE,count);
                tmp.KEY = next_key();
                parent.child.add(tmp); tmp.parent = parent;
            }
        }
        
        /**
         * Free end and joints under the root node
         */
        else
        if(parent.parent == null){
            Vector children = new Vector();
            int n = parent.child.size();
            int free_end_count = 0, joint_count = 0;
            for(int i=0;i<n;i++){
                ETNode child = parent.child(i);
                if(child.label == ETNode.FREEEND)
                    free_end_count++;
                else
                if(child.label == ETNode.JOINTS)
                    joint_count++;
                else
                if(child.isInternal()){
                    /*left free end exist, collapse into one node*/
                    if(free_end_count > 0){
                        ETNode tmp = new ETNode(3, ETNode.FREEEND,free_end_count);
                        tmp.KEY = next_key();
                        children.add(tmp); tmp.parent = parent;
                    }
                    if(joint_count > 0){
                        ETNode tmp = new ETNode(3, ETNode.JOINTS,joint_count);
                        tmp.KEY = next_key();
                        children.add(tmp); tmp.parent = parent;
                    }
                     
                    /*recursive descent on the child node*/
                    recursive_collapse(child);
                    /*re-add child node*/
                    children.add(child); child.parent = parent;
                    /*reset counter*/
                    free_end_count = 0; joint_count = 0;
                }
            }
            /*right free end*/
            if(free_end_count > 0){
                ETNode tmp = new ETNode(3, ETNode.FREEEND,free_end_count);
                tmp.KEY = next_key();
                children.add(tmp); tmp.parent = parent;
            }
            /*renew the parent node*/
            parent.child.clear();;
            parent.child = children;
            
        }
                   
         parent.type = 3; //transform parent node into structural component node
    }
    
            
    /**
     * Recursive traverse the tree and detect & label structure component
     * 
     * @param parent - The parent ETNode
     * @return the substring represented by the forest under the parent ETNode
     */
    private void recursive_detection(ETNode parent){
       
    
        /*number of childs*/
        int n = parent.child.size();       
        /*traverse through each child and build up the ETree*/
        for(int i=0;i<n;i++){
            ETNode child = (ETNode)parent.child.get(i); 
            int type = detect_type(child);
            if(type != -1)
                child.label = type;
            else
                child.label = child.parent.label;
            /*populate corresponding labels over its leaf child node*/
            /*int m = child.child.size();
            for(int j=0;j<m;j++){
                ETNode offspring = (ETNode)child.child(j);
                if(offspring.isLeaf())
                    offspring.label = child.label;
            }
            */
            
            /*recursion down to its child nodes*/
            if(child.isInternal())
                recursive_detection(child);
        }
 
    }
    
    /**
     * Detect the component type for the parent node together with its 
     * direct children 
     * 
     * Now interal nodes shall all be labeled since they are parent nodes for the 
     *  component node sets (w.r.t. its childs)
     * 
     * leaf nodes that are not free end & joints will not be labeled which belongs
     * to free bases of a particular structure component (but will be collapsed 
     * correspondingly
     * 
     * @param parent The parent node
     * @return
     */
    private int detect_type(ETNode parent){
        int type = -1;
        
        /*detect stacks (two consecutive pairs forming it)
         *
         * - parent is internal node
         * - parent contains 1 child only
         * - the child is an internal node
         * 
         */
        if(parent.isInternal())
            if(parent.child.size()==1){
                ETNode child = parent.child(0);
                if(child.isInternal()){
                    type = ETNode.STACK;
                    return type;
                }
            }
        
        /**
         * detect hairpin loops
         * 
         * - parent is internal node
         * - parent contains only leaf nodes (all childs are leaf)
         * 
         */
        if(parent.isInternal())
            if(parent.child.size()>=1){
                boolean isHairpin = true;
                int n = parent.child.size();
                for(int i=0;i<n;i++){
                    ETNode child = parent.child(i);
                    if(!child.isLeaf()){
                        isHairpin = false;
                        break;
                    }
                }
                if(isHairpin){
                    type = ETNode.HAIRPIN;
                    return type;
                }
            }
        
        /**
         * detect internal loop
         * 
         * - parent is internal node
         * - parent contains 1 and only 1 internal child node
         * - at least 1 leaf node on either side of that internal child node
         */
        if(parent.isInternal())
            if(parent.child.size()>=3){ /*one internal child, two leaf node at least*/
                int n = parent.child.size();
                int internal_count = 0;int internal_pos = -1;
                for(int i=0;i<n;i++){
                    ETNode child = parent.child(i);
                    if(child.isInternal()){
                        internal_count++;
                        internal_pos = i;
                    }                
                }
                if(internal_count == 1){
                    if(internal_pos >0 && internal_pos < n-1){ //middle of childs
                        type = ETNode.INNERLOOP;
                        return type;
                    }
                }
            }
        
        /**
         * detect bulges 
         * 
         * - parent is internal node
         * - parent contains 1 and only 1 internal child node
         * - one side of the internal node child is empty, other side contains
         *   at least 1 leaf node
         * 
         */
        if(parent.isInternal())
            if(parent.child.size()>=2){ /*one internal child, one leaf node at least*/
                int n = parent.child.size();
                int internal_count = 0;int internal_pos = -1;
                for(int i=0;i<n;i++){
                    ETNode child = parent.child(i);
                    if(child.isInternal()){
                        internal_count++;
                        internal_pos = i;
                    }                
                }
                if(internal_count == 1)
                    if(internal_pos == 0 || internal_pos == n-1){ //one side has no leaf 
                        type = ETNode.BULGE;                    //since child size >=2, then another size
                        return type;                            //has leaf
                    }                       
            }
        
        /**
         * detect multiloop
         * 
         * - parent is internal node
         * - has at least 2 internal child nodes
         */
        if(parent.isInternal())
            if(parent.child.size()>=2){ /*two child nodes at least*/
                int n = parent.child.size();
                int internal_count = 0;
                for(int i=0;i<n;i++){
                    ETNode child = parent.child(i);
                    if(child.isInternal())
                        internal_count++;             
                }
                if(internal_count >= 2){
                    type = ETNode.MULTILOOP;
                    return type;
                }
            }
        
        /**
         * detect free end
         * 
         * - leaf node itself
         * - parent's parent is the root (direct child of root)
         * - either left (or right) side has no internal nodes
         * 
         */
        
        /**
         * detect joints (unpaired free bases inbetween)
         * 
         * - leaf node itself
         * - direct child of root
         * - lies in between internal nodes (at least one internal node at both side)
         */
        
        if(parent.isLeaf())
            if(parent.parent.parent == null){ //child of root (root has no parent)
                ETNode elder = parent.parent;
                int n = elder.child.size();
                
                int parent_pos = -1;
                int first_internal_pos = -1;
                int last_internal_pos = -1;
                
                for(int i=0;i<n;i++){
                    ETNode child = elder.child(i);
                    if(child.equals(parent))    //parent position itself
                        parent_pos = i;
                    if(child.isInternal()){
                        last_internal_pos = i;    //always record the very last internal pos    
                        if(first_internal_pos == -1)
                            first_internal_pos = i; //record the first encountered internal node
                    }
                }
                
                if(parent_pos < first_internal_pos || parent_pos > last_internal_pos){
                    type = ETNode.FREEEND;
                    return type;
                }
                else
                if(parent_pos > first_internal_pos && parent_pos < last_internal_pos){
                    type = ETNode.JOINTS;
                    return type;
                }              
            }
        
        /*a leaf node that is part of the substructure yet not labeled by default*/
        //System.err.println("unknown structure detected....");
        return type;
    }
    
}









