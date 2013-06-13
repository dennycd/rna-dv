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

import java.awt.Point;


public class RealPoint {
  public double x;
  public double y;

  public RealPoint()
  {
	  x = 0.0;
	  y = 0.0;
  }

  
  public RealPoint(Point p)
  {
	  x = p.x;
	  y = p.y;
  }

  public RealPoint(double _x, double _y)
  {
	  x = _x;
	  y = _y;
  }
  
  public double distance(RealPoint p)
  { return Math.sqrt(Math.pow(x-p.x, 2)+Math.pow(y-p.y, 2));}
  
  public String toString()
  {
	  return "("+x+","+y+")";
  }
}
