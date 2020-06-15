package application;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

import application.Creature.Stage;
import application.Region.Resources;
import application.Skin.COLORS;
import application.Skin.PIGMENTS;
import application.Skin.SHADES;

public class Dragon extends Creature {
	
	private static int drgn_count = 0; 
	private static ArrayList<Creature> dragon_population = new ArrayList<Creature>();
	private static int male_pop = 0;
	private static int female_pop = 0; 
	private static ArrayList<Dragon> legends = new ArrayList<Dragon>();

	private String first_name = "";
	private String last_name = ""; 
	
	public Dragon(Double avg, Skin new_skin, int avg_life, Region region) {
		Random rand = new Random();
		drgn_count = drgn_count + 1;
		id = drgn_count;
		first_name = new_skin.get_color().toString().toLowerCase();
		last_name = "";
		name = first_name + last_name + "#" + id; 
		this.current_region = region; 
		my_class = Dragon.class;
		int gender_code = current_region.gender_check(current_region.get_dragon_population());
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
			
			if(Creature.get_avg_season_limit(region.get_dragon_population()) != 0) {
				int avg_limit = Creature.get_avg_season_limit(region.get_dragon_population());
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
				season_limit = rand.nextInt(18) + 1;
			}
			
		}
		else {
			season_limit = 144;
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
			if (avg != null) {
				if (avg > this.get_stat_coeff()) {
					attack_growth += avg/10;
					defense_growth += avg/10;
					speed_growth += avg/10;
				}
				else if (avg == this.get_stat_coeff()) {
					//do nothing
				}
				else {
					attack_growth = attack_growth - avg/10;
					defense_growth = defense_growth - avg/10;
					speed_growth = speed_growth - avg/10;
				}
			}
		}
		immune_sys = rand.nextDouble();
		int flip = rand.nextInt(2);
		if (flip == 1) {
			if (!current_region.get_dragon_population().isEmpty()) {
				if (get_avg_imm_sys(current_region.get_dragon_population()) > this.get_immune_sys()) {
					immune_sys = immune_sys + get_avg_imm_sys(current_region.get_dragon_population())/10;
				}
				else if (get_avg_imm_sys(current_region.get_dragon_population()) == this.get_immune_sys()) {
					//do nothing
				}
				else {
					immune_sys = immune_sys - get_avg_imm_sys(current_region.get_dragon_population())/10;
				}
			}
		}
		aggression = rand.nextDouble();
		int flip2 = rand.nextInt(2);
		if (flip2 == 1) {
			if (!current_region.get_dragon_population().isEmpty()) {
				if (get_avg_aggression(current_region.get_dragon_population()) > this.get_aggression()) {
					aggression = aggression + get_avg_aggression(current_region.get_dragon_population())/10;
				}
				else if (get_avg_aggression(current_region.get_dragon_population())== this.get_aggression()) {
					//do nothing
				}
				else {
					aggression = aggression - get_avg_aggression(current_region.get_dragon_population())/10;
				}
			}
		}
		parent_one = null; 
		parent_two = null; 
		population.add(this);
		dragon_population.add(this);
		current_region.get_dragon_population().add(this);
	}
	
	/*
	 * Custom Constructor
	 */
	public Dragon (Skin skin, Gender gender, Double atk_g, Double def_g, Double spd_g, Double agg, Double imm, int span, String first_name, String last_name, Region current_region) {
		drgn_count = drgn_count + 1;
		id = drgn_count;
		this.first_name = first_name;
		this.last_name = last_name; 
		name = first_name + last_name + "#" + id; 
		Name.addActiveName(first_name+last_name);
		this.skin = skin; 
		this.gender = gender; 
		this.current_region = current_region; 
		if (gender == Gender.FEMALE) {
			female_pop++;
			if(Creature.get_avg_season_limit(current_region.get_dragon_population()) != 0) {
				season_limit = Creature.get_avg_season_limit(current_region.get_dragon_population()); 
			}
			else {
				season_limit = 12;
			}
		}
		else {
			season_limit = 144;
			male_pop++;
		}
		
		if(season_limit <= 0) {
			season_limit = 1;
		}
		
		//season_limit = new Random().nextInt(18) + 1; 
		
		stage = get_stage();
		my_class = Dragon.class;
		this.defense_growth = def_g;
		this.speed_growth = spd_g; 
		this.attack_growth = atk_g; 
		this.aggression = agg;
		this.immune_sys = imm; 
		this.life_span = span; 
		
		parent_one = null; 
		parent_two = null; 
		population.add(this);
		dragon_population.add(this);
		current_region.get_dragon_population().add(this);
	}
	
	/*
	 * Cloning constructor
	 * Creates a new baby copy of whatever Dragon you want to clone 
	 */
	public Dragon(Dragon one) {
		drgn_count = drgn_count + 1;
		id = drgn_count;
		name = "Dragon#" + id; 
		my_class = Dragon.class;
		first_name = one.first_name;
		last_name = one.last_name;
		gender = one.gender;
		season_limit = one.season_limit;
		my_class = Dragon.class;
		this.current_region = one.current_region; 
		if (gender == Gender.FEMALE) {
			female_pop++;
		}
		else {
			male_pop++;
		}
		skin = one.skin;
		stage = get_stage();
		attack_growth = one.attack_growth;
		defense_growth = one.defense_growth;
		speed_growth = one.speed_growth;
		parent_one = one.parent_one; 
		parent_two = one.parent_two; 
		aggression = one.aggression;
		immune_sys = one.immune_sys;
		life_span = one.life_span;
		population.add(this);
		dragon_population.add(this);
		current_region.get_dragon_population().add(this);
	}
	
	/*
	 * Reproduction constructor 
	 * Creates a baby dragon who inherits traits from its parents
	 */
	private Dragon(Dragon one, Dragon two, int code) {
		Random rand = new Random();
		drgn_count = drgn_count + 1;
		id = drgn_count;
		my_class = Dragon.class;
		//name = "Dragon#" + id; 
		first_name = two.first_name;
		last_name = one.last_name; 
		name = first_name + last_name + "#" + id; 
		this.current_region = one.current_region; 
		parent_one = one; 
		parent_two = two;
		int gender_code = current_region.gender_check(current_region.get_dragon_population());
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
				//int diff = Math.abs(parent_one.season_limit - Creature.get_avg_season_limit(current_region.get_dragon_population()));
				int flip = rand.nextInt(3);
				if(flip == 1) {
					season_limit = parent_one.get_season_limit() - 2; 
				}
				else if (flip == 2) {
					season_limit = parent_one.get_season_limit();
				}
				else {
					season_limit = parent_one.get_season_limit() + 2;
				}
			}
			else {
				//int diff = Math.abs(parent_two.season_limit - Creature.get_avg_season_limit(current_region.get_dragon_population()));
				int flip = rand.nextInt(3);
				if(flip == 1) {
					season_limit = parent_two.get_season_limit() - 2; 
				}
				else if (flip == 2) {
					season_limit = parent_two.get_season_limit();
				}
				else {
					season_limit = parent_two.get_season_limit() + 2;
				}
			}
		}
		else {
			male_pop++;
			season_limit = 144;
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
		dragon_population.add(this);
		current_region.get_dragon_population().add(this);
	}
	
	/*
	 * Have two Dragons reproduce to make a new baby Dragon. The baby will inherit traits from both parents
	 * as well as possible mutations. 
	 * 
	 * Input:  Dragon two  - the Dragon that This should reproduce with
	 * Output: Dragon baby - the offspring of This and two
	 */
	public Dragon reproduce(Dragon two) {
		
		try {
			if (this.reachedSeasonLimit() | two.reachedSeasonLimit()) {
				//System.out.println("One or more had reached their season limit.");
				return null;
			}
			else if (gender != two.gender & get_immediate_family(current_region.get_dragon_population()).contains(two) == false & stage == Stage.ADULT & two.stage == Stage.ADULT) {
				System.out.println(name + " laid an egg with " + two.name);
				Dragon baby = null;
				if (male_pop > female_pop) {
					baby = new Dragon(this, two, 0);
				} 
				else if (female_pop > male_pop) {
					baby = new Dragon(this, two, 1);
				}
				else {
					baby = new Dragon(this, two, 2);
				}
				Name.rename(this, two, baby);
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

	private Prey choose_prey() {
		
		Prey chosen = null;
		ArrayList<Creature> easy_prey = new ArrayList<Creature>();
		ArrayList<Creature> hard_prey = new ArrayList<Creature>();
		for(Creature p: current_region.get_prey_population()) {
			
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
			chosen = (Prey) easy_prey.get(rand.nextInt(easy_prey.size()));
		}
		else if (!hard_prey.isEmpty() & easy_prey.isEmpty()) {
			chosen = (Prey) hard_prey.get(rand.nextInt(hard_prey.size()));
		}
		else if(isHealthy() & aggression > 0.5 | easy_prey.isEmpty()) {
			chosen = (Prey) hard_prey.get(rand.nextInt(hard_prey.size()));
		}
		else {
			chosen = (Prey) easy_prey.get(rand.nextInt(easy_prey.size()));
		}
		return chosen;
	}
	
	/*
	 * Considering all factors of This Dragon (Skin, Age, Experience, Growths, Health, family, etc...)combined with an element of chance, have the Dragon attempt to hunt got iys food. If it 
	 * succeeds, it gains nourishment as well as experience. If it fails, it has exhausted itself for nothing. 
	 */
	@Override
	public void hunt() {
		
		Prey prey = choose_prey();
		
		if(prey == null) {
			//System.out.println(name + " could find no prey to hunt.");
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
		
		//System.out.println(name + " begins to hunt " + prey.get_name());
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
			chance = chance + experience + (speed_growth + defense_growth + attack_growth)*10;
	
		}
		
		chance += experience/2 + speed + defense + attack; 
		chance = (int) (chance - (max_hp-current_hp)/10 - (100-stamina)/10 - (100-hunger)/10 - (100 - thirst)/10 + growth*.75);
		chance += speed - prey.speed;
		chance += attack - prey.defense;
		
		for (Creature e: this.ancestors) {
			if(!e.isDead & e.get_stage() == Stage.ELDER) {
				chance+=2; 
			}
		}
		
		for(Virus v: infections) {
			chance = chance - 5; 
		}
		
		if(prey.isAdult() & this.isAdult()) {
			if(prey.get_aggression() > 0.5 & prey.attack > this.defense_growth) {
				System.out.println(prey.get_name() + " attempts to defend themselves against (" + get_stage() + ") " + name);
				prey.fight(this);
				if(prey.isDead & !this.isDead) {
					System.out.println(name + " caught and ate (" + prey.get_stage() + ") " + prey.get_name());
					eat(prey);
					experience++; 
				}
				else if (!prey.isDead & this.isDead) {
					System.out.println(prey.get_name() + " stood its ground and killed (" + get_stage() + ") " + name);
				}
				else if (!prey.isDead & !this.isDead) {
					System.out.println("Neither could kill the other, so the hunt continues.");
				}
				else {
					System.out.println("The hunter and the prey killed each other.");
				}
			}
		}
		else if (prey.isMinor()) {
			//their parents will attempt to protect them, if they are alive, healthy, and aggressive & by chance and number of minor children
			prey.be_defended(this);
		}
		
		if(this.isDead | this.isSatisfied()) {
			return; 
		}
		
		if(!prey.isDead) {
			if((this.isHealthy() | this.aggression > 0.5)) {
				int prey_chance = prey.be_hunted(this);
				if (chance >= prey_chance | prey.isDead) {
					eat(prey);
					experience+= determine_experience(prey); 
					prey.die();
				}
				else {
					System.out.println(name + " failed to catch (" + prey.get_stage() + ") " + prey.get_name());
					current_hp = current_hp - 10;
					hunger = hunger - 10; 
				}
			}
			else {
				System.out.println(name + " gave up on hunting (" + prey.get_stage() + ") " + prey.get_name());
			}
		}
		stamina = stamina - 20;
		thirst = thirst - 20; 
		grow(); 
		check_status();
	}
	
	public int be_hunted(Predator hunter) {
		double chance = 0; 
		
		//CAMO
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
		
		//FAMILY INTERVENTION / TEACHING
		if (this.stage == Stage.EGG | this.stage == Stage.HATCHLING | this.stage == Stage.ADOLESCENT) {
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
		
		chance += experience/2 + speed + defense + attack; 
		chance = chance - (max_hp-current_hp)/10 - (100-stamina)/10 - (100-hunger)/10 - (100 - thirst)/10 + growth*.75;
		
		//Compare Predator's speed and offense to Dragon's Speed and defense
		chance += defense - hunter.attack; 
		chance += speed - hunter.speed;
		
		for(Virus v: infections) {
			chance = chance - 5; 
		}
		
		stamina = stamina - 20;
		thirst = thirst - 10; 
		hunger = hunger - 10; 
		experience += determine_experience(hunter);
		grow();
		
		return (int) (chance);
	}
	
	public void scavenge() {
		
		int chance = (current_region.get_prey_population().size()/4)/current_region.get_dragon_population().size(); 
		
		if (chance <= 0) {
			if(current_region.get_resource_levels(Resources.PREY_FOOD) <= 10) {
				famine = true;
				//System.out.println(name + " couldn't even find sticks to eat.");
			}
			else {
				hunger += 7;
				//System.out.println(name + " finds nothing to eat, so they chew on some old bones.");
				//current_region.deplete_resource(Resources.PREY_FOOD, 2);
			}
		}
		else {
			hunger += 35; 
			//System.out.println(name + " finds a prey carcass to gnaw on.");
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
	
	public boolean check_infection() {
		//return true if killed
		ArrayList<Virus> recovered = new ArrayList<Virus>();
		double resist_coeff = immune_sys - (max_hp-current_hp)/1000 - (100 - hunger)/1000 - (100 - thirst)/1000 - (100 - stamina)/1000;
		for (Virus v: infections) {
			if (v.get_resil() < resist_coeff) {
				recovered.add(v);
				v.remove_infected(this, false);
				setVisiblyIll(false); 
				immunities.add(v);
				System.out.println(this.name + " recovereed from Virus #" + v.get_id());
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
	
	public boolean infect(Dragon two) {
		
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
				System.out.println(this.name + " infected " + two.name + " with Virus #" + w.get_id());
				return true;
			}
			else {
				return false;
			}

		}
		
		return false;
		
	}
	
	@Override
	public void die() {
		//System.out.println(name + " has died. \n");
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
			
			if (growth >= 65) {
				legends.add(this);
			}
			else if (experience >= 100 & living_descendants.size() > 0) {
				legends.add(this);
			}
			
			if(legends.contains(this)) {
				Name.retire_name(first_name+last_name);
			}
			
			for (Virus e: infections) {
				e.remove_infected(this, false);
			}
			isDead = true;
			current_hp = 0; 
			population.remove(this);
			dragon_population.remove(this);
			current_region.get_dragon_population().remove(this);
			Name.removeActiveName(this.first_name + this.last_name);
			if (gender == Gender.FEMALE) {
				female_pop = female_pop - 1;
			}
			else {
				male_pop = male_pop - 1;
			}
			current_region.replenish_resource(Resources.PREY_FOOD, (int)(this.growth));
		}
	}
	
	public void add_to_legends() {
		legends.add(this);
		Name.retire_name(first_name+last_name);
	}

	public void rename(String first_name, String last_name) {
		this.first_name = first_name;
		this.last_name = last_name;
		this.name = first_name + last_name + "#" + id; 
	}
	
	public String get_first_name() {
		return first_name;
	}
	
	public String get_last_name() {
		return last_name; 
	}
	
	public int find_best_stat_for_name() {
		int code = 0;
		HashMap<Double, Integer> stats = new HashMap<Double, Integer>();
		stats.put(attack_growth, 1);
		stats.put(defense_growth, 2);
		stats.put(speed_growth, 3);
		
		Double highest_stat = 0.0;
		
		if(aggression > this.get_stat_coeff()) {
			code = 4;
		}
		else if(1.0 - aggression > this.get_stat_coeff()) {
			code = 5; 
		}
		else {
		
			for(Double num: stats.keySet()) {
				if(num > highest_stat) {
					highest_stat = num;
				}
			}
			
			code = stats.get(highest_stat);
			
		}
		
		return code; 
	}
	
	@Override
	public int get_season_limit() {

		if (gender == Gender.FEMALE) {
			//return 12;
			return season_limit; 
		}
		else {
			//return season limit;
			return 144; 
		}
		
	}
	
	@Override
	public String get_stats() {
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
		
		String legacy = ", child of ";
		String ancestry = "";
		for (Creature e: ancestors) {
			ancestry += e.get_name() + ", ";
			if(legends.contains(e)) {
				legacy += e.get_name() + ", ";
			}
		}
		
		if(!legacy.contains("#")) {
			legacy = "";
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
		
		stats +=  "Dragon ID: " + name + legacy + "\n"
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
	
	//class functions 
	/*
	 * public static int season(ArrayList<Creature> pop) {
	 * 
	 * HashMap<Creature, Integer> wins = new HashMap<Creature, Integer>();
	 * HashMap<Creature, Integer> losses = new HashMap<Creature, Integer>();
	 * ArrayList<Creature> dead = new ArrayList<Creature>(); HashMap<Creature,
	 * Creature> match_ups = new HashMap<Creature, Creature>(); ArrayList<Creature>
	 * babies = new ArrayList<Creature>();
	 * 
	 * Random rand = new Random(); ArrayList<Creature> younglings = new
	 * ArrayList<Creature>(); ArrayList<Creature> challengers = new
	 * ArrayList<Creature>(); for (Creature d: pop) { if (!d.isDead()) {
	 * d.reset_season_count(); //int seed = rand.nextInt(100); if (d.isAdult() &
	 * d.isHealthy() & !d.isDead() & !d.isVisiblyIll() & !d.reachedSeasonLimit()) {
	 * challengers.add(d); wins.put(d, 0); losses.put(d, 0); } else if (d.isMinor())
	 * { younglings.add(d); for (Creature p: d.get_parents()) { if (p != null) { if
	 * (!p.isDead()) { p.infect(d); d.infect(p); } } } } } }
	 * 
	 * for(Creature d: younglings) { if (!d.isVisiblyIll()) { for(Creature m:
	 * younglings) { if (d != m & !m.isVisiblyIll()) { d.infect(m); } } } }
	 * 
	 * for (Creature d: challengers) { //System.out.println(d.get_name() +
	 * " sizes up the field."); ArrayList<Creature> matches = new
	 * ArrayList<Creature>(); int limit = 0; if (d.isDead()) { dead.add(d);
	 * continue; } else if (!d.isAdult()) { continue; } else if (!d.isHealthy() |
	 * d.isVisiblyIll()) { continue; } else if (d.reachedSeasonLimit()) { continue;
	 * } else {
	 * 
	 * for (Creature m: challengers) {
	 * 
	 * if(match_ups.get(m) == d | match_ups.get(d) == m) { continue; }
	 * 
	 * boolean will = false; if (!m.isRelated(d) |
	 * (!m.get_immediate_family(population).contains(d) & (d.get_num_of_offspring()
	 * < 2) & (m.get_num_of_offspring() < 2))) { will = true; }
	 * 
	 * if (!m.isAdult()) { continue; } else if (m == d) { continue; } else if
	 * (m.isDead()) { dead.add(m); continue; } if (d.isDead()) { dead.add(d); break;
	 * } else if (m.isVisiblyIll()) { break; } else if (!m.isHealthy() &
	 * m.get_aggression() < 0.5) { continue; } else if (!d.isHealthy() &
	 * d.get_aggression() < 0.5) { break; } else if (d.reachedSeasonLimit() |
	 * m.reachedSeasonLimit()) { break; } else if ( d.get_gender() == m.get_gender()
	 * & (!d.get_immediate_family(population).contains(m) &
	 * !m.get_immediate_family(population).contains(d)) &
	 * !(d.find_potential().isEmpty() | m.find_potential().isEmpty())) {
	 * 
	 * boolean disjoint = true; for (Creature g: d.find_potential()) { for(Creature
	 * b: m.find_potential()) { if (g == b) { disjoint = false; } } }
	 * 
	 * if(disjoint) { System.out.println(m.get_name() + " & " + d.get_name() +
	 * " had nothing to fight over."); continue; }
	 * 
	 * //they'll fight d.infect(m); Creature winner = d.fight(m); match_ups.put(d,
	 * m); match_ups.put(m, d); if (winner == null) { dead.add(d); dead.add(m); }
	 * else if (d == winner) { wins.put(d, wins.get(d)+1); losses.put(m,
	 * losses.get(m)+1); } else { wins.put(m, wins.get(m)+1); losses.put(d,
	 * losses.get(d)+1); break; } continue; } else if ((d.get_gender() !=
	 * m.get_gender()) & (will)) {
	 * 
	 * if (wins.get(d) > losses.get(d) | match_ups.isEmpty()) {
	 * System.out.println(d.get_name() + " wins the heart of " + m.get_name() +
	 * " with a score of " + wins.get(d) + "w/l" + losses.get(d)); matches.add(m); }
	 * } else if ((d.isRelated(m))) { d.infect(m); } } for (Creature match:
	 * d.rank_choices(matches)) { if (!d.isDead() & !match.isDead()) {
	 * 
	 * d.infect(match); Dragon baby =
	 * (Dragon.class.cast(d)).reproduce(Dragon.class.cast(match)); if (baby != null)
	 * { babies.add(baby); //order_view.getItems().add(baby.get_name()); } } } } }
	 * 
	 * for (Creature d: dead) { population.remove(d); } int num_babies =
	 * babies.size(); for (Creature b: babies) { population.add(b); }
	 * //System.out.println(num_babies + " eggs were laid."); babies.clear(); return
	 * num_babies; }
	 */
	
	public static int get_dragon_count() {
		return drgn_count;
	}
	
	public static ArrayList<Dragon> get_legends() {
		return legends; 
	}
	
	public static int get_global_population() {
		return dragon_population.size();
	}
	
	public static double get_avg_immunity() {
		
		double avg = 0;
		for (Creature d: dragon_population) {
			avg += d.immune_sys;
		}
		if (dragon_population == null) {
			return 0;
		}
		else if(dragon_population.size() == 0) {
			return 0;
		}
		else {
			return avg/dragon_population.size();
		}
		
	}
	
	public static double get_avg_stat_coeff() {
		
		double avg = 0; 

		for (Creature d: dragon_population) {
			avg += d.get_stat_coeff();
		}
		if (dragon_population.size() != 0) {
			avg = avg/dragon_population.size();
		}
		return avg;
		
	}
	
	public static double get_avg_aggression () {
		double avg = 0;
		for (Creature d: dragon_population) {
			avg += d.get_aggression();
		}
		return avg/dragon_population.size();
	}
	
	public static double get_avg_imm_sys() {
		
		double avg = 0;
		for (Creature d: dragon_population) {
			avg += d.get_immune_sys();
		}
		return avg/dragon_population.size();
		
	}
	
	public static int get_avg_life_span() {
		
		int avg = 0;
		for (Creature d: dragon_population) {
			avg += d.life_span;
		}
		if (dragon_population == null) {
			return 0;
		}
		else if(dragon_population.size() == 0) {
			return 0;
		}
		else {
			return avg/dragon_population.size();
		}
		
	}
	
	
	public static int get_male_pop() {
		return male_pop;
	}
	
	public static int get_female_pop() {
		return female_pop; 
	}
	
	public static Dragon get_winner() {
		Dragon winner = legends.get(0);
		for (Dragon e: legends) {
			if (winner.descendants.size() < e.descendants.size()) {
				winner = e; 
			}
		}
		return winner; 
	}

}
