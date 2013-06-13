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


/**
 * JPanel for displaying Thermodynamic energy 
 * 
 * 
 * 
 * 
 * 
 */

package rnaedit.thermo;

import com.touchgraph.linkbrowser.TGLinkBrowser;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import rnaedit.ct.CT;
import rnaedit.ct.writeCT;

public class ThermoModelPanel extends JPanel  implements ActionListener, Runnable{
    protected JTextField textField;
    protected JTextArea textArea;
    private final static String newline = "\n";
    
    private JLabel Lmajor, Lefn,Lhotknot;
    
    //private CT rna_ct = null; //the local reference to a given RNA CT structure
    private TGLinkBrowser tg = null; // the referenece to TG browser object
    
    
    /**
     * Once CT stucture is changed, recompute the thermodynamic model 
     * 
     * @rna The CT structure that has been updated 
     */
    private void updateThermo(CT rna_ct){
        System.out.println("updating thermo energy...");
       
        /*
        textArea.append("Major Energy:  " + (new Major()).fitness(rna_ct) + "\n");
        textArea.append("Efn Energy:  " + (new Efn()).fitness(rna_ct) + "\n");
        textArea.append("HotKnot Energy:  " + (new HotKnots()).fitness(rna_ct) + "\n");
        
        textArea.setCaretPosition(textArea.getDocument().getLength());     
         */
               
        Lmajor.setText("Major Energy:  " + (new Major()).fitness(rna_ct) + "\n");
        Lefn.setText("Efn Energy:  " + (new Efn()).fitness(rna_ct) + "\n");
        Lhotknot.setText("HotKnot Energy:  " + (new HotKnots()).fitness(rna_ct) + "\n");
        
        rna_ct.updated = false; //reset the update flag
    }
    
 
    /**
     * The run thread for this Model Panel 
     *
     */            
    public void run(){
               
        while(true)
        {   
            /*sleep*/
            try {
                Thread.currentThread().sleep(1000);
            }catch (InterruptedException e) {
            }       
            /*check CT sturcture update*/
            if(tg.rna_ct != null){
                if(tg.rna_ct.updated == true){   
                    //update tmp CT file (according to Touchgraph changes)
                    (new writeCT()).write(tg.rna_ct, tg.rna_ct.ct_file);
                    //update Thermo value
                    this.updateThermo(tg.rna_ct);
                }
            }
        }
    }
    
    /**
     * initialization of the thermomodel panel 
     * 
     * Must pass the CT object to this Model Panel so that it updates accordingly
     * 
     */
    public ThermoModelPanel(TGLinkBrowser src) {
        
        super(new GridLayout(3,1));//three rows two column
        
        Lmajor  = new JLabel("Major Energy: ", JLabel.CENTER);
        Lefn = new JLabel("EFN Energy: ", JLabel.CENTER);
        Lhotknot = new JLabel("Hotknot Energy: ", JLabel.CENTER);
        
        add(Lmajor);
        add(Lefn);
        add(Lhotknot);
        /*
         super(new GridBagLayout());

        textField = new JTextField(20);
        textField.addActionListener(this);

        textArea = new JTextArea(5, 20);
        textArea.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(textArea);

        //Add Components to this panel.
        GridBagConstraints c = new GridBagConstraints();
        c.gridwidth = GridBagConstraints.REMAINDER;

        c.fill = GridBagConstraints.HORIZONTAL;
        add(textField, c);

        c.fill = GridBagConstraints.BOTH;
        c.weightx = 1.0;
        c.weighty = 1.0;
        add(scrollPane, c);  
        
         */
         
        
        
        this.tg = src; //pass in the reference to the TGLinkBrowser 
    }
    
    public void actionPerformed(ActionEvent evt) {
  
//        String text = textField.getText();
//        textArea.append(text + newline);
//        textField.selectAll();

        //Make sure the new text is visible, even if there
        //was a selection in the text area.
 //       textArea.setCaretPosition(textArea.getDocument().getLength());
    }
    
    
    /**
     * Create the GUI and show it.  For thread safety,
     * this method should be invoked from the
     * event dispatch thread.
     */
    /*
    private static void createAndShowGUI() {
        //Create and set up the window.
        JFrame frame = new JFrame("TextDemo");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        //Add contents to the window.
        frame.add(new ThermoModelPanel());

        //Display the window.
        frame.pack();
        frame.setVisible(true);
    }
    */

    /*
    public static void main(String[] args) {
        //Schedule a job for the event dispatch thread:
        //creating and showing this application's GUI.
        javax.swing.SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                JFrame frame = new JFrame("TextDemo");
                frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                frame.add(new ThermoModelPanel());
                frame.pack();
                frame.setVisible(true);
            }
        });
    }
    */
}