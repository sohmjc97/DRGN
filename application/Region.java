package application;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

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
	
	private ArrayList<Prey> prey_population = new ArrayList<Prey>(); 
	private ArrayList<Dragon> dragon_population = new ArrayList<Dragon>(); 
	private ArrayList<Predator> predator_population = new ArrayList<Predator>();
	
	
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
	}
	
	public Skin.COLORS get_env_color() {
		return env_color;
	}
	
	public void set_env_color(COLORS color) {
		env_color = color;
	}
	
	public Skin.SHADES get_env_shade() {
		return env_shade;
	}
	
	public void set_env_shade(SHADES shade) {
		env_shade = shade;
	}
	
	public Skin.PIGMENTS get_env_pigment() {
		return env_pigment; 
	}
	
	public void set_env_pigment(PIGMENTS pigment) {
		env_pigment = pigment; 
	}
	
	public ArrayList<Prey> get_prey_population() {
		return prey_population;
	}
	
	public ArrayList<Dragon> get_dragon_population() {
		return dragon_population;
	}
	
	public ArrayList<Predator> get_predator_population() {
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
		
	}
	
	//CLASS FUNCTIONS 
	
	static ArrayList<Region> get_regions() {
		return regions; 
	}
	
}
