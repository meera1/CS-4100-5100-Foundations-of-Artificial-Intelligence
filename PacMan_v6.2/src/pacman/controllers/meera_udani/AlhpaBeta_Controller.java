/*
 * NOTE: The program was initially getting stuck at a position and so my friends passed
 * down a code snippet that the TA sent to avoid that problem
 * 
 * */


package pacman.controllers.meera_udani;

import java.util.LinkedList;
import java.util.Queue;
import java.util.Random;

import pacman.controllers.Controller;
import pacman.controllers.examples.StarterGhosts;

import pacman.game.Constants;
import pacman.game.Constants.DM;
import pacman.game.Constants.GHOST;
import pacman.game.Constants.MOVE;
import pacman.game.Game;

public class AlhpaBeta_Controller extends Controller<MOVE>{
	
	public static StarterGhosts ghosts = new StarterGhosts();

	public MOVE getMove(Game game, long timeDue)
	{
		MOVE[] allMoves;
		MOVE pacmanLastMove = game.getPacmanLastMoveMade();
		int currIndex = game.getPacmanCurrentNodeIndex();
		if (pacmanLastMove != null){
			allMoves = game.getPossibleMoves(currIndex,pacmanLastMove);
			}
		else{
			allMoves = game.getPossibleMoves(currIndex);
			}

		double highScore = -1.0;
		MOVE highMove = null;

		for (MOVE m : allMoves) {
			Game gameCopy = game.copy();
			Game gameAtM = gameCopy;
			gameAtM.advanceGame(m, ghosts.getMove(gameAtM, timeDue));
			double tempHighScore = this.alphaBeta(new PacManNode(gameAtM), 7, Double.NEGATIVE_INFINITY, Double.POSITIVE_INFINITY,true);
			if (highScore < tempHighScore) {
				highScore = tempHighScore;
				highMove = m;
			}
		}
		
		return highMove;
	}

	public double alphaBeta(PacManNode rootGameState, int maxdepth, double alpha, double beta, boolean maxPlayer)
	{
		MOVE[] allMoves;
		MOVE pacmanLastMove = rootGameState.gameState.getPacmanLastMoveMade();
		int currIndex = rootGameState.gameState.getPacmanCurrentNodeIndex();
		if (pacmanLastMove != null){
			allMoves = rootGameState.gameState.getPossibleMoves(currIndex,pacmanLastMove);
			}
		else{
			allMoves = rootGameState.gameState.getPossibleMoves(currIndex);
			}

		if (maxdepth <= 0)
		{
			return (rootGameState.gameState.getScore());
		}
		if (maxPlayer)
		{
			double v = Double.NEGATIVE_INFINITY;
			for (MOVE m : allMoves)
			{
				Game gameCopy = rootGameState.gameState.copy();
				gameCopy.advanceGame(m, ghosts.getMove(gameCopy, 0));
				PacManNode node = new PacManNode(gameCopy);
				v = Math.max(v, alphaBeta(node, maxdepth - 1, alpha, beta, false));
				alpha = Math.max(alpha, v);
				if(beta <= alpha)
					break;	
			}
			return v;
		} else
		{
			double v = Double.POSITIVE_INFINITY;
			for (MOVE m : allMoves)
			{
				Game gameCopy = rootGameState.gameState.copy();
				gameCopy.advanceGame(m, ghosts.getMove(gameCopy, 0));				
				PacManNode node = new PacManNode(gameCopy);
				v = Math.min(v, alphaBeta(node, maxdepth - 1, alpha, beta, true));
				beta = Math.min(beta, v);
				if(beta <= alpha)
					break;
			}
			return v;
		}
	}

}

