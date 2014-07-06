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
	private SquareType[][] oldBoard;
	
	public Playa(ImageUtils iu, WindowManager wm) {
		imageUtils = iu;
		windowManager = wm;
		random = new Random(System.currentTimeMillis());
	}
	
	public void playGame() throws AWTException {
		
		oldBoard = new SquareType[4][4];
		boolean done = false;
		do {
			SquareType[][] board = imageUtils.getBoardState();
			
			Pair<Direction, Integer> bestCollisionOption = getBestCollisionDirection(board);
			Pair<Direction, Integer> bestNonCollisionOption = getBestNonCollisionDirection(board);
			
			if (2 * bestCollisionOption.getValue().intValue() >= bestNonCollisionOption.getValue().intValue()) {
				collide(bestCollisionOption.getKey());
			} else {
				collide(bestNonCollisionOption.getKey());
			}
			if (finished(board))
				return;
			
		} while (!done);
	}
	
	Pair<Direction, Integer> getBestNonCollisionDirection(SquareType[][] board) {
		List<Pair<Direction, Integer>> options = new ArrayList<>();

		SquareType[][] upSim = simulateUp(board);
		if (!Arrays.deepEquals(upSim,  board)) {
			options.add(new Pair<Direction, Integer>(Direction.UP, verticalScore(upSim)));
		}
		
		SquareType[][] leftSim = simulateLeft(board);
		if (!Arrays.deepEquals(leftSim,  board)) {
			options.add(new Pair<Direction, Integer>(Direction.LEFT, verticalScore(leftSim)));
		}
		
		SquareType[][] rightSim = simulateRight(board);
		if (!Arrays.deepEquals(rightSim,  board)) {
			options.add(new Pair<Direction, Integer>(Direction.RIGHT, verticalScore(rightSim)));
		}
		
		if (fillCount(board) > 12) {
			SquareType[][] downSim = simulateDown(board);
			if (!Arrays.deepEquals(downSim,  board)) {
				options.add(new Pair<Direction, Integer>(Direction.DOWN, verticalScore(downSim)));
			}
		}
		
		if (options.size() == 0) {
			return new Pair<Direction, Integer>(Direction.DOWN, Integer.valueOf(0));
		}
		
		Collections.sort(options, OPTION_COMPARATOR);
		return options.get(0);
	}

	private int fillCount(SquareType[][] board) {
		int count = 0;
		
		for (int y = 0; y < 4; y++) {
			for (int x = 0; x < 4; x++) {
				if (board[x][y] == SquareType.BLANK) {
					count++;
				}
			}
		}
		
		return count;
	}

	public Pair<Direction, Integer> getBestCollisionDirection(SquareType[][] board) {
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
			return new Pair<Direction, Integer>(Direction.DOWN, Integer.valueOf(-1));
		
		int collisionScore = (int) Math.pow(2, bestCollision.ordinal() - SquareType.TWO.ordinal() + 1);
		
		if (bestDirection == Direction.UP) 
			return new Pair<Direction, Integer>(Direction.UP, Integer.valueOf(verticalScore(simulateUp(board)) + collisionScore));
		
		SquareType[][] leftSim = simulateLeft(board);
		SquareType[][] rightSim = simulateRight(board);
		
		int leftScore = verticalScore(leftSim);
		int rightScore = verticalScore(rightSim);
		
		if (leftScore > rightScore) {
			return new Pair<Direction, Integer>(Direction.LEFT, Integer.valueOf(leftScore + collisionScore));
		} else if (leftScore < rightScore) {
			return new Pair<Direction, Integer>(Direction.RIGHT, Integer.valueOf(rightScore + collisionScore));
		}
		
		return new Pair<Direction, Integer>(random.nextBoolean() ? Direction.LEFT : Direction.RIGHT, Integer.valueOf(leftScore + collisionScore));
	}
	
	private boolean finished(SquareType[][] board) {

		boolean finished = Arrays.deepEquals(oldBoard, board);
		
		copyBoard(board, oldBoard);
		return finished;
	}
	
	int verticalScore(SquareType[][] board) {
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
			
			case DOWN:
				windowManager.key(KeyEvent.VK_DOWN);
			break;
		}	
	}
	
	SquareType[][] simulateUp(SquareType[][] board) {
		SquareType[][] simBoard = new SquareType[4][4];
		copyBoard(board, simBoard);
		
		for (int x = 0; x < 4; x++) {
			for (int y = 0; y < 3; y++) {
				SquareType yType = simBoard[x][y];
				for (int srcY = y + 1; srcY < 4; srcY++) {
					SquareType srcYType = simBoard[x][srcY];
					if (yType == SquareType.BLANK) {
						if (srcYType != SquareType.BLANK) {
							simBoard[x][y] = srcYType;
							yType = srcYType;
							int tY = srcY + 1;
							for (int copyY = y + 1; copyY < 4; copyY++) {
								if (tY < 4) {
									simBoard[x][copyY] = simBoard[x][tY++];
								} else 
									simBoard[x][copyY] = SquareType.BLANK;
							}
							srcY = y;
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
		
		return simBoard;
	}
	
	SquareType[][] simulateDown(SquareType[][] board) {
		SquareType[][] simBoard = new SquareType[4][4];
		copyBoard(board, simBoard);
		
		for (int x = 0; x < 4; x++) {
			for (int y = 3; y > 0; y--) {
				SquareType yType = simBoard[x][y];
				for (int srcY = y - 1; srcY >= 0; srcY--) {
					SquareType srcYType = simBoard[x][srcY];
					if (yType == SquareType.BLANK) {
						if (srcYType != SquareType.BLANK) {
							simBoard[x][y] = srcYType;
							yType = srcYType;
							int tY = srcY - 1;
							for (int copyY = y - 1; copyY >= 0; copyY--) {
								if (tY >= 0) {
									simBoard[x][copyY] = simBoard[x][tY--];
								} else 
									simBoard[x][copyY] = SquareType.BLANK;
							}
							srcY = y;
						}
					} else {
						if (srcYType != SquareType.BLANK) {
							if (srcYType == yType) {
								simBoard[x][y] = SquareType.values()[srcYType.ordinal() + 1];
								srcY--;
								for (int copyY = y - 1; copyY >= 0; copyY--) {
									if (srcY >= 0) {
										simBoard[x][copyY] = simBoard[x][srcY--];
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
		
		return simBoard;
	}

	SquareType[][] simulateLeft(SquareType[][] board) {
		SquareType[][] simBoard = new SquareType[4][4];
		copyBoard(board, simBoard);
		
		for (int y = 0; y < 4; y++) {
			for (int x = 0; x < 3; x++) {
				SquareType xType = simBoard[x][y];
				for (int srcX = x + 1; srcX < 4; srcX++) {
					SquareType srcXType = simBoard[srcX][y];
					if (xType == SquareType.BLANK) {
						if (srcXType != SquareType.BLANK) {
							simBoard[x][y] = srcXType;
							xType = srcXType;
							int tX = srcX + 1;
							for (int copyX = x + 1; copyX < 4; copyX++) {
								if (tX < 4) {
									simBoard[copyX][y] = simBoard[tX++][y];
								} else 
									simBoard[copyX][y] = SquareType.BLANK;
							}
							srcX = x;
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
		
		return simBoard;
	}

	SquareType[][] simulateRight(SquareType[][] board) {
		SquareType[][] simBoard = new SquareType[4][4];
		copyBoard(board, simBoard);
		
		for (int y = 0; y < 4; y++) {
			for (int x = 3; x > 0; x--) {
				SquareType xType = simBoard[x][y];
				for (int srcX = x - 1; srcX >= 0; srcX--) {
					SquareType srcXType = simBoard[srcX][y];
					if (xType == SquareType.BLANK) {
						if (srcXType != SquareType.BLANK) {
							simBoard[x][y] = srcXType;
							xType = srcXType;
							int tx = srcX - 1;
							for (int copyX = x - 1; copyX >= 0; copyX--) {
								if (tx >= 0) {
									simBoard[copyX][y] = simBoard[tx--][y];
								} else 
									simBoard[copyX][y] = SquareType.BLANK;
							}
							srcX = x;
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
		
		return simBoard;
	}
	
	private void copyBoard(SquareType[][] srcBoard, SquareType[][] dstBoard) {
		for (int y = 0; y < 4; y++) {
			for (int x = 0; x < 4; x++) {
				dstBoard[x][y] = srcBoard[x][y];
			}
		}
	}
}
