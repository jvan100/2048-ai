package org.jvan100;

import org.jvan100.jutils.structures.ImmutablePair;
import org.jvan100.jutils.structures.Pair;

import java.util.Arrays;
import java.util.Random;

public class Game {

    private int[][] board;
    private boolean[] locked;

    private int score;

    private final Random rand;

    Game() {
        //this.board = new int[4][4];
        this.board = new int[][] {{0, 10, 2, 0}, {2, 7, 9, 0}, {5, 4, 7, 2}, {4, 2, 3, 5}};
        this.locked = new boolean[4];
        this.score = 0;
        this.rand = new Random();

        //addRandomTile();
    }

    /*
        0 - UP
        1 - RIGHT
        2 - DOWN
        3 - LEFT
     */
    void move(int move) {
        final Pair<int[][], Integer> pair = simulateMove(board, move);
        board = pair.getFirst();
        score += pair.getSecond();

        addRandomTile();
    }

    static Pair<int[][], Integer> simulateMove(int[][] board, int move) {
        int[][] moveBoard = new int[4][4];
        int score = 0;
        boolean hasMoved = false;

        switch (move) {
            case 0:
                for (int col = 0; col < 4; col++) {
                    boolean[] locked = {false, false, false, false};

                    for (int row = 0; row < 4; row++) {
                        final int val = board[row][col];

                        if (val != 0) {
                            moveBoard[row][col] = val;

                            for (int testRow = row - 1; testRow >= 0; testRow--) {
                                if (moveBoard[testRow][col] != 0) {
                                    if (moveBoard[testRow][col] == val && !locked[testRow]) {
                                        moveBoard[testRow + 1][col] = 0;
                                        moveBoard[testRow][col] = val + 1;
                                        score += val + 1;
                                        locked[testRow] = true;
                                        hasMoved = true;
                                    }

                                    break;
                                }

                                moveBoard[testRow + 1][col] = 0;
                                moveBoard[testRow][col] = val;
                                hasMoved = true;
                            }
                        }
                    }
                }

                break;
            case 1:
                for (int row = 0; row < 4; row++) {
                    boolean[] locked = {false, false, false, false};

                    for (int col = 3; col >= 0; col--) {
                        final int val = board[row][col];

                        if (val != 0) {
                            moveBoard[row][col] = val;

                            for (int testCol = col + 1; testCol < 4; testCol++) {
                                if (moveBoard[row][testCol] != 0) {
                                    if (moveBoard[row][testCol] == val && !locked[testCol]) {
                                        moveBoard[row][testCol - 1] = 0;
                                        moveBoard[row][testCol] = val + 1;
                                        score += val + 1;
                                        locked[testCol] = true;
                                        hasMoved = true;
                                    }

                                    break;
                                }

                                moveBoard[row][testCol - 1] = 0;
                                moveBoard[row][testCol] = val;
                                hasMoved = true;
                            }
                        }
                    }
                }

                break;
            case 2:
                for (int col = 0; col < 4; col++) {
                    boolean[] locked = {false, false, false, false};

                    for (int row = 3; row >= 0; row--) {
                        final int val = board[row][col];

                        if (val != 0) {
                            moveBoard[row][col] = val;

                            for (int testRow = row + 1; testRow < 4; testRow++) {
                                if (moveBoard[testRow][col] != 0) {
                                    if (moveBoard[testRow][col] == val && !locked[testRow]) {
                                        moveBoard[testRow - 1][col] = 0;
                                        moveBoard[testRow][col] = val + 1;
                                        score += val + 1;
                                        locked[testRow] = true;
                                        hasMoved = true;
                                    }

                                    break;
                                }

                                moveBoard[testRow - 1][col] = 0;
                                moveBoard[testRow][col] = val;
                                hasMoved = true;
                            }
                        }
                    }
                }

                break;
            case 3:
                for (int row = 0; row < 4; row++) {
                    boolean[] locked = {false, false, false, false};

                    for (int col = 0; col < 4; col++) {
                        final int val = board[row][col];

                        if (val != 0) {
                            moveBoard[row][col] = val;

                            for (int testCol = col - 1; testCol >= 0; testCol--) {
                                if (moveBoard[row][testCol] != 0) {
                                    if (moveBoard[row][testCol] == val && !locked[testCol]) {
                                        moveBoard[row][testCol + 1] = 0;
                                        moveBoard[row][testCol] = val + 1;
                                        score += val + 1;
                                        locked[testCol] = true;
                                        hasMoved = true;
                                    }

                                    break;
                                }

                                moveBoard[row][testCol + 1] = 0;
                                moveBoard[row][testCol] = val;
                                hasMoved = true;
                            }
                        }
                    }
                }

                break;
        }

        return new ImmutablePair<>(hasMoved ? moveBoard : null, score);
    }

    private void addRandomTile() {
        while (true) {
            final int row = rand.nextInt(4);
            final int col = rand.nextInt(4);

            if (board[row][col] == 0) {
                board[row][col] = (rand.nextDouble() < 0.9) ? 2 : 4;
                break;
            }
        }
    }

    void printBoard() {
        printBoard(board);
    }

    void printBoard(int[][] board) {
        Arrays.stream(board)
              .forEach(row -> System.out.println(Arrays.toString(row)));
    }

    int[][] getBoard() {
        return board;
    }

    int getScore() {
        return score;
    }

}
