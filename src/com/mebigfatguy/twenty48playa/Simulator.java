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

import java.util.Random;

public final class Simulator {

    private static Random random = new Random();

    private Simulator() {
    }

    public static Pair<Board, Integer> simulateUp(Board board) {
        Board simBoard = board.clone();
        int score = 0;
        int preFillCount = simBoard.fillCount();

        for (int x = 0; x < 4; x++) {
            for (int y = 0; y < 3; y++) {
                SquareType yType = simBoard.get(x, y);
                for (int srcY = y + 1; srcY < 4; srcY++) {
                    SquareType srcYType = simBoard.get(x, srcY);
                    if (yType == SquareType.BLANK) {
                        if (srcYType != SquareType.BLANK) {
                            simBoard.set(x, y, srcYType);
                            yType = srcYType;
                            int tY = srcY + 1;
                            for (int copyY = y + 1; copyY < 4; copyY++) {
                                if (tY < 4) {
                                    simBoard.set(x, copyY, simBoard.get(x, tY++));
                                } else {
                                    simBoard.set(x, copyY, SquareType.BLANK);
                                }
                            }
                            srcY = y;
                        }
                    } else {
                        if ((srcYType != SquareType.BLANK) && (srcYType != SquareType.STUB)) {
                            if (srcYType == yType) {
                                simBoard.set(x, y, SquareType.values()[srcYType.ordinal() + 1]);
                                score += Math.pow(2, ((srcYType.ordinal() + 1) - SquareType.TWO.ordinal()) + 1);
                                srcY++;
                                for (int copyY = y + 1; copyY < 4; copyY++) {
                                    if (srcY < 4) {
                                        simBoard.set(x, copyY, simBoard.get(x, srcY++));
                                    } else {
                                        simBoard.set(x, copyY, SquareType.BLANK);
                                    }
                                }
                            }
                            break;
                        }
                    }
                }
            }
        }
        if (preFillCount == 16) {
            int postFillCount = simBoard.fillCount();
            if (postFillCount == 16) {
                score = Integer.MIN_VALUE;
            }
        }

        return new Pair<>(simBoard, Integer.valueOf(score));
    }

    public static Pair<Board, Integer> simulateDown(Board board) {
        Board simBoard = board.clone();
        int score = 0;
        int preFillCount = simBoard.fillCount();

        for (int x = 0; x < 4; x++) {
            for (int y = 3; y > 0; y--) {
                SquareType yType = simBoard.get(x, y);
                for (int srcY = y - 1; srcY >= 0; srcY--) {
                    SquareType srcYType = simBoard.get(x, srcY);
                    if (yType == SquareType.BLANK) {
                        if (srcYType != SquareType.BLANK) {
                            simBoard.set(x, y, srcYType);
                            yType = srcYType;
                            int tY = srcY - 1;
                            for (int copyY = y - 1; copyY >= 0; copyY--) {
                                if (tY >= 0) {
                                    simBoard.set(x, copyY, simBoard.get(x, tY--));
                                } else {
                                    simBoard.set(x, copyY, SquareType.BLANK);
                                }
                            }
                            srcY = y;
                        }
                    } else {
                        if ((srcYType != SquareType.BLANK) && (srcYType != SquareType.STUB)) {
                            if (srcYType == yType) {
                                simBoard.set(x, y, SquareType.values()[srcYType.ordinal() + 1]);
                                score += Math.pow(2, ((srcYType.ordinal() + 1) - SquareType.TWO.ordinal()) + 1);
                                srcY--;
                                for (int copyY = y - 1; copyY >= 0; copyY--) {
                                    if (srcY >= 0) {
                                        simBoard.set(x, copyY, simBoard.get(x, srcY--));
                                    } else {
                                        simBoard.set(x, copyY, SquareType.BLANK);
                                    }
                                }
                            }
                            break;
                        }
                    }
                }
            }
        }
        if (preFillCount == 16) {
            int postFillCount = simBoard.fillCount();
            if (postFillCount == 16) {
                score = Integer.MIN_VALUE;
            }
        }

        return new Pair<>(simBoard, Integer.valueOf(score));
    }

    public static Pair<Board, Integer> simulateLeft(Board board) {
        Board simBoard = board.clone();
        int score = 0;
        int preFillCount = simBoard.fillCount();

        for (int y = 0; y < 4; y++) {
            for (int x = 0; x < 3; x++) {
                SquareType xType = simBoard.get(x, y);
                for (int srcX = x + 1; srcX < 4; srcX++) {
                    SquareType srcXType = simBoard.get(srcX, y);
                    if (xType == SquareType.BLANK) {
                        if (srcXType != SquareType.BLANK) {
                            simBoard.set(x, y, srcXType);
                            xType = srcXType;
                            int tX = srcX + 1;
                            for (int copyX = x + 1; copyX < 4; copyX++) {
                                if (tX < 4) {
                                    simBoard.set(copyX, y, simBoard.get(tX++, y));
                                } else {
                                    simBoard.set(copyX, y, SquareType.BLANK);
                                }
                            }
                            srcX = x;
                        }
                    } else {
                        if ((srcXType != SquareType.BLANK) && (srcXType != SquareType.STUB)) {
                            if (srcXType == xType) {
                                simBoard.set(x, y, SquareType.values()[srcXType.ordinal() + 1]);
                                score += Math.pow(2, ((srcXType.ordinal() + 1) - SquareType.TWO.ordinal()) + 1);
                                srcX++;
                                for (int copyX = x + 1; copyX < 4; copyX++) {
                                    if (srcX < 4) {
                                        simBoard.set(copyX, y, simBoard.get(srcX++, y));
                                    } else {
                                        simBoard.set(copyX, y, SquareType.BLANK);
                                    }
                                }
                            }
                            break;
                        }
                    }
                }
            }
        }
        if (preFillCount == 16) {
            int postFillCount = simBoard.fillCount();
            if (postFillCount == 16) {
                score = Integer.MIN_VALUE;
            }
        }

        return new Pair<>(simBoard, Integer.valueOf(score));
    }

    public static Pair<Board, Integer> simulateRight(Board board) {
        Board simBoard = board.clone();
        int score = 0;
        int preFillCount = simBoard.fillCount();

        for (int y = 0; y < 4; y++) {
            for (int x = 3; x > 0; x--) {
                SquareType xType = simBoard.get(x, y);
                for (int srcX = x - 1; srcX >= 0; srcX--) {
                    SquareType srcXType = simBoard.get(srcX, y);
                    if (xType == SquareType.BLANK) {
                        if (srcXType != SquareType.BLANK) {
                            simBoard.set(x, y, srcXType);
                            xType = srcXType;
                            int tx = srcX - 1;
                            for (int copyX = x - 1; copyX >= 0; copyX--) {
                                if (tx >= 0) {
                                    simBoard.set(copyX, y, simBoard.get(tx--, y));
                                } else {
                                    simBoard.set(copyX, y, SquareType.BLANK);
                                }
                            }
                            srcX = x;
                        }
                    } else {
                        if ((srcXType != SquareType.BLANK) && (srcXType != SquareType.STUB)) {
                            if (srcXType == xType) {
                                simBoard.set(x, y, SquareType.values()[srcXType.ordinal() + 1]);
                                score += Math.pow(2, ((srcXType.ordinal() + 1) - SquareType.TWO.ordinal()) + 1);
                                srcX--;
                                for (int copyX = x - 1; copyX >= 0; copyX--) {
                                    if (srcX >= 0) {
                                        simBoard.set(copyX, y, simBoard.get(srcX--, y));
                                    } else {
                                        simBoard.set(copyX, y, SquareType.BLANK);
                                    }
                                }
                            }
                            break;
                        }
                    }
                }
            }
        }
        if (preFillCount == 16) {
            int postFillCount = simBoard.fillCount();
            if (postFillCount == 16) {
                score = Integer.MIN_VALUE;
            }
        }

        return new Pair<>(simBoard, Integer.valueOf(score));
    }

    public static boolean embellishSimulation(Board board) {
        int freeSpace = 16 - board.fillCount();
        if (freeSpace <= 0) {
            return false;
        }

        freeSpace = random.nextInt(freeSpace);
        for (int y = 0; y < 4; y++) {
            for (int x = 0; x < 4; x++) {
                if (board.isBlank(x, y)) {
                    if (freeSpace == 0) {
                        board.set(x, y, SquareType.STUB);
                        return true;
                    }
                    freeSpace--;
                }
            }
        }
        return false;
    }
}
