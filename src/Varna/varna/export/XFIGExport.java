package Varna.varna.export;

public class XFIGExport implements SecStrDrawingProducer {

	private double _scale=1.0;
	private int _font = SecStrDrawingProducer.FONT_TIMES_ROMAN;
	private long _fontsize = 10;
	
	private String XFIGHeader()
	{
		return 
		"#FIG 3.2\n"+
		"Landscape\n"+
		"Center\n"+
		"Inches\n"+
		"Letter  \n"+
		"100.00\n"+
		"Single\n"+
		"-2\n"+
		"1200 2\n";
	}
	
	StringBuffer buf = new StringBuffer();
	
	public void drawCircle(long x, long y, long radius, long thickness) {
		x *= _scale;
		y *= _scale;
		radius *= _scale;
        buf.append("1 3 0 "+thickness+" 0 7 50 -1 -1 0.000 1 0.0000 "+x+" "+y+" "+radius+" "+radius+" 1 1 1 1\n");
	}

	public void drawLine(long x0, long y0, long x1, long y1, long thickness) {
		x0 *= _scale;
		y0 *= _scale;
		x1 *= _scale;
		y1 *= _scale;
		
		buf.append("2 1 0 "+thickness+" 0 7 60 -1 -1 0.000 0 0 -1 0 0 2\n"+
		" "+x0+" "+y0+" "+x1+" "+y1+"\n");
	}

	public void drawRectangle(long x, long y, long w, long h, long thickness) {
		x *= _scale;
		y *= _scale;
		h *= _scale;
		w *= _scale;
		buf.append("2 2 0 1 0 7 50 -1 -1 0.000 0 0 -1 0 0 5\n"+
		"\t "+(x)+" "+(y)+" "+(x+w)+" "+(y)+" "+(x+w)+" "+(y+h)+" "+(x)+" "+(y+h)+" "+(x)+" "+(y)+"\n");
	}

	public void drawText(long x, long y, String txt, long height) {
		x *= _scale;
		y *= _scale;
		height *= _scale;
		buf.append("4 1 0 40 -1 "+_font+" "+_fontsize+" 0.0000 6 "+height+" "+height+" "+x+" "+(y+height/3)+" "+txt+"\\001\n");
	}

	public void fillCircle(long x, long y, long radius, long thickness,	double graylevel) 
	{
		int shade = (int)Math.round(graylevel*20);
		x *= _scale;
		y *= _scale;
		radius *= _scale;
		buf.append("1 3 0 1 0 7 50 0 "+shade+" 0.000 1 0.0000 "+x+" "+y+" "+radius+" "+radius+" 1 1 1 1\n");
	}

	public String export() {
		// TODO Auto-generated method stub
		return XFIGHeader()+buf;
	}


	public void reset() {
		buf = new StringBuffer();
	}

	public void setFont(int font, long size) {
		_font = font;
		_fontsize = size;
	}

	public void setScale(double sc) {
		_scale = sc;
	}

}
