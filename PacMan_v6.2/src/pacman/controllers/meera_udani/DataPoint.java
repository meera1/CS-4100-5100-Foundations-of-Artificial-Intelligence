package pacman.controllers.meera_udani;

import pacman.game.Constants.MOVE;

public class DataPoint {
	
	public double shortestDistBetGhostAndPacman,
	NearestGhostIfEdible,
	closestPowerPillDistFromPacman,
	pacmanEaten,
	ghostEaten,
	pacmanLastMove, 
	nearestGhostLastMove;
	
	MOVE moveToTake;
	
	
	public DataPoint(double shortestDistBetGhostAndPacman, double nearestGhostIfEdible,
			double closestPowerPillDistFromPacman, double pacmanEaten, double ghostEaten, double pacmanLastMove,
			double nearestGhostLastMove, MOVE moveToTake) {
		super();
		this.shortestDistBetGhostAndPacman = shortestDistBetGhostAndPacman;
		this.NearestGhostIfEdible = nearestGhostIfEdible;
		this.closestPowerPillDistFromPacman = closestPowerPillDistFromPacman;
		this.pacmanEaten = pacmanEaten;
		this.ghostEaten = ghostEaten;
		this.pacmanLastMove = pacmanLastMove;
		this.nearestGhostLastMove = nearestGhostLastMove;
		this.moveToTake = moveToTake;
	}
	
	public DataPoint(String line[]){
		this.shortestDistBetGhostAndPacman = Double.parseDouble(line[0]);
		this.NearestGhostIfEdible = Double.parseDouble(line[1]);
		this.closestPowerPillDistFromPacman = Double.parseDouble(line[2]);
		this.pacmanEaten = Double.parseDouble(line[3]);
		this.ghostEaten = Double.parseDouble(line[4]);
		this.pacmanLastMove = Double.parseDouble(line[5]);
		this.nearestGhostLastMove =Double.parseDouble(line[6]);
		this.moveToTake = MOVE.valueOf(line[7]);
		
	}
	
	public DataPoint(double feature[]){
		this.shortestDistBetGhostAndPacman = feature[0];
		this.NearestGhostIfEdible = feature[1];
		this.closestPowerPillDistFromPacman = feature[2];
		this.pacmanEaten = feature[3];
		this.ghostEaten = feature[4];
		this.pacmanLastMove = feature[5];
		this.nearestGhostLastMove = feature[6];
		this.moveToTake = null;
		
	}

}

