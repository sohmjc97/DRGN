package application;

import java.lang.reflect.InvocationTargetException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

import application.Region.Resources;

public abstract class Creature {
	
	/*
	 * Will include Prey and Apex 
	 * Ideas:
	 * Should the number of eggs laid depend on how available resources are?
	 * Work on egg mechanics/incubation
	 * Should number of eggs laid be an inheritable/mutable trait?
	 * Should sociability be a genetic factor? High sociability meaning bonding to others and caring (hunting, defending) for them?
	 * Should a creature keep track of its partners and care for them when they can't (sick, injured, etc)?
	 * Should males even have a season limit?
	 */
	
	protected static ArrayList<Creature> population = new ArrayList<Creature>();
	protected static int total_count = 0; 
	
	@SuppressWarnings("serial")
	protected class ActionIllegalException extends Exception {
		
		public ActionIllegalException(String errorMessage) {
			super(errorMessage);
		}
		
	}
	
	protected static enum Gender {
		MALE,
		FEMALE
	}
	
	public static enum Stage {
		EGG, 
		HATCHLING, 
		ADOLESCENT,
		ADULT,
		ELDER
	}
	
	protected int id;
	protected Gender gender;
	protected Skin skin; 
	protected Region current_region; 
	protected Stage stage = Stage.EGG;
	protected Class my_class = null;
	protected boolean famine; 
	protected boolean hasGrown = false; 
	
	protected boolean isDead = false;
	
	protected double growth = 0; 
	protected double current_season_growth = 0;
	protected boolean new_season = true; 
	protected Stage original_stage; 
	protected int experience = 0; 
	protected int life_span = 35; 
	
	protected double max_hp = 10.0; 
	protected double current_hp = 10.0;
	
	protected double hunger = 100.0;
	protected double thirst = 100.0;
	protected double stamina = 100.0;
	
	protected Double attack = 1.0;
	protected double attack_growth;
	protected Double defense = 1.0;
	protected double defense_growth;
	protected Double speed = 1.0; 
	protected double speed_growth;
	protected double aggression;
	
	protected ArrayList<Virus> infections = new ArrayList<Virus>();
	protected ArrayList<Virus> immunities = new ArrayList<Virus>();
	protected double immune_sys;
	protected boolean isVisiblyIll;
	protected int season_limit;
	protected int curr_season_count = 0; 
	
	protected ArrayList<Creature> ancestors = new ArrayList<Creature>();
	protected ArrayList<Creature> offspring = new ArrayList<Creature>(); 
	protected ArrayList<Creature> descendants = new ArrayList<Creature>();
	protected Creature parent_one = null;
	protected Creature parent_two = null;
	protected String name;
	protected double growth_rate = 0.1; 
	
	protected abstract void scavenge();
	protected abstract void hunt();

	protected void run() {
		boolean done = false; 
		Stage current_stage = stage; 
		double current_growth = growth; 
		//System.out.println("Run being called.");
		do {
			
			if(stage == Stage.EGG) {
				//growth+=1;
				hunger = 100;
				thirst = 100;
				stamina = 100; 
				grow();  
				//done = true; 
				//hasGrown = true;
				break; 
			}
			
			while((current_hp < max_hp/2 | isSatisfied() | stamina < 30) & !isDead & hunger > 40 & thirst > 40) {
				rest();
			}
			
			while (hunger > 40 & !isSatisfied() & thirst > 40 & stamina > 30 & !isDead & current_hp > max_hp/2) {
				hunt();
				if (current_stage != stage | (growth - current_growth) >= 5) {
					done = true; 
					hasGrown = true;
					break; 
				}
			}
			if (thirst <= 40 & !isDead) {
				drink();
			}
			if (hunger <= 40 & !isDead) {
				scavenge();
			}
			if (!isDead & (stamina <= 30 | isSatisfied())) {
				rest();
			}
			
			if (current_stage != stage | (growth - current_growth) >= 5) {
				done = true; 
				hasGrown = true;
				break; 
			}
			
			if(famine) {
				//migrate();
			}
			
			current_region.replenish_resource(Resources.PREY_FOOD, 1);
			
		} while (done == false & isDead == false);
	}
	
	protected boolean run_once() {
		
		if (new_season) { 
			original_stage = stage; 
			current_season_growth = growth; 
			new_season = false; 
		}
		
		if(!hasGrown) {
			//System.out.println(name + " hasn't grown yet: " + growth);
			
			if(stage == Stage.EGG) {
				//growth+=1;
				hunger = 100;
				thirst = 100;
				stamina = 100; 
				grow();  
				//hasGrown = true; 
				//new_season = true;
			}
			else {
				while((current_hp < max_hp/2 | isSatisfied() | stamina < 30) & !isDead & hunger > 40 & thirst > 40) {
					rest();
				}
				//System.out.println("Hunger: " + hunger);
				while (hunger > 40 & !isSatisfied() & thirst > 40 & stamina > 30 & !isDead & current_hp > max_hp/2) {
					
					hunt();
					if (original_stage != stage | (growth - current_season_growth) >= 5) {
						//System.out.println(name + " has already grown yet: " + growth);
						hasGrown = true; 
						new_season = true; 
						return true;
					}
					else {
						//System.out.println(name + " is not done growing yet: " + growth);
					}
					
				}
				if (thirst <= 40 & !isDead) {
					drink();
				}
				if (hunger <= 40 & !isDead) {
					scavenge();
				}
				if(stamina <= 30 & !isDead | isSatisfied()) {
					rest();
				}
			}

			if (original_stage != get_stage() | (growth - current_season_growth) >= 5) {
				hasGrown = true; 
				new_season = true;
			}
			
			if(famine) {
				Random rand = new Random();
				Double chance = rand.nextInt(101) + aggression*10;
				if (chance > 60) {
					//migrate();
					famine = false; 
				}
			}
			
			if(hasGrown & (my_class != Prey.class)) {
				if(original_stage == Stage.EGG | this.stage == Stage.HATCHLING) {
					System.out.println(name + " has hatched.");
				}
				else if (original_stage == Stage.HATCHLING & this.stage == Stage.ADOLESCENT) {
					System.out.println(name + " has reached adolescence.");
				}
				else if (original_stage == Stage.ADOLESCENT & this.stage == Stage.ADULT) {
					System.out.println(name + " has reached adulthood.");
				}
				else if (original_stage == Stage.ADULT & this.stage == Stage.ELDER) {
					System.out.println(name + " has become an elder.");
				}
			}
			current_region.replenish_resource(Resources.PREY_FOOD, 1);
		}
		else {
			//System.out.println(name + " has grown: " + growth);
		}
		
		
		
		return hasGrown; 
			
	}
	
	public boolean hasGrown() {
		return hasGrown;
	}
	
	protected void migrate() {
		
		Random rand = new Random();
		
		if(current_region.get_connecting_regions().size() != 0) {
			int seed = rand.nextInt(current_region.get_connecting_regions().size());
			if(current_region.get_connecting_regions().size() == 2) {
				for(Region r: current_region.get_connecting_regions()) {
					if (r != current_region) {
						this.set_current_region(r);
					}
				}
			}
			else {
				this.set_current_region(current_region.get_connecting_regions().get(seed));
			}
		}
		else {
			Region new_region = new Region("FarawayRegion");
			Main.regions.add(new_region);
			this.set_current_region(new_region);
		}
		
	}
	
	protected String get_stats() {
		String stats = ""; 
		String parents = "";
		if (parent_one == null) {
			parents = "Spawned/Cloned";
		}
		else {
			parents = parent_one.get_name() + " & " + parent_two.get_name();
		}
		String babies = "";
		for (Creature i: offspring) {
			babies += i.get_name() + ", ";
		}
		
		String ancestry = "";
		for (Creature e: ancestors) {
			ancestry += e.get_name() + ", ";
		}
		
		String progeny = "";
		for (Creature e: descendants) {
			progeny += e.get_name() + ", ";
		}
		
		String viruses = "";
		for (Virus e: infections) {
			viruses += e.get_id() + ", ";
		}
		
		String immunes = "";
		for (Virus e: immunities) {
			immunes += e.get_id() + ", ";
		}
		
		String live = "";
		ArrayList<Creature> living_descendants = new ArrayList<Creature>(); 
		for (Creature e: this.descendants) {
			
			if (!e.isDead) {
				living_descendants.add(e);
				live += e.name + ", ";
			}
			else {
				living_descendants.remove(e);
			}
			
		}
		
		//System.out.println(attack.toString());
		String attk = round_decimal(attack);
		String attk_growth = round_decimal(attack_growth);
		String dfns = round_decimal(defense);
		String dfns_growth = round_decimal(defense_growth);
		String spd = round_decimal(speed);
		String spd_growth = round_decimal(speed_growth);
		
		stats +=  "Dragon ID: " + name + "\n"
				+ "Gender: " + gender + "\n"
				+ "Skin: " + skin.describe()
				+ "Growth(/" + life_span + "): " + stage + " / " + growth + "\n" 
				+ "Parents: " + parents + "\n"
				+ "Ancestors: " + ancestry + "\n" 
				+ "Health: " + current_hp + "/" + max_hp +"\n"
				+ "Hunger: " + hunger + "/100 \n"
				+ "Thirst: " + thirst + "/100 \n"
				+ "Stamina: " + stamina + "/100 \n"
				+ "Experience: " + experience + "\n"
				+ "Attack/Defense/Speed: " + attk + "/" + dfns + "/" + spd + "\n"
				+ "Growth (" + round_decimal(get_stat_coeff()) + "): " + attk_growth + " / " + dfns_growth + " / "+ spd_growth + "\n"
				+ "Aggression: " + round_decimal(aggression) + "\n"
				+ "Dead: " + isDead + "\n"
				+ "Season Limit: " + this.get_season_limit() + "\n"
				+ "Offspring: " + babies + "\n"
				+ "Descendants(" + living_descendants.size() + " / " + descendants.size() + "): " + progeny + "\n"
				+ "Living: " + live + "\n"
				+ "Immune Sys: " + round_decimal(immune_sys) + "\n"
				+ "Infections: " + viruses + "\n"
				+ "Immunities: " + immunes  ;

		return stats;
	}
	
	protected boolean check_infections() {
		//return true if killed
		ArrayList<Virus> recovered = new ArrayList<Virus>();
		double resist_coeff = immune_sys - (max_hp-current_hp)/1000 - (100 - hunger)/1000 - (100 - thirst)/1000 - (100 - stamina)/1000;
		for (Virus v: infections) {
			if (v.get_resil() < resist_coeff) {
				recovered.add(v);
				v.remove_infected(this, false);
				setVisiblyIll(false); 
				immunities.add(v);
				//System.out.println(this.name + " recovereed from Virus #" + v.get_id());
			}
			else {
				current_hp = current_hp - max_hp*v.get_death();
				if (current_hp <= 0) {
					System.out.println(this.name + " succumbed to disease.");
					return true;
				}
			}
		}
		for (Virus v: recovered) {
			infections.remove(v);
		}
		return false; 
	}
	
	protected boolean infect(Creature two) {
		if (this.isDead | two.isDead) {
			return false; 
		}
		Random rand = new Random();
		double resist_coeff = immune_sys - (max_hp-current_hp)/1000 - (100 - hunger)/1000 - (100 - thirst)/1000 - (100 - stamina)/1000;
		//int g = Integer.parseInt(two.get_growth())
		for (Virus v: infections) {
			Virus w;
			if (two.get_immunities().contains(v) | two.infections.contains(v)) {
				break;
			}
			if (v.get_trans() > resist_coeff & !two.get_infections().contains(v)) {
				if (v.get_adapt() > rand.nextDouble() & Main.diseases.size() < 11) {
					w = new Virus(v);
				}
				else {
					w = v;
				}
				w.add_infected(two);
				two.infections.add(w);
				
				if (w.get_visil() > resist_coeff) {
					two.setVisiblyIll(true);
				}
				//System.out.println(this.name + " infected " + two.name + " with Virus #" + w.get_id());
				return true;
			}
			else {
				return false;
			}

		}
		
		return false;
	}
	
	protected void rest() {
		
		//System.out.println(name + " sleeps.");
		heal();
		grow();
		stamina = 100;
		hunger = hunger - 20;
		thirst = thirst - 20; 
		check_status();
		
	}
	
	protected void eat(Creature d) {
		
		System.out.println(name + " caught and ate (" + d.get_stage().toString() + ") " + d.get_name());
		
		if (stage == Stage.ADULT | stage == Stage.ELDER) {
			int share = 0;
			if (d.isAdult() | d.stage == Stage.ELDER) {
				share = 200;
			}
			else if (d.stage == Stage.ADOLESCENT) {
				share = 100;
			}
			else if (d.stage == Stage.HATCHLING) {
				share = 75;
			}
			else {
				share = 50;
			}
			
			if(this.offspring.isEmpty()) {
				hunger+= share;
			}
			else {
				int kids_share = share/offspring.size();
				int leftover = 0; 
				int kid_count = 0;
				for (Creature child: offspring) {
					if(child.isDead | child.stage != Stage.HATCHLING | child.hunger >= 200) {
						leftover += kids_share;
					}
					else {
						kid_count++;
						System.out.println(name + " fed " + child.get_name());
						child.hunger += kids_share;
					}
				}
				this.hunger += leftover;
			}
			
		}
		else if (stage == Stage.ADOLESCENT) {
			if (d.isAdult() | d.stage == Stage.ELDER) {
				hunger += 300;
			}
			else if (d.stage == Stage.ADOLESCENT) {
				hunger += 200;
			}
			else if (d.stage == Stage.HATCHLING) {
				hunger += 100;
			}
			else {
				hunger += 75;
			}
		}
		else {
			if (d.isAdult() | d.stage == Stage.ELDER) {
				hunger += 400;
			}
			else if (d.stage == Stage.ADOLESCENT) {
				hunger += 300;
			}
			else if (d.stage == Stage.HATCHLING) {
				hunger += 200;
			}
			else {
				hunger += 150;
			}
		}
		
		famine = false; 
		
	}
	
	protected boolean isSatisfied() {
		
		for (Creature c: this.offspring) {
			if (!c.isSatisfied() & c.isMinor()) {
				return false; 
			}
		}
		
		if(this.hunger >= 200 | this.isDead) {
			return true;
		}
		else {
			return false;
		}
		
	}
	
	protected void drink() {
		
		//System.out.println(name + " takes a drink.");
		//heal();
		
		Random rand = new Random();
		if(rand.nextInt(100) < 5) {
			Virus.manual_infect(this);
		}
		
		grow();
		thirst = 100;
		stamina = stamina - 10; 
		//hunger = hunger - 10;
		check_status();
		
	}
	
	protected void heal() {
		
		double regen = 5; 
		regen += hunger/20 + thirst/20 + current_hp/20 + stamina/20;
		current_hp += regen;
		if (current_hp > max_hp) {
			current_hp = max_hp;
		}
		//System.out.println(name + " has regained " + regen + "health. \n");
	
	}
	
	protected void be_defended(Creature attacker) {
		//their parents will attempt to protect them from predators, if they are alive, healthy, and aggressive & by chance and number of minor children
		Random rand = new Random();
		if(parent_one != null & parent_two != null) {
			if(!this.parent_one.isDead()) {
				
				if(this.parent_one.isHealthy() | parent_one.aggression >= 0.5) {
					
					int seed = rand.nextInt(100);
					int chance = 99;
					for(Creature c: parent_one.get_descendants()) {
						if(c.isMinor() & !c.isDead() & c != this) {
							chance = chance - 3; 
						}
					}
					
					if(chance >= seed) {
						System.out.println(name + "'s parent " + parent_one.get_name() + " defends its (" + this.get_stage() + ") child against (" + attacker.get_stage() + ") " + attacker.get_name() );
						parent_one.fight(attacker);
						if(parent_one.isDead) {
							attacker.eat(parent_one);
						}
					}
					
				}
				
			}
			if (attacker.isDead() | (!attacker.isHealthy() & attacker.get_aggression() < 0.5) | attacker.isSatisfied()) {
				return;
			}
			else if(!this.parent_two.isDead()) {
				if(this.parent_two.isHealthy() | parent_two.aggression >= 0.5) {
					
					int seed = rand.nextInt(100);
					int chance = 99;
					for(Creature c: parent_two.get_descendants()) {
						if(c.isMinor() & !c.isDead() & c != this) {
							chance = chance - 3; 
						}
					}
					
					if(chance >= seed) {
						System.out.println(name + "'s parent " + parent_two.get_name() + " defends its  (" + this.get_stage() + ") child against (" + attacker.get_stage() + ") " + attacker.get_name() );
						parent_two.fight(attacker);
						if(parent_two.isDead) {
							attacker.eat(parent_two);
						}
					}
					
				}
			}
		}
	}
	
	protected Creature play_fight(Creature enemy) {
		
		boolean green = true;
		if (!this.isHealthy() | !enemy.isHealthy()) {
			green = false; 
		}
		else if (this.isVisiblyIll | enemy.isVisiblyIll) {
			green = false;
		}
		else if (this.gender != enemy.gender) {
			green = false;
		}
		else {
			String feed = name + " is rough housing with " + enemy.get_name(); 
			double one_chance = 0;
			double two_chance = 0; 
			
			//factor in growth 
			
			one_chance += this.growth;
			two_chance += enemy.growth;
			
			//factor in each skill 
			
			one_chance += this.attack;
			one_chance += this.defense;
			one_chance += this.speed;
			
			two_chance += enemy.attack;
			two_chance += enemy.defense;
			two_chance += enemy.speed;
			
			//factor in experience 
			
			one_chance += this.experience;
			two_chance += enemy.experience;
			
			//factor in health, hunger, thirst, and stamina 
			one_chance = one_chance - (max_hp-current_hp) - (100-stamina) - (100-hunger) - (100 - thirst);
			two_chance = two_chance - (enemy.max_hp-enemy.current_hp) - (100-enemy.stamina) - (100-enemy.hunger) - (100 - enemy.thirst);
	
			//factor in some element of chance 
			
			for(Virus v: enemy.infections) {
				two_chance = two_chance - 5; 
			}
			
			for(Virus v: this.infections) {
				one_chance = one_chance - 5;
			}
			
			Creature winner = null; 
			Creature loser = null; 
			
			if (one_chance >= two_chance) {
				winner = this; 
				loser = enemy;
			}
			else {
				winner = enemy; 
				loser = this; 
			}
			
			feed += " / " + winner.name + " triumphed over " + loser.get_name();
			
			winner.experience += 5; 
			
			winner.stamina = winner.stamina - 20;
			winner.hunger = winner.hunger - 10; 
			winner.thirst = winner.thirst - 10;
			winner.check_status();
			
			loser.experience += 2; 
			loser.stamina = loser.stamina - 20;
			loser.hunger = loser.hunger - 10; 
			loser.thirst = loser.thirst - 10; 
			loser.check_status();
		
			System.out.println(feed);
			return winner; 
		}
		
		return null; 
		
	}
	
	protected void play(Creature two) {
		if(two.isHealthy() & this.isHealthy()) {
			System.out.println(name + " plays with " + two.get_name());
			experience++;
			stamina = stamina - 20;
			hunger = hunger - 10;
			thirst = thirst - 10; 
			two.experience++;
			two.stamina = two.stamina - 20;
			two.hunger = two.hunger - 10;
			two.thirst = two.thirst - 10;
		}
	}
	
	protected void grow() {
		double new_growth = growth_rate; 
		new_growth = growth_rate * (hunger/100 + thirst/100 + current_hp/100 + stamina/100);
		growth += new_growth;
		Random rand = new Random();
		int chance = rand.nextInt(10) + 1;
		switch(stage) {
			case EGG:
				break;
			case HATCHLING:
				chance = chance - 1; 
				break;
			case ADOLESCENT:
				chance = chance - 2; 
				break;
			case ADULT:
				chance = chance - 3; 
				break;
			case ELDER:
				chance = 0;
				break;
			default:
				break;
		}
		if (chance > 5) {
			defense += defense_growth * (hunger/100 + thirst/100 + current_hp/100 + stamina/100); 
			attack += attack_growth * (hunger/100 + thirst/100 + current_hp/100 + stamina/100);
			speed += speed_growth * (hunger/100 + thirst/100 + current_hp/100 + stamina/100);
		}
		if(stage == Stage.ELDER) {
			defense = defense - defense_growth /(hunger/100 + thirst/100 + current_hp/100 + stamina/100); 
			attack = attack - attack_growth /(hunger/100 + thirst/100 + current_hp/100 + stamina/100);
			speed = speed - speed_growth /(hunger/100 + thirst/100 + current_hp/100 + stamina/100);
			max_hp = max_hp - new_growth;
			immune_sys = immune_sys - .01; 
		}
		else {
			max_hp += new_growth*2;
		}
		stage = get_stage();
	}
	
	protected Creature fight(Creature enemy) {
		//System.out.println(name + " and " + enemy.get_name() + " sqaure off.");
		String feed = name + " vs. " + enemy.get_name(); 
		double one_chance = 0;
		double two_chance = 0; 
		
		//factor in growth 
		
		one_chance += this.growth;
		two_chance += enemy.growth;
		
		//factor in each skill 
		
		one_chance += this.attack;
		one_chance += this.defense;
		one_chance += this.speed;
		
		two_chance += enemy.attack;
		two_chance += enemy.defense;
		two_chance += enemy.speed;
		
		//factor in experience 
		
		one_chance += this.experience;
		two_chance += enemy.experience;
		
		//factor in health, hunger, thirst, and stamina 
		one_chance = one_chance - (max_hp-current_hp) - (100-stamina) - (100-hunger) - (100 - thirst);
		two_chance = two_chance - (enemy.max_hp-enemy.current_hp) - (100-enemy.stamina) - (100-enemy.hunger) - (100 - enemy.thirst);

		//factor in some element of chance 
		
		for(Virus v: enemy.infections) {
			two_chance = two_chance - 5; 
		}
		
		for(Virus v: this.infections) {
			one_chance = one_chance - 5;
		}
		
		/*Random rand = new Random();
		int coin_toss = rand.nextInt(2);
		
		if (coin_toss == 0) {
			one_chance += one_chance/20;
		}
		else {
			two_chance += two_chance/20; 
		}*/
		
		Creature winner = null; 
		Creature loser = null; 
		
		if (one_chance >= two_chance) {
			winner = this; 
			loser = enemy;
		}
		else {
			winner = enemy; 
			loser = this; 
		}
		
		//feed += " / " + winner.name + " triumphed over " + loser.get_name();
		
		winner.experience += determine_experience(loser) + 2; 
		winner.current_hp = winner.current_hp - 10;
		
		winner.stamina = winner.stamina - 20;
		winner.hunger = winner.hunger - 10; 
		winner.thirst = winner.thirst - 10;
		winner.check_status();
		
		loser.experience += determine_experience(winner) - 2; 
		loser.current_hp = loser.current_hp - 30;
		loser.stamina = loser.stamina - 30;
		loser.hunger = loser.hunger - 10; 
		loser.thirst = loser.thirst - 20; 
		loser.check_status();
		
		//get infected from injuries sustained during battle
		
		if(!winner.isDead & winner.current_hp < winner.max_hp/2) {
			Random rand = new Random();
			if(rand.nextInt(100) < 5) {
				Virus.manual_infect(winner);
			}
		}
		
		if(!loser.isDead & loser.current_hp < loser.max_hp/2) {
			Random rand = new Random();
			if(rand.nextInt(100) < 5) {
				Virus.manual_infect(loser);
			}
		}
		if (!winner.isDead & !loser.isDead) {
			System.out.println(winner.name + " triumphed over " + loser.get_name() + " in battle.");
		}
		else if(winner.isDead & !loser.isDead) {
			//feed += " / " + winner.name + " but later died of their injuries.";
			System.out.println("Although " + winner.name + " won the fight, they later died of their injuries, leaving " + loser.get_name() + " as the winner.");
			winner = loser;
		}
		else if (loser.isDead & !winner.isDead) {
			//feed += " / " + winner.name + " killed " + loser.name;
			System.out.println(winner.name + " killed " + loser.get_name() + " in battle.");
		}
		else if (winner.isDead & loser.isDead) {
			//feed+= " / Killed each other in battle.";
			System.out.println(winner.get_name() + " and " + loser.get_name() + " killed each other in battle.");
			return null;
		}
		//System.out.println(feed);
		return winner; 
	}
	
	protected void check_status() {
		if (current_hp > 0 & stamina > 0 & hunger > 0 & thirst > 0) {
			//dragon lives
			if (growth > life_span) {
				//chance to die of old age 
				Random rand = new Random();
				int seed = rand.nextInt(100) + 1;
				if (growth/10 > seed) {
					for (Virus e: infections) {
						e.remove_infected(this, false);
					}
					System.out.println(name + " dies of old age.");
					die(); 
					return;
				}
			}
		}
		else if (stamina <= 0) {
			System.out.println(name + " drops dead from exhaustion.");
			die();
		}
		else if (hunger <= 0) {
			System.out.println(name + " starves to death.");
			die();
		}
		else if (thirst <= 0) {
			System.out.println(name + " dies from dehydration.");
			die();
		}
		else if (current_hp <= 0) {
			System.out.println(name + " succumbs to injury/illness.");
			die();
		}
		
		if (check_infections()) {
			for (Virus e: infections) {
				e.remove_infected(this, true);
			}
			this.infections.clear();
			die();
		}
		
	}
	
	protected void age_randomly() {
		
		Random rand = new Random();
		int age = rand.nextInt(50);
		
		while(this.get_growth_num() < age & !isDead) {
			run();
		}
		
	}
	
	protected void die() {
		
		//System.out.println(name + " has died. \n");
		
		ArrayList<Creature> living_descendants = new ArrayList<Creature>(); 
		for (Creature e: this.descendants) {
			
			if (!e.isDead) {
				living_descendants.add(e);
			}
			else {
				living_descendants.remove(e);
			}
			
		}
		
		for (Virus e: infections) {
			e.remove_infected(this, false);
		}
		isDead = true;
		current_hp = 0; 
		population.remove(this);
		if(Prey.class.isInstance(this)) {
			current_region.get_prey_population().remove(this);
		}
		else if (Dragon.class.isInstance(this)) {
			current_region.get_dragon_population().remove(this);
		}
		else if (Predator.class.isInstance(this)){
			current_region.get_predator_population().remove(this);
		}
		current_region.replenish_resource(Resources.PREY_FOOD, (int)(this.growth));
	}
	
	protected boolean isWellProtected() {
		
		int chance = 0; 
		if(isMinor()) {
			if(parent_one != null) {
				if(!parent_one.isDead & (parent_one.isHealthy() | parent_one.aggression > 0.5)) {
					chance +=50;
					for (Creature p: parent_one.get_offspring()) {
						
						if(p.isMinor() & !p.isDead & p != this) {
							chance = chance - 2;
						}
					}
				}
			}
			if(parent_two != null) {
				if(!parent_two.isDead & (parent_two.isHealthy() | parent_two.aggression > 0.5)) {
					chance += 50;
					for (Creature p: parent_two.get_offspring()) {
						
						if(p.isMinor() & !p.isDead & p != this) {
							chance = chance - 2;
						}
					}
				}
			}
		}
		
		if(chance >= 40) {
			return true;
		}
		else {
			return false; 
		}

	}
	
	protected boolean isHealthy() {
		if(isVisiblyIll) {
			return false;
		}
		else if (current_hp >= max_hp/2 & stamina >= 40 & hunger >= 40 & thirst >= 40) {
			return true;
		}
		return false;
	}
	
	protected boolean isDead() {
		if (isDead) {
			return true;
		}
		else {
			return false;
		}
	}
	
	protected int get_num_minor_offspring() {
		int count = 0;
		for (Creature c: this.offspring) {
			if (!c.isDead & c.isMinor()) {
				count++;
			}
		}
		return count; 
	} 
	
	protected boolean isMinor() {
		if (this.stage == Stage.ADULT | this.stage == Stage.ELDER) {
			return false;
		}
		else {
			return true;
		}
	}
	
	protected boolean isAdult() {
		if (this.stage == Stage.ADULT | this.stage == Stage.ELDER) {
			return true;
		}
		else {
			return false;
		}
	}
	
	protected boolean isRelated(Creature two) {
		
		for (Creature a: ancestors) {
			for (Creature b: two.ancestors) {
				if (a == b) {
					return true; 
				}
			}
		}
		
		if(offspring.contains(two) | ancestors.contains(two) | two.ancestors.contains(this) | two.offspring.contains(this)) {
			return true;
		}
		else {
			return false;
		}
		
	}
	
	public ArrayList<Creature> get_immediate_family(ArrayList<Creature> pop) {
		
		ArrayList<Creature> family = new ArrayList<Creature>(); 
		
		for (Creature a: this.ancestors) {
			//parents, grandparents, etc.
			family.add(a);
		}
		for (Creature o: descendants) {
			//children, grand children, etc.
			family.add(o);
		}
		for (Creature p: pop) {
			//full siblings
			if (p.parent_one == null & this.parent_one == null) {}
			else {
				if (   (p.parent_one == this.parent_one & p.parent_two == this.parent_two) 
														| 
						(p.parent_two == this.parent_one & p.parent_one == this.parent_two)) {
					family.add(p);
				}
			}
		}
		
		return family; 
		
	}
	
	protected void add_ancestors(Creature parent) {
		
		boolean done = false;
		while (!done) {
			if (parent == null) {
				done = true; 
				break;
			}
			else {
				ancestors.add(parent);
				Creature next_1 = parent.parent_one;
				add_ancestors(next_1);
				Creature next_2 = parent.parent_two;
				add_ancestors(next_2);
			}
		}
		
	}
	
	protected ArrayList<Creature> rank_choices (ArrayList<Creature> choices) {
		
		choices = rank(choices);
		if (choices.isEmpty()) {
			return choices;
		}
		else if (choices.size() > this.get_season_limit()) {
			while (choices.size() > this.get_season_limit()) {
				Creature last = choices.get(choices.size()-1);
				choices.remove(last);
			}
			return choices;
		}
		else if (choices.size() == this.get_season_limit()) {
			return choices; 
		}
		else {
			int count = 0;
			int wrap = this.get_season_limit()/choices.size();
			while (choices.size() < this.get_season_limit()) {
				if (count > wrap) {
					count = 0; 
				}
				Creature first = choices.get(count);
				choices.add(first);
				count++;
				
			}
			return choices;
		}
		
	}
	
	protected ArrayList<Creature> rank (ArrayList<Creature> choices) {
		
		ArrayList<Creature> rank = new ArrayList<Creature>(); 
		
		Creature max = null;
		double highest = 0;
		for (Creature d: choices) {
			
			double rating = d.get_rating();
			if (rating > highest) {
				highest = rating;
				max = d;
				rank.add(0, max);
			}
			else {
				for (int i = 0; i < rank.size(); i++) {
					
					if (rank.get(i).get_rating() < rating) {
						rank.add(i, d);
						break; 
					}
					
				}
				if (!rank.contains(d)) {
					rank.add(d);
				}
			}
		}
		
		return rank;
		
	}
	
	protected ArrayList<Creature> find_potential() {
		
		ArrayList<Creature> potential = new ArrayList<Creature>(); 
		
		for (Creature d: current_region.get_kin_population(this)) {
			if (this.isAdult()) {
				if (!this.isRelated(d) & !this.get_immediate_family(current_region.get_kin_population(this)).contains(d) & this.get_gender() != d.get_gender()) {
					potential.add(d);
				}
			}
		}
		
		return potential; 
		
	}
	
	protected double get_rating() {
		double rating = 0;
		rating += experience + attack + defense + speed + immune_sys*20 + aggression*10; 
		rating += compare_immunities(this);
		rating += skin.get_color_distance(current_region.get_env_color());
		rating += skin.get_pigment_distance(current_region.get_env_pigment());
		rating += skin.get_shade_distance(current_region.get_env_shade());
		if(infections.isEmpty()) {
			rating+=1;
		}
		else {
			rating = rating-1; 
		}
		return rating;
	}
	
	protected int determine_experience(Creature defeated) {
		
		int exp_gained = 0; 
		
		switch(defeated.get_stage()) {
		
		case EGG:
			switch(this.get_stage()) {
			case EGG:
				break;
			case HATCHLING:
				exp_gained++;
				break;
			case ADOLESCENT:
				exp_gained++;
				break;
			case ADULT:
				break;
			case ELDER:
				break;
			default:
				break;
			}
			break;
		case HATCHLING:
			switch(this.get_stage()) {
			case EGG:
				break;
			case HATCHLING:
				exp_gained+=2;
				break;
			case ADOLESCENT:
				exp_gained++;
				break;
			case ADULT:
				exp_gained++;
				break;
			case ELDER:
				exp_gained++;
				break;
			default:
				break;
			}
			break;
		case ADOLESCENT:
			switch(this.get_stage()) {
			case EGG:
				break;
			case HATCHLING:
				exp_gained+=3;
				break;
			case ADOLESCENT:
				exp_gained+=2;
				break;
			case ADULT:
				exp_gained++;
				break;
			case ELDER:
				exp_gained++;
				break;
			default:
				break;
			}
			break;
		case ADULT:
			switch(this.get_stage()) {
			
			case EGG:
				break;
			case HATCHLING:
				exp_gained+=4;
				break;
			case ADOLESCENT:
				exp_gained+=3;
				break;
			case ADULT:
				exp_gained+=2;
				break;
			case ELDER:
				exp_gained++;
				break;
			default:
				break;
			
			}
			break;
		case ELDER:
			switch(this.get_stage()) {
			
			case EGG:
				break;
			case HATCHLING:
				exp_gained+=5;
				break;
			case ADOLESCENT:
				exp_gained+=4;
				break;
			case ADULT:
				exp_gained+=3;
				break;
			case ELDER:
				exp_gained+=2;
				break;
			default:
				break;
			
			}
			break;
		default:
			break;
			
		
		}
		
		return exp_gained; 
	} 
	
	protected void set_current_region(Region new_region) {
		current_region.get_kin_population(this).remove(this);
		this.current_region = new_region; 
		current_region.get_kin_population(this).add(this);
		System.out.println(name + " has migrated to " + current_region.get_name());
	}
	
	protected Region get_current_region() {
		return current_region; 
	}
	
	protected Gender get_gender() {
		return gender; 
	}
	
	protected int get_id() {
		return id; 
	}

	protected String get_name() {
		return name; 
	}
	
	protected int get_experience() {
		return experience; 
	}
	
	protected double get_current_hp() {
		return current_hp; 
	}
	
	protected double get_max_hp() {
		return max_hp; 
	}
	
	protected double get_aggression() {
		return aggression;
	}
	
	protected Skin get_skin() {
		return skin; 
	}
	
	protected Stage get_stage() {

		if(growth < 1.0) {
			return Stage.EGG;
		}
		else if (growth < 5.0) {
			return Stage.HATCHLING;
		}
		else if (growth < 15.0) {
			return Stage.ADOLESCENT;
		}
		else if (growth < 50.0) {
			return Stage.ADULT;
		}
		else {
			return Stage.ELDER;
		}
		
	}
	
	protected String get_growth() {
		return round_decimal(growth); 
	}
	
	protected double get_growth_num() {
		return growth;
	}
	
	protected int get_life_span() {
		return life_span;
	}
	
	protected int get_num_of_offspring () {
		return offspring.size();
	}
	
	protected abstract int get_season_limit(); 
	
	protected int get_curr_season_count() {
		return curr_season_count;
	}
	
	protected boolean reachedSeasonLimit() {

		if(this.curr_season_count >= this.get_season_limit() & gender == Gender.FEMALE) {
			return true;
		}
		else {
			return false; 
		}
	}
	
	protected void reset_season_count() {
		curr_season_count = 0; 
	}
	
	protected boolean isVisiblyIll() {
		return isVisiblyIll;
	}
	
	protected void setVisiblyIll(boolean visible) {
		this.isVisiblyIll = visible;
	}

	protected ArrayList<Virus> get_infections() {
		return infections;
	}
	
	protected ArrayList<Virus> get_immunities() {
		return immunities;
	}
	
	protected double get_immune_sys() {
		return immune_sys;
	}
	
	protected double get_resist_coeff() {
		return immune_sys - (max_hp-current_hp)/1000 - (100 - hunger)/1000 - (100 - thirst)/1000 - (100 - stamina)/1000;
	}
	
	protected double get_stat_coeff() {
		
		double total = 0;
		total += attack_growth + defense_growth + speed_growth;
		return total;
		
	}
	
	protected Creature[] get_parents() {
		
		Creature[] parents = {parent_one, parent_two};
		return parents; 
		
	}
	
	protected Creature get_parent_one() {
		return parent_one;
	}
	
	protected Creature get_parent_two() {
		return parent_two; 
	}
	
	protected ArrayList<Creature> get_offspring() {
		return offspring;
	}
	
	protected ArrayList<Creature> get_ancestors() {
		return ancestors;
	}
	
	protected ArrayList<Creature> get_descendants() {
		return descendants; 
	}
	
	protected int compare_immunities(Creature two) {
		
		int variants = 0;
		
		for (Virus e: immunities) {
			if (!two.immunities.contains(e)) {
				variants++;
			}
		}
		for (Virus f: two.immunities) {
			if (!this.immunities.contains(f)) {
				variants++;
			}
		}
		
		return variants; 
		
	}
	
	//CLASS FUNCTIONS//
	
	public static HashMap<String, Integer> get_census(ArrayList<Creature> pop) {
		
		HashMap<String, Integer> census = new HashMap<String, Integer>();
		
		for (application.Creature.Stage s: Creature.Stage.values()) {
			census.put(s.toString(), 0);
		}
		
		for (Creature d: pop) {
			int current = census.get(d.get_stage().toString());
			census.put(d.get_stage().toString(), current + 1);
		}
		
		return census;
		
	}
	
	public static int season(ArrayList<Creature> pop) {
		String kind = "Creature";
		if(pop.size() != 0) {
			kind = pop.get(0).my_class.toString();
			//System.out.println("Beginning season for " + kind);
		}
		
		HashMap<Creature, Integer> wins = new HashMap<Creature, Integer>(); 
		HashMap<Creature, Integer> losses = new HashMap<Creature, Integer>(); 
		ArrayList<Creature> dead = new ArrayList<Creature>();
		HashMap<Creature, Creature> match_ups = new HashMap<Creature, Creature>(); 
		ArrayList<Creature> babies = new ArrayList<Creature>();

		ArrayList<Creature> younglings = new ArrayList<Creature>();
		ArrayList<Creature> challengers = new ArrayList<Creature>();
		for (Creature d: pop) {
			d.hasGrown = false;
			if (!d.isDead()) {
				d.reset_season_count();
				if (d.isAdult() & d.isHealthy() & !d.isVisiblyIll()) {
					challengers.add(d);
					wins.put(d, 0);
					losses.put(d, 0);
				}
				else if (d.isMinor()) {
					younglings.add(d);
					for (Creature p: d.get_parents()) {
						if (p != null) {
							if (!p.isDead()) {
								p.infect(d);
								d.infect(p);
							}
						}
					}
				}
				else {
					//System.out.println(d.get_name() + "was not among the contenders because they were unwell.");
				}
			}
		}
		
		for(Creature d: younglings) {
			if (!d.isVisiblyIll()) {
				for(Creature m: younglings) {
					if (d != m & !m.isVisiblyIll()) {
						if(d.stage == Stage.HATCHLING & m.stage == Stage.HATCHLING) {
							d.play(m);
							d.infect(m);
						}
						else if (d.stage == Stage.ADOLESCENT & m.stage == Stage.ADOLESCENT) {
							d.play_fight(m);
						}
					}
				}
			}
		}
		//System.out.println(challengers.size() + " participants for " + kind + " season.");
		for (Creature d: challengers) {
			//System.out.println(d.get_name() + " sizes up the field.");
			ArrayList<Creature> matches = new ArrayList<Creature>(); 
			if (d.isDead()) {
				//System.out.println(d.get_name() + " is too dead to care.");
				dead.add(d);
				continue;
			}
			else if (!d.isAdult()) {
				//System.out.println(d.get_name() + " is too young to care.");
				continue;
			}
			else if ((!d.isHealthy() & d.aggression < 0.5) | d.isVisiblyIll()) {
				//System.out.println(d.get_name() + " is too unwell to care.");
				continue;
			}
			else if (d.reachedSeasonLimit()) {
				//System.out.println(d.get_name() + " has already done what they came here to do.");
				continue; 
			}
			else {
				
				for (Creature m: challengers) {
					
					if(match_ups.get(m) == d | match_ups.get(d) == m) {
						//System.out.println(d.get_name() + " and " + m.get_name() + " have already settled their differences.");
						continue; 
					}
					
					boolean will = false;
					
					if (!m.isRelated(d) | 
						(!m.get_immediate_family(population).contains(d) & (d.get_num_of_offspring() < 2) & (m.get_num_of_offspring() < 2))) {
						will = true;
					}
										
					if (!m.isAdult()) {
						//System.out.println(m.get_name() + " is too young to fight.");
						continue;
					}
					else if (m == d) {
						//System.out.println(d.get_name() + " was looking at their reflection. How embarressing!");
						continue;
					}
					else if (m.isDead()) {
						dead.add(m);
						//System.out.println(m.get_name() + " is too dead to fight.");
						continue; 
					}
					if (d.isDead()) {
						dead.add(d);
						//System.out.println(d.get_name() + " is too dead to continue.");
						break;
					}
					else if (m.isVisiblyIll()) {
						//System.out.println(m.get_name() + " was avoided because they looked ill.");
						break;
					}
					else if (!m.isHealthy() & m.get_aggression() < 0.5) {
						//System.out.println(m.get_name() + " was unwell but approached boldly anyway.");
						continue; 
					}
					else if (!d.isHealthy() & d.get_aggression() < 0.5) {
						//System.out.println(d.get_name() + " was unwell but approached boldly anyway.");
						break;
					}
					else if (d.reachedSeasonLimit() | m.reachedSeasonLimit()) {
						//System.out.println("One of them had reached their goal, so nothing happened.");
						break; 
					}
					else if (
							d.get_gender() == m.get_gender()
							& 
							(!d.get_immediate_family(population).contains(m) & !m.get_immediate_family(population).contains(d))
							&
							!(d.find_potential().isEmpty() | m.find_potential().isEmpty()))
							{
						
						boolean disjoint = true;
						for (Creature g: d.find_potential()) {
							for(Creature b: m.find_potential()) {
								if (g == b) {
									disjoint = false; 
								}
							}
						}
						
						if(disjoint) {
							System.out.println(m.get_name() + " & " + d.get_name() + " had nothing to fight over.");
							continue; 
						}
						
						//System.out.println(m.get_name() + " & " + d.get_name() + " squared up.");
						
						//they'll fight
						d.infect(m);
						Creature winner = d.fight(m);
						match_ups.put(d, m);
						match_ups.put(m, d);
						if (winner == null) {
							dead.add(d);
							dead.add(m);
						}
						else if (d == winner) {
							wins.put(d, wins.get(d)+1);
							losses.put(m, losses.get(m)+1);
						}
						else {
							wins.put(m, wins.get(m)+1);
							losses.put(d, losses.get(d)+1);
							break;
						}
						continue;
					}
					else if ((d.get_gender() != m.get_gender()) & (will)) {
						
						if (wins.get(d) > losses.get(d) | match_ups.isEmpty()) {
							System.out.println(d.get_name() + " wins the heart of " + m.get_name() + " with a score of " + wins.get(d) + "w/l" + losses.get(d));
							matches.add(m);
						}
					}
					else if ((d.isRelated(m))) {
						//System.out.println(d.get_name() + " and " + m.get_name() + " are family.");
						d.infect(m);
					}
				}
				for (Creature match: d.rank_choices(matches)) {
					if (!d.isDead() & !match.isDead()) {
						//System.out.println(d.get_name() + " and " + match.get_name() + " attempted to lay eggs.");
						d.infect(match);
						Creature baby = null;
						try {
							baby = (Creature) d.my_class.cast(d).getClass().getMethod("reproduce", d.my_class).invoke(d, match);
						} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException
								| NoSuchMethodException | SecurityException e) {
							e.printStackTrace();
							System.out.println("Issue with calling .reproduce()");
						}
						if (baby != null) {
							babies.add(baby);
						}
						else {
							//System.out.println(d.get_name() + " and " + match.get_name() + " couldn't lay any eggs.");
						}
					}
					else {
						//System.out.println(d.get_name() + " and/or " + match.get_name() + " were dead.");
					}
				}
			}
		}
		int num_babies = babies.size();
		System.out.println(num_babies + " eggs were laid.");
		babies.clear();
		
		//System.out.println("Season ends.");
		
		return num_babies;
		
	}
	
	protected static int get_creature_pop_size() {
		return population.size();
	}
	
	protected static int get_avg_season_limit() {
		double avg = 0;
		for (Creature d: population) {
			avg += d.season_limit;
		}
		if (population == null) {
			return 0;
		}
		else if(population.size() == 0) {
			return 0;
		}
		else {
			return (int) avg/population.size();
		}
	}
	
	protected static double get_avg_creature_immunity() {
		
		double avg = 0;
		for (Creature d: population) {
			avg += d.immune_sys;
		}
		if (population == null) {
			return 0;
		}
		else if(population.size() == 0) {
			return 0;
		}
		else {
			return avg/population.size();
		}
		
	}
	
	protected static int get_avg_season_limit(ArrayList<Creature> pop) {
		double avg = 0;
		int fem_count = 0; 
		for (Creature d: pop) {
			if(d.gender == Gender.FEMALE) {
				fem_count++;
				avg += d.season_limit;
			}
		}
		if (pop == null | fem_count == 0) {
			return 0;
		}
		else if(pop.size() == 0) {
			return 0;
		}
		else {
			return (int) avg/fem_count;
		}
	}
	
	protected static double get_avg_creature_stat_coeff(ArrayList<Creature> creatures) {
		
		double avg = 0; 

		for (Creature d: creatures) {
			avg += d.get_stat_coeff();
		}
		if (creatures.size() != 0) {
			avg = avg/creatures.size();
		}
		return avg;
		
	}
	
	public static double get_avg_immunity(ArrayList<Creature> regional_population) {
		
		double avg = 0;
		for (Creature d: regional_population) {
			avg += d.immune_sys;
		}
		if (regional_population == null) {
			return 0;
		}
		else if(regional_population.size() == 0) {
			return 0;
		}
		else {
			return avg/regional_population.size();
		}
		
	}
	
	public static double get_avg_stat_coeff(ArrayList<Creature> regional_population) {
		
		double avg = 0; 

		for (Creature d: regional_population) {
			avg += d.get_stat_coeff();
		}
		if (regional_population.size() != 0) {
			avg = avg/regional_population.size();
		}
		return avg;
		
	}
	
	public static double get_avg_aggression (ArrayList<Creature> regional_population) {
		double avg = 0;
		for (Creature d: regional_population) {
			avg += d.get_aggression();
		}
		return avg/regional_population.size();
	}
	
	public static double get_avg_imm_sys(ArrayList<Creature> regional_population) {
		
		double avg = 0;
		for (Creature d: regional_population) {
			avg += d.get_immune_sys();
		}
		return avg/regional_population.size();
		
	}
	
	public static int get_avg_life_span(ArrayList<Creature> regional_population) {
		
		int avg = 0;
		for (Creature d: regional_population) {
			avg += d.life_span;
		}
		if (regional_population == null) {
			return 0;
		}
		else if(regional_population.size() == 0) {
			return 0;
		}
		else {
			return avg/regional_population.size();
		}
		
	}
	
	protected static double get_avg_creature_aggression (ArrayList<Creature> creatures) {
		double avg = 0;
		for (Creature d: creatures) {
			avg += d.get_aggression();
		}
		return avg/creatures.size();
	}
	
	protected static double get_avg_creature_imm_sys(ArrayList<Creature> creatures) {
		
		double avg = 0;
		for (Creature d: creatures) {
			avg += d.get_immune_sys();
		}
		return avg/creatures.size();
		
	}
	
	protected static int get_avg_creature_life_span() {
		
		int avg = 0;
		for (Creature d: population) {
			avg += d.life_span;
		}
		if (population == null) {
			return 0;
		}
		else if(population.size() == 0) {
			return 0;
		}
		else {
			return avg/population.size();
		}
		
	}
	
	public static Gender random_gender() {
		
		Random rand = new Random(); 
		int a = rand.nextInt(10) + 1;
		if (a <= 5) {
			return Gender.MALE;
		}
		else {
			return Gender.FEMALE;
		}
		
	}
	
	public static String round_decimal(Double dec) {
		
		String num = "";
		String dec_str = dec.toString();
		//System.out.println(dec_str);
		int index_of_dot = dec_str.indexOf(".");
		//System.out.println(index_of_dot);
		if (index_of_dot == -1) {
			num = dec_str.substring(0, 2);
		}
		else if (dec_str.split("").length < 4) {
			num = dec_str.substring(0, 3);
		}
		else if (dec < 1.0 & dec > -1.0) {
			num = dec_str.substring(1, index_of_dot+3);
		}
		else if (dec == 0) {
			return "0";
		}
		else {
			num = dec_str.substring(0, index_of_dot + 2);
		}
		
		if (num == null) {
			System.out.println("Null while rounding");
			return "0";
		}
		else {
			return num;
		}
		
	}
	
}
