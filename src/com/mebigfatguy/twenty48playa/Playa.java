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
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Playa {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(Playa.class);
	private static final OptionComparator OPTION_COMPARATOR = new OptionComparator();
	
	
	private ImageUtils imageUtils;
	private WindowManager windowManager;
	private SquareType[][] oldBoard;
	
	public Playa(ImageUtils iu, WindowManager wm) {
		imageUtils = iu;
		windowManager = wm;
	}
	
	public void playGame() throws AWTException {
		
		oldBoard = new SquareType[4][4];
		boolean done = false;
		boolean seen2048 = false;
		
		openingGambit();
		do {
			SquareType[][] board = imageUtils.getBoardState();
			
			int recursion =  fillCount(board) - 13;
			recursion = Math.max(0, recursion);
			
			MoveOption bestOption = getBestDirection(new MoveOption(Direction.DOWN, 0, board),  recursion);
			collide(bestOption.getDirection());

			if (finished(board))
				return;
			
			if (!seen2048 && has2048(board)) {
				seen2048 = true;
				windowManager.clickContinue();
			}
			
		} while (!done);
	}

	private void openingGambit() {
		for (int i = 0; i < 10; i++) {
			for (int j = 0; j < 5; j++) {
				windowManager.key(KeyEvent.VK_UP);
			}
			
			windowManager.key(KeyEvent.VK_LEFT);
			windowManager.key(KeyEvent.VK_RIGHT);
		}
	}

	MoveOption getBestDirection(MoveOption origOption, int depth) {
		
		if (depth >= 5) {
			return origOption;
		}
		
		List<MoveOption> options = new ArrayList<MoveOption>();
		
		Pair<SquareType[][], Integer> upSim = simulateUp(origOption.getResultantBoard());
		if (!Arrays.deepEquals(origOption.getResultantBoard(), upSim.getKey())) {
			MoveOption upOption = getBestDirection(new MoveOption(Direction.UP, origOption.getScore() + upSim.getValue() / (depth + 1), upSim.getKey()), depth + 1);
			options.add(new MoveOption(Direction.UP, upOption.getScore(), upSim.getKey()));
		}
		
		if (fillCount(origOption.getResultantBoard()) > 14) {
			Pair<SquareType[][], Integer> downSim = simulateDown(origOption.getResultantBoard());
			if (!Arrays.deepEquals(origOption.getResultantBoard(), downSim.getKey())) {
				MoveOption downOption = getBestDirection(new MoveOption(Direction.DOWN, origOption.getScore() + downSim.getValue() / (depth + 1), downSim.getKey()), depth + 1);
				options.add(new MoveOption(Direction.DOWN, downOption.getScore(), downSim.getKey()));
			}
		}	
		
		Pair<SquareType[][], Integer> leftSim = simulateLeft(origOption.getResultantBoard());
		if (!Arrays.deepEquals(origOption.getResultantBoard(),  leftSim.getKey())) {
			MoveOption leftOption = getBestDirection(new MoveOption(Direction.LEFT, origOption.getScore() + leftSim.getValue() / (depth + 1), leftSim.getKey()), depth + 1);
			options.add(new MoveOption(Direction.LEFT, leftOption.getScore(), leftSim.getKey()));
		}
		
		Pair<SquareType[][], Integer> rightSim = simulateRight(origOption.getResultantBoard());
		if (!Arrays.deepEquals(origOption.getResultantBoard(), rightSim.getKey())) {
			MoveOption rightOption = getBestDirection(new MoveOption(Direction.RIGHT, origOption.getScore() + rightSim.getValue() / (depth + 1), rightSim.getKey()), depth + 1);
			options.add(new MoveOption(Direction.RIGHT, rightOption.getScore(), rightSim.getKey()));
		}
		
		if (options.isEmpty()) {
			return new MoveOption(Direction.DOWN, 0.0, origOption.getResultantBoard());
		}
		
		Collections.sort(options, OPTION_COMPARATOR);
		return options.get(0);
	}

	private boolean has2048(SquareType[][] board) {
		for (int y = 0; y < 4; y++) {
			for (int x = 0; x < 4; x++) {
				if (board[x][y] == SquareType.TWENTYFOURTYEIGHT) {
					return true;
				}
			}
		}
		
		return false;
	}
	
	private int fillCount(SquareType[][] board) {
		int count = 0;
		
		for (int y = 0; y < 4; y++) {
			for (int x = 0; x < 4; x++) {
				if (board[x][y] != SquareType.BLANK) {
					count++;
				}
			}
		}
		
		return count;
	}

	private boolean finished(SquareType[][] board) {

		boolean finished = Arrays.deepEquals(oldBoard, board);
		
		copyBoard(board, oldBoard);
		return finished;
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
		windowManager.delay(500);
	}
	
	Pair<SquareType[][], Integer> simulateUp(SquareType[][] board) {
		SquareType[][] simBoard = new SquareType[4][4];
		copyBoard(board, simBoard);
		int score = 0;
		
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
								score += Math.pow(2, (srcYType.ordinal()+1) - SquareType.TWO.ordinal() + 1);
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
		
		return new Pair<SquareType[][], Integer>(simBoard, Integer.valueOf(score));
	}
	
	Pair<SquareType[][], Integer> simulateDown(SquareType[][] board) {
		SquareType[][] simBoard = new SquareType[4][4];
		copyBoard(board, simBoard);
		int score = 0;
		
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
								score += Math.pow(2, (srcYType.ordinal()+1) - SquareType.TWO.ordinal() + 1);
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
		
		return new Pair<SquareType[][], Integer>(simBoard, Integer.valueOf(score));
	}

	Pair<SquareType[][], Integer> simulateLeft(SquareType[][] board) {
		SquareType[][] simBoard = new SquareType[4][4];
		copyBoard(board, simBoard);
		int score = 0;
		
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
								score += Math.pow(2, (srcXType.ordinal()+1) - SquareType.TWO.ordinal() + 1);
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
		
		return new Pair<SquareType[][], Integer>(simBoard, Integer.valueOf(score));
	}

	Pair<SquareType[][], Integer> simulateRight(SquareType[][] board) {
		SquareType[][] simBoard = new SquareType[4][4];
		copyBoard(board, simBoard);
		int score = 0;
		
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
								score += Math.pow(2, (srcXType.ordinal()+1) - SquareType.TWO.ordinal() + 1);
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
		
		return new Pair<SquareType[][], Integer>(simBoard, Integer.valueOf(score));
	}
	
	private void copyBoard(SquareType[][] srcBoard, SquareType[][] dstBoard) {
		for (int y = 0; y < 4; y++) {
			for (int x = 0; x < 4; x++) {
				dstBoard[x][y] = srcBoard[x][y];
			}
		}
	}
}
