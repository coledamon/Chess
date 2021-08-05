package prog;

import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

public class Board {

    public Piece[][] board = new Piece[8][8];

    public Board(){
        System.out.println("Would you like to play normal chess (Enter 0) or Chess 960 (Enter 1)?");
        Scanner in = new Scanner(System.in);
        int choice = 0;
        boolean valid = false;
        do {
            String input = in.nextLine();
            try {
                choice = Integer.parseInt(input);
                if(choice != 0 && choice != 1) throw new Exception();
                valid = true;
            } catch (Exception e) {
                System.out.println("Please enter a valid option (0 or 1).");
            }
        } while(!valid);
        switch (choice) {
            case 0:
                this.initializeNormal();
                break;
            case 1:
                this.initialize960();
                break;
        }
    }

    private void initializeNormal(){
        for(int x = 0; x<board.length; x++){
            for(int y = 0; y<board[0].length; y++){
                board[x][y] = null;
            }
        }

        // White pawns
        for(int x=0; x<8; x++){
            board[1][x] = new Pawn("white");
        }

        // Black pawns
        for(int x=0; x<8; x++){
            board[6][x] = new Pawn("black");
        }

        //Rooks
        board[0][0] = new Rook("white");
        board[0][7] = new Rook("white");
        board[7][7] = new Rook("black");
        board[7][0] = new Rook("black");

        //Knights
        board[0][1] = new Knight("white");
        board[0][6] = new Knight("white");
        board[7][6] = new Knight("black");
        board[7][1] = new Knight("black");

        //Bishops
        board[0][2] = new Bishop("white");
        board[0][5] = new Bishop("white");
        board[7][2] = new Bishop("black");
        board[7][5] = new Bishop("black");

        //Queens
        board[0][3] = new Queen("white");
        board[7][3] = new Queen("black");

        //Kings
        board[0][4] = new King("white");
        board[7][4] = new King("black");

    }

    private void initialize960(){
        //The bishops must be placed on opposite-color squares.
        //The king must be placed on a square between the rooks.
//        everything but pawns randomized
        Random rand = new Random();
        ArrayList<Integer> spaces = new ArrayList<>(Arrays.asList(0, 1, 2, 3, 4, 5, 6, 7));
        ArrayList<Integer> spacesNoChange = new ArrayList<>(spaces);
        for(int x = 0; x<board.length; x++){
            for(int y = 0; y<board[0].length; y++){
                board[x][y] = null;
            }
        }

        // White pawns
        for(int x=0; x<8; x++){
            board[1][x] = new Pawn("white");
        }

        // Black pawns
        for(int x=0; x<8; x++){
            board[6][x] = new Pawn("black");
        }

        //Rooks
        //must be at least 1 space apart, king must go between
        int index = spaces.get(rand.nextInt(spaces.size()));
        int rook1 = spacesNoChange.get(index);
        spaces.remove((Integer) index);
        index = spaces.get(rand.nextInt(spaces.size()));
        int rook2 = spacesNoChange.get(index);
        while(Math.abs(rook2-rook1) < 2) {
            index = spaces.get(rand.nextInt(spaces.size()));
            rook2 = spacesNoChange.get(index);
        }
        spaces.remove((Integer) index);

        //king king goes between rooks
        int king = rand.nextInt(Math.abs(rook2-rook1)-1)+1+Math.min(rook1, rook2);
        spaces.remove((Integer) king);
        index = spaces.get(rand.nextInt(spaces.size()));

        //bishops must be on opposite colored spaces
        int bishop1 = spacesNoChange.get(index);
        spaces.remove((Integer) index);
        List<Integer> temp = spaces.stream().filter(i-> (bishop1 % 2 == 0) == (i % 2 != 0)).collect(Collectors.toList());
        index = temp.get(rand.nextInt(temp.size()));
        int bishop2 = spacesNoChange.get(index);
        spaces.remove((Integer) index);
        index = spaces.get(rand.nextInt(spaces.size()));

        //knights
        int knight1 = spacesNoChange.get(index);
        spaces.remove((Integer) index);
        index = spaces.get(rand.nextInt(spaces.size()));
        int knight2 = spacesNoChange.get(index);
        spaces.remove((Integer) index);

        //queen
        index = spaces.get(rand.nextInt(spaces.size()));
        int queen = spacesNoChange.get(index);
        spaces.remove((Integer) index);

        //Rooks
        board[0][rook1] = new Rook("white");
        board[0][rook2] = new Rook("white");
        board[7][rook2] = new Rook("black");
        board[7][rook1] = new Rook("black");

        //Knights
        board[0][knight1] = new Knight("white");
        board[0][knight2] = new Knight("white");
        board[7][knight2] = new Knight("black");
        board[7][knight1] = new Knight("black");

        //Bishops
        board[0][bishop1] = new Bishop("white");
        board[0][bishop2] = new Bishop("white");
        board[7][bishop1] = new Bishop("black");
        board[7][bishop2] = new Bishop("black");

        //Queens
        board[0][queen] = new Queen("white");
        board[7][queen] = new Queen("black");

        //Kings
        board[0][king] = new King("white");
        board[7][king] = new King("black");

    }

    /**
     * Checks to see if the king of the given color is in check. This will be called twice after every move
     * @param color
     * @return boolean value corresponding to the king being in check
     */
    public boolean isInCheck(String color){
        int[] kingPos = getKingPos(color);
        int row = kingPos[0];
        int col = kingPos[1];

        for(int x = 0; x<board.length; x++){
            for(int y = 0; y<board[0].length; y++){
                if(board[x][y] != null){
                    if(board[x][y].validateMove(board, x, y, row, col) && !board[x][y].getColor().equals(color)){
                        return true;
                    }
                }
            }
        }

        return false;
    }

    /**
     * Performs the move, and modifies the actual board
     *
     * @throws IOException
     */
    public void performMove(String move, String color, boolean actuallyMove) throws IOException{
        int[] moveArray = parseInput(move);
        //System.out.println(board[moveArray[0]][moveArray[1]]);

        if(board[moveArray[0]][moveArray[1]] == null){
            throw new IOException();
        }

        if(!board[moveArray[0]][moveArray[1]].getColor().equals(color)){
            throw new IOException();
        }

        if(board[moveArray[2]][moveArray[3]] != null){
            if(board[moveArray[2]][moveArray[3]].getColor().equals(color)){
                throw new IOException();
            }
        }

        if(board[moveArray[0]][moveArray[1]].validateMove(board, moveArray[0], moveArray[1], moveArray[2], moveArray[3])){
            //This means the move was valid

            if(isInCheck(color)){
                //Because the current player put him/herself in check
                //I need to find out what is going to happen to the board in this case
                System.out.println("1");
                throw new IOException();
            }

            if(actuallyMove){
                //Switch the two spots on the board because the move was valid
                board[moveArray[2]][moveArray[3]] = board[moveArray[0]][moveArray[1]];
                board[moveArray[0]][moveArray[1]] = null;
            }

            if(board[moveArray[2]][moveArray[3]] != null){
                if(board[moveArray[2]][moveArray[3]].getClass().isInstance(new King("white"))){
                    if(actuallyMove){
                        ((King) board[moveArray[2]][moveArray[3]]).hasMoved = true;
                    }
                    //This Piece is a King
                    if(((King) board[moveArray[2]][moveArray[3]]).castled){
                        if(moveArray[3] - moveArray[1] == 2){
                            board[moveArray[2]][moveArray[3] - 1] = board[moveArray[2]][moveArray[3] + 1];
                            board[moveArray[2]][moveArray[3] + 1] = null;
                        }else{
                            board[moveArray[2]][moveArray[3] + 1] = board[moveArray[2]][moveArray[3] - 1];
                            board[moveArray[2]][moveArray[3] - 1] = null;
                        }
                        ((King) board[moveArray[2]][moveArray[3]]).castled = false;
                    }
                }
            }

        }else{
            throw new IOException();
        }

        //Rules dealing with pawns
        if(actuallyMove){
            Piece piece = board[moveArray[2]][moveArray[3]];
            //This way it gets toggled the next time it moves
            piece.ep_able = false;
            if(piece != null){
                if(piece.getClass().isInstance(new Pawn("white"))){
                    //The piece is a pawn
                    piece.hasMoved = true;

                    //Promotion
                    Piece replacement;
                    if(move.split(" ").length < 3){
                        move += " s";
                    }
                    if(piece.getColor().equals("white")){
                        if(moveArray[2] == 7){
                            switch(move.split(" ")[2].charAt(0)){
                                case 'N': replacement = new Knight("white"); break;
                                case 'B': replacement = new Bishop("white"); break;
                                default: replacement = new Queen("white"); break;
                            }
                            board[moveArray[2]][moveArray[3]] = replacement;
                        }
                    }else{
                        if(moveArray[2] == 0){
                            switch(move.split(" ")[2].charAt(0)){
                                case 'N': replacement = new Knight("black"); break;
                                case 'B': replacement = new Bishop("black"); break;
                                default: replacement = new Queen("black"); break;
                            }
                            board[moveArray[2]][moveArray[3]] = replacement;
                        }
                    }

                    //En passante capture
                    /*int newCol = moveArray[3];
                      int newRow = moveArray[2];
                      if(inSixthRank(color, newRow)){
                      if(newCol + 1 < 8){
                      if(board[newRow][newCol + 1] != null){
                      if(board[newRow][newCol + 1].getClass().isInstance(new Pawn("white"))){
                      if(board[][].ep_able){
                      board[][] = null;
                      }
                      }
                      }
                      }else if(newCol - 1 > 0){
                      if(board[newRow][newCol - 1] != null){
                      if(board[newRow][newCol - 1].getClass().isInstance(new Pawn("white"))){
                      if(board[][].ep_able){
                      board[][] = null;
                      }
                      }
                      }
                      }
                      }*/

                }
            }
        }
    }

    private boolean inSixthRank(String color, int rank){
        if(color.equals("white")){
            return rank == 5;
        }else{
            return rank == 2;
        }
    }

    /**
     * Parses the user's string input for a move
     * @param move
     * @return An array of size 4 with the initial x, y positions and the final x, y positions in that order
     */
    public static int[] parseInput(String move){
        int[] returnArray = new int[4];

        String[] split = move.split(" ");
        returnArray[1] = charToInt(Character.toLowerCase(split[0].charAt(0)));
        returnArray[0] = Integer.parseInt(move.charAt(1) + "") - 1;

        returnArray[3] = charToInt(Character.toLowerCase(split[1].charAt(0)));
        returnArray[2] = Integer.parseInt(split[1].charAt(1) + "") - 1;
        return returnArray;

    }

    /**
     * Returns an integer corresponding to the user input
     */
    public static int charToInt(char ch){
        switch(ch){
            case 'a': return 0;
            case 'b': return 1;
            case 'c': return 2;
            case 'd': return 3;
            case 'e': return 4;
            case 'f': return 5;
            case 'g': return 6;
            case 'h': return 7;
            default: return 8;
        }
    }

    private int[] getKingPos(String color){
        int row = 0, col = 0;

        for(int x = 0; x<board.length; x++){
            for(int y = 0; y<board[0].length; y++){
                if(board[x][y] != null){
                    if(board[x][y].getClass().isInstance(new King("white")) && board[x][y].getColor().equals(color)){
                        row = x;
                        col = y;
                    }
                }
            }
        }
        int[] returnArray = new int[2];
        returnArray[0] = row;
        returnArray[1] = col;

        return returnArray;

    }

    /**
     * Checks to see if any moves are possible. If not, then it is either a checkmate or stalemate, depending on whether or not anyone is currently in check.
     * @return
     */
    public boolean canAnyPieceMakeAnyMove(String color){

        Piece[][] oldBoard = board.clone();

        for(int x = 0; x<board.length; x++){
            for(int y = 0; y<board[0].length; y++){
                //Check this piece against every other piece...
                for(int w = 0; w<board.length; w++){
                    for(int z = 0; z<board[0].length; z++){
                        try{
                            if(board[x][y] != null){
                                if(board[x][y].getColor().equals(color)){
                                    //System.out.println(coordinatesToMoveString(x, y, w, z));
                                    performMove(coordinatesToMoveString(x, y, w, z), board[x][y].getColor(), false);
                                    board = oldBoard;
                                    return true;
                                }
                            }
                            board = oldBoard;
                        } catch(Exception e){
                            board = oldBoard;
                        }
                    }
                }
            }
        }

        board = oldBoard;
        return false;
    }

    private String coordinatesToMoveString(int row, int col, int newRow, int newCol){

        String returnString = "";

        switch(col){
            case 0: returnString += 'a'; break;
            case 1: returnString += 'b'; break;
            case 2: returnString += 'c'; break;
            case 3: returnString += 'd'; break;
            case 4: returnString += 'e'; break;
            case 5: returnString += 'f'; break;
            case 6: returnString += 'g'; break;
            case 7: returnString += 'h'; break;
            default: returnString += 'a'; break;
        }

        int addInt = row + 1;

        returnString += addInt + "";

        returnString += " ";

        switch(newCol){
            case 0: returnString += 'a'; break;
            case 1: returnString += 'b'; break;
            case 2: returnString += 'c'; break;
            case 3: returnString += 'd'; break;
            case 4: returnString += 'e'; break;
            case 5: returnString += 'f'; break;
            case 6: returnString += 'g'; break;
            case 7: returnString += 'h'; break;
            default: returnString += 'a'; break;
        }

        addInt = newRow + 1;

        returnString += addInt + "";
        //System.out.println(row + " " + col + " " + newRow + " " + newCol + " " + returnString);
        return returnString;
    }

    /**
     * Checks to see if there is a stalemate
     * @return true if stalemated, false otherwise
     */
    public boolean staleMate(String color){
        return false;
    }

    public String toString(){
        String string = "";
        int fileCount = 0;
        for(Piece[] pieces: board){
            int rankCount = 0;
            for(Piece piece: pieces){
                if(piece==null){
                    if(fileCount%2 == 0){
                        if(rankCount%2 == 0){
                            string += "##";
                        }else{
                            string += "  ";
                        }
                    }else{
                        if(rankCount%2 == 0){
                            string += "  ";
                        }else{
                            string += "##";
                        }
                    }
                }else{
                    string += piece;
                }
                string += " ";
                rankCount++;
            }
            fileCount++;
            string += "\n";
        }

        String reverseString = "";

        reverseString += "  a  b  c  d  e  f  g  h \n";
        String[] stringSplit = string.split("\n");
        for(int x = stringSplit.length-1; x >= 0; x--){
            reverseString += x+1 + " " + stringSplit[x] + "\n";
        }

        return reverseString;
    }

}