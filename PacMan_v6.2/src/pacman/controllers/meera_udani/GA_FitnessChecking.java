package pacman.controllers.meera_udani;

import static pacman.game.Constants.DELAY;

import java.util.ArrayList;
import java.util.EnumMap;
import java.util.List;
import java.util.Random;

import pacman.Executor;
import pacman.controllers.Controller;
import pacman.game.Game;
import pacman.game.Constants.GHOST;
import pacman.game.Constants.MOVE;

public class GA_FitnessChecking extends Executor {
	
	public static List<ArrayList<MOVE>> parents = new ArrayList<ArrayList<MOVE>>();	
	public static List<ArrayList<MOVE>> prepareInitialPopulation()
	{	
		List<MOVE> initialPopulation1 = new ArrayList<MOVE>();
		List<MOVE> initialPopulation2 = new ArrayList<MOVE>();
		List<MOVE> initialPopulation3 = new ArrayList<MOVE>();
		List<MOVE> initialPopulation4 = new ArrayList<MOVE>();
		List<ArrayList<MOVE>> parents = new ArrayList<ArrayList<MOVE>>();	

		
		
		for(int i=0;i<50;i++)
		{
			Random r = new Random();
			int max = 6, min = 0;
	
			int ip1 = r.nextInt((max - min) + min) + 0;
			initialPopulation1.add(MOVE.valueOf(ip1));
			
			int ip2 = r.nextInt((max - min) + min) + 0;
			initialPopulation2.add(MOVE.valueOf(ip2));
			
			int ip3 = r.nextInt((max - min) + min) + 0;
			initialPopulation3.add(MOVE.valueOf(ip3));
			
			int ip4 = r.nextInt((max - min) + min) + 0;
			initialPopulation4.add(MOVE.valueOf(ip4));
		}
		
		parents.add((ArrayList<MOVE>) initialPopulation1);
		parents.add((ArrayList<MOVE>) initialPopulation2);
		parents.add((ArrayList<MOVE>) initialPopulation3);
		parents.add((ArrayList<MOVE>) initialPopulation4);
		
		return parents;
	}
	
	
	public void runExperiment(Controller<MOVE> GA_Contoller,Controller<EnumMap<GHOST,MOVE>> ghostController,int trials, List<ArrayList<MOVE>> parents)
    {
		int tempScore=0;
    	int highScore = 0;
    	Random rnd=new Random(0);
		Game game;
		int counter = 0;
		for(int i=0;i<trials;i++)
		{
			game=new Game(rnd.nextLong());
			
			while(!game.gameOver())
			{
				//System.out.println(GA_Contoller.getMove(game.copy(), System.currentTimeMillis(), parents));
				for(int j=0;j<=3;j++)
				{
					ArrayList<MOVE> ip = parents.get(j);
					for(MOVE m : ip)
					{
					game.advanceGame(m, ghostController.getMove(game.copy(),System.currentTimeMillis()));
			        tempScore = tempScore + game.getScore();
					}
			        if(highScore < tempScore)
			        {
			        	highScore = tempScore;
			        	counter = j;
			        }
				}
				
				//mutation(parents.get(counter));
			}
		}
    }
}
