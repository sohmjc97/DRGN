package application;

import java.util.HashMap;

public class Skills {

	public static enum Evo_Traits {
		IRON_SCALES,
		SPIKED_TAIL,
		POISON_FANG,
		CAMO_SCALES,
		SILENT_WINGS,
		FIRE_BREATH,
		STRONG_VOICE
	}
	
	public static enum Moves {
		CRY,
		TACKLE,
		HIDE,
		GRAPPLE,
		GROWL,
		BITE,
		SNEAK,
		THREAT,
		CLAW,
		DODGE,
		TAIL_SLAM,
		FLY,
		ROAR,
		HORN_RAM,
		EVADE,
		WING_SLAM,
		DIVE_BOMB,
		DOG_FIGHT,
		SNEAK_ATTACK,
		SIREN_SONG,
		FIRE_BREATH,
		SILENT_FLIGHT
	}
	
	private final Dragon owner;
	private HashMap<Moves, Integer> skillsSet = new HashMap<Moves, Integer>();
	private int points_spent = 0;
	private int points_available = 0;
	
	public Skills (Dragon dragon) {
		owner = dragon;
		points_available = dragon.get_experience();
	}
	
	public boolean hasPointsToSpend() {
		points_available = owner.get_experience() - points_spent;
		return points_available > 0;
	}
	
	public int getAvailablePoints() {
		points_available = owner.get_experience() - points_spent;
		return points_available;
	}
	
	public void updateSkill(Moves move) {
		points_spent++;
		points_available = owner.experience - points_spent;
		if(skillsSet.containsKey(move)) {
			skillsSet.put(move, (skillsSet.get(move)+1));
		}
		else {
			skillsSet.put(move, 1);
		}
		switch(move) {
			case BITE:
			case CLAW:
			case GRAPPLE:
			case TACKLE:
			case TAIL_SLAM:
			case WING_SLAM:
			case HORN_RAM:
			case DOG_FIGHT:
				owner.attack++;
				break;
			case HIDE:
			case DODGE:
			case SNEAK:
			case EVADE:
			case DIVE_BOMB:
				owner.speed++;
				break;
			case CRY:
			case GROWL:
			case THREAT:
			case ROAR:
			case SIREN_SONG:
				owner.defense++;
				break;
		default:
			break;		
		}
	}
	
	public int getPointsBySkill(Moves move) {
		if(skillsSet.get(move) == null) {
			return 0;
		}
		return skillsSet.get(move);
	}
	
	public int getPointsBySkill(String move) {
		Moves myMove = convertMovesFromString(move);
		if(skillsSet.get(myMove) == null) {
			return 0;
		}
		return skillsSet.get(myMove);
	}
	
	public Moves convertMovesFromString(String move) {
		Moves myMove = null;
		if(move.toString().equals("Evasive Maneuvers")) {
			myMove = Moves.EVADE;
		}
		switch (move) {
		case "Tackle":
			return Moves.TACKLE;
		case "Hide":
			return Moves.HIDE;
		case "Bite":
			return Moves.BITE;
		case "Claw":
			return Moves.CLAW;
		case "Cry":
			return Moves.CRY;
		case "Growl":
			return Moves.GROWL;
		case "Evasive Maneuvers":
			return Moves.EVADE;
		case "Roar":
			return Moves.ROAR;
		case "Threat Display":
			return Moves.THREAT;
		case "Fly":
			return Moves.FLY;
		case "Sneak":
			return Moves.SNEAK;
		case "Dodge":
			return Moves.DODGE;
		case "Dog Fight":
			return Moves.DOG_FIGHT;
		case "Siren Song":
			return Moves.SIREN_SONG;
		case "Tail Slam":
			return Moves.TAIL_SLAM;
		case "Wing Slam":
			return Moves.WING_SLAM;
		case "Horn Ram": 
			return Moves.HORN_RAM;
		case "Dive Bomb":
			return Moves.DIVE_BOMB;
		case "Grapple":
			return Moves.GRAPPLE;
		case "Sneak Attack":
			return Moves.SNEAK_ATTACK;
		}
		/*for (Moves m: Moves.values()) {
			/*if((m.toString().toCharArray()[0] + m.toString().toLowerCase().substring(1)).equals(move)) {
				return m;
			}
			if((m.toString().toCharArray()[0] + m.toString().toLowerCase().substring(1)).replace("_", " ").equals(move)) {
				return m;
			}*/
			
		//}*/
		
		if(myMove == null) {
			System.out.println("Move for " + move + " could not be found.");
		}
		else {
			System.out.println("Move for " + move + " was found.");
		}
		
		return myMove;
	}
	
}
