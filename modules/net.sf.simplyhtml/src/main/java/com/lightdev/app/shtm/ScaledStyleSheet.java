/*
 *  Freeplane - mind map editor
 *  Copyright (C) 2012 Freeplane team and others
 *
 *  this file is created by Dimitry Polivaev in 2012.
 *
 *  This program is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 2 of the License, or
 *  (at your option) any later version.
 *
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.lightdev.app.shtm;

import java.awt.Font;
import java.awt.Toolkit;

import javax.swing.text.AttributeSet;
import javax.swing.text.html.CSS;
import javax.swing.text.html.StyleSheet;


public class ScaledStyleSheet extends StyleSheet{
	
    /**
     * 
     */
    private static final long serialVersionUID = 1L;
	public Font getFont(AttributeSet a) {
	    final Font font = super.getFont(a);
	    final float fontScaleFactor = getFontScaleFactor(a);
	    return super.getFont(font.getFamily(), font.getStyle(), Math.round(font.getSize2D() * fontScaleFactor));
    }

	public static float FONT_SCALE_FACTOR;
	static {
		float factor = 1f; 
		try {
	        factor = Toolkit.getDefaultToolkit().getScreenResolution()  / 72f;
        }
        catch (Exception e) {
        }
		FONT_SCALE_FACTOR = factor;
	}
	
	private float getFontScaleFactor(AttributeSet a) {
		final Object attribute = a.getAttribute(CSS.Attribute.FONT_SIZE);
		if(attribute == null)
			return FONT_SCALE_FACTOR;
		final String fontSize = attribute.toString();
		final int fsLength = fontSize.length();
		if(fsLength <= 1 
				|| Character.isDigit(fontSize.charAt(fsLength-1))
				|| fontSize.endsWith("pt"))
			return FONT_SCALE_FACTOR;
		if(fontSize.endsWith("px"))
			return 1/1.3f;
		if(fontSize.endsWith("%") || fontSize.endsWith("em") || fontSize.endsWith("ex")
				|| fontSize.endsWith("er"))
			return getFontScaleFactor(a.getResolveParent());
		return FONT_SCALE_FACTOR;
    }


}