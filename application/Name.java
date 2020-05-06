package application;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

public class Name {

	public enum FirstNames {
		Bright,
		Dark,
		Dead,
		Silent,
		Wild,
		Swift,
		Lost, 
		Ragged,
		Dancing,
		Fallen,
		Rising,
		Winter,
		Summer,
		Fleet,
		Clear,
		Burning,
		Frozen
	}
	
	public enum LastNames {
		Step,
		Claw,
		Swoop,
		Fang,
		Talon,
		Wing,
		Tail,
		Horn,
		Scale,
		Roar,
		Breath,
		Song,
		Flight,
		Heart,
		Shine,
		Strike,
		Eye,
		Scar,
		Pool
	}
	
	public enum VersNames {
		Sky,
		Wind,
		Breeze,
		Cloud,
		Storm,
		Sea,
		Stone,
		Dawn,
		Ocean,
		Mist,
		Rain,
		Thunder,
		Lightning,
		Fire,
		Ridge,
		Moon,
		Shadow,
		River,
		Hail,
		Snow,
		Ice,
		Frost,
		Sleet,
		Light,
		Lake,
		Sun,
		Star,
		Night,
		Ripple
	}
	
	static HashMap<String, String> incompatible_names = new HashMap<String, String>(); 
	
	static {
		incompatible_names.put("Striking", "Strike");
		incompatible_names.put("Dancing", "Dance");
		incompatible_names.put("Rising", "Rise");
		incompatible_names.put("Falling", "Fall");
		incompatible_names.put("Scorching", "Scorch");
		incompatible_names.put("Fire", "Flame");
		incompatible_names.put("Dawn", "Dusk");
		incompatible_names.put("Sun", "Moon");
		incompatible_names.put("Dark", "Light");
		incompatible_names.put("Black", "Light");
		incompatible_names.put("Fleet", "Flight");
		incompatible_names.put("Thunder", "Lightning");
	}
	
	static ArrayList<String> active_names = new ArrayList<String>();
	static ArrayList<String> retired_names = new ArrayList<String>();
	
	public static void rename(Dragon d) {
		String color = d.get_skin().get_color().toString().toLowerCase();
		color = color.substring(0, 1).toUpperCase() + color.substring(1);
		
		String color_name = color;
		ArrayList<String> color_names_first = first_name_by_color(color_name);
		ArrayList<String> color_names_last = last_name_by_color(color_name);
		ArrayList<String> shade_name_first = first_name_by_shade(d.get_skin().get_shade().toString());
		ArrayList<String> shade_name_last = last_name_by_shade(d.get_skin().get_shade().toString());
		ArrayList<String> pigm_name_first = first_name_by_pigment(d.get_skin().get_pigment().toString());
		
		boolean found = false;
		Random rand = new Random(); 
		while(!found) {
			
			int num1 = rand.nextInt(VersNames.values().length);
			
			String vers_name = VersNames.values()[num1].toString();
			
			int num2 = rand.nextInt(FirstNames.values().length);
			
			String first_name = FirstNames.values()[num2].toString();
			
			int num3 = rand.nextInt(LastNames.values().length);
			
			int num4 = rand.nextInt(color_names_first.size());
			
			color_name = color_names_first.get(num4);
			
			String last_name = LastNames.values()[num3].toString();

			ArrayList<String> orig_names = find_best_original_names(d);
			
			int num5 = rand.nextInt(orig_names.size());
			
			String orig_name = orig_names.get(num5);
			
			if (!active_names.contains(color_name + last_name)) {
				found = true;
				active_names.add(color_name+last_name);
				d.rename(color_name, last_name);
			}
			else if (!active_names.contains(color_name + vers_name)) {
				found = true;
				active_names.add(color_name+vers_name);
				d.rename(color_name, vers_name);
			}
			else if (!active_names.contains(orig_name)) {
				found = true;
				active_names.add(orig_name.replace("0", ""));
				d.rename(orig_name.split("0")[0], orig_name.split("0")[1]);
			}
			else if (!active_names.contains(first_name + last_name)) {
				found = true;
				active_names.add(first_name+last_name);
				d.rename(first_name, last_name);
			}
			else if (!active_names.contains(first_name + vers_name)) {
				found = true;
				active_names.add(first_name+vers_name);
				d.rename(first_name, vers_name);
			}
			else if (!active_names.contains(vers_name + last_name)) {
				found = true;
				active_names.add(vers_name+last_name);
				d.rename(vers_name, last_name);
			}
			else {
				continue; 
			}
			
			if(!isValidName(d.get_first_name(), d.get_last_name())) {
				found = false;
				continue; 
			}
			else {
				break; 
			}
			
			
		}
		
	}
	
	private static boolean isValidName(String first, String last) {
		
		boolean compatible = true;
		boolean incompatible = false;
		if (first.equals(last) | last.equals(first)) {
			return incompatible;
		}
		else if (retired_names.contains(first+last)) {
			return incompatible; 
		}
		else if(incompatible_names.get(first) != null) {
			if(incompatible_names.get(first).equals(last)) {
				return incompatible;
			}
		}
		else if(incompatible_names.get(last) != null) {
			if(incompatible_names.get(last).equals(first)) {
				return incompatible;
			}
		}
		return compatible; 
	}
	
	public static void rename(Dragon one, Dragon two, Dragon three) {
		String color = three.get_skin().get_color().toString().toLowerCase();
		color = color.substring(0, 1).toUpperCase() + color.substring(1);
		
		String color_name = color;
		
		ArrayList<String> color_names = first_name_by_color(color_name);
		ArrayList<String> last_color_names = last_name_by_color(color_name);
		
		boolean found = false;
		Random rand = new Random(); 
		boolean tried1 = false;
		boolean tried2 = false;
		boolean tried3 = false;
		boolean tried4 = false;
		boolean tried5 = false;
		boolean tried6 = false;
		boolean tried7 = false;
		boolean tried8 = false;
		boolean tried9 = false;
		while(!found) {
			String one_first = one.get_first_name();
			String one_last = one.get_last_name();
			String two_first = two.get_first_name();
			String two_last = two.get_last_name();
			
			int num1 = rand.nextInt(VersNames.values().length);
			
			String vers_name = VersNames.values()[num1].toString();
			
			int num2 = rand.nextInt(FirstNames.values().length);
			
			String first_name = FirstNames.values()[num2].toString();
			
			int num3 = rand.nextInt(LastNames.values().length);
			
			String last_name = LastNames.values()[num3].toString();
			
			int num4 = rand.nextInt(color_names.size());
			
			color_name = color_names.get(num4);
			
			ArrayList<String> orig_names = find_best_original_names(three);
			
			int num5 = rand.nextInt(orig_names.size());
			
			String orig_name = orig_names.get(num5);
			
			int num6 = rand.nextInt(last_color_names.size());
			
			String last_color_name = last_color_names.get(num6);
			
			if(!active_names.contains(one_first + two_last) & !tried1) {
				found = true;
				tried1 = true; 
				active_names.add(one_first+two_last);
				three.rename(one_first, two_last);
			}
			else if(!active_names.contains(two_first + one_last) & !tried2) {
				found = true;
				tried2 = true;
				active_names.add(two_first + one_last);
				three.rename(two_first, one_last);
			}
			else if(!active_names.contains(one_last + two_last) & isVers(one_last) & !tried3) {
				tried3 = true;
				if(!active_names.contains(one_last + two_last)) {
					found = true;
					active_names.add(one_last+two_last);
					three.rename(one_last, two_last);
				}
			}
			else if(!active_names.contains(two_last + one_last) &  isVers(two_last) & !tried4) {
				tried4 = true;
				if(!active_names.contains(two_last + one_last)) {
					found = true;
					active_names.add(two_last + one_last);
					three.rename(two_last, one_last);
				}
			}
			else if(!active_names.contains(one_first + two_first) & isVers(two_first) & !tried5) {
				tried5 = true;
				if(!active_names.contains(one_first + two_first)) {
					found = true;
					active_names.add(one_first + two_first);
					three.rename(one_first, two_first);
				}
			}
			else if(!active_names.contains(two_first + one_first) & isVers(one_first) & !tried6) {
				tried6 = true;
				if(!active_names.contains(two_first + one_first)) {
					found = true;
					active_names.add(two_first + one_first);
					three.rename(two_first, one_first);
				}
			}
			else if (!active_names.contains(color_name + two_last)) {
				found = true;
				active_names.add(color_name + two_last);
				three.rename(color_name, two_last);
			}
			else if(!active_names.contains(color_name + one_last)) {
				found = true;
				active_names.add(color_name + one_last);
				three.rename(color_name, one_last);
			}
			else if (!active_names.contains(two_first + last_color_name)) {
				found = true;
				active_names.add(two_first + last_color_name);
				three.rename(two_first, last_color_name);
			}
			else if(!active_names.contains(one_first + last_color_name)) {
				found = true;
				active_names.add(one_first + last_color_name);
				three.rename(one_first, last_color_name);
			}
			else if (!active_names.contains(one_first + last_name)) {
				found = true;
				active_names.add(one_first + last_name);
				three.rename(one_first, last_name);
			}
			else if (!active_names.contains(two_first + last_name)) {
				found = true;
				active_names.add(two_first + last_name);
				three.rename(two_first, last_name);
			}
			else if(!active_names.contains(one_first + vers_name)) {
				found = true;
				active_names.add(one_first + vers_name);
				three.rename(one_first, vers_name);
			}
			else if(!active_names.contains(two_first + vers_name)) {
				found = true;
				active_names.add(two_first + vers_name);
				three.rename(two_first, vers_name);
			}
			else if (!active_names.contains(vers_name + one_last)) {
				found = true;
				active_names.add(vers_name + one_last);
				three.rename(vers_name, one_last);
			} 
			else if (!active_names.contains(vers_name + two_last)) {
				found = true;
				active_names.add(vers_name + two_last);
				three.rename(vers_name, two_last);
			}
			else if (!active_names.contains(orig_name)) {
				found = true;
				active_names.add(orig_name.replace("0", ""));
				three.rename(orig_name.split("0")[0], orig_name.split("0")[1]);
			}
			else {
				continue;
			}
			
			if(!isValidName(three.get_first_name(), three.get_last_name())) {
				found = false;
				continue; 
			}
			else {
				break;
			}
			
		}
	}
	
	private static boolean isVers(String name) {
		for(VersNames n: VersNames.values()) {
			if(n.toString().equals(name)) {
				return true;
			}
		}
		return false; 
	}
	
	public static boolean isActiveName(String name) {
		return active_names.contains(name);
	}
	
	public static boolean removeActiveName(String name) {
		return active_names.remove(name);
	}
	
	public static boolean addActiveName(String name) {
		if(active_names.contains(name)) {
			return false;
		}
		else {
			active_names.add(name);
			return true;
		}
	}
	
	public static void retire_name(String name) {
		if(active_names.contains(name)) {
			active_names.remove(name);
		}
		retired_names.add(name);
	}
	
	private static ArrayList<String> find_best_original_names(Dragon d) {
		
		ArrayList<String> best_names = new ArrayList<String>(); 
		
		String color = d.get_skin().get_color().toString().toLowerCase();
		color = color.substring(0, 1).toUpperCase() + color.substring(1);
		
		String color_name = color;
		ArrayList<String> color_names_first = first_name_by_color(color_name);
		ArrayList<String> color_names_last = last_name_by_color(color_name);
		ArrayList<String> shade_name_first = first_name_by_shade(d.get_skin().get_shade().toString());
		ArrayList<String> shade_name_last = last_name_by_shade(d.get_skin().get_shade().toString());
		ArrayList<String> pigm_name_first = first_name_by_pigment(d.get_skin().get_pigment().toString());
		
		int stat_code = d.find_best_stat_for_name();
		
		ArrayList<String> stat_name_first = first_name_by_stat(stat_code);
		ArrayList<String> stat_name_last = last_name_by_stat(stat_code);
		
		ArrayList<String> color_shade = combine_into_full_names(color_names_first, shade_name_last);
		ArrayList<String> shade_color = combine_into_full_names(shade_name_first, color_names_last);
		ArrayList<String> pigm_color = combine_into_full_names(pigm_name_first, color_names_last);
		ArrayList<String> stat_color = combine_into_full_names(stat_name_first, color_names_last);
		ArrayList<String> color_stat = combine_into_full_names(color_names_first, stat_name_last);
		ArrayList<String> shade_stat = combine_into_full_names(shade_name_first, stat_name_last);
		ArrayList<String> pigm_stat = combine_into_full_names(pigm_name_first, stat_name_last);
		ArrayList<String> stat_stat = combine_into_full_names(stat_name_first, stat_name_last);
		ArrayList<String> stat_shade = combine_into_full_names(stat_name_first, shade_name_last);
		
		best_names.addAll(color_shade);
		best_names.addAll(shade_color);
		best_names.addAll(pigm_color);
		best_names.addAll(stat_color);
		best_names.addAll(color_stat);
		best_names.addAll(shade_stat);
		best_names.addAll(stat_stat);
		best_names.addAll(pigm_stat);
		best_names.addAll(stat_shade);
		
		return best_names; 
		
	}
	
	private static ArrayList<String> combine_into_full_names(ArrayList<String> first_names, ArrayList<String> last_names) {
		
		ArrayList<String> full_names = new ArrayList<String>(); 
		
		for(String first_name: first_names) {
			for(String last_name: last_names) {
				if(first_name!=last_name) {
					full_names.add(first_name+ "0" +last_name);
				}
			}
		}
		
		return full_names;
		
	}
	
	private static ArrayList<String> first_name_by_color(String color) {
		
		ArrayList<String> better_names = new ArrayList<String>();
		
		switch(color) {
			case "Brown":
				better_names.add("Oak");
				better_names.add("Pine");
				better_names.add("Aspen");
				better_names.add("Rowan");
				better_names.add("Bark");
				better_names.add("Mud");
				better_names.add("Earth");
				break;
			case "Orange":
			case "Yellow":
				better_names.add("Sun");
				better_names.add("Sand");
				better_names.add("Flame");
				better_names.add("Ember");
				better_names.add("Fire");
				better_names.add("Golden");
				better_names.add("Lightning");
				break;
			case "Blue":
				better_names.add("Azure");
				better_names.add("Blue");
				better_names.add("Rain");
				better_names.add("Sea");
				better_names.add("Ocean");
				better_names.add("Sky");
				better_names.add("River");
				break;
			case "Purple":
				better_names.add("Dusk");
				better_names.add("Dawn");
				better_names.add("Storm");
				better_names.add("Thunder");
				break;
			case "Red":
				better_names.add("Blood");
				better_names.add("Ruby"); 
				better_names.add("Red");
				better_names.add("Autumn");
				better_names.add("Crimson");
				better_names.add("Scarlet");
				better_names.add("Flame");
				better_names.add("Ember");
				better_names.add("Fire");
				break;
			case "Black":
				better_names.add("Shadow");
				better_names.add("Night");
				better_names.add("Black");
				better_names.add("Dark");
				better_names.add("Onyx");
				break;
			case "White":
				better_names.add("Ivory");
				better_names.add("Silver");
				better_names.add("White");
				better_names.add("Ice");
				better_names.add("Snow");
				better_names.add("Frost");
				better_names.add("Frozen");
				better_names.add("Moon");
				better_names.add("Cloud");
				better_names.add("Winter");
				break;
			case "Gray":
				better_names.add("Silver");
				better_names.add("Slate");
				better_names.add("Ash");
				better_names.add("Storm");
				better_names.add("Cloud");
				better_names.add("Stone");
				break;
			case "Green":
				better_names.add("Jade");
				better_names.add("Summer");
				better_names.add("Jasmine");
				better_names.add("Emerald");
				better_names.add("Leaf");
				better_names.add("Vine");
				better_names.add("Fern");
				break;
				
		}
		
		return better_names;
	}
	
	private static ArrayList<String> last_name_by_color(String color) {
		
		ArrayList<String> better_names = new ArrayList<String>();
		
		switch(color) {
			case "Brown":
				better_names.add("Cliff");
				better_names.add("Ridge");
				better_names.add("Shade");
				better_names.add("Grove");
				break;
			case "Orange":
				better_names.add("Clay");
			case "Yellow":
				better_names.add("Sun");
				better_names.add("Sand");
				better_names.add("Flame");
				better_names.add("Ember");
				better_names.add("Lightning");
				better_names.add("Fire");
				break;
			case "Blue":
				better_names.add("Rain");
				better_names.add("Sea");
				better_names.add("Ocean");
				better_names.add("Sky");
				better_names.add("River");
				better_names.add("Stream");
				break;
			case "Purple":
				better_names.add("Dusk");
				better_names.add("Dawn");
				better_names.add("Storm");
				better_names.add("Thunder");
				break;
			case "Red":
				better_names.add("Blood");
				better_names.add("Ruby"); 
				better_names.add("Autumn");
				better_names.add("Flame");
				better_names.add("Ember");
				better_names.add("Fire");
				break;
			case "Black":
				better_names.add("Shadow");
				better_names.add("Night");
				better_names.add("Onyx");
				better_names.add("Smoke");
				break;
			case "White":
				better_names.add("Ivory");
				better_names.add("Silver");
				better_names.add("Ice");
				better_names.add("Snow");
				better_names.add("Frost");
				better_names.add("Moon");
				better_names.add("Cloud");
				better_names.add("Winter");
				break;
			case "Gray":
				better_names.add("Silver");
				better_names.add("Slate");
				better_names.add("Ash");
				better_names.add("Storm");
				better_names.add("Cloud");
				better_names.add("Stone");
				break;
			case "Green":
				better_names.add("Jade");
				better_names.add("Jasmine");
				better_names.add("Leaf");
				better_names.add("Vine");
				better_names.add("Fern");
				better_names.add("Moss");
				better_names.add("Shade");
				better_names.add("Grove");
				break;
				
		}
		
		return better_names;
	}
	
	private static ArrayList<String> first_name_by_shade(String shade) {
		ArrayList<String> better_names = new ArrayList<String>();

		switch(shade) {
			case "LIGHT":
				better_names.add("Light");
				break;
			case "DARK":
				better_names.add("Dark");
				break;
		}
		
		return better_names;
	}
	
	private static ArrayList<String> last_name_by_shade(String shade) {
		ArrayList<String> better_names = new ArrayList<String>();

		switch(shade) {
			case "LIGHT":
				better_names.add("Light");
				break;
			case "DARK":
				better_names.add("Shadow");
				break;
		}
		
		return better_names;
	}
	
	private static ArrayList<String> first_name_by_pigment(String pigment) {
		ArrayList<String> better_names = new ArrayList<String>();

		switch(pigment) {
			case "PALE":
				better_names.add("Pale");
				break;
			case "INTENSE":
				better_names.add("Bright");
				break;
		}
		
		return better_names;
	}
	
	private static ArrayList<String> first_name_by_stat(int code) {
		
		ArrayList<String> better_names = new ArrayList<String>();
		/*
		 * 1 - STR
		 * 2 - DEF
		 * 3 - SPD
		 * 4 - AGG - HIGH
		 * 5 - AGG - LOW
		 * 
		 */
		switch(code) {
		case 1:
			better_names.add("Strong");
			better_names.add("Fierce");
			better_names.add("Burning");
			better_names.add("Striking");
			better_names.add("Scorching");
			break;
		case 2:
			better_names.add("Tough");
			better_names.add("Hard");
			better_names.add("Solid");
			better_names.add("Thunder");
			break;
		case 3:
			better_names.add("Quick");
			better_names.add("Fleet");
			better_names.add("Rushing");
			better_names.add("Dancing");
			better_names.add("Lightning");
			break; 
		case 4: 
			better_names.add("Brave");
			better_names.add("Loud");
			better_names.add("Rising");
			better_names.add("Wild");
			better_names.add("Striking");
			break;
		case 5: 
			better_names.add("Gentle");
			better_names.add("Quiet");
			better_names.add("Soft");
			better_names.add("Fallen");
			better_names.add("Silent");
			break;
		}
		
		
		return better_names;
		
	}
	
	private static ArrayList<String> last_name_by_stat(int code) {
		
		ArrayList<String> better_names = new ArrayList<String>();
		/*
		 * 1 - STR
		 * 2 - DEF
		 * 3 - SPD
		 * 4 - AGG - HIGH
		 * 5 - AGG - LOW
		 * 
		 */
		switch(code) {
		case 1:
			better_names.add("Strike");
			better_names.add("Fang");
			better_names.add("Claw");
			better_names.add("Talon");
			break;
		case 2:
			better_names.add("Stone");
			better_names.add("Scale");
			better_names.add("Tail");
			better_names.add("Horn");
			better_names.add("Shoulder");
			better_names.add("Ridge");
			better_names.add("Thunder");
			break;
		case 3:
			better_names.add("Streak");
			better_names.add("Flight");
			better_names.add("Dance");
			better_names.add("Wing");
			better_names.add("Chaser");
			better_names.add("Bolt"); 
			better_names.add("Lightning");
			break; 
		case 4: 
			better_names.add("Charge"); 
			better_names.add("Bolt"); 
			better_names.add("Chaser");
			better_names.add("Strike");
			better_names.add("Scorch");
			break;
		case 5: 
			better_names.add("Spirit");
			better_names.add("Soul");
			better_names.add("Feather");
			better_names.add("Song");
			better_names.add("Shine");
			better_names.add("Pool");
			better_names.add("Rain");
			better_names.add("Cloud");
			break;
		}
		
		return better_names;
		
	}
	
	
}
