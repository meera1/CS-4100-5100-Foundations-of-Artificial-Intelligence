package pacman.controllers.meera_udani;

import java.util.Comparator;

import pacman.game.Constants.MOVE;

public class DistanceIndex{


	Double euclideanDistance;
	MOVE move;
	
	
	
	public DistanceIndex(double euclideanDistance, MOVE move) {
		super();
		this.euclideanDistance = euclideanDistance;
		this.move = move;
	}

}

class DistanceIndexComparator implements Comparator<DistanceIndex> {

	@Override
	public int compare(DistanceIndex o1, DistanceIndex o2) {
		// TODO Auto-generated method stub
		return o1.euclideanDistance.compareTo(o2.euclideanDistance);
	}
	
}
