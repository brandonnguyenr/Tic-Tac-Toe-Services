package io.github.coreutils.proj.enginedata;


import java.util.Arrays;
import java.util.Objects;

/**
 * Board is a data-only class that holds a 2-dimensional array representing the current
 * state of a tic tac toe match.
 * @author Grant Goldsworth
 */
public class Board {
    public final int BOARD_ROWS = 3;
    public final int BOARD_COLUMNS = 3;
    private final Token[][] board = new Token[BOARD_ROWS][BOARD_COLUMNS];

    /**
     * Constructs a Board object by setting all values of Token[][] board to BLANK.
     */
    public Board() {
        Arrays.stream(board).forEach(str -> Arrays.fill(str, Token.BLANK));
    }

    /**
     * Gets the current Token[][] board of a Board object.
     * @return the Token[][] board of a Board object
     */
    public Token[][] getUnderlyingBoard() { return board; }

    /**
     * After checking to make sure the passed in position is within the board,
     * the method updates a token of Token[][] board.
     * <p>NOTE: This method uses x for column and y for row as per compatibility with
     * JFX's grid pane management.</p>
     * @param col the x-value for the token in the array [0, 1, 2]
     * @param row the y-value for the token in the array [0, 1, 2]
     * @param c the token for the array [X, O]
     */
    public void updateToken(int col, int row, Token c) {
        if ((col > BOARD_COLUMNS || row > BOARD_ROWS) || ( col < 0 || row < 0))
            throw new IllegalArgumentException(String.format("invalid xy position --> (%d, %d):" +
                    "valid bounds are (%d, %d]", col, row, 0, BOARD_ROWS));
        board[row][col] = c;
    }

    /**
     * Returns the token of a specific position in Token[][] board.
     * <p>NOTE: This method uses x for column and y for row as per compatibility with
     * JFX's grid pane management.</p>
     * @param col the x-value for the token in the array [0, 1, 2]
     * @param row the y-value for the token in the array [0, 1, 2]
     * @return The token [X, O, BLANK] of a specific position in Token[][] board.
     */
    public Token getToken(int col, int row) {
        if ((col > BOARD_COLUMNS || row > BOARD_ROWS) || ( col < 0 || row < 0))
            throw new IllegalArgumentException(String.format("invalid xy position --> (%d, %d):" +
                    "valid bounds are (%d, %d]", col, row, 0, BOARD_ROWS));
        return board[row][col];
    }

    /**
     * Checks to see if the board is full or not (all cells have a token that
     * is not the character ' ').
     * @return true if all cells have a value besides BLANK, false otherwise
     * @author Grant Goldsworth
     */
    public boolean isBoardFull() {
        for (Token[] row : board) {
            for (Token col : row) {
                // does cell have a valid token? If not, board isn't empty
                if (col == Token.BLANK)
                    return false;
            }
        }
        // all board cells have a token
        return true;
    }

    /**
     * Sets all values within Token[][] board to BLANK.
     */
    public void clearBoard() {
        for (Token[] row: board) Arrays.fill(row, Token.BLANK);
    }

    /**
     * Checks whether or not a Board object has the same size and state as another Board object.
     * @param o The Board that is being checked for equality
     * @return A boolean representing the equality of two Board objects
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Board)) return false;
        Board board1 = (Board) o;
        return Objects.equals(BOARD_ROWS, board1.BOARD_ROWS) &&
                Objects.equals(BOARD_COLUMNS, board1.BOARD_COLUMNS) &&
                // Uses deepEquals to check nested arrays
                Arrays.deepEquals(getUnderlyingBoard(), board1.getUnderlyingBoard());
    }

    /**
     * Returns the hash code of the calling Board object.
     * @return An integer representing the hashcode of the calling Board object
     */
    @Override
    public int hashCode() {
        int result = Objects.hash(BOARD_ROWS, BOARD_COLUMNS);
        result = 31 * result + Arrays.deepHashCode(getUnderlyingBoard());
        return result;
    }

    /**
     * Returns a string containing status of a Board object.
     * @return A string containing the status of a Board object
     */
    @Override
    public String toString() {
        // TODO: may need to be StringBuffer for thread safety?
        char delim = '|';
        StringBuilder buf = new StringBuilder();
        int i = 0;
        for (Token[] row : board) {
            for (var col : row) {
                buf.append(col);
                if (i < 2)
                    buf.append(delim);
                i++;
            }
            buf.append('\n');
            i = 0;
        }
        return buf.toString();
    }
}
