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
	private Rectangle boardCoordinates;

	private Rectangle[][] squareCoordinates;
	
	public ImageUtils() throws AWTException {
		robot = new Robot();
		debugDir = new File(System.getProperty("user.home"), ".2048playa");
		debugDir.mkdirs();
	}
	
	public void initialize() {
		findBoardCoordinates();
	}
	
	public SquareType[][] getBoardState() {
		SquareType[][] state = new SquareType[4][4];
		BufferedImage image = getScreenBuffer(boardCoordinates);
		for (int y = 0; y < 4; y++) {
			for (int x = 0; x < 4; x++) {	
				BufferedImage square = getImageBuffer(image, squareCoordinates[x][y]);
				state[x][y] = getMajorColor(square);
			}
		}
		
		return state;
	}
	
	public Rectangle getBoardCoordinates() {
		return boardCoordinates;
	}
	
	public Rectangle getScreenBounds() {
		GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
		GraphicsDevice gd = ge.getDefaultScreenDevice();
		GraphicsConfiguration gc = gd.getDefaultConfiguration();
		return gc.getBounds();
	}
	
	public BufferedImage getScreenBuffer() {
		return getScreenBuffer(getScreenBounds());
	}
	
	public BufferedImage getScreenBuffer(Rectangle bounds) {
		return to8Bit(robot.createScreenCapture(bounds));
	}
	
	public BufferedImage getImageBuffer(BufferedImage srcImage, Rectangle bounds) {
		BufferedImage dstImage = new BufferedImage(bounds.width, bounds.height, BufferedImage.TYPE_BYTE_INDEXED, SquareType.getColorModel());
		Graphics graphix = dstImage.getGraphics();
		try {
			graphix.drawImage(srcImage, -bounds.x, -bounds.y, null);
		} finally {
			graphix.dispose();
		}
		
		return dstImage;
	}
	
	public BufferedImage to8Bit(BufferedImage srcImage) {
		
		BufferedImage dstImage = new BufferedImage(srcImage.getWidth(), srcImage.getHeight(), BufferedImage.TYPE_BYTE_INDEXED, SquareType.getColorModel());
		Graphics graphix = dstImage.getGraphics();
		try {
			graphix.drawImage(srcImage, 0, 0, null);
		} finally {
			graphix.dispose();
		}
		
		return dstImage;
	}
	
	public Rectangle findBlackInset(BufferedImage image) {
		Raster r = image.getRaster();
		
		Point topLeft = null;
		Point bottomRight = null;
		
		int width = image.getWidth();
		int height = image.getHeight();
		
		int[] data = new int[width];
		
		y: for (int y = 0; y < height; y++) {
			r.getPixels(0, y, width, 1, data);
			
			for (int x = 0; x < width; x++) {
				if (data[x] != SquareType.BLACK.ordinal()) {
					topLeft = new Point(x, y);
					break y;
				}
			}
		}
		
		y: for (int y = height - 1; y >= 0; y--) {
			r.getPixels(0, y, width, 1, data);
			
			for (int x = width - 1; x >= 0; x--) {
				if (data[x] != SquareType.BLACK.ordinal()) {
					bottomRight = new Point(x, y);
					break y;
				}
			}
		}
		
		if ((topLeft == null) || (bottomRight == null)) 
			return null;
		
		return new Rectangle(topLeft.x, topLeft.y, bottomRight.x - topLeft.x + 1, bottomRight.y - topLeft.y + 1);
	}
	
	private void findBoardCoordinates() {
		BufferedImage image = getScreenBuffer();
		
		Point center = new Point(image.getWidth() / 2, image.getHeight() / 2);
		int left = findColor(image, 30, center.y, 1, 0, SquareType.EDGE.ordinal()).x;
		int right = findColor(image, image.getWidth() - 30, center.y, -1, 0, SquareType.EDGE.ordinal()).x;
		int top = findColor(image, left + 4, center.y, 0, -1, SquareType.OUTSIDE.ordinal()).y + 1;
		int bottom = findColor(image, left + 4, center.y, 0, 1, SquareType.OUTSIDE.ordinal()).y - 1;
		
		boardCoordinates = new Rectangle(left, top, right - left, bottom - top);
		image = getImageBuffer(image, boardCoordinates);
		
		squareCoordinates = new Rectangle[4][4];
		
		for (int y = 0; y < 4; y++) {
			for (int x = 0; x < 4; x++) {
				squareCoordinates[x][y] = findSquareCoordinates(image, x, y);
			}
		}
	}
	

	private static Rectangle findSquareCoordinates(BufferedImage image, int x, int y) {
		
		int startX = ((x * 4 + 1) * image.getWidth()) / 16;
		int startY = ((y * 4 + 1) * image.getHeight()) / 16;
		int left = findColor(image, startX, startY, -1, 0, SquareType.EDGE.ordinal()).x + 1;
		int top = findColor(image, startX, startY, 0, -1, SquareType.EDGE.ordinal()).y + 1;
		
		startX = ((x * 4 + 3) * image.getWidth()) / 16;
		startY = ((y * 4 + 3) * image.getHeight()) / 16;
		int right = findColor(image, startX, startY, 1, 0, SquareType.EDGE.ordinal()).x - 1;
		int bottom = findColor(image, startX, startY, 0, 1, SquareType.EDGE.ordinal()).y - 1;
		
		return new Rectangle(left, top, right - left, bottom - top);
	}

	private static Point findColor(BufferedImage image, int startX, int startY, int dirX, int dirY, int colorIndex) {
		
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
	
	private static SquareType getMajorColor(BufferedImage image) {
		SquareType[] values = SquareType.values();
		int[] counts = new int[values.length];
		
		Raster r = image.getRaster();
		int width = image.getWidth();
		int height = image.getHeight();
		int[] pixels = new int[width];
		
		for (int y = 0; y < height; y++) {
			r.getPixels(0,  y,  width,  1,  pixels);
			for (int i = 0; i < pixels.length; i++) {
				counts[pixels[i]]++;
			}
		}
		
		int max = 0;
		int maxIndex = 0;
		
		for (int i = 0; i < counts.length; i++) {
			if (counts[i] > max) {
				max = counts[i];
				maxIndex = i;
			}
		}

		return values[maxIndex];
	}

	public void writeImage(BufferedImage image, String fileName) {
		try {
			ImageIO.write(image,  "png",  new File(debugDir, fileName));
		} catch (IOException ioe) {
			LOGGER.error("Failed writing image to file {}", fileName + ".png", ioe);
		}
	}
}
