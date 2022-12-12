package com.comp301.a09akari.model;

public class PuzzleImpl implements Puzzle {

    private final int[][] board;

    public PuzzleImpl(int[][] board) {
        validateBoard(board);
        this.board = board;
    }

    /**
     * Helper function to check that no invalid puzzle board is constructed
     *
     * @param board puzzle to check for validity
     */
    private void validateBoard(int[][] board) {
        if (board == null) {
            throw new RuntimeException("Provided board cannot be null");
        }
        int rowSize = -1;
        // Check nulls and lengths
        for (int[] row : board) {
            if (row == null) {
                throw new RuntimeException("Provided board has an internal null row");
            }
            if (rowSize == -1) {
                rowSize = row.length;
            } else if (rowSize != row.length) {
                throw new RuntimeException("Provided board has different length rows");
            }
        }
        for (int[] row : board) {
            for (int cell : row) {
                if (0 > cell || cell > 6) {
                    throw new RuntimeException("Provided board has invalid cell type value [" + cell + "]");
                }
            }
        }
    }

    @Override
    public int getWidth() {
        return board[0].length;
    }

    @Override
    public int getHeight() {
        return board.length;
    }

    @Override
    public CellType getCellType(int r, int c) {
        if (r < 0 || c < 0 || r >= getHeight() || c >= getWidth()) {
            throw new IndexOutOfBoundsException();
        }

        CellType ct = null;
        switch (board[r][c]) {
            case 0:
            case 1:
            case 2:
            case 3:
            case 4:
                ct = CellType.CLUE;
                break;
            case 5:
                ct = CellType.WALL;
                break;
            case 6:
                ct = CellType.CORRIDOR;
                break;
            default:
                throw new RuntimeException("CellType not defined for [" + board[r][c] + "]");
        }
        return ct;
    }

    @Override
    public int getClue(int r, int c) {
        if (r < 0 || c < 0 || r >= getHeight() || c >= getWidth()) {
            throw new IndexOutOfBoundsException();
        }
        if (getCellType(r, c) != CellType.CLUE) {
            throw new IllegalArgumentException();
        }
        // return value of the clue
        return board[r][c];
    }
}