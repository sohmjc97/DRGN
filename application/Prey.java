package application;

import java.util.ArrayList;
import java.util.Random;

import application.Creature.Gender;
import application.Creature.Stage;
import application.Region.Resources;

public class Prey extends Creature {
	
	private static int prey_count = 0;
	private static ArrayList<Prey> prey_population = new ArrayList<Prey>();
	private static int male_pop = 0;
	private static int female_pop = 0;
	
	private int season_limit = 36; 
	
	public Prey(Double stat_avg, Skin new_skin, int avg_life, Region current_region) {
		Random rand = new Random();
		prey_count = prey_count + 1;
		id = prey_count;
		name = "Prey#" + id; 
		my_class = Prey.class;
		this.current_region = current_region; 
		this.region_of_origin = current_region;
		int gender_code = current_region.gender_check(current_region.get_prey_population());
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
			if(Creature.get_avg_season_limit(current_region.get_prey_population()) != 0) {
				int avg_limit = Creature.get_avg_season_limit(current_region.get_prey_population());
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
				season_limit = 18;
			}
		}
		else {
			season_limit = 180;
			male_pop++;
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
			if (!current_region.get_prey_population().isEmpty()) {
				if (get_avg_imm_sys() > this.get_immune_sys()) {
					immune_sys = immune_sys + get_avg_imm_sys(current_region.get_prey_population())/10;
				}
				else if (get_avg_imm_sys(current_region.get_prey_population()) == this.get_immune_sys()) {
					//do nothing
				}
				else {
					immune_sys = immune_sys - get_avg_imm_sys(current_region.get_prey_population())/10;
				}
			}
		}
		aggression = rand.nextDouble();
		int flip2 = rand.nextInt(2);
		if (flip2 == 1) {
			if (!current_region.get_prey_population().isEmpty()) {
				if (get_avg_aggression() > this.get_aggression()) {
					aggression = aggression + get_avg_aggression(current_region.get_prey_population())/10;
				}
				else if ( get_avg_aggression(current_region.get_prey_population()) == this.get_aggression()) {
					//do nothing
				}
				else {
					aggression = aggression - get_avg_aggression(current_region.get_prey_population())/10;
				}
			}
		}
		parent_one = null; 
		parent_two = null; 
		population.add(this);
		prey_population.add(this);
		current_region.get_prey_population().add(this);
	}
	
	public Prey(Prey one, Prey two, int code) {
		Random rand = new Random();
		prey_count = prey_count + 1;
		id = prey_count;
		name = "Prey#" + id; 
		my_class = Prey.class;
		this.current_region = one.current_region; 
		this.region_of_origin = current_region;
		parent_one = one; 
		parent_two = two;
		int gender_code = current_region.gender_check(current_region.get_prey_population());
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
				//int diff = Math.abs(parent_one.season_limit - Creature.get_avg_season_limit(current_region.get_prey_population()));
				int flip = rand.nextInt(3);
				if(flip == 1) {
					season_limit = parent_one.get_season_limit() - 4; 
				}
				else if (flip == 2) {
					season_limit = parent_one.get_season_limit();
				}
				else {
					season_limit = parent_one.get_season_limit() + 4;
				}
			}
			else {
				//int diff = Math.abs(parent_two.season_limit - Creature.get_avg_season_limit(current_region.get_prey_population()));
				int flip = rand.nextInt(3);
				if(flip == 1) {
					season_limit = parent_two.get_season_limit() - 4; 
				}
				else if (flip == 2) {
					season_limit = parent_two.get_season_limit();
				}
				else {
					season_limit = parent_two.get_season_limit() + 4;
				}
			}
		}
		else {
			male_pop++;
			season_limit = 180;
		}
		
		if(season_limit <= 0) {
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
		prey_population.add(this);
		current_region.get_prey_population().add(this);
	}

	@Override
	protected void hunt() {
		
		//System.out.println(name + " is grazing w/ Hunger " + hunger);
		
		int food_level = current_region.get_resource_levels(Resources.PREY_FOOD);
		int share = food_level/current_region.get_prey_population().size();
		
		int taken = 0;
		
		int orig_hunger = (int) hunger;
		
		if(share <= 10) {
			famine = true; 
			if(food_level <= 0) {
				hunger = hunger - 5; 
			}
			else if (food_level - 100 < 0) {
				hunger += food_level;
				taken = food_level;
				if(hunger > 100) {
					hunger = 100;
					taken = 100 - orig_hunger;
				}
			}
			else {
				hunger = 100; 
				taken = 100 - orig_hunger;
			}
		}
		else {
			hunger+=share;
			if(hunger > 100) {
				taken = 100 - orig_hunger;
				hunger = 100;
			}
			else {
				taken = share;
			}
			famine = false;
			
			if(stage == Stage.HATCHLING) {
				taken = taken/4;
			}
			else if (stage == Stage.ADOLESCENT) {
				taken = taken /2;
			}
			
			if(taken == 0) {
				taken = 1; 
			}
			
		}
		
		taken = taken/2;
		
		//System.out.println(name + " used up " + taken  + " resources and are now at Hunger " + hunger);
		current_region.deplete_resource(Resources.PREY_FOOD, taken);
		
		stamina = stamina - 10;
		thirst = thirst - 10; 
		grow(); 
		check_status();
		
	}

	protected int be_hunted(Dragon hunter) {
		
		int chance = 0; 
		
		if (skin.get_color() == current_region.get_env_color()) {
			chance += 10;
		}
		else {
			int dist = skin.get_color_distance(current_region.get_env_color());
			if (dist == 1) {
				chance += 2;
			}
			else if(dist == 2) {
				//chance = chance - 2; 
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
		
		if (this.isMinor()) {
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
				chance = (int) (chance + experience + (get_stat_coeff())*10);
			}
		}
		
		chance += speed - hunter.speed;
		chance += defense - hunter.attack;
		
		chance += experience/2 + speed + defense + attack; 
		chance = (int) (chance - (max_hp-current_hp)/10 - (100-stamina)/10 - (100-hunger)/10 - (100 - thirst)/10 + growth*.75);
		
		for (Creature e: this.ancestors) {
			if(!e.isDead & e.get_stage() == Stage.ELDER) {
				chance+=2; 
			}
		}
		
		for(Virus v: infections) {
			chance = chance - 5; 
		}
		
		stamina = stamina - 20;
		thirst = thirst - 20; 
		hunger = hunger - 10; 
		experience += determine_experience(hunter);
		grow();
		check_status();
		return chance;
		
	}

	public Prey reproduce(Prey two) {
		try {
			if (this.reachedSeasonLimit() | two.reachedSeasonLimit()) {
				//System.out.println("One or more had reached their season limit.");
				return null;
			}
			else if (gender != two.gender & get_immediate_family(current_region.get_prey_population()).contains(two) == false & stage == Stage.ADULT & two.stage == Stage.ADULT) {
				System.out.println(name + " laid an egg with " + two.name);
				Prey baby = null;
				if (male_pop > female_pop) {
					baby = new Prey(this, two, 0);
				} 
				else if (female_pop > male_pop) {
					baby = new Prey(this, two, 1);
				}
				else {
					baby = new Prey(this, two, 2);
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
	protected void die() {
		
		//System.out.println(name + " has died.");
		if(!isDead) {
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
			prey_population.remove(this);
			current_region.get_prey_population().remove(this);
			current_region.replenish_resource(Resources.PREY_FOOD, (int)(this.growth));
		}
	}

	@Override
	protected void scavenge() {
		//hunger += 1;
		hunt();
	}
	
	@Override
	protected boolean isSatisfied() {
		if(hunger >= 100 | isDead) {
			return true;
		}
		else {
			return false;
		}
	}
	
	@Override
	protected Stage get_stage() {

		if(growth < 1.0) {
			return Stage.EGG;
		}
		else if (growth < 5.0) {
			return Stage.HATCHLING;
		}
		else if (growth < 10.0) {
			return Stage.ADOLESCENT;
		}
		else if (growth < 50.0) {
			return Stage.ADULT;
		}
		else {
			return Stage.ELDER;
		}
		
	}
	
	@Override
	public int get_season_limit() {
		return this.season_limit;
	}

	//Class Functions 
	
	public static int get_prey_count() {
		return prey_count;
	}
	
	public static int get_pop_size() {
		return prey_population.size();
	}
	
	public static double get_avg_immunity() {
		
		double avg = 0;
		for (Prey d: prey_population) {
			avg += d.immune_sys;
		}
		if (prey_population == null) {
			return 0;
		}
		else if(prey_population.size() == 0) {
			return 0;
		}
		else {
			return avg/prey_population.size();
		}
		
	}
	
	public static double get_avg_stat_coeff() {
		
		double avg = 0; 

		for (Prey d: prey_population) {
			avg += d.get_stat_coeff();
		}
		if (prey_population.size() != 0) {
			avg = avg/prey_population.size();
		}
		return avg;
		
	}
	
	public static double get_avg_aggression () {
		double avg = 0;
		for (Prey d: prey_population) {
			avg += d.get_aggression();
		}
		return avg/prey_population.size();
	}
	
	public static double get_avg_imm_sys() {
		
		double avg = 0;
		for (Prey d: prey_population) {
			avg += d.get_immune_sys();
		}
		return avg/prey_population.size();
		
	}
	
	public static int get_avg_life_span() {
		
		int avg = 0;
		for (Creature d: prey_population) {
			avg += d.life_span;
		}
		if (prey_population == null) {
			return 0;
		}
		else if(prey_population.size() == 0) {
			return 0;
		}
		else {
			return avg/prey_population.size();
		}
		
	}

}
