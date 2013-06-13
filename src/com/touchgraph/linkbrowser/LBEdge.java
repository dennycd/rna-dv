/*
 * TouchGraph LLC. Apache-Style Software License
 *
 *
 * Copyright (c) 2002 Alexander Shapiro. All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions
 * are met:
 *
 * 1. Redistributions of source code must retain the above copyright
 *    notice, this list of conditions and the following disclaimer. 
 *
 * 2. Redistributions in binary form must reproduce the above copyright
 *    notice, this list of conditions and the following disclaimer in
 *    the documentation and/or other materials provided with the
 *    distribution.
 *
 * 3. The end-user documentation included with the redistribution,
 *    if any, must include the following acknowledgment:  
 *       "This product includes software developed by 
 *        TouchGraph LLC (http://www.touchgraph.com/)."
 *    Alternately, this acknowledgment may appear in the software itself,
 *    if and wherever such third-party acknowledgments normally appear.
 *
 * 4. The names "TouchGraph" or "TouchGraph LLC" must not be used to endorse 
 *    or promote products derived from this software without prior written 
 *    permission.  For written permission, please contact 
 *    alex@touchgraph.com
 *
 * 5. Products derived from this software may not be called "TouchGraph",
 *    nor may "TouchGraph" appear in their name, without prior written
 *    permission of alex@touchgraph.com.
 *
 * THIS SOFTWARE IS PROVIDED ``AS IS'' AND ANY EXPRESSED OR IMPLIED
 * WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES
 * OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED.  IN NO EVENT SHALL TOUCHGRAPH OR ITS CONTRIBUTORS BE 
 * LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR 
 * CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF 
 * SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR 
 * BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, 
 * WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE 
 * OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, 
 * EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 * ====================================================================
 *
 */

package com.touchgraph.linkbrowser;

import com.touchgraph.graphlayout.TGPanel;
import java.io.*;
import java.awt.*;

/**  LBEdge:  A LinkBrowser Edge.  Extends edge by adding different edge types, specifically a bidirectional
  *  edge type rendered as a thin line.  
  *
  *  @author   Alexander Shapiro                                        
  *  @version  1.20
  */

public class LBEdge extends com.touchgraph.graphlayout.Edge {
    public final static int BIDIRECTIONAL_EDGE=0;
    public final static int HIERARCHICAL_EDGE=1;
    
    public static int DEFAULT_TYPE = 1;
    
	public int edgeType;

    public LBEdge(LBNode f, LBNode t) {
        this(f, t, DEFAULT_LENGTH);
	}	

    public LBEdge(LBNode f, LBNode t, int len) {
        super(f,t,len);
        edgeType = DEFAULT_TYPE;
	}	


    public void setType(int t) { 
        edgeType = t;
    }
    
    public static void setEdgeDafaultType(int type) { DEFAULT_TYPE = type; }
        
    public int getType() { 
        return edgeType;
    }

	public static void paintFatLine(Graphics g, int x1, int y1, int x2, int y2, Color c) {  
	    g.setColor(c);  
        g.drawLine(x1,   y1,   x2,   y2);
        g.drawLine(x1+1, y1,   x2+1, y2);
        g.drawLine(x1+1, y1+1, x2+1, y2+1);
        g.drawLine(x1,   y1+1, x2  , y2+1);
	}

    public static void paint(Graphics g, int x1, int y1, int x2, int y2, Color c, int type) {
        switch (type) {
            case BIDIRECTIONAL_EDGE:   paintFatLine(g, x1, y1, x2, y2, c); break;
            case HIERARCHICAL_EDGE:  /*paintArrow(g, x1, y1, x2, y2, c)*/
                paintFatLine(g, x1, y1, x2, y2, c);  break;
        }       
    }

	public void paint(Graphics g, TGPanel tgPanel) {
        Color c;
        
        if (tgPanel.getMouseOverN()==from || tgPanel.getMouseOverE()==this) 
            c = MOUSE_OVER_COLOR; 
        else
            c = col;        

            //c = Color.ORANGE;
        
		int x1=(int) from.drawx;
		int y1=(int) from.drawy;
		int x2=(int) to.drawx;
		int y2=(int) to.drawy;
		if (intersects(tgPanel.getSize())) {
            paint(g, x1, y1, x2, y2, c, edgeType);
		}
	}	
}