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
import java.awt.Point;
import java.awt.event.KeyEvent;
import java.util.Arrays;
import java.util.Random;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Playa {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(Playa.class);
	
	
	private ImageUtils imageUtils;
	private WindowManager windowManager;
	private Random random;
	
	public Playa(ImageUtils iu, WindowManager wm) {
		imageUtils = iu;
		windowManager = wm;
		random = new Random(System.currentTimeMillis());
	}
	
	public void playGame() throws AWTException {
		
		boolean done = false;
		do {
			SquareType[][] board = imageUtils.getBoardState();
			
			Direction cd = getBestCollisionDirection(board);
			
			if (cd != null) {
				collide(cd);
			} else {
				if (finished(board))
					return;
				
				for (Direction d : Direction.values()) {
					collide(d);
					SquareType[][] after = imageUtils.getBoardState();
					if (!Arrays.deepEquals(board, after))
						break;
				}
			}
			
			
		} while (!done);
	}

	private boolean finished(SquareType[][] board) {
		for (int y = 0; y < 4; y++) {
			for (int x = 0; x < 4; x++) {
				if (board[x][y] == SquareType.BLANK)
					return false;
			}
		}
		return true;
	}

	public Direction getBestCollisionDirection(SquareType[][] board) {
		SquareType bestCollision = SquareType.BLANK;
		Direction bestDirection = Direction.NONE;
		
		for (int y = 0; y < 4; y++) {
			for (int x = 0; x < 4; x++) {
				SquareType st = board[x][y];
				if (st != SquareType.BLANK) {
					for (Direction d : Direction.values()) {
						if (d == Direction.RIGHT) {
							break;
						}
						SquareType neighbor = getNextSquareType(board, x, y, d);
						if (st == neighbor) {
							if ((st.ordinal() > bestCollision.ordinal()) || ((st.ordinal() == bestCollision.ordinal()) && (d.ordinal() < bestDirection.ordinal()))) {
								bestCollision = st;
								bestDirection = d;
							}
						}
					}
				}
			}
		}
		
		if (bestCollision == SquareType.BLANK)
			return null;
		
		if (bestDirection == Direction.LEFT) {
			bestDirection = random.nextBoolean() ? Direction.LEFT : Direction.RIGHT;
		}
		
		return bestDirection;
	}
	
	private SquareType getNextSquareType(SquareType[][] board, int x, int y, Direction d) {
		Point move = d.getMovement();
		
		x+= move.x;
		y+= move.y;
		
		while ((x >= 0) && (x <= 3) && (y >= 0) && (y <= 3)) {
			SquareType st = board[x][y];
			if (st != SquareType.BLANK)
				return st;
			x+= move.x;
			y+= move.y;
		}
		
		return SquareType.BLANK;
	}

	private void collide(Direction d) {
		switch (d) {
			case UP:
				windowManager.key(KeyEvent.VK_UP);
			break;
			
			case LEFT:
				windowManager.key(KeyEvent.VK_LEFT);
			break;
				
			case RIGHT:
				windowManager.key(KeyEvent.VK_RIGHT);
			break;
		}	
	}
}
