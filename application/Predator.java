package application;

import java.util.ArrayList;
import java.util.Random;

import application.Creature.ActionIllegalException;
import application.Creature.Gender;
import application.Creature.Stage;
import application.Dragon.STAGE;

public class Predator extends Creature {
	
	private static int predator_count = 0;
	private static ArrayList<Predator> predator_population = new ArrayList<Predator>();
	
	public Predator(Double stat_avg, Skin new_skin, int avg_life) {
		predator_count = predator_count + 1;
		id = predator_count;
		name = "Predator#" + id; 
		if (male_pop > female_pop) {
			gender = Gender.FEMALE;
		} 
		else if (female_pop > male_pop) {
			gender = Gender.MALE;
		}
		else {
			gender = random_gender();
		}
		
		if (gender == Gender.FEMALE) {
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
		predator_population.add(this);
		current_region.get_predator_population().add(this);
	}

	public Predator(Predator one, Predator two, int code) {
		predator_count = predator_count + 1;
		id = predator_count;
		name = "Predator#" + id; 
		if (code == 0) {
			gender = Gender.FEMALE;
		}
		else if (code == 1) {
			gender = Gender.MALE;
		}
		else {
			gender = random_gender();
		}
		if (gender == Gender.FEMALE) {
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
		predator_population.add(this);
		current_region.get_predator_population().add(this);
	}

	protected boolean hunt(Dragon d) {
		// Hunt a Dragon 
		//System.out.println(name + " is on the hunt.");
		double chance = 0; 
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
		chance += experience + speed + attack;
		
		for (Creature e: this.ancestors) {
			if(!e.isDead & e.get_stage() == Stage.ELDER) {
				chance+=2; 
			}
		}
		
		for(Virus v: infections) {
			chance = chance - 5; 
		}
		
		//int dragon_chance = d.be_hunted();
		int dragon_chance = 0; 
		if (chance > dragon_chance) {
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

	protected Predator reproduce(Predator two) {
			try {
				if (this.curr_season_count >= this.season_limit | two.curr_season_count >= two.season_limit) {
					return null;
				}
				else if (gender != two.gender & get_immediate_family(population).contains(two) == false & stage == Stage.ADULT & two.stage == Stage.ADULT) {
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
				System.out.println(e.getMessage());
				return null;
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
