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

package rnadv;

import com.touchgraph.linkbrowser.TGLinkBrowser;
import java.awt.Dimension;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import javax.swing.JApplet;

/**
 *
 * @author  cda18
 */
public class TestJFrame extends javax.swing.JFrame {

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        java.awt.EventQueue.invokeLater(new Runnable() {

            public void run() {
                TestJFrame tf = null;
                installation(tf);
                //System.out.println("Initiating GUI....");
                //JOptionPane.showMessageDialog(null, "Yes I'm running");
                //tf.setVisible(true);
            }
        });
    }
    
    /*run in applet mode - disable all local file access*/
    public TestJFrame(JApplet jp){
        this.setApplet(jp);
        initComponents();
        initComponentsCustomized();
    }

    /** Creates new form TestJFrame */
    public TestJFrame() {
        appletCheck(true);
        initComponents();
        initComponentsCustomized();
        
    }
    
    public void setApplet(JApplet jp){
        this.mainApplet = jp;
    }

    
    private boolean appletCheck(boolean msg){
                      
        boolean isApplet;
        SecurityManager s = System.getSecurityManager();
        if(s != null){
            try{
                s.checkAccess(Thread.currentThread());
                isApplet = true;
                if(msg)
                    System.out.println("security check succeed....");
            }
            catch(SecurityException e ){
                isApplet = true;
                if(msg)
                    System.out.println("security check exception....");
            }
        }
        else{
            if(msg)
                System.out.println("security handler is null....");
            isApplet = false;
        }
      
        if(isApplet)
            if(msg)
                System.out.println("Running as web applet....");
        else
            if(msg)
                System.out.println("Running as standalone app....");
        
        return isApplet;
    }
    
    /**
     * Customized version of GUI 
     * 
     */
    private void initComponentsCustomized() {
        
    }

    @Override
    public void setDefaultCloseOperation(int operation) {
        //super.setDefaultCloseOperation(operation);
    }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        energyModelDisplayPanel1 = new rnaedit.GUI.EnergyModelDisplayPanel();
        directoryTreePanel1 = new rnaedit.GUI.DirectoryTreePanel();
        tGLinkBrowser1 = new com.touchgraph.linkbrowser.TGLinkBrowser();
        jMenuBar1 = new javax.swing.JMenuBar();
        jMenu1 = new javax.swing.JMenu();
        jMenu2 = new javax.swing.JMenu();
        structureModifierPanel1 = new rnaedit.GUI.StructureModifierPanel();
        jMenuBar2 = new javax.swing.JMenuBar();
        jMenu3 = new javax.swing.JMenu();
        jMenu4 = new javax.swing.JMenu();
        jMenuBar3 = new javax.swing.JMenuBar();
        jMenu5 = new javax.swing.JMenu();
        jMenu6 = new javax.swing.JMenu();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(energyModelDisplayPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, 205, Short.MAX_VALUE)
                    .addComponent(directoryTreePanel1, javax.swing.GroupLayout.DEFAULT_SIZE, 205, Short.MAX_VALUE)))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(directoryTreePanel1, javax.swing.GroupLayout.DEFAULT_SIZE, 425, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(energyModelDisplayPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        energyModelDisplayPanel1.SetTouchGraph(tGLinkBrowser1);
        //initiate thread
        Thread thread2 = new Thread(energyModelDisplayPanel1,"EnergyDisplayThread");
        thread2.start();
        directoryTreePanel1.SetTouchGraph(tGLinkBrowser1);

        jMenu1.setText("File");
        jMenuBar1.add(jMenu1);

        jMenu2.setText("Edit");
        jMenuBar1.add(jMenu2);

        jMenu3.setText("File");
        jMenuBar2.add(jMenu3);

        jMenu4.setText("Edit");
        jMenuBar2.add(jMenu4);

        jMenu5.setText("File");
        jMenuBar3.add(jMenu5);

        jMenu6.setText("Edit");
        jMenuBar3.add(jMenu6);

        setJMenuBar(jMenuBar3);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(tGLinkBrowser1, javax.swing.GroupLayout.DEFAULT_SIZE, 562, Short.MAX_VALUE))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(12, 12, 12)
                        .addComponent(structureModifierPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, 773, Short.MAX_VALUE)))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(tGLinkBrowser1, javax.swing.GroupLayout.DEFAULT_SIZE, 580, Short.MAX_VALUE)
                    .addComponent(jPanel1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(12, 12, 12)
                .addComponent(structureModifierPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        this.addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {System.exit(0);}
        });

        if(appletCheck(false)){
            if(mainApplet!=null)
            mainApplet.setJMenuBar(tGLinkBrowser1.addMainMenuBar());
        }
        else
        this.setJMenuBar(tGLinkBrowser1.addMainMenuBar());
        structureModifierPanel1.SetTouchGraph(tGLinkBrowser1);

        //initiate thread
        Thread thread1 = new Thread(structureModifierPanel1,"ModifierThread");
        thread1.start();

        pack();
    }// </editor-fold>//GEN-END:initComponents

    
    /**
     * Check to see whether energy binary code exist
     * Otherwise download the package
     */
    public static void installation(TestJFrame tf){

        InstallationFrame installF = new InstallationFrame();
        installF.setMainFrame(tf);
        installF.setTitle("RNA-DV Update");
        installF.setVisible(true);
        
        
        Thread thread = new Thread(installF,"InstallationThread");
        thread.start();
        
    
        
    }
    
    public void setVisible(boolean b) {
        /*user screen size*/
        Dimension ss = java.awt.Toolkit.getDefaultToolkit().getScreenSize();
        /*self size*/
        Dimension selfs = this.getSize();
        super.setLocation( ss.width/2 - selfs.width/2, ss.height/2 - selfs.height/2);
        super.setVisible(b);
    }
    

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private rnaedit.GUI.DirectoryTreePanel directoryTreePanel1;
    private rnaedit.GUI.EnergyModelDisplayPanel energyModelDisplayPanel1;
    private javax.swing.JMenu jMenu1;
    private javax.swing.JMenu jMenu2;
    private javax.swing.JMenu jMenu3;
    private javax.swing.JMenu jMenu4;
    private javax.swing.JMenu jMenu5;
    private javax.swing.JMenu jMenu6;
    private javax.swing.JMenuBar jMenuBar1;
    private javax.swing.JMenuBar jMenuBar2;
    private javax.swing.JMenuBar jMenuBar3;
    private javax.swing.JPanel jPanel1;
    private rnaedit.GUI.StructureModifierPanel structureModifierPanel1;
    private com.touchgraph.linkbrowser.TGLinkBrowser tGLinkBrowser1;
    // End of variables declaration//GEN-END:variables

    private TGLinkBrowser touchPanel;
    private JApplet mainApplet; 
}
