package com.comp301.a09akari.model;

import java.util.ArrayList;
import java.util.List;

public class ModelImpl implements Model {
    private PuzzleLibrary puzzleLibrary;
    private int activePuzzleIndex;
    private boolean[][] lamps;
    private List<ModelObserver> observers;

    public ModelImpl(PuzzleLibrary puzzleLibrary) {
        this.puzzleLibrary = puzzleLibrary;
        this.activePuzzleIndex = 0;
        resetPuzzle(); // must be called after setting active index to initialize array
        this.observers = new ArrayList<>();
    }

    private void checkLampConditions(int r, int c) {
        if (outOfBounds(r, c)) {
            throw new IndexOutOfBoundsException("Board position (" + r + ", " + c + ") is out bounds");
        }
        if (cellTypeAt(r, c) != CellType.CORRIDOR) {
            throw new IllegalArgumentException("Lamps can only be used in corridors!");
        }
    }

    @Override
    public void addLamp(int r, int c) {
        checkLampConditions(r, c);
        lamps[r][c] = true;
        notifyObservers();
    }

    @Override
    public void removeLamp(int r, int c) {
        checkLampConditions(r, c);
        lamps[r][c] = false;
        notifyObservers();
    }

    @Override
    public boolean isLit(int r, int c) {
        checkLampConditions(r, c);
        return isLamp(r, c) || hasDirectViewToLamp(r, c);
    }

    private boolean hasDirectViewToLamp(int r, int c) {
        return checkLeft(r, c) || checkRight(r, c) || checkUp(r, c) || checkDown(r, c);
    }

    // Returns true iff there is a lamp visible to the left
    private boolean checkLeft(int r, int c) {
        while (0 < c--) {
            if (cellTypeAt(r, c) != CellType.CORRIDOR) {
                return false;
            }
            if (isLamp(r, c)) {
                return true;
            }
        }
        return false;
    }

    // Returns true iff there is a lamp visible to the right
    private boolean checkRight(int r, int c) {
        Puzzle p = getActivePuzzle();
        while (++c < p.getWidth()) {
            if (cellTypeAt(r, c) != CellType.CORRIDOR) {
                return false;
            }
            if (isLamp(r, c)) {
                return true;
            }
        }
        return false;
    }

    // Returns true iff there is a lamp visible above
    private boolean checkUp(int r, int c) {
        while (0 < r--) {
            if (cellTypeAt(r, c) != CellType.CORRIDOR) {
                return false;
            }
            if (isLamp(r, c)) {
                return true;
            }
        }
        return false;
    }

    // Returns true iff there is a lamp visible below
    private boolean checkDown(int r, int c) {
        Puzzle p = getActivePuzzle();
        while (++r < p.getHeight()) {
            if (cellTypeAt(r, c) != CellType.CORRIDOR) {
                return false;
            }
            if (isLamp(r, c)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean isLamp(int r, int c) {
        checkLampConditions(r, c);
        return lamps[r][c];
    }

    @Override
    public boolean isLampIllegal(int r, int c) {
        if (outOfBounds(r, c)) {
            throw new IndexOutOfBoundsException();
        }
        if (!isLamp(r, c)) {
            throw new IllegalArgumentException();
        }
        return hasDirectViewToLamp(r, c);
    }

    @Override
    public Puzzle getActivePuzzle() {
        return puzzleLibrary.getPuzzle(activePuzzleIndex);
    }

    @Override
    public int getActivePuzzleIndex() {
        return activePuzzleIndex;
    }

    @Override
    public void setActivePuzzleIndex(int index) {
        if (index < 0 || index >= getPuzzleLibrarySize()) {
            throw new IndexOutOfBoundsException(
                "No puzzle exists for index ["
                    + index
                    + "] with puzzle list size ["
                    + getPuzzleLibrarySize()
                    + "]");
        }
        this.activePuzzleIndex = index;
        this.lamps = new boolean[getActivePuzzle().getHeight()][getActivePuzzle().getWidth()];
        notifyObservers();
    }

    @Override
    public int getPuzzleLibrarySize() {
        return this.puzzleLibrary.size();
    }

    @Override
    public void resetPuzzle() {
        Puzzle p = getActivePuzzle();
        this.lamps = new boolean[p.getHeight()][p.getWidth()];
        notifyObservers();
    }

    @Override
    public boolean isSolved() {
        for (int r = 0; r < getActivePuzzle().getHeight(); ++r) {
            for (int c = 0; c < getActivePuzzle().getWidth(); ++c) {
                // Check that all clues satisfied
                if (cellTypeAt(r, c) == CellType.CLUE) {
                    if (!isClueSatisfied(r, c)) {
                        return false;
                    }
                }

                // Check for no illegal lamps
                if (lamps[r][c]) {
                    if (isLampIllegal(r, c)) {
                        return false;
                    }
                }

                // Check that all corridors are lit
                if (cellTypeAt(r, c) == CellType.CORRIDOR) {
                    if (!isLit(r, c)) {
                        return false;
                    }
                }
            }
        }
        return true;
    }

    @Override
    public boolean isClueSatisfied(int r, int c) {
        if (outOfBounds(r, c)) {
            throw new IndexOutOfBoundsException();
        }
        if (cellTypeAt(r, c) != CellType.CLUE) {
            throw new IllegalArgumentException("Only clues can be satisfied");
        }
        return getActivePuzzle().getClue(r, c) == countAdjacentLamps(r, c);
    }

    private int countAdjacentLamps(int row, int col) {
        int count = 0;
        if (safeIsLamp(row - 1, col)) { // up
            count++;
        }
        if (safeIsLamp(row + 1, col)) { // down
            count++;
        }
        if (safeIsLamp(row, col - 1)) { // left
            count++;
        }
        if (safeIsLamp(row, col + 1)) { // right
            count++;
        }
        return count;
    }

    private boolean safeIsLamp(int row, int col) {
        if (outOfBounds(row, col)) {
            return false;
        }
        return lamps[row][col];
    }

    private boolean outOfBounds(int r, int c) {
        Puzzle puzzle = getActivePuzzle();
        return (r < 0 || c < 0 || r >= puzzle.getHeight() || c >= puzzle.getWidth());
    }

    @Override
    public void addObserver(ModelObserver observer) {
        observers.add(observer);
    }

    @Override
    public void removeObserver(ModelObserver observer) {
        observers.remove(observer);
    }

    private CellType cellTypeAt(int row, int column) {
        return getActivePuzzle().getCellType(row, column);
    }

    private void notifyObservers() {
        if (observers == null) {
            return;
        }
        for (ModelObserver mo : this.observers) {
            if (mo != null) {
                mo.update(this);
            }
        }
    }
}