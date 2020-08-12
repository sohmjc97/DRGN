package application;

import java.util.HashMap;
import java.util.Random;

public class DNA {
	
	private static int strand_length = 10;
	private static Random rand = new Random();
	
	/*
	static enum Immune_Genes {
		A,
		B,
		C,
		D,
		E,
		F,
		G,
		H,
		I,
		J,
		K
	}
	
	static HashMap<Immune_Genes, Boolean> Immunity_Expression = new HashMap<Immune_Genes, Boolean>();
	
	static {
		int count = 0;
		for (Immune_Genes x: Immune_Genes.values()) {
			if ( (count%2) == 0) {
				Immunity_Expression.put(x, true);
			}
			else {
				Immunity_Expression.put(x, false);
			}
		}
	}
	
	static enum Longevity_Genes {
		QUICK_GROWTH,
		SLOW_GROWTH,
		EARLY_EXPITAION,
		LATE_EXPIRATION
	}
	*/
	//private Immune_Genes[] immunities = {Immune_Genes.A, Immune_Genes.B};
	
	/*
	 * Gene 1: Male/Female (0/0 = Female, 0/1 = Male, 1/0 = Male, 1/1 = Genderless)
	 * Gene 2: Horns/No Horns (0/0 = No Horns, other = Horns)
	 * Gene 3: Tail/ No Tail (0/0 = No Tail, other = Tail)
	 * Gene 4: Eye color (0/0 = Blue, 0/1 = Brown, 1/0 = Brown, 1/1 = Brown)
	 * Gene 5: Hair color (0/0 = Blond, 0/1 = Brown, 1/0 = Brown, 1/1 = Brown)
	 * Gene 6: Height (0/0 = Short, 0/1 = Medium, 1/0 = Medium, 1/1 = Tall)
	 * Gene 7: Size  (0/0 = Small, 0/1 = Medium, 1/0 = Medium, 1/1 = Large)
	 * Gene 8: Speed  (0/0 = Slow, 0/1 = Medium, 1/0 = Medium, 1/1 = Fast)
	 * Gene 9: Strength  (0/0 = Weak, 0/1 = Medium, 1/0 = Medium, 1/1 = Strong)
	 * Gene 10: Defense (0/0 = Weak, 0/1 = Medium, 1/0 = Medium, 1/1 = Tough)
	 */
	private String strand1 = "";
	private String strand2 = "";
	
	public DNA() {
		int index = 0;
		while(index < strand_length) {
			strand1 += rand.nextInt(2);
			strand2 += rand.nextInt(2);
			index++;
		}
	}
	
	public DNA(DNA one, DNA two) {
		String split_one = one.getStrand(rand.nextInt(2) +1);
		String split_two = two.getStrand(rand.nextInt(2) +1);
		int chance = rand.nextInt(2);
		if (chance == 0) {
			this.strand1 = split_one;
			this.strand2 = split_two;
		}
		else {
			this.strand1 = split_two;
			this.strand2 = split_one;
		}
	}
	
	private String getStrand(int num) {
		if(num == 1) {
			return strand1;
		}
		else {
			return strand2;
		}
	}
	
	/*
	public Immune_Genes determine_phenotype() {
		boolean one_dom = Immunity_Expression.get(immunities[0]);
		boolean two_dom = Immunity_Expression.get(immunities[1]);
		if (one_dom & !two_dom) {
			return immunities[0];
		}
		else if (!one_dom & two_dom) {
			immunities[1];
		}
	}*/
	
}
