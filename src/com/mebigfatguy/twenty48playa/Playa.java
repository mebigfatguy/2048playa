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

import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Playa {

    private static final Logger LOGGER = LoggerFactory.getLogger(Playa.class);
    private static final OptionComparator OPTION_COMPARATOR = new OptionComparator();
    private static final int RECURSION_DEPTH = 4;
    private static final double TERMINATION_PENALTY = 0.5;

    private final ImageUtils imageUtils;
    private final WindowManager windowManager;
    private Board oldBoard;
    private final Random random;

    public Playa(ImageUtils iu, WindowManager wm) {
        imageUtils = iu;
        windowManager = wm;
        random = new Random(System.currentTimeMillis());
    }

    public void playGame() {

        oldBoard = new Board();
        boolean done = false;
        boolean seen2048 = false;

        openingGambit();
        do {
            Board board = imageUtils.getBoardState();

            MoveOption bestOption = getBestDirection(new MoveOption(Direction.DOWN, 0, board), RECURSION_DEPTH);
            collide(bestOption.getDirection());

            if (finished(board)) {
                return;
            }

            if (!seen2048 && board.has2048()) {
                seen2048 = true;
                windowManager.clickContinue();
            }

        } while (!done);
    }

    private void openingGambit() {
        Board board = imageUtils.getBoardState();
        oldBoard = new Board();

        while (board.fillCount() < 6) {
            oldBoard = board.clone();
            for (int i = 0; i < 10; i++) {
                for (int j = 0; j < 5; j++) {
                    windowManager.key(KeyEvent.VK_UP);
                }

                windowManager.key(KeyEvent.VK_LEFT);
                windowManager.key(KeyEvent.VK_RIGHT);
            }
            board = imageUtils.getBoardState();

            if (oldBoard.equals(board)) {
                break;
            }
        }
    }

    MoveOption getBestDirection(MoveOption origOption, int depth) {

        if (depth == 0) {
            return origOption;
        }

        List<MoveOption> options = new ArrayList<>();

        Pair<Board, Double> upSim = Simulator.simulateUp(origOption.getResultantBoard());
        if (!origOption.getResultantBoard().equals(upSim.getKey())) {
            double weightedScore = origOption.getScore() + ((upSim.getValue().doubleValue() * depth) / RECURSION_DEPTH);
            if (Simulator.embellishSimulation(upSim.getKey())) {
                MoveOption upOption = getBestDirection(new MoveOption(Direction.UP, weightedScore, upSim.getKey()), depth - 1);
                options.add(new MoveOption(Direction.UP, upOption.getScore(), upSim.getKey()));
            } else {
                options.add(new MoveOption(Direction.UP, weightedScore * depth * TERMINATION_PENALTY, upSim.getKey()));
            }
        }

        if (origOption.getResultantBoard().fillCount() > 10) {
            Pair<Board, Double> downSim = Simulator.simulateDown(origOption.getResultantBoard());
            if (!origOption.getResultantBoard().equals(downSim.getKey())) {
                double weightedScore = origOption.getScore() + ((downSim.getValue().doubleValue() * depth) / RECURSION_DEPTH);
                if (Simulator.embellishSimulation(downSim.getKey())) {
                    MoveOption downOption = getBestDirection(new MoveOption(Direction.DOWN, weightedScore, downSim.getKey()), depth - 1);
                    options.add(new MoveOption(Direction.DOWN, downOption.getScore(), downSim.getKey()));
                } else {
                    options.add(new MoveOption(Direction.DOWN, weightedScore * depth * TERMINATION_PENALTY, downSim.getKey()));
                }
            }
        }

        Pair<Board, Double> leftSim = Simulator.simulateLeft(origOption.getResultantBoard());
        if (!origOption.getResultantBoard().equals(leftSim.getKey())) {
            double weightedScore = origOption.getScore() + ((leftSim.getValue().doubleValue() * depth) / RECURSION_DEPTH);
            if (Simulator.embellishSimulation(leftSim.getKey())) {
                MoveOption leftOption = getBestDirection(new MoveOption(Direction.LEFT, weightedScore, leftSim.getKey()), depth - 1);
                options.add(new MoveOption(Direction.LEFT, leftOption.getScore(), leftSim.getKey()));
            } else {
                options.add(new MoveOption(Direction.LEFT, weightedScore * depth * TERMINATION_PENALTY, leftSim.getKey()));
            }
        }

        Pair<Board, Double> rightSim = Simulator.simulateRight(origOption.getResultantBoard());
        if (!origOption.getResultantBoard().equals(rightSim.getKey())) {
            double weightedScore = origOption.getScore() + ((rightSim.getValue().doubleValue() * depth) / RECURSION_DEPTH);
            if (Simulator.embellishSimulation(rightSim.getKey())) {
                MoveOption rightOption = getBestDirection(new MoveOption(Direction.RIGHT, weightedScore, rightSim.getKey()), depth - 1);
                options.add(new MoveOption(Direction.RIGHT, rightOption.getScore(), rightSim.getKey()));
            } else {
                options.add(new MoveOption(Direction.RIGHT, weightedScore * depth * TERMINATION_PENALTY, rightSim.getKey()));
            }
        }

        if (options.isEmpty()) {
            return new MoveOption(Direction.DOWN, 0.0, origOption.getResultantBoard());
        }

        Collections.sort(options, OPTION_COMPARATOR);

        if (options.size() > 1) {
            MoveOption op1 = options.get(0);
            MoveOption op2 = options.get(1);

            if (((op1.getScore() == op2.getScore()) && (op1.getDirection() == Direction.LEFT)) && (op2.getDirection() == Direction.RIGHT)) {
                return random.nextBoolean() ? op1 : op2;
            }
        }
        return options.get(0);
    }

    private boolean finished(Board board) {

        boolean finished = board.equals(oldBoard);

        oldBoard = board.clone();
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

            default:
            break;
        }
        windowManager.delay(500);
    }
}
