package com.comp301.a09akari.model;

import com.comp301.a09akari.SamplePuzzles;

public final class PuzzleLibraryFactory {
    private PuzzleLibraryFactory() {}

    public static PuzzleLibrary ofDefault() {
        PuzzleLibrary puzzleLibrary = new PuzzleLibraryImpl();
        puzzleLibrary.addPuzzle(new PuzzleImpl(SamplePuzzles.PUZZLE_01));
        puzzleLibrary.addPuzzle(new PuzzleImpl(SamplePuzzles.PUZZLE_02));
        puzzleLibrary.addPuzzle(new PuzzleImpl(SamplePuzzles.PUZZLE_03));
        puzzleLibrary.addPuzzle(new PuzzleImpl(SamplePuzzles.PUZZLE_04));
        puzzleLibrary.addPuzzle(new PuzzleImpl(SamplePuzzles.PUZZLE_05));
        return puzzleLibrary;
    }
}
