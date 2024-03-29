package application;
	
import java.util.ArrayList;
import java.util.HashMap;

import application.Region.Resources;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class Main extends Application {
	
	private static ArrayList<Creature> dragons = new ArrayList<Creature>(); 
	private static ArrayList<Creature> prey = new ArrayList<Creature>(); 
	private static ArrayList<Creature> predators = new ArrayList<Creature>(); 
	public static ArrayList<Region> regions = new ArrayList<Region>();
	private static Region my_region = new Region("Region1"); 
	private static Creature selected = null;
	private static BorderPane root = new BorderPane();
	private static TextArea center_text = new TextArea(); 
	private static ListView<String> dragon_order = new ListView<String>(); 
	private static ListView<String> prey_order = new ListView<String>(); 
	private static ListView<String> predator_order = new ListView<String>(); 
	private static VBox bottom = new VBox(10);
	private static HBox left = new HBox(10);
	private static HBox top = new HBox(10); 
	private static VBox right = new VBox(10);
	private static VBox center = new VBox(10);
	private static Text env = new Text(); 
	private static Stage myStage;
	public static ArrayList<Virus> diseases = new ArrayList<Virus>();
	private static Text total_population = new Text("0");
	
	@Override
	public void start(Stage primaryStage) {
		try {
			
			myStage = primaryStage;
			
			setup_bottom();
			setup_left(); 
			setup_center();
			setup_top();
			setup_right();

			primaryStage.setTitle("DRGN");
			root.setTop(top);
			root.setLeft(left);
			root.setRight(right);
			root.setCenter(center);;
			BorderPane.setAlignment(root, Pos.CENTER);
			Scene scene = new Scene(root, 1000,675);
			scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
			primaryStage.setScene(scene);
			primaryStage.show();
			
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args) {
		launch(args);
	}
	
	private static void setup_left() {
		
		left.getChildren().add(prey_order);
		left.getChildren().add(dragon_order);
		left.getChildren().add(predator_order);
		left.setMinSize(450, 500);
		left.setMaxSize(450, 500);
		dragon_order.setMinWidth(125);
		prey_order.setMinWidth(125);
		predator_order.setMinWidth(125);
		
		dragon_order.setOnMouseClicked(e -> {
			ArrayList<Creature> dead = new ArrayList<Creature>();
			String name = dragon_order.getSelectionModel().getSelectedItem();
			if (dragons.isEmpty() & name == null) {}
			else {
				for (Creature m: dragons) {
					m.get_name();
					if (name == m.get_name()) {
						selected = m;
						//System.out.println(m.get_name() + " has been selected.\n");
						center_text.setText(selected.get_stats());
					}
					if (m.isDead()) {
						dead.add(m);
					}
				}
				for (Creature d: dead) {
					dragons.remove(d);
					dragon_order.getItems().remove(d.get_name());
				}
				dead.clear();
			}
			update_orders();
			dragon_order.getSelectionModel().select(name);
			if(name != null) {
				center_text.setText(selected.get_stats());
			}
		});
		
		prey_order.setOnMouseClicked(e -> {
			
			ArrayList<Creature> dead = new ArrayList<Creature>();
			String name = prey_order.getSelectionModel().getSelectedItem();
			if (prey.isEmpty() & name == null) {}
			else {
				for (Creature m: prey) {
					m.get_name();
					if (name == m.get_name()) {
						selected = m;
						center_text.setText(selected.get_stats());
					}
					if (m.isDead()) {
						dead.add(m);
					}
				}
				for (Creature d: dead) {
					prey.remove(d);
					prey_order.getItems().remove(d.get_name());
				}
				dead.clear();
			}
			update_orders();
			prey_order.getSelectionModel().select(name);
			if(name != null) {
				center_text.setText(selected.get_stats());
			}
		});
		
		predator_order.setOnMouseClicked(e -> {
			ArrayList<Creature> dead = new ArrayList<Creature>();
			String name = predator_order.getSelectionModel().getSelectedItem();
			if (predators.isEmpty() & name == null) {}
			else {
				for (Creature m: my_region.get_predator_population()) {
					m.get_name();
					if (name == m.get_name()) {
						selected = m;
						center_text.setText(selected.get_stats());
					}
					if (m.isDead()) {
						dead.add(m);
					}
				}
				for (Creature d: dead) {
					predators.remove(d);
					predator_order.getItems().remove(d.get_name());
				}
				dead.clear();
			}
			update_orders();
			predator_order.getSelectionModel().select(name);
			if(name != null) {
				center_text.setText(selected.get_stats());
			}
		});
		
		update_orders();
		
	}
	
	private static void setup_right() {
		
		Button manage_skills = new Button("Skills");
		manage_skills.setOnAction(e -> {
			if(Dragon.class.isInstance(selected)) {
				PopUpBox manager = new PopUpBox();
				manager.manage_skills((Dragon) selected);
			}
		});
		
		Button add_legend = new Button("Add Legend");
		add_legend.setOnAction(e -> {
			if(selected != null) {
				if(Dragon.class.isInstance(selected)) {
					Dragon selected_dragon = (Dragon) selected; 
					selected_dragon.add_to_legends();
				}
			}
		});
		
		Button get_legends = new Button("Get Legends");
		get_legends.setOnAction(e -> {
			for (Dragon d: Dragon.get_legends()) {
				System.out.println(d.get_stats() + "\n\n");
			}
		});
		
		Button infect = new Button("Infect");
		infect.setOnAction(e-> {
			if(selected != null) {
				Virus novel = Virus.manual_infect(selected);
				System.out.println(novel.get_stats());
			}
		});
		
		Button age = new Button("Age");
		age.setOnAction(e -> {
			if(selected != null) {
				selected.run();
				center_text.setText(selected.get_stats());
			}
		});
		
		right.getChildren().addAll(manage_skills, infect, age, add_legend, get_legends);
		right.setAlignment(Pos.CENTER);
		right.setPadding(new Insets(10, 10, 10, 10));
		
	}
	
	private static void setup_bottom() {
		
		regions.add(my_region);
		my_region.randomize_env();
		Skin env_skin = my_region.get_environment_skin();
		env.setText(env_skin.describe());
		
		Button randomize_env = new Button("Randomize");
		randomize_env.setOnAction(e-> {
			my_region.randomize_env();
			env.setText(my_region.get_environment_skin().describe());
		});
		
		ChoiceBox<Skin.COLORS> color_choice = new ChoiceBox<Skin.COLORS>();
		for (Skin.COLORS i: Skin.COLORS.values()) {
			color_choice.getItems().add(i);
		}
		color_choice.setValue(my_region.get_env_color());
		ChoiceBox<Skin.SHADES> shade_choice = new ChoiceBox<Skin.SHADES>();
		for (Skin.SHADES s: Skin.SHADES.values()) {
			shade_choice.getItems().add(s);
		}
		shade_choice.setValue(my_region.get_env_shade());
		ChoiceBox<Skin.PIGMENTS> pigment_choice = new ChoiceBox<Skin.PIGMENTS>();
		for (Skin.PIGMENTS p: Skin.PIGMENTS.values()) {
			pigment_choice.getItems().add(p);
		}
		pigment_choice.setValue(my_region.get_env_pigment());
		
		Button set_choice = new Button("Set");
		set_choice.setOnAction(e -> {
			my_region.set_environment_skin(color_choice.getValue(), shade_choice.getValue(), pigment_choice.getValue());
			env.setText(my_region.get_environment_skin().describe());
		});
		
		HBox one = new HBox(10);
		
		HBox two = new HBox(10); 
		one.setPadding(new Insets(10, 10, 10, 10));
		one.setAlignment(Pos.CENTER);
		one.getChildren().addAll(color_choice, shade_choice, pigment_choice, set_choice);
		two.setPadding(new Insets(0, 10, 10, 10));
		two.setAlignment(Pos.CENTER);
		two.getChildren().addAll(env, randomize_env);
		bottom.getChildren().addAll(one, two);
		root.setBottom(bottom);
		
	}
	
	public static void update_top() {
		top.getChildren().clear();
		setup_top();
	}
	
	private static ArrayList<Region> get_regions() {
		return regions;
	}
	
	private static void setup_top() {
		
		ChoiceBox<String> region_choices = new ChoiceBox<String>();
		region_choices.getItems().add(my_region.get_name());
		region_choices.setValue(my_region.get_name());
		region_choices.setOnMouseClicked(e -> {
			region_choices.getItems().clear();
			for(Region i: get_regions()) {
				region_choices.getItems().add(i.get_name());
			}
		});
		
		Button set_region = new Button("Go");
		set_region.setOnAction(e -> {
			for(Region i: regions) {
				if(i.get_name().equals(region_choices.getValue())) {
					my_region = i; 
					update_orders();
					Skin env_skin = my_region.get_environment_skin();
					env.setText(env_skin.describe());
					
					Button randomize_env = new Button("Randomize");
					randomize_env.setOnAction(d-> {
						my_region.randomize_env();
						env.setText(my_region.get_environment_skin().describe());
					});
					
					HBox two = new HBox(10); 
					two.setPadding(new Insets(0, 10, 10, 10));
					two.setAlignment(Pos.CENTER);
					two.getChildren().addAll(env, randomize_env);

					bottom.getChildren().remove(1);
					bottom.getChildren().add(two);
					
					Skin collective = Skin.collective_mix(my_region.get_dragon_population());
					String label = collective.get_pigment() + " " + collective.get_shade() + " " + collective.get_color(); 
					shift_color(label);
					
				}
			}
		});
		
		Button custom = new Button("Custom");
		custom.setOnAction(e-> {
			custom_builder(); 
		} );
		
		Button simulate = new Button("Simulate");
		simulate.setOnAction(e-> {
			ArrayList<Region> current_regions = new ArrayList<Region>();
			current_regions.addAll(regions);
			for (Region i: current_regions) {
				System.out.println(" --- " + i.get_name() + " ---");
				i.short_sim();
				i.disease_report(diseases);
				dragons = i.get_dragon_population();
				prey = i.get_prey_population();
				predators = i.get_predator_population();
				Skin collective = Skin.collective_mix(i.get_dragon_population());
				String label = collective.get_pigment() + " " + collective.get_shade() + " " + collective.get_color(); 
				shift_color(label);
				System.out.println(collective.describe());
				System.out.println("dM: " + Dragon.get_male_pop() + " // dF: " + Dragon.get_female_pop());
				HashMap<String, Integer> d_census = Creature.get_census(i.get_dragon_population());
				HashMap<String, Integer> p_census = Creature.get_census(i.get_prey_population());
				HashMap<String, Integer> f_census = Creature.get_census(i.get_predator_population());
				try {
					System.out.println("Foliage Percentage: " + i.get_resource_levels(Resources.PREY_FOOD)/10 + "%");
					Statistics p_stat = new Statistics(prey);
					Statistics d_stat = new Statistics(dragons);
					Statistics f_stat = new Statistics(predators);
				} catch(Exception g) {
					System.out.println("Unable to gather population data.");
				}
				total_population.setText(Integer.toString(Creature.population.size()));
				String feed = "PREY CENSUS: (" + i.get_prey_population().size() + ") ";
				for (String j: p_census.keySet()) {
					feed += " // " + j + ": " + p_census.get(j);
				}
				feed += "\nDRAGON CENSUS: (" + i.get_dragon_population().size() + ") ";
				for (String j: d_census.keySet()) {
					feed += " // " + j + ": " + d_census.get(j);
				}
				feed += "\nPREDATOR CENSUS: (" + i.get_predator_population().size() + ") ";
				for (String j: f_census.keySet()) {
					feed += " // " + j + ": " + f_census.get(j);
				}
				System.out.println(feed);
				//update_orders(); 
			}
			Skin collective = Skin.collective_mix(my_region.get_dragon_population());
			String label = collective.get_pigment() + " " + collective.get_shade() + " " + collective.get_color(); 
			shift_color(label);
			update_orders(); 
			
		});
		
		Button long_sim = new Button("Long Sim");
		long_sim.setOnAction(e-> {
			for (Region i: regions) {
				i.long_sim(100);
				update_orders(); 
			}
		});
		
		Button season = new Button("Season");
		season.setOnAction(e-> {
			Creature.season(my_region.get_prey_population());
			Creature.season(my_region.get_dragon_population());
			Creature.season(my_region.get_predator_population());
			update_orders();
		});
		
		Button spawn = new Button("Spawn");
		spawn.setOnAction(e-> {
			if(selected == null) {
				Dragon d_spawn = my_region.spawn_dragon();
				dragons.add(d_spawn);
				dragon_order.getItems().add(d_spawn.get_name());
				Predator f_spawn = my_region.spawn_predator();
				predators.add(f_spawn);
				predator_order.getItems().add(f_spawn.get_name());
				Prey p_spawn = my_region.spawn_prey();
				prey.add(p_spawn);
				prey_order.getItems().add(p_spawn.get_name());
			}
			else if (Dragon.class.isInstance(selected)) {
				Dragon d_spawn = my_region.spawn_dragon();
				dragons.add(d_spawn);
				dragon_order.getItems().add(d_spawn.get_name());
			}
			else if (Predator.class.isInstance(selected)) {
				Predator f_spawn = my_region.spawn_predator();
				predators.add(f_spawn);
				predator_order.getItems().add(f_spawn.get_name());
			}
			else if (Prey.class.isInstance(selected)) {
				Prey p_spawn = my_region.spawn_prey();
				prey.add(p_spawn);
				prey_order.getItems().add(p_spawn.get_name());
			}
		});
		
		top.setPadding(new Insets(10, 10, 10, 10));
		top.setAlignment(Pos.CENTER);
		top.getChildren().addAll(set_region, region_choices, custom, spawn, season, simulate, long_sim, total_population);
		
	}
	
	private static void setup_center() {
		center.setMinSize(200, 500);
		center.getChildren().add(center_text);
		center_text.setMinSize(200, 500);
		center.setPadding(new Insets(0, 10, 0, 10));
		root.setCenter(center);
	}
	
	public static void update_orders() {
		
		//System.out.println("Updating orders.");
		
		dragons = my_region.get_dragon_population();
		prey = my_region.get_prey_population();
		predators = my_region.get_predator_population();
		
		dragon_order.getItems().clear();
		
		for(Creature d: dragons) {
			if(!d.isDead & !dragon_order.getItems().contains(d.get_name())) {
				dragon_order.getItems().add(d.get_name());
			}
		}
		
		prey_order.getItems().clear();
		
		for (Creature p: prey) {
			if(!p.isDead & !prey_order.getItems().contains(p.get_name())) {
				prey_order.getItems().add(p.get_name());
			}
		}
		
		predator_order.getItems().clear();
		
		for(Creature f: predators) {
			if(!f.isDead & !predator_order.getItems().contains(f.get_name())) {
				predator_order.getItems().add(f.get_name());
			}
		}
		
		//System.out.println("Orders updated.");
		
	}
	
	private static void custom_builder() {
		PopUpBox alert = new PopUpBox();
		Dragon baby = alert.custom_build(my_region);
		if (alert.getStatus()) {
			Stage alertWindow = alert.getWindow();
			dragon_order.getItems().add(baby.get_name());
			dragons.add(baby);
			alert.killAlert(alertWindow);
		}
	}
	
	private static void shift_color(String color) {
		String name = "";
		switch(color) {
		
		case "INTENSE DARK BLACK":
			name = "jet-black";
			break;
		
		case "INTENSE DARK BROWN":
			name = "mahogany";
			break;
		case "INTENSE MEDIUM BROWN": 
			name = "intense-medium-brown";
			break;
		case "INTENSE LIGHT BROWN": 
			name = "intense-light-red";
			break;
		case "MODERATE DARK BROWN":
			name = "moderate-dark-brown";
			break;
		case "MODERATE MEDIUM BROWN":
			name = "brown";
			break;
		case "MODERATE LIGHT BROWN":
			name = "moderate-light-brown";
			break;
		case "PALE DARK BROWN":
			name = "pale-dark-brown";
			break; 
		case "PALE MEDIUM BROWN": 
			name = "pale-medium-red";
			break;
		case "PALE LIGHT BROWN": 
			name = "pale-light-brown";
			break;
		
		
		case "INTENSE DARK RED":
			name = "deep-red";
			break;
		case "INTENSE MEDIUM RED": 
			name = "rose-red";
			break;
		case "INTENSE LIGHT RED": 
			name = "blood-red";
			break;
		case "MODERATE DARK RED":
			name = "dark-rose";
			break;
		case "MODERATE MEDIUM RED":
			name = "red";
			break;
		case "MODERATE LIGHT RED":
			name = "deep-blush";
			break;
		case "PALE DARK RED":
			name = "pale-dark-red";
			break; 
		case "PALE MEDIUM RED": 
			name = "pale-red";
			break;
		case "PALE LIGHT RED": 
			name = "light-blush";
			break;
		
		
		
		case "INTENSE DARK PURPLE":
			name = "twilight";
			break;
		case "INTENSE MEDIUM PURPLE": 
			name = "intense-medium-purple";
			break;
		case "INTENSE LIGHT PURPLE": 
			name = "intense-light-purple";
			break;
		case "MODERATE DARK PURPLE":
			name = "moderate-dark-purple";
			break;
		case "MODERATE MEDIUM PURPLE":
			name = "purple";
			break;
		case "MODERATE LIGHT PURPLE":
			name = "moderate-light-purple";
			break;
		case "PALE DARK PURPLE":
			name = "pale-dark-purple";
			break; 
		case "PALE MEDIUM PURPLE": 
			name = "pale-medium-purple";
			break;
		case "PALE LIGHT PURPLE": 
			name = "lilac";
			break;
			
		case "INTENSE DARK BLUE":
			name = "night-blue";
			break;
		case "INTENSE MEDIUM BLUE": 
			name = "intense-medium-blue";
			break;
		case "INTENSE LIGHT BLUE": 
			name = "intense-light-blue";
			break;
		case "MODERATE DARK BLUE":
			name = "moderate-dark-blue";
			break;
		case "MODERATE MEDIUM BLUE":
			name = "blue";
			break;
		case "MODERATE LIGHT BLUE":
			name = "moderate-light-blue";
			break;
		case "PALE DARK BLUE":
			name = "pale-dark-blue";
			break; 
		case "PALE MEDIUM BLUE": 
			name = "pale-medium-blue";
			break;
		case "PALE LIGHT BLUE": 
			name = "pale-light-blue";
			break;
			
			
		case "INTENSE DARK GREEN":
			name = "evergreen";
			break;
		case "INTENSE MEDIUM GREEN": 
			name = "intense-medium-green";
			break;
		case "INTENSE LIGHT GREEN": 
			name = "intense-light-green";
			break;
		case "MODERATE DARK GREEN":
			name = "moderate-dark-green";
			break;
		case "MODERATE MEDIUM GREEN":
			name = "green";
			break;
		case "MODERATE LIGHT GREEN":
			name = "moderate-light-green";
			break;
		case "PALE DARK GREEN":
			name = "pale-dark-green";
			break; 
		case "PALE MEDIUM GREEN": 
			name = "pale-medium-green";
			break;
		case "PALE LIGHT GREEN": 
			name = "pale-light-green";
			break;
			
			
		case "INTENSE DARK YELLOW":
			name = "brass";
			break;
		case "INTENSE MEDIUM YELLOW": 
			name = "intense-medium-yellow";
			break;
		case "INTENSE LIGHT YELLOW": 
			name = "intense-light-yellow";
			break;
		case "MODERATE DARK YELLOW":
			name = "moderate-dark-yellow";
			break;
		case "MODERATE MEDIUM YELLOW":
			name = "yellow";
			break;
		case "MODERATE LIGHT YELLOW":
			name = "moderate-light-yellow";
			break;
		case "PALE DARK YELLOW":
			name = "pale-dark-yellow";
			break; 
		case "PALE MEDIUM YELLOW": 
			name = "pale-medium-yellow";
			break;
		case "PALE LIGHT YELLOW": 
			name = "pale-light-yellow";
			break;
			
			
		case "INTENSE DARK ORANGE":
			name = "burnt-orange";
			break;
		case "INTENSE MEDIUM ORANGE": 
			name = "intense-medium-orange";
			break;
		case "INTENSE LIGHT ORANGE": 
			name = "intense-light-orange";
			break;
		case "MODERATE DARK ORANGE":
			name = "moderate-dark-orange";
			break;
		case "MODERATE MEDIUM ORANGE":
			name = "orange";
			break;
		case "MODERATE LIGHT ORANGE":
			name = "moderate-light-orange";
			break;
		case "PALE DARK ORANGE":
			name = "pale-dark-orange";
			break; 
		case "PALE MEDIUM ORANGE": 
			name = "pale-medium-orange";
			break;
		case "PALE LIGHT ORANGE": 
			name = "pale-light-orange";
			break;
			
			
		case "INTENSE DARK GRAY":
			name = "storm-gray";
			break;
		case "INTENSE MEDIUM GRAY": 
			name = "intense-medium-gray";
			break;
		case "INTENSE LIGHT GRAY": 
			name = "intense-light-gray";
			break;
		case "MODERATE DARK GRAY":
			name = "moderate-dark-gray";
			break;
		case "MODERATE MEDIUM GRAY":
			name = "gray";
			break;
		case "MODERATE LIGHT GRAY":
			name = "moderate-light-gray";
			break;
		case "PALE DARK GRAY":
			name = "pale-dark-gray";
			break; 
		case "PALE MEDIUM GRAY": 
			name = "pale-medium-gray";
			break;
		case "PALE LIGHT GRAY": 
			name = "pale-light-gray";
			break;
			
		case "MODERATE DARK WHITE":
		case "PALE DARK WHITE":
		case "INTENSE DARK WHITE":
			name = "moon-shadow";
			break; 
		case "PALE LIGHT WHITE":
		case "MODERATE LIGHT WHITE":
		case "INTENSE LIGHT WHITE":
			name = "snow-white";
			break;
		default:
			name = "default";
			break;
		}	
		if(root.getStyleClass().size() > 1) {
			root.getStyleClass().remove(1);
		}
		root.getStyleClass().add(name);
		//System.out.println(root.getStyleClass());
	}
	
	/*
	
	private static void setup_right() {
		
		Button rename = new Button("Rename");
		rename.setOnAction(e -> {
			PopUpBox alert = new PopUpBox();
			String new_name = alert.rename();
			if (alert.getStatus()) {
				Stage alertWindow = alert.getWindow();
				alert.killAlert(alertWindow);
			}
		});
		rename.setDisable(true);
		
		Button scavenge = new Button("Scavenge");
		scavenge.setOnAction(e -> {
			selected.scavenge();
			center.setText(selected.get_stats());
		});
		
		Button hunt = new Button("Hunt");
		hunt.setOnAction(e -> {
			selected.hunt();
			center.setText(selected.get_stats());
			});
		Button be_hunted = new Button("Be Hunted");
		be_hunted.setOnAction(e -> {
			selected.be_hunted();
			center.setText(selected.get_stats());
			});
		Button drink = new Button("Drink");
		drink.setOnAction(e -> {
			selected.drink();
			center.setText(selected.get_stats());
					});
		Button rest = new Button("Rest");
		rest.setOnAction(e->{
			selected.rest();
			center.setText(selected.get_stats());
			});
		Button lay_egg = new Button("Lay Egg");
		lay_egg.setOnAction(e-> {
			Random rand = new Random(); 
			boolean found = false;
			int count = 0;
			while (!found) {
				int a = rand.nextInt(dragons.size());
				if (count >= dragons.size()) {
					found = true; 
					break;
				}
				if (dragons.get(a) == selected) {
					count++;
					continue;
				}
				else if (
						(selected.get_gender() != dragons.get(a).get_gender())
						&
						(selected.get_stage() == dragons.get(a).get_stage())
						&
						(selected.is_related(dragons.get(a)) == false)
						) {
					int seed = rand.nextInt(100) + 1;
					int chance = 50 + selected.get_experience() + dragons.get(a).get_experience();
					if (chance > seed) {
						Dragon baby = selected.reproduce(dragons.get(a));
						if (baby != null) {
							dragons.add(baby);
							order_view.getItems().add(baby.get_name());
						}
					}
					found = true;
					break;
				}
				count++;
				
			}
		});
		
	}
	
	private static void setup_top() {
		
		Button spawn = new Button("Spawn");
		spawn.setOnAction(e -> {
			Dragon baby = null;
			Double avg = Dragon.get_avg_stat_coeff(dragons);
			int avg_life = Dragon.get_avg_life_span();
			if (dragons == null) {
				baby = new Dragon(avg, new Skin(), avg_life);
			}
			else if (dragons.isEmpty()) {
				baby = new Dragon(avg, new Skin(), 35);
			}
			else {
				Skin collective = Skin.collective_mix(dragons);
				Random rand = new Random();
				int chance = rand.nextInt(10);
				if (chance > 4) {
					baby = new Dragon(avg, new Skin(),  avg_life);
				}
				else {
					baby = new Dragon(avg, collective,  avg_life);
				}
			}
			Name.rename(baby);
			order_view.getItems().add(baby.get_name());
			dragons.add(baby);
		});
		Button clone = new Button("Clone");
		clone.setOnAction(e -> {
			Dragon baby = new Dragon(selected);
			order_view.getItems().add(baby.get_name());
			dragons.add(baby);
		});
		spawn.setAlignment(Pos.CENTER);
		
		Button simulate = new Button("Simulate");
		
		Button long_sim = new Button("Long Sim");
		long_sim.setOnAction(e -> {
			boolean done = false;
			int count = 0;
			while (count < 1000 & !done) {
				simulate.fire();
				ArrayList<Dragon> dead = new ArrayList<Dragon>();
				String name = order_view.getSelectionModel().getSelectedItem();
				if (dragons.isEmpty() & name == null) {
					done = true; 
				}
				else {
					for (Dragon m: dragons) {
						m.get_name();
						if (name == m.get_name()) {
							selected = m;
							//System.out.println(m.get_name() + " has been selected.\n");
							center.setText(selected.get_stats());
						}
						if (m.isDead()) {
							dead.add(m);
						}
					}
					for (Dragon d: dead) {
						dragons.remove(d);
						order_view.getItems().remove(d.get_name());
					}
					dead.clear();
					population.setText(Integer.toString(dragons.size()));
				}
				count++;
			}
		});
		
		Button season = new Button("Season");
		season.setOnAction(e-> {
			
			HashMap<Dragon, Integer> wins = new HashMap<Dragon, Integer>(); 
			HashMap<Dragon, Integer> losses = new HashMap<Dragon, Integer>(); 
			ArrayList<Dragon> winners = new ArrayList<Dragon>();
			ArrayList<Dragon> losers = new ArrayList<Dragon>(); 
			ArrayList<Dragon> dead = new ArrayList<Dragon>();
			HashMap<Dragon, Dragon> match_ups = new HashMap<Dragon, Dragon>(); 
			//competition
			Random rand = new Random();
			ArrayList<Dragon> younglings = new ArrayList<Dragon>();
			ArrayList<Dragon> challengers = new ArrayList<Dragon>();
			for (Dragon d: dragons) {
				if (!d.isDead()) {
					d.reset_season_count();
					int seed = rand.nextInt(100);
					if (seed > d.get_resist_coeff()*100 & dragons.size() > seed & diseases.isEmpty()) {
						Virus.manual_infect(d);
					}
					if (d.get_stage() == STAGE.ADULT & d.isHealthy() & !d.isDead() & !d.isVisiblyIll() & !d.reachedSeasonLimit()) {
						challengers.add(d);
						wins.put(d, 0);
						losses.put(d, 0);
					}
					else if (d.get_stage() == STAGE.ADOLESCENT | d.get_stage() == STAGE.HATCHLING) {
						younglings.add(d);
						for (Dragon p: d.get_parents()) {
							if (p != null) {
								if (!p.isDead()) {
									p.infect(d);
									d.infect(p);
								}
							}
						}
					}
				}
			}
			
			for(Dragon d: younglings) {
				if (!d.isVisiblyIll()) {
					for(Dragon m: younglings) {
						if (d != m & d.get_stage() == m.get_stage() & !m.isVisiblyIll()) {
							d.infect(m);
						}
					}
				}
			}
			
			for (Dragon d: challengers) {
				//System.out.println(d.get_name() + " sizes up the field.");
				ArrayList<Dragon> matches = new ArrayList<Dragon>(); 
				int limit = 0;
				if (d.isDead()) {
					dead.add(d);
					//System.out.println(d.get_name() + " is dead, and therefore can't compete.");
					continue;
				}
				else if (d.get_stage() != Dragon.STAGE.ADULT) {
					//System.out.println(d.get_name() + " is too young to compete.");
					continue;
				}
				else if (!d.isHealthy() | d.isVisiblyIll()) {
					//System.out.println(d.get_name() + " is not well enough to compete.");
					continue;
				}
				else if (d.reachedSeasonLimit()) {
					continue; 
				}
				else {
					
					for (Dragon m: challengers) {
						
						if(match_ups.get(m) == d | match_ups.get(d) == m) {
							continue; 
						}
						
						boolean will = false;
						if (!m.is_related(d) | 
							(!m.get_immediate_family(dragons).contains(d) & (d.get_num_of_offspring() < 2) & (m.get_num_of_offspring() < 2))) {
							will = true;
						}
						
						//System.out.println(d.get_name() + " sizes up " + m.get_name());
						
						if (m.get_stage() != STAGE.ADULT) {
							//System.out.println(m.get_name() + " is too young.");
							continue;
						}
						else if (m == d) {
							//System.out.println(m.get_name() + " was looking at their reflection in the water. How embarressing!");
							continue;
						}
						else if (m.isDead()) {
							//System.out.println(m.get_name() + " is dead.");
							dead.add(m);
							continue; 
						}
						if (d.isDead()) {
							dead.add(d);
							//System.out.println(d.get_name() + " is dead, and therefore can't compete.");
							break;
						}
						else if (m.isVisiblyIll()) {
							break;
						}
						else if (!m.isHealthy() & m.get_aggression() < 0.5) {
							//System.out.println(m.get_name() + " backs down.");
							continue; 
						}
						else if (!d.isHealthy() & d.get_aggression() < 0.5) {
							//System.out.println(d.get_name() + " backs down.");
							break;
						}
						else if (d.reachedSeasonLimit() | m.reachedSeasonLimit()) {
							break; 
						}
						else if (
								d.get_gender() == m.get_gender()
								& 
								(!d.get_immediate_family(dragons).contains(m) & !m.get_immediate_family(dragons).contains(d))
								&
								!(find_potential(d).isEmpty() | find_potential(m).isEmpty()))
								{
							
							boolean disjoint = true;
							for (Dragon g: find_potential(d)) {
								for( Dragon b: find_potential(m)) {
									if (g == b) {
										disjoint = false; 
									}
								}
							}
							
							if(disjoint) {
								System.out.println(m.get_name() + " & " + d.get_name() + " had nothing to fight over.");
								continue; 
							}
							
							//they'll fight
							d.infect(m);
							Dragon winner = d.fight(m);
							match_ups.put(d, m);
							match_ups.put(m, d);
							if (winner == null) {
								dead.add(d);
								dead.add(m);
							}
							else if (d == winner) {
								//winners.add(d);
								//losers.add(m);
								wins.put(d, wins.get(d)+1);
								losses.put(m, losses.get(m)+1);
							}
							else {
								//winners.add(m);
								//losers.add(m);
								wins.put(m, wins.get(m)+1);
								losses.put(d, losses.get(d)+1);
								break;
							}
							continue;
						}
						else if ((d.get_gender() != m.get_gender()) & (will)) {
							
							if (wins.get(d) > losses.get(d) | match_ups.isEmpty()) {
								System.out.println(d.get_name() + " wins the heart of " + m.get_name() + " with a score of " + wins.get(d) + "w/l" + losses.get(d));
								matches.add(m);
							}
						}
						else if ((d.is_related(m))) {
							//System.out.println(d.get_name() + " and " + m.get_name() + " are family.");
							d.infect(m);
						}
					}
					for (Dragon match: d.rank_choices(matches)) {
						if (!d.isDead() & !match.isDead()) {
							d.infect(match);
							Dragon baby = d.reproduce(match);
							if (baby != null) {
								eggs.add(baby);
								order_view.getItems().add(baby.get_name());
							}
						}
					}
				}
			}

			for (Dragon d: dead) {
				dragons.remove(d);
				order_view.getItems().remove(d.get_name());
			}
			babies = eggs.size();
			for (Dragon b: eggs) {
				dragons.add(b);
			}
			System.out.println(babies + " eggs were laid.");
			eggs.clear();
			System.out.println(disease_report());
		
			
		});
		
		simulate.setOnAction(e -> {
			for (String i: order_view.getItems()) {
				for (Dragon d: dragons) {
					if (d.get_name().equals(i)) {
						d.run();
					}
				}
			}
			season.fire();
			ArrayList<Dragon> dead = new ArrayList<Dragon>();
			for (Dragon m: dragons) {
				if (m.isDead()) {
					dead.add(m);
				}
			}
			for (Dragon d: dead) {
				dragons.remove(d);
				order_view.getItems().remove(d.get_name());
			}
			dead.clear();
			Skin collective = Skin.collective_mix(dragons);
			String label = collective.get_pigment() + " " + collective.get_shade() + " " + collective.get_color(); 
			shift_color(label);
			System.out.println(label);
			
			if (babies < 3) {
				while (babies < 4) {
					spawn.fire();
					babies++; 
				}
			}
			else {
				spawn.fire();
				spawn.fire();
			}
			HashMap<String, Integer> census = get_census(); 
			try {
				Statistics stat = new Statistics(dragons);
			} catch(Exception g) {
				System.out.println("Unable to gather population data.");
			}
			population.setText(Integer.toString(dragons.size()));
			String feed = "CENSUS: ";
			for (String i: census.keySet()) {
				feed += " // " + i + ": " + census.get(i);
			}
			System.out.println(feed);
		});
		
		top.setPadding(new Insets(10, 10, 10, 10));
		top.setAlignment(Pos.CENTER);
		top.getChildren().addAll(custom, spawn,season, simulate, long_sim, population);
		
	} */
	
}
