package pacman.controllers.meera_udani;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
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

public class Perceptron_Controller extends Controller<MOVE> {
	HashMap<Integer, Weight> trainedWeightMap = new HashMap<>();
	public static StarterGhosts ghosts = new StarterGhosts();

	int i =0;
	public void readWeights(){
		try {
			BufferedReader in = new BufferedReader(new FileReader("weights.txt"));
			String str;
			while ((str = in.readLine()) != null) {
				String line[] = str.split("\t");
				trainedWeightMap.put(i, new Weight(line));
				i++;
			}
			in.close();
		} catch (IOException e) {
			System.out.println("Could not load data from file");
		}


	}
	@Override
	public MOVE getMove(Game game, long timeDue) {
		Double prediction = 0.0;
		HashMap<Integer,Double> predictionMap = new HashMap<>();
		readWeights();
		Game gameCopy = game.copy();
        Game gameAtM = gameCopy;
        double currentGameFeature [] = getCurrentGameFeatures(gameAtM);
        for(Integer key : trainedWeightMap.keySet()){
			Weight weight= trainedWeightMap.get(key);
			prediction = weight.wt[0]* currentGameFeature[0] +
					weight.wt[1]* currentGameFeature[1] +
					weight.wt[2]* currentGameFeature[2] +
					weight.wt[3]* currentGameFeature[3] +
					weight.wt[4]* currentGameFeature[4] +
					weight.wt[5]* currentGameFeature[5] +
					weight.wt[6]* currentGameFeature[6];
			predictionMap.put(key, prediction);
			
		}
        int maxMove = findMax(predictionMap);
        MOVE move = numberToMove(maxMove);
        MOVE correctMove = checkMove(move, gameAtM);
//        System.out.println("Move predicted by controller "+ move);
        return correctMove;
	}
	
	
	public MOVE checkMove(MOVE move, Game game){
		MOVE[] allMoves;
		MOVE pacmanLastMove = game.getPacmanLastMoveMade();
		int currIndex = game.getPacmanCurrentNodeIndex();
		if (pacmanLastMove != null) {
			allMoves = game.getPossibleMoves(currIndex, pacmanLastMove);
			boolean b = false;
			for(int i = 0; i < allMoves.length; i++){
				if(move == allMoves[i])
					b = true;
				}
			if(b == true)
				return move;
			else return allMoves[0];
		} else {
			allMoves = game.getPossibleMoves(currIndex);
			return allMoves[0];
		}

	}
	
	public int findMax(HashMap<Integer, Double> predictionMap) {
		return sortByValue(predictionMap).keySet().toArray(new Integer[5])[0];
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


	
	
	public int booleanToNumber(boolean booleanValue) {
		if(booleanValue)
			return 1;
		else
			return -1;
	}
	
	public int moveToNumber (MOVE move){
		switch (move) {
		case UP:  return 1;
		case RIGHT:  return 2;
		case DOWN:  return 3;
		case LEFT:  return 4;
		case NEUTRAL: return 0;
		default: return 0;
		}
	}

	public MOVE numberToMove (int n){
		switch(n){
		case 0: return MOVE.NEUTRAL;
		case 1: return MOVE.UP;
		case 2: return MOVE.RIGHT;
		case 3: return MOVE.DOWN;
		case 4: return MOVE.LEFT;
		default: return MOVE.NEUTRAL;

		}
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





}


