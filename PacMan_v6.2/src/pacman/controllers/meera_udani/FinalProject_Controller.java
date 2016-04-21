package pacman.controllers.meera_udani;

import java.util.ArrayList;
import pacman.controllers.Controller;
import pacman.game.Game;
import pacman.game.Constants.DM;
import pacman.game.Constants.GHOST;

import static pacman.game.Constants.*;

/*
 * Pac-Man controller as part of the starter package - simply upload this file as a zip called
 * MyPacMan.zip and you will be entered into the rankings - as simple as that! Feel free to modify 
 * it or to start from scratch, using the classes supplied with the original software. Best of luck!
 * 
 * This controller utilises 3 tactics, in order of importance:
 * 1. Get away from any non-edible ghost that is in close proximity
 * 2. Go after the nearest edible ghost
 * 3. Go to the nearest pill/power pill
 */
public class FinalProject_Controller extends Controller<MOVE>
{	
	private static final int MIN_DISTANCE1=10;
	private static final int MIN_DISTANCE2=20;//if a ghost is this close, run away

	private static int counter = 1;

	public MOVE getMove(Game gameAtm,long timeDue)
	{		
		Game gameCopy = gameAtm.copy();
        Game game = gameCopy;
		counter++;
		int current=game.getPacmanCurrentNodeIndex();

		
		
		int minDistance=Integer.MAX_VALUE;
		GHOST minGhost=null;		

		for(GHOST ghost : GHOST.values())
			if(game.getGhostEdibleTime(ghost)>0)
			{
				int distance=game.getShortestPathDistance(current,game.getGhostCurrentNodeIndex(ghost));

				if(distance<minDistance)
				{
					minDistance=distance;
					minGhost=ghost;
				}
			}

		if(minGhost!=null) 	//we found an edible ghost
			return game.getNextMoveTowardsTarget(game.getPacmanCurrentNodeIndex(),game.getGhostCurrentNodeIndex(minGhost),DM.PATH);




		//		//Strategy 3: go after the pills and power pills
		int[] pills=game.getPillIndices();
		int[] powerPills=game.getActivePowerPillsIndices();


		// declare an array of length of the active power pills
		//int manhattanDistanceFromPowerPills [] = new int [powerPills.length];
		int minDistace = Integer.MAX_VALUE;
		for (int i = 0; i< powerPills.length; i++) {
			minDistace = Integer.min(game.getManhattanDistance(current, powerPills[i]),minDistace);
			if(minDistace == 1 && game.isPowerPillStillAvailable(i)) {
				for(GHOST ghost : GHOST.values()) {
					if(game.getGhostEdibleTime(ghost)==0 && game.getGhostLairTime(ghost)==0){
						if(game.getShortestPathDistance(current,game.getGhostCurrentNodeIndex(ghost))<MIN_DISTANCE1) {
							return game.getNextMoveTowardsTarget(current,powerPills[i],DM.PATH);
						}
					}
				}
				counter++;
				if(counter >= 300) {					
					counter = 0;
					return game.getNextMoveTowardsTarget(current,powerPills[i],DM.PATH); 
				}
				return game.getPacmanLastMoveMade().opposite();
			}
		}
		
		for(GHOST ghost : GHOST.values())
			if(game.getGhostEdibleTime(ghost)==0 && game.getGhostLairTime(ghost)==0)
				if(game.getShortestPathDistance(current,game.getGhostCurrentNodeIndex(ghost))<MIN_DISTANCE2)
					return game.getNextMoveAwayFromTarget(game.getPacmanCurrentNodeIndex(),game.getGhostCurrentNodeIndex(ghost),DM.PATH);


		ArrayList<Integer> targets=new ArrayList<Integer>();
		//		
				for(int i=0;i<pills.length;i++)					//check which pills are available			
					if(game.isPillStillAvailable(i))
						targets.add(pills[i]);
				
		for(int i=0;i<powerPills.length;i++)			//check with power pills are available
			if(game.isPowerPillStillAvailable(i))
				targets.add(powerPills[i]);				
		//		
		int[] targetsArray=new int[targets.size()];		//convert from ArrayList to array
		//		
				for(int i=0;i<targetsArray.length;i++)
					targetsArray[i]=targets.get(i);
		//		
		//		//return the next direction once the closest target has been identified
		return game.getNextMoveTowardsTarget(current,game.getClosestNodeIndexFromNodeIndex(current,targetsArray,DM.PATH),DM.PATH);
	}
}























