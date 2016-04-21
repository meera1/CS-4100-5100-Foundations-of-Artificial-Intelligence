package pacman.controllers.meera_udani;

import java.util.LinkedList;
import java.util.Queue;

import pacman.controllers.Controller;
import pacman.controllers.examples.StarterGhosts;
import pacman.game.Constants;
import pacman.game.Constants.DM;
import pacman.game.Constants.GHOST;
import pacman.game.Constants.MOVE;
import pacman.game.Game;

public class FinalProject_ControllerTry extends Controller<MOVE>
{
	public static StarterGhosts ghosts = new StarterGhosts();
	public static int MIN_VALUE = 20;

	// this function calculates the manhattan distance of pacman from all the power pills and sends 
	// back a total of the distance as a heuristic
	public int minDistanceFromPowerPillHeuristic(Game currentGameState, FinalProject_Node currentNode) 
	{
		//get the current index of pacman present in the current game state
		int current=currentGameState.getPacmanCurrentNodeIndex(); 

		int minDistance=Integer.MAX_VALUE;
		GHOST minGhost=null;	
		for(GHOST ghost : GHOST.values())
			if(currentGameState.getGhostEdibleTime(ghost)>0) {
				int distance=currentGameState.getManhattanDistance(current,currentGameState.getGhostCurrentNodeIndex(ghost));
				if(distance<minDistance) {
					minDistance=distance;
					minGhost=ghost;
				}

			}
		if (minGhost!=null) return minDistance;



		// get the current active power pill indices
		int powerPills [] = currentGameState.getActivePowerPillsIndices(); 
		if(powerPills.length > 0){
			// declare an array of length of the active power pills
			//int manhattanDistanceFromPowerPills [] = new int [powerPills.length];
			int minDistace = Integer.MAX_VALUE;
			for (int i = 0; i< powerPills.length; i++)
			{
				minDistace = Integer.min(currentGameState.getManhattanDistance(current, powerPills[i]),minDistace);
				
			}
			return minDistace;
		} else {
			int pills [] = currentGameState.getActivePillsIndices();
			
				// declare an array of length of the active power pills
				//int manhattanDistanceFromPowerPills [] = new int [powerPills.length];
				int minDistace = Integer.MAX_VALUE;
				for (int i = 0; i< pills.length; i++)
				{
					minDistace = Math.min(currentGameState.getManhattanDistance(current, pills[i]),minDistace);
				}
				return minDistace;
			
		}
		
	}

	public int minDistanceEdibleGhostHeuristic(Game currentGameState, FinalProject_Node currentNode) {

		//get the current index of pacman present in the current game state
		int current=currentGameState.getPacmanCurrentNodeIndex(); 
		int minDistance=Integer.MAX_VALUE;

		for(GHOST ghost : GHOST.values())
			if(currentGameState.getGhostEdibleTime(ghost)>0) {
				int distance=currentGameState.getShortestPathDistance(current,currentGameState.getGhostCurrentNodeIndex(ghost));
				if(distance<minDistance) 
					minDistance=distance;

			}
		return minDistance;
	}

	@Override
	public MOVE getMove(Game game, long timeDue) {





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
			int tempHighScore = this.aStar(new FinalProject_Node(gameAtM, 0,0,0), 7);

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


	public int aStar(FinalProject_Node rootGameState, int maxdepth)
	{
		MOVE[] allMoves=Constants.MOVE.values();
		int highScore = -1;
		// An open set of nodes to hold the nodes to visit
		Queue<FinalProject_Node> open = new LinkedList<FinalProject_Node>();

		// A set of all the nodes that have already been visited
		LinkedList<FinalProject_Node> closed = new LinkedList<FinalProject_Node>();

		open.add(rootGameState); // adding the root node initially to visit

		while(!open.isEmpty())
		{
			FinalProject_Node pmnode = open.remove(); // eject the root node to explore

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
				FinalProject_Node childNode = new FinalProject_Node(gameCopy,pmnode.costFromStart+1,0, pmnode.depth+1);
				int minDistanceHuer1 = minDistanceFromPowerPillHeuristic(gameCopy, childNode);
				//int minDistanceHuer2 = minDistanceFromPowerPillHeuristic(gameCopy, childNode);
				int currentNodeHeuristic = Integer.min(minDistanceHuer1,Integer.MAX_VALUE);
				//                    int currentNodeHeuristic = 0;
				if((boolean) (
						pmnode.costFromStart+ childNode.costFromStart +
						currentNodeHeuristic < costfunction))
				{	// a function in A star algorithm that is similar to 'fun = cost + heuristic'
					// depending which value, that particular child will be explored
					
					costfunction = pmnode.costFromStart + childNode.costFromStart+ currentNodeHeuristic;
					nextMove = m;
				}

			}

			Game gameCopy = pmnode.gameState.copy();
			gameCopy.advanceGame(nextMove, ghosts.getMove(gameCopy, 0));
			FinalProject_Node node = new FinalProject_Node(gameCopy,pmnode.costFromStart+1,0, pmnode.depth+1);

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

class FinalProject_Node 
{
	Game gameState;
	int costFromStart;
	int heuristic;
	int depth;

	public FinalProject_Node(Game game, int costFromStart, int heuristic, int depth)
	{
		this.gameState = game;
		this.costFromStart = costFromStart;
		this.heuristic = heuristic;
		this.depth = depth;
	}
}