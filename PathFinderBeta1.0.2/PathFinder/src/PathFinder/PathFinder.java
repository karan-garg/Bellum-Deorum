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
    static int count = 0;
    static boolean flag = false;
    public static void main(String[] args) {
        //construct a board
        char grid [][] = new char[8][12];
        for (int i = 0; i < grid.length; i++){
            for (int j = 0; j < grid[0].length; j++){
                grid[i][j] = '.';
            }
        }
        
        //place some pieces around the board
        grid[3][5] = 'X';
        grid[1][5] = 'X';
        grid[2][6] = 'X';
        grid[5][7] = 'X';
        grid[1][3] = 'X';
        grid[6][0] = 'X';
        grid[7][7] = 'X';
        grid[6][4] = 'X';
        //define initial position and destination
        int initialPos[] = {5, 4};
        grid[4][5] = 'I';
        
        int finalPos[] = {3, 0};
        int moves = 3;
        ArrayList<int[]> destTiles = compWithinReach(initialPos, moves, grid);
        System.out.println(destTiles);
         for (int i = 0; i < destTiles.size(); i++){
            System.out.println(Arrays.toString(destTiles.get(i)));
            grid[destTiles.get(i)[1]][destTiles.get(i)[0]] = 'D';
        }
          for (int i = 0; i < destTiles.size(); i++){
            paths = new ArrayList<int[]>();
            pathFinder(initialPos, destTiles.get(i), moves, grid);
            if (paths.size() > 0){
                grid[destTiles.get(i)[1]][destTiles.get(i)[0]] = '*';
            }
        }
//        pathFinder(initialPos, finalPos, moves, grid);
//        System.out.println(paths);
//        for (int i = 0; i < paths.size(); i++){
//            System.out.print(Arrays.toString(paths.get(i)) + "  ");
//        }
//        printGrid(grid);
//       
//        for (int i = 0; i < paths.size(); i++){
//            grid[paths.get(i)[1]][paths.get(i)[0]] = '*';
//        }
//        System.out.println("\n");
        grid[0][3] = 'F';
        grid[4][5] = 'I';
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
    
    public static void pathFinder(int initialPos[], int finalPos[], int moves, char grid[][]){
        //System.out.println("Current Position: " + Arrays.toString(initialPos));
        if (initialPos[0] == finalPos[0] && initialPos[1] == finalPos[1]){
            flag = true;
        }
        else{
        //figure out number of moves required to reach destination
        int diff = compDiff(initialPos, finalPos);
       
        //try moving to each adjacent tile, if possible
        int leftPos[] = {(initialPos[0]-1), initialPos[1]};
        int rightPos[] = {(initialPos[0]+1), initialPos[1]};
        int downPos[] = {(initialPos[0]), initialPos[1]+1};
        int upPos[] = {(initialPos[0]), initialPos[1]-1};
        
        int leftMoves = moves, rightMoves = moves, upMoves = moves, downMoves = moves;
        
        if (leftPos[0] >= 0 && leftPos[0] < grid[0].length && leftPos[1] >= 0 && leftPos[1] < grid.length && compDiff(leftPos, finalPos) < moves && grid[leftPos[1]][leftPos[0]] != 'X'){
            flag = false;
            leftMoves--;
                            System.out.println("Current Pos: " + Arrays.toString(leftPos) + compDiff(leftPos, finalPos));
            pathFinder(leftPos, finalPos, leftMoves, grid);
            if (flag){
                paths.add(leftPos);
                
            }     
    }
        
        if (upPos[0] >= 0 && upPos[0] < grid[0].length && upPos[1] >= 0 && upPos[1] < grid.length && compDiff(upPos, finalPos) < moves && grid[upPos[1]][upPos[0]] != 'X'){
            flag = false;
            count++;
            // System.out.println("Current Pos: " + Arrays.toString(upPos));
            upMoves--;
            pathFinder(upPos, finalPos, upMoves, grid);
            if (flag){
                paths.add(upPos);
                
        }
        }
        
        if (rightPos[0] >= 0 && rightPos[0] < grid[0].length && rightPos[1] >= 0 && rightPos[1] < grid.length && compDiff(rightPos, finalPos) < moves && grid[rightPos[1]][rightPos[0]] != 'X'){
            flag = false;
            rightMoves--;
            pathFinder(rightPos, finalPos, rightMoves, grid);
           if (flag){
                paths.add(rightPos);
                
            }    
      }
        if (downPos[0] >= 0 && downPos[0] < grid[0].length && downPos[1] >= 0 && downPos[1] < grid.length && compDiff(downPos, finalPos) < moves && grid[downPos[1]][downPos[0]] != 'X'){
           flag = false;
           downMoves--;
            pathFinder(downPos, finalPos, downMoves, grid);
            if (flag){
                paths.add(downPos);
                
       
            }   
    }
        
     
    
    }
        }
    private static int compDiff(int[] currentPos, int[] finalPos){
        return Math.abs(finalPos[0] - currentPos[0]) + Math.abs(finalPos[1] - currentPos[1]);
    }
    
    public static ArrayList <int[]> compWithinReach(int initialPos[], int moves, char grid[][]){
        int diff;
        int count = 0;
        ArrayList <int[]> possibleDestinations = new ArrayList<int[]>();
        for (int i = 0; i < grid.length; i++){
            for (int j = 0; j < grid[0].length; j++){
                int[] finalPos = {j, i};
                count++;
               // System.out.print(Arrays.toString(finalPos) + count);
                diff = compDiff(initialPos, finalPos);
                
                if (grid[finalPos[1]][finalPos[0]] != 'X' && diff <= moves && (finalPos[0] != initialPos[0] || finalPos[1] != initialPos[1])){
                    //System.out.println(Arrays.toString(finalPos));
                    possibleDestinations.add(finalPos);
                }
            }
            
        }
        return possibleDestinations;
    }
}
