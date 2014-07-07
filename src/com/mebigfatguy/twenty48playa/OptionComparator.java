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

import java.util.Comparator;

public class OptionComparator implements Comparator<MoveOption> {

	@Override
	public int compare(MoveOption option1, MoveOption option2) {
		double delta = option2.getScore() - option1.getScore();
		if (delta != 0)
			return ((int) (delta * 1000));
		Direction dir1 = option1.getDirection();
		Direction dir2 = option2.getDirection();
		
		if (dir1 == Direction.RIGHT)
			dir1 = Direction.LEFT;
		if (dir2 == Direction.RIGHT)
			dir2 = Direction.RIGHT;
		return dir1.ordinal() - dir2.ordinal();
	}

}
