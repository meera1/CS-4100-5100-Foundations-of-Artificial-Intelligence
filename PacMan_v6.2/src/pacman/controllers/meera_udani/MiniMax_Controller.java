package pacman.controllers.meera_udani;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.Random;

import pacman.controllers.Controller;
import pacman.controllers.examples.StarterGhosts;
import pacman.game.Constants;
import pacman.game.Constants.DM;
import pacman.game.Constants.GHOST;
import pacman.game.Constants.MOVE;
import pacman.game.Game;
import pacman.game.internal.Node;
import pacman.controllers.examples.StarterPacMan;
import pacman.controllers.meera_udani.PacManNode;

public class MiniMax_Controller extends Controller<MOVE> {
	
	int maxdepth = 7;
	

		public static StarterGhosts ghosts = new StarterGhosts();

		public MOVE getMove(Game game, long timeDue) {
			Random rnd = new Random();
			MOVE[] allMoves = MOVE.values();
			

			double highScore = -1;
			MOVE highMove = null;

			for (MOVE m : allMoves) {
				// System.out.println("Trying Move: " + m);
				Game gameCopy = game.copy();
				Game gameAtM = gameCopy;
				gameAtM.advanceGame(m, ghosts.getMove(gameAtM, timeDue));
				double tempHighScore = this.minimax(new PacManNode(gameAtM), 0, true);
				if (highScore < tempHighScore) {
					highScore = tempHighScore;
					highMove = m;
				}
				
				System.out.println("Trying Move: " + m + ", Score: "
						+ tempHighScore);
			}

			System.out.println("High Score: " + highScore + ", High Move:"
					+ highMove);
			return highMove;
		}

		
		public double minimax (PacManNode rootGameState, int depth, boolean max)
		{
			double bestValue; 
			MOVE[] allMoves = Constants.MOVE.values();
			
			if(rootGameState.depth == 7 || rootGameState.depth == maxdepth)
				return rootGameState.gameState.getScore();
			
			else if(max)
			{
				bestValue = Double.NEGATIVE_INFINITY;
				for(MOVE m : allMoves)
				{
					Game gameCopy = rootGameState.gameState.copy();
                    gameCopy.advanceGame(m, ghosts.getMove(gameCopy, 0));
                    PacManNode child = new PacManNode(gameCopy, depth+1);
                    bestValue = Math.max(bestValue, minimax(child, depth+1, false));
					
				}
				return bestValue;
			}
			
			else
			{
				bestValue = Double.POSITIVE_INFINITY;
				for(MOVE m : allMoves)
				{
					Game gameCopy = rootGameState.gameState.copy();
                    gameCopy.advanceGame(m, ghosts.getMove(gameCopy, 0));
                    PacManNode child = new PacManNode(gameCopy, depth+1);
                    bestValue = Math.min(bestValue, minimax(child, depth+1, true));
                }
				return bestValue;
				
			}
			
			
		}
		
		
		

}
