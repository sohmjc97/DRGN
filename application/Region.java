package application;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

import application.Creature.Gender;
import application.Skin.COLORS;
import application.Skin.PIGMENTS;
import application.Skin.SHADES;

public class Region {

	private static ArrayList<Region> regions = new ArrayList<Region>();
	private static int region_count = 0;
	private static HashMap<Resources, Integer> resource_levels = new HashMap<Resources, Integer>(); 
	
	static {
		for (Resources resource: Resources.values()) {
			resource_levels.put(resource, 1000);
		}
	}
	
	public static enum Resources {
		PREY_FOOD,
		WATER,
		SPACE
	}
	
	private final int id;
	private final String name;
	private ArrayList<Region> connecting_regions = new ArrayList<Region>();
	private Skin environment_skin;
	private COLORS env_color;
	private SHADES env_shade;
	private PIGMENTS env_pigment; 
	
	private ArrayList<Creature> prey_population = new ArrayList<Creature>(); 
	private ArrayList<Creature> dragon_population = new ArrayList<Creature>(); 
	private ArrayList<Creature> predator_population = new ArrayList<Creature>();
	
	
	public Region(String name) {
		regions.add(this);
		region_count++;
		id = region_count;
		this.name = name; 
	}
	
	//MEMBER FUNCTIONS
	
	public int get_id() {
		return id; 
	}
	
	public String get_name() {
		return name; 
	}
	
	public Skin get_environment_skin() {
		return environment_skin;
	}
	
	public void set_environment_skin(Skin skin) {
		environment_skin = skin; 
		this.env_color = skin.get_color();
		this.env_shade = skin.get_shade(); 
		this.env_pigment = skin.get_pigment(); 
	}
	
	public void set_environment_skin(COLORS color, SHADES shade, PIGMENTS pigment) {
		environment_skin = new Skin(color, shade, pigment); 
		this.env_color = color;
		this.env_shade = shade; 
		this.env_pigment = pigment; 
	}
	
	public Skin.COLORS get_env_color() {
		return env_color;
	}
	
	public Skin.SHADES get_env_shade() {
		return env_shade;
	}
	
	public Skin.PIGMENTS get_env_pigment() {
		return env_pigment; 
	}
	
	public ArrayList<Creature> get_prey_population() {
		return prey_population;
	}
	
	public ArrayList<Creature> get_dragon_population() {
		return dragon_population;
	}
	
	public ArrayList<Creature> get_predator_population() {
		return predator_population; 
	}
	
	public Integer get_resource_levels(Resources resource) {
		return resource_levels.get(resource);
	}
	
	public void deplete_resource(Resources resource, int taken) {
		resource_levels.put(resource, resource_levels.get(resource) - taken);
	}
	
	public void replenish_resource(Resources resource, int gained) {
		resource_levels.put(resource, resource_levels.get(resource) + gained);
	}
	
	public ArrayList<Creature> get_kin_population(Creature creature) {
		if(creature.my_class == Prey.class) {
			return prey_population;
		}
		else if (creature.my_class == Dragon.class) {
			return dragon_population;
		}
		else if (creature.my_class == Predator.class) {
			return predator_population;
		}
		else {
			return new ArrayList<Creature>(); 
		}
	}
	
	public ArrayList<Region> get_connecting_regions() {
		return connecting_regions; 
	}
	
	public boolean connect_region(Region adj) {
		if(connecting_regions.add(adj)) {
			return true;
		}
		else {
			return false; 
		}
	}
	
	public String disease_report(ArrayList<Virus> diseases) {
		
		//System.out.println("Reporting");
		String report = "DISEASE REPORT\n";
		report += "Recurrent Infections: ";
		for (Virus i: Virus.get_recurrent_infections()) {
			report += "#" + i.get_id() + ", ";
		}
		report += "\n";
		ArrayList<Virus> eradicated = new ArrayList<Virus>();
		int count = 0;
		for (Virus e: diseases) {
			//System.out.println("Checking " + e.get_id());
			if (e.get_current_infected() == 0) {
				//System.out.println(e.get_id() + " has died out.");
				report = report + e.get_id() + " is inactive. \n";
				eradicated.add(e);
			}
			else {
				//System.out.println("Adding " + e.get_id() + " to report.");
				report = report + e.get_stats();
				count++;
			}
		}
		//System.out.println("Exit disease loop.");
		for (Virus e: eradicated) {
			diseases.remove(e);
		}
		eradicated.clear();
		//System.out.println("End Report");
		report = report + count + " active viruses.\n";
		return report; 
		
	}
	
	public static String global_disease_report(ArrayList<Virus> diseases) {
		
		//System.out.println("Reporting");
		String report = "GLOBAL DISEASE REPORT\n";
		report += "Recurrent Infections: ";
		for (Virus i: Virus.get_recurrent_infections()) {
			report += "#" + i.get_id() + ", ";
		}
		report += "\n";
		ArrayList<Virus> eradicated = new ArrayList<Virus>();
		int count = 0;
		for (Virus e: diseases) {
			//System.out.println("Checking " + e.get_id());
			if (e.get_current_infected() == 0) {
				//System.out.println(e.get_id() + " has died out.");
				report = report + e.get_id() + " is inactive. \n";
				eradicated.add(e);
			}
			else {
				//System.out.println("Adding " + e.get_id() + " to report.");
				report = report + e.get_stats();
				count++;
			}
		}
		//System.out.println("Exit disease loop.");
		for (Virus e: eradicated) {
			diseases.remove(e);
		}
		eradicated.clear();
		//System.out.println("End Report");
		report = report + count + " active viruses.\n";
		return report; 
		
	}
	
	public void randomize_env() {
		
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
		
		this.environment_skin = new Skin(env_color, env_shade, env_pigment);
		
	}
	
	public Prey spawn_prey() {
		Prey baby = null;
		Skin collective = Skin.collective_mix(prey_population);
		Random rand = new Random();
		int chance = rand.nextInt(10);
		if (chance > 4) {
			baby = new Prey(Prey.get_avg_stat_coeff(prey_population), collective, Prey.get_avg_life_span(prey_population), this);
		}
		else {
			baby = new Prey(Prey.get_avg_stat_coeff(prey_population), new Skin(), Prey.get_avg_life_span(prey_population), this);
		}
		return baby;
	}
	
	public Dragon spawn_dragon() {
		
		Dragon baby = null;
		Skin collective = Skin.collective_mix(dragon_population);
		Random rand = new Random();
		int chance = rand.nextInt(10);
		if (chance > 4) {
			baby = new Dragon(Dragon.get_avg_stat_coeff(dragon_population), collective,  Dragon.get_avg_life_span(dragon_population), this);
		}
		else {
			baby = new Dragon(Dragon.get_avg_stat_coeff(dragon_population), new Skin(),  Dragon.get_avg_life_span(dragon_population), this);
		}
		Name.rename(baby);
		return baby;
		
	}
	
	public Predator spawn_predator() {
		Predator baby = null;
		Skin collective = Skin.collective_mix(predator_population);
		Random rand = new Random();
		int chance = rand.nextInt(10);
		if (chance > 4) {
			baby = new Predator(Creature.get_avg_stat_coeff(predator_population), collective,  Creature.get_avg_life_span(predator_population), this);
		}
		else {
			baby = new Predator(Creature.get_avg_stat_coeff(predator_population), new Skin(),  Creature.get_avg_life_span(predator_population), this);
		}
		return baby;
	}
	
	public void short_sim() {
		//System.out.println("Beginning Simulation...");
		ArrayList<Creature> pr = new ArrayList<Creature>(prey_population);
		//System.out.println("Beginning Prey Simulation...");
		ArrayList<Boolean> finished = new ArrayList<Boolean>();
		ArrayList<Creature> preds = new ArrayList<Creature>(predator_population);
		ArrayList<Creature> drags = new ArrayList<Creature>(dragon_population);
		System.out.println("Starting Foliage: " + this.get_resource_levels(Resources.PREY_FOOD));
		do {
			finished.clear();
			for (Creature p: pr) {
				if (!p.hasGrown & !p.isDead) {
					finished.add(p.run_once());
				}
			}
			for(Creature d: drags) {
				if(!d.isDead & !d.hasGrown) {
					finished.add(d.run_once());
				}
			}
			for(Creature f: preds) {
				if(!f.isDead & !f.hasGrown) {
					//System.out.println("Another day for ..." + f.get_name());
					finished.add(f.run_once());
				} 
				else {
					//System.out.println(f.get_name() + " already completed cycle.");
				}
			}
		} while(!finished.isEmpty());
		int num = Prey.season(prey_population);
		if(num == 0 & this.get_resource_levels(Resources.PREY_FOOD) > 200) {
			spawn_prey(); 
			while (num < 12 & this.get_resource_levels(Resources.PREY_FOOD) > 800) {
				Prey new_prey = spawn_prey(); 
				new_prey.age_randomly();
				num++;
			}
		}
		else if (this.get_resource_levels(Resources.PREY_FOOD) > 200) {
			spawn_prey(); 
			spawn_prey(); 
			spawn_prey(); 
			spawn_prey();
		}
		//System.out.println("Beginning Dragon Simulation...");
		num = Dragon.season(dragon_population);
		if(num == 0) {
			//spawn_dragon(); 
			while (num < 2 & prey_population.size() > dragon_population.size()*2) {
				Dragon d = spawn_dragon();
				//d.age_randomly();
				num++;
			}
		}
		else {
			spawn_dragon(); 
			//spawn_dragon(); 
		}
		//System.out.println("Beginning Predator Simulation...");
		num = Predator.season(predator_population);
		if(num == 0) {
			while (num < 2 & (dragon_population.size() > predator_population.size()*4 | dragon_population.size() > prey_population.size())) {
				Predator p = spawn_predator(); 
				//p.age_randomly();
				num++;
			}
		}
		else {
			spawn_predator(); 
			//spawn_predator(); 
		}
		this.replenish_resource(Resources.PREY_FOOD, 500);
	}
	
	public void long_sim(int count) {
		int x = 0;
		while (x < count) {
			short_sim();
			x++; 
		}
	}
	
	public int gender_check(ArrayList<Creature> pop) {
		int males = 0;
		int females = 0;
		for(Creature d: pop) {
			if(d.isDead) {
				//don't count
			}
			else if (d.gender == Gender.FEMALE) {
				females++;
			}
			else if (d.gender == Gender.MALE) {
				males++;
			}
		}
		
		if(males > females) {
			return 1;
		}
		else if (females > males) {
			return 2;
		}
		else {
			return 0; 
		}
		
	}
	
	//CLASS FUNCTIONS 
	
	static ArrayList<Region> get_regions() {
		return regions; 
	}
	
}
