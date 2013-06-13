/**
 * 
 */
package Varna.varna.export;

/**
 * @author ponty
 *
 */
public class PSExport implements SecStrDrawingProducer{
  StringBuffer _s = new StringBuffer();
  long minX = Long.MAX_VALUE;
  long minY = Long.MAX_VALUE;
  long maxX = Long.MIN_VALUE;
  long maxY = Long.MIN_VALUE;
  double scale = 1.0;
  
  private void updateBoundingBox(long x, long y, long w, long h)
  {
	  maxX = Math.max(maxX, x+w);
	  minX = Math.min(minX, x);
	  maxY = Math.max(maxY, y+h);
	  minY = Math.min(minY, y);
  }
	
  private String PSMacros()
  {
	String setFontSize = 
	// Params [fontsize|...]
	"/setbasefont \n"+
    "{ /Helvetica-Bold findfont\n"+ // => [font|scale|...]
    "  exch scalefont\n"+	        // => [scaled_font|...]
    "  setfont \n"+                 // => [...]
    "  } def\n";
	
	String writeTextCentered = 
	// Params [txt|size|...]
	"/txtcenter \n"+
    "{ dup \n"+                  // => [txt|txt|size|...]
    "  stringwidth pop\n"+       // => [wtxt|txt|size|...]
    "  2 div neg \n"+            // => [-wtxt/2|txt|size|...]
    "  3 -1 roll \n"+            // => [size|-wtxt/2|txt...]
    "  2 div neg\n"+             // => [-size/2|-wtxt/2|txt|...]
    "  rmoveto\n"+               // => [txt|...]
    "  show\n"+                  // => [...]
    "  } def\n";
	return setFontSize+writeTextCentered; 
  }

  private String EPSHeader()
  {
	String bbox = PSBBox();
	String macros = PSMacros();
	return bbox + macros;
  }

  private String EPSFooter()
  {
	String showpage = "showpage\n";
	return showpage;
  }
  
  
  private void PSNewPath()
  {
	  _s.append("newpath\n");
  }

  private void PSMoveTo(long x, long y)
  {
	  _s.append(""+x+" "+y+" moveto\n");
  }

  private void PSLineTo(long dx, long dy)
  {
	  _s.append(""+dx+" "+dy+" lineto\n");
  }
  
  private void PSRLineTo(long dx, long dy)
  {
	  _s.append(""+dx+" "+dy+" rlineto\n");
  }

  private void PSSetLineWidth(long thickness)
  {
	  _s.append(""+thickness+" setlinewidth\n");
  }

  private void PSStroke()
  {
	  _s.append("stroke\n");
  }

  private void PSArc(long x, long y, long radius, long angleFrom, long angleTo)
  {
	  _s.append(""+x+" "+y+" "+radius+" "+angleFrom+" "+angleTo+"  arc\n");
  }

  private String PSBBox()
  {
	  return ("%%BoundingBox: "+minX+" "+minY+" "+maxX+" "+maxY+"\n");
  }

  private void PSText(String txt)
  {
	  _s.append("("+txt+")\n");
  }

  private void PSShow()
  {
	  _s.append("show\n");
  }

  private void PSClosePath()
  {
	  _s.append("show\n");
  }

  private void PSFill()
  {
	  _s.append("fill\n");
  }

  private void PSSetGray(double level)
  {
	  _s.append(""+level+" setgray\n");
  }

  

  private String fontName(int font)
  {
	  switch(font)
	  {
	    case (FONT_TIMES_ROMAN):
	    	return "/Times-Roman";
	    case (FONT_TIMES_BOLD): 
	     	return "/Times-Bold";
	    case (FONT_TIMES_ITALIC): 
	    	return "/Times-Italic";
	    case (FONT_TIMES_BOLD_ITALIC): 
	    	return "/Times-BoldItalic";
	    case (FONT_HELVETICA): 
	    	return "/Helvetica";
	    case (FONT_HELVETICA_BOLD): 
	    	return "/Helvetica-Bold";
	    case (FONT_HELVETICA_OBLIQUE):
	    	return "/Helvetica-Oblique";
	    case (FONT_HELVETICA_BOLD_OBLIQUE): 
	    	return "/Helvetica-BoldOblique";
	    case (FONT_COURIER): 
	    	return "/Courier";
	    case (FONT_COURIER_BOLD): 
	    	return "/Courier-Bold";
	    case (FONT_COURIER_OBLIQUE): 
	    	return "/Courier-Oblique";
	    case (FONT_COURIER_BOLD_OBLIQUE): 
	    	return "/Courier-BoldOblique";
	  }
	  return "/Helvetica";
  }  

  private void PSSetFont(int font,long size)
  {
	  size *= scale;
	  _s.append(fontName(font)+" findfont "+size+" scalefont setfont\n");
  }

  public void setFont(int font,long size)
  {
	  PSSetFont(font,size);
  }

  public void drawLine(long x0, long y0, long x1, long y1, long thickness)
  {
	  x0 *= scale;
	  y0 *= scale;
	  x1 *= scale;
	  y1 *= scale;
	  PSMoveTo(x0,y0);
      PSLineTo(x1,y1);
      PSSetLineWidth(thickness);
      PSStroke();
  }

  
  public void drawText(long x, long y, String txt, long height)
  {
	  x *= scale;
	  y *= scale;
	  PSMoveTo(x,y);
	  _s.append(""+(height-5)+" \n");
	  PSText(txt);
	  _s.append(" txtcenter\n");
  }

  public void drawRectangle(long x,long y, long w, long h, long thickness)
  {
    x *= scale;
	y *= scale;
    w *= scale;
	h *= scale;
    updateBoundingBox(x,y,w,h);
	PSNewPath();
    PSMoveTo(x,y);
    PSRLineTo(0,w);
    PSRLineTo(h,0);
    PSRLineTo(0,-w);
    PSRLineTo(-h,0);
    PSClosePath();
    PSSetLineWidth(thickness);
    PSStroke();
  }

  public void drawCircle(long x,long y, long radius, long thickness)
  {
    x *= scale;
    y *= scale;
    radius *= scale;
    updateBoundingBox(x-radius,y-radius,2*radius,2*radius);
	PSNewPath();
	PSArc(x,y, radius, 0,360);
	PSSetLineWidth(thickness);
	PSStroke();
  }

  public void fillCircle(long x,long y, long radius, long thickness,double graylevel)
  {
    x *= scale;
    y *= scale;
    radius *= scale;
    updateBoundingBox(x-radius,y-radius,2*radius,2*radius);
	PSNewPath();
	PSArc(x,y, radius, 0,360);
	PSSetLineWidth(thickness);
	PSSetGray(graylevel);
	PSFill();
	PSSetGray(0.0);
  }

  public void setScale(double sc)
  {
	  scale = sc;
  }
  
  public String export()
  {
	return EPSHeader()+_s.toString()+EPSFooter();
  }

  public void reset()
  {
	  _s = new StringBuffer();
	  minX = Long.MAX_VALUE;
	  minY = Long.MAX_VALUE;
	  maxX = Long.MIN_VALUE;
	  maxY = Long.MIN_VALUE;
	  scale = 1.0;
  }

}
