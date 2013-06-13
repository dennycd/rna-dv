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

import com.touchgraph.linkbrowser.*;
import java.awt.*;
import javax.swing.*;
import rnadv.InstallationFrame;
import rnadv.TestJFrame;

public class RnaDvApplet extends JApplet {
	
    
	private TGLinkBrowser tgLinkBrowser;
	
        
    @Override
    public void init() {
    	//System.out.println(getAppletInfo());
               /*Check whether running under Applet mode*/

        
      
        getContentPane().setLayout(new BorderLayout());
        setSize(500,500);
        
        /*local installation process*/
        TestJFrame tf = new TestJFrame(this);
        InstallationFrame installF = new InstallationFrame();
        installF.setMainFrame(tf);
        installF.setAppletPane(this);
        installF.setTitle("RNA-DV Update");
        
        this.setContentPane(installF.getContentPane());
        
        Thread thread = new Thread(installF,"InstallationThread");
        thread.start();
        
        
        //this.setContentPane(tf.getContentPane());
        
        //create linkbrowser instance 
        //tgLinkBrowser = new TGLinkBrowser(this);
        //add to applet panel 
       // getContentPane().add(tgLinkBrowser, BorderLayout.CENTER);
    }
    
    
	public void setLocale(String nodeName) {
		tgLinkBrowser.setLocale(nodeName,2);	
	}
}	

