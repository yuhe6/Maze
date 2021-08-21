package byow.Core;

import byow.TileEngine.TETile;
import byow.TileEngine.Tileset;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class BSPTree{
    private BSPLeaf root;
    private final int N_ITERATION;
    private Random rand;
    int greenCount = 0;
    int yellowCount = 0;
    int redCount = 0;
    int doorCount = 0;
    int lifeCount = 0;
    int roomCount = 0;

    /** default constructor
     * @param Map tiles
     */
    public BSPTree(TETile[][] Map){
        this.root = new BSPLeaf(new Point(0, 0), Map.length, Map[0].length);
        this.N_ITERATION = 8;
        this.rand = new Random(123);
    }

    /** constructor with arguments
     * @param n iteration
     * @param rand for randomness, user-specified
     * @param Map tiles
     */
    public BSPTree(int n, Random rand, TETile[][] Map){
        this.root = new BSPLeaf(new Point(0, 0), Map.length, Map[0].length);
        this.N_ITERATION = n;
        this.rand = rand;
    }

    /**
     * Generates the partitions of the map, and assigns them to the leaf nodes of the BSPTree
     * @param curr
     * @param iteration
     */
    private void generateMapHelper(BSPLeaf curr, int iteration){
        // base case
        if (iteration<1)
            return;
        if(curr == null){
            return;
        }
        curr.splitLeaf();
        generateMapHelper(curr.leftChild, iteration-1);
        generateMapHelper(curr.rightChild, iteration-1);
    }


    public void generateMap(TETile[][] Map){
        // split areas
        generateMapHelper(this.root, N_ITERATION);
        // generate rooms + add walls around
        root.createRooms(Map);
        // connect rooms / generate hallways + add walls along
        root.connectSpaces(Map);

    }


    private class BSPLeaf{
        public BSPLeaf leftChild;
        public BSPLeaf rightChild;
        public Point bottomLeft;
        int width;
        int height;
        Rectangle room;
        List<Rectangle> rooms = new ArrayList<>();
        static final double SPLIT_LOWER_RATIO = 0.4;
        static final double SPLIT_UPPER_RATIO = 0.6;
        static final int minLeafSize = 15; //Minimum size of a partition

        /** default constructor */
        private BSPLeaf(){
        }

        /** constructor with arguments */
        private BSPLeaf(Point bottomLeft, int width, int height){
            this.leftChild = null;
            this.rightChild = null;
            this.width = width;
            this.height = height;
            this.bottomLeft = bottomLeft;
        }

        /** helper function to randomly generate axis to separate rectangle */
        private boolean splitAxisHelper(){
            if (width == 1){
                return false;
            }else if (height == 1){
                return true;
            }else{
                return RandomUtils.bernoulli(rand);
            }
        }

        /**
         * @param length can either be height or width
         * @return a random integer index from uniform distribution
         */
        private int splitIndexHelper(int length){
            return (int) RandomUtils.uniform(rand, length*SPLIT_LOWER_RATIO, length*SPLIT_UPPER_RATIO);
        }

        /** add leftChild and rightChild to this*/
        public void splitLeaf(){
            if ((leftChild != null && rightChild != null) || width <= minLeafSize || height <= minLeafSize)
                return;
            boolean horizontal = splitAxisHelper();
            if (horizontal){
                int split = splitIndexHelper(width);
                leftChild = new BSPLeaf(bottomLeft, width-split, height);
                rightChild = new BSPLeaf(new Point(bottomLeft.x+width-split, bottomLeft.y), split, height);
            } else {
                int split = splitIndexHelper(height);
                leftChild = new BSPLeaf(bottomLeft, width, height-split);
                rightChild = new BSPLeaf(new Point(bottomLeft.x, bottomLeft.y+height-split), width, split);
            }
        }

        public Point bottomLeftRoomCorner(){
            int x = RandomUtils.uniform(rand, bottomLeft.x+1, bottomLeft.x+width/2);
            int y = RandomUtils.uniform(rand, bottomLeft.y+1, bottomLeft.y+height/2);
            return new Point(x, y);
        }

        public Point topRightRoomCorner(){
            int x = RandomUtils.uniform(rand, bottomLeft.x+width/2, bottomLeft.x+width-1);
            int y = RandomUtils.uniform(rand, bottomLeft.y+height/2, bottomLeft.y+height-1);
            return new Point(x, y);
        }

        public void createRooms(TETile[][] Map){
            if (leftChild != null && rightChild != null){
                leftChild.createRooms(Map);
                rightChild.createRooms(Map);
                rooms.addAll(leftChild.rooms);
                rooms.addAll(rightChild.rooms);
                return;
            }

            // base case
            room = new Rectangle(bottomLeftRoomCorner(), topRightRoomCorner());
            room.fill(Map, Tileset.FLOOR);
            roomCount ++;

            greenCount ++;
            room.possiblyAddObject(Map, rand, Tileset.GREEN_VACCINE, 1.0/greenCount);
            room.possiblyAddObject(Map, rand, Tileset.GREEN_PATIENT, 1.0/greenCount);
            yellowCount ++;
            room.possiblyAddObject(Map, rand, Tileset.YELLOW_VACCINE, 0.8/yellowCount);
            room.possiblyAddObject(Map, rand, Tileset.YELLOW_PATIENT, 0.8/yellowCount);
            redCount ++;
            room.possiblyAddObject(Map, rand, Tileset.RED_VACCINE, 0.5/redCount);
            room.possiblyAddObject(Map, rand, Tileset.RED_PATIENT, 0.5/redCount);
            lifeCount ++;
            room.possiblyAddObject(Map, rand, Tileset.LIFE, 0.7/lifeCount);

            // Engine.printTitles(Map);
            if (roomCount == 4){
                room.addAroundWithoutOverWrite(Map, Tileset.WALL, rand, Tileset.LOCKED_DOOR);
            }else{
                room.addAroundWithoutOverWrite(Map, Tileset.WALL);
            }

            if (roomCount == 2){
                room.possiblyAddObject(Map, rand, Tileset.PERMISSION, 1);
            }
            // Engine.printTitles(Map);
            rooms.add(room);
        }


        /**
         * This function needs to be fixed. Should connect all the of the leaf nodes
         * @param Map
         */
        public void connectSpaces(TETile[][] Map){
            if (leftChild.leftChild == null && rightChild.leftChild == null){
                connectRooms(Map, leftChild.room, rightChild.room);
            }else if (leftChild.leftChild == null){
                rightChild.connectSpaces(Map);
                int idx = RandomUtils.uniform(rand, rightChild.rooms.size());
                connectRooms(Map, leftChild.room, rightChild.rooms.get(idx));
            }else if (rightChild.leftChild == null){
                leftChild.connectSpaces(Map);
                int idx = RandomUtils.uniform(rand, leftChild.rooms.size());
                connectRooms(Map, leftChild.rooms.get(idx),rightChild.room);
            }else{
                leftChild.connectSpaces(Map);
                rightChild.connectSpaces(Map);
                int idx = RandomUtils.uniform(rand, leftChild.rooms.size());
                int idx2 = RandomUtils.uniform(rand, rightChild.rooms.size());
                connectRooms(Map, leftChild.rooms.get(idx), rightChild.rooms.get(idx2));
            }
            // Engine.printTitles(Map);
        }

        /**
         * Connects room a and b
         */
        private void connectRooms(TETile[][] Map, Rectangle a, Rectangle b){
            //Chooses a random point in the interior of both rooms
            Point inRoomA = new Point(RandomUtils.uniform(rand,a.bottomLeft.x, a.topRight.x),
                    RandomUtils.uniform(rand,a.bottomLeft.y, a.topRight.y));
            Point inRoomB = new Point(RandomUtils.uniform(rand,b.bottomLeft.x, b.topRight.x),
                    RandomUtils.uniform(rand,b.bottomLeft.y, b.topRight.y));
            //Straight path
            Rectangle path, path2;
            if(inRoomA.x == inRoomB.x || inRoomA.y == inRoomB.y){
                path = new Rectangle(inRoomB, inRoomA);
                path.fill(Map, Tileset.FLOOR);
                path.addAroundWithoutOverWrite(Map, Tileset.WALL);
            }else{
                path = new Rectangle(new Point(inRoomA.x, inRoomB.y), inRoomA);
                path2 = new Rectangle(new Point(inRoomA.x, inRoomB.y), inRoomB);
                path.fill(Map,Tileset.FLOOR);
                path.addAroundWithoutOverWrite(Map,Tileset.WALL);
                path2.fill(Map,Tileset.FLOOR);
                path2.addAroundWithoutOverWrite(Map,Tileset.WALL);
            }
        }

    }
}
