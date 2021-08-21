package byow.TileEngine;

import java.awt.*;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

/**
 * Contains constant tile objects, to avoid having to remake the same tiles in different parts of
 * the code.
 *
 * You are free to (and encouraged to) create and add your own tiles to this file. This file will
 * be turned in with the rest of your code.
 *
 * Ex:
 *      world[x][y] = Tileset.FLOOR;
 *
 * The style checker may crash when you try to style check this file due to use of unicode
 * characters. This is OK.
 */

public class Tileset {
    public static final TETile AVATAR = new TETile('@', new Color(0, 255, 225), Color.black, "AVATAR");
    public static final TETile KNIGHT = new TETile('@', new Color(255, 140, 0), Color.black, "KNIGHT");
    public static final TETile MORGANA = new TETile('@', new Color(255, 62, 150), Color.black, "MORGANA");
    public static final TETile ASSASSIN = new TETile('@', new Color(86, 241, 81), Color.black, "ASSASSIN");
    public static final TETile MERLIN = new TETile('@', new Color(40, 197, 255), Color.black, "MERLIN");
    public static final TETile[] ROLES = {AVATAR, KNIGHT, MORGANA, ASSASSIN, MERLIN};

    public static final TETile WALL = new TETile('#', new Color(216, 128, 128), Color.darkGray,
            "wall");
    public static final TETile FLOOR = new TETile('·', new Color(128, 192, 128), Color.black,
            "floor");
    public static final TETile NOTHING = new TETile(' ', Color.black, Color.black, "nothing");
    public static final TETile GRASS = new TETile('"', Color.green, Color.black, "grass");
    public static final TETile WATER = new TETile('≈', Color.blue, Color.black, "water");
    public static final TETile FLOWER = new TETile('❀', Color.magenta, Color.pink, "flower");
    public static final TETile LOCKED_DOOR = new TETile('▶', Color.black, Color.orange,
            "locked door");
    public static final TETile PERMISSION = new TETile('▶', Color.orange, Color.black,
            "permission");
    public static final TETile UNLOCKED_DOOR = new TETile('▢', Color.orange, Color.black,
            "unlocked door");
    public static final TETile SAND = new TETile('▒', Color.yellow, Color.black, "sand");
    public static final TETile MOUNTAIN = new TETile('▲', Color.gray, Color.black, "mountain");
    public static final TETile TREE = new TETile('♠', Color.green, Color.black, "tree");
    public static final TETile GREEN_VACCINE = new TETile('♦', new Color(94, 255, 58), Color.black, "green vaccine");
    public static final TETile GREEN_PATIENT = new TETile('▦', new Color(94, 255, 58), Color.black, "green patient");
    public static final TETile YELLOW_VACCINE = new TETile('♦', new Color(255, 245, 58), Color.black, "yellow vaccine");
    public static final TETile YELLOW_PATIENT = new TETile('▦', new Color(255, 245, 58), Color.black, "yellow patient");
    public static final TETile RED_VACCINE = new TETile('♦', new Color(255, 11, 11), Color.black, "red vaccine");
    public static final TETile RED_PATIENT = new TETile('▦', new Color(255, 11, 11), Color.black, "red patient");
    public static final TETile LIFE = new TETile('✚', Color.red, Color.white, "Life");
    public static final Set<TETile> vaccineTiles = new HashSet<>(Arrays.asList(GREEN_VACCINE, RED_VACCINE, YELLOW_VACCINE));
    public static final Set<TETile> patientsTiles =  new HashSet<>(Arrays.asList(GREEN_PATIENT, RED_PATIENT, YELLOW_PATIENT));
}


