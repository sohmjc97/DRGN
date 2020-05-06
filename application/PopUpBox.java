package application;

import application.Dragon.GENDER;
import application.Skin.COLORS;
import application.Skin.PIGMENTS;
import application.Skin.SHADES;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class PopUpBox {
	
	private boolean m_exit = false; 
	private Stage window;
	private Dragon m_baby = null;
	private String new_name = "";
	
	public String rename() {

		Stage alertWindow = new Stage(); 
		window = alertWindow;
		alertWindow.initModality(Modality.APPLICATION_MODAL);
		alertWindow.setTitle("Custom Builder");
		alertWindow.setMinWidth(400);
		
		VBox thing = new VBox(10);
		
		HBox name_box = new HBox(10);	
		Label name_label = new Label("Name");
		TextField name_field = new TextField();
		name_field.setPromptText("Enter first_name here...");
		TextField last_name_field = new TextField();
		last_name_field.setPromptText("Enter last name here...");
		name_box.getChildren().addAll(name_label, name_field, last_name_field);
		
		GridPane options = new GridPane();
		options.setPadding(new Insets(10, 10, 10, 10));
		options.setHgap(10);
		options.setAlignment(Pos.CENTER);
		Button yes = new Button("Continue");
		GridPane.setConstraints(yes, 0, 0);
		yes.setOnAction(e -> {
			String name = name_field.getText() + "1" + last_name_field.getText();
			this.new_name = name; 
			this.close(alertWindow);
			});
		Button no = new Button("Cancel");
		GridPane.setConstraints(no, 1, 0);
		no.setOnAction(e -> killAlert(alertWindow));
		options.getChildren().addAll(yes, no);
		
		thing.getChildren().addAll(name_box, options);
		
		Scene scene = new Scene(thing);
		scene.getStylesheets().add("application/application.css");
		alertWindow.setScene(scene);
		alertWindow.showAndWait();
		
		return new_name;
		
	}
	
	public Dragon custom_build () {
		
		Stage alertWindow = new Stage(); 
		window = alertWindow;
		alertWindow.initModality(Modality.APPLICATION_MODAL);
		alertWindow.setTitle("Custom Builder");
		alertWindow.setMinWidth(400);
		//alertWindow.setMaxHeight(600);
		//alertWindow.setMaxWidth(400);
		
		BorderPane layout = new BorderPane();
		layout.setPadding(new Insets(10, 10, 10, 10));
		
		VBox middle = new VBox(20);
		middle.setPadding(new Insets(10, 10, 10, 10));
		middle.setAlignment(Pos.CENTER);
		//middle.setMaxWidth(400);
		Label msg = new Label("Fill out the form to build your custom dragon.");
		msg.getStyleClass().add("wrappable");
		middle.getChildren().add(msg);


		HBox gender_box = new HBox(10);
		Label gender_label = new Label("Pick a gender: ");
		ChoiceBox<Dragon.GENDER> gender_options = new ChoiceBox<Dragon.GENDER>(); 
		gender_options.getItems().addAll(GENDER.FEMALE, GENDER.MALE);
		gender_options.setValue(GENDER.MALE);
		gender_box.getChildren().addAll(gender_label, gender_options);
		
		HBox skin_box = new HBox(10); 
		Label skin_label = new Label("Pick a skin: ");
		ChoiceBox<Skin.PIGMENTS> pigment_options = new ChoiceBox<Skin.PIGMENTS>(); 
		for (Skin.PIGMENTS p: Skin.PIGMENTS.values()) {
			pigment_options.getItems().add(p);
		}
		pigment_options.setValue(PIGMENTS.INTENSE);
		ChoiceBox<Skin.SHADES> shade_options = new ChoiceBox<Skin.SHADES>(); 
		for (Skin.SHADES s: Skin.SHADES.values()) {
			shade_options.getItems().add(s);
		}
		shade_options.setValue(SHADES.DARK);
		ChoiceBox<Skin.COLORS> color_options = new ChoiceBox<Skin.COLORS>(); 
		for (Skin.COLORS i: Skin.COLORS.values()) {
			color_options.getItems().add(i);
		}
		color_options.setValue(COLORS.BLACK);
		skin_box.getChildren().addAll(skin_label, pigment_options, shade_options, color_options);
		
		HBox growth_box = new HBox(10); 
		Label attack = new Label("Attack: ");
		ChoiceBox<Double> attack_options = new ChoiceBox<Double>(); 
		Label defense = new Label("Defense: ");
		ChoiceBox<Double> defense_options = new ChoiceBox<Double>(); 
		Label speed = new Label("Speed: "); 
		ChoiceBox<Double> speed_options = new ChoiceBox<Double>(); 
		ChoiceBox<Double> aggression = new ChoiceBox<Double>();
		ChoiceBox<Double> immune_sys = new ChoiceBox<Double>();
		int value = 0;
		while (value <= 9) {
			Double num = Double.parseDouble(0+ "." + value);
			attack_options.getItems().add(num);
			defense_options.getItems().add(num);
			speed_options.getItems().add(num);
			aggression.getItems().add(num);
			immune_sys.getItems().add(num);
			value++; 
		}
		Double one = 1.0; 
		attack_options.getItems().add(one);
		defense_options.getItems().add(one);
		speed_options.getItems().add(one);
		aggression.getItems().add(one);
		immune_sys.getItems().add(one);
		attack_options.setValue(0.0);
		defense_options.setValue(0.0);
		speed_options.setValue(0.0);
		growth_box.getChildren().addAll(attack, attack_options, defense, defense_options, speed, speed_options);
		
		HBox stat_box = new HBox(10); 
		
		Label imm_sys = new Label("Immunity: ");
		immune_sys.setValue(0.0);
		Label agg = new Label("Aggression: ");
		aggression.setValue(0.0);
		Label span = new Label("Life Span: ");
		ChoiceBox<Integer> life_span = new ChoiceBox<Integer>();
		int age = 20;
		while (age < 105) {
			life_span.getItems().add(age);
			age += 5; 
		}
		life_span.setValue(35);	
		stat_box.getChildren().addAll(imm_sys, immune_sys, agg, aggression, span, life_span);
		
		HBox name_box = new HBox(10);	
		Label name_label = new Label("Name");
		TextField name_field = new TextField();
		name_field.setPromptText("Enter first_name here...");
		TextField last_name_field = new TextField();
		last_name_field.setPromptText("Enter last name here...");
		name_box.getChildren().addAll(name_label, name_field, last_name_field);
		
		middle.getChildren().addAll(gender_box, skin_box, growth_box, stat_box, name_box);
		
		ScrollPane scroll = new ScrollPane(); 
		scroll.setMinSize(400, 600);
		scroll.setContent(middle);
		
		GridPane options = new GridPane();
		options.setPadding(new Insets(10, 10, 10, 10));
		options.setHgap(10);
		options.setAlignment(Pos.CENTER);
		Button yes = new Button("Continue");
		GridPane.setConstraints(yes, 0, 0);
		yes.setOnAction(e -> {
			GENDER gender = gender_options.getValue();
			Double atk = attack_options.getValue();
			Double def = defense_options.getValue();
			Double spd = speed_options.getValue();
			Double immsys = immune_sys.getValue();
			Double aggr = aggression.getValue();
			Integer lifespan = life_span.getValue();
			String first_name = name_field.getText();
			String last_name = last_name_field.getText();
			Skin skin = new Skin(color_options.getValue(), shade_options.getValue(), pigment_options.getValue());
			Dragon new_baby = new Dragon(skin, gender, atk, def, spd, aggr, immsys, lifespan, first_name, last_name);
			m_baby = new_baby; 
			this.close(alertWindow);
			});
		Button no = new Button("Cancel");
		GridPane.setConstraints(no, 1, 0);
		no.setOnAction(e -> killAlert(alertWindow));
		options.getChildren().addAll(yes, no);
		
		layout.setCenter(scroll);
		layout.setBottom(options);
		Scene scene = new Scene(layout);
		scene.getStylesheets().add("application/application.css");
		alertWindow.setScene(scene);
		alertWindow.showAndWait();
		
		return m_baby; 
		
	}
	
	
	private void close(Stage window) {
		
		this.m_exit = true;
		window.close();
		
	}
	
	public boolean getStatus() {
		
		return m_exit; 
		
	}
	
	public Stage getWindow() {
		
		return window;
		
	}
	
	public void killAlert(Stage window) {
		
		window.close();
		
	}
	

}
