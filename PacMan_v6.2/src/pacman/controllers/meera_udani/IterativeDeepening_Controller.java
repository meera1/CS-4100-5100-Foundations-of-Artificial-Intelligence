/*
 * This program is the same as the dfs program implemented.
 * The only difference in this one is that the maxDepth, that is the
 * depth till which the pacman finds the high move and high score
 * recursively is incremented from 0 till a limited depth of 7 for every 
 * possible move from the root
 */


package pacman.controllers.meera_udani;

import java.util.Stack;

import pacman.controllers.Controller;
import pacman.controllers.examples.StarterGhosts;
import pacman.game.Constants.MOVE;
import pacman.game.Constants;
import pacman.game.Game;

public class IterativeDeepening_Controller extends Controller<MOVE> {

	
	public static StarterGhosts ghosts = new StarterGhosts();
	
	
	public MOVE getMove(Game game, long timeDue) {
		MOVE[] allMoves=MOVE.values();
        
        int highScore = -1;
        MOVE highMove = null;
        
        for(int maxdepth = 0; maxdepth <= 7; maxdepth++)
        {
        	for(MOVE m: allMoves)
            {

                
                	//System.out.println("Currently at depth "+ maxdepth + " for the iterative functionality" );
            	
	                //System.out.println("Trying Move: " + m);
	                Game gameCopy = game.copy();
	                Game gameAtM = gameCopy; // 
	                gameAtM.advanceGame(m, ghosts.getMove(gameAtM, timeDue));
	                int tempHighScore = this.iterative_dfs(new PacManNode(gameAtM, 0), maxdepth);
	                
	                if(highScore < tempHighScore)
	                {
	                    highScore = tempHighScore;
	                    highMove = m;
	                }
	                
	                //System.out.println("Trying Move: " + m + ", Score: " + tempHighScore);
	               
            }
        }
        
        
        //System.out.println("High Score: " + highScore + ", High Move:" + highMove);
        
        return highMove;
	}
	
	
	public int iterative_dfs(PacManNode rootGameState, int maxDepth)
	{
		MOVE[] allMoves=Constants.MOVE.values();
        int highScore = -1;
        Stack<PacManNode> s = new Stack<PacManNode>();
        s.push(rootGameState);
        while(!s.isEmpty())
        {
        	PacManNode currentNode = s.peek();
        	//System.out.println("current node popped " + currentNode);
        	
        	if(currentNode.depth >= maxDepth)
        	{
        		int score = currentNode.gameState.getScore();
                if (highScore < score) 
                {
               	 highScore = score;
               	 
                }
        	}
        	else
        	{
        		for(MOVE m: allMoves)
        		{
        			//System.out.println("Trying Move in DFS function: " + m);
                    Game gameCopy = currentNode.gameState.copy();
                    gameCopy.advanceGame(m, ghosts.getMove(gameCopy, 0));
                    PacManNode node = new PacManNode(gameCopy, currentNode.depth+1);
                    s.push(node);
                    highScore = iterative_dfs(node, maxDepth);
                    s.pop();
        		}
        	}
        	s.pop();
        }
        return highScore;
	}

}
