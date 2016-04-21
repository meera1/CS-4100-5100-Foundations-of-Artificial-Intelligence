package pacman.controllers.meera_udani;

import java.util.LinkedList;
import java.util.Queue;

import pacman.controllers.Controller;
import pacman.controllers.examples.StarterGhosts;
import pacman.game.Constants;
import pacman.game.Constants.MOVE;
import pacman.game.Game;

public class AStar_Controller extends Controller<MOVE>
{
	public static StarterGhosts ghosts = new StarterGhosts();
	
	
	// this function calculates the manhattan distance of pacman from all the power pills and sends 
	// back a total of the distance as a heuristic
	public int AstarHeuristic(Game currentGameState, AStarPacManNode currentNode) 
	{
		//get the current index of pacman present in the current game state
		int current=currentGameState.getPacmanCurrentNodeIndex(); 
		// get the current active power pill indices
		int powerPills [] = currentGameState.getActivePowerPillsIndices(); 
		// declare an array of length of the active power pills
		//int manhattanDistanceFromPowerPills [] = new int [powerPills.length];
		int minDistace = Integer.MAX_VALUE;
		for (int i = 0; i< powerPills.length; i++)
		{
			minDistace = Math.min(currentGameState.getManhattanDistance(current, powerPills[i]),minDistace);
		}
		return minDistace;
		
	}

	@Override
	public MOVE getMove(Game game, long timeDue)
	{
//        MOVE[] allMoves=MOVE.values();
    
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
        
       
        for(MOVE m: allMoves)
        {
        	
            //System.out.println("Trying Move: " + m);
            Game gameCopy = game.copy();
            Game gameAtM = gameCopy;
            gameAtM.advanceGame(m, ghosts.getMove(gameAtM, timeDue));
            int tempHighScore = this.aStar(new AStarPacManNode(gameAtM, 0,0,0), 7);
            
            if(highScore < tempHighScore)
            {
                highScore = tempHighScore;
                highMove = m;
            }
            
//            System.out.println("Trying Move: " + m + ", Score: " + tempHighScore);
           
        }
        
//        System.out.println("High Score: " + highScore + ", High Move:" + highMove);
          return highMove;        
	}
	
    	
	public int aStar(AStarPacManNode rootGameState, int maxdepth)
	{
		MOVE[] allMoves=Constants.MOVE.values();
        int highScore = -1;
        // An open set of nodes to hold the nodes to visit
        Queue<AStarPacManNode> open = new LinkedList<AStarPacManNode>();
        
        // A set of all the nodes that have already been visited
        LinkedList<AStarPacManNode> closed = new LinkedList<AStarPacManNode>();
        
        open.add(rootGameState); // adding the root node initially to visit
        
        while(!open.isEmpty())
        {
        	AStarPacManNode pmnode = open.remove(); // eject the root node to explore
        	
        	if(pmnode.depth >= maxdepth)
            {
                int score = pmnode.gameState.getScore();
                 if (highScore < score)
                          highScore = score;
            }
        	else
            {   int costfunction = Integer.MAX_VALUE;
            	MOVE nextMove = null;
            	
        		for(MOVE m: allMoves)
                {
                    Game gameCopy = pmnode.gameState.copy();
                    gameCopy.advanceGame(m, ghosts.getMove(gameCopy, 0));
                    AStarPacManNode childNode = new AStarPacManNode(gameCopy,pmnode.costFromStart+1,0, pmnode.depth+1);
                    int currentNodeHeuristic = AstarHeuristic(gameCopy, childNode);
//                    int currentNodeHeuristic = 0;
                    if((boolean) (pmnode.costFromStart+ childNode.costFromStart + currentNodeHeuristic < costfunction))
                    	{	// a function in A star algorithm that is similar to 'fun = cost + heuristic'
                    		// depending which value, that particular child will be explored
                    		costfunction = pmnode.costFromStart + childNode.costFromStart+ currentNodeHeuristic;
                    		nextMove = m;
                    	}

                }

        		Game gameCopy = pmnode.gameState.copy();
                gameCopy.advanceGame(nextMove, ghosts.getMove(gameCopy, 0));
                AStarPacManNode node = new AStarPacManNode(gameCopy,pmnode.costFromStart+1,0, pmnode.depth+1);
                
                // once we are sure we have visited the parent node, we will add it to the list of closed set
                // We are sure we have visited it since we have advanced the game using that node
                closed.add(pmnode); 
                
                
                // check if the new node is present in the set of already visited nodes
                if(!closed.contains(node))
                {	
                	open.add(node); // if not, then add it to the list of nodes to be explored
                }
            }
        }
        return highScore;
	}

}

class AStarPacManNode 
{
    Game gameState;
    int costFromStart;
    int heuristic;
    int depth;
    
    public AStarPacManNode(Game game, int costFromStart, int heuristic, int depth)
    {
        this.gameState = game;
        this.costFromStart = costFromStart;
        this.heuristic = heuristic;
        this.depth = depth;
    }
}