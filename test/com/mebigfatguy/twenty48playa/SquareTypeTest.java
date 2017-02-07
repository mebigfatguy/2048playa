package com.mebigfatguy.twenty48playa;

import org.junit.Assert;
import org.junit.Test;

public class SquareTypeTest {

    @Test
    public void testGetValue() {

        Assert.assertEquals(0, SquareType.BLANK.getValue());
        Assert.assertEquals(0, SquareType.STUB.getValue());
        Assert.assertEquals(2, SquareType.TWO.getValue());
        Assert.assertEquals(4, SquareType.FOUR.getValue());
    }
}
