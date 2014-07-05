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
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Random;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Playa {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(Playa.class);
	private static final OptionComparator OPTION_COMPARATOR = new OptionComparator();
	
	
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
				
				List<Pair<Integer, Direction>> options = new ArrayList<>();

				SquareType[][] upSim = simulateUp(board);
				if (!Arrays.deepEquals(upSim,  board)) {
					options.add(new Pair(verticalScore(upSim), Direction.UP));
				}
				
				SquareType[][] leftSim = simulateLeft(board);
				if (!Arrays.deepEquals(leftSim,  board)) {
					options.add(new Pair(verticalScore(leftSim), Direction.LEFT));
				}
				
				SquareType[][] rightSim = simulateRight(board);
				if (!Arrays.deepEquals(rightSim,  board)) {
					options.add(new Pair(verticalScore(rightSim), Direction.RIGHT));
				}
				
				if (options.size() == 0) {
					collide(Direction.DOWN);
				} else {
					Collections.sort(options, OPTION_COMPARATOR);
					collide(options.get(0).getValue());
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
			SquareType[][] leftSim = simulateLeft(board);
			SquareType[][] rightSim = simulateRight(board);
			
			int leftScore = verticalScore(leftSim);
			int rightScore = verticalScore(rightSim);
			
			if (leftScore > rightScore) {
				return Direction.LEFT;
			} else if (leftScore < rightScore) {
				return Direction.RIGHT;
			}
			
			bestDirection = random.nextBoolean() ? Direction.LEFT : Direction.RIGHT;
		}
		
		return bestDirection;
	}
	
	private int verticalScore(SquareType[][] board) {
		int score = 0;
		
		for (int y = 0; y < 3; y++) {
			for (int x = 0; x < 4; x++) {
				if ((board[x][y] == board[x][y+1]) && (board[x][y] != SquareType.BLANK)) {
					score += Math.pow(2, board[x][y].ordinal() - SquareType.TWO.ordinal() + 1);
				}
			}
		}
		
		return score;
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
	
	private SquareType[][] simulateUp(SquareType[][] board) {
		SquareType[][] simBoard = new SquareType[4][4];
		copyBoard(board, simBoard);
		
		for (int x = 0; x < 4; x++) {
			if (!fullColumn(simBoard, x)) {
				for (int y = 0; y < 3; y++) {
					SquareType yType = simBoard[x][y];
					for (int srcY = y + 1; srcY < 4; srcY++) {
						SquareType srcYType = simBoard[x][srcY];
						if (yType == SquareType.BLANK) {
							if (srcYType != SquareType.BLANK) {
								simBoard[x][y] = srcYType;
								srcY++;
								for (int copyY = y + 1; copyY < 4; copyY++) {
									if (srcY < 4) {
										simBoard[x][copyY] = simBoard[x][srcY++];
									} else 
										simBoard[x][copyY] = SquareType.BLANK;
								}
							}
						} else {
							if (srcYType != SquareType.BLANK) {
								if (srcYType == yType) {
									simBoard[x][y] = SquareType.values()[srcYType.ordinal() + 1];
									srcY++;
									for (int copyY = y + 1; copyY < 4; copyY++) {
										if (srcY < 4) {
											simBoard[x][copyY] = simBoard[x][srcY++];
										} else 
											simBoard[x][copyY] = SquareType.BLANK;
									}
								}
								break;
							}
						}
					}
				}
			}
		}
		
		return simBoard;
	}

	private SquareType[][] simulateLeft(SquareType[][] board) {
		SquareType[][] simBoard = new SquareType[4][4];
		copyBoard(board, simBoard);
		
		for (int y = 0; y < 4; y++) {
			if (!fullRow(simBoard, y)) {
				for (int x = 0; x < 3; x++) {
					SquareType xType = simBoard[x][y];
					for (int srcX = x + 1; srcX < 4; srcX++) {
						SquareType srcXType = simBoard[srcX][y];
						if (xType == SquareType.BLANK) {
							if (srcXType != SquareType.BLANK) {
								simBoard[x][y] = srcXType;
								srcX++;
								for (int copyX = x + 1; copyX < 4; copyX++) {
									if (srcX < 4) {
										simBoard[copyX][y] = simBoard[srcX++][y];
									} else 
										simBoard[copyX][y] = SquareType.BLANK;
								}
							}
						} else {
							if (srcXType != SquareType.BLANK) {
								if (srcXType == xType) {
									simBoard[x][y] = SquareType.values()[srcXType.ordinal() + 1];
									srcX++;
									for (int copyX = x + 1; copyX < 4; copyX++) {
										if (srcX < 4) {
											simBoard[copyX][y] = simBoard[srcX++][y];
										} else 
											simBoard[copyX][y] = SquareType.BLANK;
									}
								}
								break;
							}
						}
					}
				}
			}
		}
		
		return simBoard;
	}

	private SquareType[][] simulateRight(SquareType[][] board) {
		SquareType[][] simBoard = new SquareType[4][4];
		copyBoard(board, simBoard);
		
		for (int y = 0; y < 4; y++) {
			if (!fullRow(simBoard, y)) {
				for (int x = 3; x > 0; x--) {
					SquareType xType = simBoard[x][y];
					for (int srcX = x - 1; srcX >= 0; srcX--) {
						SquareType srcXType = simBoard[srcX][y];
						if (xType == SquareType.BLANK) {
							if (srcXType != SquareType.BLANK) {
								simBoard[x][y] = srcXType;
								srcX--;
								for (int copyX = x - 1; copyX >= 0; copyX--) {
									if (srcX >= 0) {
										simBoard[copyX][y] = simBoard[srcX--][y];
									} else 
										simBoard[copyX][y] = SquareType.BLANK;
								}
							}
						} else {
							if (srcXType != SquareType.BLANK) {
								if (srcXType == xType) {
									simBoard[x][y] = SquareType.values()[srcXType.ordinal() + 1];
									srcX--;
									for (int copyX = x - 1; copyX >= 0; copyX--) {
										if (srcX >= 0) {
											simBoard[copyX][y] = simBoard[srcX--][y];
										} else 
											simBoard[copyX][y] = SquareType.BLANK;
									}
								}
								break;
							}
						}
					}
				}
			}
		}
		
		return simBoard;
	}
	
	private boolean fullRow(SquareType[][] board, int y) {
		for (int x = 0; x < 4; x++) {
			if (board[x][y] == SquareType.BLANK) {
				return false;
			}
		}
		
		return true;
	}
	
	private boolean fullColumn(SquareType[][] board, int x) {
		for (int y = 0; y < 4; y++) {
			if (board[x][y] == SquareType.BLANK) {
				return false;
			}
		}
		
		return true;
	}
	
	private void copyBoard(SquareType[][] srcBoard, SquareType[][] dstBoard) {
		for (int y = 0; y < 4; y++) {
			for (int x = 0; x < 4; x++) {
				dstBoard[x][y] = srcBoard[x][y];
			}
		}
	}
}
