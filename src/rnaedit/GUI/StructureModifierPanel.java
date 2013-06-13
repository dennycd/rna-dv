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
import rnaedit.ct.readCT;
import rnaedit.ct.writeCT;
import rnaedit.tgxml.TGConverter;

/**
 *
 * @author  cda18
 */
public class StructureModifierPanel extends javax.swing.JPanel implements Runnable {

    private TGLinkBrowser tg = null; // the referenece to TG browser object
    private String old_seq = "";
    private String old_struct = "";

    /**
     * The run thread for this Model Panel 
     *
     */
    public void run() {

        System.out.println("Structure modifier listening....");

        while (true) {
            /*sleep*/
            try {
                Thread.currentThread().sleep(1000);
            } catch (InterruptedException e) {
            }

            /*check CT sturcture update*/
            if (tg.rna_ct != null) {

                /*check the current structure & sequence*/
                String new_seq = tg.rna_ct.generateSequence();
                String new_struct = tg.rna_ct.generateStruct();
                /*update only if different*/
                if (!new_seq.equals(old_seq)) {
                    sequence_box.setText(new_seq);
                    old_seq = new_seq;
                //System.out.println("Updating sequence...");
                }
                if (!new_struct.equals(old_struct)) {
                    structure_box.setText(new_struct);
                    old_struct = new_struct;
                //System.out.println("Updating structure...");
                }

            }

            if (tg.stauts == TGLinkBrowser.TG_LOAD_FILE) {
                if (Lstatus.getText().indexOf("Loading") != -1) {
                    Lstatus.setText(Lstatus.getText() + ".");
                } else {
                    Lstatus.setText("Loading structure files...");
                }
            } else if (tg.stauts == TGLinkBrowser.TG_SAVE_FILE) {
                if (Lstatus.getText().indexOf("Saving") != -1) {
                    Lstatus.setText(Lstatus.getText() + ".");
                } else {
                    Lstatus.setText("Saving structure files...");
                }
            } else if (tg.stauts == TGLinkBrowser.TG_GEN_XML) {
                if (Lstatus.getText().indexOf("Converting") != -1) {
                    Lstatus.setText(Lstatus.getText() + ".");
                } else {
                    Lstatus.setText("Converting XML files...");
                }
            } else if (tg.stauts == TGLinkBrowser.TG_DISPLAY) {
                int index = tg.rna_ct.ct_file.indexOf(".tmp");
                if (index != -1) {
                    Lstatus.setText("File name: " + tg.rna_ct.ct_file.substring(0, index));
                } else {
                    Lstatus.setText("File name: " + tg.rna_ct.ct_file);
                }
            }
        //System.out.println("sructure modefier listneing...");
        }
    }

    /** Creates new form StructureModifierPanel */
    public StructureModifierPanel() {
        initComponents();
    }

    /**
     * Use structure_box and sequence_box content 
     * to update TouchGraph 
     * 
     */
    private void updateTouchGraph() {

        new Thread() {

            @Override
            public void run() {
                /**/
                tg.stauts = TGLinkBrowser.TG_SAVE_FILE;

                /*update rna_ct*/
                tg.rna_ct = (new readCT()).parseBracketString(structure_box.getText(), sequence_box.getText());
                /*update temporary CT files*/
                (new writeCT()).write(tg.rna_ct, tg.rna_ct.ct_file);

                /*repopulate XML touch graph file*/
                tg.stauts = TGLinkBrowser.TG_GEN_XML;
                String xmlFileName = tg.rna_ct.ct_file + ".xml"; /*the converted xml file name*/
                TGConverter converter = new TGConverter();
                converter.convert(tg.rna_ct, xmlFileName,tg.viewOpt);
                tg.tgPanel.clearAll();
                try {
                    tg.xmlio.read(xmlFileName, new RestoreExactGraph());
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
                tg.stauts = TGLinkBrowser.TG_DISPLAY;

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

    /**
     * initialization of the thermomodel panel 
     * 
     * Must pass the CT object to this Model Panel so that it updates accordingly
     * 
     */
    public void SetTouchGraph(TGLinkBrowser src) {
        this.tg = src; //pass in the reference to the TGLinkBrowser    
    }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        Apply = new javax.swing.JButton();
        Reset = new javax.swing.JButton();
        sequence_box = new javax.swing.JTextField();
        structure_box = new javax.swing.JTextField();
        Lstatus = new javax.swing.JLabel();

        jLabel1.setText("Sequence");

        jLabel2.setText("Structure");

        Apply.setText("Apply");
        Apply.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ApplyActionPerformed(evt);
            }
        });

        Reset.setText("Reset");
        Reset.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ResetActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel1)
                    .addComponent(jLabel2))
                .addGap(27, 27, 27)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(sequence_box, javax.swing.GroupLayout.DEFAULT_SIZE, 470, Short.MAX_VALUE)
                    .addComponent(structure_box, javax.swing.GroupLayout.DEFAULT_SIZE, 470, Short.MAX_VALUE))
                .addContainerGap())
            .addGroup(layout.createSequentialGroup()
                .addGap(12, 12, 12)
                .addComponent(Apply)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(Reset)
                .addGap(18, 18, 18)
                .addComponent(Lstatus, javax.swing.GroupLayout.DEFAULT_SIZE, 212, Short.MAX_VALUE)
                .addContainerGap(242, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(sequence_box, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel1))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(structure_box, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel2))
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(13, 13, 13)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(Reset)
                            .addComponent(Apply)))
                    .addGroup(layout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(Lstatus, javax.swing.GroupLayout.DEFAULT_SIZE, 30, Short.MAX_VALUE)))
                .addContainerGap())
        );

        layout.linkSize(javax.swing.SwingConstants.VERTICAL, new java.awt.Component[] {sequence_box, structure_box});

    }// </editor-fold>//GEN-END:initComponents

private void ApplyActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ApplyActionPerformed

    /*update local storage*/
    old_seq = sequence_box.getText();
    old_struct = structure_box.getText();

    /*update the touch graph using input box for structure & sequence*/
    updateTouchGraph();

}//GEN-LAST:event_ApplyActionPerformed

private void ResetActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ResetActionPerformed

    /*clear both input box*/
    structure_box.setText("");
    sequence_box.setText("");
}//GEN-LAST:event_ResetActionPerformed
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton Apply;
    private javax.swing.JLabel Lstatus;
    private javax.swing.JButton Reset;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JTextField sequence_box;
    private javax.swing.JTextField structure_box;
    // End of variables declaration//GEN-END:variables
}
