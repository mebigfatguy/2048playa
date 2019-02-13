/** 2048 playa - a 2048 autonomous player.
 * Copyright 2014-2019 MeBigFatGuy.com
 * Copyright 2014-2019 Dave Brosius
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

public class Board implements Cloneable {

    SquareType[][] squares;

    public Board() {
        squares = new SquareType[4][4];
    }

    @Override
    public Board clone() {
        try {
            Board newBoard = (Board) super.clone();
            newBoard.squares = new SquareType[4][4];

            for (int y = 0; y < 4; y++) {
                for (int x = 0; x < 4; x++) {
                    newBoard.squares[x][y] = squares[x][y];
                }
            }

            return newBoard;
        } catch (CloneNotSupportedException e) {
            throw new Error(e);
        }
    }

    public SquareType get(int x, int y) {
        return squares[x][y];
    }

    public void set(int x, int y, SquareType type) {
        squares[x][y] = type;
    }

    public boolean isBlank(int x, int y) {
        return squares[x][y] == SquareType.BLANK;
    }

    public boolean is2048(int x, int y) {
        return squares[x][y] == SquareType.TWENTYFOURTYEIGHT;
    }

    public boolean has2048() {
        for (int y = 0; y < 4; y++) {
            for (int x = 0; x < 4; x++) {
                if (is2048(x, y)) {
                    return true;
                }
            }
        }

        return false;
    }

    public int fillCount() {
        int count = 0;

        for (int y = 0; y < 4; y++) {
            for (int x = 0; x < 4; x++) {
                if (squares[x][y] != SquareType.BLANK) {
                    count++;
                }
            }
        }

        return count;
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof Board)) {
            return false;
        }

        Board that = (Board) o;

        return Arrays.deepEquals(squares, that.squares);
    }

    @Override
    public int hashCode() {
        return Arrays.deepHashCode(squares);
    }

    @Override
    public String toString() {
        return Arrays.deepToString(squares);
    }
}
