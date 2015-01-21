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

import java.awt.Point;

enum Direction {
	UP(0, -1), 
	LEFT(-1, 0), 
	RIGHT(1, 0), 
	DOWN(0, 1), 
	NONE(0, 0);
	
	private Point move;
	
	private Direction(int x, int y) {
		move = new Point(x, y);
	}
	
	public Point getMovement() {
		return move;
	}
};
