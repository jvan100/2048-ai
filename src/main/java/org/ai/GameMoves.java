package org.ai;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;

public abstract class GameMoves {

    private static final HashMap<Integer, Integer> rightDownMoves;
    private static final HashMap<Integer, Integer> leftUpMoves;

    static {
        rightDownMoves = new HashMap<>();
        leftUpMoves = new HashMap<>();

        try {
            BufferedReader br = new BufferedReader(new FileReader(new File("right_down_moves.txt")));

            String line;
            String[] split;

            while ((line = br.readLine()) != null) {
                split = line.split(" ");
                rightDownMoves.put(Integer.parseInt(split[0]), Integer.parseInt(split[1]));
            }

            br.close();

            br = new BufferedReader(new FileReader(new File("left_up_moves.txt")));

            while ((line = br.readLine()) != null) {
                split = line.split(" ");
                leftUpMoves.put(Integer.parseInt(split[0]), Integer.parseInt(split[1]));
            }

            br.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static int getRightDownMove(int rowCol) {
        final Integer i = rightDownMoves.get(rowCol);
        return (i == null) ? -1 : i;
    }

    public static int getLeftUpMove(int rowCol) {
        final Integer i = leftUpMoves.get(rowCol);
        return (i == null) ? -1 : i;
    }

}
