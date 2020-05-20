package application;

import java.util.ArrayList;

public class Statistics {
	
	private static int stat_count = 0;
	private int id; 

	private static ArrayList<Creature> total_population = new ArrayList<Creature>();
	private ArrayList<Creature> current_population = new ArrayList<Creature>();
	
	private static double survival_to_adulthood;
	private static double survive_and_reproduce;
	private static double survival_to_adolescence;
	
	private Creature highest_growths;
	private Creature lowest_growths;
	private Creature highest_imm;
	private Creature lowest_imm; 
	private Creature highest_agg;
	private Creature lowest_agg;
	private Creature best_overall;
	private Creature worst_overall;
	
	private double avg_growths;
	private double avg_imm;
	private double avg_agg;
	private double avg_rating; 
	
	private double avg_lifespan; 
	private double avg_offspring; 
	
	public Statistics (ArrayList<Creature> pop) {
		
		stat_count++;
		id = stat_count; 
		
		for (Creature d: pop) {
			if (!total_population.contains(d)) {
				total_population.add(d);
			}
		}
		
		current_population = pop;
		avg_imm = get_avg_immunity(); 
		avg_growths = get_avg_stat_coeff();
		avg_agg = get_avg_aggression();
		avg_lifespan = get_avg_life_span();
		find_end_growths(); 
		find_end_imms();
		find_end_aggs();
		
		find_biggest_dynasty_of_living_ancestors(pop);
		find_longest_dynasty_of_living_ancestor(pop);
		find_longest_surviving_dynasty(pop);
		
		System.out.println(report());
		
	}
	
	
	public int get_avg_life_span() {
		
		int avg = 0;
		for (Creature d: current_population) {
			avg += d.get_life_span();
		}
		if (current_population == null) {
			return 0;
		}
		else if(current_population.size() == 0) {
			return 0;
		}
		else {
			return avg/current_population.size();
		}
		
	}
	
	public double get_avg_immunity() {
		
		double avg = 0;
		for (Creature d: current_population) {
			avg += d.get_immune_sys();
		}
		if (current_population == null) {
			return 0;
		}
		else if(current_population.size() == 0) {
			return 0;
		}
		else {
			return avg/current_population.size();
		}
		
	}
	
	public double get_avg_stat_coeff() {
		
		double avg = 0; 

		for (Creature d: current_population) {
			avg += d.get_stat_coeff();
		}
		if (current_population.size() != 0) {
			avg = avg/current_population.size();
		}
		return avg;
		
	}
	
	public double get_avg_aggression () {
		double avg = 0;
		for (Creature d: current_population) {
			avg += d.get_aggression();
		}
		if (current_population.size() != 0) {
			return avg/current_population.size();
		}
		return 0;
	}
	
	public void find_end_growths() {
		
		Creature max = null;
		Creature min = null;
		double max_coeff = 0;
		double min_coeff = 100;
		for (Creature d: current_population) {
			double coeff = d.get_stat_coeff();
			if (coeff > max_coeff) {
				max = d; 
				max_coeff = coeff;
			}
			else if (coeff < min_coeff) {
				min = d;
				min_coeff = coeff;
			}
		}
		highest_growths = max;
		lowest_growths = min; 
		
	}
	
	public void find_end_imms() {
		
		Creature max = null;
		Creature min = null;
		double max_coeff = 0;
		double min_coeff = 100;
		for (Creature d: current_population) {
			double coeff = d.get_immune_sys();
			if (coeff > max_coeff) {
				max = d; 
				max_coeff = coeff;
			}
			else if (coeff < min_coeff) {
				min = d;
				min_coeff = coeff;
			}
		}
		highest_imm = max;
		lowest_imm = min; 
		
	}
	
	public void find_end_aggs() {
		
		Creature max = null;
		Creature min = null;
		double max_coeff = 0;
		double min_coeff = 100;
		for (Creature d: current_population) {
			double coeff = d.get_aggression();
			if (coeff > max_coeff) {
				max = d; 
				max_coeff = coeff;
			}
			else if (coeff < min_coeff) {
				min = d;
				min_coeff = coeff;
			}
		}
		highest_agg = max;
		lowest_agg = min; 
		
	}
	
	private int find_average_offspring() {
		int avg = 0; 
		int total = 0;
		int count = 0; 
		
		for(Creature g: current_population) {
			if(g.isAdult()) {
				total += g.get_num_of_offspring();
				count++;
			}
		}
		
		avg = total/count;
		
		return avg; 
	}
	
	private Creature find_greatest_offspring() {
		Creature big_daddy = null; 
		int num = 0;
		
		for(Creature g: current_population) {
			if (g.get_num_of_offspring() > num) {
				big_daddy = g;
				num = g.get_num_of_offspring();
			}
		}
		
		if(big_daddy == null) {
			big_daddy = current_population.get(0);
		}
		
		return big_daddy; 
	}
	
	public void find_biggest_dynasty_of_living_ancestors (ArrayList<Creature> list) {
		
		Creature maxim = null;
		int max = 0; 
		Creature little = null;
		String lineage = "No lineage found.";
		for(Creature e: list) {
			//System.out.println("Investigating " + e.name);
			int check = find(e.get_offspring(), 1);
			if (check > max) {
				max = check;
				maxim = e; 
				//System.out.println(e.name + " is max. Investigating their offspring.");
				little = find_two(e.get_offspring(), 1, check, e);
				lineage = trace_lineage(maxim, little);
			}
		}
		if (maxim != null) {
			//System.out.println(maxim.get_name() + " is the head of the longest living dynasty containing " + max + " generation(s).");
		}
		else {
			//System.out.println("No elder found.");
		}
		if (little != null) {
			//System.out.println("The youngest of " + maxim.get_name() + "'s line is " + little.get_name());
		}
		else {
			//System.out.println("No little found.");
		}
		System.out.println("(P/M)atriarch: " + lineage);
		
	}
	
	public int find (ArrayList<Creature> list, int count) {
		
		int generations = 0 ; 
		
		if (list.isEmpty()) {
			generations = count; 
			//System.out.println(name + " has no offspring. " + generations + count);
		}
		int max = 1; 
		//int body_count = 0;
		for (Creature e: list) {
			if(e.isDead()) {
				//body_count++;
				//System.out.println(e.get_name() + " is dead, so they don't count." + generations + max + count);
				continue; 
			}
			else if(e.get_offspring().isEmpty()) {
				generations = count + 1; 
				//System.out.println(e.get_name() + " has no offspring." + generations + max + count);
			}
			else {
				//System.out.println("Looking into " + e.get_name() + "'s offspring." + generations + max + count);
				generations = find(e.get_offspring(), count+1);
			}
			if (generations > max) {
				max = generations; 
				//System.out.println("New max of " + max + " reached with " + e.get_name());
			}
		}
		//System.out.println("Find: " + max + " " + count);
		return max; 
	}
	
	public Creature find_two (ArrayList<Creature> list, int count, int aim, Creature d) {
		Creature b = null;
		Creature saved = null;
		if (list.isEmpty() & aim == count & !d.isDead()) {
			//System.out.println(this.name + " found w/ aim: " + aim + "/ count: " + count);
			return d; 
		}
		//System.out.println("Find_two: " + aim + " " + count);
		for (Creature e: list) {
				if (e.isDead() == true) {
					continue; 
				}
				else {
					if (e.get_offspring().isEmpty() & aim == count) {
						return e; 
					}
					b = find_two(e.get_offspring(), count+1, aim, e);
					if (b != null) {
						saved = b; 
					}
				}
		}
		return saved; 
	}
	
	/*
	 * Move to Statistics
	 */
	public String trace_lineage(Creature oldest, Creature youngest) {
		
		String none = "No lineage could be traced.";
		if (oldest != null & youngest != null) {
			String lineage = oldest.get_name();
			Creature next = oldest;
			boolean done = false; 
			int index = 0; 
			
			for (Creature d: next.get_offspring()) {
				if (d.get_offspring() == null | d.get_descendants() == null) {
					continue; 
				}
				else if (d == youngest) {
					lineage += " --> " + youngest.get_name();
					done = true; 
					break;
				}
				else if (d.get_offspring().contains(youngest)) {
					lineage += " --> " + d.get_name() + " --> " + youngest.get_name();
					done = true; 
					break;
				}
				else if (d.get_descendants().contains(youngest)) {
					lineage += " --> " + d.get_name();
					next = d; 
					continue; 
				}
				else {
					continue; 
				}
			}
			return lineage; 
		}
		else {
			return none; 
		}
			
	}
	
	public void find_longest_dynasty_of_living_ancestor(ArrayList<Creature> list) {
		
		int diff = 0; 
		int generations = 1; 
		Creature oldest = null; 
		Creature youngest = null; 
		for (Creature e: list) {
			int smaller_id = e.get_id();
			for (Creature d: e.get_descendants()) {
				//first is oldest ancestor, last is youngest descendant
				int bigger_id = d.get_id();
				if (bigger_id - smaller_id > diff) {
					oldest = e;
					youngest = d; 
					diff = bigger_id - smaller_id; 
				}
			}
		}
		if (oldest != null & youngest != null) {
			String lineage = oldest.get_name();
			Creature next = oldest;
			boolean done = false; 
			int index = 0; 
			
			for (Creature d: next.get_offspring()) {
				if (d.get_offspring() == null | d.get_descendants() == null) {
					continue; 
				}
				else if (d == youngest) {
					lineage += " --> " + youngest.get_name();
					done = true; 
					generations++;
					
					break;
				}
				else if (d.get_offspring().contains(youngest)) {
					lineage += " --> " + d.get_name() + " --> " + youngest.get_name();
					done = true; 
					generations+= 2;
					break;
				}
				else if (d.get_descendants().contains(youngest)) {
					lineage += " --> " + d.get_name();
					next = d; 
					generations++;
					continue; 
				}
				else {
					continue; 
				}
			}
			
			System.out.println("LD: " + oldest.get_name() + " --- " + generations + " ---> " + youngest.get_name());
			System.out.println("Lineage: " + lineage + "\n");
		}
		else {
			//System.out.println("No dynasties found.");
		}
	}
	
	public void find_longest_surviving_dynasty(ArrayList<Creature> list) {
		
		int diff = 0; 
		int generations = 1; 
		Creature oldest = null; 
		Creature youngest = null; 
		for (Creature living: list) {
			//for every living dragon
			int bigger_id = living.get_id();
			for (Creature elder: living.get_ancestors()) {
				//look at all of the ancestors of each dragon 
				int smaller_id = elder.get_id();
				//if they have the oldest ancestors
				if (bigger_id - smaller_id > diff) {
					//first ancestor, last descendants
					oldest = elder;
					youngest = living; 
					diff = bigger_id - smaller_id; 
				}
			}
		}
		//DragonG -- DragonD -- DragonGD
		if (oldest != null & youngest != null) {
			String lineage = youngest.get_name();
			Creature next = youngest;
			boolean done = false; 
			while (done == false) {
					if (next.get_parent_one() == oldest | next.get_parent_two() == oldest) {
						lineage = lineage + " <-- " + oldest.get_name();
						done = true; 
						generations++;
						break;
					}
					else if(next.get_parent_one() == null | next.get_parent_two() == null) {
						System.out.println("WHAT");
						done = true; 
						break;
					}
					else if (next.get_parent_one().get_ancestors().contains(oldest)) {
						next = next.get_parent_one();
						generations++;
						lineage = lineage + " <-- " + next.get_name();
					}
					else if (next.get_parent_two().get_ancestors().contains(oldest)) {
						next = next.get_parent_two();
						generations++;
						lineage = lineage + " <-- " + next.get_name();
					}
					else {
						System.out.println("ISSUE");
					}
			}
			System.out.println("SD: " + youngest.get_name() + " <--- " + generations + " --- " + oldest.get_name());
			System.out.println("Lineage: " + lineage + "\n");
		}
		else {
			//System.out.println("No dynasties to report.");
		}
	}
	
	public String report() {
		
		String report = "";
		
		report += "Highest Stats: " + highest_growths.get_name() + " - " + highest_growths.get_stat_coeff() +"\n"
				+ "Average stats: " + avg_growths + "\n"
				+ "Lowest stats: " + lowest_growths.get_name() + " - " + lowest_growths.get_stat_coeff() + "\n\n"
				+ "Highest Imm.: " + highest_imm.get_name() + " - " + highest_imm.get_immune_sys() + "\n"
				+ "Average Imm.: " + avg_imm + "\n"
				+ "Lowest Imm.: " + lowest_imm.get_name() + " - " + lowest_imm.get_immune_sys() + "\n\n"
				+ "Highest Agg: " + highest_agg.get_name() + " - " + highest_agg.get_aggression() + "\n"
				+ "Average Agg: " + avg_agg + "\n"
				+ "Lowest Agg: " + lowest_agg.get_name() + " - " + lowest_agg.get_aggression() + "\n"
				+ "Average Lifespan: " + avg_lifespan + "\n"
				+ "Average # offspring: " + find_average_offspring() + "\n"
				+ "Highest # offspring: " + find_greatest_offspring().get_name() + " // " + find_greatest_offspring().get_num_of_offspring() + "\n\n";
		
		return report; 
		
	}
	
}
