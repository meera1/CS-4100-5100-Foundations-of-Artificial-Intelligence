package pacman.controllers.meera_udani;

import java.io.BufferedReader;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import pacman.game.Constants.MOVE;

public class Perceptron_training {

	public ArrayList<DataPoint> trainingData = new ArrayList<DataPoint>();

	HashMap<Integer, double[]> weightMap = new HashMap<>();

	public void initMap(){
		weightMap.put(0, new double[7]); // Neutral
		weightMap.put(1, new double[7]); // up
		weightMap.put(2, new double[7]); // right
		weightMap.put(3, new double[7]); // down
		weightMap.put(4, new double[7]); // left

	}




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

	public void train(){
		boolean b = false;
		int i=1;
		HashMap<Integer,Double> predictionMap = new HashMap<>();
		while(i <= 10000){
			System.out.println(i++);
			//b = true;
			double predictionForKey = 0.0;
			for(DataPoint d : trainingData){
				for(Integer key : weightMap.keySet()){
					double [] weight= weightMap.get(key);
					predictionForKey = weight[0]* d.shortestDistBetGhostAndPacman +
							weight[1]* d.NearestGhostIfEdible +
							weight[2]* d.closestPowerPillDistFromPacman +
							weight[3]* d.pacmanEaten +
							weight[4]* d.ghostEaten +
							weight[5]* d.pacmanLastMove +
							weight[6]* d.nearestGhostLastMove;
					predictionMap.put(key, predictionForKey);
				}			
				int maxIndex = findMax(predictionMap);
				int dataPointClass = moveToNumber(d.moveToTake);
				//System.out.println("Predicted Value  "+ maxIndex + " True Lable "+ d.moveToTake);
				if(maxIndex != dataPointClass){
					b = false;
					double wrongClassWeight [] = weightMap.get(maxIndex);
					wrongClassWeight[0] -= d.shortestDistBetGhostAndPacman;
					wrongClassWeight[1] -= d.NearestGhostIfEdible;
					wrongClassWeight[2] -= d.closestPowerPillDistFromPacman;
					wrongClassWeight[3] -= d.pacmanEaten;
					wrongClassWeight[4] -= d.ghostEaten;
					wrongClassWeight[5] -= d.pacmanLastMove;
					wrongClassWeight[6] -= d.nearestGhostLastMove;
					weightMap.put(maxIndex, wrongClassWeight);

					double correctClassWeight [] = weightMap.get(dataPointClass);
					correctClassWeight[0] += d.shortestDistBetGhostAndPacman;
					correctClassWeight[1] += d.NearestGhostIfEdible;
					correctClassWeight[2] += d.closestPowerPillDistFromPacman;
					correctClassWeight[3] += d.pacmanEaten;
					correctClassWeight[4] += d.ghostEaten;
					correctClassWeight[5] += d.pacmanLastMove;
					correctClassWeight[6] += d.nearestGhostLastMove;
					weightMap.put(dataPointClass, correctClassWeight);
				}
			}

		}

		for(Integer key : weightMap.keySet()){
			double w [] = weightMap.get(key);
			try 
			{
				FileOutputStream outS= new FileOutputStream("weights.txt", true);
				PrintWriter pw=new PrintWriter(outS);
				for(int j = 0; j < w.length; j++)
					pw.print(w[j] + "\t");
				pw.println();
				pw.flush();
				pw.close();
				outS.close();



			}
			catch (IOException e)
			{
				System.out.println("Could not save data!");	
			}
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







}

class Perceptron_training_main{
	public static void main(String args[]){

		Perceptron_training p = new Perceptron_training();
		p.initMap();
		p.fileRead();
		p.train();





	}

}
