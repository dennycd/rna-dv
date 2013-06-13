/*
 * Main.java
 *
 * Created on
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package rnadv;
import com.touchgraph.linkbrowser.TGLinkBrowser;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import javax.swing.JFrame;
import rnaedit.GUI.EnergyModelDisplayPanel;
import rnaedit.GUI.StructureModifierPanel;


/**
 *
 * @author Denny Chen Dai
 */
public class Main {
    
    /** Creates a new instance of Main */
    public Main() {
        
    }
    
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
                 

        /*******************LOAD TOUCHGRAPH FRAME****************************/
        JFrame lbFrame;
        lbFrame = new JFrame("RNA-Edit Version 0.9");
        //lbFrame.setTitle("Denny");
        final TGLinkBrowser lbPanel = new TGLinkBrowser(lbFrame);
        lbFrame.addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {System.exit(0);}
        });       
        lbFrame.getContentPane().add("Center", lbPanel);
        lbFrame.setJMenuBar(lbPanel.addMainMenuBar());
        lbFrame.setSize(1024,726);  
        lbFrame.setVisible(true);               
        lbFrame.setResizable(true);
        
        /********************LOAD THERMO MODEL FRAME**************************/
        JFrame modelFrame = new JFrame("Thermodynamic Energy");
        modelFrame.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
        final EnergyModelDisplayPanel TMpanel = new EnergyModelDisplayPanel();
        TMpanel.SetTouchGraph(lbPanel);
        modelFrame.add(TMpanel);
        modelFrame.pack();
        modelFrame.setSize(200, 300);
        modelFrame.setVisible(true);
        
        /*******************LOAD STRUCTURE CONTROL PANEL************************/
        JFrame structureFrame = new JFrame("Structure Controller");
        structureFrame.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
        final StructureModifierPanel SCpanel = new StructureModifierPanel();
        SCpanel.SetTouchGraph(lbPanel);
        structureFrame.add(SCpanel);
        structureFrame.pack();
        structureFrame.setSize(600, 100);
        structureFrame.setVisible(true);
        
        /**************initialize the model thread**********************************/  
        Thread thread1 = new Thread(TMpanel,"ModelThread");
//        Thread thread2 = new Thread(SCpanel,"StructThread");
        thread1.start();
 //       thread2.start();
    }
    
    
    
}
