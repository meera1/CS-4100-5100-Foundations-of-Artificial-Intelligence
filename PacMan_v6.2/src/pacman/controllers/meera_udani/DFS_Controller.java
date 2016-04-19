package pacman.controllers.meera_udani;

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
//	                System.out.println("Trying Move: " + m);
	                Game gameCopy = game.copy();
	                Game gameAtM = gameCopy;
	                gameAtM.advanceGame(m, ghosts.getMove(gameAtM, timeDue));
	                int tempHighScore = this.dfs_meera(new PacManNode(gameAtM, 0), 7);
	                
	                if(highScore < tempHighScore)
	                {
	                    highScore = tempHighScore;
	                    highMove = m;
	                }
	                
	                //System.out.println("Trying Move: " + m + ", Score: " + tempHighScore);
	            }
	            
	            //System.out.println("High Score: " + highScore + ", High Move:" + highMove);
	              
	              return highMove;
	                
		}
	        
		
		
		
	        public int dfs_meera(PacManNode pmnode, int maxdepth)
		{
	            MOVE[] allMoves=Constants.MOVE.values();
	            int highScore = -1;
	            Stack<PacManNode> stack = new Stack<PacManNode>();
	            stack.push(pmnode);
	            while(!stack.isEmpty())
	                {
	            		
	                    PacManNode currentNode = (PacManNode) stack.peek(); // just checking the top of stack node say PacManNode N1, not popping 
	                    
	                    
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
	                        for(MOVE m: allMoves)  // for all the children of N1, repeat the following steps
	                        	
	                        	
	                            {
	                        	
	                            // Working on the game copy
	                            Game gameCopy = currentNode.gameState.copy(); 
	                            
	                            
	                            // advance the game to find out the next state of the game if the move 'm' would be taken
	                            gameCopy.advanceGame(m, ghosts.getMove(gameCopy, 0)); 
	                             
	                            // create a node PacManNode of N1's children at a depth + 1
	                            PacManNode node = new PacManNode(gameCopy, currentNode.depth+1);  
	                            
	                            // push its first child taken into consideration
	                            stack.push(node); 
	                            
	                            // call the dfs function on the child pushed in stack, this is a recursive call,
	                            // so we will call the dfs function on the child of the child currently being explored,
	                            // this will happen till we reach the maxDepth so that now the nodes will be popped,
	                            // and the high score will be returned after comparing it with the game score
	                            // NOTE: this recursive call and the pop that follows it, replicated the dfs feature of the also
	                            highScore = dfs_meera(node,maxdepth);  
	                            stack.pop(); // this pop is to pop the child that has been explored, i.e child of child of child till ax depth 
	                        }
	                        
	                       
	                       }
	                    
	                    stack.pop(); // this will pop the node N1 that we took as an example above on line 63
	                    }
	            return highScore;
	          }

}
