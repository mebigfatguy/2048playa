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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Playa {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(Playa.class);
	
	private ImageUtils imageUtils;
	
	public Playa(ImageUtils iu) {
		imageUtils = iu;
	}
	
	public void playGame() throws AWTException {
		
		boolean done = false;
		do {
			SquareType[][] board = imageUtils.getBoardState();
		} while (!done);
		
	}
}
