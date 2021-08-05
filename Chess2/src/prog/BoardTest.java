package prog;

import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;

class BoardTest {

    @Test
    void InitializeNormalPawns() {
        Board board = new Board(0);
        for (int i = 0; i < board.board[1].length; i++) {
            assertTrue(board.board[1][i] instanceof Pawn);
            assertTrue(board.board[6][i] instanceof Pawn);
        }
    }

    @Test
    void InitializeNormalOthers() {
        Board board = new Board(0);
        //rooks
        assertTrue(board.board[0][0] instanceof Rook);
        assertTrue(board.board[0][7] instanceof Rook);
        assertTrue(board.board[7][0] instanceof Rook);
        assertTrue(board.board[7][7] instanceof Rook);
        //knights
        assertTrue(board.board[0][1] instanceof Knight);
        assertTrue(board.board[0][6] instanceof Knight);
        assertTrue(board.board[7][1] instanceof Knight);
        assertTrue(board.board[7][6] instanceof Knight);
        //bishops
        assertTrue(board.board[0][2] instanceof Bishop);
        assertTrue(board.board[0][5] instanceof Bishop);
        assertTrue(board.board[7][2] instanceof Bishop);
        assertTrue(board.board[7][5] instanceof Bishop);
        //kings
        assertTrue(board.board[0][4] instanceof King);
        assertTrue(board.board[7][4] instanceof King);
        //queens
        assertTrue(board.board[0][3] instanceof Queen);
        assertTrue(board.board[7][3] instanceof Queen);
    }

    @Test
    void Initialize960Pawns() {
        Board board = new Board(1);
        for (int i = 0; i < board.board[1].length; i++) {
            assertTrue(board.board[1][i] instanceof Pawn);
            assertTrue(board.board[6][i] instanceof Pawn);
        }
    }

    @Test
    void Initialize960OthersAllThere() {
        Board board = new Board(1);
        assertEquals(2, Arrays.stream(board.board[0]).filter(p -> p instanceof Rook).count());
        assertEquals(2, Arrays.stream(board.board[0]).filter(p -> p instanceof Knight).count());
        assertEquals(2, Arrays.stream(board.board[0]).filter(p -> p instanceof Bishop).count());
        assertEquals(1, Arrays.stream(board.board[0]).filter(p -> p instanceof King).count());
        assertEquals(1, Arrays.stream(board.board[0]).filter(p -> p instanceof Queen).count());

        assertEquals(2, Arrays.stream(board.board[7]).filter(p -> p instanceof Rook).count());
        assertEquals(2, Arrays.stream(board.board[7]).filter(p -> p instanceof Knight).count());
        assertEquals(2, Arrays.stream(board.board[7]).filter(p -> p instanceof Bishop).count());
        assertEquals(1, Arrays.stream(board.board[7]).filter(p -> p instanceof King).count());
        assertEquals(1, Arrays.stream(board.board[7]).filter(p -> p instanceof Queen).count());
    }

    @Test
    void Initialize960RookRulesFollowed() {
        Board board = new Board(1);
        List<Rook> pieces = Arrays.stream(board.board[0]).filter(p-> p instanceof Rook).map(p-> (Rook)p).collect(Collectors.toList());
        King king = (King) Arrays.stream(board.board[0]).filter(p-> p instanceof King).findFirst().get();
        assertEquals(pieces.size(), 2);
        int rook1Index = Arrays.stream(board.board[0]).collect(Collectors.toList()).indexOf(pieces.get(0));
        int rook2Index = Arrays.stream(board.board[0]).collect(Collectors.toList()).indexOf(pieces.get(1));
        int kingIndex = Arrays.stream(board.board[0]).collect(Collectors.toList()).indexOf(king);
        assertTrue(rook2Index > rook1Index ? (kingIndex > rook1Index && kingIndex < rook2Index) : (kingIndex < rook1Index && kingIndex > rook2Index));

        pieces = Arrays.stream(board.board[7]).filter(p-> p instanceof Rook).map(p-> (Rook)p).collect(Collectors.toList());
        king = (King) Arrays.stream(board.board[7]).filter(p-> p instanceof King).findFirst().get();
        assertEquals(pieces.size(), 2);
        rook1Index = Arrays.stream(board.board[7]).collect(Collectors.toList()).indexOf(pieces.get(0));
        rook2Index = Arrays.stream(board.board[7]).collect(Collectors.toList()).indexOf(pieces.get(1));
        kingIndex = Arrays.stream(board.board[7]).collect(Collectors.toList()).indexOf(king);
        assertTrue(rook2Index > rook1Index ? (kingIndex > rook1Index && kingIndex < rook2Index) : (kingIndex < rook1Index && kingIndex > rook2Index));
    }

    @Test
    void Initialize960BishopRulesFollowed() {
        Board board = new Board(1);
        List<Bishop> pieces = Arrays.stream(board.board[0]).filter(p-> p instanceof Bishop).map(p-> (Bishop)p).collect(Collectors.toList());
        assertEquals(pieces.size(), 2);
        int bishop1Index = Arrays.stream(board.board[0]).collect(Collectors.toList()).indexOf(pieces.get(0));
        int bishop2Index = Arrays.stream(board.board[0]).collect(Collectors.toList()).indexOf(pieces.get(1));
        assertTrue(bishop1Index %2 != bishop2Index %2);

        pieces = Arrays.stream(board.board[7]).filter(p-> p instanceof Bishop).map(p-> (Bishop)p).collect(Collectors.toList());
        assertEquals(pieces.size(), 2);
        bishop1Index = Arrays.stream(board.board[7]).collect(Collectors.toList()).indexOf(pieces.get(0));
        bishop2Index = Arrays.stream(board.board[7]).collect(Collectors.toList()).indexOf(pieces.get(1));
        assertTrue(bishop1Index %2 != bishop2Index %2);
    }
}