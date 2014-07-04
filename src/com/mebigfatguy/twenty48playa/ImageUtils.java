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

import java.awt.AWTException;
import java.awt.Graphics;
import java.awt.GraphicsConfiguration;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.Robot;
import java.awt.image.BufferedImage;
import java.awt.image.Raster;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ImageUtils {

	private static final Logger LOGGER = LoggerFactory.getLogger(ImageUtils.class);
	
	private Robot robot;
	private File debugDir;
	private Rectangle boardDimensions;
	
	public ImageUtils() throws AWTException {
		robot = new Robot();
		debugDir = new File(System.getProperty("user.home"), ".2048playa");
		debugDir.mkdirs();
	}
	
	public void initialize() throws AWTException {
		boardDimensions = findBoardCoordinates();
	}
	
	public int[][] getBoardState() throws AWTException {
		int[][] state = new int[4][4];
		BufferedImage image = getScreenBuffer(boardDimensions);
		int width = image.getWidth() / 4;
		int height = image.getHeight() / 4;
		for (int y = 0; y < 4; y++) {
			for (int x = 0; x < 4; x++) {
				Rectangle squareDim = new Rectangle(x * width, y * height, width, height);
				BufferedImage square = getImageBuffer(image, squareDim);
				writeImage(square, "(" + x + "," + y + ")");
			}
		}
		
		return state;
	}
	
	public BufferedImage getScreenBuffer() throws AWTException {
		GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
		GraphicsDevice gd = ge.getDefaultScreenDevice();
		GraphicsConfiguration gc = gd.getDefaultConfiguration();
		return getScreenBuffer(gc.getBounds());
	}
	
	public BufferedImage getScreenBuffer(Rectangle bounds) throws AWTException {
		return to8Bit(robot.createScreenCapture(bounds));
	}
	
	public BufferedImage getImageBuffer(BufferedImage srcImage, Rectangle bounds) throws AWTException {
		BufferedImage dstImage = new BufferedImage(bounds.width, bounds.height, BufferedImage.TYPE_BYTE_INDEXED, ColorTable.getColorModel());
		Graphics graphix = dstImage.getGraphics();
		try {
			graphix.drawImage(srcImage, -bounds.x, -bounds.y, null);
		} finally {
			graphix.dispose();
		}
		
		return dstImage;
	}
	
	public BufferedImage to8Bit(BufferedImage srcImage) {
		
		BufferedImage dstImage = new BufferedImage(srcImage.getWidth(), srcImage.getHeight(), BufferedImage.TYPE_BYTE_INDEXED, ColorTable.getColorModel());
		Graphics graphix = dstImage.getGraphics();
		try {
			graphix.drawImage(srcImage, 0, 0, null);
		} finally {
			graphix.dispose();
		}
		
		return dstImage;
	}
	
	public Point findFirstDifference(BufferedImage image1, BufferedImage image2, int diffLen) {
		Raster r1 = image1.getRaster();
		Raster r2 = image2.getRaster();
		
		int height = image1.getHeight();
		int width = image1.getWidth();
		int[] data1 = new int[width];
		int[] data2 = new int[width];
		
		for (int y = 0; y < height; y++) {
			r1.getPixels(0, y, width, 1, data1);
			r2.getPixels(0, y, width, 1, data2);
			
			int runDiff = 0;;
			for (int x = 0; x < width; x++) {
				if (data1[x] == data2[x]) {
					runDiff = 0;
				} else {
					runDiff++;
					if (runDiff >= diffLen)
						return new Point(x, y);
				}
			}
		}
		
		return null;
	}
	
	public Rectangle findBoardCoordinates() throws AWTException {
		BufferedImage image = getScreenBuffer();
		writeImage(image, "screen.png");
		
		Point center = new Point(image.getWidth() / 2, image.getHeight() / 2);
		int left = findColor(image, 30, center.y, 1, 0, ColorTable.EDGE.ordinal()).x;
		int right = findColor(image, image.getWidth() - 30, center.y, -1, 0, ColorTable.EDGE.ordinal()).x;
		int top = findColor(image, left + 4, center.y, 0, -1, ColorTable.OUTSIDE.ordinal()).y + 1;
		int bottom = findColor(image, left + 4, center.y, 0, 1, ColorTable.OUTSIDE.ordinal()).y - 1;
		
		Rectangle bounds = new Rectangle(left, top, right - left, bottom - top);
		writeImage(getImageBuffer(image, bounds), "board.png");
		return bounds;
	}
	

	private Point findColor(BufferedImage image, int startX, int startY, int dirX, int dirY, int colorIndex) {
		
		int curX = startX;
		int curY = startY;
		int[] pixel = new int[1];
		
		Raster r = image.getRaster();
		do {
			r.getPixel(curX, curY, pixel);
			if (pixel[0] == colorIndex) {
				return new Point(curX, curY);
			}
			curX += dirX;
			curY += dirY;
		} while (true);
	}

	public void writeImage(BufferedImage image, String fileName) {
		try {
			ImageIO.write(image,  "png",  new File(debugDir, fileName));
		} catch (IOException ioe) {
			LOGGER.error("Failed writing image to file {}", fileName, ioe);
		}
	}


}
