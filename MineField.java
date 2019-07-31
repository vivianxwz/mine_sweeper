// Name:Xiaowen Zhang
// USC NetID:zhan204
// CS 455 PA3
// Spring 2019


import java.util.*;
/** 
   MineField
      class with locations of mines for a game.
      This class is mutable, because we sometimes need to change it once it's created.
      mutators: populateMineField, resetEmpty
      includes convenience method to tell the number of mines adjacent to a location.
 */
public class MineField {
   
   // <put instance variables here>
   private boolean[][] mineArray;
   private int numOfMines;
   
   
   /**
      Create a minefield with same dimensions as the given array, and populate it with the mines in the array
      such that if mineData[row][col] is true, then hasMine(row,col) will be true and vice versa.  numMines() for
      this minefield will corresponds to the number of 'true' values in mineData.
    * @param mineData  the data for the mines; must have at least one row and one col.
    */
   public MineField(boolean[][] mineData) {
	   
	  int arrayRow = mineData.length;
	  int arrayCol = mineData[0].length;
	  mineArray = new boolean[arrayRow][arrayCol];
	  
      for(int i = 0; i < arrayRow; i++) {
    	 for(int j = 0; j < arrayCol; j++) {
    		mineArray[i][j] = mineData[i][j];
    	    if(mineArray[i][j]) {
    	       numOfMines++;
    	    }
    	 }
      }
   }
   
   
   /**
      Create an empty minefield (i.e. no mines anywhere), that may later have numMines mines (once 
      populateMineField is called on this object).  Until populateMineField is called on such a MineField, 
      numMines() will not correspond to the number of mines currently in the MineField.
      @param numRows  number of rows this minefield will have, must be positive
      @param numCols  number of columns this minefield will have, must be positive
      @param numMines   number of mines this minefield will have,  once we populate it.
      PRE: numRows > 0 and numCols > 0 and 0 <= numMines < (1/3 of total number of field locations). 
    */
   public MineField(int numRows, int numCols, int numMines) {
	  
	  numOfMines = numMines;
	  mineArray = new boolean[numRows][numCols];
      
   }
   

   /**
      Removes any current mines on the minefield, and puts numMines() mines in random locations on the minefield,
      ensuring that no mine is placed at (row, col).
      @param row the row of the location to avoid placing a mine
      @param col the column of the location to avoid placing a mine
      PRE: inRange(row, col)
    */
   public void populateMineField(int row, int col) {
	  resetEmpty();
	  
      Random rand = new Random();
      int chooseRow;
      int chooseCol;
      int totalRows = mineArray.length;
      int totalColumn = mineArray[0].length;
      int loopCount = 0;
      while(loopCount < numOfMines) {
         //choose which row to have the mine
    	 chooseRow = rand.nextInt(totalRows);
         //choose which column to have the mine
    	 chooseCol = rand.nextInt(totalColumn);
    	 if((chooseRow != row && chooseCol != col) && hasMine(chooseRow, chooseCol) == false) {
    		loopCount++;
        	mineArray[chooseRow][chooseCol] = true;
    	 }
      }
   }
   
   
   /**
      Reset the minefield to all empty squares.  This does not affect numMines(), numRows() or numCols()
      Thus, after this call, the actual number of mines in the minefield does not match numMines().  
      Note: This is the state the minefield is in at the beginning of a game.
    */
   public void resetEmpty() {
	  
	  int mineRow = mineArray.length;
	  int mineCol = mineArray[0].length;
	  
      for(int i = 0; i < mineRow; i++) {
    	 for(int j = 0; j < mineCol; j++) {
    		mineArray[i][j] = false;
    	 }
      }
   }

   
  /**
     Returns the number of mines adjacent to the specified mine location (not counting a possible 
     mine at (row, col) itself).
     Diagonals are also considered adjacent, so the return value will be in the range [0,8]
     @param row  row of the location to check
     @param col  column of the location to check
     @return  the number of mines adjacent to the square at (row, col)
     PRE: inRange(row, col)
   */
   public int numAdjacentMines(int row, int col) {
	  assert inRange(row,col);
	  int count = 0;
	  int curPositionX;
	  int curPositionY;
      // the index of x and y position
	  int[] xPosition = new int[] {-1, -1, -1, 0, 0, 1, 1, 1};
	  int[] yPosition = new int[] {-1, 0, 1, -1, 1, -1, 0, 1};
	  int length = xPosition.length;
	  for(int i = 0; i < length; i++) {
		 curPositionX = row + xPosition[i];
		 curPositionY = col + yPosition[i];
		 if(inRange(curPositionX, curPositionY) && mineArray[curPositionX][curPositionY]) {
			count++;
		 }
	  }
      return count;       // DUMMY CODE so skeleton compiles
   }
   
   /**
      Returns true iff (row,col) is a valid field location.  Row numbers and column numbers
      start from 0.
      @param row  row of the location to consider
      @param col  column of the location to consider
      @return whether (row, col) is a valid field location
   */
   public boolean inRange(int row, int col) {
	  if(row < 0 || row >= mineArray.length) {
		 return false;
	  }
	  if(col < 0 || col >= mineArray[0].length) {
		 return false;
	  }
      return true;       // DUMMY CODE so skeleton compiles
   }
   
   
   /**
      Returns the number of rows in the field.
      @return number of rows in the field
   */  
   public int numRows() {
      return mineArray.length;       // DUMMY CODE so skeleton compiles
   }
   
   
   /**
      Returns the number of columns in the field.
      @return number of columns in the field
   */    
   public int numCols() {
      return mineArray[0].length;       // DUMMY CODE so skeleton compiles
   }
   
   
   /**
      Returns whether there is a mine in this square
      @param row  row of the location to check
      @param col  column of the location to check
      @return whether there is a mine in this square
      PRE: inRange(row, col)   
   */    
   public boolean hasMine(int row, int col) {
      assert inRange(row,col);
      if(mineArray[row][col] == true) {
    	 return true;
      }
	  return false;       // DUMMY CODE so skeleton compiles
   }
   
   
   /**
      Returns the number of mines you can have in this minefield.  For mines created with the 3-arg constructor,
      some of the time this value does not match the actual number of mines currently on the field.  See doc for that
      constructor, resetEmpty, and populateMineField for more details.
    * @return
    */
   public int numMines() {
      return numOfMines;       // DUMMY CODE so skeleton compiles
   }

   
   // <put private methods here>
   
         
}

