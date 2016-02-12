package pacman.controller.meera_udani;

import java.util.Stack;

import pacman.controllers.Controller;
import pacman.controllers.examples.StarterGhosts;
import pacman.game.Constants.MOVE;
import pacman.game.Constants;
import pacman.game.Game;

public class DFS_Controller extends Controller<MOVE>{


	
	 public static StarterGhosts ghosts = new StarterGhosts();
		
		
		public MOVE getMove(Game game,long timeDue)
		{
	            
	            MOVE[] allMoves=Constants.MOVE.values();
	        
	            int highScore = -1;
	            MOVE highMove = null;
	          
	            
	           
	            for(MOVE m: allMoves)
	            {
	                System.out.println("Trying Move: " + m);
	                Game gameCopy = game.copy();
	                Game gameAtM = gameCopy;
	                gameAtM.advanceGame(m, ghosts.getMove(gameAtM, timeDue));
	                int tempHighScore = this.dfs_meera(new PacManNode(gameAtM, 0), 7);
	                
	                if(highScore < tempHighScore)
	                {
	                    highScore = tempHighScore;
	                    highMove = m;
	                }
	                
	                System.out.println("Trying Move: " + m + ", Score: " + tempHighScore);
	            }
	            
	            System.out.println("High Score: " + highScore + ", High Move:" + highMove);
	              return highMove;
	                
		}
	        
		
		
		
	        public int dfs_meera(PacManNode pmnode, int maxdepth)
		{
	            MOVE[] allMoves=Constants.MOVE.values();
	            int highScore = -1;
	            
	            //Queue<PacManNode> queue = new LinkedList<PacManNode>();
	           
	           // queue.add(rootGameState);
//	            System.out.println(pmnode.depth + " depth at the start");
	            Stack<PacManNode> stack = new Stack<PacManNode>();
	            stack.push(pmnode);

			   //System.out.println("Adding Node at Depth: " + rootGameState.depth);
	                
	            
	            
	            
	            while(!stack.isEmpty())
	                {
	            		System.out.println("stack not empty");
	                    PacManNode currentNode = (PacManNode) stack.peek();
	                    //System.out.println("Removing Node at Depth: " + currentNode.depth);
	                    
	                    if(currentNode.depth >= maxdepth)
	                    {
	                        int score = currentNode.gameState.getScore();
	                         if (highScore < score) {
	                        	 highScore = score;
	                        	 
	                    }
	                         
	                         
	                                 
	                    }
	                    else
	                    {
	                    	
	                        //GET CHILDREN
	                        for(MOVE m: allMoves)
	                        	
	                        	
	                        {
	                        	
	                        //	System.out.println("Trying Move in DFS function: " + m);
	                            Game gameCopy = currentNode.gameState.copy();
	                            gameCopy.advanceGame(m, ghosts.getMove(gameCopy, 0));
	                            PacManNode node = new PacManNode(gameCopy, currentNode.depth+1);
	                            stack.push(node);
	                            highScore = dfs_meera(node,maxdepth);
	                        }
	                       }
	                    }
	            
	           // stack.pop();
	            return highScore;
	          }

}
