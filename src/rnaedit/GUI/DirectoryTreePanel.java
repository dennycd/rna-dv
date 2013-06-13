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

package rnaedit.GUI;

import com.touchgraph.graphlayout.TGException;
import com.touchgraph.linkbrowser.TGLinkBrowser;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FilenameFilter;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.DefaultListModel;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;
import javax.swing.event.TreeModelListener;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreeModel;
import javax.swing.tree.TreePath;
import rnaedit.ct.readCT;
import rnaedit.ct.writeCT;
import rnaedit.tgxml.TGConverter;

/**
 *
 * @author  cda18
 */
public class DirectoryTreePanel extends javax.swing.JPanel implements Runnable {

    private TGLinkBrowser tg = null; // the referenece to TG browser object
    //private DefaultMutableTreeNode root = null;
    private String dir_escape = "";
    private DefaultListModel lmodel = null;
    private FileTreeModel model = null;
    private boolean winOS = false;
    public JPopupMenu popMenu = null; //load all valid file under a directory
    public JPopupMenu popMenuShow = null; //popup for load and display a ct
    private static String RNALIB = "RnaDVLib";
    private String hostname = "";
    
    
    
    private boolean appletCheck(){
                      
        boolean isApplet;
        SecurityManager s = System.getSecurityManager();
        if(s != null){
            try{
                s.checkAccess(Thread.currentThread());
                isApplet = true;
                System.out.println("security check succeed....");
            }
            catch(SecurityException e ){
                isApplet = true;
                System.out.println("security check exception....");
            }
        }
        else{
            System.out.println("security handler is null....");
            isApplet = false;
        }
      
        if(isApplet)
            System.out.println("Running as web applet....");
        else
            System.out.println("Running as standalone app....");
        
        return isApplet;
    }
    

    /** Creates new form DirectoryTreePanel */
    public DirectoryTreePanel() {

        initDirectoryTree();
        
        
        /*stop processing the tree if running in applet mode*/
        //if(appletCheck())
        //    return;
        
        initComponents();
        jList1.validate();
        
        
  

        
       
        
        /*expand the tree to current working directory*/
        String curDir = System.getProperty("user.dir");
        if((new File(RNALIB + File.separator + "ct")).exists())
            curDir = curDir + File.separator + RNALIB + File.separator + "ct" + dir_escape;
        /*create path list*/
        //String[] pt = this.create_path_list(curDir);
        String[] pt = null;
        if(winOS){
            pt = this.create_path_list(curDir);
            pt[0] = pt[0] + "\\";
        }
        else{
            pt = curDir.split(File.separator);
        
        /*get machine name*/
        try {
            hostname = InetAddress.getLocalHost().getHostName();
        } catch (UnknownHostException ex) {
            Logger.getLogger(FileTreeModel.class.getName()).log(Level.SEVERE, null, ex);
        }
        pt[0] = hostname;
        }
        
        Object root = model.getRoot();
        /*build tree path*/
        Object parent = root;
        
        
        jTree1.setSelectionRow(1);
        TreePath tp = jTree1.getSelectionPath();
        tp = tp.getParentPath();
        TreePath curPath = null;
         
        int i = winOS ? 0:1;
        for(;i<pt.length;i++){
            
            /*locate the right child*/
            Object child = null;
            int n = model.getChildCount(parent);
            int j=0;
            for(j=0;j<n;j++){
                child = model.getChild(parent, j);
                if(child.toString().equals(pt[i]))                  
                    break;
            }
            /*descend over it */     
            curPath = tp.pathByAddingChild(child);
            tp = curPath;
            parent = child;
            jTree1.fireTreeExpanded(tp);
            jTree1.setSelectionPath(tp);
        }
        
       jTree1.scrollPathToVisible(tp);
       this.loadDirectoryFiles();
        
    }
    

    private String[] create_path_list(String curDir){
        
        //System.out.println("---create_path_list---");
         
        String cell = "";      
        ArrayList al = new ArrayList();
        
        
        char escape = dir_escape.charAt(0);     
        System.out.println(curDir);
        //System.out.printf("seperator is %s \n", escape);
        
        for(int i=0;i<curDir.length();i++){
            char tmp = curDir.charAt(i);
            if(tmp != escape)
                cell = cell + String.valueOf(tmp);
            else{
                //System.out.println("adding entry " + cell);
                al.add(cell);
                cell = "";
            }
        }
        String[] pt = new String[al.size()];
        for(int i=0;i<al.size();i++){
            pt[i] = (String)al.get(i);
            //System.out.printf("pt[%d] is %s\n",i,pt[i]);
        }

        return pt;
    }
    
    /**
     *  Recursive expand the tree according to curernt working directory
     * 
     * @param parent
     * @param path
     */
    private void recursive_expand(TreePath parent, String[] path){
        /*expand on parent*/
        jTree1.expandPath(parent);
       
        String child = path[0];
        
        /*locate next child*/
        Enumeration children = jTree1.getExpandedDescendants(parent);
        int i=0;
        TreePath tp = null;
        while(children.hasMoreElements()){
            tp = (TreePath)children.nextElement();
            String cur = tp.toString();
            cur = cur.substring(1, cur.length()-1);
            if(cur.equals(child))
                break;
        }
        
        if(tp!=null)
            recursive_expand(tp, path);
        
    }
    
   

    public void run() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    /**
     * initialization of the thermomodel panel 
     * 
     * Must pass the CT object to this Model Panel so that it updates accordingly
     * 
     */
    public void SetTouchGraph(TGLinkBrowser src) {
        this.tg = src; //pass in the reference to the TGLinkBrowser    
    }

    /*initialize the directory tree to current program folder*/
    private void initDirectoryTree() {

        String os = System.getProperty("os.name");
        if (os.startsWith("Windows")) {
            winOS = true;
            dir_escape = "\\";
        } else {
            winOS = false;
            dir_escape = "/";
        }

        //create a tree model
        model = new FileTreeModel();
        lmodel = new DefaultListModel();


        /*initialize popup menu for loading files into JList*/
        popMenu = new JPopupMenu();
        JMenuItem menuItem;
        menuItem = new JMenuItem("Expand");
        ActionListener editNodeAction = new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                /*load all valide files*/
                loadDirectoryFiles();
                /*expand under it*/
                TreePath treepath = jTree1.getSelectionPath();
                jTree1.expandPath(treepath);
            }
        };

        menuItem.addActionListener(editNodeAction);
        popMenu.add(menuItem);

        /*initialize popup menu for loading file for display in touchgraph*/
        popMenuShow = new JPopupMenu();
        JMenuItem menuItem2;
        menuItem2 = new JMenuItem("Load");
        ActionListener editNodeAction2 = new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                int index = jList1.getSelectedIndex();
                if (index == -1) {
                    return;
                /*now load the selected CT and update touchgraph*/
                }
                TreeFile target_ct = (TreeFile) lmodel.get(index);
                updateTouchGraph(target_ct.getAbsolutePath());
            }
        };

        menuItem2.addActionListener(editNodeAction2);
        popMenuShow.add(menuItem2);
        

    }

    
    public void loadDirectoryFiles() {

        /*capture selection event*/
        //TreePath treepath = jTree1.getPathForLocation(evt.getX(), evt.getY());
        TreePath treepath = jTree1.getSelectionPath();

        if (treepath == null) {
            return;
        /*load files into JList*/
        }
        String full_path = "";
        String tp = treepath.toString();
        String[] path = tp.substring(1, tp.length() - 1).split(",");
        path[0] = "";
        for (int i = 0; i < path.length; i++) {
            full_path = full_path + path[i].trim() + dir_escape;
        /*obtain all file list under expanding directory*/
        }
        TreeFile cur = new TreeFile(full_path);
        CustomizedFilter ff = new CustomizedFilter();
        ff.dirToggle(false);
        ff.addFilter("ct");
        ff.addFilter("rnaml");
        ff.addFilter("dp");
        File[] tmpL = cur.listFiles((FilenameFilter) ff);
        TreeFile[] fileL = new TreeFile[tmpL.length];
        for (int i = 0; i < fileL.length; i++) {
            fileL[i] = new TreeFile(tmpL[i].getAbsolutePath());
        }

        //File[] fileL = cur.listFiles();

        if (fileL == null) {
            return;
        /*refresh JList*/
        }
        lmodel.clear();
        for (int i = 0; i < fileL.length; i++) {
            lmodel.addElement(fileL[i]);
        }
    }

    /** 
     *  Recursive construction of all tree node hiararchy
     * 
     *  Small routine that will make node out of the first entry
     *  in the array, then make nodes out of subsequent entries
     *  and make them child nodes of the first one. The process is
     *  repeated recursively for entries that are arrays.
     * 
     * 
     * 
     * @param hierarchy object array of strings - very first string of the array
     *        represent the parent node name 
     */
    private DefaultMutableTreeNode processHierarchy(Object[] hierarchy) {
        DefaultMutableTreeNode node = new DefaultMutableTreeNode(hierarchy[0]);
        DefaultMutableTreeNode child;
        for (int i = 1; i < hierarchy.length; i++) {
            Object nodeSpecifier = hierarchy[i];
            if (nodeSpecifier instanceof Object[]) // Ie node with children
            {
                child = processHierarchy((Object[]) nodeSpecifier);
            } else {
                child = new DefaultMutableTreeNode(nodeSpecifier); // Ie Leaf
            }
            node.add(child);
        }
        return (node);
    }

    /**
     * Use structure_box and sequence_box content 
     * to update TouchGraph 
     * 
     * @param filename The filename to load in 
     */
    private void updateTouchGraph(final String filename) {

        /*read either CT or RNAML or DP files*/
        int index = filename.lastIndexOf('.');
        if (index == -1) {
            return;
        }


        final String ext = filename.substring(index + 1);
        /*update rna_ct*/
        final TGLinkBrowser tgp = this.tg;

        new Thread() {

            @Override
            public void run() {
                /*load the structure file */
                tgp.stauts = TGLinkBrowser.TG_LOAD_FILE;

                if (ext.equals("ct")) {
                    tgp.rna_ct = (new readCT()).parseCT(filename);
                } else if (ext.equals("rnaml")) {
                    tgp.rna_ct = (new readCT()).parseRNAML(filename);
                } else if (ext.equals("dp")) {
                    tgp.rna_ct = (new readCT()).parseDP(filename);
                } else {
                    return;
                /*update temporary CT files*/
                }
                tgp.rna_ct.ct_file = filename + ".tmp";
                (new writeCT()).write(tgp.rna_ct, tgp.rna_ct.ct_file);

                /*repopulate XML touch graph file*/
                tgp.stauts = TGLinkBrowser.TG_GEN_XML;
                //String xmlFileName = tgp.rna_ct.ct_file + ".xml"; /*the converted xml file name*/
                String xmlFileName = filename + ".xml"; /*the converted xml file name*/  
                
              
                if (!(new File(xmlFileName)).exists()) {    //check existing xml 
                    TGConverter converter = new TGConverter();
                    converter.convert(tgp.rna_ct, xmlFileName,tg.viewOpt);       
                }
                else{
                    System.out.println("loading existing xml file...");
                }
                                 
                tgp.tgPanel.clearAll();
                try {
                    tgp.xmlio.read(xmlFileName, new RestoreExactGraph());
                } catch (Exception ex) {
                    ex.printStackTrace();
                }

                tgp.stauts = TGLinkBrowser.TG_DISPLAY;
            }
        }.start();

    }

    /** A thread executed after reading from an XML file, that 
     * sets the visibleLocale to the nodes makred as visible.
     */
    private class RestoreExactGraph extends Thread {

        public void run() {
            try {
                tg.tgPanel.updateLocalityFromVisibility();
            } catch (TGException ex) {
                ex.printStackTrace();
            }

            tg.tgPanel.clearSelect();
            tg.tgPanel.fireResetEvent();
            tg.restoreSavedParameters(tg.xmlio.getParameterHash());
        }
    }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jScrollPane1 = new javax.swing.JScrollPane();
        jTree1 = new javax.swing.JTree(model);
        jScrollPane2 = new javax.swing.JScrollPane();
        jList1 = new javax.swing.JList(lmodel);

        jTree1.setModel(model);
        jTree1.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jTree1MouseClicked(evt);
            }
            public void mousePressed(java.awt.event.MouseEvent evt) {
                jTree1MousePressed(evt);
            }
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                jTree1MouseReleased(evt);
            }
        });
        jTree1.addTreeExpansionListener(new javax.swing.event.TreeExpansionListener() {
            public void treeCollapsed(javax.swing.event.TreeExpansionEvent evt) {
            }
            public void treeExpanded(javax.swing.event.TreeExpansionEvent evt) {
                jTree1TreeExpanded(evt);
            }
        });
        jTree1.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                jTree1KeyTyped(evt);
            }
        });
        jScrollPane1.setViewportView(jTree1);

        jList1.setModel(new javax.swing.AbstractListModel() {
            String[] strings = { "Item 1", "Item 2", "Item 3", "Item 4", "Item 5" };
            public int getSize() { return strings.length; }
            public Object getElementAt(int i) { return strings[i]; }
        });
        jList1.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jList1MouseClicked(evt);
            }
            public void mousePressed(java.awt.event.MouseEvent evt) {
                jList1MousePressed(evt);
            }
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                jList1MouseReleased(evt);
            }
        });
        jList1.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                jList1KeyTyped(evt);
            }
        });
        jScrollPane2.setViewportView(jList1);
        jList1.setModel(lmodel);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane1)
            .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 79, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 261, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane2))
        );
    }// </editor-fold>//GEN-END:initComponents

private void jTree1TreeExpanded(javax.swing.event.TreeExpansionEvent evt) {//GEN-FIRST:event_jTree1TreeExpanded
}//GEN-LAST:event_jTree1TreeExpanded

private void jTree1MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jTree1MouseClicked


    if (evt.getClickCount() < 2) {
        return;
    }
    this.loadDirectoryFiles();

}//GEN-LAST:event_jTree1MouseClicked

private void jList1MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jList1MouseClicked
// TODO add your handling code here:
    int index = jList1.getSelectedIndex();
    if (index == -1 || evt.getClickCount() < 2) {
        return;
    /*now load the selected CT and update touchgraph*/
    }
    TreeFile target_ct = (TreeFile) lmodel.get(index);
    updateTouchGraph(target_ct.getAbsolutePath());


}//GEN-LAST:event_jList1MouseClicked

private void jTree1MousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jTree1MousePressed

    // TODO add your handling code here:  
    if (evt.isPopupTrigger()) {
        TreePath tp = jTree1.getPathForLocation(evt.getX(), evt.getY());
        if (tp != null) {
            jTree1.setSelectionPath(tp);
            this.popMenu.show(evt.getComponent(), evt.getX(), evt.getY());
        }
    }

}//GEN-LAST:event_jTree1MousePressed

private void jTree1MouseReleased(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jTree1MouseReleased
// TODO add your handling code here:
    if (evt.isPopupTrigger()) {
        TreePath tp = jTree1.getPathForLocation(evt.getX(), evt.getY());
        if (tp != null) {
            jTree1.setSelectionPath(tp);
            this.popMenu.show(evt.getComponent(), evt.getX(), evt.getY());
        }
    }
}//GEN-LAST:event_jTree1MouseReleased

private void jTree1KeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTree1KeyTyped
// TODO add your handling code here:
    if (evt.getKeyChar() == evt.VK_ENTER) /*load all valide files*/ 
        loadDirectoryFiles();
}//GEN-LAST:event_jTree1KeyTyped

private void jList1MousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jList1MousePressed
// TODO add your handling code here:
    if (evt.isPopupTrigger()) {
        int index = jList1.locationToIndex(new Point(evt.getX(), evt.getY()));
        if (index == -1) {
            return;
        }
        jList1.setSelectedIndex(index);
        this.popMenuShow.show(evt.getComponent(), evt.getX(), evt.getY());
    }
}//GEN-LAST:event_jList1MousePressed

private void jList1MouseReleased(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jList1MouseReleased
// TODO add your handling code here:

    if (evt.isPopupTrigger()) {
        //int index = jList1.getSelectedIndex();
        //Object pos = jList1.getComponentAt(evt.getX(), evt.getY());
        //if (pos instanceof javax.swing.JList){
        //    index = ((JList)pos).getLeadSelectionIndex();
        //}
        int index = jList1.locationToIndex(new Point(evt.getX(), evt.getY()));
        if (index == -1) {
            return;
        }
        jList1.setSelectedIndex(index);
        this.popMenuShow.show(evt.getComponent(), evt.getX(), evt.getY());
    }
}//GEN-LAST:event_jList1MouseReleased

private void jList1KeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jList1KeyTyped
// TODO add your handling code here:
    if (evt.getKeyChar() == evt.VK_ENTER) {
        int index = jList1.getSelectedIndex();
        if (index == -1) {
            return;
        }
        TreeFile target_ct = (TreeFile) lmodel.get(index);
        updateTouchGraph(target_ct.getAbsolutePath());
    }
}//GEN-LAST:event_jList1KeyTyped
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JList jList1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JTree jTree1;
    // End of variables declaration//GEN-END:variables
}

/**
 * The methods in this class allow the JTree component to traverse
 * the file system tree, and display the files and directories.
 **/
class FileTreeModel implements TreeModel {
    // We specify the root directory when we create the model.
    protected TreeFile root = null;
    private boolean winOS;
    private CustomizedFilter ff = null;

    public FileTreeModel() {


        ff = new CustomizedFilter();
        ff.dirToggle(true);
        //ff.addFilter("ct");
        //ff.addFilter("rnaml");

        String hostname = "";
        /*get machine name*/
        try {
            hostname = InetAddress.getLocalHost().getHostName();
        } catch (UnknownHostException ex) {
            Logger.getLogger(FileTreeModel.class.getName()).log(Level.SEVERE, null, ex);
        }

        /*create virtual root*/
        this.root = new TreeFile(hostname);
        /*determine OS type*/
        String os = System.getProperty("os.name");
        if (os.startsWith("Windows")) {
            winOS = true;
        }
    }

    public FileTreeModel(TreeFile root) {
        this.root = root;
    }
    // The model knows how to return the root object of the tree
    public Object getRoot() {
        return root;
    }
    // Tell JTree whether an object in the tree is a leaf or not
    public boolean isLeaf(Object node) { 
        return ((TreeFile) node).isFile();
    }
    // Tell JTree how many children a node has
    public int getChildCount(Object parent) {

        String[] children = null;
        /*take OS root file list*/
        if (parent.equals(root)) {

            File[] rootF = TreeFile.listRoots();
            if (!winOS) {
                rootF = rootF[0].listFiles((FilenameFilter) ff);
            }
            if (rootF != null) {
                return rootF.length;
            } else {
                return 0;
            }
        } else {
            children = ((TreeFile) parent).list((FilenameFilter) ff);
        }
        if (children == null) {
            return 0;
        } else {
            return children.length;
        }
    }
    // Fetch any numbered child of a node for the JTree.
    // Our model returns File objects for all nodes in the tree.  The
    // JTree displays these by calling the File.toString() method.
    public Object getChild(Object parent, int index) {



        String[] children = null;
        /*take OS root file list*/
        if (parent.equals(root)) {

            File[] rootF = TreeFile.listRoots();
            if (!winOS) {
                rootF = rootF[0].listFiles((FilenameFilter) ff);            //System.out.println("total of " + rootF.length + " root directories");
            }
            children = new String[rootF.length];
            for (int i = 0; i < rootF.length; i++) {
                children[i] = rootF[i].getAbsolutePath();
            }
        } else {
            children = ((TreeFile) parent).list((FilenameFilter) ff);
        }
        if ((children == null) || (index >= children.length)) {
            return null;        //return new TreeFile(children[index]);
        }
        if (parent.equals(root)) {
            return new TreeFile(children[index]);
        } else {
            return new TreeFile((TreeFile) parent, children[index]);
        }
    }
    // Figure out a child's position in its parent node.
    public int getIndexOfChild(Object parent, Object child) {
        String[] children = ((File) parent).list();
        if (children == null) {
            return -1;
        }
        String childname = ((File) child).getName();
        for (int i = 0; i < children.length; i++) {
            if (childname.equals(children[i])) {
                return i;
            }
        }
        return -1;
    }
    // This method is only invoked by the JTree for editable trees.  
    // This TreeModel does not allow editing, so we do not implement 
    // this method.  The JTree editable property is false by default.
    public void valueForPathChanged(TreePath path, Object newvalue) {
    }
    // Since this is not an editable tree model, we never fire any events,
    // so we don't actually have to keep track of interested listeners.
    public void addTreeModelListener(TreeModelListener l) {
    }

    public void removeTreeModelListener(TreeModelListener l) {
    }
}

class TreeFile extends File {

    public TreeFile(TreeFile parent, String filename) {
        super((File) parent, filename);
    }

    public TreeFile(String filename) {
        super(filename);
    }

    /**
     * Override toString +
     * 
     * @return
     */
    public String toString() {
        String path = null;


        path = super.getName();

        if ((System.getProperty("os.name")).startsWith("Windows")) {
            if (super.getParentFile() == null && super.exists()) {
                path = super.getAbsolutePath();
            }
        }
        return path;
    }
}

class CustomizedFilter implements FilenameFilter {

    Vector format = new Vector();
    boolean showDir = true; /*toggle show or hide directory - defaulted shown*/


    public CustomizedFilter() {
    }

    public void addFilter(String filter) {
        format.add(filter);
    }

    public void dirToggle(boolean status) {
        showDir = status;
    }

    public boolean accept(File f, String s) {


        File cur = new File(f, s);

        if (cur.isDirectory()) {
            if (showDir) {
                
                if(s.startsWith("."))
                    return false;
                else
                    return true;
                
            } else {
                return false;
            }
        }
        int index = s.lastIndexOf('.');
        if (index == -1) {
            return false;
        }
        String ext = s.substring(index + 1);
        for (int i = 0; i < format.size(); i++) {
            if (((String) format.get(i)).equals(ext)) {
                return true;
            }
        }
        return false;
    }
}

  
