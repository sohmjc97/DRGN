package application;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

public class Skin {

	public static enum COLORS {
		RED,
		PURPLE,
		BLUE,
		GREEN,
		YELLOW,
		ORANGE, 
		BLACK,
		WHITE,
		GRAY, 
		BROWN
	}
	
	public static enum SHADES {
		LIGHT,
		MEDIUM,
		DARK
	}
	
	public static enum PIGMENTS {
		PALE,
		MODERATE,
		INTENSE
	}
	
	private COLORS color;
	private SHADES shade;
	private PIGMENTS pigment; 
	
	private static HashMap<String, String> descriptions = new HashMap<String, String>();
	
	public Skin() {
		color = random_color();
		shade = random_shade();
		pigment = random_pigment(); 
		reconcile_skin();
	}
	
	public Skin(Skin skin, Skin skin2) {
		color = mix(skin.color, skin2.color);
		shade = mix(skin.shade, skin2.shade);
		pigment = mix(skin.pigment, skin2.pigment);
		reconcile_skin();
	}
	
	public Skin(COLORS color, SHADES shade, PIGMENTS pigment) {
		this.color = color;
		this.shade = shade;
		this.pigment = pigment; 
	}
	
	public String describe() {
		descriptions.put("INTENSE LIGHT WHITE", "SNOW WHITE");
		descriptions.put("INTENSE DARK BLACK", "JET BLACK");
		descriptions.put("INTENSE DARK BLUE", "ROYAL BLUE");
		descriptions.put("INTENSE DARK PURPLE", "DEEP PURPLE");
		descriptions.put("INTENSE DARK RED", "BLOOD RED");
		descriptions.put("INTENSE DARK GREEN", "EVERGREEN");
		descriptions.put("INTENSE DARK BROWN", "MAHOGANY");
		descriptions.put("INTENSE DARK GRAY", "STORM GRAY");
		descriptions.put("INTENSE DARK WHITE", "MOON SHADOW");
		descriptions.put("INTENSE DARK YELLOW", "GOLDEN");
		descriptions.put("INTENSE DARK ORANGE", "SUNSET ORANGE");
		String descrip;
		descrip = this.pigment + " " + this.shade + " " + this.color;
		if(!(descriptions.get(descrip) == null)) {
			descrip = descriptions.get(descrip) + "\n";
		}
		else if (shade.equals(SHADES.MEDIUM) & pigment.equals(PIGMENTS.MODERATE)) {
			descrip = this.color + "\n";
		}
		else if (pigment.equals(PIGMENTS.MODERATE) & !shade.equals(SHADES.MEDIUM)) {
			descrip = this.shade + " " + this.color + "\n";
		}
		else if (shade.equals(SHADES.MEDIUM) & !pigment.equals(PIGMENTS.MODERATE)) {
			descrip = this.pigment + " " + this.color + "\n";
		}
		else {
			descrip = this.pigment + " " + this.shade + " " + this.color + "\n";
		}
		return descrip;
	}
	
	private void reconcile_skin() {
		
		//if light and black, turn gray
		//if dark and white turn gray
		
		if (shade.equals(SHADES.LIGHT) & color.equals(COLORS.BLACK)) {
			color = COLORS.GRAY;
			shade = SHADES.DARK;
		}
		else if (shade.equals(SHADES.DARK) & color.equals(COLORS.WHITE)) {
			color = COLORS.GRAY;
			shade = SHADES.LIGHT;
		}
		
	}
	
	public static Skin collective_mix(ArrayList<Creature> pop) {
		
		COLORS mcc = COLORS.WHITE;
		SHADES mcs = SHADES.LIGHT;
		PIGMENTS mcp = PIGMENTS.PALE;
		
		ArrayList<Skin> skins = new ArrayList<Skin>();
		for (Creature e: pop) {
			skins.add(e.get_skin());
		}
		HashMap<COLORS, Integer> color_tracker = new HashMap<COLORS, Integer>();
		HashMap<SHADES, Integer> shade_tracker = new HashMap<SHADES, Integer>();
		HashMap<PIGMENTS, Integer> pigment_tracker = new HashMap<PIGMENTS, Integer>();
		
		for (COLORS c: COLORS.values()) {
			color_tracker.put(c, 0);
		}
		for(SHADES s: SHADES.values()) {
			shade_tracker.put(s, 0);
		}
		for(PIGMENTS p: PIGMENTS.values()) {
			pigment_tracker.put(p, 0);
		}
		
		for (Skin skin: skins) {
			color_tracker.put(skin.get_color(), color_tracker.get(skin.get_color()) + 1);
			shade_tracker.put(skin.get_shade(), shade_tracker.get(skin.get_shade()) + 1);
			pigment_tracker.put(skin.get_pigment(), pigment_tracker.get(skin.get_pigment()) + 1);
		}
		
		
		for (COLORS c: color_tracker.keySet()) {
			if (color_tracker.get(c) > color_tracker.get(mcc)) {
				mcc = c; 
			}
		}
		
		for(SHADES s: shade_tracker.keySet()) {
			if(shade_tracker.get(s) > shade_tracker.get(mcs)) {
				mcs = s;
			}
		}
		
		for (PIGMENTS p: pigment_tracker.keySet()) {
			if (pigment_tracker.get(p) > pigment_tracker.get(mcp)) {
				mcp = p; 
			}
		}
		
		//look at each dragon and get its skin
		//average or mix all Skins or figure out most common skin
		//return that Skin 
		
		return new Skin(mcc, mcs, mcp); 
	}
	
	private static COLORS random_color() {
		Random rand = new Random();
		int index = rand.nextInt(COLORS.values().length);
		int count = 0;
		for (COLORS i:COLORS.values()) {
			if (index == count) {
				return i;
			}
			count++;
		}
		return COLORS.BLACK;
	}
	
	private static SHADES random_shade() {
		Random rand = new Random();
		int index = rand.nextInt(SHADES.values().length);
		int count = 0;
		for (SHADES i:SHADES.values()) {
			if (index == count) {
				return i;
			}
			count++;
		}
		return SHADES.DARK;
	}
	
	private static PIGMENTS random_pigment() {
		Random rand = new Random();
		int index = rand.nextInt(PIGMENTS.values().length);
		int count = 0;
		for (PIGMENTS i:PIGMENTS.values()) {
			if (index == count) {
				return i;
			}
			count++;
		}
		return PIGMENTS.INTENSE;
	}

	public static COLORS mix (COLORS one, COLORS two) {
		ArrayList<COLORS> colors = new ArrayList<COLORS>();
		colors.add(one);
		colors.add(two);
		
		if(one == COLORS.BROWN & two == COLORS.BROWN) {
			Random seed = new Random();
			int chance = seed.nextInt(100) + 1;
			int index = seed.nextInt(COLORS.values().length);
			ArrayList<COLORS> all = new ArrayList<COLORS>();
			for (COLORS c: COLORS.values()) {
				all.add(c);
			}
			if (chance < 60) {
				return COLORS.BROWN;
			}
			else {
				return all.get(index);
			}
		}
		else if (one == two) {
			return one;
		}
		else if (colors.contains(COLORS.BLACK) & colors.contains(COLORS.WHITE)) {
			return COLORS.GRAY;
		}
		else if (colors.contains(COLORS.BLACK)) {
			Random rand = new Random(); 
			int a = rand.nextInt(10);
			colors.remove(COLORS.BLACK);
			if (a > 2) {
				return COLORS.BLACK;
			}
			else {
				return colors.get(0);
			}
		}
		else if (colors.contains(COLORS.WHITE)) {
			Random rand = new Random(); 
			int a = rand.nextInt(10);
			colors.remove(COLORS.WHITE);
			if (a <= 2) {
				return COLORS.WHITE;
			}
			else {
				return colors.get(0);
			}
		}
		else if (colors.contains(COLORS.GRAY)) {
			Random rand = new Random(); 
			int a = rand.nextInt(2);
			return colors.get(a);
		}
		else if (colors.contains(COLORS.RED) & colors.contains(COLORS.BLUE)) {
			Random rand = new Random();
			int a = rand.nextInt(12) + 1;
			if (a <= 3 ) {
				return COLORS.RED;
			}
			else if (a <= 9) {
				return COLORS.PURPLE;
			}
			else {
				return COLORS.BLUE;
			}
		}
		else if (colors.contains(COLORS.BLUE) & colors.contains(COLORS.YELLOW)) {
			Random rand = new Random();
			int a = rand.nextInt(12) + 1;
			if (a <= 3 ) {
				return COLORS.YELLOW;
			}
			else if (a <= 9) {
				return COLORS.GREEN;
			}
			else {
				return COLORS.BLUE;
			}
		}
		else if (colors.contains(COLORS.RED) & colors.contains(COLORS.YELLOW)) {
			Random rand = new Random();
			int a = rand.nextInt(12) + 1;
			if (a <= 3 ) {
				return COLORS.RED;
			}
			else if (a <= 9) {
				return COLORS.ORANGE;
			}
			else {
				return COLORS.YELLOW;
			}
		}
		else if (colors.contains(COLORS.PURPLE) & (colors.contains(COLORS.BLUE) | colors.contains(COLORS.RED))) {
			Random rand = new Random();
			int a = rand.nextInt(3) + 1;
			if (a == 1) {
				return COLORS.PURPLE;
			}
			else if (a ==2) {
				return COLORS.RED;
			}
			else {
				return COLORS.BLUE;
			}
		}
		else if (colors.contains(COLORS.ORANGE) & (colors.contains(COLORS.YELLOW) | colors.contains(COLORS.RED))) {
			Random rand = new Random();
			int a = rand.nextInt(3) + 1;
			if (a == 1) {
				return COLORS.ORANGE;
			}
			else if (a ==2) {
				return COLORS.RED;
			}
			else {
				return COLORS.YELLOW;
			}
		}
		else if (colors.contains(COLORS.GREEN) & (colors.contains(COLORS.YELLOW) | colors.contains(COLORS.BLUE))) {
			Random rand = new Random();
			int a = rand.nextInt(3) + 1;
			if (a == 1) {
				return COLORS.GREEN;
			}
			else if (a ==2) {
				return COLORS.BLUE;
			}
			else {
				return COLORS.YELLOW;
			}
		}
		else {
			Random rand = new Random();
			int a = rand.nextInt(10) + 1;
			if (a < 3) {
				return colors.get(0);
			}
			else if (a > 8) {
				return colors.get(1);
			}
			else {
				return COLORS.BROWN;
			}
		}
	}
	
	public static SHADES mix (SHADES one, SHADES two) {
		
		ArrayList<SHADES> shades = new ArrayList<SHADES>();
		shades.add(one);
		shades.add(two);
		
		if (one == two) {
			return one;
		}
		else if (shades.contains(SHADES.LIGHT) & shades.contains(SHADES.MEDIUM)) {
			Random rand = new Random(); 
			int a = rand.nextInt(10);
			if (a <= 2) {
				return SHADES.LIGHT;
			}
			else if (a > 8) {
				return SHADES.DARK;
			}
			else {
				return SHADES.MEDIUM;
			}
		}
		else if (shades.contains(SHADES.DARK) & shades.contains(SHADES.LIGHT)) {
			Random rand = new Random(); 
			int a = rand.nextInt(10);
			if (a < 2) {
				return SHADES.LIGHT;
			}
			else if (a > 7){
				return SHADES.DARK;
			}
			else {
				return SHADES.MEDIUM;
			}
		}
		else {
			Random rand = new Random(); 
			int a = rand.nextInt(10);
			if (a <= 1) {
				return SHADES.MEDIUM;
			}
			else if (a > 9) {
				return SHADES.LIGHT;
			}
			else {
				return SHADES.DARK;
			}
		}
		
	}
	
	public static PIGMENTS mix(PIGMENTS one, PIGMENTS two) {
		ArrayList<PIGMENTS> pigments = new ArrayList<PIGMENTS>();
		pigments.add(one);
		pigments.add(two);
		
		if (one == two) {
			return one;
		}
		else if (pigments.contains(PIGMENTS.PALE) & pigments.contains(PIGMENTS.MODERATE)) {
			Random rand = new Random(); 
			int a = rand.nextInt(10);
			if (a <= 1) {
				return PIGMENTS.PALE;
			}
			if (a > 8) {
				return PIGMENTS.INTENSE;
			}
			else {
				return PIGMENTS.MODERATE;
			}
		}
		else if (pigments.contains(PIGMENTS.INTENSE) & pigments.contains(PIGMENTS.PALE)) {
			Random rand = new Random(); 
			int a = rand.nextInt(10);
			if (a < 1) {
				return PIGMENTS.PALE;
			}
			else if (a > 7){
				return PIGMENTS.INTENSE;
			}
			else {
				return PIGMENTS.MODERATE;
			}
		}
		else {
			Random rand = new Random(); 
			int a = rand.nextInt(10);
			if (a <= 1) {
				return PIGMENTS.MODERATE;
			}
			if (a > 8) {
				return PIGMENTS.PALE;
			}
			else {
				return PIGMENTS.INTENSE;
			}
		}
	}
	
	public int get_color_distance(COLORS two_color) {
		/*
		 * 0 - they are the same color
		 * 1 - mix one new color to first to get the other / close colors
		 * 2 - mix multiple new colors to first to get second / far but potential
		 * 3 - no way to get one color from another / opposite colors
		 */
		ArrayList<COLORS> primary = new ArrayList<COLORS>();
		primary.add(COLORS.RED);
		primary.add(COLORS.BLUE);
		primary.add(COLORS.YELLOW);
		ArrayList<COLORS> secondary = new ArrayList<COLORS>();
		secondary.add(COLORS.PURPLE);
		secondary.add(COLORS.GREEN);
		secondary.add(COLORS.ORANGE);
			
		ArrayList<COLORS> colors = new ArrayList<COLORS>();
		colors.add(this.color);
		colors.add(two_color);
		
		if (this.color == two_color) {
			return 0; 
		}
		else if (colors.contains(COLORS.BLACK) & colors.contains(COLORS.WHITE)) {
			return 3;
		}
		else if (
				(colors.contains(COLORS.BLACK) & colors.contains(COLORS.GRAY) )
												|
				(colors.contains(COLORS.WHITE) & colors.contains(COLORS.GRAY))
				) {
			return 1;
		}
		else if (colors.contains(COLORS.WHITE)) {
			return 3; 
		}
		else if (colors.contains(COLORS.GRAY) | colors.contains(COLORS.BLACK)) {
			return 2; 
		}
		else if (primary.contains(this.color) & primary.contains(two_color)) {
			return 3; 
		}
		else if (secondary.contains(this.color) & secondary.contains(two_color)) {
			return 3; 
		}
		else if (colors.contains(COLORS.BROWN) & (secondary.contains(colors.get(0)) | secondary.contains(colors.get(1)))) {
			return 2; 
		}
		else if ((colors.contains(COLORS.BROWN) & (primary.contains(colors.get(0)) | primary.contains(colors.get(1))))) {
			return 2; 
		}
		else if (
				(colors.contains(COLORS.GREEN) & colors.contains(COLORS.BLUE))
				|
				(colors.contains(COLORS.GREEN) & colors.contains(COLORS.YELLOW))) {
			return 1; 
		}
		else if (
				(colors.contains(COLORS.PURPLE) & colors.contains(COLORS.BLUE))
				|
				(colors.contains(COLORS.PURPLE) & colors.contains(COLORS.RED))) {
			return 1; 
		}
		else if (
				(colors.contains(COLORS.ORANGE) & colors.contains(COLORS.RED))
				|
				(colors.contains(COLORS.ORANGE) & colors.contains(COLORS.YELLOW))) {
			return 1; 
		}
		return 0; 
	}
	
	public COLORS get_color() {
		return color; 
	}
	
	public int get_shade_distance(SHADES two_shade) {
		
		ArrayList<SHADES> shades = new ArrayList<SHADES>();
		shades.add(this.shade);
		shades.add(two_shade);
		
		if(this.shade == two_shade) {
			return 0;
		}
		else if (shades.contains(SHADES.DARK) & shades.contains(SHADES.LIGHT)) {
			return 2; 
		}
		else {
			return 1; 
		}
		
	}
	
	public SHADES get_shade() {
		return shade;
	}
	
	public int get_pigment_distance(PIGMENTS two_pigment) {
		
		ArrayList<PIGMENTS> pigments = new ArrayList<PIGMENTS>();
		pigments.add(this.pigment);
		pigments.add(two_pigment);
		
		if(this.pigment== two_pigment) {
			return 0;
		}
		else if (pigments.contains(PIGMENTS.INTENSE) & pigments.contains(PIGMENTS.PALE)) {
			return 2; 
		}
		else {
			return 1; 
		}
		
	}
	
	public PIGMENTS get_pigment() {
		return pigment; 
	}
	
}
