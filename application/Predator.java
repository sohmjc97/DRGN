package application;

import java.util.ArrayList;
import java.util.Random;

import application.Creature.Gender;
import application.Region.Resources;

public class Predator extends Creature {
	
	private static int predator_count = 0;
	private static ArrayList<Predator> predator_population = new ArrayList<Predator>();
	private static int male_pop = 0;
	private static int female_pop = 0;
	
	private int season_limit = 3;
	
	public Predator(Double stat_avg, Skin new_skin, int avg_life, Region current_region) {
		Random rand = new Random();
		predator_count = predator_count + 1;
		id = predator_count;
		name = "Predator#" + id; 
		this.current_region = current_region; 
		my_class = Predator.class;
		int gender_code = current_region.gender_check(current_region.get_predator_population());
		if (gender_code == 1) {
			gender = Gender.FEMALE;
		} 
		else if (gender_code == 2) {
			gender = Gender.MALE;
		}
		else {
			gender = Creature.random_gender();
		}
		
		if (gender == Gender.FEMALE) {
			female_pop++;
			if(Creature.get_avg_season_limit(current_region.get_predator_population()) != 0) {
				int avg_limit = Creature.get_avg_season_limit(current_region.get_predator_population());
				int coin_flip = rand.nextInt(3);
				if (coin_flip == 0) {
					season_limit = avg_limit - 2;
				}
				else if(coin_flip == 1) {
					season_limit = avg_limit;
				}
				else {
					season_limit = avg_limit + 2;
				}
				
			}
			else {
				season_limit = rand.nextInt(4) + 1;
			}
		}
		else {
			season_limit = 24;
			male_pop++;
		}
		
		if(season_limit <= 0) {
			season_limit = 1;
		}
		
		skin = new_skin;
		if(avg_life == 0) {
			avg_life = 35;
		}
		life_span = avg_life; 
		stage = get_stage();
		
		attack_growth = rand.nextDouble()/3;
		defense_growth = rand.nextDouble()/3;
		speed_growth = rand.nextDouble()/3;
		int chance = rand.nextInt(2);
		if (chance == 1) {
			if (stat_avg != null) {
				if (stat_avg > this.get_stat_coeff()) {
					attack_growth += stat_avg/10;
					defense_growth += stat_avg/10;
					speed_growth += stat_avg/10;
				}
				else if (stat_avg == this.get_stat_coeff()) {
					//do nothing
				}
				else {
					attack_growth = attack_growth - stat_avg/10;
					defense_growth = defense_growth - stat_avg/10;
					speed_growth = speed_growth - stat_avg/10;
				}
			}
		}
		immune_sys = rand.nextDouble();
		int flip = rand.nextInt(2);
		if (flip == 1) {
			if (!current_region.get_predator_population().isEmpty()) {
				if (get_avg_imm_sys() > this.get_immune_sys()) {
					immune_sys = immune_sys + get_avg_imm_sys(current_region.get_predator_population())/10;
				}
				else if (get_avg_imm_sys(current_region.get_predator_population()) == this.get_immune_sys()) {
					//do nothing
				}
				else {
					immune_sys = immune_sys - get_avg_imm_sys(current_region.get_predator_population())/10;
				}
			}
		}
		aggression = rand.nextDouble();
		int flip2 = rand.nextInt(2);
		if (flip2 == 1) {
			if (!current_region.get_predator_population().isEmpty()) {
				if (get_avg_aggression() > this.get_aggression()) {
					aggression = aggression + get_avg_aggression(current_region.get_predator_population())/10;
				}
				else if (get_avg_aggression()== this.get_aggression()) {
					//do nothing
				}
				else {
					aggression = aggression - get_avg_aggression(current_region.get_predator_population())/10;
				}
			}
		}
		parent_one = null; 
		parent_two = null; 
		population.add(this);
		predator_population.add(this);
		current_region.get_predator_population().add(this);
	}

	public Predator(Predator one, Predator two, int code) {
		Random rand = new Random();
		predator_count = predator_count + 1;
		id = predator_count;
		name = "Predator#" + id; 
		my_class = Predator.class;
		this.current_region = one.current_region; 
		parent_one = one; 
		parent_two = two;
		int gender_code = current_region.gender_check(current_region.get_predator_population());
		if (gender_code == 1) {
			gender = Gender.FEMALE;
		} 
		else if (gender_code == 2) {
			gender = Gender.MALE;
		}
		else {
			gender = Creature.random_gender();
		}
		if (gender == Gender.FEMALE) {
			female_pop++;
			if(parent_one.gender == Gender.FEMALE) {
				//int diff = Math.abs(parent_one.season_limit - Creature.get_avg_season_limit(current_region.get_predator_population()));
				int flip = rand.nextInt(3);
				if(flip == 1) {
					season_limit = parent_one.get_season_limit() - 1; 
				}
				else if (flip == 2) {
					season_limit = parent_one.get_season_limit();
				}
				else {
					season_limit = parent_one.get_season_limit() + 1;
				}
			}
			else {
				//int diff = Math.abs(parent_two.season_limit - Creature.get_avg_season_limit(current_region.get_predator_population()));
				int flip = rand.nextInt(3);
				if(flip == 1) {
					season_limit = parent_two.get_season_limit() - 1; 
				}
				else if (flip == 2) {
					season_limit = parent_two.get_season_limit();
				}
				else {
					season_limit = parent_two.get_season_limit() + 1;
				}
			}
		}
		else {
			male_pop++;
			season_limit = 24;
		}
		
		if(season_limit == 0) {
			season_limit = 1; 
		}
		
		skin = new Skin(one.skin, two.skin);
		stage = get_stage();
		
		one.offspring.add(this);
		two.offspring.add(this);
		immunities.addAll(one.get_immunities());
		for (Virus v: two.get_immunities()) {
			if (!immunities.contains(v)) {
				immunities.add(v);
			}
		}
		experience = 0;
		ancestors.add(one);
		for (Creature a: one.ancestors) {
			if (!ancestors.contains(a)) {
				ancestors.add(a);
			}
		}
		ancestors.add(two);
		for (Creature e: two.ancestors) {
			if (!ancestors.contains(e)) {
				ancestors.add(e);
			}
		};
		
		for (Creature d: ancestors) {
			if (!d.descendants.contains(d)) {
				d.descendants.add(this);
			}
		}
		
		//allow for random mutation
		
		int atkcoeff = rand.nextInt(3) ;
		if (atkcoeff < 1) {
			attack_growth = (one.attack_growth + two.attack_growth)/2 + .05;
		}
		else if(atkcoeff < 2) {
			attack_growth = (one.attack_growth + two.attack_growth)/2;
		}
		else {
			attack_growth = (one.attack_growth + two.attack_growth)/2 - .05;
		}
		
		int defcoeff = rand.nextInt(3) ;
		if (defcoeff < 1) {
			defense_growth = (one.defense_growth + two.defense_growth)/2 + .05;
		}
		else if(defcoeff < 2) {
			defense_growth = (one.defense_growth + two.defense_growth)/2;
		}
		else {
			defense_growth = (one.defense_growth + two.defense_growth)/2 - .05;
		}
		
		int spdcoeff = rand.nextInt(3) ;
		if (spdcoeff < 1) {
			speed_growth = (one.speed_growth + two.speed_growth)/2 + .05;
		}
		else if(spdcoeff < 2) {
			speed_growth = (one.speed_growth + two.speed_growth)/2;
		}
		else {
			speed_growth = (one.speed_growth + two.speed_growth)/2 - .05;
		}
		
		int agg_coeff = rand.nextInt(3);
		if (agg_coeff < 1) {
			aggression = (one.aggression + two.aggression)/2 + .05;
		}
		else if(agg_coeff < 2) {
			aggression = (one.aggression + two.aggression)/2;
		}
		else {
			aggression = (one.aggression + two.aggression)/2 - .05;
		}
		
		int imm_coeff = rand.nextInt(3);
		if (imm_coeff < 1) {
			immune_sys = (one.immune_sys + two.immune_sys)/2 + .05;
		}
		else if(imm_coeff < 2) {
			immune_sys = (one.immune_sys + two.immune_sys)/2;
		}
		else {
			immune_sys = (one.immune_sys + two.immune_sys)/2 - .05;
		}
		
		int age_coeff = rand.nextInt(3);
		if(age_coeff < 1) {
			life_span = (one.life_span + two.life_span)/2 + 5; 
		}
		else if (age_coeff < 2) {
			life_span = (one.life_span + two.life_span)/2; 
		}
		else {
			life_span = (one.life_span + two.life_span)/2 - 5; 
		}
		
		population.add(this);
		predator_population.add(this);
		current_region.get_predator_population().add(this);
	}
	
	private Dragon choose_prey() {
		
		Dragon chosen = null;
		ArrayList<Creature> easy_prey = new ArrayList<Creature>();
		ArrayList<Creature> hard_prey = new ArrayList<Creature>();
		for(Creature p: current_region.get_dragon_population()) {
			
			if(!p.isDead) {
				if(this.stage == Stage.HATCHLING) {
					if(p.stage == Stage.EGG & !p.isWellProtected()) {
						easy_prey.add(p);
					}
					else {
						hard_prey.add(p);
					}
				}
				else {
					if (p.isVisiblyIll) {
						easy_prey.add(p);
					}
					else if (p.get_stat_coeff() < this.get_stat_coeff()) {
						easy_prey.add(p);
					}
					else if (p.isMinor() & !p.isWellProtected()) {
						easy_prey.add(p);
					}
					else {
						hard_prey.add(p);
					}
				}
			}
		}
		
		Random rand = new Random();
		if(hard_prey.isEmpty() & easy_prey.isEmpty()) {
			return null;
		}
		else if (hard_prey.isEmpty() & !easy_prey.isEmpty()) {
			chosen = (Dragon) easy_prey.get(rand.nextInt(easy_prey.size()));
		}
		else if (!hard_prey.isEmpty() & easy_prey.isEmpty()) {
			chosen = (Dragon) hard_prey.get(rand.nextInt(hard_prey.size()));
		}
		else if(isHealthy() & aggression > 0.5 | easy_prey.isEmpty()) {
			chosen = (Dragon) hard_prey.get(rand.nextInt(hard_prey.size()));
		}
		else {
			chosen = (Dragon) easy_prey.get(rand.nextInt(easy_prey.size()));
		}
		easy_prey.clear();
		hard_prey.clear();
		return chosen;
	}
	
	@Override
	protected void die() {
			if (!isDead) {
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
			
			if(gender == Gender.FEMALE) {
				female_pop = female_pop - 1;
			}
			else if (gender == Gender.MALE) {
				male_pop = male_pop - 1; 
			}
			
			population.remove(this);
			predator_population.remove(this);
			current_region.get_predator_population().remove(this);
			current_region.replenish_resource(Resources.PREY_FOOD, (int)(this.growth));
		}
	}

	@Override
	protected void hunt() {
		// Hunt a Dragon 
		Dragon d = choose_prey();
		
		if(d == null) {
			//System.out.println(name + " could find no dragons to hunt.");
			hunger = hunger - 10; 
			stamina = stamina - 10;
			thirst = thirst - 10; 
			grow(); 
			check_status();
			famine = true; 
			return; 
		}
		else {
			famine = false; 
		}
		
		//System.out.println(name + " begins to hunt " + d.get_name());
		double chance = 0; 
		
		//STEALTH
		if (skin.get_color() == current_region.get_env_color()) {
			chance += 10;
		}
		else {
			int dist = skin.get_color_distance(current_region.get_env_color());
			if (dist == 1) {
				chance += 2;
			}
			else if(dist == 2) {
				chance = chance - 2;
			}
			else {
				chance = chance - 5; 
			}
		}
		if (skin.get_shade() == current_region.get_env_shade()) {
			chance += 10;
		}
		else {
			int dist = skin.get_shade_distance(current_region.get_env_shade());
			if(dist == 2) {
				chance = chance - 5;
			}
		}
		if (skin.get_pigment() == current_region.get_env_pigment()) {
			chance += 10;
		}
		else {
			int dist = skin.get_pigment_distance(current_region.get_env_pigment());
			if(dist == 2) {
				chance = chance - 5;
			}
		}
		
		//FAMILY AID/ TEACHING
		if (this.stage == Stage.HATCHLING | this.stage == Stage.ADOLESCENT) {
			if (parent_one != null & parent_two != null) {
				if (!parent_one.isDead) {
					chance += (parent_one.experience)/parent_one.get_num_minor_offspring(); 
				}
				else {
					chance = chance - 5; 
				}
				if (!parent_two.isDead) {
					chance += (parent_two.experience)/parent_two.get_num_minor_offspring(); 
				}
				else {
					chance = chance - 5; 
				}
			}
			if (get_stat_coeff() < 0.5) {
				chance = chance - 5;
			}
			else {
				chance = chance + experience + (get_stat_coeff())*10;
			}
		}
		
		for (Creature e: this.ancestors) {
			if(!e.isDead & e.get_stage() == Stage.ELDER) {
				chance+=2; 
			}
		}
		
		for(Virus v: infections) {
			chance = chance - 5; 
		}
		
		chance = (int) (chance - (max_hp-current_hp)/10 - (100-stamina)/10 - (100-hunger)/10 - (100 - thirst)/10 + growth*.75);
		
		//Compare Predator's speed and offense to Dragon's Speed and defense
		chance += experience;
		chance += attack - d.defense; 
		chance += speed - d.speed;
		
		if(d.isAdult() & this.isAdult()) {
			if(d.get_aggression() > 0.5 & d.attack > this.defense_growth) {
				System.out.println(d.get_name() + " attempts to defend themselves against (" + get_stage() + ") " + name);
				d.fight(this);
				if(d.isDead & !this.isDead) {
					System.out.println(name + " caught and ate (" + d.get_stage() + ") " + d.get_name());
					hunger = 100; 
					experience++; 
				}
				else if (!d.isDead & this.isDead) {
					System.out.println(d.get_name() + " stood its ground and killed (" + get_stage() + ") " + name);
				}
				else if (!d.isDead & !this.isDead) {
					System.out.println("Neither could kill the other, so the hunt continues.");
				}
				else if (isDead & d.isDead){
					System.out.println("The hunter and the prey killed each other.");
				}
			}
		}
		else if (d.isMinor()) {
			d.be_defended(this);
		}
		
		if(this.isDead | this.isSatisfied()) {
			return; 
		}
		
		if(!d.isDead) {
			if((!this.isHealthy() & this.aggression > 0.5) | this.isHealthy()) {
				int dragon_chance = d.be_hunted(this);
				if (chance >= dragon_chance) {
					eat(d);
					experience += determine_experience(d); 
					d.die();
				}
				else {
					System.out.println(name + " failed to catch (" + d.get_stage() + ") " + d.get_name());
					hunger = hunger - 10; 
					current_hp = current_hp - 5;
				}
			}
			else {
				System.out.println(name + " gave up on hunting (" + d.get_stage() + ") " + d.get_name());
			}
		}
		stamina = stamina - 20;
		thirst = thirst - 20; 
		grow(); 
		check_status();
	}

	public Predator reproduce(Predator two) {
		try {
			if (this.reachedSeasonLimit() | two.reachedSeasonLimit()) {
				//System.out.println("One or more had reached their season limit.");
				return null;
			}
			else if (gender != two.gender & get_immediate_family(current_region.get_predator_population()).contains(two) == false & stage == Stage.ADULT & two.stage == Stage.ADULT) {
				System.out.println(name + " laid an egg with " + two.name);
				Predator baby = null;
				if (male_pop > female_pop) {
					baby = new Predator(this, two, 0);
				} 
				else if (female_pop > male_pop) {
					baby = new Predator(this, two, 1);
				}
				else {
					baby = new Predator(this, two, 2);
				}
				this.curr_season_count++;
				two.curr_season_count++;
				return baby;
			}
			else {
				throw new ActionIllegalException("This pairing is unsuccessful.");
			}
		} catch (ActionIllegalException e) {
			//System.out.println(e.getMessage());
			return null;
		}
	}

	@Override
	protected void scavenge() {

		int chance = (current_region.get_dragon_population().size()/4)/current_region.get_predator_population().size(); 
		
		if (chance <= 0) {
			
			if(current_region.get_resource_levels(Resources.PREY_FOOD) < 10) {
				famine = true;
				//System.out.println(name + " couldn't even find sticks to eat.");
			}
			else {
				hunger += 10;
				//System.out.println(name + " finds nothing to eat, so they chew on some sticks.");
				//current_region.deplete_resource(Resources.PREY_FOOD, 1);
			}
		}
		else {
			hunger += 50; 
			//System.out.println(name + " finds an old carcass to gnaw on.");
			//current_region.deplete_resource(Resources.PREY_FOOD, 5);
		}
		
		
		
		Random rand = new Random();
		if(rand.nextInt(100) < 5) {
			Virus.manual_infect(this);
		}
		
		grow(); 
		thirst = thirst - 10;
		stamina = stamina - 10;
		check_status();
		
	}
	
	@Override
	public int get_season_limit() {

		if (gender == Gender.FEMALE) {
			return season_limit;
		}
		else {
			return 24; 
		}
		
	}
	
	//Class Functions 
	
	public static int get_predator_count() {
		return predator_count;
	}
	
	public static int get_pop_size() {
		return predator_population.size();
	}
	
	public static double get_avg_immunity() {
		
		double avg = 0;
		for (Predator d: predator_population) {
			avg += d.immune_sys;
		}
		if (predator_population == null) {
			return 0;
		}
		else if(predator_population.size() == 0) {
			return 0;
		}
		else {
			return avg/predator_population.size();
		}
		
	}
	
	public static double get_avg_stat_coeff() {
		
		double avg = 0; 

		for (Predator d: predator_population) {
			avg += d.get_stat_coeff();
		}
		if (predator_population.size() != 0) {
			avg = avg/predator_population.size();
		}
		return avg;
		
	}
	
	public static double get_avg_aggression () {
		double avg = 0;
		for (Predator d: predator_population) {
			avg += d.get_aggression();
		}
		return avg/predator_population.size();
	}
	
	public static double get_avg_imm_sys() {
		
		double avg = 0;
		for (Predator d: predator_population) {
			avg += d.get_immune_sys();
		}
		return avg/predator_population.size();
		
	}
	
	public static int get_avg_life_span() {
		
		int avg = 0;
		for (Predator d: predator_population) {
			avg += d.life_span;
		}
		if (predator_population == null) {
			return 0;
		}
		else if(predator_population.size() == 0) {
			return 0;
		}
		else {
			return avg/predator_population.size();
		}
		
	}


}
