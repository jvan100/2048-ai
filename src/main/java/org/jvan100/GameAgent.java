package org.jvan100;
import org.jvan100.jutils.iterable.IntEnumerator;

import java.util.Arrays;
import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;

public class GameAgent implements Comparable<GameAgent> {

    // Monotonicity, smoothness, free space
    private final double[] weights;
    private int fitness;
    private final Random rand;

    int[][] board;

    private static final double[][][] MONOTONICITY_GRIDS = {
            {{16384, 4096, 1024, 256}, {4096, 1024, 256, 64}, {1024, 256, 64, 16}, {256, 64, 16, 4}},
            {{256, 1024, 4096, 16384}, {64, 256, 1024, 4096}, {16, 64, 256, 1024}, {4, 16, 64, 256}},
            {{4, 16, 64, 256}, {16, 64, 256, 1024}, {64, 256, 1024, 4096}, {256, 1024, 4096, 16384}},
            {{256, 64, 16, 4}, {1024, 256, 64, 16}, {4096, 1024, 256, 64}, {16384, 4096, 1024, 256}}
    };

    GameAgent() {
        this.weights = new double[3];
        this.rand = new Random();

        randomizeWeights();
    }

    private GameAgent(double[] weights) {
        this.weights = weights;
        this.rand = new Random();
    }

    void randomizeWeights() {
        for (int i = 0; i < weights.length; i++)
            weights[i] = rand.nextDouble();
    }

    void run() {
        final Game game = new Game();

        while (true) {
            final int move = expectimax(game.getBoard());

            if (move == -1)
                break;

//            game.printBoard();
//            System.out.println(move);

            game.move(move);
        }

        fitness = game.getScore();
        board = game.getBoard();
    }

    void setWeight(int i, double weight) {
        weights[i] = weight;
    }

    double getWeight(int i) {
        return weights[i];
    }

    int getFitness() {
        return fitness;
    }

    private int expectimax(int[][] board) {
        final ExecutorService es = Executors.newCachedThreadPool();

        double[] vals = {Double.NEGATIVE_INFINITY, Double.NEGATIVE_INFINITY, Double.NEGATIVE_INFINITY, Double.NEGATIVE_INFINITY};
        int[][][] boards = new int[4][4][4];

        AtomicBoolean gameOver = new AtomicBoolean(true);

        for (int i = 0; i < 4; i++) {
            final int finalI = i;

            boards[finalI] = Game.simulateMove(board, finalI).getFirst();

            es.execute(() -> {
                if (boards[finalI] != null) {
                    gameOver.set(false);
                    vals[finalI] = value(boards[finalI], 6, false);
                }
            });
        }

        es.shutdown();

        try {
            es.awaitTermination(400, TimeUnit.MILLISECONDS);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        if (gameOver.get())
            return -1;

        int maxIndex = 0;

        for (int i = 0; i < 4; i++) {
            if (vals[i] > vals[maxIndex])
                maxIndex = i;
        }

        return maxIndex;
    }

    private double value(int[][] board, int depth, boolean maximiser) {
        if (depth == 0)
            return evalTerminalState(board);

        return (maximiser ? maximisation(board, depth - 1) : expectation(board, depth - 1));
    }

    private double maximisation(int[][] board, int depth) {
        double max = Double.NEGATIVE_INFINITY;

        for (int i = 0; i < 4; i++) {
            final int[][] moveBoard = Game.simulateMove(board, i).getFirst();

            if (moveBoard != null)
                max = Math.max(max, value(moveBoard, depth, false));
        }

        return (max == Double.NEGATIVE_INFINITY) ? 0 : max;
    }

    private double expectation(int[][] board, int depth) {
        double avg = 0;

        for (int row = 0; row < 4; row++) {
            for (int col = 0; col < 4; col++) {
                if (board[row][col] == 0) {
                    final int val = board[row][col];

                    board[row][col] = 2;
                    avg += 0.9 * value(board, depth, true);

                    board[row][col] = 4;
                    avg += 0.1 * value(board, depth, true);

                    board[row][col] = val;
                }
            }
        }

        return avg;
    }

    private double evalTerminalState(int[][] board) {
        // Monotonicity
        double mUtility = 0;

        for (final double[][] monotonicityGrid : MONOTONICITY_GRIDS) {
            double gridUtility = 0;

            for (int row = 0; row < 4; row++) {
                for (int col = 0; col < 4; col++)
                    gridUtility += board[row][col] * monotonicityGrid[row][col];
            }

            mUtility = Math.max(mUtility, gridUtility);
        }

        // Smoothness
        double sUtility = 0;

        // Free space
        int fUtility = 0;

        for (int row = 0; row < 4; row++) {
            for (int col = 0; col < 4; col++) {
                if (board[row][col] != 0) {
                    if (col != 3 && board[row][col + 1] != 0)
                        sUtility += Math.pow(4, Math.abs(board[row][col] - board[row][col + 1]));

                    if (row != 3 && board[row + 1][col] != 0)
                        sUtility += Math.pow(4, Math.abs(board[row][col] - board[row + 1][col]));
                } else {
                    fUtility += 1;
                }
            }
        }

        fUtility = (int) Math.pow(2, fUtility);

        return weights[0] * mUtility - weights[1] * sUtility + weights[2] * fUtility;
    }

    GameAgent cloneAgent() {
        return new GameAgent(Arrays.copyOf(weights, weights.length));
    }

    @Override
    public int compareTo(GameAgent o) {
        return Integer.compare(o.getFitness(), fitness);
    }
}
