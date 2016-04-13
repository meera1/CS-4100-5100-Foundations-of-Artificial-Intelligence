package pacman.controllers.meera_udani;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;

public class Perceptron_Controller {

	HashMap<Integer, Weight> trainedWeightMap = new HashMap<>();
	
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
		
		
		for(Integer key : trainedWeightMap.keySet()){
			System.out.println(key + " "+ trainedWeightMap.get(key));
		}

	}

	
}



class Perceptron_Contoller_main{
	public static void main(String args[]){

		Perceptron_Controller p = new Perceptron_Controller();
		p.readWeights();




	}

}
