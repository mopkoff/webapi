package lab9.Model;

import java.io.Serializable;

public class Point  implements Serializable {
    private double x = 0;
    private double y = 0;
    private int isInside = 0;

    public Point(double x, double y, int isInside) {
        this.x = x;
        this.y = y;
        this.isInside = isInside;
    }

    public Point(PointEntity pointEntity) {
        this.x = pointEntity.getX();
        this.y = pointEntity.getY();
        this.isInside = pointEntity.getIsInside();
    }
    public double getX() {
        return x;
    }

    public void setX(double x) {
        this.x = x;
    }

    public double getY() {
        return y;
    }

    public void setY(double y) {
        this.y = y;
    }

    public int getIsInside() {
        return isInside;
    }

    public void setIsInside(int isInside) {
        this.isInside = isInside;
    }

}
