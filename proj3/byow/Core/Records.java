package byow.Core;

import java.io.Serializable;
import java.util.Comparator;
import java.util.LinkedList;

public class Records implements Serializable {
    public LinkedList<userInfo> localRecords;

    private class userInfo implements Serializable{
        private int score;
        private String name;
        private long seed;

        userInfo(){
            this.score = 0;
            this.name = "";
        }

        userInfo(int score, String name, long seed){
            this.score = score;
            this.name = name;
            this.seed = seed;
        }
    }

    Records(){
        localRecords = new LinkedList<>();
    }

    public void add(int newScore, String name, long seed){
        localRecords.add(new userInfo(newScore, name, seed));
        localRecords.sort(Comparator.comparingInt(o -> o.score*(-1)));
        while (localRecords.size() > 30){
            localRecords.pollLast();
        }
    }

    public long getSeed(int i){
        return localRecords.get(i).seed;
    }

    public String getName(int i){
        return localRecords.get(i).name;
    }

    public int getScore(int i){
        return localRecords.get(i).score;
    }
}
