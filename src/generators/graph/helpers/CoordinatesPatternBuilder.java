/**  * Timm Welz, 2018 for the Animal project at TU Darmstadt.  * Copying this file for educational purposes is permitted without further authorization.  */ package generators.graph.helpers;

import algoanim.util.Coordinates;

public class CoordinatesPatternBuilder {

    public static Coordinates[] getCircularPattern(int numberOfItems, int centerX, int centerY, int radius) {
        double currentAngle = -90.0;
        double radiusD = (double) radius;
        double incrValue = 360.0 / numberOfItems;
        double centerXD = (double) centerX;
        double centerYD = (double) centerY;
        int x, y;
        Coordinates[] result = new Coordinates[numberOfItems];
        for (int i = 0; i < numberOfItems; i++) {
            x = (int) (centerXD + radiusD * Math.cos(Math.toRadians(currentAngle)));
            y = (int) (centerYD + radiusD * Math.sin(Math.toRadians(currentAngle)));
            result[i] = new Coordinates(x, y);
            currentAngle += incrValue;
        }
        return result;
    }

    public static Coordinates[] getChequerPattern(int numberOfItems, int initialX, int initialY, int minY, int maxY, int distance, int driftOffset) {
        Coordinates[] result = new Coordinates[numberOfItems];
        Coordinates last = null;
        int x, y;
        for (int i = 0; i < numberOfItems; i++) {
            if (last == null) {
                x = initialX;
                y = initialY;
            } else if (last.getY() == minY) {
                x = last.getX() + driftOffset;
                y = maxY;
            } else {
                x = last.getX() + distance;
                y = last.getY() - distance;
            }
            last = new Coordinates(x, y);
            result[i] = last;
        }
        return result;
    }

    public static Coordinates[] getChequerPattern(int numberOfItems) {
        return getChequerPattern(numberOfItems, 50, 100, 50, 250, 100, 0);
    }

    public static Coordinates[] getRandomPattern(int numberOfItems, int minX, int minY, int maxX, int maxY){
        Coordinates[] result = new Coordinates[numberOfItems];
        int x, y;
        for (int i = 0; i < numberOfItems; i++) {
            x=(int)(Math.random()*(maxX-minX))+minX;
            y=(int)(Math.random()*(maxY-minY))+minY;
//            System.out.println("new rnd "+x+" "+y);
            result[i] =new Coordinates(x, y);
        }
        return result;
    }
}
