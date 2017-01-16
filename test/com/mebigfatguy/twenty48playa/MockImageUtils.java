/** 2048 playa - a 2048 autonomous player. 
 * Copyright 2014-2017 MeBigFatGuy.com 
 * Copyright 2014-2017 Dave Brosius 
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

public class MockImageUtils extends ImageUtils {

	SquareType[][] board = new SquareType[4][4];
	
	public MockImageUtils() throws AWTException {
		super();
	}

	public void setBoardState(String boardState) {
		
		String[] numbers = boardState.trim().split("\\s+");
		if (numbers.length != 16) {
			throw new RuntimeException("Invalid number of board positions: " + numbers.length);
		}
		
		int i = 0;
		for (int y = 0; y < 4; y++) {
			for (int x = 0; x < 4; x++) {
				board[x][y] = Enum.valueOf(SquareType.class, numbers[i++]);
			}
		}
	}
	
	@Override
	public SquareType[][] getBoardState() {
		return board;
	}

	
}
