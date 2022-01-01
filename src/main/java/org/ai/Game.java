package org.ai;

import org.jvan100.jutils.structures.ImmutablePair;
import org.jvan100.jutils.structures.Pair;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Arrays;
import java.util.Random;

public class Game {

    public static void main(String[] args) {
        Game game = new Game();
//        game.board = toLong(new int[][] {{2, 2, 0, 4}, {0, 0, 1, 1}, {10, 10, 11, 1}, {0, 13, 0, 0}});
//        game.printBoard();
//        game.move(3);
//        game.printBoard();

        try {
            final BufferedWriter bw = new BufferedWriter(new FileWriter(new File("scores.txt")));

            for (int i = 0; i < 65536; i++) {
                int[] row = toRow(i);

                int score = 0;

                for (int val : row) {
                    score += val;
                }

                bw.write(i + " " + score);
                bw.newLine();
            }

            bw.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private long board;
    private final Random rand;

    public Game() {
        this.board = 0;
        this.rand = new Random();

        addRandomTile();
    }

    void move(int move) {
        final Pair<Long, Boolean> pair = move(board, move);
        board = pair.getFirst();
    }

    static Pair<Long, Boolean> move(long board, int move) {
        long newBoard = board;
        boolean hasMoved = false;
        int c, r;

        switch (move) {
            case 0:
                for (int col = 0; col < 4; col++) {
                    c = GameMoves.getLeftUpMove(getCol(board, col));

                    if (c != -1) {
                        newBoard = setCol(newBoard, col, c);
                        hasMoved = true;
                    }
                }

                break;
            case 1:
                for (int row = 0; row < 4; row++) {
                    r = GameMoves.getRightDownMove(getRow(board, row));

                    if (r != -1) {
                        newBoard = setRow(newBoard, row, r);
                        hasMoved = true;
                    }
                }

                break;
            case 2:
                for (int col = 0; col < 4; col++) {
                    c = GameMoves.getRightDownMove(getCol(board, col));

                    if (c != -1) {
                        newBoard = setCol(newBoard, col, c);
                        hasMoved = true;
                    }
                }

                break;
            case 3:
                for (int row = 0; row < 4; row++) {
                    r = GameMoves.getLeftUpMove(getRow(board, row));

                    if (r != -1) {
                        newBoard = setRow(newBoard, row, r);
                        hasMoved = true;
                    }
                }

                break;
        }

        return new ImmutablePair<>(newBoard, hasMoved);
    }

    private int getRow(int rowLoc) {
        return getRow(board, rowLoc);
    }

    static int getRow(long board, int rowLoc) {
        return (int) ((board >> (rowLoc * 16)) & 65535);
    }

    private int getCol(int colLoc) {
        return getCol(board, colLoc);
    }

    static int getCol(long board, int colLoc) {
        int a = (int) ((board & (15L << ((12 + colLoc) * 4))) >> 36);
        int b = (int) ((board & (15L << ((8 + colLoc) * 4))) >> 24);
        int c = (int) ((board & (15L << ((4 + colLoc) * 4))) >> 12);
        int d = (int) (board & (15L << (colLoc * 4)));
        return (a | b | c | d) >> (colLoc * 4);
    }

    private byte getVal(int loc) {
        return getVal(board, loc);
    }

    static byte getVal(long board, int loc) {
        return (byte) ((board >> (loc * 4)) & 15);
    }

    private void setVal(int loc, byte val) {
        board = setVal(board, loc, val);
    }

    static long setVal(long board, int loc, byte val) {
        long newBoard = board;
        newBoard &= ~(15L << (loc * 4));
        newBoard |= ((long) val << (loc * 4));
        return newBoard;
    }

    private void setRow(int rowLoc, int row) {
        board = setRow(board, rowLoc, row);
    }

    static long setRow(long board, int rowLoc, int row) {
        long newBoard = board;
        newBoard &= ~(65535L << (rowLoc * 16));
        newBoard |= ((long) row << (rowLoc * 16));
        return newBoard;
    }

    private void setCol(int colLoc, int col) {
        board = setCol(board, colLoc, col);
    }

    static long setCol(long board, int colLoc, int col) {
        long newBoard = board;
        newBoard = setVal(newBoard, 12 + colLoc, (byte) (col >> 12));
        newBoard = setVal(newBoard, 8 + colLoc, (byte) ((col >> 8) & 15));
        newBoard = setVal(newBoard, 4 + colLoc, (byte) ((col >> 4) & 15));
        newBoard = setVal(newBoard, colLoc, (byte) (col & 15));
        return newBoard;
    }

    private void addRandomTile() {
        int loc;

        while (true) {
            loc = rand.nextInt(16);

            if (getVal(loc) == 0) {
                setVal(loc, (byte) (rand.nextDouble() < 0.9 ? 1 : 2));
                break;
            }
        }
    }

    private void printBoard() {
        printBoard(board);
    }

    static void printBoard(long board) {
        int val;

        for (int row = 0; row < 4; row++) {
            for (int col = 0; col < 4; col++) {
                val = getVal(board, convertToLoc(row, col));
                System.out.print(((val == 0) ? 0 : (int) Math.pow(2, val)) + " ");
            }

            System.out.println();
        }
    }

    static int convertToLoc(int row, int col) {
        return (3 - row) * 4 + (3 - col);
    }

    static long toLong(int[][] board) {
        long b = 0;

        for (int row = 0; row < 4; row++) {
            for (int col = 0; col < 4; col++) {
                b &= ~(15L << (convertToLoc(row, col) * 4));
                b |= ((long) board[row][col] << (convertToLoc(row, col) * 4));
            }
        }

        return b;
    }

    static int[] toRow(int row) {
        int[] r = new int[4];

        for (int i = 0; i < 4; i++) {
            r[i] = (row >> (12 - (i * 4))) & 15;
        }

        return r;
    }

    static int toInt(int[] row) {
        int r = 0;

        for (int col = 0; col < 4; col++) {
            r &= ~(15L << ((3 - col) * 4));
            r |= (row[col] << ((3 - col) * 4));
        }

        return r;
    }

    static String paddedBinaryString(int board, int padding) {
        return String.format("%" + padding + "s", Integer.toBinaryString(board)).replace(' ', '0');
    }
}
