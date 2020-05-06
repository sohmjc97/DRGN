package application;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

import application.Dragon.STAGE;

public abstract class Creature {
	
	/*
	 * Will include Prey and Apex 
	 */
	
	protected static ArrayList<Creature> population = new ArrayList<Creature>();
	protected static int male_pop = 0;
	protected static int female_pop = 0; 
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
	
	protected boolean isDead = false;
	
	protected double growth = 0; 
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
	protected double growth_rate; 
	
	protected abstract void migrate();
	protected abstract void scavenge();
	protected abstract boolean check_infections();
	protected abstract boolean infect(Creature two); 
	protected abstract void run();
	protected abstract String get_stats();
	
	protected void rest() {
		
		//System.out.println(name + " sleeps.");
		heal();
		grow();
		stamina = 100;
		hunger = hunger - 20;
		thirst = thirst - 20; 
		check_status();
		
	}
	
	protected void drink() {
		
		//System.out.println(name + " takes a drink.");
		//heal();
		
		Random rand = new Random();
		if(rand.nextInt(100) < 5) {
			//Virus.manual_infect(this);
		}
		
		grow();
		thirst = 100;
		stamina = stamina - 10; 
		hunger = hunger - 10;
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
		
		feed += " / " + winner.name + " triumphed over " + loser.get_name();
		
		winner.experience += 5; 
		winner.current_hp = winner.current_hp - 10;
		
		winner.stamina = winner.stamina - 20;
		winner.hunger = winner.hunger - 10; 
		winner.thirst = winner.thirst - 10;
		winner.check_status();
		
		loser.experience += 2; 
		loser.current_hp = loser.current_hp - 30;
		loser.stamina = loser.stamina - 30;
		loser.hunger = loser.hunger - 20; 
		loser.thirst = loser.thirst - 20; 
		loser.check_status();
		
		//get infected from injuries sustained during battle
		
		if(!winner.isDead & winner.current_hp < winner.max_hp/2) {
			Random rand = new Random();
			if(rand.nextInt(100) < 5) {
				//Virus.manual_infect(winner);
			}
		}
		
		if(!loser.isDead & loser.current_hp < loser.max_hp/2) {
			Random rand = new Random();
			if(rand.nextInt(100) < 5) {
				//Virus.manual_infect(loser);
			}
		}
		
		if(winner.isDead & !loser.isDead) {
			feed += " / " + winner.name + " but later died of their injuries.";
			System.out.println("Although " + winner.name + " won the fight, they later died of their injuries, leaving " + loser.get_name() + " as the winner.");
			winner = loser;
		}
		else if (loser.isDead & !winner.isDead) {
			feed += " / " + winner.name + " killed " + loser.name;
			//System.out.println(winner.name + " killed " + loser.get_name() + " in battle.");
		}
		else if (winner.isDead & loser.isDead) {
			feed+= " / Killed each other in battle.";
			//System.out.println("The dragons killed each other in battle.");
			return null;
		}
		System.out.println(feed);
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
						//e.remove_infected(this, false);
					}
					die(); 
					return;
				}
			}
		}
		else {
			//die from starvation, thirst, exhaustion, or injury
			for (Virus e: infections) {
				//e.remove_infected(this, true);
			}
			die();
			return;
		}
		
		if (check_infections()) {
			for (Virus e: infections) {
				//e.remove_infected(this, true);
			}
			this.infections.clear();
			die();
		}
		
	}
	
	public void die() {
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
			//e.remove_infected(this, false);
		}
		isDead = true;
		current_hp = 0; 
		population.remove(this);
		if (gender == Gender.FEMALE) {
			female_pop = female_pop - 1;
		}
		else {
			male_pop = male_pop - 1;
		}
	}
	
	protected boolean isHealthy() {
		
		if(isVisiblyIll) {
			return false;
		}
		else if (current_hp >= 30 & stamina >= 40 & hunger >= 40 & thirst >= 40) {
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
		else if (choices.size() > season_limit) {
			while (choices.size() > season_limit) {
				Creature last = choices.get(choices.size()-1);
				choices.remove(last);
			}
			return choices;
		}
		else if (choices.size() == season_limit) {
			return choices; 
		}
		else {
			int count = 0;
			int wrap = season_limit/choices.size();
			while (choices.size() < season_limit) {
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
		
		for (Creature d: population) {
			if (this.isAdult()) {
				if (!this.isRelated(d) & !this.get_immediate_family(population).contains(d) & this.get_gender() != d.get_gender()) {
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
	
	protected void set_current_region(Region new_region) {
		this.current_region = new_region; 
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
		return stage; 
	}
	
	protected String get_growth() {
		return round_decimal(growth); 
	}
	
	protected int get_life_span() {
		return life_span;
	}
	
	protected int get_num_of_offspring () {
		return offspring.size();
	}
	
	protected int get_season_limit() {
		return season_limit;
	}
	
	protected int get_curr_season_count() {
		return curr_season_count;
	}
	
	protected boolean reachedSeasonLimit() {
		if(this.curr_season_count >= season_limit) {
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
	
	protected static void season() {
		
		HashMap<Creature, Integer> wins = new HashMap<Creature, Integer>(); 
		HashMap<Creature, Integer> losses = new HashMap<Creature, Integer>(); 
		ArrayList<Creature> dead = new ArrayList<Creature>();
		HashMap<Creature, Creature> match_ups = new HashMap<Creature, Creature>(); 
		ArrayList<Creature> babies = new ArrayList<Creature>();

		Random rand = new Random();
		ArrayList<Creature> younglings = new ArrayList<Creature>();
		ArrayList<Creature> challengers = new ArrayList<Creature>();
		for (Creature d: population) {
			if (!d.isDead()) {
				d.reset_season_count();
				//int seed = rand.nextInt(100);
				if (d.isAdult() & d.isHealthy() & !d.isDead() & !d.isVisiblyIll() & !d.reachedSeasonLimit()) {
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
			}
		}
		
		for(Creature d: younglings) {
			if (!d.isVisiblyIll()) {
				for(Creature m: younglings) {
					if (d != m & !m.isVisiblyIll()) {
						d.infect(m);
					}
				}
			}
		}
		
		for (Creature d: challengers) {
			//System.out.println(d.get_name() + " sizes up the field.");
			ArrayList<Creature> matches = new ArrayList<Creature>(); 
			int limit = 0;
			if (d.isDead()) {
				dead.add(d);
				continue;
			}
			else if (!d.isAdult()) {
				continue;
			}
			else if (!d.isHealthy() | d.isVisiblyIll()) {
				continue;
			}
			else if (d.reachedSeasonLimit()) {
				continue; 
			}
			else {
				
				for (Creature m: challengers) {
					
					if(match_ups.get(m) == d | match_ups.get(d) == m) {
						continue; 
					}
					
					boolean will = false;
					if (!m.isRelated(d) | 
						(!m.get_immediate_family(population).contains(d) & (d.get_num_of_offspring() < 2) & (m.get_num_of_offspring() < 2))) {
						will = true;
					}
										
					if (!m.isAdult()) {
						continue;
					}
					else if (m == d) {
						continue;
					}
					else if (m.isDead()) {
						dead.add(m);
						continue; 
					}
					if (d.isDead()) {
						dead.add(d);
						break;
					}
					else if (m.isVisiblyIll()) {
						break;
					}
					else if (!m.isHealthy() & m.get_aggression() < 0.5) {
						continue; 
					}
					else if (!d.isHealthy() & d.get_aggression() < 0.5) {
						break;
					}
					else if (d.reachedSeasonLimit() | m.reachedSeasonLimit()) {
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
						d.infect(m);
					}
				}
				for (Creature match: d.rank_choices(matches)) {
					if (!d.isDead() & !match.isDead()) {
						d.infect(match);
						Creature baby = d.reproduce(match);
						if (baby != null) {
							babies.add(baby);
							//order_view.getItems().add(baby.get_name());
						}
					}
				}
			}
		}

		for (Creature d: dead) {
			population.remove(d);		
		}
		//int num_babies = babies.size();
		for (Creature b: babies) {
			population.add(b);
		}
		//System.out.println(num_babies + " eggs were laid.");
		babies.clear();
		
	}
	
	protected static void simulate() {
		for(Creature d: population) {
			d.run();
		}
		season(); 
	}
	
	protected static int get_pop_size() {
		return population.size();
	}
	
	protected static double get_avg_immunity() {
		
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
	
	protected static double get_avg_stat_coeff(ArrayList<Creature> creatures) {
		
		double avg = 0; 

		for (Creature d: creatures) {
			avg += d.get_stat_coeff();
		}
		if (creatures.size() != 0) {
			avg = avg/creatures.size();
		}
		return avg;
		
	}
	
	protected static double get_avg_aggression (ArrayList<Creature> creatures) {
		double avg = 0;
		for (Creature d: creatures) {
			avg += d.get_aggression();
		}
		return avg/creatures.size();
	}
	
	protected static double get_avg_imm_sys(ArrayList<Creature> creatures) {
		
		double avg = 0;
		for (Creature d: creatures) {
			avg += d.get_immune_sys();
		}
		return avg/creatures.size();
		
	}
	
	protected static int get_avg_life_span() {
		
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
