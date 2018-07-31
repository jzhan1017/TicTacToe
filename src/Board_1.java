
import java.util.ArrayList;
import java.util.Scanner;

public class Board_1 {
	public int choice;  // 1 if AI is player 2, 2 if AI is player 1
	public boolean turn; // keeps track of if it is AI's turn 
	public int result; // 1 for win, -1 for loss, 0 for tie
	public int nodeCount; // stores number of nodes state space search goes through
	public static final int notTie = -10;
	public int[][] initialState = new int[3][3];
	public int[][] currentState = new int[3][3];
	public Board_1(String choice) {
		if (choice.equals("x")) {
			this.choice = 1;
		} else {
			this.choice = 2;
		}
		if (this.choice == 2) {
			turn = true;
		} else {
			turn = false;
		}
		createInitialState();
		result = -2;
	}
	public void createInitialState() {
		for (int i = 0; i <= 2; i ++) {
			for (int j = 0; j <= 2; j ++) {
				initialState[i][j] = 0;
			}
		}
		updateState(initialState);
	}
	public void updateState(int[][] state) {
		for (int i = 0; i <= 2; i ++) {
			for (int j = 0; j <= 2; j ++) {
				currentState[i][j] = state[i][j];
			}
		}
	}
	public ArrayList<int[][]> createDecisionSet(int[][] state , int choice) {
		ArrayList<int[][]> stateSpace = new ArrayList<>();
//		ArrayList<Integer> moveSpace = new ArrayList<>();
		for (int i = 0; i <= 2; i ++) {
			for (int j = 0; j <= 2; j ++) {
				// creates a deep copy of state array
				int[][] nextState = new int[3][3]; 
				for (int k = 0; k <= 2; k ++) {
					for (int l = 0; l <= 2; l ++) {
						nextState[k][l] = state[k][l];
					}
				}
				if (state[i][j] == 0) {
					if (choice == 2) {
						nextState[i][j] = 1;
					} else {
						nextState[i][j] = -1;
					}
//					int move = reverseConvert(i, j);
//					moveSpace.add(move);
					stateSpace.add(nextState);
				} 
			}
		}
		return stateSpace;
	} 
	public ArrayList<Integer> createMoveSpace(int[][] state) {
		ArrayList<Integer> moveSpace = new ArrayList<>();
		for (int i = 0; i <= 2; i ++) {
			for (int j = 0; j <= 2; j ++) {
				if (state[i][j] == 0) {
					int move = reverseConvert(i, j);
					moveSpace.add(move);
				}
			}
		}
		return moveSpace;
	} 
	public boolean checkAnyMoreMoves(int[][] state) {
		for (int i = 0; i <= 2; i ++) {
			for (int j = 0; j <= 2; j ++) {
				if (state[i][j] == 0) {
					return true;
				}
			}
		}
		return false;
	}
	// returns 1 if win, -1 if loss, 0 if draw
	public int checkTermination(int[][] state) {
		int row1 = 0, row2 = 0, row3 = 0, column1 = 0, column2 = 0, column3 = 0, diagonal1 = 0, diagonal2 = 0;
		for (int i = 0; i <= 2; i ++) {
			row1 += state[0][i];
			row2 += state[1][i];
			row3 += state[2][i];
			column1 += state[i][0];
			column2 += state[i][1];
			column3 += state[i][2];
			diagonal1 += state[i][i];
			diagonal2 += state[i][2-i];
		} 
		// win is if diagonal, row or column adds up to 3, loss if adds up to -3
		if (row1 == 3 || row2 == 3 || row3 == 3 || column1 == 3 || column2 == 3 || column3 == 3 || diagonal1 == 3 || diagonal2 == 3) {
			return 1;
		} else if ((row1 == -3 || row2 == -3 || row3 == -3 || column1 == -3 || column2 == -3 || column3 == -3 || diagonal1 == -3 || diagonal2 == -3)) {
			return -1;
		} else {
			if (checkAnyMoreMoves(state)) { //checks if it is not a tie by seeing if there are anymore moves
				return notTie;
			}
			return 0;
		}
	}
	// minimax algorithm
	public int minimax(int[][] state, int choice) {
		ArrayList<int[][]> stateSpace = createDecisionSet(state, choice);
		if (checkTermination(state) != notTie) {
			return checkTermination(state);
		} 
		if (choice == 2) {
			nodeCount++;
			int bestValue = Integer.MIN_VALUE;
			for (int i = 0; i < stateSpace.size(); i ++) {
				int v = minimax(stateSpace.get(i), 1);
				bestValue = Math.max(bestValue, v);
			}
			return bestValue;
		} else {
			nodeCount++;
			int worstValue = Integer.MAX_VALUE;
			for (int i = 0; i < stateSpace.size(); i ++) {
				int v = minimax(stateSpace.get(i), 2);
				worstValue = Math.min(worstValue, v);
			}
			return worstValue;
		}
	}
	public void test1Board(int[][] state) {
		int check;
		do {
			if (turn) {
				nodeCount = 0;
				ArrayList<int[][]> stateSpace = createDecisionSet(state, choice);
				ArrayList<Integer> moveSpace = createMoveSpace(state);
				int[] best = new int[stateSpace.size()];
				// runs minimax on all the next possible states
				for (int i = 0; i < stateSpace.size(); i ++) {
					if (choice == 2) {
						best[i] = minimax(stateSpace.get(i), 1);
					} else { 
						best[i] = -minimax(stateSpace.get(i), 2);
					}
//					System.err.print(best[i] + " ");
				}
				System.err.println();
				int max = 0;
				int stored = best[0];
				for(int k = 1; k < best.length; k ++) {
					if (stored < best[k]) {
						stored = best[k];
						max = k;
					}
				}
				// AI's move will the max utility of the next possible states
				for (int i = 0; i <= 2; i ++) {
					for (int j = 0; j <= 2; j ++) {
						state[i][j] = stateSpace.get(max)[i][j];
					}
				}
				System.err.println("AI Move: " + moveSpace.get(max));
				System.err.print("Summary: ");
				System.err.println("Node count: " + nodeCount);
				updateState(state);
				turn = !turn;
			} else {
				int k;
				int marker;
				Scanner scanner = new Scanner(System.in);
				do {
					do {
						System.err.print("Pick your position: ");
						k = scanner.nextInt();
					} while (k > 9 || k < 0);
					int[] vector = convert(k);
					int row = vector[0]; int column = vector[1];
					marker = state[row][column];
					if (choice == 2 && marker == 0) {
						state[row][column] = -1;
					} else if (choice == 1 && marker == 0) {
						state[row][column] = 1;
					} else {
						System.err.println("This position has been already taken: ");
					}
				} while (marker != 0);
				updateState(state);
				turn = !turn;
			}
			printBoard(state);
			System.err.println();
			check = checkTermination(state);
		} while (check == notTie);
		result = checkTermination(state);
		if (result == 1 && choice == 2 || result == -1 && choice == 1) {
			System.err.println("You lose!, AI Win!");
		} else if (result == -1 && choice == 2 || result == 1 && choice == 1 ) {
			System.err.println("You win!, AI loses!");
		} else {
			System.err.println("Tie game");
		}
	}
	// converts vector <row, column> to a number between 1-9
	public int reverseConvert(int row, int column) {
		return row*3+column+1;
	}
	// converts number to a vector <row, column>
	public int[] convert(int k) {
		int[] vector = new int[2];
		if (k%3 == 0) {
			vector[0] = k/3 - 1;
			vector[1] = 2;
		} else if (k%3 == 1) {
			vector[0] = k/3;
			vector[1] = 0;
		} else {
			vector[0] = k/3;
			vector[1] = 1;
		}
		return vector;
	}
	public void printBoard(int state[][]) {
		for (int i = 0; i <= 2; i ++) {
			for (int j = 0; j <= 2; j ++) {
				if (state[i][j] == 0) {
					System.err.print("-" + " ");
				} else if (state[i][j] == 1) {
					System.err.print("X" + " ");
				} else {
					System.err.print("O" + " ");
				}
			}
			System.err.println();
		}
	}
}
