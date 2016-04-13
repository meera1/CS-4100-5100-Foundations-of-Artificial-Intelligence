package pacman.controllers.meera_udani;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import pacman.controllers.Controller;
import pacman.controllers.examples.StarterGhosts;
import pacman.game.Constants.DM;
import pacman.game.Constants.GHOST;
import pacman.game.Constants.MOVE;
import pacman.game.Game;

public class KNN_Controller extends Controller<MOVE>{

	public int N;

	public KNN_Controller(int n) {
		N = n;
		fileRead();
	}

	public static StarterGhosts ghosts = new StarterGhosts();
	public ArrayList<DataPoint> trainingData = new ArrayList<DataPoint>();
	
	public void fileRead(){
		try {
			BufferedReader in = new BufferedReader(new FileReader("training-data.txt"));
			String str;
			while ((str = in.readLine()) != null) {
				String line[] = str.split("\t");
				trainingData.add(new DataPoint(line));
			}
			in.close();
		} catch (IOException e) {
			System.out.println("Could not load data from file");
		}

	}

	@Override
	public MOVE getMove(Game gameAtM, long timeDue) {
		// TODO Auto-generated method stub
		{
//			Game gameCopy = game.copy();
//			Game gameAtM = gameCopy;
//			gameAtM.advanceGame(,ghosts.getMove(gameAtM, timeDue));
			double currentGameFeatures [] = getCurrentGameFeatures(gameAtM);
			DataPoint currentGame = new DataPoint(currentGameFeatures);
			MOVE knnMove = calculateMove(currentGame, trainingData);
			return knnMove;
		}
	}

	private MOVE calculateMove(DataPoint currentGamePoint, ArrayList<DataPoint> trainingData) {
		// TODO Auto-generated method stub
		ArrayList<DistanceIndex> distanceWithMove = new ArrayList<DistanceIndex>();
		for (DataPoint dataPoint : trainingData) {
			double distance = Math.sqrt(Math.pow(currentGamePoint.shortestDistBetGhostAndPacman - dataPoint.shortestDistBetGhostAndPacman,2)+
					Math.pow(currentGamePoint.NearestGhostIfEdible - dataPoint.NearestGhostIfEdible,2)+
					Math.pow(currentGamePoint.closestPowerPillDistFromPacman - dataPoint.closestPowerPillDistFromPacman,2)+
					Math.pow(currentGamePoint.pacmanEaten - dataPoint.pacmanEaten,2)+
					Math.pow(currentGamePoint.ghostEaten - dataPoint.ghostEaten,2)+
					Math.pow(currentGamePoint.pacmanLastMove - dataPoint.pacmanLastMove,2)+
					Math.pow(currentGamePoint.nearestGhostLastMove - dataPoint.nearestGhostLastMove,2));
			distanceWithMove.add(new DistanceIndex(distance, dataPoint.moveToTake));
		}

		Collections.sort(distanceWithMove, new DistanceIndexComparator());
		List<DistanceIndex> sub =  distanceWithMove.subList(0, N);
		MOVE finalMove = getMajorityNeighbor(sub);
		return finalMove;
	}

	private MOVE getMajorityNeighbor(List<DistanceIndex> sub) {

		HashMap<MOVE, Integer> map = new HashMap<>();
		map.put(MOVE.UP, 0);
		map.put(MOVE.DOWN, 0);
		map.put(MOVE.NEUTRAL, 0);
		map.put(MOVE.LEFT, 0);
		map.put(MOVE.RIGHT, 0);
		for (DistanceIndex i : sub) {
			map.put(i.move, map.get(i.move) + 1);
		}
		
		HashMap<MOVE, Integer> sortedMap = (HashMap<MOVE, Integer>) sortByValue(map);
		return sortedMap.keySet().toArray(new MOVE[sortedMap.size()])[0];
	}

	public static <K, V extends Comparable<? super V>> Map<K, V> 
	sortByValue( Map<K, V> map )
	{
		List<Map.Entry<K, V>> list =
				new LinkedList<>( map.entrySet() );
		Collections.sort( list, new Comparator<Map.Entry<K, V>>()
		{
			@Override
			public int compare( Map.Entry<K, V> o1, Map.Entry<K, V> o2 )
			{
				return (o2.getValue()).compareTo( o1.getValue() );
			}
		} );

		Map<K, V> result = new LinkedHashMap<>();
		for (Map.Entry<K, V> entry : list)
		{
			result.put( entry.getKey(), entry.getValue() );
		}
		return result;
	}

	private double[] getCurrentGameFeatures(Game game) {
		// TODO Auto-generated method stub
		double features [] = new double [7];
		features[0] = Double.POSITIVE_INFINITY; // first column in training data file
		GHOST closestGhostFromPacman = GHOST.BLINKY;	
		int current1 = game.getPacmanCurrentNodeIndex();
		for(GHOST ghost : GHOST.values()){				
			int index = game.getGhostCurrentNodeIndex(ghost);
			double distance = game.getDistance(current1, index, DM.MANHATTAN);
			if(distance < features[0]){
				features[0] = distance;
				closestGhostFromPacman = ghost;
			}

		}
		features[1] = booleanToNumber(game.isGhostEdible(closestGhostFromPacman)); // 2nd value in file
		int [] activePowerPills = game.getActivePowerPillsIndices();
		features[2] = Double.MAX_VALUE;
		if(activePowerPills.length > 0){
			for(int j = 0 ; j < activePowerPills.length; j++){
				double distance1 = game.getDistance(current1, activePowerPills[j], DM.MANHATTAN);
				if(distance1 < features[2]){
					features[2] = distance1;  // 3rd value
				}
			}
		}
		else
		{
			// make the closestPowerPillDistanceFromPacman index to 0;
			features[2] = 0;
		}

		features[3] = booleanToNumber(game.wasPacManEaten()) ;  // 4th value
		features[4] = booleanToNumber(game.wasGhostEaten(closestGhostFromPacman)) ;  // 5th value
		features[5] = moveToNumber(game.getPacmanLastMoveMade()); // 6th value
		features[6] = moveToNumber(game.getGhostLastMoveMade(closestGhostFromPacman)); // 7th Value



		return features;
	}


	private int booleanToNumber(boolean booleanValue) {
		if(booleanValue)
			return 1;
		else
			return -1;
	}

	private int moveToNumber (MOVE move){
		switch (move) {
		case UP:  return 1;
		case RIGHT:  return 2;
		case DOWN:  return 3;
		case LEFT:  return 4;
		case NEUTRAL: return 0;
		default: return 0;
		}



	}
}


