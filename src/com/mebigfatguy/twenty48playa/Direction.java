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
