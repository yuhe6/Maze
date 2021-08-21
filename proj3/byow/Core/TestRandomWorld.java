package byow.Core;

import byow.TileEngine.TETile;
import edu.princeton.cs.introcs.StdDraw;
import org.junit.Test;
import static org.junit.Assert.*;

public class TestRandomWorld {
    @Test
    public void testRandomWorldMap(){
        // sparse
        System.out.println("2 sub spaces");
        Engine engine = new Engine(2);
        engine.interactWithInputString("n123s");

        //// default
        System.out.println("8 sub spaces");
        engine = new Engine(8);
        engine.interactWithInputString("n123s");

        //// dense
        System.out.println("10 sub spaces");
        engine = new Engine(10);
        engine.interactWithInputString("n123s");

        //// extreme case safe test
        System.out.println("20 sub spaces");
        engine = new Engine(20);
        engine.interactWithInputString("n123s");

        // saving and loading
        Engine engine1 = new Engine(6);
        Engine engine2 = new Engine(6);
        Engine engine3 = new Engine(6);
        TETile[][] tiles2, tiles3;
        engine1.interactWithInputString("N999SDDD:Q");
        tiles2 = engine2.interactWithInputString("LWWWDDD");
        tiles3 = engine3.interactWithInputString("N999SDDDWWWDDD");
        assertArrayEquals(tiles2, tiles3);
    }


    @Test
    public void testPersistence(){
        // persistence
        Engine engine = new Engine();
        engine.interactWithInputString("n123sss:q");
        engine.interactWithInputString("lww");
    }

    @Test
    public void testDisplayMenu() {
        Engine engine = new Engine();
        engine.displayMenu();
        StdDraw.pause(5000);
    }

    @Test
    public void testInteractWithKeyboard(){
        Engine engine = new Engine();
        engine.interactWithKeyboard();
    }
}
