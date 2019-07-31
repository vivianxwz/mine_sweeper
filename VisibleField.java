// author: vivianxwz(github)



/**
  VisibleField class
  This is the data that's being displayed at any one point in the game (i.e., visible field, because it's what the
  user can see about the minefield), Client can call getStatus(row, col) for any square.
  It actually has data about the whole current state of the game, including  
  the underlying mine field (getMineField()).  Other accessors related to game status: numMinesLeft(), isGameOver().
  It also has mutators related to actions the player could do (resetGameDisplay(), cycleGuess(), uncover()),
  and changes the game state accordingly.
  
  It, along with the MineField (accessible in mineField instance variable), forms
  the Model for the game application, whereas GameBoardPanel is the View and Controller, in the MVC design pattern.
  It contains the MineField that it's partially displaying.  That MineField can be accessed (or modified) from 
  outside this class via the getMineField accessor.  
 */
public class VisibleField {
   // ----------------------------------------------------------   
   // The following public constants (plus numbers mentioned in comments below) are the possible states of one
   // location (a "square") in the visible field (all are values that can be returned by public method 
   // getStatus(row, col)).
   
   // Covered states (all negative values):
   public static final int COVERED = -1;   // initial value of all squares
   public static final int MINE_GUESS = -2;
   public static final int QUESTION = -3;

   // Uncovered states (all non-negative values):
   
   // values in the range [0,8] corresponds to number of mines adjacent to this square
   
   public static final int MINE = 9;      // this loc is a mine that hasn't been guessed already (end of losing game)
   public static final int INCORRECT_GUESS = 10;  // is displayed a specific way at the end of losing game
   public static final int EXPLODED_MINE = 11;   // the one you uncovered by mistake (that caused you to lose)
   // ----------------------------------------------------------   
  
   // <put instance variables here>
   private MineField visibleMineField;
   private int[][] mineStatus;
   private int rowsNum;
   private int colsNum;
   private int mineNum;
   private int mineGuessNum;
   private boolean failStatus;
   

   /**
      Create a visible field that has the given underlying mineField.
      The initial state will have all the mines covered up, no mines guessed, and the game
      not over.
      @param mineField  the minefield to use for for this VisibleField
    */
   public VisibleField(MineField mineField) {
	  
	  visibleMineField = mineField;
	  
	  rowsNum = visibleMineField.numRows();
	  colsNum = visibleMineField.numCols();
      
      mineStatus = new int[rowsNum][colsNum];
      for(int i = 0; i < rowsNum; i++) {
    	 for(int j = 0; j < colsNum; j++) {
    		mineStatus[i][j] = COVERED;
    	 }
      }
      mineGuessNum = 0;
      mineNum = visibleMineField.numMines();
      failStatus = false;
      
   }
   
   
   /**
      Reset the object to its initial state (see constructor comments), using the same underlying
      MineField. 
   */     
   public void resetGameDisplay() {
	  
	  visibleMineField.resetEmpty();
	      
	  for(int i = 0; i < rowsNum; i++) {
	     for(int j = 0; j < colsNum; j++) {
	    	mineStatus[i][j] = COVERED;
	     }
	  }
	  
	  mineGuessNum = 0;
      mineNum = visibleMineField.numMines();
      failStatus = false;
      
   }
  
   
   /**
      Returns a reference to the mineField that this VisibleField "covers"
      @return the minefield
    */
   public MineField getMineField() {
      return visibleMineField;       
   }
   
   
   /**
      Returns the visible status of the square indicated.
      @param row  row of the square
      @param col  col of the square
      @return the status of the square at location (row, col).  See the public constants at the beginning of the class
      for the possible values that may be returned, and their meanings.
      PRE: getMineField().inRange(row, col)
    */
   public int getStatus(int row, int col) {
	  assert getMineField().inRange(row,col);
      return mineStatus[row][col];       
   }

   
   /**
      Returns the the number of mines left to guess.  This has nothing to do with whether the mines guessed are correct
      or not.  Just gives the user an indication of how many more mines the user might want to guess.  So the value can
      be negative, if they have guessed more than the number of mines in the minefield.     
      @return the number of mines left to guess.
    */
   public int numMinesLeft() {
	  mineGuessNum = 0;
	  for(int i = 0; i < rowsNum; i++) {
		 for(int j = 0; j < colsNum; j++) {
		    if(mineStatus[i][j] == MINE_GUESS) {
		       mineGuessNum++;
		    }
		 }
	  }
      return mineNum - mineGuessNum;       

   }
 
   
   /**
      Cycles through covered states for a square, updating number of guesses as necessary.  Call on a COVERED square
      changes its status to MINE_GUESS; call on a MINE_GUESS square changes it to QUESTION;  call on a QUESTION square
      changes it to COVERED again; call on an uncovered square has no effect.  
      @param row  row of the square
      @param col  col of the square
      PRE: getMineField().inRange(row, col)
    */
   public void cycleGuess(int row, int col) {
	   
      assert getMineField().inRange(row,col);
      if(mineStatus[row][col] == COVERED) {
    	 mineStatus[row][col] = MINE_GUESS;
      }else if(mineStatus[row][col] == MINE_GUESS) {
    	 mineStatus[row][col] = QUESTION;
      }else if(mineStatus[row][col] == QUESTION) {
    	 mineStatus[row][col] =COVERED;
      }
      
   }

   
   /**
      Uncovers this square and returns false iff you uncover a mine here.
      If the square wasn't a mine or adjacent to a mine it also uncovers all the squares in 
      the neighboring area that are also not next to any mines, possibly uncovering a large region.
      Any mine-adjacent squares you reach will also be uncovered, and form 
      (possibly along with parts of the edge of the whole field) the boundary of this region.
      Does not uncover, or keep searching through, squares that have the status MINE_GUESS. 
      Note: this action may cause the game to end: either in a win (opened all the non-mine squares)
      or a loss (opened a mine).
      @param row  of the square
      @param col  of the square
      @return false   iff you uncover a mine at (row, col)
      PRE: getMineField().inRange(row, col)
    */
   public boolean uncover(int row, int col) {
	  if(visibleMineField.hasMine(row,col)) {
		 mineStatus[row][col] = EXPLODED_MINE;
		 failStatus = true;
		 return false;
	  }else {
		 uncoverHelper(row, col);
		 return true;
	  }
         
   }
 
   
   /**
      Returns whether the game is over.
      (Note: This is not a mutator.)
      @return whether game over
    */
   public boolean isGameOver() {
	  if(failStatus == true) {
		 loseCondition();
		 return true;
	  }
	  int countMine = 0;
	  int countNoMine = 0;
	  for(int i = 0; i < rowsNum; i++) {
		 for(int j = 0; j < colsNum; j++) {
			if(visibleMineField.hasMine(i,j)) {
			   if(mineStatus[i][j] == COVERED || mineStatus[i][j] == MINE_GUESS) {
				  countMine++;
			   }
			}else {
			   if(mineStatus[i][j] == COVERED || mineStatus[i][j] == MINE_GUESS) {
				  countNoMine++;
			   }
			}
		 }
	  }
	  if(countMine == mineNum && countNoMine == 0) {
		 winCondition();
		 return true;
	  }
	  return false;
            
   }
 
   
   /**
      Returns whether this square has been uncovered.  (i.e., is in any one of the uncovered states, 
      vs. any one of the covered states).
      @param row of the square
      @param col of the square
      @return whether the square is uncovered
      PRE: getMineField().inRange(row, col)
    */
   public boolean isUncovered(int row, int col) {
	  assert getMineField().inRange(row,col);
	  if(mineStatus[row][col] == COVERED) {
		 return false;
	  }
	  if(mineStatus[row][col] == MINE_GUESS) {
		 return false;
	  }
	  if(mineStatus[row][col] == QUESTION) {
		 return false;
	  }
      return true;       
   }
   
 
   // <put private methods here>
   private void uncoverHelper(int row, int col) {
	  //check corner case first
	  if(visibleMineField.inRange(row,col) == false) {
		 return;
	  }
	  if(mineStatus[row][col] == MINE_GUESS) {
		 return;
	  }
	  if(visibleMineField.hasMine(row,col)) {
		 return;
	  }
	  if(isUncovered(row,col)) {
		 return;
	  }
	  //the 8 direction based on the current position
	  int[] xDirection = {-1, -1, -1, 0, 0, 1, 1, 1};
	  int[] yDirection = {-1, 0, 1, -1, 1, -1, 0, 1};
      int curX;
      int curY;
	  int searchRange = xDirection.length;
	  //check the current position
      mineStatus[row][col] = visibleMineField.numAdjacentMines(row,col);
      
	  //check the surrouding position
	  if(mineStatus[row][col] == 0) {
		 for(int i = 0; i < searchRange; i++) {
			curX = row + xDirection[i];
			curY = col + yDirection[i];
			uncoverHelper(curX, curY);
		 }
	  }
   }
   
   
   //set lose the game condition
   private void loseCondition() {
	  for(int i = 0; i < rowsNum; i++) {
		 for(int j = 0; j < colsNum; j++) {
            //check not guessed position, whether it has mine or not
			if(mineStatus[i][j] == COVERED || getStatus(i,j) == QUESTION) { 
			   if(visibleMineField.hasMine(i,j)) {
				  mineStatus[i][j] = MINE;
			   }
			}
            //check the guess place whether the guess is correct or not
			if(mineStatus[i][j] == MINE_GUESS) { 
			   if(visibleMineField.hasMine(i,j) == false) {
				  mineStatus[i][j] = INCORRECT_GUESS;
			   }
			}
		 }
	  }
   }
   
   //set win the game condition
   private void winCondition() {
	  for(int i = 0; i < rowsNum; i++) {
		 for(int j = 0; j < colsNum; j++){
            //in the win condition, no matter COVERED or MINE_GUESS, just set to MINE_GUESS
            if(visibleMineField.hasMine(i,j)) { 
			   mineStatus[i][j] = MINE_GUESS;
		    }
	     }
      }
   }

   
}
