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

package Varna.varna;


import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.text.ParseException;
import java.util.Stack;
import java.util.Vector;


public class RNASecStr {

	char[] _seq;
	int[] _str;
	
	public RNASecStr(char[] seq, int[] str)
	{
	  _seq = seq;
	  _str = str;
	}

	public RNASecStr(String seq, int[] str)
	{
		this(seq.toCharArray(),str);
	}

	public RNASecStr(String seq, String str) throws ParseException
	{
		this(seq.toCharArray(),parseStruct(str));
	}

	static int[] parseStruct(String str) throws ParseException
	{
		int[] result = new int[str.length()];
		Stack<Integer> p = new Stack<Integer>();
		for (int i=0;i<str.length();i++)
		{
			char c = str.charAt(i);
			if (c=='(')
			{ p.push(new Integer(i)); }
			else if (c=='.')
			{ result[i]= -1; }
			else if (c==')')
			{
				if(p.size()==0)
				{
					throw(new ParseException("Bad secondary structure (DBN format): Unmatched closing parentheses ')'",i+1));
				}
				int j = p.pop().intValue();
				result[i]=j;
				result[j]=i;
			}
		}
		if(p.size()!=0)
		{
			throw(new ParseException("Bad secondary structure (DBN format): Unmatched closing parentheses ')'",p.pop().intValue()+1));
		}
		return result;
	}
	
	public RNASecStr(String seq)
	{
	  _seq = seq.toCharArray();
	  _str = new int[seq.length()];
	  for (int i=0;i<seq.length();i++)
	  {
		  _str[i] = -1;
	  }
	}

	public char getRes(int i)
	{
		if (i<_seq.length)
		{
			return _seq[i];
		}
		return 'E';
	}
	
	public int getSize()
	{
		return _seq.length;
	}

	public int getBP(int i)
	{
		if (i<_seq.length)
		{
			return _str[i];
		}
		return -1;
	}
	
	public Range getHelix(int index)
	{
		if((index<0)||(index>=_str.length))
		{ return new Range(index,index);}
		int j = _str[index];
		if(j!=-1)
		{
			int minH = index;
			int maxH = index;
			if (j>index)
			{ maxH = j; }
			else
			{ minH = j; }
			boolean over = false;
			while (!over)
			{
				if((minH<0)||(maxH>=_str.length))
				{over = true;}
				else
				{
					if (_str[minH]==maxH)
					{minH--;maxH++;}
					else
					{over = true;}
				}	
			}
			minH++;
			maxH--;
			return new Range(minH,maxH);
		}
		return new Range(0,0);
	}

	public Range getMultiLoop(int index)
	{
		if((index<0)||(index>=_str.length))
		{ return new Range(index,index);}
		Range h = getHelix(index);
		int minH = h.begin-1;
		int maxH = h.end+1;
		boolean over = false;
		while (!over)
		{
			if(minH<0)
			{
				over = true;
				minH=0;
			}
			else
			{
				if(_str[minH]==-1)
				{minH--;}
				else if (_str[minH]<minH)
				{minH = _str[minH]-1;}
				else
				{over = true;}
			}	
		}
		over = false;
		while (!over)
		{
			if(maxH>_str.length-1)
			{
				over = true;
				maxH=_str.length-1;
			}
			else
			{
				if(_str[maxH]==-1)
				{maxH++;}
				else if (_str[maxH]>maxH)
				{maxH = _str[maxH]+1;}
				else
				{over = true;}
			}	
		}
		return new Range(minH,maxH);
	}

	public static RNASecStr loadFromFile(String path)
	{
		RNASecStr r = new RNASecStr("ACAGUAGC");
		return r;
	}
	
	public String getStructDBN()
	{
		String result = "";
		for(int i=0;i<_str.length;i++)
		{
			if(_str[i]==-1)
			{
				result += ".";
			}
			else if(_str[i]>i)
			{
				result += "(";
			}
			else				
			{
				result += ")";
			}
		}
		return result;
	}

	public String getStructBPSEQ()
	{
		String result = "";
		for(int i=0;i<_str.length;i++)
		{
			result += (i+1)+" "+_seq[i]+" "+(_str[i]+1)+"\n";
		}
		return result;
	}

	public String getStructCT()
	{
		String result = "";
		for(int i=0;i<_str.length;i++)
		{
			result += (i+1)+" "+_seq[i]+" "+i+" "+(i+2)+" "+(_str[i]+1)+" "+(i+1)+"\n";
		}
		return result;
	}

	public void exportBPSEQ(String path, String title)
	{
		try
		{
		  FileWriter f = new FileWriter(path);
		  f.write("# "+title+"\n");
		  f.write(this.getStructBPSEQ()+"\n");
		  f.close();
		}
		catch(IOException e)
		{
			System.err.println("Export failed!\n File '"+path+"' cannot be created or overwritten !");
		}
		catch(Exception egen)
		{
			System.err.println("Permission denied for security reason !\nConsider using the VARNA panel class in a signed context.");
		}
	}

	public void exportCT(String path, String title)
	{
		try
		{
		  FileWriter f = new FileWriter(path);
		  f.write(""+_seq.length+" "+title+"\n");
		  f.write(this.getStructCT()+"\n");
		  f.close();
		}
		catch(IOException e)
		{
			System.err.println("Export failed!\n File '"+path+"' cannot be created or overwritten !");
		}
		catch(Exception egen)
		{
			System.err.println("Permission denied for security reason !\nConsider using the VARNA panel class in a signed context.");
		}
	}

	public void exportDBN(String path, String title)
	{
		try
		{
		  FileWriter f = new FileWriter(path);
		  f.write("> "+title+"\n");
		  f.write(new String(_seq) + "\n");
		  f.write(getStructDBN()+"\n");
		  f.close();
		}
		catch(IOException e)
		{
			System.err.println("Export failed!\n File '"+path+"' cannot be created or overwritten !");
		}
		catch(Exception egen)
		{
			System.err.println("Permission denied for security reason !\nConsider using the VARNA panel class in a signed context.");
		}
	}
	
	public static RNASecStr loadSecStrBPSEQ(String path)
	{
		RNASecStr res = null;
		try
		{
			BufferedReader fr  = new BufferedReader(new FileReader(path));
			String line = fr.readLine();
			String seqTmp = "";
			Vector<Integer> strTmp = new Vector<Integer>();
			while(line!= null)
			{
    			line = line.trim();
				String[] tokens = line.split("\\s+");
				if (tokens.length==3)
				{
					try
					{
					  int bpFrom = (Integer.parseInt(tokens[0]))-1;
					  char base = tokens[1].charAt(tokens[1].length()-1);
					  int bpTo =   (Integer.parseInt(tokens[2]))-1;
					  if (bpFrom!=seqTmp.length())
					  {
						  System.err.println("Discontinuity detected between nucleotides "+(seqTmp.length())+" and "+(bpFrom+1)+"!");
						  System.err.println("Filling in missing portions with unpaired unknown 'X' nucleotides ...");
						  while(bpFrom!=seqTmp.length())
						  {
							  seqTmp += 'X';
							  strTmp.add(-1);							  
						  }
					  }
					  seqTmp += base;
					  strTmp.add(bpTo);
					}
					catch(Exception e3)
					{ } 
				}
				line = fr.readLine();
			}
			if (strTmp.size()!=0)
			{
				char[] seq = seqTmp.toCharArray();
				int[] str = new int[strTmp.size()];
				for(int i=0;i<strTmp.size();i++)
				{ str[i] = strTmp.elementAt(i).intValue(); }
				res = new RNASecStr(seq,str);
			}
		}
		catch(IOException e)
		{ System.err.println("Loading failed!\n File '"+path+"' cannot be read !");	}
		catch(Exception egen)
		{ System.err.println("Permission denied for security reason !\nConsider using the VARNA panel class in a signed context."); }
		return res;
	}

	public static RNASecStr loadSecStrCT(String path)
	{
		RNASecStr res = null;
		try
		{
			BufferedReader fr  = new BufferedReader(new FileReader(path));
			String line = fr.readLine();
			String seqTmp = "";
			Vector<Integer> strTmp = new Vector<Integer>();
			while(line!= null)
			{
				line = line.trim();
				String[] tokens = line.split("\\s+");
				if (tokens.length>=6)
				{
					try
					{
					  int bpFrom = (Integer.parseInt(tokens[0]))-1;
					  char base = tokens[1].charAt(tokens[1].length()-1);
					  int bpTo =   (Integer.parseInt(tokens[4]))-1;
					  Integer.parseInt(tokens[2]);
					  Integer.parseInt(tokens[3]);
					  Integer.parseInt(tokens[5]);
					  System.err.print("("+bpFrom+","+base+","+bpTo+")");
					  if (bpFrom!=seqTmp.length())
					  {
						  System.err.println("Discontinuity detected between nucleotides "+(seqTmp.length())+" and "+(bpFrom+1)+"!");
						  System.err.println("Filling in missing portions with unpaired unknown 'X' nucleotides ...");
						  while(bpFrom!=seqTmp.length())
						  {
							  seqTmp += 'X';
							  strTmp.add(-1);							  
						  }
					  }
					  seqTmp += base;
					  strTmp.add(bpTo);
					}
					catch(Exception e3)
					{} 
				}
				line = fr.readLine();
			}
			if (strTmp.size()!=0)
			{
				char[] seq = seqTmp.toCharArray();
				int[] str = new int[strTmp.size()];
				for(int i=0;i<strTmp.size();i++)
				{str[i] = strTmp.elementAt(i).intValue();}
				res = new RNASecStr(seq,str);
			}
		}
		catch(IOException e)
		{ System.err.println("Loading failed!\n File '"+path+"' cannot be read !");	}
		catch(Exception egen)
		{ System.err.println("Permission denied for security reason !\nConsider using the VARNA panel class in a signed context."); }
		return res;
	}

	public static RNASecStr loadSecStrDBN(String path)
	{
		try
		{
			BufferedReader fr  = new BufferedReader(new FileReader(path));
			String line = fr.readLine();
			String seqTmp = "";
			String strTmp = "";
			while((line!= null)&&(strTmp.equals("")))
			{
				line = line.trim();
				if (!line.startsWith(">"))
				{
					if (seqTmp.equals(""))
					{ seqTmp = line; }
					else
					{ strTmp = line; }	
				}
				line = fr.readLine();
			}
			if (strTmp.length()!=0)
			{ return new RNASecStr(seqTmp,strTmp); }
		}
		catch(IOException e)
		{ System.err.println("Loading failed!\n File '"+path+"' cannot be read !");	}
		catch(ParseException e)
		{ System.err.println("Unbalanced parentheses expression, cannot resolve secondary structure.");	}
		catch(Exception egen)
		{ System.err.println("Permission denied for security reason !\nConsider using the VARNA panel class in a signed context."); }
		return null;
	}
	
	public static RNASecStr loadSecStr(String path)
	{
		RNASecStr s = null;
		s = loadSecStrCT(path); 
		if (s != null)
		{
			System.err.println("CT format detected for file '"+path+"'. \nStructure successfully loaded !");
			return s;
		}
		else System.err.println("Not in CT format.");
			
		s = loadSecStrBPSEQ(path);
		if (s != null)
		{
			System.err.println("BPSEQ format detected for file '"+path+"'. \nStructure successfully loaded !");
			return s;
		}
		else System.err.println("Not in BPSEQ format.");
		s = loadSecStrDBN(path);
		if (s != null)
		{
			System.err.println("DBN format detected for file '"+path+"'. \nStructure successfully loaded !");
			return s;
		}
		else System.err.println("Not in DBN format.");
		System.err.println("Unknown format or syntax error in file '"+path+"'. \nLoading cancelled !");
		return s;
	}
	
}
