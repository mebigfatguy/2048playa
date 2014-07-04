/** 2048 playa - a 2048 autonomous player. 
 * Copyright 2014 MeBigFatGuy.com 
 * Copyright 2014 Dave Brosius 
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); 
 * you may not use this file except in compliance with the License. 
 * You may obtain a copy of the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0 
 * 
 * Unless required by applicable law or agreed to in writing, 
 * software distributed under the License is distributed on an "AS IS" BASIS, 
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. 
 * See the License for the specific language governing permissions and limitations 
 * under the License. 
 */
package com.mebigfatguy.twenty48playa;

import java.awt.Color;
import java.awt.image.IndexColorModel;
import java.io.BufferedInputStream;
import java.io.IOException;
import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public enum ColorTable {

	OUTSIDE,
	EDGE,
	BLANK,
	TWO,
	FOUR,
	EIGHT,
	SIXTEEN,
	THIRTYTWO,
	SIXTYFOUR,
	ONETWENTYEIGHT,
	TWOFIFTYSIX,
	FIVETWELVE,
	TENTWENTYFOUR,
	TWENTYFOURTYEIGHT,
	FOURTYNINETYSIX;
	
	private static Logger LOGGER = LoggerFactory.getLogger(ColorTable.class);
	
	private static IndexColorModel COLOR_MODEL;
	
	static {
		int numColors = ColorTable.values().length;
		
		byte[] r = new byte[numColors];
		byte[] g = new byte[numColors];
		byte[] b = new byte[numColors];

		try (BufferedInputStream bis = new BufferedInputStream(ColorTable.class.getResourceAsStream("/com/mebigfatguy/twenty48playa/colortable.properties"))) {
			
			Properties p = new Properties();
			p.load(bis);
			int index = 0;
			for (ColorTable ct : ColorTable.values()) {
				String clr = p.getProperty(ct.name());
				Color color = Color.decode(clr);
				r[index] = (byte) color.getRed();
				g[index] = (byte) color.getGreen();
				b[index] = (byte) color.getBlue();
				index++;
			}
			
			COLOR_MODEL = new IndexColorModel(8, numColors, r, g, b);
			
		} catch (IOException ioe) {
			LOGGER.error("Failed to load 2048 properties file");
			throw new RuntimeException("Failed to load 2048 properties file", ioe);
		}
	}
	
	public static IndexColorModel getColorModel() {
		return COLOR_MODEL;
	}
	
}
