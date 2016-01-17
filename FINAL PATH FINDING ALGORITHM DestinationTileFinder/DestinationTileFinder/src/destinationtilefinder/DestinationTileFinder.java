/*
 * Karan Garg
 * May 28, 2014
 * DestinationTileFinder 1.0 ***Does not take terrain into consideration***
 * Algorithm for figuring out all possible destination tiles the player can move a piece to, considering the number of moves 
 * the piece is capable of moving in a turn.
 * 
 * ***This program DOES NOT RETURN THE PATH TAKEN BY THE PIECE, it only returns the possible destination tiles the piece can
 *    reach. Use the PathFinder algorithm to compute the actual paths possible to reach a given destination tile.***
 * 
 * ***To maintain consistenty with the indexing format of 2D arrays (row, column), 
 COORDINATES FOR POSITION OF A PIECE ON THE BOARD SHOULD BE GIVEN IN THE FORM (Y, X)*** 
 */
package destinationtilefinder;

import java.util.ArrayList;
import java.util.Arrays;

public class DestinationTileFinder {

    public static void main(String[] args) {
        //Test Algorithm
        int row = 0, column = 0, player;
        ArrayList<int[]> possibleDestinationTiles;
        int[] finalPos = new int[2];
        Piece testPiece;
        Piece[][] board = new Piece[8][12];       //create a 12 X 8 board

        //place pieces at random locations around the board
        for (int i = 0; i < 15; i++) {
            row = (int) (Math.random() * board.length);
            column = (int) (Math.random() * board.length);
            if (i % 2 == 0) {
                player = 1;
            } else {
                player = 2;
            }
            board[row][column] = new Piece(player, row, column);
        }
        printBoard(board);

        testPiece = board[row][column];

        int[] initialPos = {row, column};
        //find an empty final position
        for (int i = row; i < board.length; i++){
            for (int j = column; j < board[0].length; j++){
                if (board[i][j] == null){
                    finalPos[0] = i;
                    finalPos[1] = j;
                    break;
                }
            }
        }
        testPiece.setPlayer(3);
        board[finalPos[0]][finalPos[1]] = new Piece(4, finalPos[0], finalPos[1]);
        //find all possible destination tiles for a given piece
        possibleDestinationTiles = genAttackSideList(finalPos, initialPos, testPiece.getMoves(), board);
        for (int i = 0; i < possibleDestinationTiles.size(); i++) {
            board[possibleDestinationTiles.get(i)[0]][possibleDestinationTiles.get(i)[1]] = new Piece(0, possibleDestinationTiles.get(i)[0], possibleDestinationTiles.get(i)[1]);
        }

        System.out.println("\n\nMoves: " + testPiece.getMoves() + "\tSize: " + possibleDestinationTiles.size());
        printBoard(board);
    }

    //private helper method to help in testing
    public static void printBoard(Piece[][] board) {
        for (int i = 0; i < board.length; i++) {
            System.out.println();
            for (int j = 0; j < board[0].length; j++) {
                if (board[i][j] == null) {
                    System.out.print(". ");
                } else {
                    System.out.print(board[i][j].toString() + " ");
                }
            }
        }
    }

    //private helper method--> returns true if finalPosition is reachable from initialPosition, false otherwise
    //***To maintain consistenty with the indexing format of 2D arrays (row, column), 
    //      COORDINATES FOR POSITION OF A PIECE ON THE BOARD SHOULD BE GIVEN IN THE FORM (Y, X)*** 
    private static boolean determineAccessibility(int initialPos[], int finalPos[], int moves, Piece board[][]) {
        if (initialPos[0] == finalPos[0] && initialPos[1] == finalPos[1]) { //base case--> piece has reached destination
            return true;
        } else {      //inductive case
            //declare and initialise required variables
            //for each path taken (approaching from the left/right/up/down), have a new copy of moves--> trying out one path 
            //should not affect the number of available moves left for the piece for another path
            int leftMoves = moves, rightMoves = moves, upMoves = moves, downMoves = moves;

            //figure out minimum number of moves required to reach destination
            int diff = compDiff(initialPos, finalPos);

            //try moving to each adjacent tile if it leads to the destination tile (and is unoccupied)
            int upPos[] = {(initialPos[0] - 1), initialPos[1]};
            int downPos[] = {(initialPos[0] + 1), initialPos[1]};
            int rightPos[] = {(initialPos[0]), initialPos[1] + 1};
            int leftPos[] = {(initialPos[0]), initialPos[1] - 1};

            //LEFT
            if (leftPos[0] >= 0 && leftPos[0] < board.length && leftPos[1] >= 0 && leftPos[1] < board[0].length && compDiff(leftPos, finalPos) < moves && (board[leftPos[0]][leftPos[1]] == null || (leftPos[0] == finalPos[0] && leftPos[1] == finalPos[1]))) {
                leftMoves--;    //decrement number of moves left
                if (determineAccessibility(leftPos, finalPos, leftMoves, board)) { //recursive call
                    return true;
                }
            }

            //RIGHT
            if (rightPos[0] >= 0 && rightPos[0] < board.length && rightPos[1] >= 0 && rightPos[1] < board[0].length && compDiff(rightPos, finalPos) < moves && (board[rightPos[0]][rightPos[1]] == null || (rightPos[0] == finalPos[0] && rightPos[1] == finalPos[1]))) {
                rightMoves--;    //decrement number of moves left
                if (determineAccessibility(rightPos, finalPos, rightMoves, board)) { //recursive call
                    return true;
                }
            }

            //UP
            if (upPos[0] >= 0 && upPos[0] < board.length && upPos[1] >= 0 && upPos[1] < board[0].length && compDiff(upPos, finalPos) < moves && (board[upPos[0]][upPos[1]] == null || (upPos[0] == finalPos[0] && upPos[1] == finalPos[1]))) {
                upMoves--;    //decrement number of moves left
                if (determineAccessibility(upPos, finalPos, upMoves, board)) { //recursive call
                    return true;
                }
            }

            //DOWN
            if (downPos[0] >= 0 && downPos[0] < board.length && downPos[1] >= 0 && downPos[1] < board[0].length && compDiff(downPos, finalPos) < moves && (board[downPos[0]][downPos[1]] == null || (downPos[0] == finalPos[0] && downPos[1] == finalPos[1]))) {
                downMoves--;    //decrement number of moves left
                if (determineAccessibility(downPos, finalPos, downMoves, board)) { //recursive call
                    return true;
                }
            }

            return false;   //no possible path to destination tile
        }
    }

    //private helper method; returns the distance (in terms of horizontal/vertical paths; NOT diagonal/straight line distance)
    //between a piece's current position on the board and a given destination.
    private static int compDiff(int[] currentPos, int[] finalPos) {
        return Math.abs(finalPos[0] - currentPos[0]) + Math.abs(finalPos[1] - currentPos[1]);
    }

    //returns an ArrayList (of int[] arrays) containing all possible positions that can be reached from the initial position, 
    //given the number of moves the piece if capable of moving in a turn  
    //***To maintain consistenty with the indexing format of 2D arrays (row, column), 
    //      COORDINATES FOR POSITION OF A PIECE ON THE BOARD SHOULD BE GIVEN IN THE FORM (Y, X)***  
    public static ArrayList<int[]> genDestinationList(int initialPos[], int moves, int player, Piece board[][]) {
        //declare and initialise required variables
        int diff;
        ArrayList<int[]> possibleDestinations = new ArrayList<int[]>();

        //iterate over all tiles in board--> all possible positions
        for (int i = 0; i < board.length; i++) {
            for (int j = 0; j < board[0].length; j++) {
                int[] finalPos = {i, j};    //make sure to declare & initialise a new array every time (reference variable)
                diff = compDiff(initialPos, finalPos);  //compute distance (number of tiles) between finalPos & initialPos

                //make sure destination tile is either empty or contains an enemy piece and is accessible
                //***EACH PIECE OBJECT SHOULD HAVE AN ATTRIBUTE "int player" (1 OR 2) AND AN ACCESSOR ".getplayer()"***
                //***NOT CHECKING FOR TERRAIN***
                if ((board[finalPos[0]][finalPos[1]] == null || board[finalPos[0]][finalPos[1]].getPlayer() != player) && diff <= moves && (finalPos[0] != initialPos[0] || finalPos[1] != initialPos[1])) {
                    if (determineAccessibility(initialPos, finalPos, moves, board) == true) {
                        //add position to destination list
                        possibleDestinations.add(finalPos);
                    }
                }
            }
        }
        return possibleDestinations;
    }

    //returns an ArrayList (of int[] arrays) containing all possible sides (tiles adjacent to finalPos) from which a piece
    //can attack another piece (maximum 4) 
    //***To maintain consistenty with the indexing format of 2D arrays (row, column), 
    //      COORDINATES FOR POSITION OF A PIECE ON THE BOARD SHOULD BE GIVEN IN THE FORM (Y, X)*** 
    public static ArrayList<int[]> genAttackSideList(int finalPos[], int initialPos[], int moves, Piece[][] board) {
        //declare and initialse arrayList
        ArrayList<int[]> finalAttackSideList = new ArrayList<int[]>();
        int leftPos[] = new int[2], rightPos[] = new int[2], downPos[] = new int[2], upPos[] = new int[2];
        int possibleAttackSideList[][] = {leftPos, rightPos, downPos, upPos};
        //System.out.println("Initial Pos: " + Arrays.toString(initialPos) + "  finalPos: " + Arrays.toString(finalPos) + "  Moves: " + moves);
        //figure out coordinates of tiles on all 4 sides of finalPos
        leftPos[0] = finalPos[0] - 1;
        leftPos[1] = finalPos[1];

        rightPos[0] = finalPos[0] + 1;
        rightPos[1] = finalPos[1];

        downPos[0] = finalPos[0];
        downPos[1] = finalPos[1] + 1;

        upPos[0] = finalPos[0];
        upPos[1] = finalPos[1] - 1;

        for (int i = 0; i < possibleAttackSideList.length; i++) {
            //check if each destination tile is within bounds of board and is empty
            if (possibleAttackSideList[i][0] >= 0 && possibleAttackSideList[i][0] < board.length && possibleAttackSideList[i][1] >= 0 && possibleAttackSideList[i][1] < board[0].length && board[possibleAttackSideList[i][0]][possibleAttackSideList[i][1]] == null) {
                //System.out.println("AttackSide: [" + possibleAttackSideList[i][0] + "," + possibleAttackSideList[i][1] + "]" );
                //determine if a path exists to the destination tile
                if (determineAccessibility(initialPos, possibleAttackSideList[i], moves - 1, board)) {
                    //add position to list
                    finalAttackSideList.add(possibleAttackSideList[i]);
                }
            }
        }
        return finalAttackSideList;
    }
}