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

import java.io.FileWriter;
import java.util.Vector;

import Varna.varna.export.SecStrDrawingProducer;
import Varna.varna.export.PSExport;
import Varna.varna.export.XFIGExport;

public class EmbeddedRNA {
	
	public RNASecStr _str;
	public Embedding _em;
	
	static int BASE_RADIUS = 10; 
	static double LOOP_DISTANCE = 40.0;
	static double BASE_PAIR_DISTANCE = 45.0;
	static double MULTILOOP_DISTANCE = 35.0;
	
	
	
	public EmbeddedRNA()
	{
		_str = new RNASecStr("CAGCUAACG");
		_em = EmbeddedRNA.drawRNASecStr(_str);
	}

	public EmbeddedRNA(RNASecStr s)
	{
	  _str = s;
	  _em = EmbeddedRNA.drawRNASecStr(s);
	}

	public EmbeddedRNA(RNASecStr s, Embedding em)
	{
	  _str = s;
	  _em = em;
	}

	
	public EmbeddedRNA(RNASecStr s, RealPoint[] coords)
	{
	  Embedding em = new Embedding();
	  em._coords = coords;
	  _str = s;
	  _em = em;
	}

	
	public void saveEmbeddedRNADBN(String path, String title)
	{
		 try
		 {
			FileWriter out = new FileWriter(path);
			if (!title.equals(""))
			{
				out.write("> "+title+"\n");				
			}
			out.write(_str._seq);
			out.write('\n');
			String str = "";
			for (int i=0;i<_str._str.length;i++)
			{
				if (_str._str[i]==-1)
				{
					str += '.';
				}
				else
				{
					if (_str._str[i]>i)
					{
						str += '(';
					}
					else
					{
						str += ')';
					}
				}
			}
			out.write(str);
			out.write('\n');
			out.close();
		 }
		 catch(Exception e)
		 {
			System.err.println("Writing is not allowed with current security policy."); 
		 }
	}
	
	private void saveEmbeddedRNA(String path, String title, int freqNumbers, double scale, SecStrDrawingProducer out)
	{
		 try
		 {
			 out.setScale(scale);
			 
			 // Computing bounding boxes
			 double EPSMargin = 40;
			 double minX = Double.MAX_VALUE;
			 double maxX = Double.MIN_VALUE;
			 double minY = Double.MAX_VALUE;
			 double maxY = Double.MIN_VALUE;
			 for (int i=0;i<_em._coords.length;i++)
			 {
			   minX = Math.min(minX, (_em._coords[i].x-BASE_RADIUS-EPSMargin));
			   minY = Math.min(minY, -(_em._coords[i].y-BASE_RADIUS-EPSMargin));
			   maxX = Math.max(maxX, (_em._coords[i].x+BASE_RADIUS+EPSMargin));
			   maxY = Math.max(maxY, -(_em._coords[i].y+BASE_RADIUS+EPSMargin));
			 }
			 
			 // Drawing backbone
			 for (int i=1;i<_em._coords.length;i++)
			 {
			   long x0 = (long)(_em._coords[i-1].x-minX);
			   long y0 = -(long)(_em._coords[i-1].y-minY);
			   long x1 = (long)(_em._coords[i].x-minX);
			   long y1 = -(long)(_em._coords[i].y-minY);
			   out.drawLine(x0, y0, x1, y1,1);
			 }

			 // Drawing bonds
			 for (int i=0;i<_em._coords.length;i++)
			 {
			   if (_str._str[i]>i)
			   {
				 double x0 = (_em._coords[i].x-minX);
			     double y0 = -(_em._coords[i].y-minY);
			     double x1 = (_em._coords[_str._str[i]].x-minX);
			     double y1 = -(_em._coords[_str._str[i]].y-minY);
				   double dx = x1-x0;
				   double dy = y1-y0;
				   double norm = Math.sqrt(dx*dx+dy*dy);
				   dx /= norm;
				   dy /= norm;
				   if (_em._style==VARNAPanel.DRAW_MODE_CIRCLE)
				   {
					   out.drawLine((long)x0, (long)y0, (long)x1, (long)y1,1);
				   }
				   else if(_em._style==VARNAPanel.DRAW_MODE_RADIATE)
				   {
					   out.drawLine((long)(x0+1.5*BASE_RADIUS*dx), (long)(y0+1.5*BASE_RADIUS*dy), (long)(x1-1.5*BASE_RADIUS*dx), (long)(y1-1.5*BASE_RADIUS*dy),2);				   
				   }   
			   }
			 }
			 
			 // Drawing Bases
             long baseFontSize = (long)(1.5*BASE_RADIUS);
			 out.setFont(PSExport.FONT_HELVETICA_BOLD,baseFontSize);
			 for (int i=0;i<_em._coords.length;i++)
			 {
			   long x = (long)(_em._coords[i].x-minX);
			   long y = -(long)(_em._coords[i].y-minY);
			   out.fillCircle(x, y, BASE_RADIUS, 1l,0.95);
			   out.drawCircle(x, y, BASE_RADIUS, 1l);
			   out.drawText(x, y, ""+_str._seq[i],baseFontSize);
			 }

			 // Drawing base numbers
             long numFontSize = (long)(1.5*BASE_RADIUS);
			 out.setFont(PSExport.FONT_HELVETICA_BOLD,numFontSize);

			 for (int i=0;i<_em._coords.length;i++)
			 {
				 if ((i==0)||((i+1)%freqNumbers==0))
				 {
					 double x0 = (_em._coords[i].x-minX);
					 double y0 = -(_em._coords[i].y-minY);
					 double x1 = (_em._centers[i].x-minX);
					 double y1 = -(_em._centers[i].y-minY);
					 double dx = x1-x0;
					 double dy = y1-y0;
					 double norm = Math.sqrt(dx*dx+dy*dy);
					 dx /= norm;
					 dy /= norm;
					 out.drawLine((long)(x0-1.5*BASE_RADIUS*dx), (long)(y0-1.5*BASE_RADIUS*dy), (long)(x0-2.5*BASE_RADIUS*dx), (long)(y0-2.5*BASE_RADIUS*dy),1);
					 out.drawText((long)(x0-4*BASE_RADIUS*dx), (long)(y0-4*BASE_RADIUS*dy), ""+(i+1),numFontSize);
				 }
			 }

			 
			 FileWriter fout = new FileWriter(path);
			 fout.write(out.export());
			 fout.close();
		 }

		 catch(Exception e)
		 {
			 
		 }
	}
	
	public void saveEmbeddedRNAEPS(String path, String title, int freqNumbers)
	{
		 PSExport out = new PSExport();
		 saveEmbeddedRNA(path,title,freqNumbers,1,out);
	}
	
	public void saveEmbeddedRNAXFIG(String path, String title, int freqNumbers)
	{
		 XFIGExport out = new XFIGExport();
		 saveEmbeddedRNA(path,title,freqNumbers,20,out);
	}

	
	public void loadEmbeddedRNA(String path)
	{
		
	}
	
	public RealRectangle getBBox()
	{
		RealRectangle result = new RealRectangle(11,10,10,10);
		RealPoint[] _coords = _em._coords;
		double minx,maxx,miny,maxy;
		minx = Double.MAX_VALUE; 
		miny = Double.MAX_VALUE; 
		maxx = Double.MIN_VALUE; 
		maxy = Double.MIN_VALUE; 
		for (int i=0;i<_coords.length;i++)
		{
			minx = Math.min(_coords[i].x-BASE_RADIUS,minx);
			miny = Math.min(_coords[i].y-BASE_RADIUS,miny);
			maxx = Math.max(_coords[i].x+BASE_RADIUS,maxx);
			maxy = Math.max(_coords[i].y+BASE_RADIUS,maxy);
		}
		result.x = minx;
		result.y = miny;
		result.width = (maxx - minx);
		result.height = (maxy - miny);
		return result;
	}
	
	public int getSize()
	{
		return _str.getSize();
	}

	public RNASecStr getRNASecStr()
	{
		return _str;
	}

	public void setCoord(int index, RealPoint p)
	{
		setCoord(index,p.x,p.y);
	}

	public void setCoord(int index, double x, double y)
	{
		if (index<_str.getSize())
		{
			RealPoint[] _coords = _em._coords;
			_coords[index].x = x;
			_coords[index].y = y;
		}
	}

	public RealPoint getCoord(int i)
	{
		if (i<_str.getSize())
		{
			RealPoint[] _coords = _em._coords;
			return _coords[i];
		}
		return new RealPoint();
	}

	public RealPoint getCenter(int i)
	{
		if (i<_str.getSize())
		{
			RealPoint[] _centers = _em._centers;
			return _centers[i];
		}
		return new RealPoint();
	}

	public char getRes(int i)
	{
		if (i<_str.getSize())
		{
			return _str.getRes(i);
		}
		return 'E';
	}

	public int getBP(int i)
	{
		if (i<_str.getSize())
		{
			return _str.getBP(i);
		}
		return -1;
	}

	public static Embedding drawRNASecStrCircle(RNASecStr s)
	{
		Embedding result = new Embedding();
		RealPoint[] coords = new RealPoint[s.getSize()];
		RealPoint[] centers = new RealPoint[s.getSize()];
		int radius = (int)((3*(coords.length+1)*BASE_RADIUS)/(2*Math.PI));
		for (int i=0;i<coords.length;i++)
		{
			double angle = -((((double)-(i+1))*2.0*Math.PI)/((double)(coords.length+1))-Math.PI/2.0);
			coords[i] = new RealPoint((radius*Math.cos(angle)),(radius*Math.sin(angle)));
			centers[i] = new RealPoint(0.0,0.0);
		}
		result._coords=coords;
		result._centers=centers;
		result._style = VARNAPanel.DRAW_MODE_CIRCLE;
		return result;
	}

	
	private static void drawLoop(int i, int j, double x, double y, double dirAngle,RNASecStr s, RealPoint[] coords, RealPoint[] centers)
	{
		if(i>=j)
		{return ;}
		if (s.getBP(i)==j)
		{
			double normalAngle = Math.PI/2.0;
			centers[i] = new RealPoint(x,y);
			centers[j] = new RealPoint(x,y);
			coords[i].x = (x+BASE_PAIR_DISTANCE*Math.cos(dirAngle-normalAngle)/2.0);
			coords[i].y = (y+BASE_PAIR_DISTANCE*Math.sin(dirAngle-normalAngle)/2.0);
			coords[j].x = (x+BASE_PAIR_DISTANCE*Math.cos(dirAngle+normalAngle)/2.0);
			coords[j].y = (y+BASE_PAIR_DISTANCE*Math.sin(dirAngle+normalAngle)/2.0);
			drawLoop(i+1, j-1, x+LOOP_DISTANCE*Math.cos(dirAngle), 
					           y+LOOP_DISTANCE*Math.sin(dirAngle), dirAngle,s, coords, centers);
		}
		else
		{
			int k = i;
			Vector<Integer> basesMultiLoop = new Vector<Integer>();
			Vector<Integer> helices = new Vector<Integer>();
			while(k<=j)
			{
				int l = s.getBP(k);
				if (l>k)
				{
					basesMultiLoop.add(new Integer(k));
					basesMultiLoop.add(new Integer(l));
					helices.add(new Integer(k));
					k = l+1;
				}
				else
				{
					basesMultiLoop.add(new Integer(k));
					k++;
				}
			}
			int mlSize = basesMultiLoop.size()+2;
			int numHelices = helices.size()+1;
			double totalLength = MULTILOOP_DISTANCE*(mlSize-numHelices)+BASE_PAIR_DISTANCE*numHelices;
			double multiLoopRadius = (totalLength)/(2.0*Math.PI);
			
			double centerDist = Math.sqrt(Math.pow(multiLoopRadius,2)-Math.pow(BASE_PAIR_DISTANCE/2.0,2));
			RealPoint mlCenter = new RealPoint(
					(x+(centerDist-LOOP_DISTANCE)*Math.cos(dirAngle)),
					(y+(centerDist-LOOP_DISTANCE)*Math.sin(dirAngle)));
			
			double angleIncrementML = -2.0*Math.PI*(MULTILOOP_DISTANCE/totalLength);
			double angleIncrementBP = -2.0*Math.PI*(BASE_PAIR_DISTANCE/totalLength);
			double baseAngle = dirAngle +Math.PI+ 0.5*angleIncrementBP+1.0*angleIncrementML;
			double[] angles = new double[s.getSize()];
			for(k=basesMultiLoop.size()-1;k>=0;k--)
			{
				int l = basesMultiLoop.get(k).intValue();
				centers[l] = mlCenter;
				angles[l] = baseAngle;
				coords[l].x =  mlCenter.x + multiLoopRadius*Math.cos(baseAngle);
				coords[l].y =  mlCenter.y + multiLoopRadius*Math.sin(baseAngle);
				if ((s.getBP(l)<l)&&(s.getBP(l)!=-1))
				{ baseAngle += angleIncrementBP; }
				else
				{ baseAngle += angleIncrementML; }
			}
			for(k=0;k<helices.size();k++)
			{
				int m = helices.get(k).intValue();
				int n = s.getBP(m);
				double newAngle = (angles[m]+angles[n])/2.0;
				drawLoop(m+1, n-1, 
						  (LOOP_DISTANCE*Math.cos(newAngle))+(coords[m].x+coords[n].x)/2.0, 
						  (LOOP_DISTANCE*Math.sin(newAngle))+(coords[m].y+coords[n].y)/2.0, 
						  newAngle, s, coords, centers);
				
			}
		}
	}
	
	
	public static Embedding drawRNASecStr(RNASecStr s)
	{
		Embedding result = new Embedding();
		RealPoint[] coords = new RealPoint[s.getSize()];		
		for (int i=0;i<coords.length;i++)
		{
			coords[i] = new RealPoint(0,0);
		}
		RealPoint[] centers = new RealPoint[s.getSize()];		
		for (int i=0;i<centers.length;i++)
		{
			centers[i] = new RealPoint(0,0);
		}
		double initialAngle = -1.0;
		EmbeddedRNA.drawLoop(0,s.getSize()-1,0,0,initialAngle,s,coords,centers);
		
		result._coords = coords;
		result._centers = centers;
		result._style = VARNAPanel.DRAW_MODE_RADIATE;
		return result;
	}
	
	public Range getHelix(int i)
	{
		if (_str != null)
		{
			return _str.getHelix(i);
		}
		return new Range(0,0);
	}
	
	public Range getMultiLoop(int i)
	{
		if (_str != null)
		{
			return _str.getMultiLoop(i);
		}
		return new Range(0,0);
	}
	
}

