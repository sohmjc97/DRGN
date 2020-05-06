package application;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

import application.Skin.COLORS;
import application.Skin.PIGMENTS;
import application.Skin.SHADES;

public class Dragon {
	
	private static int drgn_count = 0; 
	private static double growth_rate = 0.1; 
	private static Skin.COLORS env_color = Skin.COLORS.WHITE;
	private static Skin.SHADES env_shade = Skin.SHADES.LIGHT;
	private static Skin.PIGMENTS env_pigment = Skin.PIGMENTS.PALE;
	private static ArrayList<Dragon> population = new ArrayList<Dragon>();
	private static int male_pop = 0;
	private static int female_pop = 0; 
	private static ArrayList<Dragon> legends = new ArrayList<Dragon>();
	
	private static enum SPECIES {
		WYVERN, 
		QUADREN, 
		SERPENT
	}
	private static enum ELEMENT {
		WATER,
		FIRE,
		AIR,
		EARTH
	}
	public static enum GENDER {
		MALE,
		FEMALE
	}
	
	public static enum STAGE {
		EGG, 
		HATCHLING, 
		ADOLESCENT,
		ADULT,
		ELDER
	}
	
	private final int id;
	private final SPECIES species; 
	private final ELEMENT element;
	private final GENDER gender; 
	private final Skin skin; 
	private final Dragon parent_one; 
	private final Dragon parent_two;
	
	private ArrayList<Dragon> ancestors = new ArrayList<Dragon>();
	private ArrayList<Dragon> offspring = new ArrayList<Dragon>(); 
	private ArrayList<Dragon> descendants = new ArrayList<Dragon>();
	
	private String name;
	private String first_name = "";
	private String last_name = ""; 
	private STAGE stage; 
	private boolean isDead = false;
	
	private double growth = 0; 
	private int experience = 0; 
	private int life_span = 35; 
	
	private double max_hp = 10.0; 
	private double current_hp = 10.0;
	
	private double hunger = 100.0;
	private double thirst = 100.0;
	private double stamina = 100.0;
	
	private Double attack = 1.0;
	private double attack_growth;
	private Double defense = 1.0;
	private double defense_growth;
	private Double speed = 1.0; 
	private double speed_growth;
	private double aggression;
	
	ArrayList<Virus> infections = new ArrayList<Virus>();
	ArrayList<Virus> immunities = new ArrayList<Virus>();
	private double immune_sys;
	private boolean visible;
	private final int season_limit;
	private int curr_season_count = 0; 
	
	public Dragon(Double avg, Skin new_skin, int avg_life) {
		drgn_count = drgn_count + 1;
		id = drgn_count;
		//name = "Dragon#" + id; 
		first_name = new_skin.get_color().toString().toLowerCase();
		last_name = "";
		name = first_name + last_name + "#" + id; 
		
		//Name.rename(this);
		
		species = random_species(); 
		element = random_element();
		if (male_pop > female_pop) {
			gender = GENDER.FEMALE;
		} 
		else if (female_pop > male_pop) {
			gender = GENDER.MALE;
		}
		else {
			gender = random_gender();
		}
		
		if (gender == GENDER.FEMALE) {
			female_pop++;
			season_limit = 12;
		}
		else {
			season_limit = 24;
			male_pop++;
		}
		
		skin = new_skin;
		life_span = avg_life; 
		stage = get_stage();
		Random rand = new Random();
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
			if (!population.isEmpty()) {
				if (get_avg_imm_sys(population) > this.get_immune_sys()) {
					immune_sys = immune_sys + get_avg_imm_sys(population)/10;
				}
				else if (get_avg_imm_sys(population) == this.get_immune_sys()) {
					//do nothing
				}
				else {
					immune_sys = immune_sys - get_avg_imm_sys(population)/10;
				}
			}
		}
		aggression = rand.nextDouble();
		int flip2 = rand.nextInt(2);
		if (flip2 == 1) {
			if (!population.isEmpty()) {
				if (get_avg_aggression(population) > this.get_aggression()) {
					aggression = aggression + get_avg_aggression(population)/10;
				}
				else if (get_avg_aggression(population)== this.get_aggression()) {
					//do nothing
				}
				else {
					aggression = aggression - get_avg_aggression(population)/10;
				}
			}
		}
		parent_one = null; 
		parent_two = null; 
		population.add(this);
	}
	
	/*
	 * Custom Constructor
	 */
	public Dragon (Skin skin, GENDER gender, Double atk_g, Double def_g, Double spd_g, Double agg, Double imm, int span, String first_name, String last_name) {
		drgn_count = drgn_count + 1;
		id = drgn_count;
		this.first_name = first_name;
		this.last_name = last_name; 
		name = first_name + last_name + "#" + id; 
		Name.addActiveName(first_name+last_name);
		species = random_species(); 
		element = random_element();
		this.skin = skin; 
		this.gender = gender; 
		if (gender == GENDER.FEMALE) {
			female_pop++;
			season_limit = 12;
		}
		else {
			season_limit = 24;
			male_pop++;
		}
		stage = get_stage();
		
		this.defense_growth = def_g;
		this.speed_growth = spd_g; 
		this.attack_growth = atk_g; 
		this.aggression = agg;
		this.immune_sys = imm; 
		this.life_span = span; 
		
		parent_one = null; 
		parent_two = null; 
		population.add(this);
	}
	
	/*
	 * Cloning constructor
	 * Creates a new baby copy of whatever Dragon you want to clone 
	 */
	public Dragon(Dragon one) {
		drgn_count = drgn_count + 1;
		id = drgn_count;
		name = "Dragon#" + id; 
		first_name = one.first_name;
		last_name = one.last_name;
		species = one.species; 
		element = one.element;
		gender = one.gender;
		season_limit = one.season_limit;
		if (gender == GENDER.FEMALE) {
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
	}
	
	/*
	 * Reproduction constructor 
	 * Creates a baby dragon who inherits traits from its parents
	 */
	private Dragon(Dragon one, Dragon two, int code) {
		drgn_count = drgn_count + 1;
		id = drgn_count;
		//name = "Dragon#" + id; 
		first_name = two.first_name;
		last_name = one.last_name; 
		name = first_name + last_name + "#" + id; 
		//Name.rename(one, two, this);
		species = choose_species(one, two); 
		element = choose_element(one, two);
		if (code == 0) {
			gender = GENDER.FEMALE;
		}
		else if (code == 1) {
			gender = GENDER.MALE;
		}
		else {
			gender = random_gender();
		}
		if (gender == GENDER.FEMALE) {
			female_pop++;
			season_limit = 12;
		}
		else {
			male_pop++;
			season_limit = 24;
		}
		skin = new Skin(one.skin, two.skin);
		stage = get_stage();
		parent_one = one; 
		parent_two = two;
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
		for (Dragon a: one.ancestors) {
			if (!ancestors.contains(a)) {
				ancestors.add(a);
			}
		}
		ancestors.add(two);
		for (Dragon e: two.ancestors) {
			if (!ancestors.contains(e)) {
				ancestors.add(e);
			}
		};
		
		for (Dragon d: ancestors) {
			if (!d.descendants.contains(d)) {
				d.descendants.add(this);
			}
		}
		
		//allow for random mutation
		Random rand = new Random();
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
			if (this.curr_season_count >= this.season_limit | two.curr_season_count >= two.season_limit) {
				return null;
			}
			else if (gender != two.gender & get_immediate_family(population).contains(two) == false & stage == STAGE.ADULT & two.stage == STAGE.ADULT) {
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
			System.out.println(e.getMessage());
			return null;
		}
		
	}
	
	/*
	 * Considering all factors of This Dragon (Skin, Age, Experience, Growths, Health, family, etc...)combined with an element of chance, have the Dragon attempt to hunt got iys food. If it 
	 * succeeds, it gains nourishment as well as experience. If it fails, it has exhausted itself for nothing. 
	 */
	public void hunt() {
		//System.out.println(name + " is on the hunt.");
		double chance = 10; 
		if (population.size() > 15) {
			chance = chance - population.size()/2; 
		}
		if (skin.get_color() == env_color) {
			chance += 10;
		}
		else {
			int dist = skin.get_color_distance(env_color);
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
		if (skin.get_shade() == env_shade) {
			chance += 10;
		}
		else {
			int dist = skin.get_shade_distance(env_shade);
			if(dist == 2) {
				chance = chance - 5;
			}
		}
		if (skin.get_pigment() == env_pigment) {
			chance += 10;
		}
		else {
			int dist = skin.get_pigment_distance(env_pigment);
			if(dist == 2) {
				chance = chance - 5;
			}
		}
		if (this.stage == STAGE.HATCHLING | this.stage == STAGE.ADOLESCENT) {
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
		chance += experience + speed + attack;
		
		for (Dragon e: this.ancestors) {
			if(!e.isDead & e.get_stage() == STAGE.ELDER) {
				chance+=2; 
			}
		}
		
		for(Virus v: infections) {
			chance = chance - 5; 
		}
		
		Random rand = new Random();
		int seed = rand.nextInt(100) + 1;
		if (chance >= seed) {
			//System.out.println(name + " caught fresh prey.");
			hunger = 100; 
			experience++; 
		}
		else {
			//System.out.println(name + " failed to catch anything.");
			hunger = hunger - 10; 
		}
		stamina = stamina - 20;
		thirst = thirst - 20; 
		grow(); 
		check_status();
	}
	
	public int hunt(Prey p) {
		return 0;
	}
	
	public void be_hunted() {
		//System.out.println(name + " is being hunted by a predator.");
		double chance = 40; 
		if (population.size() > 15) {
			chance = chance + population.size()/10; 
		}
		if (skin.get_color() == env_color) {
			chance += 10;
		}
		else {
			int dist = skin.get_color_distance(env_color);
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
		if (skin.get_shade() == env_shade) {
			chance += 10;
		}
		else {
			int dist = skin.get_shade_distance(env_shade);
			if(dist == 2) {
				chance = chance - 5;
			}		
		}
		if (skin.get_pigment() == env_pigment) {
			chance += 10;
		}
		else {
			int dist = skin.get_pigment_distance(env_pigment);
			if(dist == 2) {
				chance = chance - 5;
			}
		}
		if (this.stage == STAGE.EGG | this.stage == STAGE.HATCHLING | this.stage == STAGE.ADOLESCENT) {
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
		chance += experience/2 + speed + defense + attack; 
		chance = chance - (max_hp-current_hp)/10 - (100-stamina)/10 - (100-hunger)/10 - (100 - thirst)/10 + growth*.75;
		
		for (Dragon e: this.ancestors) {
			if(!e.isDead & e.get_stage() == STAGE.ELDER) {
				chance+=2; 
			}
		}
		
		for(Virus v: infections) {
			chance = chance - 5; 
		}
		
		Random rand = new Random();
		int seed = rand.nextInt(100) + 1;
		if (chance <= seed) {
			//System.out.println(name + " was caught and killed by a predator.");
			die();
		}
		else if (chance - 20 <= seed) {
			//System.out.println(name + " was wounded by a predator, but managed to escape.");
			current_hp = current_hp - 10; 
			stamina = stamina - 20;
			thirst = thirst - 20; 
			hunger = hunger - 10; 
			experience++;
			grow();
			check_status();
		}
		else {
			//System.out.println(name + " evaded the predator.");
			stamina = stamina - 20;
			thirst = thirst - 10; 
			hunger = hunger - 10; 
			experience++;
			grow();
			check_status();
		}
		
	}
	
	public void rest() {
		
		//System.out.println(name + " sleeps.");
		heal();
		grow();
		stamina = 100;
		hunger = hunger - 20;
		thirst = thirst - 20; 
		check_status();
		
	}
	
	public void scavenge() {
		
		//System.out.println(name + " finds an old carcass to gnaw on.");
		int chance = 100 - population.size(); 
		
		if (chance < 0) {
			hunger += 10;
			if (hunger > 100) {
				hunger = 100; 
			}
		}
		else {
			hunger += 50; 
			if (hunger > 100) {
				hunger = 100; 
			}
		}
		
		Random rand = new Random();
		if(rand.nextInt(100) < 5) {
			Virus.manual_infect(this);
		}
		
		thirst = thirst - 10;
		stamina = stamina - 10;
		check_status();
		
	}
	
	public void drink() {
		
		//System.out.println(name + " takes a drink.");
		//heal();
		
		Random rand = new Random();
		if(rand.nextInt(100) < 5) {
			Virus.manual_infect(this);
		}
		
		grow();
		thirst = 100;
		stamina = stamina - 10; 
		hunger = hunger - 10;
		check_status();
		
	}
	
	public void heal() {
		
		double regen = 5; 
		regen += hunger/20 + thirst/20 + current_hp/20 + stamina/20;
		current_hp += regen;
		if (current_hp > max_hp) {
			current_hp = max_hp;
		}
		//System.out.println(name + " has regained " + regen + "health. \n");
	
	}
	
	public void grow() {
		
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
		if(stage == STAGE.ELDER) {
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
	
	public Dragon fight(Dragon enemy) {
		
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
		
		Dragon winner = null; 
		Dragon loser = null; 
		
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
				Virus.manual_infect(winner);
			}
		}
		
		if(!loser.isDead & loser.current_hp < loser.max_hp/2) {
			Random rand = new Random();
			if(rand.nextInt(100) < 5) {
				Virus.manual_infect(loser);
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
	
	public void check_status() {
		
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
					die(); 
					return;
				}
			}
		}
		else {
			//die from starvation, thirst, exhaustion, or injury
			for (Virus e: infections) {
				e.remove_infected(this, true);
			}
			die();
			return;
		}
		
		if (check_infection()) {
			for (Virus e: infections) {
				e.remove_infected(this, true);
			}
			this.infections.clear();
			die();
		}
		
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
	
	public void die() {
		//System.out.println(name + " has died. \n");
		
		ArrayList<Dragon> living_descendants = new ArrayList<Dragon>(); 
		for (Dragon e: this.descendants) {
			
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
		Name.removeActiveName(this.first_name + this.last_name);
		if (gender == GENDER.FEMALE) {
			female_pop = female_pop - 1;
		}
		else {
			male_pop = male_pop - 1;
		}
	}
	
	public void run() {
		
		boolean done = false; 
		STAGE current_stage = stage; 
		double current_growth = growth; 
		do {
			
			Random rand = new Random();
			int chance = rand.nextInt(100) +1;
			if (chance > 80) {
				be_hunted(); 
			}
			
			if(current_hp < max_hp/2 & !isDead & hunger > 40 & thirst > 40 & stamina > 30) {
				rest();
			}
			
			while (hunger > 40 & thirst > 40 & stamina > 30 & !isDead) {
				hunt();
			}
			if (thirst <= 40 & !isDead) {
				drink();
			}
			if (hunger <= 40 & !isDead) {
				scavenge();
			}
			if(stamina <= 30 & !isDead) {
				rest();
			}
			
			if (current_stage != stage | (growth - current_growth) >= 5) {
				done = true; 
				break; 
			}
			
		} while (done == false & isDead == false);
		
	}
	
	public void add_to_legends() {
		legends.add(this);
		Name.retire_name(first_name+last_name);
	}
	
	
	public boolean isHealthy() {
		
		if(visible) {
			return false;
		}
		else if (current_hp >= 30 & stamina >= 40 & hunger >= 40 & thirst >= 40) {
			return true;
		}
		return false;
	}
	
	public boolean isDead() {
		if (isDead) {
			return true;
		}
		else {
			return false;
		}
	}
	
	public boolean is_related(Dragon two) {
		
		for (Dragon a: ancestors) {
			for (Dragon b: two.ancestors) {
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
	
	public int get_num_minor_offspring() {
		int num = 0;
		for(Dragon g: offspring) {
			if(g.get_stage() == STAGE.EGG | g.get_stage() == STAGE.HATCHLING | g.get_stage() == STAGE.ADOLESCENT) {
				num++;
			}
		}
		return num; 
		
	}
	
	public ArrayList<Dragon> get_immediate_family(ArrayList<Dragon> pop) {
		
		ArrayList<Dragon> family = new ArrayList<Dragon>(); 
		
		for (Dragon a: this.ancestors) {
			//parents, grandparents, etc.
			family.add(a);
		}
		for (Dragon o: descendants) {
			//children, grand children, etc.
			family.add(o);
		}
		for (Dragon p: pop) {
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
	
	public void add_ancestors(Dragon parent) {
		
		boolean done = false;
		while (!done) {
			if (parent == null) {
				done = true; 
				break;
			}
			else {
				ancestors.add(parent);
				Dragon next_1 = parent.parent_one;
				add_ancestors(next_1);
				Dragon next_2 = parent.parent_two;
				add_ancestors(next_2);
			}
		}
		
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
	
	public STAGE get_stage() {
		if (growth < 1) {
			return STAGE.EGG;
		}
		if (growth < 5) {
			return STAGE.HATCHLING;
		}
		else if (growth < 15) {
			return STAGE.ADOLESCENT;
		}
		else if (growth < 50) {
			return STAGE.ADULT;
		}
		else {
			return STAGE.ELDER;
		}
	}
	
	public SPECIES get_species() {
		return species; 
	}
	
	public ELEMENT get_element() {
		return element; 
	}
	
	public GENDER get_gender() {
		return gender; 
	}
	
	public int get_id() {
		return id; 
	}

	public String get_name() {
		return name; 
	}
	
	public int get_experience() {
		return experience; 
	}
	
	public double get_current_hp() {
		return current_hp; 
	}
	
	public double get_max_hp() {
		return max_hp; 
	}
	
	public double get_aggression() {
		return aggression;
	}
	
	public Skin get_skin() {
		return skin; 
	}
	
	public String get_growth() {
		return round_decimal(growth); 
	}
	
	public int get_life_span() {
		return life_span;
	}
	
	public int get_num_of_offspring () {
		return offspring.size();
	}
	
	public int get_season_limit() {
		return season_limit;
	}
	
	public int get_curr_season_count() {
		return curr_season_count;
	}
	
	public boolean reachedSeasonLimit() {
		if(this.curr_season_count >= season_limit) {
			return true;
		}
		else {
			return false; 
		}
	}
	
	public void reset_season_count() {
		curr_season_count = 0; 
	}
	
	public boolean isVisiblyIll() {
		return visible;
	}
	
	public void setVisiblyIll(boolean visible) {
		this.visible = visible;
	}

	public ArrayList<Virus> get_infections() {
		return infections;
	}
	
	public ArrayList<Virus> get_immunities() {
		return immunities;
	}
	
	public double get_immune_sys() {
		return immune_sys;
	}
	
	public double get_resist_coeff() {
		return immune_sys - (max_hp-current_hp)/1000 - (100 - hunger)/1000 - (100 - thirst)/1000 - (100 - stamina)/1000;
	}
	
	public double get_stat_coeff() {
		
		double total = 0;
		total += attack_growth + defense_growth + speed_growth;
		return total;
		
	}
	
	public Dragon[] get_parents() {
		
		Dragon[] parents = {parent_one, parent_two};
		return parents; 
		
	}
	
	public Dragon get_parent_one() {
		return parent_one;
	}
	
	public Dragon get_parent_two() {
		return parent_two; 
	}
	
	public ArrayList<Dragon> get_offspring() {
		return offspring;
	}
	
	public ArrayList<Dragon> get_ancestors() {
		return ancestors;
	}
	
	public ArrayList<Dragon> get_descendants() {
		return descendants; 
	}

	public ArrayList<Dragon> rank_choices (ArrayList<Dragon> choices) {
		
		choices = rank(choices);
		if (choices.isEmpty()) {
			return choices;
		}
		else if (choices.size() > season_limit) {
			while (choices.size() > season_limit) {
				Dragon last = choices.get(choices.size()-1);
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
				Dragon first = choices.get(count);
				choices.add(first);
				count++;
				
			}
			return choices;
		}
		
	}
	
	public ArrayList<Dragon> rank (ArrayList<Dragon> choices) {
		
		ArrayList<Dragon> rank = new ArrayList<Dragon>(); 
		
		Dragon max = null;
		double highest = 0;
		for (Dragon d: choices) {
			
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
	
	private double get_rating() {
		double rating = 0;
		rating += experience + attack + defense + speed + immune_sys*20 + aggression*10; 
		rating += compare_immunities(this);
		rating += skin.get_color_distance(env_color);
		rating += skin.get_pigment_distance(env_pigment);
		rating += skin.get_shade_distance(env_shade);
		if(infections.isEmpty()) {
			rating+=1;
		}
		else {
			rating = rating-1; 
		}
		return rating;
	}
	
	public int compare_immunities(Dragon two) {
		
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
		for (Dragon i: offspring) {
			babies += i.get_name() + ", ";
		}
		
		String legacy = ", child of ";
		String ancestry = "";
		for (Dragon e: ancestors) {
			ancestry += e.get_name() + ", ";
			if(legends.contains(e)) {
				legacy += e.get_name() + ", ";
			}
		}
		
		if(!legacy.contains("#")) {
			legacy = "";
		}
		
		String progeny = "";
		for (Dragon e: descendants) {
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
		ArrayList<Dragon> living_descendants = new ArrayList<Dragon>(); 
		for (Dragon e: this.descendants) {
			
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
				+ "Offspring: " + babies + "\n"
				+ "Descendants(" + living_descendants.size() + " / " + descendants.size() + "): " + progeny + "\n"
				+ "Living: " + live + "\n"
				+ "Immune Sys: " + round_decimal(immune_sys) + "\n"
				+ "Infections: " + viruses + "\n"
				+ "Immunities: " + immunes  ;

		return stats;
	}
	
	//class functions 
	
	
	public static int get_dragon_count() {
		return drgn_count;
	}
	
	public static ArrayList<Dragon> get_legends() {
		return legends; 
	}
	
	public static int get_pop_size() {
		return population.size();
	}
	
	public static double get_avg_immunity() {
		
		double avg = 0;
		for (Dragon d: population) {
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
	
	public static double get_avg_stat_coeff(ArrayList<Dragon> dragons) {
		
		double avg = 0; 

		for (Dragon d: dragons) {
			avg += d.get_stat_coeff();
		}
		if (dragons.size() != 0) {
			avg = avg/dragons.size();
		}
		return avg;
		
	}
	
	public static double get_avg_aggression (ArrayList<Dragon> dragons) {
		double avg = 0;
		for (Dragon d: dragons) {
			avg += d.get_aggression();
		}
		return avg/dragons.size();
	}
	
	public static double get_avg_imm_sys(ArrayList<Dragon> dragons) {
		
		double avg = 0;
		for (Dragon d: dragons) {
			avg += d.get_immune_sys();
		}
		return avg/dragons.size();
		
	}
	
	public static int get_avg_life_span() {
		
		int avg = 0;
		for (Dragon d: population) {
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
	
	public static Dragon get_winner() {
		Dragon winner = legends.get(0);
		for (Dragon e: legends) {
			if (winner.descendants.size() < e.descendants.size()) {
				winner = e; 
			}
		}
		return winner; 
	}
	
	private static SPECIES choose_species(Dragon one, Dragon two) {
		
		Random rand = new Random(); 
		int a = rand.nextInt(10) + 1;
		if (a <= 5) {
			return one.species;
		}
		else {
			return two.species;
		}
		
	}
	
	private static ELEMENT choose_element(Dragon one, Dragon two) {
		
		Random rand = new Random(); 
		int a = rand.nextInt(10) + 1;
		if (a <= 5) {
			return one.element;
		}
		else {
			return two.element;
		}
		
	}
	
	private static GENDER random_gender() {
		
		Random rand = new Random(); 
		int a = rand.nextInt(10) + 1;
		if (a <= 5) {
			return GENDER.MALE;
		}
		else {
			return GENDER.FEMALE;
		}
		
	}
	
	private static SPECIES random_species() {
		
		Random rand = new Random(); 
		int a = rand.nextInt(3) + 1;
		if (a == 1) {
			return SPECIES.WYVERN;
		}
		else if (a == 2) {
			return SPECIES.QUADREN;
		}
		else {
			return SPECIES.SERPENT;
		}
		
	}
		
	private static ELEMENT random_element() {
		
		Random rand = new Random(); 
		int a = rand.nextInt(4) + 1;
		if (a == 1) {
			return ELEMENT.WATER;
		}
		else if (a == 2) {
			return ELEMENT.AIR;
		}
		else if (a == 3) {
			return ELEMENT.FIRE;
		}
		else {
			return ELEMENT.EARTH;
		}
		
	}
	
	public static Skin.COLORS get_env_color() {
		return env_color;
	}
	
	public static void set_env_color(COLORS color) {
		env_color = color;
	}
	
	public static Skin.SHADES get_env_shade() {
		return env_shade;
	}
	
	public static void set_env_shade(SHADES shade) {
		env_shade = shade;
	}
	
	public static Skin.PIGMENTS get_env_pigment() {
		return env_pigment; 
	}
	
	public static void set_env_pigment(PIGMENTS pigment) {
		env_pigment = pigment; 
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
	
	
	public static void randomize_env() {
		
		Random rand = new Random();
		int color = rand.nextInt(Skin.COLORS.values().length); 
		int count = 0;
		for (COLORS i: COLORS.values()) {
			if (count == color) {
				env_color = i; 
			}
			count++;
		}
		
		int shade = rand.nextInt(Skin.SHADES.values().length); 
		count = 0;
		for (Skin.SHADES i: Skin.SHADES.values()) {
			if (count == shade) {
				env_shade = i; 
			}
			count++;
		}
		
		int pigment = rand.nextInt(Skin.PIGMENTS.values().length); 
		count = 0;
		for (Skin.PIGMENTS i: Skin.PIGMENTS.values()) {
			if (count == pigment) {
				env_pigment = i; 
			}
			count++;
		}
		
	}
	
	@SuppressWarnings("serial")
	private class ActionIllegalException extends Exception {
		
		public ActionIllegalException(String errorMessage) {
			super(errorMessage);
		}
		
	}

}
