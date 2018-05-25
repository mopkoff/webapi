package lab9.Model;

import lab9.Model.User;

import javax.persistence.*;
import java.io.Serializable;

@Entity(name = "points")
public class PointEntity implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id ;
    private double x = 0;
    private double y = 0;
    private int isInside = 0;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "UserID")
    private User user;
    public PointEntity(){}

    public PointEntity(double x, double y) {
        this.x = x;
        this.y = y;
    }

    public PointEntity(double x, double y, double r) {
        this.x = x;
        this.y = y;
        this.check(r);
    }

    public PointEntity(double x, double y, double r, User user) {
        this.x = x;
        this.y = y;
        this.check(r);
        this.user = user;
    }

    public void check(double r) {
        r = r*4.3;
        if (x >= 0 && y >= 0 && x * x + y * y <= r * r / 4) {
            isInside = 1;
        }
        else if (x <= 0 && y <= 0 && 2 * x + y + r >= 0) {
            isInside = 1;
        }
        else if (x >= 0 && y <= 0 && x <= r && y >= -r) {
            isInside = 1;
        }
        else isInside = 0;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public User getUser(){return user;}

    public void setUser(User user){this.user = user;}

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
