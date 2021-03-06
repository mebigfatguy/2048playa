/** 2048 playa - a 2048 autonomous player. 
 * Copyright 2014-2019 MeBigFatGuy.com 
 * Copyright 2014-2019 Dave Brosius 
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

import javax.swing.JOptionPane;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Twenty48Playa {

	private static final Logger LOGGER = LoggerFactory.getLogger(Twenty48Playa.class);

	public static void main(String[] args) {
		try {
			ImageUtils iu = new ImageUtils();
			WindowManager wm = new WindowManager(iu);
			
			boolean firstTime = true;
			int choice = JOptionPane.CANCEL_OPTION;
			do {
				wm.launch2048(firstTime);
				firstTime = false;
				Playa playa = new Playa(iu, wm);
				
				playa.playGame();
				
				//choice = JOptionPane.showConfirmDialog(null, "Try again?");
				choice = JOptionPane.OK_OPTION;
			} while (choice == JOptionPane.OK_OPTION);
				
		} catch (Exception e) {
			LOGGER.error("Failed launching browser to 2048", e);
		}
	}
}
