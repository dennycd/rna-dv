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
import java.applet.*;
import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

import Varna.varna.VARNAPanel;


import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.InputMethodEvent;
import java.awt.event.InputMethodListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
/**
 * An RNA 2d Panel demo applet
 * @author Yann Ponty
 *
 */


public class VARNADemo extends JApplet {
		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;

		VARNAPanel _mainSurface = new VARNAPanel();

		
		JPanel _tools = new JPanel();
		JPanel _input = new JPanel();
		JButton _go = new JButton("Display");

		JPanel _seqPanel = new JPanel();
		JPanel _structPanel = new JPanel();
		JTextField _struct = new JTextField();
		JTextField _seq = new JTextField();
		JLabel _structLabel = new JLabel(" Str: ");
		JLabel _seqLabel = new JLabel(" Seq: ");
		
		JPanel _backgroundColorPanel = new JPanel();
		JLabel _backgroundColorLabel = new JLabel("Background ");
		JLabel _backgroundColorRLabel = new JLabel("R:");
		JSlider _backgroundColorRSlider = new JSlider(JSlider.HORIZONTAL,0,15,14);
		JLabel _backgroundColorGLabel = new JLabel("G:");
		JSlider _backgroundColorGSlider = new JSlider(JSlider.HORIZONTAL,0,15,14);
		JLabel _backgroundColorBLabel = new JLabel("B:");
		JSlider _backgroundColorBSlider = new JSlider(JSlider.HORIZONTAL,0,15,14);
		JLabel _numPeriodLabel = new JLabel(" Base numbers: ");
		JSlider _numPeriodSlider = new JSlider(JSlider.HORIZONTAL,1,25,10);

		JPanel _titlePanel = new JPanel();
		JLabel _titleLabel = new JLabel(" Title: ");
		JTextField _title = new JTextField("VARNA Demo");
		JLabel _titleSizeLabel = new JLabel(" Title size: ");
		JSlider _titleSizeSlider = new JSlider(JSlider.HORIZONTAL,7,30,10);
		JCheckBox _circularCheckbox = new JCheckBox("Circular drawing");

		
		
		// Applet provided parameters
		Color _backgroundColor = Color.white;
		String _sseq =    "CAGCACGACACUAGCAGUCAGUGUCAGACUGCAIACAGCACGACACUAGCAGUCAGUGUCAGACUGCAIACAGCACGACACUAGCAGUCAGUGUCAGACUGCAIA";
		String _sstruct = "..(((((...(((((...(((((...(((((.....)))))...))))).....(((((...(((((.....)))))...))))).....)))))...)))))..";
		int _periodResNum = 5;
		
		public VARNADemo()
		{
			super();
			RNAPanelDemoInit();
		}
	
		private void RNAPanelDemoInit()
		{
			int marginTools = 40;

			
			setSize(600,600);
			setBackground(_backgroundColor);
			_mainSurface.setBackground(_backgroundColor);

			_mainSurface.setRNA(_sseq, _sstruct);			

			Font _textFieldsFont = Font.decode("MonoSpaced-PLAIN-12");

			_seqLabel.setHorizontalTextPosition(JLabel.LEFT);
			_seqLabel.setPreferredSize(new Dimension(marginTools,15));
			_seq.setFont(_textFieldsFont);
			_seq.setText(_mainSurface.getSeq());
			_seq.getDocument().addDocumentListener(new DocumentListener(){
				public void changedUpdate(DocumentEvent arg0) {
					getRNA();
				}
				public void insertUpdate(DocumentEvent arg0) {
					getRNA();
				}
				public void removeUpdate(DocumentEvent arg0) {
					getRNA();
				}});
			_struct.getDocument().addDocumentListener(new DocumentListener(){
				public void changedUpdate(DocumentEvent arg0) {
					getRNA();
				}
				public void insertUpdate(DocumentEvent arg0) {
					getRNA();
				}
				public void removeUpdate(DocumentEvent arg0) {
					getRNA();
				}});
			_titleSizeSlider.addChangeListener(new ChangeListener(){
				public void stateChanged(ChangeEvent arg0) {
					_mainSurface.setTitleFontSize(_titleSizeSlider.getValue());
				}});
			_seqPanel.setLayout(new BorderLayout());
			_seqPanel.add(_seqLabel,BorderLayout.WEST);
			_seqPanel.add(_seq,BorderLayout.CENTER);

			_structLabel.setPreferredSize(new Dimension(marginTools,15));
			_structLabel.setHorizontalTextPosition(JLabel.LEFT);
			_struct.setFont(_textFieldsFont);
			_struct.setText(_mainSurface.getStructDBN());
			_structPanel.setLayout(new BorderLayout());
			_structPanel.add(_structLabel,BorderLayout.WEST);
			_structPanel.add(_struct,BorderLayout.CENTER);
			

			/*Color[] colors = {new ColorWrapper(Color.BLACK,"black"),
					new ColorWrapper(Color.WHITE,"white"),
					new ColorWrapper(Color.RED,"red"),
					new ColorWrapper(Color.BLUE,"blue"),
					new ColorWrapper(Color.GREEN,"green"),
					new ColorWrapper(Color.GRAY,"gray")};
			_backgroundColors = new JComboBox(colors);
			_backgroundColors.setPreferredSize(new Dimension(marginTools,15));
			_backgroundColors.addActionListener(new ActionListener(){
				public void actionPerformed(ActionEvent arg0) {
					_mainSurface.setBackground((Color)_backgroundColors.getSelectedItem());
				}});*/
			_backgroundColorLabel.setHorizontalTextPosition(JLabel.LEFT);
			_numPeriodSlider.setPreferredSize(new Dimension(100,20));
			_numPeriodSlider.addChangeListener(new ChangeListener(){
				public void stateChanged(ChangeEvent arg0) {
					_mainSurface.setNumPeriod(_numPeriodSlider.getValue());
				}});
			_backgroundColorPanel.setLayout(new FlowLayout(FlowLayout.LEFT));
			_backgroundColorRSlider.setPreferredSize(new Dimension(60,20));
			_backgroundColorRSlider.addChangeListener(new ChangeListener(){
				public void stateChanged(ChangeEvent arg0) {
					_mainSurface.setBackground(new Color(_backgroundColorRSlider.getValue()*16+15,
								_backgroundColorGSlider.getValue()*16+15,
								_backgroundColorBSlider.getValue()*16+15));
				}});
			_backgroundColorGSlider.setPreferredSize(new Dimension(60,20));
			_backgroundColorGSlider.addChangeListener(new ChangeListener(){
				public void stateChanged(ChangeEvent arg0) {
					_mainSurface.setBackground(new Color(_backgroundColorRSlider.getValue()*16+15,
								_backgroundColorGSlider.getValue()*16+15,
								_backgroundColorBSlider.getValue()*16+15));
				}});
			_backgroundColorBSlider.setPreferredSize(new Dimension(60,20));
			_backgroundColorBSlider.addChangeListener(new ChangeListener(){
				public void stateChanged(ChangeEvent arg0) {
					_mainSurface.setBackground(new Color(_backgroundColorRSlider.getValue()*16+15,
								_backgroundColorGSlider.getValue()*16+15,
								_backgroundColorBSlider.getValue()*16+15));
				}});
			_backgroundColorPanel.add(_backgroundColorLabel);
			_backgroundColorPanel.add(_backgroundColorRLabel);
			_backgroundColorPanel.add(_backgroundColorRSlider);
			_backgroundColorPanel.add(_backgroundColorGLabel);
			_backgroundColorPanel.add(_backgroundColorGSlider);
			_backgroundColorPanel.add(_backgroundColorBLabel);
			_backgroundColorPanel.add(_backgroundColorBSlider);
			_backgroundColorPanel.add(_numPeriodLabel);
			_backgroundColorPanel.add(_numPeriodSlider);

			_title.setMinimumSize(new Dimension(50,22));
			_title.setColumns(15);
			_title.getDocument().addDocumentListener(new DocumentListener(){
				public void changedUpdate(DocumentEvent arg0) {
					_mainSurface.setTitle(_title.getText());
				}
				public void insertUpdate(DocumentEvent arg0) {
					_mainSurface.setTitle(_title.getText());
				}
				public void removeUpdate(DocumentEvent arg0) {
					_mainSurface.setTitle(_title.getText());
				}});
			_titleSizeSlider.addChangeListener(new ChangeListener(){
				public void stateChanged(ChangeEvent arg0) {
					_mainSurface.setTitleFontSize(_titleSizeSlider.getValue());
				}});
			_titleSizeSlider.setPreferredSize(new Dimension(130,20));
			_circularCheckbox.addActionListener(new ActionListener(){
				public void actionPerformed(ActionEvent arg0) {
					if(_circularCheckbox.isSelected())
					{
						_mainSurface.redoLayout(VARNAPanel.DRAW_MODE_CIRCLE);
					}
					else
					{
						_mainSurface.redoLayout(VARNAPanel.DRAW_MODE_RADIATE);
					}						
					
				}});
			_titlePanel.setLayout(new FlowLayout(FlowLayout.LEFT));
			_titlePanel.add(_titleLabel);
			_titlePanel.add(_title);
			_titlePanel.add(_titleSizeLabel);
			_titlePanel.add(_titleSizeSlider);
			_titlePanel.add(_circularCheckbox);
			
			
			_mainSurface.setTitle(_title.getText());
			
			
		    _tools.setLayout(new BorderLayout());
		    _tools.add(_input,BorderLayout.CENTER);
		    //_tools.add(_go,BorderLayout.EAST);
		    _input.setLayout(new GridLayout(4,0));
		    _input.add(_seqPanel);
		    _input.add(_structPanel);
		    _input.add(_backgroundColorPanel);
		    _input.add(_titlePanel);
		    
		    _go.addActionListener(new ActionListener(){

				public void actionPerformed(ActionEvent arg0) {
					_mainSurface.setRNA(_seq.getText(), _struct.getText());			
				}});
		    

			getContentPane().setLayout(new BorderLayout());
			getContentPane().add(_mainSurface,BorderLayout.CENTER);
			getContentPane().add(_tools,BorderLayout.SOUTH);

		    setVisible(true);

		}
		
		public String[][] getParameterInfo() {
		    String[][] info = {
		      // Parameter Name     Kind of Value   Description
		        {"background",      "Color",        "A color for the background"},
		        {"sequenceDBN",     "String",       "A raw RNA sequence"},
		        {"structureDBN",    "String",       "An RNA structure in dot bracket notation (DBN)"},
		        {"periodNum",       "int",          "Interval between two consecutive base numbers"},
		    };
		    return info;
		}    
		
        public void init()
        {
			retrieveParametersValues();
			_mainSurface.setRNA(_sseq, _sstruct);
			_mainSurface.setBackground(_backgroundColor);
			_mainSurface.setNumPeriod(_periodResNum);
			_seq.setText(_mainSurface.getSeq());
			_struct.setText(_mainSurface.getStructDBN());
        }
		
        private Color getSafeColor(String col, Color def)
        {
			Color result;
        	try
        	{
        		result = Color.decode(col);
				
        	} 
        	catch(Exception e) 
			{
				try
				{
					result = Color.getColor(col,def);
				}
				catch(Exception e2)
				{
					System.err.println("'"+col+"' is not a valid color !");
					return def;
				}
			}
        	return result;
        }
		
        
		private String getParameterValue(String key, String def)
		{
			String tmp;
			tmp = getParameter(key);
			if (tmp==null)
			{
				return def;
			}
			else
			{
				return tmp;
			}
		}
		
		private void retrieveParametersValues()
		{
			String tmp2 = getParameterValue("background",_backgroundColor.toString());
			_backgroundColor = getSafeColor(tmp2,_backgroundColor);
			_sseq = getParameterValue("sequenceDBN",_sseq);
			_sstruct = getParameterValue("structureDBN",_sstruct);
			try
			{_periodResNum = Integer.parseInt(getParameterValue("periodNum",""+_periodResNum));}
			catch(Exception e)
			{ System.err.println("'"+getParameterValue("periodNum",""+_periodResNum)+"' is not a integer value for the period of residue numbers !");}
		}
		
		private void getRNA()
		{
			if (! _mainSurface.setRNA(_seq.getText(),_struct.getText()))
			{
				_seq.setForeground(Color.red);
				_struct.setForeground(Color.red);
			}
			else
			{
				_seq.setForeground(Color.black);
				_struct.setForeground(Color.black);
			}

		}
		
		/**
		 * @param args
		 */
		public static void main(String[] args) {
			// TODO Auto-generated method stub
		      VARNAOfflineDemo mainFrame = new VARNAOfflineDemo();
		      
		}
  private class ColorWrapper extends Color{
    String _name;
    public ColorWrapper(Color c,String name){
    	super(c.getRGB());
    	_name = name;
    }
    
    public String toString()
    {
    	return _name;
    }
  }

}
