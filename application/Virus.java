package application;

import java.util.ArrayList;
import java.util.Random;

public class Virus {

	/*
	 * Create a virus which characteristics that can evolve, reproduce, and spread. 
	 */
	
	private static int virus_count = 0;
	private static int virus_cap = 10;
	
	private static ArrayList<Virus> recurrent_infections = new ArrayList<Virus>();
	
	private int id; 
	private int current_infected = 0;
	private int total_infected = 0;
	private int killed = 0;
	private ArrayList<Dragon> infected = new ArrayList<Dragon>();
	private Virus parent = null;
	
	private double trans;	//how easily it spreads
	private double resil; 	//how tough it is to kill
	private double death;	//how deadly it is
	private double visil; 	//how visible it is
	private double adapt; 	//how quickly it can mutate
	
	/*
	 * Spawn
	 */
	public Virus () {
		
		Random rand = new Random();
		trans = rand.nextDouble();
		resil = rand.nextDouble();
		death = rand.nextDouble();
		visil = rand.nextDouble();
		adapt = rand.nextDouble()/3;
		
		virus_count++;
		id = virus_count; 
		Main.diseases.add(this);
		
	}
	
	/*
	 * Evolution
	 */
	public Virus (Virus parent) {
		
		virus_count++;
		id = virus_count; 
		Random rand = new Random();
		int trans_ev = rand.nextInt(3);
		if (trans_ev == 0 ) {
			trans = parent.trans - 0.1;
		}
		else if (trans_ev == 1) {
			trans = parent.trans;
		}
		else {
			trans = parent.trans + 0.1; 
		}
		
		int resil_ev = rand.nextInt(3);
		if (resil_ev == 0) {
			resil = parent.resil - 0.1;
		}
		else if (resil_ev == 1) {
			resil = parent.resil;
		}
		else {
			resil = parent.resil + 0.1; 
		}
		
		int death_ev = rand.nextInt(3);
		if (death_ev == 0 ) {
			death = parent.death - 0.1;
		}
		else if (death_ev == 1) {
			death = parent.death;
		}
		else {
			death = parent.death + 0.1; 
		}
		if (death < 0) {
			death = 0.01;
		}
		
		int visil_ev = rand.nextInt(3);
		if (visil_ev == 0) {
			visil = parent.visil - 0.1;
		}
		else if (visil_ev == 1) {
			visil = parent.visil;
		}
		else {
			visil = parent.visil + 0.1; 
		}
		
		int adapt_ev = rand.nextInt(3);
		if (adapt_ev == 0) {
			adapt = parent.adapt - 0.1;
		}
		else if (adapt_ev == 1) {
			adapt = parent.adapt;
		}
		else {
			adapt = parent.adapt + 0.1; 
		}
		if (adapt > 0.5) {
			adapt = 0.5;
		}
		else if (adapt < 0) {
			adapt = 0.01;
		}
		
		this.parent = parent;
		Main.diseases.add(this);
		
	}
	
	public double get_trans () {
		return trans;
	}
	
	public double get_visil() {
		return visil;
	}
	
	public double get_death() {
		return death;
	}
	
	public double get_resil() {
		return resil;
	}
	
	public double get_adapt() {
		return adapt;
	}
	
	public int get_current_infected() {
		return current_infected;
	}
	
	public int get_total_infectd() {
		return total_infected;
	}
	
	public int get_id() {
		return id;
	}
	
	public int get_killed() {
		return killed; 
	}
	
	public ArrayList<Dragon> get_infected() {
		return infected;
	}
	
	public Virus get_parent() {
		return parent; 
	}
	
	public boolean add_infected(Dragon dragon) {
		if(infected.contains(dragon)) {
			return false;
		}
		else {
			infected.add(dragon);
			current_infected++;
			total_infected++;
			return true;
		}
	}
	
	public boolean remove_infected(Dragon dragon, boolean isdead) {
		if (infected.remove(dragon)) {
			if (isdead) {
				killed++;
			}
			current_infected = current_infected - 1;
			return true;
		} 
		else {
			return false;
		}
	}
	
	public void document_fatality() {
		current_infected = current_infected - 1;
		killed++;
	}
	
	public void document_recovery() {
		current_infected = current_infected - 1;
	}
	
	public static Virus manual_infect(Dragon d) {
		
		Virus novel;
		Random rand = new Random();
		int seed = rand.nextInt(4);
		if (seed >= 2 & !recurrent_infections.isEmpty()) {
			int index = rand.nextInt(recurrent_infections.size());
			Virus recurrent_infection = recurrent_infections.get(index); 
			System.out.println("Virus #" + recurrent_infection.get_id() + " has reappeared.");
			if (!d.infections.contains(recurrent_infection) & !d.immunities.contains(recurrent_infection)) {
				novel = recurrent_infection; 
				Main.diseases.add(novel);
			}
			else {
				novel = new Virus();
			}
		}
		else {
			novel = new Virus();
		}
		d.infections.add(novel);
		novel.add_infected(d);
		
		if (novel.get_visil() > d.get_resist_coeff()) {
			d.setVisiblyIll(true);
		}
		
		//d.infect(d);
		return novel; 

	}
	
	public String trace_evolution() {
		String lineage = "Viral heritage: " + this.id;
		Virus i = this;
		while (i.parent != null) {
			lineage += " --> " + i.parent.get_id();
			i = i.parent;
		}
		return lineage; 
	}
	
	public static ArrayList<Virus> get_recurrent_infections() {
		return recurrent_infections; 
	}
	
	private void purge() {
		
		ArrayList<Dragon> out = new ArrayList<Dragon>();
		for (Dragon d: infected) {
			if (d.isDead() | !d.get_infections().contains(this)) {
				out.add(d);
			}
		}
		for (Dragon e: out) {
			remove_infected(e, false);
		}
		
		if(total_infected > Dragon.get_pop_size()/2 & !recurrent_infections.contains(this)) {
			recurrent_infections.add(this);
			System.out.println("Virus #" + this.id + " added to recurrent infections.");
		}
		
	}
	
	public String get_stats() {
		
		purge();
		
		String parent = "Original Virus";
		if (this.parent != null) {
			parent = "Parent virus: " + this.parent.get_id();
		}
		String lof = "";
		for (Dragon i: infected) {
			lof = lof + i.get_name() + ", ";
		}
		String stats = "ID: " + id + "\n"
					 + parent + "\n"
					 + "Transmission: " + trans + "\n"
					 + "Resillienc: " + resil + "\n"
					 + "Deadliness: " + death + "\n"
					 + "Visibility: " + visil + "\n"
					 + "Adaptability: " + adapt + "\n"
					 + "Current_infected: " + current_infected + "\n"
					 + "Total Infected: " + total_infected + "\n"
					 + "Fatalities: " + killed + "\n"
					 + "Infected: " + lof + "\n"
					 + trace_evolution() + "\n";
		return stats;
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
