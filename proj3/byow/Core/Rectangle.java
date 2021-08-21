package byow.Core;

import byow.TileEngine.TETile;
import byow.TileEngine.Tileset;

import java.util.LinkedList;
import java.util.Random;

public class Rectangle {
    public int width;
    public int height;
    public Point bottomLeft;
    public Point topRight;

    public Rectangle(){

    }

    public Rectangle(Point corner, Point corner2){
        height = Math.abs(corner.y-corner2.y);
        width = Math.abs(corner.x - corner2.x);
        topRight = new Point(Math.max(corner.x, corner2.x),
                             Math.max(corner.y, corner2.y));
        bottomLeft = new Point(Math.min(corner.x, corner2.x),
                               Math.min(corner.y, corner2.y));

    }

    public Rectangle(Point bottomLeft, int width, int height){
        this.width = width;
        this.height = height;
        this.bottomLeft = bottomLeft;
        this.topRight = new Point(bottomLeft.x+width-1, bottomLeft.y+height-1);
    }

    public void fill(TETile[][] tiles, TETile tile){
        for (int i = bottomLeft.x; i <= topRight.x; i++){
            for (int j = bottomLeft.y; j<= topRight.y; j++){
                // otherwise it will overwrite other tiles, including key and locked door
                if (tiles[i][j].equals(Tileset.NOTHING) || tiles[i][j].equals(Tileset.WALL)){
                    tiles[i][j] = tile;
                }
            }
        }
    }

    public boolean possiblyAddObject(TETile[][] tiles, Random rand, TETile tile, double p){
        boolean add = RandomUtils.bernoulli(rand, p);
        if (add){
            int x = RandomUtils.uniform(rand, bottomLeft.x, topRight.x+1);
            int y = RandomUtils.uniform(rand, bottomLeft.y, topRight.y+1);
            tiles[x][y] = tile;
        }
        return add;
    }

    public static boolean withinMap(int x, int y){
        return (x>=0 && x< Engine.WIDTH && y>=0 && y< Engine.HEIGHT);
    }

    /**
     * Add a certain type of tiles around this rectangle object on tiles/map
     */
    public void addAround(TETile[][] tiles, TETile tile){
        // add walls at top and bottom
        for (int i=bottomLeft.x; i<= topRight.x; i++){
            if (withinMap(i, bottomLeft.y-1))
                tiles[i][bottomLeft.y-1] = tile;
            if (withinMap(i, topRight.y+1))
                tiles[i][topRight.y+1] = tile;
        }

        // add walls on left and on right
        for (int i = bottomLeft.y; i<=topRight.y; i++){
            if (withinMap(bottomLeft.x, i))
                tiles[bottomLeft.x][i] = tile;
            if (withinMap(topRight.x, i))
                tiles[topRight.x][i] = tile;
        }
    }

    /**
     * Add a certain type of tiles around this rectangle object on tiles/map
     * without overwriting any existing tile
     *
     * One application of this is to add walls along hallways, and this method ensures
     * that no room tiles will be overwritten
     */
    public void addAroundWithoutOverWrite(TETile[][] tiles, TETile tile){
        // add walls at top and bottom
        for (int i=bottomLeft.x-1; i<= topRight.x+1; i++){
            if (withinMap(i, bottomLeft.y-1) && tiles[i][bottomLeft.y-1].equals(Tileset.NOTHING))
                tiles[i][bottomLeft.y-1] = tile;
            if (withinMap(i, topRight.y+1) && tiles[i][topRight.y+1].equals(Tileset.NOTHING))
                tiles[i][topRight.y+1] = tile;
        }

        // add walls on left and on right
        for (int i= bottomLeft.y; i<=topRight.y; i++){
            if (withinMap(bottomLeft.x-1, i) && tiles[bottomLeft.x-1][i].equals(Tileset.NOTHING))
                tiles[bottomLeft.x-1][i] = tile;
            if (withinMap(topRight.x+1, i) && tiles[topRight.x+1][i].equals(Tileset.NOTHING))
                tiles[topRight.x+1][i] = tile;
        }
    }

    public void addAroundWithoutOverWrite(TETile[][] tiles, TETile tile, Random rand, TETile doorTile){
        LinkedList<Point> walls = new LinkedList<>();

        // add walls at top and bottom
        for (int i=bottomLeft.x-1; i<= topRight.x+1; i++){
            if (withinMap(i, bottomLeft.y-1) && tiles[i][bottomLeft.y-1].equals(Tileset.NOTHING)){
                tiles[i][bottomLeft.y-1] = tile;
                walls.add(new Point(i, bottomLeft.y-1));
            }
            if (withinMap(i, topRight.y+1) && tiles[i][topRight.y+1].equals(Tileset.NOTHING)){
                tiles[i][topRight.y+1] = tile;
                walls.add(new Point(i, topRight.y+1));
            }
        }

        // avoid adding a locked door in any of the wall corner
        walls.pollFirst();
        walls.pollFirst();
        walls.pollLast();
        walls.pollLast();

        // add walls on left and on right
        for (int i= bottomLeft.y; i<=topRight.y; i++){
            if (withinMap(bottomLeft.x-1, i) && tiles[bottomLeft.x-1][i].equals(Tileset.NOTHING)){
                tiles[bottomLeft.x-1][i] = tile;
                walls.add(new Point(bottomLeft.x-1, i));
            }
            if (withinMap(topRight.x+1, i) && tiles[topRight.x+1][i].equals(Tileset.NOTHING)){
                tiles[topRight.x+1][i] = tile;
                walls.add(new Point(topRight.x+1, i));
            }
        }

        int idx = RandomUtils.uniform(rand, walls.size());
        Point p = walls.get(idx);
        tiles[p.x][p.y] = doorTile;
    }
}
