package com.simorgh.cycleview;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ExampleUnitTest {
    int offsetAngle = 10;
    CycleView.ClueData clueData = new CycleView.ClueData(6, 21);

    @Test
    public void addition_isCorrect() {
//        assertEquals(4, 2 + 2);
        float dayAng = calculateAngleForDay(120);
        int dayN = getDayFromAngle(dayAng+360);
        System.out.println(dayAng + "  " + dayN);
    }

    @Test
    public void testAngleCalculationForDays() {
        for (int red = 1; red <= 12; red++) {
            for (int gray = 1; gray <= 28; gray++) {
                float dayAng;
                int dayN;
                clueData = new CycleView.ClueData(red, gray);
                System.out.println("#################################");
                System.out.println("#################################");
                System.out.println("red: " + red + " ----- " + "white: " + gray);
                for (int i = 1; i <= clueData.getTotalDays(); i++) {
                    dayAng = calculateAngleForDay(i);
                    dayN = getDayFromAngle(dayAng);
                    assertEquals(i, dayN);
                    System.out.println(i + " : " + dayAng + " : " + dayN);
                }
                System.out.println("#################################");
                System.out.println("#################################");
            }
        }
    }

    private float calculateAngleForDay(int dayNumber) {
        if (dayNumber > clueData.getTotalDays()) {
            dayNumber = dayNumber % clueData.getTotalDays();
        }
        float angleUnit = (360 - 2 * offsetAngle) / (float) clueData.getTotalDays();
        return angleUnit * dayNumber + offsetAngle - 90;
    }

    private int getDayFromAngle(float currentAngle) {
        if (currentAngle > 270) {
            currentAngle = 360 + offsetAngle - (currentAngle % 360);
            currentAngle = -currentAngle;
        }
        float angleUnit = (360 - 2 * offsetAngle) / (float) clueData.getTotalDays();
        float temp = ((currentAngle + 90 - offsetAngle) / angleUnit);
        int currentDay;
        if (temp - Math.floor(temp) <= 0.5) {
            temp = (float) Math.floor(temp);
            currentDay = (int) temp;
        } else {
            temp = (float) Math.ceil(temp);
            currentDay = (int) temp;
        }
        return currentDay;
    }
}