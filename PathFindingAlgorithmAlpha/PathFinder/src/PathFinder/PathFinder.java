/*
 * Karan Garg
 * May 26, 2014
 * Algorithm for figuring out all possible paths the player can choose to make a piece reach a destination tile, 
 * considering the number of moves the piece is capable of moving in one turn.
 */
package PathFinder;
import java.util.ArrayList;
import java.util.Arrays;

public class PathFinder {
    private static ArrayList<int[]> paths = new ArrayList<int[]>();
    
    public static void main(String[] args) {
        //construct a board
        char grid [][] = new char[8][12];
        for (int i = 0; i < grid.length; i++){
            for (int j = 0; j < grid[0].length; j++){
                grid[i][j] = '.';
            }
        }
        
        //place some pieces around the board
        grid[0][3] = 'X';
        grid[1][5] = 'X';
        grid[2][6] = 'X';
        grid[5][7] = 'X';
        grid[1][3] = 'X';
        grid[6][0] = 'X';
        grid[7][7] = 'X';
        grid[6][4] = 'X';
        //define initial position and destination
        int initialPos[] = {5, 4};
        int finalPos[] = {2, 3};
        int moves = 6;
        pathFinder(initialPos, finalPos, moves, grid);
        for (int i = 0; i < paths.size(); i++){
            System.out.print(Arrays.toString(paths.get(i)) + "  ");
        }
        printGrid(grid);
       
    }
    
    public static void printGrid(char [][] grid){
         for (int i = 0; i < grid.length; i++){
             System.out.println();
            for (int j = 0; j < grid[0].length; j++){
                System.out.print(grid[i][j] + " ");
            }
        }
    }
    
    public static int[] pathFinder(int initialPos[], int finalPos[], int moves, char grid[][]){
        if (initialPos == finalPos){
            return initialPos;
        }
        else{
        //figure out number of moves required to reach destination
        int diff = compDiff(initialPos, finalPos);
        
        //try moving to each adjacent tile, if possible
        int leftPos[] = {(initialPos[0]-1), initialPos[1]};
        int rightPos[] = {(initialPos[0]+1), initialPos[1]};
        int downPos[] = {(initialPos[0]), initialPos[1]-1};
        int upPos[] = {(initialPos[0]-1), initialPos[1]+1};
        
        if (leftPos[0] >= 0 && leftPos[0] < grid.length && leftPos[1] >= 0 && leftPos[1] < grid[0].length && compDiff(leftPos, finalPos) < moves && grid[leftPos[0]][leftPos[1]] == '.'){
            moves--;
            paths.add(pathFinder(leftPos, finalPos, moves, grid));       
    }
        if (rightPos[0] >= 0 && rightPos[0] < grid.length && rightPos[1] >= 0 && rightPos[1] < grid[0].length && compDiff(rightPos, finalPos) < moves && grid[rightPos[0]][rightPos[1]] == '.'){
            moves--;
            paths.add(pathFinder(rightPos, finalPos, moves, grid));       
    }
        if (downPos[0] >= 0 && downPos[0] < grid.length && downPos[1] >= 0 && downPos[1] < grid[0].length && compDiff(downPos, finalPos) < moves && grid[downPos[0]][downPos[1]] == '.'){
            moves--;
            paths.add(pathFinder(downPos, finalPos, moves, grid));       
    }
        if (upPos[0] >= 0 && upPos[0] < grid.length && upPos[1] >= 0 && upPos[1] < grid[0].length && compDiff(upPos, finalPos) < moves && grid[upPos[0]][upPos[1]] == '.'){
            moves--;
            paths.add(pathFinder(upPos, finalPos, moves, grid));       
    }
     
    }
        return initialPos;
    
    }
    private static int compDiff(int[] currentPos, int[] finalPos){
        return (finalPos[0] - currentPos[0] + finalPos[1] - currentPos[1]);
    }
}
