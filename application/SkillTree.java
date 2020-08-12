package application;

import java.util.ArrayList;
import java.util.HashMap;

import application.Creature.Gender;
import application.Skills.Moves;

public class SkillTree {

	//Skills
	//Branches 
	//Classes
	/*
	public static void main(String[] args) {
		SkillTree myTree = new SkillTree("WaterBending", null);
		Branch heal = myTree.addBranch("Healing");
		Branch ice = myTree.addBranch("Ice");
		Branch traditional = myTree.addBranch("Traditional");
		
		heal.addBranchDescrip("You begin to study the healing arts. You will learn to heal injuries " +
		" and save lives with your waterbending."); 
		
		ice.addBranchDescrip("You begin to study the frozen art of icebending. A fiercesine offensive and " +
		" defensive style, icebending also lends itself to creative uses.");
		
		traditional.addBranchDescrip("You begin to study traditional waterbending. This style focuses on the forces " +
		" of moving water--the push and the pull-- which is as unstoppable as the tide or the tsunami." );
		
		Leaf mend_wound = heal.addLeaf("Close Wound");
		mend_wound.addDescription("You can use your hands to close an open wound.");
		mend_wound.addMasterThreshold(5);
		
		Leaf cure_burn = heal.addLeaf("Cure Burn");
		mend_wound.addAfterLeaf(cure_burn);
		cure_burn.addBeforeLeaf(mend_wound);
		cure_burn.addUnlockCondition(mend_wound, 3);
		cure_burn.addMasterThreshold(5);
		cure_burn.addDescription("You can use your bending to cure even the most severe burns.");
		
		
	}
	
	public static enum Classes {
		WEAPON_USER,
		HAND_TO_HAND,
		WATER_BENDER,
		EARTH_BENDER,
		FIRE_BENDER,
		AIR_BENDER
	}*/
	
	private final String tree_name;
	private Dragon m_dragon;
	
	private ArrayList<Branch> branches = new ArrayList<Branch>();
	
	public SkillTree(Dragon dragon) {
		tree_name = dragon.get_name() + "'s Skill Tree";
		m_dragon = dragon;
		Branch stealth_branch = addBranch("Stealth Branch");
		stealth_branch.addBranchDescrip("Stealth moves will help dragons sneak up on prey and avoid predators.");
		
		Leaf hide = stealth_branch.addLeaf("Hide");
		hide.addMasterThreshold(5);
		hide.setPoints(dragon.getSkills().getPointsBySkill(Moves.HIDE));
		hide.addDescription("Learn how to find a hiding place to avoid being spotted by predators.");
		
		Leaf sneak = stealth_branch.addLeaf("Sneak");
		hide.addAfterLeaf(sneak);
		sneak.addBeforeLeaf(hide);
		sneak.addDescription("Learn how to creep around without drawing attention to yourself.");
		sneak.addUnlockCondition(hide, 3);
		sneak.addMasterThreshold(5);
		sneak.setPoints(dragon.getSkills().getPointsBySkill(Moves.SNEAK));
		
		Leaf dodge = stealth_branch.addLeaf("Dodge");
		dodge.addBeforeLeaf(sneak);
		sneak.addAfterLeaf(dodge);
		dodge.addDescription("Learn how to dodge attacks from prey, predators, and other dragons.");
		dodge.addUnlockCondition(sneak, 3);
		dodge.addMasterThreshold(6);
		dodge.setPoints(dragon.getSkills().getPointsBySkill(Moves.DODGE));
		
		Leaf sneak_attack = stealth_branch.addLeaf("Sneak Attack");
		sneak_attack.addBeforeLeaf(dodge);
		dodge.addAfterLeaf(sneak_attack);
		sneak_attack.addDescription("Learn how to sneak up and catch an enemy by surprise.");
		sneak_attack.addUnlockCondition(dodge, 4);
		sneak_attack.addMasterThreshold(8);
		sneak_attack.setPoints(dragon.getSkills().getPointsBySkill(Moves.SNEAK_ATTACK));
		
		Leaf evade = stealth_branch.addLeaf("Evasive Maneuvers");
		evade.addBeforeLeaf(sneak_attack);
		sneak_attack.addAfterLeaf(evade);
		evade.addDescription("Learn how to fly evasively to avoid being caught.");
		evade.addUnlockCondition(sneak_attack, 5);
		evade.addMasterThreshold(10);
		evade.setPoints(dragon.getSkills().getPointsBySkill(Moves.EVADE));
		//evade.lockManually();
		
		Leaf dive_bomb = stealth_branch.addLeaf("Dive Bomb");
		dive_bomb.addBeforeLeaf(evade);
		sneak_attack.addAfterLeaf(dive_bomb);
		dive_bomb.addDescription("Learn how to drop on your enemies as an unseen shadow of death from above.");
		dive_bomb.addUnlockCondition(evade, 6);
		dive_bomb.addMasterThreshold(10);
		dive_bomb.setPoints(dragon.getSkills().getPointsBySkill(Moves.DIVE_BOMB));
		//dive_bomb.lockManually();
		
		Branch attack_branch = addBranch("Attack Branch");
		attack_branch.addBranchDescrip("Attacks moves will help prey take down prey, fend off predators, and fight other dragons.");
		
		Leaf tackle = attack_branch.addLeaf("Tackle");
		tackle.addDescription("Learn how to use your body weight to take down prey and rival dragons.");
		tackle.setPoints(dragon.getSkills().getPointsBySkill(Moves.TACKLE));
		tackle.addMasterThreshold(5);
		
		Leaf bite = attack_branch.addLeaf("Bite");
		bite.addDescription("Learn how to sink your fangs into prey or rival dragons.");
		bite.setPoints(dragon.getSkills().getPointsBySkill(Moves.BITE));
		bite.addBeforeLeaf(tackle);
		tackle.addAfterLeaf(bite);
		bite.addUnlockCondition(tackle, 3);
		bite.addMasterThreshold(7);
		
		Leaf claw = attack_branch.addLeaf("Claw");
		claw.addDescription("Learn how to use your claws to tear into prey or rival dragons.");
		claw.setPoints(dragon.getSkills().getPointsBySkill(Moves.CLAW));
		claw.addBeforeLeaf(bite);
		bite.addAfterLeaf(claw);
		claw.addUnlockCondition(bite, 5);
		claw.addMasterThreshold(8);
		
		Leaf grapple = attack_branch.addLeaf("Grapple");
		grapple.addDescription("Learn how to wrestle prey or rival dragons to the ground.");
		grapple.setPoints(dragon.getSkills().getPointsBySkill(Moves.GRAPPLE));
		grapple.addBeforeLeaf(claw);
		claw.addAfterLeaf(grapple);
		grapple.addUnlockCondition(claw, 6);
		grapple.addMasterThreshold(10);
		
		Leaf tail = attack_branch.addLeaf("Tail Slam");
		tail.addDescription("Learn how to use your tail as a weapon to slam and stun prey or rivals.");
		tail.addBeforeLeaf(grapple);
		tail.setPoints(dragon.getSkills().getPointsBySkill(Moves.TAIL_SLAM));
		grapple.addAfterLeaf(tail);
		tail.addUnlockCondition(grapple, 7);
		tail.addMasterThreshold(12);
		
		Leaf wing = attack_branch.addLeaf("Wing Slam");
		wing.addDescription("Learn how to use your powerful wings to batter enemies or rivals.");
		wing.setPoints(dragon.getSkills().getPointsBySkill(Moves.WING_SLAM));
		wing.addBeforeLeaf(tail);
		tail.addAfterLeaf(wing);
		wing.addUnlockCondition(tail, 8);
		wing.addMasterThreshold(14);
		
		Leaf horn = attack_branch.addLeaf("Horn Ram");
		horn.addDescription("Learn how to weaponize your horn to strike rivals and prey alike.");
		horn.setPoints(dragon.getSkills().getPointsBySkill(Moves.HORN_RAM));
		horn.addBeforeLeaf(wing);
		wing.addAfterLeaf(horn);
		horn.addUnlockCondition(wing, 10);
		horn.addMasterThreshold(15);
		
		Leaf dog_fight = attack_branch.addLeaf("Dog Fight");
		dog_fight.addDescription("Learn how to dominate the skies with the ultimate aerial combat.");
		dog_fight.setPoints(dragon.getSkills().getPointsBySkill(Moves.DOG_FIGHT));
		dog_fight.addBeforeLeaf(horn);
		dog_fight.addBeforeLeaf(evade);
		horn.addAfterLeaf(dog_fight);
		evade.addAfterLeaf(dog_fight);
		dog_fight.addUnlockCondition(horn, 10);
		dog_fight.addUnlockCondition(wing, 12);
		dog_fight.addUnlockCondition(dive_bomb, 10);
		dog_fight.addUnlockCondition(evade, 8);
		
		Branch threat_branch = addBranch("Threat Branch");
		threat_branch.addBranchDescrip("Threat moves will help dragons deter predators and rival dragons.");
		
		Leaf cry = threat_branch.addLeaf("Cry");
		cry.addDescription("Learn how to use your voice to call other dragons to help you.");
		cry.setPoints(dragon.getSkills().getPointsBySkill(Moves.CRY));
		cry.addMasterThreshold(5);
		
		Leaf growl = threat_branch.addLeaf("Growl");
		growl.addDescription("Learn how to use your voice to intimidate predators or rivals.");
		growl.setPoints(dragon.getSkills().getPointsBySkill(Moves.GROWL));
		growl.addBeforeLeaf(cry);
		cry.addAfterLeaf(growl);
		growl.addUnlockCondition(cry, 3);
		growl.addMasterThreshold(6);
		
		Leaf threat = threat_branch.addLeaf("Threat Display");
		threat.addDescription("Learn how to indtimidate predators and rivals with a threatening visual display.");
		threat.addBeforeLeaf(growl);
		threat.setPoints(dragon.getSkills().getPointsBySkill(Moves.THREAT));
		growl.addAfterLeaf(threat);
		threat.addUnlockCondition(growl, 4);
		threat.addMasterThreshold(8);
		
		Leaf roar = threat_branch.addLeaf("Roar");
		roar.addDescription("Learn how to roar to scare off predators and rivals.");
		roar.addBeforeLeaf(threat);
		roar.addBeforeLeaf(growl);
		growl.addAfterLeaf(roar);
		threat.addAfterLeaf(roar);
		roar.addUnlockCondition(growl, 6);
		roar.addUnlockCondition(threat, 6);
		roar.addMasterThreshold(12);
		roar.setPoints(dragon.getSkills().getPointsBySkill(Moves.ROAR));
		
		Leaf song = threat_branch.addLeaf("Siren Song");
		song.addBeforeLeaf(roar);
		song.addBeforeLeaf(cry);
		cry.addAfterLeaf(song);
		roar.addAfterLeaf(song);
		song.addDescription("Learn to sing a song that will incapacitate your foes.");
		song.addUnlockCondition(roar, 12);
		song.addUnlockCondition(cry, 6);
		song.setPoints(dragon.getSkills().getPointsBySkill(Moves.SIREN_SONG));
		song.addMasterThreshold(15);
		
	}
	
	public String getTreeName() {
		return tree_name;
	}
	
	public Branch addBranch(String name) {
		Branch new_branch = new Branch(name,this);
		branches.add(new_branch);
		return new_branch;
	}
	
	public ArrayList<Branch> getBranches() {
		return branches;
	}
	
	public class Branch {
		
		private final String branch_name;
		private final SkillTree parent_tree;
		private String branch_descrip = "";
		private ArrayList<Leaf> leafs = new ArrayList<Leaf>();
		private boolean mastered = false;
		
		public Branch(String name, SkillTree parent) {
			branch_name = name;
			parent_tree = parent;
		}
		
		public Branch(String name, String descrip, SkillTree parent) {
			branch_name = name;
			parent_tree = parent;
			branch_descrip = descrip;
		}
		
		public void addBranchDescrip(String descrip) {
			branch_descrip = descrip;
		}
		
		public SkillTree getParentTree() {
			return parent_tree;
		}
		
		public String getBranchName() {
			return branch_name;
		}
		
		public String getBranchDescrip() {
			return branch_descrip;
		}
		
		public boolean checkMastered() {
			for (Leaf f: leafs) {
				if(f.getLeafPoints() < f.pointsToMaster()) {
					mastered = false;
					return false;
				}
			}
			mastered = true;
			return true;
		}
		
		public Leaf addLeaf(String name) {
			Leaf new_leaf = new Leaf(name, parent_tree, this);
			leafs.add(new_leaf);
			return new_leaf;
		}
		
		public ArrayList<Leaf> getLeafs() {
			return leafs;
		}
		
	}
	
	public class Leaf {
		
		private SkillTree parent_tree;
		private Branch parent_branch;
		private ArrayList<Leaf> before_leafs = new ArrayList<Leaf>();
		private ArrayList<Leaf> after_leafs = new ArrayList<Leaf>();
		private int points = 0; 
		private int points_to_master;
		private HashMap<Leaf, Integer> unlock_condition = new HashMap<Leaf, Integer>();
		private HashMap<Object, Object> special_conditions = new HashMap<Object, Object>();
		private boolean manual_lock = false;
		private boolean unlocked = false;
		private String name;
		private String descrip;
		
		public Leaf(String aname, SkillTree parent, Branch bparent) {
			name = aname;
			parent_tree = parent;
			parent_branch = bparent;
		}
		
		public void addBeforeLeaf(Leaf bLeaf) {
			if(!before_leafs.contains(bLeaf)) {
				before_leafs.add(bLeaf);
			}
		}
		
		public void addAfterLeaf(Leaf aLeaf) {
			if(!after_leafs.contains(aLeaf)) {
				after_leafs.add(aLeaf);
			}
		}
		
		public void addUnlockCondition(Leaf leaf, int points) {
			if(before_leafs.contains(leaf)) {
				unlock_condition.put(leaf, points);
			}
		}
		
		public int getUnlockCondition(Leaf leaf) {
			return unlock_condition.get(leaf);
		}
		
		public void addSpecialUnlockCondition(Object object, Object value) {
			special_conditions.put(object, value);
		}
		
		public boolean checkUnlocked() {
			
			if(manual_lock) {
				return false;
			}
			
			if(unlocked) {
				return true;
			}
			
			for (Leaf l: unlock_condition.keySet()) {
				if (l.points < unlock_condition.get(l)) {
					unlocked = false;
					return false;
				}
			}
			
			unlocked = true;
			return true;
		}
		
		public void lockManually() {
			unlocked = false;
			manual_lock = true;
		}
		
		public void addPoint() {
			if(unlocked) {
				points++;
				Skills skill = m_dragon.getSkills();
				Moves move = skill.convertMovesFromString(name);
				skill.updateSkill(move);
			}
		}
		
		public boolean isLocked() {
			return !unlocked;
		}
		
		public void setPoints(int num) {
			points = num;
		}
		
		public void addMasterThreshold(int num) {
			if (num > 0) {
				points_to_master = num;
			}
		}
		
		public boolean mastered() {
			return points >= points_to_master;
		}
		
		public void addDescription(String description) {
			descrip = description;
		}
		
		public String getLeafName() {
			return name;
		}
		
		public String getLeafDescrip() {
			return descrip;
		}
		
		public int getLeafPoints() {
			return points;
		}
		
		public int pointsToMaster() {
			return points_to_master;
		}
		
		public ArrayList<Leaf>  getBeforeLeafs() {
			return before_leafs;
		}
		
		public ArrayList<Leaf> getAfterLeafs() {
			return after_leafs;
		}
		
		public SkillTree getParentTree() {
			return parent_tree;
		}
		
		public Branch getParentBranch() {
			return parent_branch;
		}
		
	}
}

