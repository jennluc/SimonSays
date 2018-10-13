/*
 * Name: Abena Bonsu
 * Login: cs8bwalo
 * Date: February 4, 2016
 * File: Board.java
 * Sources of Help: textbook, tutors
 * 
 * File Name: Board.java
 * Names: Elton Chan cs8bshx / Wesley Kwan cs8bsin
 * Emails: elc044 / wekwan
 * Date: 5/7/17
 * References Used: internet and tutors
 *
 * Describe what the program does here:
 * This Class is used to construct a Board object to be used
 * for the simulation of the game 2048. It can create a fresh
 * board or load an already existing board. In addition this
 * class allows the user to save their current game to a new, 
 * specified file. The class also allows for the board to be 
 * rotated 90 degrees to the right or left. Based on the direction
 * passed in by the user, this class will then move tiles
 * existing on the board in a certain direction, combining tiles
 * of the same value. The game is considered to be over when
 * the board cannot move in any direction.
 * 
 * File Header:
 * Class Header:
 * 
 * /

//------------------------------------------------------------------//
// Board.java                                                       //
//                                                                  //
// Class used to represent a 2048 game board                        //
//                                                                  //
// Author:  W16-CSE8B-TA group                                      //
// Date:    1/17/16                                                 //
//------------------------------------------------------------------//

/**
 * Sample Board
 * <p/>
 * 0   1   2   3
 * 0   -   -   -   -
 * 1   -   -   -   -
 * 2   -   -   -   -
 * 3   -   -   -   -
 * <p/>
 * The sample board shows the index values for the columns and rows
 * Remember that you access a 2D array by first specifying the row
 * and then the column: grid[row][column]
 */

import java.util.Random;

public class Board {
    /**
     * Number of tiles showing when the game starts
     */
    public final int NUM_START_TILES = 2;

    /**
     * The probability (times 100) that a randomly
     * generated tile will be a 2 (vs a 4)
     */
    public final int TWO_PROBABILITY = 90;

    /**
     * The size of the grid
     */
    public final int GRID_SIZE;

    private int[][] grid;  // The grid of tile values
    private int score;     // The current score

    // You do not have to use these variables
    private final Random random;    // A random number generator (for testing)

    /**
     * Name: Board(Random random, int boardSize).
     * 
     * Purpose: The purpose of this method is to create or construct a fresh
     * board for the user with two random tiles places within the board. This
     * board will have a particular boardSize that the user sets, as well as a
     * random
     *
     * @param boardSize size of the 2048 game board to be used.
     * @param random    Random random represents the random number which 
     *                  be used to specific where (after every move) a 
     *                  new tile should be added to the board when playing.
     */
    public Board(Random random, int boardSize) {
        if (boardSize < 2) boardSize = 4;

        // initialize member variables
        this.random = random;
        this.GRID_SIZE = boardSize;
        this.grid = new int[boardSize][boardSize];
        this.score = 0;

        // loop through and add two initial tiles to the board randomly
        for (int index = 0; index < NUM_START_TILES; index++) {
            addRandomTile();
        }
    }

    /**
     * Constructor used to load boards for grading/testing
     * 
     * THIS IS USED FOR GRADING - DO NOT CHANGE IT
     *
     * @param random
     * @param inputBoard
     */
    public Board(Random random, int[][] inputBoard) {
        this.random = random;
        this.GRID_SIZE = inputBoard.length;
        this.grid = new int[GRID_SIZE][GRID_SIZE];
        for (int r = 0; r < GRID_SIZE; r++) {
            for (int c = 0; c < GRID_SIZE; c++) {
                this.grid[r][c] = inputBoard[r][c];
            }
        }
    }

    /**
     * return the tile value in a particular cell in the grid.
     *
     * @param row The row
     * @param col The column
     * @return The value of the tile at (row, col)
     */
    public int getTileValue(int row, int col) {
        return grid[row][col];
    }

    /**
     * Get the current score
     *
     * @return the current score of the game
     */
    public int getScore() {
        return score;
    }

    /**
     * Name: addRandomTile()
     * 
     * Purpose: The purpose of this method is to add a random tile of either
     * value 2 or 4 to a random empty space on the 2048
     * board. The place where this tile is added is dependent on the random
     * value associated with each board object. If no tiles are empty, it
     * returns without changing the board.
     */
    public void addRandomTile() {
        int count = 0;
        // loop through grid keeping count of every empty space on board
        for (int rowI = 0; rowI < grid.length; rowI++) {
            for (int colI = 0; colI < grid[rowI].length; colI++) {
                if (grid[rowI][colI] == 0) {
                    count++;
                }
            }
        }

        // if count is still 0 after loop, no empty spaces, return
        if (count == 0) {
            System.out.println("There are no empty spaces!");
            return;
        }

        // keep track of where on board random tile should be placed
        int location = random.nextInt(count);
        int value = random.nextInt(100);

        // reset count
        count = 0;
        // loop through grid checking where grid is 0 & incrementing count
        for (int rowI = 0; rowI < grid.length; rowI++) {
            for (int colI = 0; colI < grid[rowI].length; colI++) {
                if (grid[rowI][colI] == 0) {
                    // if count equals random location generated, place tile
                    if (count == location) {
                        System.out.println("Adding a tile to location " + rowI + ", " + colI);
                        if (value < TWO_PROBABILITY) {
                            grid[rowI][colI] = 2;
                        } else {
                            grid[rowI][colI] = 4;
                        }
                    }
                    count++;
                }
            }
        }
    }

    /**
     * Name: isGameOver()
     * <p>
     * Purpose: The purpose of this method is to check whether or not the game
     * in play is over. The game is officially over once there are no longer any
     * valid moves that can be made in any direction. If the game is over, this
     * method will return true and print the words: "Game Over!" This method
     * will be checked before any movement is ever made.
     *
     * @return true if the game is over, and false if the game isn't over
     */
    public boolean isGameOver() {
        return (!canMoveLeft() && !canMoveRight() && !canMoveUp()
                && !canMoveDown());
    }


    /**
     * Name: canMove(Direction direction)
     * 
     * Purpose: The purpose of this method is to check to see if the movement of
     * the tiles in any direction can actually take place. It does not move the
     * tiles, but at every index of the grid, checks to see if there is a tile
     * above, below, to the left or right that has the same value. If this is
     * the case, then that tile can be moved. It also checks if there is an
     * empty (0) tile at a specified index, as this also indicates that movement
     * can be possible. This method is called within move() so that that method
     * can determine whether or not tiles should be moved.
     *
     * @param direction direction the tiles will move (if possible)
     * @return true if the movement can be done and false if it cannot
     */
    public boolean canMove(Direction direction) {
        // utilize helper methods to check if movement in a particular
        // direction is possible
    	if(direction == null) return false;
    	switch (direction) {
            case UP:
                return canMoveUp();
            case RIGHT:
                return canMoveRight();
            case DOWN:
                return canMoveDown();
            case LEFT:
                return canMoveLeft();
            default:
                // If we got here, something went wrong, so return false
                return false;
        }
    }

    /**
     * Name: move(Direction direction)
     * 
     * Purpose: The purpose of this method is to move the tiles in the game
     * board by a specified direction passed in as a parameter. If the movement
     * cannot be done, the method returns false. If the movement can be done, it
     * moves the tiles and returns true. This method relies on the help of four
     * other helper methods to perform the game play.
     *
     * @param direction direction the tiles will move (if possible)
     * @return true if the movement can be done and false if it cannot
     */
    public boolean move(Direction direction) {
        // if canMove is false, exit and don't move tiles
        if (!canMove(direction)) return false;

        // move in relationship to the direction passed in
        switch (direction) {
            case UP:
                moveUp();
                break;
            case RIGHT:
                moveRight();
                break;
            case DOWN:
                moveDown();
                break;
            case LEFT:
                moveLeft();
                break;
            default:
                // This should never happen
                return false;
        }

        return true;
    }

    //TODO: You will implement the methods below this comment as described in
    // the PA writeup

    // TODO: Implement this method and add header comment
    /*
     * Method Header: A helper method that "moves" the tiles of
     * the board to the right. It gets the tile values of the grid
     * and modifies them to make the tiles shift right.
     */
    private void moveRight() {
    	int[][] intArr = new int[GRID_SIZE][GRID_SIZE]; 
    	int[][] newIntArr = new int[GRID_SIZE][GRID_SIZE];
    	// fills intArr with the tile values of the grid
    	for(int row = 0; row < GRID_SIZE; row++) {
    		for(int col = 0; col < GRID_SIZE; col++) {
    			intArr[row][col] = this.getTileValue(row, col);
    		}
    	}
    	// loops through the 2D array going by rows starting from
    	// the right hand side because the rightmost tiles have 
    	// priority when merging right
    	for(int r = 0; r < GRID_SIZE; r++) {
    		for(int c = GRID_SIZE - 1; c > 0; c--) { 
    			// if the value is 0, proceed to the next value of the loop
    			if(intArr[r][c] == 0) {
    				continue;
    			}
    			// if the value is equal to the value on the immediate left,
    			// add them together and set the value of the immediate left
    			// to 0
    			else if(intArr[r][c] == intArr[r][c-1]){  
    				intArr[r][c] = intArr[r][c] + intArr[r][c-1]; 
    				intArr[r][c-1] = 0;
    				this.score = this.score + intArr[r][c];
			
    			}
    			// If the value on the immediate left is 0, searches the row
    			// going left to see if there are more 0's or if there is an
    			// equal value to the current value. If there is an equal value,
    			// they will be added together and the value where the equal value
    			// was will be set to 0. If these conditions are not met, the loop
    			// will proceed to the next value. 
    			else if(intArr[r][c-1] == 0) {  	
    				for(int n = -1; c+n >= 0; n--){
    					if(intArr[r][c+n] == 0) {
    						continue;
    					}
    					else if(intArr[r][c+n] == intArr[r][c]){
    						intArr[r][c] = intArr[r][c+n] + intArr[r][c]; 
    						intArr[r][c+n] = 0;
    						this.score = this.score + intArr[r][c];
    						break;
    					}
    					else {
    						break;
    					}
    				}
    			}
    		}	
    	}	
    	// Places all the values from intArr to newIntArr, shifting 
    	// the values to the right. The loop starts on the right side
    	// and places the values from intArr into newIntArr ignoring
    	// the zeros between values. 
    	for(int i = 0; i < GRID_SIZE; i++){
    		int c2 = GRID_SIZE -1; 	
    		for(int j = GRID_SIZE - 1; j >= 0; j--){ 	
    			if(intArr[i][j] != 0){ 
    				newIntArr[i][c2] = intArr[i][j]; 
    				c2--;
    			}
    		}
    	}
    	// sets the values of the grid to the shifted values
    	// in newIntArr
    	for(int i = 0; i < GRID_SIZE; i++){
    		for(int j = 0; j < GRID_SIZE; j++){
    			this.grid[i][j] = newIntArr[i][j];
    		}
    	} 		
    }

    // TODO: Implement this method and add header comment
    /*
     * Method Header: A helper method that "moves" the tiles of
     * the board to the left. It gets the tile values of the grid
     * and modifies them to make the tiles shift left.
     */
    private void moveLeft() {
    	int[][] intArr = new int[GRID_SIZE][GRID_SIZE]; 
    	int[][] newIntArr = new int[GRID_SIZE][GRID_SIZE];
    	// fills intArr with the tile values of the grid
    	for(int row = 0; row < GRID_SIZE; row++) {
    		for(int col = 0; col < GRID_SIZE; col++) {
    			intArr[row][col] = this.getTileValue(row, col);
    		}
    	}
    	// loops through the 2D array going by rows starting from 
    	// the left hand side because the leftmost tiles have 
    	// priority when merging left
    	for(int r = 0; r < GRID_SIZE; r++) {
    		for(int c = 0; c < GRID_SIZE - 1; c++) { 
    			// if the value is 0, proceed to the next value of the loop
    			if(intArr[r][c] == 0) {
    				continue;
    			}
    			// if the value is equal to the value on the immediate right,
    			// add them together and set the value of the immediate right
    			// to 0
    			else if(intArr[r][c] == intArr[r][c+1]){  
    					intArr[r][c] = intArr[r][c] + intArr[r][c+1]; 
    					intArr[r][c+1] = 0;
    					this.score = this.score + intArr[r][c];
    			
    			}
    			// If the value on the immediate right is 0, searches the row
    			// going right to see if there are more 0's or if there is an
    			// equal value to the current value. If there is an equal value,
    			// they will be added together and the value where the equal value
    			// was will be set to 0. If these conditions are not met, the loop
    			// will proceed to the next value. 
    			else if(intArr[r][c+1] == 0) {  	
    				for(int n = 1; c+n < GRID_SIZE; n++){
    					if(intArr[r][c+n] == 0) {
    						continue;
    					}
    					else if(intArr[r][c+n] == intArr[r][c]){
    						intArr[r][c] = intArr[r][c+n] + intArr[r][c]; 
    						intArr[r][c+n] = 0;
    						this.score = this.score + intArr[r][c];
    						break;
    					}
    					else {
    						break;
    					}
    				}
    		 	}
    		}	
    	}	  
    	// Places all the values from intArr to newIntArr, shifting 
    	// the values to the left. The loop starts on the left side
    	// and places the values from intArr into newIntArr ignoring
    	// the zeros between values. 
    	for(int i = 0; i < GRID_SIZE; i++){
    		int c2 = 0; 	
    		for(int j = 0; j < GRID_SIZE; j++){ 	
    			if(intArr[i][j] != 0){ 
    				newIntArr[i][c2] = intArr[i][j]; 
    				c2++;
    			}
    		}
    	}
    	// sets the values of the grid to the shifted values
    	// in newIntArr
    	for(int i = 0; i < GRID_SIZE; i++){
    		for(int j = 0; j < GRID_SIZE; j++){
    			this.grid[i][j] = newIntArr[i][j];
    		}
    	} 		
     }
    // TODO: Implement this method and add header comment
    /*
     * Method Header: A helper method that "moves" the tiles of
     * the board to the bottom. It gets the tile values of the grid
     * and modifies them to make the tiles shift down.
     */
    private void moveDown() {
    	int[][] intArr = new int[GRID_SIZE][GRID_SIZE]; 
    	int[][] newIntArr = new int[GRID_SIZE][GRID_SIZE];
    	// fills intArr with the tile values of the grid
    	for(int row = 0; row < GRID_SIZE; row++) {
    		for(int col = 0; col < GRID_SIZE; col++) {
    			intArr[row][col] = this.getTileValue(row, col);
    		}
    	}
    	// loops through the 2D array going column-wise starting 
    	// from the bottom because the tiles closest to the 
    	// bottom have priority when merging down
    	for(int c = 0; c < GRID_SIZE; c++) {
    		for(int r = GRID_SIZE - 1; r > 0; r--) { 
    			// if the value is 0, proceed to the next value of the loop
    			if(intArr[r][c] == 0) {
    				continue;
    			}
    			// if the value is equal to the value on the immediate top,
    			// add them together and set the value of the immediate top
    			// to 0
    			else if(intArr[r][c] == intArr[r-1][c]){  
    					intArr[r][c] = intArr[r][c] + intArr[r-1][c]; 
    					intArr[r-1][c] = 0;
    					this.score = this.score + intArr[r][c];
    			}	
    			// If the value on the immediate top is 0, searches the row
    			// going up to see if there are more 0's or if there is an
    			// equal value to the current value. If there is an equal value,
    			// they will be added together and the value where the equal value
    			// was will be set to 0. If these conditions are not met, the loop
    			// will proceed to the next value. 
    			else if(intArr[r-1][c] == 0) {  	
    				for(int n = -1; r+n >= 0; n--){
    					if(intArr[r+n][c] == 0) {
    						continue;
    					}
    					else if(intArr[r+n][c] == intArr[r][c]){
    						intArr[r][c] = intArr[r+n][c] + intArr[r][c]; 
    						intArr[r+n][c] = 0;
    						this.score = this.score + intArr[r][c];
    						break;
    					}
    					else {
    						break;
    					}
    				}
    		 	}
    		}	
    	}	  
    	// Places all the values from intArr to newIntArr, shifting 
    	// the values down. The loop starts on the bottom
    	// and places the values from intArr into newIntArr ignoring
    	// the zeros between values. 
    	for(int j = 0; j < GRID_SIZE; j++){
    		int c2 = GRID_SIZE - 1; 	
    		for(int i = GRID_SIZE - 1; i >= 0; i--){ 	
    			if(intArr[i][j] != 0){ 
    				newIntArr[c2][j] = intArr[i][j]; 
    				c2--;
    			}
    		}
    	}
    	// sets the values of the grid to the shifted values
    	// in newIntArr
    	for(int i = 0; i < GRID_SIZE; i++){
    		for(int j = 0; j < GRID_SIZE; j++){
    			this.grid[i][j] = newIntArr[i][j];
    		}
    	} 		
     }

    // TODO: Implement this method and add header comment
    /*
     * Method Header: A helper method that "moves" the tiles of
     * the board to the top. It gets the tile values of the grid
     * and modifies them to make the tiles shift up.
     */
    private void moveUp() {
    	int[][] intArr = new int[GRID_SIZE][GRID_SIZE]; 
    	int[][] newIntArr = new int[GRID_SIZE][GRID_SIZE];
    	// fills intArr with the tile values of the grid
    	for(int row = 0; row < GRID_SIZE; row++) {
    		for(int col = 0; col < GRID_SIZE; col++) {
    			intArr[row][col] = this.getTileValue(row, col);
    		}
    	}
    	// loops through the 2D array going column-wise starting 
    	// from the top because the tiles closest to the top have 
    	// priority when merging up
    	for(int c = 0; c < GRID_SIZE; c++) {
    		for(int r = 0; r < GRID_SIZE - 1; r++) { 
    			// if the value is 0, proceed to the next value of the loop
    			if(intArr[r][c] == 0) {
    				continue;
    			}
    			// if the value is equal to the value on the immediate bottom,
    			// add them together and set the value of the immediate bottom
    			// to 0
    			else if(intArr[r][c] == intArr[r+1][c]){  
    					intArr[r][c] = intArr[r][c] + intArr[r+1][c]; 
    					intArr[r+1][c] = 0;
    					this.score = this.score + intArr[r][c];
    			
    			}	
    			// If the value on the immediate bottom is 0, searches the row
    			// going down to see if there are more 0's or if there is an
    			// equal value to the current value. If there is an equal value,
    			// they will be added together and the value where the equal value
    			// was will be set to 0. If these conditions are not met, the loop
    			// will proceed to the next value. 
    			else if(intArr[r+1][c] == 0) {  	
    				for(int n = 1; r+n < GRID_SIZE; n++){
    					if(intArr[r+n][c] == 0) {
    						continue;
    					}
    					else if(intArr[r+n][c] == intArr[r][c]){
    						intArr[r][c] = intArr[r+n][c] + intArr[r][c]; 
    						intArr[r+n][c] = 0;
    						this.score = this.score + intArr[r][c];
    						break;
    					}
    					else {
    						break;
    					}
    				}
    		 	}
    			
    		}	
    	}	  
    	// Places all the values from intArr to newIntArr, shifting 
    	// the values to the top. The loop starts on the top
    	// and places the values from intArr into newIntArr ignoring
    	// the zeros between values. 
    	for(int j = 0; j < GRID_SIZE; j++){
    		int c2 = 0; 	
    		for(int i = 0; i < GRID_SIZE; i++){ 	
    			if(intArr[i][j] != 0){ 
    				newIntArr[c2][j] = intArr[i][j]; 
    				c2++;
    			}
    		}
    	}
    	// sets the values of the grid to the shifted values
    	// in newIntArr
    	for(int i = 0; i < GRID_SIZE; i++){
    		for(int j = 0; j < GRID_SIZE; j++){
    			this.grid[i][j] = newIntArr[i][j];
    		}
    	} 
     }

    // TODO: Implement this method and add header comment
    /*
     * Method Header: A helper method to determine if the tiles on the 
     * board can be shifted left. Returns a boolean.
     */
    private boolean canMoveLeft() {
    	// Loops through the board / grid checking each value and its
    	// immediate left value. If the value itself is not a zero and 
    	// the value to its immediate left is either zero or equal to its 
    	// value, then the method will return true. 
    	for(int r = 0; r < GRID_SIZE; r++){
    		for(int c = 1; c < GRID_SIZE; c++){		
    			if(this.getTileValue(r,c) == this.getTileValue(r,c-1) || 
    					this.getTileValue(r,c-1) == 0){
    				if(this.getTileValue(r,c) != 0 
    						|| this.getTileValue(r,c-1) != 0){	
    					return true; 
    				}
    			}
    		}
    	}		
        return false;
    }

    // TODO: Implement this method and add header comment
    /*
     * Method Header: A helper method to determine if the tiles on the 
     * board can be shifted right. Returns a boolean.
     */
    private boolean canMoveRight() {
    	// Loops through the board / grid checking each value and its
        // immediate right value. If the value itself is not a zero and 
        // the value to its immediate right is either zero or equal to its 
        // value, then the method will return true.
    	for(int r = 0; r < GRID_SIZE; r++){
    		for(int c = 0; c < GRID_SIZE-1; c++){
    			if(this.getTileValue(r,c) == this.getTileValue(r,c+1) ||
    					this.getTileValue(r,c+1) == 0){
    				if(this.getTileValue(r,c) != 0 
    						|| this.getTileValue(r,c+1) !=0){
    					return true; 
    				}
    			}
    		}
    	} 
    	return false;
    }

    // TODO: Implement this method and add header comment
    /*
     * Method Header: A helper method to determine if the tiles on the 
     * board can be shifted up. Returns a boolean.
     */
    private boolean canMoveUp() {
    	// Loops through the board / grid checking each value and its
        // immediate top value. If the value itself is not a zero and 
        // the value to its immediate top is either zero or equal to its 
        /// value, then the method will return true.
        for(int r = 1; r < GRID_SIZE; r++){
        	for(int c = 0; c < GRID_SIZE; c++){
        		if(this.getTileValue(r,c) == this.getTileValue(r-1,c) ||
        				this.getTileValue(r-1,c) == 0){
        			if(this.getTileValue(r,c) != 0 
        					|| this.getTileValue(r-1,c) != 0){
        				return true;
        			}
        		}
        	}
        }
        return false;
    }

    // TODO: Implement this method and add header comment
    /*
     * Method Header: A helper method to determine if the tiles on the 
     * board can be shifted down. Returns a boolean.
     */
    private boolean canMoveDown() {
    	// Loops through the board / grid checking each value and its
        // immediate bottom value. If the value itself is not a zero and 
        // the value to its immediate bottom is either zero or equal to its 
        /// value, then the method will return true.
        for(int r = 0; r < GRID_SIZE-1; r++){
        	for(int c = 0; c < GRID_SIZE; c++){
        		if(this.getTileValue(r,c) == this.getTileValue(r+1,c) ||
        				this.getTileValue(r+1,c) == 0){
        			if(this.getTileValue(r,c) != 0 
        					|| this.getTileValue(r+1,c) != 0){
        				return true; 
        			}
        		}
        	}
        }
        return false;
    }


    @Override
    public String toString() {
        StringBuilder outputString = new StringBuilder();
        outputString.append(String.format("Score: %d\n", score));
        for (int row = 0; row < GRID_SIZE; row++) {
            for (int column = 0; column < GRID_SIZE; column++)
                outputString.append(grid[row][column] == 0 ? "    -"
                        : String.format("%5d", grid[row][column]));

            outputString.append("\n");
        }
        return outputString.toString();
    }

    /**
     * Set the grid to a new value.  This method can be used for
     * testing and is used by our testing/grading script.
     * @param newGrid The values to set the grid to
     */
    public void setGrid(int[][] newGrid)
    {
        if (newGrid.length != GRID_SIZE ||
                newGrid[0].length != GRID_SIZE) {
            System.out.println("Attempt to set grid to incorrect size");
            return;
                }
        this.grid = new int[GRID_SIZE][GRID_SIZE];
        for (int r = 0; r < grid.length; r++)
        {
            for (int c = 0; c < grid[r].length; c++) {
                grid[r][c] = newGrid[r][c];
            }
        }
    }

    /**
     * get a copy of the grid
     * @return A copy of the grid
     */
    public int[][] getGrid()
    {
        int[][] gridCopy = new int[GRID_SIZE][GRID_SIZE];
        for (int r = 0; r < grid.length; r++)
        {
            for (int c = 0; c < grid[r].length; c++) {
                gridCopy[r][c] = grid[r][c];
            }
        }
        return gridCopy;
    }
}