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
import java.awt.Color;
import java.awt.Desktop;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.Robot;
import java.awt.event.InputEvent;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

import javax.swing.JWindow;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class WindowManager {

    private static final Logger LOGGER = LoggerFactory.getLogger(WindowManager.class);

    private final ImageUtils imageUtils;
    private final Robot robot;

    public WindowManager(ImageUtils iu) throws AWTException {
        imageUtils = iu;
        robot = new Robot();
    }

    public void launch2048(boolean firstTime) throws URISyntaxException, IOException {

        JWindow blackWindow = null;
        try {
            Rectangle screenBounds = imageUtils.getScreenBounds();

            if (firstTime) {
                blackWindow = new JWindow() {
                    private static final long serialVersionUID = -9148401691937086414L;

                    @Override
                    public void paint(Graphics g) {
                    }
                };
                blackWindow.setBounds(screenBounds);
                blackWindow.setBackground(Color.BLACK);
                blackWindow.setVisible(true);
            }

            Desktop dt = Desktop.getDesktop();
            dt.browse(new URI("http://gabrielecirulli.github.io/2048/"));

            robot.delay(7000);
            if (firstTime) {
                BufferedImage image = imageUtils.getScreenBuffer();

                Rectangle browserBounds = imageUtils.findBlackInset(image);

                if ((browserBounds != null) && !browserBounds.equals(screenBounds)) {
                    doubleClick(new Point(browserBounds.x + 40, browserBounds.y + 5));
                    robot.delay(2000);
                }
            }

            int attempts = 10;
            while (attempts > 0) {
                robot.delay(1000);
                try {
                    imageUtils.initialize();
                    return;
                } catch (ArrayIndexOutOfBoundsException e) {
                    // the window is taking forever to show up
                    attempts--;
                }
            }
        } finally {
            if (blackWindow != null) {
                blackWindow.dispose();
            }
        }
    }

    public void click(Point pt) {
        robot.mouseMove(pt.x, pt.y);
        robot.mousePress(InputEvent.BUTTON1_DOWN_MASK);
        robot.delay(10);
        robot.mouseRelease(InputEvent.BUTTON1_DOWN_MASK);
    }

    public void doubleClick(Point pt) {
        click(pt);
        click(pt);
    }

    public void key(int keycode) {
        robot.keyPress(keycode);
        robot.delay(10);
        robot.keyRelease(keycode);
    }

    public void delay(int millis) {
        robot.delay(millis);
    }

    public void clickContinue() {
        robot.delay(5000);

        Rectangle bounds = imageUtils.getBoardCoordinates();
        click(new Point(bounds.x + (bounds.width / 3), bounds.y + (bounds.height / 2)));
        robot.delay(5000);
    }
}
