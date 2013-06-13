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
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
/**
 * An RNA 2d Panel demo applet
 * @author Yann Ponty
 *
 */


public class VARNA extends JApplet {
		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;

		VARNAPanel _mainSurface = new VARNAPanel();
		
		
		public VARNA()
		{
			super();
			RNAPanelDemoInit();
		}
	
		private void RNAPanelDemoInit()
		{
			_mainSurface.setBackground(_backgroundColor);
			getContentPane().add(_mainSurface,BorderLayout.CENTER);
		    setVisible(true);

		}
		// Applet assignable parameters
		private String _algo = "radiate";
		private Color _backgroundColor = Color.white;
		private String _sseq =    "CAGCACGACACUAGCAGUCAGUGUCAGACUGCAIACAGCACGACACUAGCAGUCAGUGUCAGACUGCAIACAGCACGACACUAGCAGUCAGUGUCAGACUGCAIA";
		private String _sstruct = "..(((((...(((((...(((((...(((((.....)))))...))))).....(((((...(((((.....)))))...))))).....)))))...)))))..";
		private int _periodResNum = 5;
		private int _numRows = 1;
		private int _numColumns = 1;
		private String _title ="";
		private int _titleSize = 18;
		private Color _backboneColor = Color.black;
		private Color _bondColor = Color.blue;
		private Color _baseOutlineColor = Color.darkGray;
		private Color _baseInnerColor = new Color(230,230,230);
		private Color _baseNumColor = Color.darkGray;
		private Color _baseNameColor = Color.black;
		private Color _titleColor = Color.black;

		
        public void init()
        {
			retrieveParametersValues();
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
		private String numColumnsOpt = "rows"; 
		private String numRowsOpt = "columns"; 
		private String algoOpt = "algorithm"; 
		private String backboneColorOpt = "backbone"; 
		private String backgroundColorOpt = "background"; 
		private String baseInnerColorOpt = "baseInner"; 
		private String baseNameColorOpt = "baseName"; 
		private String baseNumbersColorOpt = "baseNum"; 
		private String baseOutlineColorOpt = "baseOutline"; 
		private String bondColorOpt = "bp"; 
		private String periodNumOpt = "periodNum";
		private String sequenceOpt = "sequenceDBN"; 
		private String structureOpt = "structureDBN";
		private String titleOpt = "title";
		private String titleColorOpt = "titleColor";
		private String titleSizeOpt = "titleSize";
		
		
		
		public String[][] getParameterInfo() {
		    String[][] info = {
		      // Parameter Name     Kind of Value   Description
		        {algoOpt,               "String",        "The drawing algorithm"},
		        {backboneColorOpt,      "Color",        "A color for the backbone"},
		        {backgroundColorOpt,      "Color",        "A color for the background"},
		        {baseInnerColorOpt,      "Color",        "A color for the inner disc of a base drawing"},
		        {baseNameColorOpt,      "Color",        "A color for the residue"},
		        {baseNumbersColorOpt,      "Color",        "A color for the base numbers"},
		        {baseOutlineColorOpt,      "Color",        "A color for the outer circle of a base drawing"},
		        {bondColorOpt,      "Color",        "A color for the hydrogen bonds"},
		        {sequenceOpt,     "String",       "A raw RNA sequence"},
		        {structureOpt,    "String",       "An RNA structure in dot bracket notation (DBN)"},
		        {periodNumOpt,       "int",          "Interval between two consecutive base numbers"},
		        {titleOpt,    "String",       "A title for this RNA drawing"},
		        {titleColorOpt,    "Color",       "A color for the title"},
		        {titleSizeOpt,    "int",       "A font sized for the title"},
		    };
		    return info;
		}    

		
		private void retrieveParametersValues()
		{
			try
			{_numRows = Integer.parseInt(getParameterValue(numRowsOpt,""+_numRows));}
			catch(Exception e)
			{ System.err.println("'"+getParameterValue(numRowsOpt,""+_numRows)+"' is not a integer value for the number of rows !");}
			try
			{_numColumns = Integer.parseInt(getParameterValue(numColumnsOpt,""+_numColumns));}
			catch(Exception e)
			{ System.err.println("'"+getParameterValue(numColumnsOpt,""+_numColumns)+"' is not a integer value for the number of rows !");}
				getContentPane().removeAll();
				getContentPane().setLayout(new GridLayout(_numColumns,_numRows));
				for(int i=0;i<_numColumns;i++)
				{
					for(int j=0;j<_numRows;j++)
					{
						int x = 1+j+i*_numRows;
						String n=""+x;
						if((_numColumns==1)&&(_numRows==1))
						{
							n="";
						}
				        _algo = getParameterValue(algoOpt+n,_algo);
						_backboneColor = getSafeColor(getParameterValue(backboneColorOpt+n,_backboneColor.toString()),_backboneColor);
				        _backgroundColor = getSafeColor(getParameterValue(backgroundColorOpt+n,_backgroundColor.toString()),_backgroundColor);
				        _bondColor = getSafeColor(getParameterValue(bondColorOpt+n,_bondColor.toString()),_bondColor);
				        _baseInnerColor = getSafeColor(getParameterValue(baseInnerColorOpt+n,_baseInnerColor.toString()),_baseInnerColor);
				        _baseNameColor = getSafeColor(getParameterValue(baseNameColorOpt+n,_baseNameColor.toString()),_baseNameColor);
				        _baseNumColor = getSafeColor(getParameterValue(baseNumbersColorOpt+n,_baseNumColor.toString()),_baseNumColor);
				        _baseOutlineColor = getSafeColor(getParameterValue(baseOutlineColorOpt+n,_baseOutlineColor.toString()),_baseOutlineColor);
				        _sseq = getParameterValue(sequenceOpt+n,_sseq);
						_sstruct = getParameterValue(structureOpt+n,_sstruct);
						try
						{_periodResNum = Integer.parseInt(getParameterValue(periodNumOpt+n,""+_periodResNum));}
						catch(Exception e)
						{ System.err.println("'"+getParameterValue(periodNumOpt+n,""+_periodResNum)+"' is not a integer value for the period of residue numbers !");}
						_title = getParameterValue(titleOpt+n,_title);
				        _titleColor = getSafeColor(getParameterValue(titleColorOpt+n,_titleColor.toString()),_titleColor);
						try
						{_titleSize = Integer.parseInt(getParameterValue(titleSizeOpt+n,""+_titleSize));}
						catch(Exception e)
						{ System.err.println("'"+getParameterValue(titleSizeOpt+n,""+_titleSize)+"' is not a integer value for the number of rows !");}
                        //System.out.println(n+" "+_algo);
				        VARNAPanel mainSurface = new VARNAPanel();
				        int algoCode = (_algo.equals("circular")?VARNAPanel.DRAW_MODE_CIRCLE:VARNAPanel.DRAW_MODE_RADIATE);
				        mainSurface.setRNA(_sseq, _sstruct, algoCode);
						mainSurface.setBackground(_backgroundColor);
						mainSurface.setNumPeriod(_periodResNum);
						mainSurface.setBackboneColor(_backboneColor);
						mainSurface.setBondColor(_bondColor);
						mainSurface.setBaseOutlineColor(_baseOutlineColor);
						mainSurface.setBaseInnerColor(_baseInnerColor);
						mainSurface.setBaseNumbersColor(_baseNumColor);
						mainSurface.setBaseNameColor(_baseNameColor);
						mainSurface.setTitleFontColor(_titleColor);
						mainSurface.setTitle(_title);
						mainSurface.setTitleFontSize(_titleSize);
						this.getContentPane().add(mainSurface);
					}
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
