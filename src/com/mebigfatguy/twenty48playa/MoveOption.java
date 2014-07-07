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

import java.util.Arrays;

public class MoveOption {
	Direction direction;
	private double score;
	SquareType[][] resultantBoard;
	
	
	public MoveOption(Direction dir, double dirScore, SquareType[][] board) {
		direction = dir;
		score = dirScore;
		resultantBoard = board;
	}

	public Direction getDirection() {
		return direction;
	}


	public double getScore() {
		return score;
	}


	public SquareType[][] getResultantBoard() {
		return resultantBoard;
	}
	
	@Override
	public int hashCode() {
		return direction.hashCode() ^ ((int) score) ^ Arrays.deepHashCode(resultantBoard);
	}
	
	@Override
	public boolean equals(Object o) {
		if (!(o instanceof MoveOption))
			return false;
		
		MoveOption that = (MoveOption) o;
		
		return direction == that.direction && score == that.score && Arrays.deepEquals(resultantBoard, that.resultantBoard);
	}
	
	@Override
	public String toString() {
		return "MoveOption[" + direction + ", " + score + ", " + Arrays.deepToString(resultantBoard) + "]";
	}
	
}
