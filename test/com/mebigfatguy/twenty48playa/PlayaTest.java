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

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class PlayaTest {
	
	private Playa playa;
	private MockImageUtils iu;
	
	@Before
	public void setUp() throws AWTException {
		
		iu = new MockImageUtils();
		WindowManager wm = new WindowManager(iu);
		playa = new Playa(iu, wm);
	}
	
	@Test
	public void testSimulateUp1() {
		
		iu.setBoardState(
			" BLANK TWO TWO TWO " +
			" TWO TWO TWO BLANK " +
			" TWO FOUR TWO FOUR " +
			" FOUR TWO TWO FOUR "
		);
		
		SquareType[][] origBoard = iu.getBoardState();
		Pair<SquareType[][], Integer> newSim = playa.simulateUp(origBoard);
		SquareType[][] newBoard = newSim.getKey();
		
		Assert.assertEquals(SquareType.FOUR, newBoard[0][0]);
		Assert.assertEquals(SquareType.FOUR, newBoard[1][0]);
		Assert.assertEquals(SquareType.FOUR, newBoard[2][0]);
		Assert.assertEquals(SquareType.TWO, newBoard[3][0]);
		
		Assert.assertEquals(SquareType.FOUR, newBoard[0][1]);
		Assert.assertEquals(SquareType.FOUR, newBoard[1][1]);
		Assert.assertEquals(SquareType.FOUR, newBoard[2][1]);
		Assert.assertEquals(SquareType.EIGHT, newBoard[3][1]);
		
		Assert.assertEquals(SquareType.BLANK, newBoard[0][2]);
		Assert.assertEquals(SquareType.TWO, newBoard[1][2]);
		Assert.assertEquals(SquareType.BLANK, newBoard[2][2]);
		Assert.assertEquals(SquareType.BLANK, newBoard[3][2]);
		
		Assert.assertEquals(SquareType.BLANK, newBoard[0][3]);
		Assert.assertEquals(SquareType.BLANK, newBoard[1][3]);
		Assert.assertEquals(SquareType.BLANK, newBoard[2][3]);
		Assert.assertEquals(SquareType.BLANK, newBoard[3][3]);
	}

	@Test
	public void testSimulateUp2() {
		
		iu.setBoardState(
			" THIRTYTWO BLANK FOUR EIGHT " +
			" EIGHT BLANK BLANK BLANK " +
			" FOUR SIXTEEN FOUR EIGHT " +
			" FOUR SIXTEEN SIXTEEN EIGHT "
		);
		
		SquareType[][] origBoard = iu.getBoardState();
		Pair<SquareType[][], Integer> newSim = playa.simulateUp(origBoard);
		SquareType[][] newBoard = newSim.getKey();
		
		Assert.assertEquals(SquareType.THIRTYTWO, newBoard[0][0]);
		Assert.assertEquals(SquareType.THIRTYTWO, newBoard[1][0]);
		Assert.assertEquals(SquareType.EIGHT, newBoard[2][0]);
		Assert.assertEquals(SquareType.SIXTEEN, newBoard[3][0]);
		
		Assert.assertEquals(SquareType.EIGHT, newBoard[0][1]);
		Assert.assertEquals(SquareType.BLANK, newBoard[1][1]);
		Assert.assertEquals(SquareType.SIXTEEN, newBoard[2][1]);
		Assert.assertEquals(SquareType.EIGHT, newBoard[3][1]);
		
		Assert.assertEquals(SquareType.EIGHT, newBoard[0][2]);
		Assert.assertEquals(SquareType.BLANK, newBoard[1][2]);
		Assert.assertEquals(SquareType.BLANK, newBoard[2][2]);
		Assert.assertEquals(SquareType.BLANK, newBoard[3][2]);
		
		Assert.assertEquals(SquareType.BLANK, newBoard[0][3]);
		Assert.assertEquals(SquareType.BLANK, newBoard[1][3]);
		Assert.assertEquals(SquareType.BLANK, newBoard[2][3]);
		Assert.assertEquals(SquareType.BLANK, newBoard[3][3]);
	}
	
	@Test
	public void testSimulateDown1() {
		
		iu.setBoardState(
			" BLANK TWO TWO TWO " +
			" TWO TWO TWO BLANK " +
			" TWO FOUR TWO FOUR " +
			" FOUR TWO TWO FOUR "
		);
		
		SquareType[][] origBoard = iu.getBoardState();
		Pair<SquareType[][], Integer> newSim = playa.simulateDown(origBoard);
		SquareType[][] newBoard = newSim.getKey();
		
		Assert.assertEquals(SquareType.BLANK, newBoard[0][0]);
		Assert.assertEquals(SquareType.BLANK, newBoard[1][0]);
		Assert.assertEquals(SquareType.BLANK, newBoard[2][0]);
		Assert.assertEquals(SquareType.BLANK, newBoard[3][0]);
		
		Assert.assertEquals(SquareType.BLANK, newBoard[0][1]);
		Assert.assertEquals(SquareType.FOUR, newBoard[1][1]);
		Assert.assertEquals(SquareType.BLANK, newBoard[2][1]);
		Assert.assertEquals(SquareType.BLANK, newBoard[3][1]);
		
		Assert.assertEquals(SquareType.FOUR, newBoard[0][2]);
		Assert.assertEquals(SquareType.FOUR, newBoard[1][2]);
		Assert.assertEquals(SquareType.FOUR, newBoard[2][2]);
		Assert.assertEquals(SquareType.TWO, newBoard[3][2]);
		
		Assert.assertEquals(SquareType.FOUR, newBoard[0][3]);
		Assert.assertEquals(SquareType.TWO, newBoard[1][3]);
		Assert.assertEquals(SquareType.FOUR, newBoard[2][3]);
		Assert.assertEquals(SquareType.EIGHT, newBoard[3][3]);
	}

	@Test
	public void testSimulateDown2() {
		
		iu.setBoardState(
			" THIRTYTWO BLANK FOUR EIGHT " +
			" EIGHT BLANK BLANK BLANK " +
			" FOUR SIXTEEN FOUR EIGHT " +
			" FOUR SIXTEEN SIXTEEN EIGHT "
		);
		
		SquareType[][] origBoard = iu.getBoardState();
		Pair<SquareType[][], Integer> newSim = playa.simulateDown(origBoard);
		SquareType[][] newBoard = newSim.getKey();
		
		Assert.assertEquals(SquareType.BLANK, newBoard[0][0]);
		Assert.assertEquals(SquareType.BLANK, newBoard[1][0]);
		Assert.assertEquals(SquareType.BLANK, newBoard[2][0]);
		Assert.assertEquals(SquareType.BLANK, newBoard[3][0]);
		
		Assert.assertEquals(SquareType.THIRTYTWO, newBoard[0][1]);
		Assert.assertEquals(SquareType.BLANK, newBoard[1][1]);
		Assert.assertEquals(SquareType.BLANK, newBoard[2][1]);
		Assert.assertEquals(SquareType.BLANK, newBoard[3][1]);
		
		Assert.assertEquals(SquareType.EIGHT, newBoard[0][2]);
		Assert.assertEquals(SquareType.BLANK, newBoard[1][2]);
		Assert.assertEquals(SquareType.EIGHT, newBoard[2][2]);
		Assert.assertEquals(SquareType.EIGHT, newBoard[3][2]);
		
		Assert.assertEquals(SquareType.EIGHT, newBoard[0][3]);
		Assert.assertEquals(SquareType.THIRTYTWO, newBoard[1][3]);
		Assert.assertEquals(SquareType.SIXTEEN, newBoard[2][3]);
		Assert.assertEquals(SquareType.SIXTEEN, newBoard[3][3]);
	}
	
	@Test
	public void testSimulateLeft1() {
		
		iu.setBoardState(
			" BLANK TWO TWO TWO " +
			" TWO TWO TWO BLANK " +
			" TWO FOUR TWO FOUR " +
			" FOUR TWO TWO FOUR "
		);
		
		SquareType[][] origBoard = iu.getBoardState();
		Pair<SquareType[][], Integer> newSim = playa.simulateLeft(origBoard);
		SquareType[][] newBoard = newSim.getKey();
		
		Assert.assertEquals(SquareType.FOUR, newBoard[0][0]);
		Assert.assertEquals(SquareType.TWO, newBoard[1][0]);
		Assert.assertEquals(SquareType.BLANK, newBoard[2][0]);
		Assert.assertEquals(SquareType.BLANK, newBoard[3][0]);
		
		Assert.assertEquals(SquareType.FOUR, newBoard[0][1]);
		Assert.assertEquals(SquareType.TWO, newBoard[1][1]);
		Assert.assertEquals(SquareType.BLANK, newBoard[2][1]);
		Assert.assertEquals(SquareType.BLANK, newBoard[3][1]);
		
		Assert.assertEquals(SquareType.TWO, newBoard[0][2]);
		Assert.assertEquals(SquareType.FOUR, newBoard[1][2]);
		Assert.assertEquals(SquareType.TWO, newBoard[2][2]);
		Assert.assertEquals(SquareType.FOUR, newBoard[3][2]);
		
		Assert.assertEquals(SquareType.FOUR, newBoard[0][3]);
		Assert.assertEquals(SquareType.FOUR, newBoard[1][3]);
		Assert.assertEquals(SquareType.FOUR, newBoard[2][3]);
		Assert.assertEquals(SquareType.BLANK, newBoard[3][3]);
	}
	
	@Test
	public void testSimulateLeft2() {
		
		iu.setBoardState(
			" THIRTYTWO BLANK FOUR EIGHT " +
			" EIGHT BLANK BLANK BLANK " +
			" FOUR SIXTEEN FOUR EIGHT " +
			" FOUR SIXTEEN SIXTEEN EIGHT "
		);
		
		SquareType[][] origBoard = iu.getBoardState();
		Pair<SquareType[][], Integer> newSim = playa.simulateLeft(origBoard);
		SquareType[][] newBoard = newSim.getKey();
		
		Assert.assertEquals(SquareType.THIRTYTWO, newBoard[0][0]);
		Assert.assertEquals(SquareType.FOUR, newBoard[1][0]);
		Assert.assertEquals(SquareType.EIGHT, newBoard[2][0]);
		Assert.assertEquals(SquareType.BLANK, newBoard[3][0]);
		
		Assert.assertEquals(SquareType.EIGHT, newBoard[0][1]);
		Assert.assertEquals(SquareType.BLANK, newBoard[1][1]);
		Assert.assertEquals(SquareType.BLANK, newBoard[2][1]);
		Assert.assertEquals(SquareType.BLANK, newBoard[3][1]);
		
		Assert.assertEquals(SquareType.FOUR, newBoard[0][2]);
		Assert.assertEquals(SquareType.SIXTEEN, newBoard[1][2]);
		Assert.assertEquals(SquareType.FOUR, newBoard[2][2]);
		Assert.assertEquals(SquareType.EIGHT, newBoard[3][2]);
		
		Assert.assertEquals(SquareType.FOUR, newBoard[0][3]);
		Assert.assertEquals(SquareType.THIRTYTWO, newBoard[1][3]);
		Assert.assertEquals(SquareType.EIGHT, newBoard[2][3]);
		Assert.assertEquals(SquareType.BLANK, newBoard[3][3]);
	}

	
	@Test
	public void testSimulateRight1() {
		
		iu.setBoardState(
			" BLANK TWO TWO TWO " +
			" TWO TWO TWO BLANK " +
			" TWO FOUR TWO FOUR " +
			" FOUR TWO TWO FOUR "
		);
		
		SquareType[][] origBoard = iu.getBoardState();
		Pair<SquareType[][], Integer> newSim = playa.simulateRight(origBoard);
		SquareType[][] newBoard = newSim.getKey();
		
		Assert.assertEquals(SquareType.BLANK, newBoard[0][0]);
		Assert.assertEquals(SquareType.BLANK, newBoard[1][0]);
		Assert.assertEquals(SquareType.TWO, newBoard[2][0]);
		Assert.assertEquals(SquareType.FOUR, newBoard[3][0]);
		
		Assert.assertEquals(SquareType.BLANK, newBoard[0][1]);
		Assert.assertEquals(SquareType.BLANK, newBoard[1][1]);
		Assert.assertEquals(SquareType.TWO, newBoard[2][1]);
		Assert.assertEquals(SquareType.FOUR, newBoard[3][1]);
		
		Assert.assertEquals(SquareType.TWO, newBoard[0][2]);
		Assert.assertEquals(SquareType.FOUR, newBoard[1][2]);
		Assert.assertEquals(SquareType.TWO, newBoard[2][2]);
		Assert.assertEquals(SquareType.FOUR, newBoard[3][2]);
		
		Assert.assertEquals(SquareType.BLANK, newBoard[0][3]);
		Assert.assertEquals(SquareType.FOUR, newBoard[1][3]);
		Assert.assertEquals(SquareType.FOUR, newBoard[2][3]);
		Assert.assertEquals(SquareType.FOUR, newBoard[3][3]);
	}
	
	@Test
	public void testSimulateRight2() {
		
		iu.setBoardState(
			" THIRTYTWO BLANK FOUR EIGHT " +
			" EIGHT BLANK BLANK BLANK " +
			" FOUR SIXTEEN FOUR EIGHT " +
			" FOUR SIXTEEN SIXTEEN EIGHT "
		);
		
		SquareType[][] origBoard = iu.getBoardState();
		Pair<SquareType[][], Integer> newSim = playa.simulateRight(origBoard);
		SquareType[][] newBoard = newSim.getKey();
		
		Assert.assertEquals(SquareType.BLANK, newBoard[0][0]);
		Assert.assertEquals(SquareType.THIRTYTWO, newBoard[1][0]);
		Assert.assertEquals(SquareType.FOUR, newBoard[2][0]);
		Assert.assertEquals(SquareType.EIGHT, newBoard[3][0]);
		
		Assert.assertEquals(SquareType.BLANK, newBoard[0][1]);
		Assert.assertEquals(SquareType.BLANK, newBoard[1][1]);
		Assert.assertEquals(SquareType.BLANK, newBoard[2][1]);
		Assert.assertEquals(SquareType.EIGHT, newBoard[3][1]);
		
		Assert.assertEquals(SquareType.FOUR, newBoard[0][2]);
		Assert.assertEquals(SquareType.SIXTEEN, newBoard[1][2]);
		Assert.assertEquals(SquareType.FOUR, newBoard[2][2]);
		Assert.assertEquals(SquareType.EIGHT, newBoard[3][2]);
		
		Assert.assertEquals(SquareType.BLANK, newBoard[0][3]);
		Assert.assertEquals(SquareType.FOUR, newBoard[1][3]);
		Assert.assertEquals(SquareType.THIRTYTWO, newBoard[2][3]);
		Assert.assertEquals(SquareType.EIGHT, newBoard[3][3]);

	}
}
