package byow.Core;

import byow.InputDemo.InputSource;
import byow.InputDemo.KeyboardInputSource;
import byow.InputDemo.StringInputDevice;
import byow.TileEngine.TERenderer;
import byow.TileEngine.TETile;
import byow.TileEngine.Tileset;
import edu.princeton.cs.introcs.StdDraw;

import java.awt.*;
import java.io.File;
import java.util.Random;

public class Engine {
    TERenderer ter = new TERenderer();
    /* Feel free to change the width and height. */
    public static final File CWD = new File(System.getProperty("user.dir"));
    public static final int WIDTH = 80;
    public static final int HEIGHT = 40;
    public int BSP_ITERATION = 8;  // complexity of a map
    Font fontBig = new Font("Monaco", Font.BOLD, 45);  // 48
    Font fontMedium = new Font("Monaco", Font.BOLD, 30); // 32
    Font fontMediumSmall = new Font("Monaco", Font.BOLD, 22);
    Font fontSmall = new Font("Monaco", Font.BOLD, 15); // 16
    private Random rand;
    long seed;
    Avatar character;
    int upperFrameHeight = 3;
    boolean hideView = false;
    int level = 1;    // mainly used for output and generating parameter for different levels
    int timeLimit = 120;
    Records records;

    public void StdDrawConfiguration() {
        StdDraw.setCanvasSize(WIDTH * 16, HEIGHT * 16);
        StdDraw.setXscale(0, WIDTH);
        StdDraw.setYscale(0, HEIGHT);
    }

    public Engine() {
    }

    public Engine(int n) {
        this.BSP_ITERATION = n;
    }

    // the relationship between level and map complexity
    public int getNewBSP_ITERATION(){
        return level + 7;
    }

    public static void printTitles(TETile[][] tiles) {
        int width = tiles.length;
        int height = tiles[0].length;
        for (int i = height - 1; i >= 0; i--) {
            for (int j = 0; j < width; j++) {
                System.out.print(tiles[j][i].character());
            }
            System.out.println();
        }
    }

    public void fillWithNothing(TETile[][] map) {
        for (int i = 0; i < WIDTH; i++) {
            for (int j = 0; j < HEIGHT; j++) {
                map[i][j] = Tileset.NOTHING;
            }
        }
    }

    public void drawFrame(String s) {
        /* Take the input string S and display it at the center of the screen,
         * with the pen settings given below. */
        StdDraw.clear(Color.BLACK);
        StdDraw.setPenColor(Color.WHITE);
        StdDraw.setFont(fontMedium);
        StdDraw.text(WIDTH * 0.5, HEIGHT * 0.6, s);
        StdDraw.show();
        StdDraw.setFont(fontSmall);
    }

    public void displayMenu() {
        StdDraw.clear(Color.BLACK);
        StdDraw.setPenColor(Color.WHITE);

        StdDraw.setFont(fontBig);
        StdDraw.text(WIDTH * 0.5, HEIGHT * 0.75, "CS61B: THE GAME");

        StdDraw.setFont(fontMedium);
        StdDraw.text(WIDTH * 0.5, HEIGHT * 0.6, "New Game (N)");
        StdDraw.text(WIDTH * 0.5, HEIGHT * 0.6 - 2, "Select Role (R)");
        StdDraw.text(WIDTH * 0.5, HEIGHT * 0.6 - 4, "Load Game (L)");
        StdDraw.text(WIDTH * 0.5, HEIGHT * 0.6 - 6, "Guide (G)");
        StdDraw.text(WIDTH * 0.5, HEIGHT * 0.6 - 8, "Quit (Q)");
        StdDraw.show();
        StdDraw.setFont(fontSmall);
    }

    public void displayRoleMenu(){
        StdDraw.clear(Color.BLACK);
        StdDraw.setPenColor(Color.WHITE);
        StdDraw.setFont(fontMedium);
        StdDraw.text(WIDTH * 0.5, HEIGHT * 0.7, "Type the number of roles to select");

        double height = HEIGHT * 0.6;
        int count = 1;
        StdDraw.setFont(fontMediumSmall);
        for (TETile role : Tileset.ROLES){
            role.draw(WIDTH * 0.5 - 2.2, height-0.5);
            StdDraw.setPenColor(Color.WHITE);
            StdDraw.text(WIDTH * 0.5 - 4, height, count + "." );
            StdDraw.text(WIDTH * 0.5 + 3, height, role.description());
            height -= 2;
            count += 1;
        }

        StdDraw.show();
        StdDraw.setFont(fontSmall);
    }

    public void displayRecords(){
        int recordsToBeDisplayed = Math.min(10, records.localRecords.size());
        int i = 0;
        StdDraw.setFont(fontMediumSmall);
        StdDraw.setPenColor(Color.white);
        StdDraw.text(WIDTH * 0.5, HEIGHT * 0.6,  "Rank       Name      Score           Seed");
        StdDraw.setPenColor(Color.LIGHT_GRAY);
        while (i < recordsToBeDisplayed){
            StdDraw.text(WIDTH * 0.5-15, HEIGHT * 0.55-2*i, Integer.toString(i+1));
            StdDraw.text(WIDTH * 0.5-6, HEIGHT * 0.55-2*i, records.getName(i));
            StdDraw.text(WIDTH * 0.5+2.5, HEIGHT * 0.55-2*i, Integer.toString(records.getScore(i)));
            StdDraw.text(WIDTH * 0.5+15, HEIGHT * 0.55-2*i, Long.toString(records.getSeed(i)));
            i ++;
        }
    }

     public void dispalyGameOver(String reason){
         StdDraw.clear(Color.BLACK);
         StdDraw.setPenColor(Color.red);
         StdDraw.setFont(fontBig);
         StdDraw.text(WIDTH * 0.5, HEIGHT * 0.9, "GAME OVER");

         StdDraw.setPenColor(Color.green);
         StdDraw.setFont(fontMedium);
         StdDraw.text(WIDTH * 0.5, HEIGHT * 0.8, reason);
         StdDraw.setPenColor(Color.yellow);
         StdDraw.text(WIDTH * 0.5, HEIGHT * 0.7, "Your final score: " + character.score);
         displayRecords();

         StdDraw.show();
         // StdDraw.pause(50000);
     }

    public void displaySecondGuide(){
        StdDraw.clear(Color.BLACK);
        StdDraw.setPenColor(Color.WHITE);
        StdDraw.setFont(fontMediumSmall);
        //StdDraw.text(WIDTH * 0.5, HEIGHT, "Be careful!");

        // TODO: Display rules
        StdDraw.setFont(fontSmall);
        double step = 1.5;
        double start = 0.95;

        StdDraw.setPenColor(Color.WHITE);
        StdDraw.setFont(fontMediumSmall);
        StdDraw.text(WIDTH * 0.5, HEIGHT*start-step*5, "Be careful!");
        StdDraw.setFont(fontSmall);
        StdDraw.setPenColor(Color.LIGHT_GRAY);

        StdDraw.text(WIDTH*0.5, HEIGHT*start-step*8, "Though you are vaccinated against the disease, you aren't completely immune.");
        StdDraw.text(WIDTH*0.5, HEIGHT*start-step*9, "Being in an area with such a high concentration of the virus in the air occasionally causes your vision to blur.");
        StdDraw.text(WIDTH*0.5, HEIGHT*start-step*10, "Periodically your field of vision will shrink, till you can barely see your hand in front of you.");
        StdDraw.setPenColor(Color.pink);
        StdDraw.text(WIDTH*0.5, HEIGHT*start-step*11, "You should be careful and slow your pace.");
        StdDraw.text(WIDTH*0.5, HEIGHT*start-step*12, "Otherwise you may crash with some patient and if you don't have vaccine for the patient.");
        StdDraw.text(WIDTH*0.5, HEIGHT*start-step*13, "YOU MAY DIE...");

        StdDraw.setPenColor(Color.WHITE);
        StdDraw.text(WIDTH * 0.5, HEIGHT*0.07, "Type M to go back to the main menu");
        StdDraw.text(WIDTH * 0.5, HEIGHT*0.03, "Type G to go back to the previous page");

        StdDraw.show();
        StdDraw.setFont(fontSmall);
    }

    public void displayFirstGuide(){
        StdDraw.clear(Color.BLACK);
        StdDraw.setPenColor(Color.WHITE);
        StdDraw.setFont(fontMediumSmall);
        StdDraw.text(WIDTH * 0.5, HEIGHT, "Background & Instructions");

        // TODO: Display rules
        StdDraw.setFont(fontSmall);
        double step = 1.5;
        double start = 0.95;
        StdDraw.setPenColor(Color.LIGHT_GRAY);
        StdDraw.text(WIDTH*0.5, HEIGHT*start-step*0, "The year is 2023. 6 months ago the eta-virus was discovered. It was like nothing that had been seen before.");
        StdDraw.text(WIDTH*0.5, HEIGHT*start-step*1, "Deadly and highly contagious, 80% of the world population was infected within a month.");
        StdDraw.text(WIDTH*0.5, HEIGHT*start-step*2, "You are part of a special unit assigned to cities where there are still hopes of saving people.");
        StdDraw.text(WIDTH*0.5, HEIGHT*start-step*3, "You start with 5 health. Health kits are scattered around the map, picking one up will increase your health to a maximum of 9");
        StdDraw.setPenColor(Color.green);
        StdDraw.text(WIDTH*0.5, HEIGHT*start-step*4, "heart: " + "♥ ".repeat(5));

        StdDraw.setPenColor(Color.LIGHT_GRAY);
        StdDraw.text(WIDTH*0.5, HEIGHT*start-step*5, "Your goal is to save as many patients as possible.");
        StdDraw.text(WIDTH*0.5, HEIGHT*start-step*6, "There are three types of patients all in square--green, yellow and red.");
        Tileset.GREEN_PATIENT.draw(WIDTH*0.5-5, HEIGHT*start-step*7-0.3);
        Tileset.YELLOW_PATIENT.draw(WIDTH*0.5, HEIGHT*start-step*7-0.3);
        Tileset.RED_PATIENT.draw(WIDTH*0.5+5, HEIGHT*start-step*7-0.3);

        StdDraw.setPenColor(Color.LIGHT_GRAY);
        StdDraw.text(WIDTH*0.5, HEIGHT*start-step*8, "There are also three types of vaccines all in diamonds--green, yellow and red.");
        Tileset.GREEN_VACCINE.draw(WIDTH*0.5-5, HEIGHT*start-step*9-0.3);
        Tileset.YELLOW_VACCINE.draw(WIDTH*0.5, HEIGHT*start-step*9-0.3);
        Tileset.RED_VACCINE.draw(WIDTH*0.5+5, HEIGHT*start-step*9-0.3);

        StdDraw.setPenColor(Color.LIGHT_GRAY);
        StdDraw.text(WIDTH*0.5, HEIGHT*start-step*10, "You should collect vaccines on the way and cure patients with correct vaccines.");
        StdDraw.text(WIDTH*0.5, HEIGHT*start-step*11, "Note that number of vaccines and patients might not be consistent.");
        StdDraw.text(WIDTH*0.5, HEIGHT*start-step*12, "If vaccines are more than patients, these vaccines can be saved to the next level");
        StdDraw.text(WIDTH*0.5, HEIGHT*start-step*13, "If patients are more than vaccines, you will have to give these patients up");
        StdDraw.setPenColor(Color.pink);
        StdDraw.text(WIDTH*0.5, HEIGHT*start-step*14, "Curing a green patient increase your score by 1, yellow by 2, and red by 3");
        StdDraw.text(WIDTH*0.5, HEIGHT*start-step*15, "However, if you don't have the correct vaccine for the patient that you encounter,");
        StdDraw.text(WIDTH*0.5, HEIGHT*start-step*16, "you will lose lives, green for 1, yellow for 2, and red for 3.");

        StdDraw.setPenColor(Color.LIGHT_GRAY);
        StdDraw.text(WIDTH*0.5, HEIGHT*start-step*17, "There is a locked door on each level. You need to go through the door to get to the next level.");
        StdDraw.text(WIDTH*0.5, HEIGHT*start-step*18, "A count down will be triggered once you enter a region.");
        StdDraw.setPenColor(Color.pink);
        StdDraw.text(WIDTH*0.5, HEIGHT*start-step*19, "If you run out of time but do not get to the next level, you'll lose the game.");

        StdDraw.setPenColor(Color.LIGHT_GRAY);
        StdDraw.text(WIDTH*0.5, HEIGHT*start-step*20, "In order to get through the locked door, you need to collect a triangle key to be verified.");
        StdDraw.text(WIDTH*0.5, HEIGHT*start-step*21, "Key     Locked door");
        Tileset.PERMISSION.draw(WIDTH*0.5-3, HEIGHT*start-step*21-0.3);
        Tileset.LOCKED_DOOR.draw(WIDTH*0.5+6, HEIGHT*start-step*21-0.3);
        StdDraw.setPenColor(Color.LIGHT_GRAY);
        StdDraw.text(WIDTH*0.5, HEIGHT*start-step*22, "The verification status will be shown on the top of the screen as either            or        ");
        StdDraw.setPenColor(Color.RED);
        StdDraw.text(WIDTH*0.5+17.6, HEIGHT*start-step*22, "unverified");
        StdDraw.setPenColor(Color.GREEN);
        StdDraw.text(WIDTH*0.5+24.5, HEIGHT*start-step*22, "verified");

        StdDraw.setPenColor(Color.WHITE);
        StdDraw.text(WIDTH * 0.5, HEIGHT*0.07, "Type M to go back to the main menu");
        StdDraw.text(WIDTH * 0.5, HEIGHT*0.03, "Type L to go to the next page");

        StdDraw.show();
        StdDraw.setFont(fontSmall);
    }

    public void goBackToMenuOrContinue(InputSource source){
        char action = source.getNextKey();
        while (!(action=='M')){
            if (action == 'L'){
                displaySecondGuide();
            }else if (action == 'G'){
                displayFirstGuide();
            }
            action = source.getNextKey();
        }
        displayMenu();
    }

    public void setPlayerAppearance(InputSource source){
        char action = source.getNextKey();
        int upperBound = Tileset.ROLES.length;
        while (!Character.isDigit(action) || (Character.getNumericValue(action) > upperBound)){
            action = source.getNextKey();
        }
        //
        character = new Avatar(Tileset.ROLES[Character.getNumericValue(action)-1]);

        StdDraw.setFont(fontMedium);
        StdDraw.clear(Color.BLACK);
        StdDraw.setPenColor(Color.WHITE);
        StdDraw.text(WIDTH*0.5-1, HEIGHT*0.7,"You have selected " + character.avatarTile.description());
        character.avatarTile.draw(WIDTH*0.5 + 14, HEIGHT*0.7-0.5);
        StdDraw.setPenColor(Color.WHITE);
        StdDraw.text(WIDTH*0.5, HEIGHT*0.6, "Returning to the main menu");
        StdDraw.show();
        StdDraw.setFont(fontSmall);
        StdDraw.pause(2000);
    }

    public Point getMouse(){
        return new Point((int) StdDraw.mouseX(), (int) StdDraw.mouseY());
    }

    /**
     * provides additional information that maybe useful to the user.
     * At the bare minimum, this should include Text that describes the tile currently under the mouse pointer.
     * extra: optional information
     */
    public void HUD(TETile[][] tiles) {
        /* Take the input string S and display it at the center of the screen,
         * with the pen settings given below. */
        Point mouse = getMouse();
        String description = "";
        if (mouse.y < tiles[0].length){
            description = tiles[mouse.x][mouse.y].description();
            if (tiles[mouse.x][mouse.y].equals(character.avatarTile) && (character.name != null)){
                description = description + "(" + character.name + ")";
            }
        }
        StdDraw.setPenColor(Color.WHITE);
        StdDraw.text(5, HEIGHT+Math.ceil(upperFrameHeight*0.5), description);
    }

    /**
     * After the user has pressed the final number in their seed, they should press S to tell the system
     * that they’ve entered the entire seed that they want. Your world generator should be able to handle any
     * positive seed up to 9,223,372,036,854,775,807.
     *
     * @return seed
     */
    public long getRandomSeedFromUser() {
        drawFrame("Please enter a seed and press S to finish");
        String seed = "";
        InputSource userInput = new KeyboardInputSource();
        char s = userInput.getNextKey();
        while (s != 'S') {
            seed += Character.toString(s);
            drawFrame(seed);
            s = userInput.getNextKey();
        }
        return Long.parseLong(seed);
    }

    public String getUserName() {
        drawFrame("Please enter your user name and press 1 to finish");
        String name = "";
        InputSource userInput = new KeyboardInputSource();
        char s = userInput.getNextKey();
        while (s != '1') {
            name += Character.toString(s);
            drawFrame(name);
            s = userInput.getNextKey();
        }
        return name;
    }

    public TETile[][] loadLastGame() {
        TETile[][] tiles = Utils.readObject(Utils.join(CWD, "Map.txt"), TETile[][].class);
        this.rand = Utils.readObject(Utils.join(CWD, "rand.txt"), Random.class);
        this.character = Utils.readObject(Utils.join(CWD,"character.txt"), Avatar.class);
        return tiles;
    }

    public void updateRecords(){
        File localRecordsFile = Utils.join(CWD, "localRecords.txt");
        if (localRecordsFile.exists())
            this.records = Utils.readObject(localRecordsFile, Records.class);
        else
            this.records = new Records();
        this.records.add(character.score, character.name, character.seed);
        Utils.writeObject(Utils.join(CWD, "localRecords.txt"), this.records);
    }

    public void saveGame(TETile[][] tiles) {
        Utils.writeObject(Utils.join(CWD, "Map.txt"), tiles);       //Map
        Utils.writeObject(Utils.join(CWD, "rand.txt"), rand);       //Rand
        Utils.writeObject(Utils.join(CWD, "character.txt"), this.character); //Player character
    }


    public void displayHiddenView(TETile[][] tiles, int visibleDistance) {
        TETile[][] tilesToBeDisplayed = new TETile[WIDTH][HEIGHT];
        fillWithNothing(tilesToBeDisplayed);
        for (int x = 0; x < WIDTH; x+=1){
            for (int y = 0; y < HEIGHT; y += 1){
                int disX = Math.abs(x-character.location.x);
                int disY = Math.abs(y-character.location.y);
                if (disX<visibleDistance && disY<visibleDistance && (disY+disX)<visibleDistance) {
                    tilesToBeDisplayed[x][y] = tiles[x][y];
                }
            }
        }
        ter.renderFrameNotShow(tilesToBeDisplayed);
    }

    public void displayLevelUp(){
        StdDraw.setFont(fontMedium);
        StdDraw.clear(Color.BLACK);
        StdDraw.setPenColor(Color.YELLOW);
        StdDraw.text(WIDTH*0.5, HEIGHT*0.7,"Big Congrats!");
        StdDraw.text(WIDTH*0.5, HEIGHT*0.6, "You have passed level " + this.level);
        StdDraw.show();
        StdDraw.setFont(fontSmall);
        StdDraw.pause(2000);
    }


    public void displayAll(){
        StdDraw.setPenColor(Color.green);
        StdDraw.text(WIDTH-30, HEIGHT+Math.ceil(upperFrameHeight*0.5),
                "left: " + character.greenVaccine);

        StdDraw.setPenColor(Color.yellow);
        StdDraw.text(WIDTH-25, HEIGHT+Math.ceil(upperFrameHeight*0.5),
                "left: " + character.yellowVaccine);

        StdDraw.setPenColor(Color.red);
        StdDraw.text(WIDTH-20, HEIGHT+Math.ceil(upperFrameHeight*0.5),
                "left: " + character.redVaccine);

        StdDraw.setPenColor(Color.green);
        StdDraw.text(WIDTH-10, HEIGHT+Math.ceil(upperFrameHeight*0.5),
                "heart: " + "♥ ".repeat(character.health));

        StdDraw.setPenColor(Color.white);
        StdDraw.text(WIDTH-35, HEIGHT+Math.ceil(upperFrameHeight*0.5),
                "score: " + character.score);

        if (character.verified)
            StdDraw.setPenColor(Color.green);
        else
            StdDraw.setPenColor(Color.red);
        StdDraw.text(WIDTH-41, HEIGHT+Math.ceil(upperFrameHeight*0.5),
                character.verifyStatusForOutput());

        StdDraw.setPenColor(Color.white);
        //StdDraw.text(WIDTH-50, HEIGHT+Math.ceil(upperFrameHeight*0.5),
        //        "Seconds left: " + (timeLimit - character.getSecondUsed()));

        StdDraw.text(WIDTH-50, HEIGHT+Math.ceil(upperFrameHeight*0.5),
                "Seconds left: " + character.timeLeft);
    }

    public void gameplayForUser(InputSource source, TETile[][] tiles) {
        char last, curr = 'x';
        Point lastMouse = getMouse();
            while(true){
                Point currMouse = getMouse();
                // if the user has typed sth
                if (currMouse.equals(lastMouse) && StdDraw.hasNextKeyTyped()){
                    last = curr;
                    curr = source.getNextKey();
                    if (curr == 'Q' && last == ':') {
                        saveGame(tiles);
                        System.exit(0);
                        return;
                    }
                    if (curr == 'H'){
                        this.hideView = !this.hideView;
                    }

                    boolean leveUp = moveAvatar(tiles, curr);

                    // check if the user has passed current level
                    if (leveUp){
                        displayLevelUp();
                        this.level ++;
                        this.BSP_ITERATION = getNewBSP_ITERATION();
                        tiles = createNewWorld();
                        ter.renderFrame(tiles);
                        timeLimit = Math.max(timeLimit-10, 30);
                        character.resetForNextLevel(timeLimit);
                    }
                    // check if the game is over
                    if (character.health<=0) {
                        updateRecords();
                        dispalyGameOver("You used up your lives");
                        while (true){
                            if (StdDraw.hasNextKeyTyped()){
                                last = curr;
                                curr = source.getNextKey();
                                if (curr == 'Q' && last == ':'){
                                    return;
                                }
                            }
                        }
                    }
                }

                character.autoSetTime();
                if (this.hideView || character.fogTime >= 0){
                    if (character.fogTime >= 0){
                        int dis = Math.abs(5 - (int) character.fogTime);
                        displayHiddenView(tiles, Math.max(2, dis));
                    }else{
                        displayHiddenView(tiles, 6);
                    }
                }else{
                    ter.renderFrameNotShow(tiles);
                }

                if (character.timeLeft <= 0){
                    updateRecords();
                    dispalyGameOver("You ran out of time");
                    while (true){
                        if (StdDraw.hasNextKeyTyped()){
                            last = curr;
                            curr = source.getNextKey();
                            if (curr == 'Q' && last == ':'){
                                return;
                            }
                        }
                    }
                }
                HUD(tiles);
                displayAll();
                StdDraw.show();
                lastMouse = currMouse;
        }
    }

    public void gameplayForAG(InputSource source, TETile[][] tiles) {
        char last, curr = 'x';
        while(source.possibleNextInput()){
                last = curr;
                curr = source.getNextKey();
                if (curr == 'Q' && last == ':') {
                    saveGame(tiles);
                    return;
                }
                moveAvatar(tiles, curr);
        }
    }

    public boolean moveAvatarHelper(int x, int y, TETile[][] tiles){
        if (tiles[x][y].equals(Tileset.FLOOR)) {
            tiles[x][y] = character.avatarTile;
            tiles[character.location.x][character.location.y] = Tileset.FLOOR;
            character.location = new Point(x, y);
        } else if (Tileset.vaccineTiles.contains(tiles[x][y])){
            character.addVaccine(tiles[x][y]);
            tiles[x][y] = character.avatarTile;
            tiles[character.location.x][character.location.y] = Tileset.FLOOR;
            character.location = new Point(x, y);
        } else if (Tileset.patientsTiles.contains(tiles[x][y]) && character.consumeVaccine(tiles[x][y])){
            tiles[x][y] = character.avatarTile;
            tiles[character.location.x][character.location.y] = Tileset.FLOOR;
            character.location = new Point(x, y);
        } else if (tiles[x][y].equals(Tileset.LIFE)){
            character.addHealth(1);
            tiles[x][y] = character.avatarTile;
            tiles[character.location.x][character.location.y] = Tileset.FLOOR;
            character.location = new Point(x, y);
        } else if (tiles[x][y].equals(Tileset.PERMISSION)){
            tiles[x][y] = character.avatarTile;
            tiles[character.location.x][character.location.y] = Tileset.FLOOR;
            character.location = new Point(x, y);
            character.verified = true;
        } else if (tiles[x][y].equals(Tileset.LOCKED_DOOR)){
            if (character.verified){
                tiles[x][y] = character.avatarTile;
                tiles[character.location.x][character.location.y] = Tileset.FLOOR;
                character.location = new Point(x, y);
                return true;
            }
        }
        return false;
    }

    public boolean moveAvatar(TETile[][] tiles, char curr) {
        boolean levelUp = false;
        switch (curr) {
            case 'W':
                levelUp = moveAvatarHelper(character.location.x, character.location.y + 1, tiles);
                break;
            case 'A':
                levelUp = moveAvatarHelper(character.location.x-1, character.location.y, tiles);
                break;
            case 'S':
                levelUp = moveAvatarHelper(character.location.x, character.location.y-1, tiles);
                break;
            case 'D':
                levelUp = moveAvatarHelper(character.location.x+1, character.location.y, tiles);
                break;
        }
        return levelUp;
    }


    public Point getEmptyPointWithinMap(TETile[][] tiles, TETile replace){
        int x = RandomUtils.uniform(rand, WIDTH);
        int y = RandomUtils.uniform(rand, HEIGHT);
        while (!tiles[x][y].equals(replace)) {
            x = RandomUtils.uniform(rand, WIDTH);
            y = RandomUtils.uniform(rand, HEIGHT);
        }
        return new Point(x, y);
    }

    public void addAvatar(TETile[][] tiles) {
        Point p = getEmptyPointWithinMap(tiles, Tileset.FLOOR);
        if(character == null){
            character = new Avatar(p);
        }else{
            character.location = p;
        }
        tiles[p.x][p.y] = character.avatarTile;
    }


    public TETile[][] createNewWorld(){
        TETile[][] tiles = new TETile[WIDTH][HEIGHT];
        fillWithNothing(tiles);
        BSPTree map = new BSPTree(BSP_ITERATION, rand, tiles);
        map.generateMap(tiles);
        addAvatar(tiles);
        return tiles;
    }

    /**
     * Method used for exploring a fresh world. This method should handle all inputs,
     * including inputs from the main menu.
     */
    public void interactWithKeyboard() {
        StdDrawConfiguration();
        ter.initialize(WIDTH, HEIGHT+upperFrameHeight);
        displayMenu();
        InputSource source = new KeyboardInputSource();
        while (true) {
            char action = source.getNextKey();
            //New Game
            if (action == 'N') {
                // get seed from user
                seed = getRandomSeedFromUser();
                this.rand = new Random(seed);
                TETile[][] tiles = createNewWorld();
                character.SetUserName(getUserName());
                character.seed = seed;
                ter.renderFrame(tiles);
                character.resetForNextLevel(timeLimit);
                character.startCount();
                gameplayForUser(source, tiles);
                return;
            } else if (action == 'L') {
                TETile[][] tiles = loadLastGame();
                ter.renderFrame(tiles);
                character.startCount();
                gameplayForUser(source, tiles);
                return;
            } else if (action == 'R') {
                displayRoleMenu();
                setPlayerAppearance(source);
                displayMenu();
            } else if (action == 'G'){
                displayFirstGuide();
                goBackToMenuOrContinue(source);
            } else if (action == 'Q') {
                return;
            }
        }
    }

    /**
     * Method used for autograding and testing your code. The input string will be a series
     * of characters (for example, "n123sswwdasdassadwas", "n123sss:q", "lwww". The engine should
     * behave exactly as if the user typed these characters into the engine using
     * interactWithKeyboard.
     * <p>
     * Recall that strings ending in ":q" should cause the game to quite save. For example,
     * if we do interactWithInputString("n123sss:q"), we expect the game to run the first
     * 7 commands (n123sss) and then quit and save. If we then do
     * interactWithInputString("l"), we should be back in the exact same state.
     * <p>
     * In other words, both of these calls:
     * - interactWithInputString("n123sss:q")
     * - interactWithInputString("lww")
     * <p>
     * should yield the exact same world state as:
     * - interactWithInputString("n123sssww")
     *
     * @param input the input string to feed to your program
     * @return the 2D TETile[][] representing the state of the world
     */
    public TETile[][] interactWithInputString(String input) {
        // TODO: Fill out this method so that it run the engine using the input
        // passed in as an argument, and return a 2D tile representation of the
        // world that would have been drawn if the same inputs had been given
        // to interactWithKeyboard().
        //
        // See proj3.byow.InputDemo for a demo of how you can make a nice clean interface
        // that works for many different input types.
        input = input.toUpperCase();
        InputSource source = new StringInputDevice(input);
        char curr = source.getNextKey();
        if(curr == 'N'){
            // get seed from user
            String seed = "";
            curr = source.getNextKey();
            while(curr != 'S'){
                seed += curr;
                curr = source.getNextKey();
            }
            this.seed = Long.parseLong(seed);
            this.rand = new Random(this.seed);
            TETile[][] tiles = createNewWorld();
            // ter.renderFrame(tiles);
            gameplayForAG(source, tiles);
            return tiles;
        }
        else if(curr == 'L'){
            TETile[][] tiles = loadLastGame();
            // ter.renderFrame(tiles);
            //Main loop of game
            gameplayForAG(source, tiles);
            return tiles;
        }
        return null;
    }
}
