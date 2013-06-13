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


package rnaedit.structure;

import com.touchgraph.graphlayout.TGException;
import com.touchgraph.linkbrowser.TGLinkBrowser;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import rnaedit.ct.readCT;
import rnaedit.ct.writeCT;
import rnaedit.tgxml.TGConverter;

public class StructurePanel extends JPanel  implements ActionListener, Runnable, DocumentListener{


    private JTextArea structure_box = new JTextArea(); 
    private JTextArea sequence_box = new JTextArea();
    
    private JButton clearButton = new JButton("Clear");
    private JButton applyButton = new JButton("Apply");
    
    private TGLinkBrowser tg = null; // the referenece to TG browser object
    
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
                    structure_box.setText(tg.rna_ct.generateStruct());
                    sequence_box.setText(tg.rna_ct.generateSequence());
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
    public StructurePanel(TGLinkBrowser src) {
        
        super(new GridLayout(3,2));//three rows two column
        
        
        
        structure_box.setEditable(true);
        structure_box.setFont(new Font(null, Font.BOLD, 13));
        structure_box.setLineWrap(true);
        structure_box.setWrapStyleWord(true);

        sequence_box.setEditable(true);
        sequence_box.setFont(new Font(null, Font.BOLD, 13));
        sequence_box.setLineWrap(true);
        sequence_box.setWrapStyleWord(true);        
        
        
        JScrollPane structScrollPane = new JScrollPane(structure_box);
        structScrollPane.setVerticalScrollBarPolicy(
        JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        structScrollPane.setPreferredSize(new Dimension(250, 250));
        
   
        JScrollPane seqScrollPane = new JScrollPane(sequence_box);
        seqScrollPane.setVerticalScrollBarPolicy(
        JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        seqScrollPane.setPreferredSize(new Dimension(250, 250));

        //structure_box.getDocument().addDocumentListener(this);
        //sequence_box.getDocument().addDocumentListener(this);
        
        //Create and initialize the buttons.
        
        applyButton.addActionListener(this);
        applyButton.setActionCommand("Apply");
        
        clearButton.addActionListener(this);
        clearButton.setActionCommand("Clear");
    
        
        add(seqScrollPane);
        add(structScrollPane);
        add(clearButton);
        add(applyButton);

        this.tg = src; //pass in the reference to the TGLinkBrowser 
    }
    
    /**
     * Use structure_box and sequence_box content 
     * to update TouchGraph 
     * 
     */
    private void updateTouchGraph(){
        
        /*update rna_ct*/
        tg.rna_ct = (new readCT()).parseBracketString(structure_box.getText(), sequence_box.getText());       
        /*update temporary CT files*/
        (new writeCT()).write(tg.rna_ct, tg.rna_ct.ct_file);
        
        /*repopulate XML touch graph file*/
        String xmlFileName = tg.rna_ct.ct_file + ".xml"; /*the converted xml file name*/
        TGConverter converter = new TGConverter();
        converter.convert(tg.rna_ct, xmlFileName,tg.viewOpt);
        tg.tgPanel.clearAll();    
        try {
            tg.xmlio.read(xmlFileName, new RestoreExactGraph());
        }
        catch (Exception ex) { ex.printStackTrace(); }       
    
    }
    
    
  /** A thread executed after reading from an XML file, that 
     * sets the visibleLocale to the nodes makred as visible.
     */
    private class RestoreExactGraph extends Thread {                                    
        public void run() {        
            try {                      
               tg.tgPanel.updateLocalityFromVisibility();
            }
            catch (TGException ex) { ex.printStackTrace(); }
               
            tg.tgPanel.clearSelect();
            tg.tgPanel.fireResetEvent();                                                        
            tg.restoreSavedParameters(tg.xmlio.getParameterHash());                            
        }
    }
    
    public void actionPerformed(ActionEvent evt) {
  
        if("Apply".equals(evt.getActionCommand())){
            /* invalidate the main touchgraph using the inputed 
             * structure and sequence content
             */
            updateTouchGraph();
            
        }else
        if("Clear".equals(evt.getActionCommand())){
            structure_box.setText("");
            sequence_box.setText("");
        }
    }
    
     
    public void changedUpdate(DocumentEvent ev) {
        System.out.println("changeUpdate");
    }
    
    public void removeUpdate(DocumentEvent ev) {
        System.out.println("removeUpdate");
    }
    
    public void insertUpdate(DocumentEvent ev) {

        System.out.println("insertUpdate");
    }
}

