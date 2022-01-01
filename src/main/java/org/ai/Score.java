package org.ai;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;

public abstract class Score {

    private static final HashMap<Integer, Integer> scores;

    static {
        scores = new HashMap<>();

        try {
            final BufferedReader br = new BufferedReader(new FileReader(new File("")));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static int getScore(int row) {
        return scores.get(row);
    }

}
