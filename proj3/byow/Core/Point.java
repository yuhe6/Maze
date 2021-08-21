package byow.Core;

import java.io.Serializable;

/** The Point object represents a point in the cartesian plane with coordinates (x,y) */

public class Point implements Serializable {
    final  int x;
    final  int y;

    Point(int x, int y){
        this.x = x;
        this.y = y;
    }

    @Override
    public String toString(){
    String returnString = "(" + x + ", " + y + ")";
    return returnString;
    }

    @Override
    public boolean equals(Object o){
    if (o == null) {
        return false;
    } else if (getClass() == o.getClass()) {
        Point p = (Point) o;
        return p.x == x && p.y == y;
    }
    return false;
    }

}