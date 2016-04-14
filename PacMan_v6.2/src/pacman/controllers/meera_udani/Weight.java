package pacman.controllers.meera_udani;

public class Weight {

	double wt[];
	public Weight(String[] line) {
		
		wt= new double[line.length];
		for(int i = 0; i < wt.length; i++)
			wt[i] = Double.parseDouble(line[i]);
	}
	
	

}
