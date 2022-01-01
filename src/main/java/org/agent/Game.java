package org.agent;

import org.jvan100.jutils.structures.ImmutablePair;
import org.jvan100.jutils.structures.Pair;

import java.util.Arrays;
import java.util.Random;

public class Game {

    private final int[][] board;
    private int score;

    private final Random rand;

    Game() {
        this.board = new int[4][4];
        this.score = 0;
        this.rand = new Random();

        addRandomTile();
    }

    void move(int move) {
        final Pair<int[][], Integer> pair = simulateMove(board, move);

    }

    int move(int[][] board, int move) {
        int[][] newBoard = new int[4][4];
        int gainedScore = 0;
        boolean hasMoved = false;

        switch (move) {
            // UP
            case 0:
                for (int col = 0; col < 4; col++) {
                    boolean[] locked = {false, false, false, false};

                    for (int row = 0; row < 4; row++) {
                        final int val = board[row][col];

                        for (int testRow = row - 1; testRow >= 0; testRow--) {
                            if (board[testRow][col] != 0) {
                                if (board[testRow][col] == val && !locked[testRow]) {
                                    board[testRow + 1][col] = 0;
                                    board[testRow][col] = val + 1;
                                    gainedScore += val + 1;
                                    locked[testRow] = true;
                                    hasMoved = true;
                                }

                                break;
                            }

                            board[testRow + 1][col] = 0;
                            board[testRow][col] = val;
                            hasMoved = true;
                        }
                    }
                }

                break;
            // RIGHT
            case 1:
                for (int row = 0; row < 4; row++) {
                    boolean[] locked = {false, false, false, false};

                    for (int col = 3; col >= 0; col--) {
                        final int val = board[row][col];

                        if (val != 0) {
                            newBoard[row][col] = val;

                            for (int testCol = col + 1; testCol < 4; testCol++) {
                                if (newBoard[row][testCol] != 0) {
                                    if (newBoard[row][testCol] == val && !locked[testCol]) {
                                        newBoard[row][testCol - 1] = 0;
                                        newBoard[row][testCol] = val + 1;
                                        gainedScore += val + 1;
                                        locked[testCol] = true;
                                        hasMoved = true;
                                    }

                                    break;
                                }

                                newBoard[row][testCol - 1] = 0;
                                newBoard[row][testCol] = val;
                                hasMoved = true;
                            }
                        }
                    }
                }

                break;
            // DOWN
            case 2:
                for (int col = 0; col < 4; col++) {
                    boolean[] locked = {false, false, false, false};

                    for (int row = 3; row >= 0; row--) {
                        final int val = board[row][col];

                        if (val != 0) {
                            newBoard[row][col] = val;

                            for (int testRow = row + 1; testRow < 4; testRow++) {
                                if (newBoard[testRow][col] != 0) {
                                    if (newBoard[testRow][col] == val && !locked[testRow]) {
                                        newBoard[testRow - 1][col] = 0;
                                        newBoard[testRow][col] = val + 1;
                                        gainedScore += val + 1;
                                        locked[testRow] = true;
                                        hasMoved = true;
                                    }

                                    break;
                                }

                                newBoard[testRow - 1][col] = 0;
                                newBoard[testRow][col] = val;
                                hasMoved = true;
                            }
                        }
                    }
                }

                break;
            // LEFT
            case 3:
                for (int row = 0; row < 4; row++) {
                    boolean[] locked = {false, false, false, false};

                    for (int col = 0; col < 4; col++) {
                        final int val = board[row][col];

                        if (val != 0) {
                            newBoard[row][col] = val;

                            for (int testCol = col - 1; testCol >= 0; testCol--) {
                                if (newBoard[row][testCol] != 0) {
                                    if (newBoard[row][testCol] == val && !locked[testCol]) {
                                        newBoard[row][testCol + 1] = 0;
                                        newBoard[row][testCol] = val + 1;
                                        gainedScore += val + 1;
                                        locked[testCol] = true;
                                        hasMoved = true;
                                    }

                                    break;
                                }

                                newBoard[row][testCol + 1] = 0;
                                newBoard[row][testCol] = val;
                                hasMoved = true;
                            }
                        }
                    }
                }

                break;
        }

        return 0;
    }
    
    static Pair<int[][], Integer> simulateMove(int[][] board, int move) {
        final int[][] newBoard = new int[4][4];
        int gainedScore = 0;
        boolean hasMoved = false;
        
        switch (move) {
            // UP
            case 0:
                for (int col = 0; col < 4; col++) {
                    boolean[] locked = {false, false, false, false};

                    for (int row = 0; row < 4; row++) {
                        final int val = board[row][col];

                        if (val != 0) {
                            newBoard[row][col] = val;

                            for (int testRow = row - 1; testRow >= 0; testRow--) {
                                if (newBoard[testRow][col] != 0) {
                                    if (newBoard[testRow][col] == val && !locked[testRow]) {
                                        newBoard[testRow + 1][col] = 0;
                                        newBoard[testRow][col] = val + 1;
                                        gainedScore += val + 1;
                                        locked[testRow] = true;
                                        hasMoved = true;
                                    }

                                    break;
                                }

                                newBoard[testRow + 1][col] = 0;
                                newBoard[testRow][col] = val;
                                hasMoved = true;
                            }
                        }
                    }
                }

                break;
            // RIGHT
            case 1:
                for (int row = 0; row < 4; row++) {
                    boolean[] locked = {false, false, false, false};

                    for (int col = 3; col >= 0; col--) {
                        final int val = board[row][col];

                        if (val != 0) {
                            newBoard[row][col] = val;

                            for (int testCol = col + 1; testCol < 4; testCol++) {
                                if (newBoard[row][testCol] != 0) {
                                    if (newBoard[row][testCol] == val && !locked[testCol]) {
                                        newBoard[row][testCol - 1] = 0;
                                        newBoard[row][testCol] = val + 1;
                                        gainedScore += val + 1;
                                        locked[testCol] = true;
                                        hasMoved = true;
                                    }

                                    break;
                                }

                                newBoard[row][testCol - 1] = 0;
                                newBoard[row][testCol] = val;
                                hasMoved = true;
                            }
                        }
                    }
                }

                break;
            // DOWN
            case 2:
                for (int col = 0; col < 4; col++) {
                    boolean[] locked = {false, false, false, false};

                    for (int row = 3; row >= 0; row--) {
                        final int val = board[row][col];

                        if (val != 0) {
                            newBoard[row][col] = val;

                            for (int testRow = row + 1; testRow < 4; testRow++) {
                                if (newBoard[testRow][col] != 0) {
                                    if (newBoard[testRow][col] == val && !locked[testRow]) {
                                        newBoard[testRow - 1][col] = 0;
                                        newBoard[testRow][col] = val + 1;
                                        gainedScore += val + 1;
                                        locked[testRow] = true;
                                        hasMoved = true;
                                    }

                                    break;
                                }

                                newBoard[testRow - 1][col] = 0;
                                newBoard[testRow][col] = val;
                                hasMoved = true;
                            }
                        }
                    }
                }

                break;
            // LEFT
            case 3:
                for (int row = 0; row < 4; row++) {
                    boolean[] locked = {false, false, false, false};

                    for (int col = 0; col < 4; col++) {
                        final int val = board[row][col];

                        if (val != 0) {
                            newBoard[row][col] = val;

                            for (int testCol = col - 1; testCol >= 0; testCol--) {
                                if (newBoard[row][testCol] != 0) {
                                    if (newBoard[row][testCol] == val && !locked[testCol]) {
                                        newBoard[row][testCol + 1] = 0;
                                        newBoard[row][testCol] = val + 1;
                                        gainedScore += val + 1;
                                        locked[testCol] = true;
                                        hasMoved = true;
                                    }

                                    break;
                                }

                                newBoard[row][testCol + 1] = 0;
                                newBoard[row][testCol] = val;
                                hasMoved = true;
                            }
                        }
                    }
                }

                break;
        }

        return new ImmutablePair<>(hasMoved ? newBoard : null, gainedScore);
    }

    private void addRandomTile() {
        int row, col;

        while (true) {
            row = rand.nextInt(4);
            col = rand.nextInt(4);

            if (board[row][col] == 0) {
                board[row][col] = (rand.nextDouble() < 0.9) ? 2 : 4;
                break;
            }
        }
    }

    void printBoard() {
        printBoard(board);
    }

    static void printBoard(int[][] board) {
        System.out.println(Arrays.deepToString(board));
    }

}
