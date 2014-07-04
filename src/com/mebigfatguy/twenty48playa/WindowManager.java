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
import java.awt.Desktop;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.Robot;
import java.awt.event.InputEvent;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class WindowManager {

	private static final Logger LOGGER = LoggerFactory.getLogger(WindowManager.class);
	
	private ImageUtils imageUtils;
	private Robot robot;
	
	public WindowManager(ImageUtils iu) throws AWTException{
		imageUtils = iu;
		robot = new Robot();
	}
	
	public void launch2048() throws URISyntaxException, IOException, AWTException {
		BufferedImage beforeImage = imageUtils.getScreenBuffer();
		
		Desktop dt = Desktop.getDesktop();
		dt.browse(new URI("http://gabrielecirulli.github.io/2048/"));
		
		robot.delay(2000);
		BufferedImage afterImage = imageUtils.getScreenBuffer();
		
		Point difference = imageUtils.findFirstDifference(beforeImage, afterImage, 50);
		
		if (difference == null) {
			throw new AWTException("Unabled to find title bar of browser");
		}
		doubleClick(new Point(difference.x - 5,  difference.y + 5));
		robot.delay(1000);
		imageUtils.initialize();
	}
	
	public void click(Point pt) {
		robot.mouseMove(pt.x, pt.y);
		robot.mousePress(InputEvent.BUTTON1_DOWN_MASK);
		robot.delay(10);
		robot.mouseRelease(InputEvent.BUTTON1_DOWN_MASK);
	}
	
	public void doubleClick(Point pt) {
		click(pt);
		click(pt);
	}
}
