package application;

import java.util.ArrayList;
import java.util.Random;

public class Dragon2 extends Creature {
	
	private static int drgn_count = 0; 
	private static ArrayList<Dragon> population = new ArrayList<Dragon>();
	private static int male_pop = 0;
	private static int female_pop = 0; 
	private static ArrayList<Dragon> legends = new ArrayList<Dragon>();
	
	private String first_name = "";
	private String last_name = ""; 
	
	public Dragon2(Double avg, Skin new_skin, int avg_life) {
		drgn_count = drgn_count + 1;
		id = drgn_count;
		//name = "Dragon#" + id; 
		first_name = new_skin.get_color().toString().toLowerCase();
		last_name = "";
		name = first_name + last_name + "#" + id; 
		//Name.rename(this);
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

	@Override
	protected void migrate() {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected void scavenge() {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected boolean check_infections() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	protected boolean infect(Creature two) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	protected void run() {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected String get_stats() {
		// TODO Auto-generated method stub
		return null;
	}

}
