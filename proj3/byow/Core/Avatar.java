package byow.Core;

import byow.TileEngine.TETile;
import byow.TileEngine.Tileset;

import java.io.Serializable;
import java.util.Timer;
import java.util.TimerTask;

public class Avatar implements Serializable {
    Point location;
    TETile avatarTile;
    String name;
    long seed;
    int health = 5;
    int greenVaccine = 0;
    int yellowVaccine = 0;
    int redVaccine = 0;
    int score = 0;
    boolean verified = false;
    long timeLeft;
    long safeTime;
    long fogTime;

    Avatar(Point location, TETile avatarTile){
        this.location = location;
        this.avatarTile = avatarTile;
    }

    Avatar(Point location){
        this.location = location;
        this.avatarTile = Tileset.AVATAR;
    }

    Avatar(TETile avatarTile){
        this.location = null;
        this.avatarTile = avatarTile;
    }

    public void addVaccine(TETile tile){
        if (tile.equals(Tileset.GREEN_VACCINE))
            greenVaccine ++;
        else if (tile.equals(Tileset.YELLOW_VACCINE))
            yellowVaccine ++;
        else if (tile.equals(Tileset.RED_VACCINE))
            redVaccine ++;
    }

    public boolean consumeVaccine(TETile tile){
        if (tile.equals(Tileset.GREEN_PATIENT)){
            if (greenVaccine > 0){
                greenVaccine --;
                score += 1;
                return true;
            }
            deductHealth(1);
        } else if (tile.equals(Tileset.YELLOW_PATIENT)){
            if (yellowVaccine > 0){
                yellowVaccine --;
                score += 2;
                return true;
            }
            deductHealth(2);
        } else if (tile.equals(Tileset.RED_PATIENT)){
            if (redVaccine > 0){
                redVaccine --;
                score += 3;
                return true;
            }
            deductHealth(3);
        }
        return false;
    }

    public void addHealth(int health){
        if (this.health < 9){
            this.health += health;
        }
    }

    public void deductHealth(int health){
        this.health -= health;
    }

    public void resetForNextLevel(long timeLimit){
        this.verified = false;
        timeLeft = timeLimit;
        safeTime = 20;
    }

    public Timer startCount(){
        TimerTask exit = new TimerTask() {
            @Override
            public void run() {
                timeLeft--;
                safeTime--;
                fogTime--;
            }
        };
        Timer countdown = new Timer();
        countdown.schedule(exit, 0,1000);
        return countdown;
    }

    public String verifyStatusForOutput(){
        if (verified){
            return "verified";
        }
        return "unverified";
    }

    public void SetUserName(String name){
        this.name = name;
    }

    public void autoSetTime(){
        if (safeTime <= 0){
            fogTime = 10;
            safeTime = 25;
        }
    }

}