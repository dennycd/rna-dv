package Varna;

/*
    VARNA is a Java library for quick automated drawings RNA secondary structure 
    Copyright (C) 2007  Yann Ponty

    This program is free software: you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    This program is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with this program.  If not, see <http://www.gnu.org/licenses/>. 
*/

import Varna.varna.*;
import javax.swing.*;

import Varna.varna.VARNAPanel;


import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
/**
 * @author ponty
 *
 */
public class VARNAOfflineDemo extends JFrame {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	VARNAPanel _mainSurface = new VARNAPanel(
		"CAGCACGACACUAGCAGUCAGUGUCAGACUGCAIACAGCACGACACUAGCAGUCAGUGUCAGACUGCAIACAGCACGACACUAGCAGUCAGUGUCAGACUGCAIA",
		"..(((((...(((((...(((((...(((((.....)))))...))))).....(((((...(((((.....)))))...))))).....)))))...)))))..");

	JPanel _tools = new JPanel();
	JPanel _input = new JPanel();
	JButton _go = new JButton("Display");
	JTextField _struct = new JTextField();
	JTextField _seq = new JTextField();
	
	public VARNAOfflineDemo()
	{
		super();
		RNAPanelDemoInit();
	}
	
	private void RNAPanelDemoInit()
	{
		setSize(600,600);
		Font _textFieldsFont = Font.decode("MonoSpaced-PLAIN-12");
		_seq.setFont(_textFieldsFont);
		_seq.setText(_mainSurface.getSeq());
		_struct.setFont(_textFieldsFont);
		_struct.setText(_mainSurface.getStructDBN());
		
		
	    _tools.setLayout(new BorderLayout());
	    _tools.add(_input,BorderLayout.CENTER);
	    _tools.add(_go,BorderLayout.EAST);
	    _input.setLayout(new GridLayout(2,0));
	    _input.add(_struct);
	    _input.add(_seq);
	    
	    _go.addActionListener(new ActionListener(){

			public void actionPerformed(ActionEvent arg0) {
				_mainSurface.setRNA(_seq.getText(), _struct.getText());
				
			}});
	    
		getContentPane().setLayout(new BorderLayout());
		getContentPane().add(_mainSurface,BorderLayout.CENTER);
		getContentPane().add(_tools,BorderLayout.SOUTH);

	    addWindowListener(new WindowListener(){
			public void windowActivated(WindowEvent arg0) {}
			public void windowClosed(WindowEvent arg0) {}
			public void windowClosing(WindowEvent arg0) 
			{	System.exit(0);	}
			public void windowDeactivated(WindowEvent arg0) {}
			public void windowDeiconified(WindowEvent arg0) {}
			public void windowIconified(WindowEvent arg0) {}
			public void windowOpened(WindowEvent arg0) {}});
	    setVisible(true);

	}
	
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
	      VARNAOfflineDemo mainFrame = new VARNAOfflineDemo();
	}

}
