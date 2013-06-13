package Varna.varna.export;

public interface SecStrDrawingProducer {
	  public static final int FONT_TIMES_ROMAN = 0;
	  public static final int FONT_TIMES_BOLD = 1;
	  public static final int FONT_TIMES_ITALIC = 2;
	  public static final int FONT_TIMES_BOLD_ITALIC = 3;
	  public static final int FONT_HELVETICA = 16;
	  public static final int FONT_HELVETICA_OBLIQUE = 17;
	  public static final int FONT_HELVETICA_BOLD = 18;
	  public static final int FONT_HELVETICA_BOLD_OBLIQUE = 19;
	  public static final int FONT_COURIER = 12;
	  public static final int FONT_COURIER_BOLD = 13;
	  public static final int FONT_COURIER_OBLIQUE = 14;
	  public static final int FONT_COURIER_BOLD_OBLIQUE = 15;
	  public void drawLine(long x0, long y0, long x1, long y1, long thickness);
	  public void drawText(long x, long y, String txt, long height);
	  public void drawRectangle(long x,long y, long w, long h, long thickness);
	  public void drawCircle(long x,long y, long radius, long thickness);
	  public void fillCircle(long x,long y, long radius, long thickness,double graylevel);
	  public void setScale(double sc);
	  public void setFont(int font,long size);
	  public String export();
	  public void reset();
}
