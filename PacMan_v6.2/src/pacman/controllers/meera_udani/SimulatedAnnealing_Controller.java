/* So what hill climbing does is that it continuously climbs
 * up the hill as quickly as it can which can lead to getting stuck
 * but SA tries to explore more of the state space with the hope of 
 * actually climbing higher
*/
package pacman.controllers.meera_udani;

import java.util.LinkedList;
import java.util.Queue;
import java.util.Random;
import pacman.controllers.Controller;
import pacman.controllers.examples.StarterGhosts;
import pacman.controllers.examples.StarterPacMan;
import pacman.game.Constants;
import pacman.game.Constants.DM;
import pacman.game.Constants.GHOST;
import pacman.game.Constants.MOVE;
import pacman.game.Game;

public class SimulatedAnnealing_Controller extends Controller<MOVE>
{
	public static StarterGhosts ghosts = new StarterGhosts();
	public static StarterPacMan pacMan = new StarterPacMan();
	@Override
	public MOVE getMove(Game game, long timeDue)
	{
		
//	    MOVE[] allMoves=MOVE.values();
	
	    MOVE[] allMoves;
		MOVE pacmanLastMove = game.getPacmanLastMoveMade();
		int currIndex = game.getPacmanCurrentNodeIndex();
		if (pacmanLastMove != null) {
			allMoves = game.getPossibleMoves(currIndex, pacmanLastMove);
		} else {
			allMoves = game.getPossibleMoves(currIndex);
		}
    
	    int highScore = -1;
	    MOVE highMove = null;
	    
	    // In simulated annealing, the idea is to gradually decrease the temperature, as 
	    // we used to in our previous algorithms when we increased the depth.
	    // In out algorithm, thus depth and temperature means the same
	    int temperature = 7;
	    int highScoreTemp = 0;
		while(temperature > 0)
		   {
		        for(MOVE m: allMoves)
		        {
//		            System.out.println("Trying Move: " + m);
		            Game gameCopy = game.copy();
		            Game gameAtM = gameCopy;
		            gameAtM.advanceGame(m, ghosts.getMove(gameAtM, timeDue));
		            highScoreTemp = this.simulatedAnnealing(new PacManNode(gameAtM, 0), temperature);
		            
		            if(highScore < highScoreTemp)
		            {
		                highScore = highScoreTemp;
		                highMove = m;
		            }
		            
//		            System.out.println("Trying Move: " + m + ", Score: " + highScoreTemp);
		           
		        }
		        temperature--;
		   } 
//        System.out.println("High Score: " + highScore + ", High Move:" + highMove);
          return highMove;        
	}
	
    	
	public int simulatedAnnealing(PacManNode rootGameState, int maxdepth)
	{
		MOVE[] allMoves=Constants.MOVE.values();
        int highScore = -1;
        
        Queue<PacManNode> explore = new LinkedList<PacManNode>(); // to keep track of the nodes to explore
        explore.add(rootGameState); // add the root node first
        
        while(!explore.isEmpty())
        {
        	PacManNode pmnode = explore.remove(); // explore the root node
        	if(pmnode.depth >= maxdepth)
            {
                int score = pmnode.gameState.getScore();
                 if (highScore < score)
                          highScore = score;
            }
        	else
            {
        		for(MOVE m: allMoves)
                {
                    Game gameCopy = pmnode.gameState.copy();
                    gameCopy.advanceGame(m, ghosts.getMove(gameCopy, 0));
                    PacManNode child = new PacManNode(gameCopy, pmnode.depth+1);
                    /*
                     * Since we require a fitness function to evaluate the node, 
                     * we will use the predefined 'getaaaScore' function.
                     * */
                    int childScore = child.gameState.getScore();
                    int parentScore = pmnode.gameState.getScore();
                    /*
                     * If the fitness of the child is more than
                     * the current node, then add the child to the 
                     * list of nodes to be explored 
                     * */
                    if(childScore > parentScore)
                    {
                    	explore.add(child);
                    }
                    /*
                     * If the fitness is not more than that of the current node
                     * then don't reject that child node immediately, try to evaluate it 
                     * further probabilistically such that if the value of the 
                     * probability is 1 then we will still explore the child, if 
                     * the value is 0 then reject that child. But if the value is 
                     * between 1 and 0 then determine a cutoff value based on experimentation
                     * which value gives better results, and if the probability is greater 
                     * than that value then add it to the explore list else discard
                     * */
                    else
                    {
                    	double cutOff = 0.5;
                    	/*
                    	 * The probability is found using the formula:
                    	 * 
                    	 * P = exp((fitness of child - fitness of parent)/ current temperature)
                    	 * */
                    	double probability = Math.exp((parentScore - childScore)/maxdepth);
                    	if(probability >= cutOff)
                    		explore.add(child);
                    }
                }
            }
        }
        
        return highScore;
	}
	

}