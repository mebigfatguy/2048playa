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

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class SimulatorTest {

    private MockImageUtils iu;

    @Before
    public void setUp() throws AWTException {

        iu = new MockImageUtils();
        WindowManager wm = new WindowManager(iu);
    }

    @Test
    public void testSimulateUp1() {

        iu.setBoardState(" BLANK TWO TWO TWO " + " TWO TWO TWO BLANK " + " TWO FOUR TWO FOUR " + " FOUR TWO TWO FOUR ");

        Board origBoard = iu.getBoardState();
        Pair<Board, Double> newSim = Simulator.simulateUp(origBoard);
        Board newBoard = newSim.getKey();

        Assert.assertEquals(SquareType.FOUR, newBoard.get(0, 0));
        Assert.assertEquals(SquareType.FOUR, newBoard.get(1, 0));
        Assert.assertEquals(SquareType.FOUR, newBoard.get(2, 0));
        Assert.assertEquals(SquareType.TWO, newBoard.get(3, 0));

        Assert.assertEquals(SquareType.FOUR, newBoard.get(0, 1));
        Assert.assertEquals(SquareType.FOUR, newBoard.get(1, 1));
        Assert.assertEquals(SquareType.FOUR, newBoard.get(2, 1));
        Assert.assertEquals(SquareType.EIGHT, newBoard.get(3, 1));

        Assert.assertEquals(SquareType.BLANK, newBoard.get(0, 2));
        Assert.assertEquals(SquareType.TWO, newBoard.get(1, 2));
        Assert.assertEquals(SquareType.BLANK, newBoard.get(2, 2));
        Assert.assertEquals(SquareType.BLANK, newBoard.get(3, 2));

        Assert.assertEquals(SquareType.BLANK, newBoard.get(0, 3));
        Assert.assertEquals(SquareType.BLANK, newBoard.get(1, 3));
        Assert.assertEquals(SquareType.BLANK, newBoard.get(2, 3));
        Assert.assertEquals(SquareType.BLANK, newBoard.get(3, 3));
    }

    @Test
    public void testSimulateUp2() {

        iu.setBoardState(" THIRTYTWO BLANK FOUR EIGHT " + " EIGHT BLANK BLANK BLANK " + " FOUR SIXTEEN FOUR EIGHT " + " FOUR SIXTEEN SIXTEEN EIGHT ");

        Board origBoard = iu.getBoardState();
        Pair<Board, Double> newSim = Simulator.simulateUp(origBoard);
        Board newBoard = newSim.getKey();

        Assert.assertEquals(SquareType.THIRTYTWO, newBoard.get(0, 0));
        Assert.assertEquals(SquareType.THIRTYTWO, newBoard.get(1, 0));
        Assert.assertEquals(SquareType.EIGHT, newBoard.get(2, 0));
        Assert.assertEquals(SquareType.SIXTEEN, newBoard.get(3, 0));

        Assert.assertEquals(SquareType.EIGHT, newBoard.get(0, 1));
        Assert.assertEquals(SquareType.BLANK, newBoard.get(1, 1));
        Assert.assertEquals(SquareType.SIXTEEN, newBoard.get(2, 1));
        Assert.assertEquals(SquareType.EIGHT, newBoard.get(3, 1));

        Assert.assertEquals(SquareType.EIGHT, newBoard.get(0, 2));
        Assert.assertEquals(SquareType.BLANK, newBoard.get(1, 2));
        Assert.assertEquals(SquareType.BLANK, newBoard.get(2, 2));
        Assert.assertEquals(SquareType.BLANK, newBoard.get(3, 2));

        Assert.assertEquals(SquareType.BLANK, newBoard.get(0, 3));
        Assert.assertEquals(SquareType.BLANK, newBoard.get(1, 3));
        Assert.assertEquals(SquareType.BLANK, newBoard.get(2, 3));
        Assert.assertEquals(SquareType.BLANK, newBoard.get(3, 3));
    }

    @Test
    public void testSimulateDown1() {

        iu.setBoardState(" BLANK TWO TWO TWO " + " TWO TWO TWO BLANK " + " TWO FOUR TWO FOUR " + " FOUR TWO TWO FOUR ");

        Board origBoard = iu.getBoardState();
        Pair<Board, Double> newSim = Simulator.simulateDown(origBoard);
        Board newBoard = newSim.getKey();

        Assert.assertEquals(SquareType.BLANK, newBoard.get(0, 0));
        Assert.assertEquals(SquareType.BLANK, newBoard.get(1, 0));
        Assert.assertEquals(SquareType.BLANK, newBoard.get(2, 0));
        Assert.assertEquals(SquareType.BLANK, newBoard.get(3, 0));

        Assert.assertEquals(SquareType.BLANK, newBoard.get(0, 1));
        Assert.assertEquals(SquareType.FOUR, newBoard.get(1, 1));
        Assert.assertEquals(SquareType.BLANK, newBoard.get(2, 1));
        Assert.assertEquals(SquareType.BLANK, newBoard.get(3, 1));

        Assert.assertEquals(SquareType.FOUR, newBoard.get(0, 2));
        Assert.assertEquals(SquareType.FOUR, newBoard.get(1, 2));
        Assert.assertEquals(SquareType.FOUR, newBoard.get(2, 2));
        Assert.assertEquals(SquareType.TWO, newBoard.get(3, 2));

        Assert.assertEquals(SquareType.FOUR, newBoard.get(0, 3));
        Assert.assertEquals(SquareType.TWO, newBoard.get(1, 3));
        Assert.assertEquals(SquareType.FOUR, newBoard.get(2, 3));
        Assert.assertEquals(SquareType.EIGHT, newBoard.get(3, 3));
    }

    @Test
    public void testSimulateDown2() {

        iu.setBoardState(" THIRTYTWO BLANK FOUR EIGHT " + " EIGHT BLANK BLANK BLANK " + " FOUR SIXTEEN FOUR EIGHT " + " FOUR SIXTEEN SIXTEEN EIGHT ");

        Board origBoard = iu.getBoardState();
        Pair<Board, Double> newSim = Simulator.simulateDown(origBoard);
        Board newBoard = newSim.getKey();

        Assert.assertEquals(SquareType.BLANK, newBoard.get(0, 0));
        Assert.assertEquals(SquareType.BLANK, newBoard.get(1, 0));
        Assert.assertEquals(SquareType.BLANK, newBoard.get(2, 0));
        Assert.assertEquals(SquareType.BLANK, newBoard.get(3, 0));

        Assert.assertEquals(SquareType.THIRTYTWO, newBoard.get(0, 1));
        Assert.assertEquals(SquareType.BLANK, newBoard.get(1, 1));
        Assert.assertEquals(SquareType.BLANK, newBoard.get(2, 1));
        Assert.assertEquals(SquareType.BLANK, newBoard.get(3, 1));

        Assert.assertEquals(SquareType.EIGHT, newBoard.get(0, 2));
        Assert.assertEquals(SquareType.BLANK, newBoard.get(1, 2));
        Assert.assertEquals(SquareType.EIGHT, newBoard.get(2, 2));
        Assert.assertEquals(SquareType.EIGHT, newBoard.get(3, 2));

        Assert.assertEquals(SquareType.EIGHT, newBoard.get(0, 3));
        Assert.assertEquals(SquareType.THIRTYTWO, newBoard.get(1, 3));
        Assert.assertEquals(SquareType.SIXTEEN, newBoard.get(2, 3));
        Assert.assertEquals(SquareType.SIXTEEN, newBoard.get(3, 3));
    }

    @Test
    public void testSimulateLeft1() {

        iu.setBoardState(" BLANK TWO TWO TWO " + " TWO TWO TWO BLANK " + " TWO FOUR TWO FOUR " + " FOUR TWO TWO FOUR ");

        Board origBoard = iu.getBoardState();
        Pair<Board, Double> newSim = Simulator.simulateLeft(origBoard);
        Board newBoard = newSim.getKey();

        Assert.assertEquals(SquareType.FOUR, newBoard.get(0, 0));
        Assert.assertEquals(SquareType.TWO, newBoard.get(1, 0));
        Assert.assertEquals(SquareType.BLANK, newBoard.get(2, 0));
        Assert.assertEquals(SquareType.BLANK, newBoard.get(3, 0));

        Assert.assertEquals(SquareType.FOUR, newBoard.get(0, 1));
        Assert.assertEquals(SquareType.TWO, newBoard.get(1, 1));
        Assert.assertEquals(SquareType.BLANK, newBoard.get(2, 1));
        Assert.assertEquals(SquareType.BLANK, newBoard.get(3, 1));

        Assert.assertEquals(SquareType.TWO, newBoard.get(0, 2));
        Assert.assertEquals(SquareType.FOUR, newBoard.get(1, 2));
        Assert.assertEquals(SquareType.TWO, newBoard.get(2, 2));
        Assert.assertEquals(SquareType.FOUR, newBoard.get(3, 2));

        Assert.assertEquals(SquareType.FOUR, newBoard.get(0, 3));
        Assert.assertEquals(SquareType.FOUR, newBoard.get(1, 3));
        Assert.assertEquals(SquareType.FOUR, newBoard.get(2, 3));
        Assert.assertEquals(SquareType.BLANK, newBoard.get(3, 3));
    }

    @Test
    public void testSimulateLeft2() {

        iu.setBoardState(" THIRTYTWO BLANK FOUR EIGHT " + " EIGHT BLANK BLANK BLANK " + " FOUR SIXTEEN FOUR EIGHT " + " FOUR SIXTEEN SIXTEEN EIGHT ");

        Board origBoard = iu.getBoardState();
        Pair<Board, Double> newSim = Simulator.simulateLeft(origBoard);
        Board newBoard = newSim.getKey();

        Assert.assertEquals(SquareType.THIRTYTWO, newBoard.get(0, 0));
        Assert.assertEquals(SquareType.FOUR, newBoard.get(1, 0));
        Assert.assertEquals(SquareType.EIGHT, newBoard.get(2, 0));
        Assert.assertEquals(SquareType.BLANK, newBoard.get(3, 0));

        Assert.assertEquals(SquareType.EIGHT, newBoard.get(0, 1));
        Assert.assertEquals(SquareType.BLANK, newBoard.get(1, 1));
        Assert.assertEquals(SquareType.BLANK, newBoard.get(2, 1));
        Assert.assertEquals(SquareType.BLANK, newBoard.get(3, 1));

        Assert.assertEquals(SquareType.FOUR, newBoard.get(0, 2));
        Assert.assertEquals(SquareType.SIXTEEN, newBoard.get(1, 2));
        Assert.assertEquals(SquareType.FOUR, newBoard.get(2, 2));
        Assert.assertEquals(SquareType.EIGHT, newBoard.get(3, 2));

        Assert.assertEquals(SquareType.FOUR, newBoard.get(0, 3));
        Assert.assertEquals(SquareType.THIRTYTWO, newBoard.get(1, 3));
        Assert.assertEquals(SquareType.EIGHT, newBoard.get(2, 3));
        Assert.assertEquals(SquareType.BLANK, newBoard.get(3, 3));
    }

    @Test
    public void testSimulateRight1() {

        iu.setBoardState(" BLANK TWO TWO TWO " + " TWO TWO TWO BLANK " + " TWO FOUR TWO FOUR " + " FOUR TWO TWO FOUR ");

        Board origBoard = iu.getBoardState();
        Pair<Board, Double> newSim = Simulator.simulateRight(origBoard);
        Board newBoard = newSim.getKey();

        Assert.assertEquals(SquareType.BLANK, newBoard.get(0, 0));
        Assert.assertEquals(SquareType.BLANK, newBoard.get(1, 0));
        Assert.assertEquals(SquareType.TWO, newBoard.get(2, 0));
        Assert.assertEquals(SquareType.FOUR, newBoard.get(3, 0));

        Assert.assertEquals(SquareType.BLANK, newBoard.get(0, 1));
        Assert.assertEquals(SquareType.BLANK, newBoard.get(1, 1));
        Assert.assertEquals(SquareType.TWO, newBoard.get(2, 1));
        Assert.assertEquals(SquareType.FOUR, newBoard.get(3, 1));

        Assert.assertEquals(SquareType.TWO, newBoard.get(0, 2));
        Assert.assertEquals(SquareType.FOUR, newBoard.get(1, 2));
        Assert.assertEquals(SquareType.TWO, newBoard.get(2, 2));
        Assert.assertEquals(SquareType.FOUR, newBoard.get(3, 2));

        Assert.assertEquals(SquareType.BLANK, newBoard.get(0, 3));
        Assert.assertEquals(SquareType.FOUR, newBoard.get(1, 3));
        Assert.assertEquals(SquareType.FOUR, newBoard.get(2, 3));
        Assert.assertEquals(SquareType.FOUR, newBoard.get(3, 3));
    }

    @Test
    public void testSimulateRight2() {

        iu.setBoardState(" THIRTYTWO BLANK FOUR EIGHT " + " EIGHT BLANK BLANK BLANK " + " FOUR SIXTEEN FOUR EIGHT " + " FOUR SIXTEEN SIXTEEN EIGHT ");

        Board origBoard = iu.getBoardState();
        Pair<Board, Double> newSim = Simulator.simulateRight(origBoard);
        Board newBoard = newSim.getKey();

        Assert.assertEquals(SquareType.BLANK, newBoard.get(0, 0));
        Assert.assertEquals(SquareType.THIRTYTWO, newBoard.get(1, 0));
        Assert.assertEquals(SquareType.FOUR, newBoard.get(2, 0));
        Assert.assertEquals(SquareType.EIGHT, newBoard.get(3, 0));

        Assert.assertEquals(SquareType.BLANK, newBoard.get(0, 1));
        Assert.assertEquals(SquareType.BLANK, newBoard.get(1, 1));
        Assert.assertEquals(SquareType.BLANK, newBoard.get(2, 1));
        Assert.assertEquals(SquareType.EIGHT, newBoard.get(3, 1));

        Assert.assertEquals(SquareType.FOUR, newBoard.get(0, 2));
        Assert.assertEquals(SquareType.SIXTEEN, newBoard.get(1, 2));
        Assert.assertEquals(SquareType.FOUR, newBoard.get(2, 2));
        Assert.assertEquals(SquareType.EIGHT, newBoard.get(3, 2));

        Assert.assertEquals(SquareType.BLANK, newBoard.get(0, 3));
        Assert.assertEquals(SquareType.FOUR, newBoard.get(1, 3));
        Assert.assertEquals(SquareType.THIRTYTWO, newBoard.get(2, 3));
        Assert.assertEquals(SquareType.EIGHT, newBoard.get(3, 3));

    }
}
