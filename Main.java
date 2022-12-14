import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
//import javafx.css.converter.StringConverter;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory;

import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.Duration;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.control.TextField;
import javafx.scene.control.DatePicker;
import javafx.scene.layout.GridPane;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import java.io.*;
import java.time.LocalDate;
import java.util.Scanner;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.RowConstraints;
import javafx.scene.Group;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.animation.Timeline;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.util.Duration;

//Main class
public class Main extends Application {
	
	Stage window, LogInStage;
	public Scene homeScene, statusScene, orderScene, logScene;
	ProgressBar orderProgress = new ProgressBar();
	private static final Integer STARTTIME = 15;
	private IntegerProperty timeSeconds = new SimpleIntegerProperty(STARTTIME*100);
	private Timeline timeline;
	private Timeline statusTimeline;
	public Label orderStatus = new Label("Preparing");

	int SCREEN_WIDTH = 800;
	int SCREEN_HEIGHT = 600;

	Text pizzaToppings;
	Text pizzaType;
	Text pizzaSize;
	Text subtitle3;

	Text total;

	ChoiceBox cbType;
	ChoiceBox cbSize;

	CheckBox cbM, cbOl, cbP, cbOn, cbEx, cbPa;

	boolean selected = false;

	String sizes[] = { "Small", "Medium", "Large"};
	String selectedSize;
	String selectedType;
	String toppings;
	String delimiter = "|";

	static String storageFileName = "SunDevilPizzaStorage.txt";
	static String previousOrders = "";
	String currentOrder;
	static File f;
	String userID;
	Double price; 

	HBox hboxPrice = new HBox();
	final DatePicker datePicker= new DatePicker();
	Spinner<Integer> spinnerHour = new Spinner<>();
	Spinner<String> spinnerMinute = new Spinner<>();
	ObservableList<String> minute;


	public static void main(String[] args) throws IOException {
		//create file
		f = new File(storageFileName);
		f.createNewFile();

		//read previous orders
		try {
			Scanner reader = new Scanner(f);
			while (reader.hasNextLine()) {
				previousOrders = previousOrders + reader.nextLine() + "\n";
			}
			reader.close();
		} catch (FileNotFoundException e) {
			System.out.println("An error occurred.");
			e.printStackTrace();
		}
		launch(args);
	}
	
	//initialize screens
	public void start(Stage primaryStage) throws Exception {
		window = primaryStage;
		
		//BorderPane for home screens
		BorderPane homeRoot = new BorderPane();
		homeRoot.setStyle("-fx-background-color: #D6D6D6;");
		homeRoot.setPadding(new Insets(20));
		homeRoot.setBottom(homeHBox());
		homeRoot.setTop(homeVBox());
		homeRoot.setCenter(dealsHBox());
		homeScene = new Scene(homeRoot, 800, 600);
	
	
		//BorderPane for status screen
		BorderPane statusRoot = new BorderPane();
		statusRoot.setStyle("-fx-background-color: #D6D6D6;");
		statusRoot.setPadding(new Insets(20));
		statusRoot.setBottom(returnHBox());
		statusRoot.setTop(titleVBox());
		statusRoot.setCenter(statusHBox());
		statusScene = new Scene(statusRoot, 800, 600);
		
		
		//BorderPane for order screen
		VBox orderRoot = new VBox();
		orderRoot.setStyle("-fx-background-color: #D6D6D6;");
		orderRoot.setSpacing(10);
		orderRoot.setPadding(new Insets(10, 10, 10, 10));
		orderRoot.setAlignment(Pos.TOP_CENTER);
		Text title = new Text("Pizza Selection");
		title.setStyle("-fx-font: 24 arial;");
		orderRoot.getChildren().addAll(title, hMain());
		orderScene = new Scene(orderRoot, 800, 600);
	
		
		//Group for login screen                                                                                                
		Group logRoot = new Group();
		logScene = new Scene(logRoot);
		logRoot.getChildren().addAll(LogInGridPane());
		
		
		//set window to homescene
		window.setScene(homeScene);
		window.setTitle("Sun Devil Pizza");
		window.show();
	}

//Login Screen handling
public GridPane LogInGridPane() {
	GridPane gridPaneLogIn = new GridPane();
	gridPaneLogIn.setStyle("-fx-background-color: #D6D6D6;");
	gridPaneLogIn.setPrefWidth(800);
	gridPaneLogIn.setPrefHeight(600);
	gridPaneLogIn.getColumnConstraints().add(new ColumnConstraints(0));
	gridPaneLogIn.getRowConstraints().add(new RowConstraints(50));
	
	//text configurations for labels 
	Text asuId = new Text();
	Text warningId = new Text();
	asuId.setText("ASURITE ID: ");
	asuId.setFont(Font.font("Impact", 20));
	warningId.setText("Please Enter Your ASURITE ID");
	warningId.setFont(Font.font("Impact", 20));
	
	//textfield to enter id
	TextField logInIdText = new TextField();
	logInIdText.setDisable(false);
	
	//log in button
	Button logInButton = new Button("Log In");
	
	//action for when user presses log in
	//cases to consider to pass log in
	logInButton.setOnAction(new EventHandler<ActionEvent>() {
		@Override public void handle(ActionEvent e) {
			String text = "";
			boolean numbersOnly;
			Timeline pause = new Timeline();
			if (logInIdText.getText().isEmpty() == false) {
				text = logInIdText.getText();
				numbersOnly = text.chars().allMatch(Character::isDigit);
				if (numbersOnly == true)
				{
					//if id is less than 10 digits output warning message
					if (text.length() < 10) {
						warningId.setText("             Not Enough Digits            ");
					}
					//if id is more than 10 digits output warning message
					if (text.length() > 10) {
						warningId.setText("             Too Many Digits              ");
					}

					//if id is equal to 10 digits log in

					if (text.length() == 10) {
						logInIdText.setDisable(true);
						pause.getKeyFrames().add(
								new KeyFrame(Duration.seconds(0.2),
								new KeyValue(warningId.textProperty(), "             Logging In ...               ")		));
						pause.getKeyFrames().add(
								new KeyFrame(Duration.seconds(2),
								new KeyValue(warningId.textProperty(), "                Logged In                 ")		));
						pause.playFromStart();
            userID = text;
						pause.setOnFinished(e2 -> window.setScene(orderScene));		
					}
				}
				else if (numbersOnly == false) {
					warningId.setText("       Please Enter Only Digits    ");
				}
			}
			else
			{
				warningId.setText("Please Enter Your ASURITE ID");
			}
			
			
		}
	});
	
	gridPaneLogIn.add(asuId, 1, 1);
	gridPaneLogIn.add(warningId, 2, 0);
	gridPaneLogIn.add(logInIdText, 2, 1);
	gridPaneLogIn.add(logInButton, 3, 1);
	gridPaneLogIn.setAlignment(Pos.CENTER);
	gridPaneLogIn.setPadding(new Insets(0, 25, 100, -20));
	
	return gridPaneLogIn;
}





//returnHBox used for the bottom of the status screen
public HBox returnHBox() {
		HBox hbox = new HBox();
		hbox.setAlignment(Pos.BOTTOM_RIGHT);
		
		//make a new order button set to return to home screen
		Button returnHome = new Button("Make a New Order");
		returnHome.setOnAction(new EventHandler<ActionEvent>() {
		@Override
		public void handle(ActionEvent event) {

			//reset selection screen
			price = 0.0;
			cbM.setSelected(false);
			cbOl.setSelected(false);
			cbP.setSelected(false);
			cbOn.setSelected(false);
			cbEx.setSelected(false);
			cbPa.setSelected(false);
			pizzaSize.setText("Size: ");
			pizzaType.setText("Type: ");
			total.setText("Your total is: " );
			pizzaToppings.setText("Toppings: ");
			cbType.setValue("Pepperoni");
			cbSize.setValue("Small");
			datePicker.setValue(null);
			SpinnerValueFactory<Integer> valueHour = new SpinnerValueFactory.IntegerSpinnerValueFactory(1,12);
			SpinnerValueFactory<String> valueMinute = new SpinnerValueFactory.ListSpinnerValueFactory<String>(minute);
			spinnerHour.setValueFactory(valueHour);
			spinnerMinute.setValueFactory(valueMinute);
			selected = false;

			//reset status screen
			orderStatus.setText("Preparing");
			timeSeconds.set((STARTTIME+1)*100);
			timeline = new Timeline();
			timeline.getKeyFrames().add(
					new KeyFrame(Duration.seconds(STARTTIME+1),
							new KeyValue(timeSeconds, 0)));
			timeline.playFromStart();

			statusTimeline = new Timeline();
			statusTimeline.getKeyFrames().add(
					new KeyFrame(Duration.seconds(6),
							new KeyValue(orderStatus.textProperty(), "Preparing")));
			statusTimeline.getKeyFrames().add(
					new KeyFrame(Duration.seconds(11),
							new KeyValue(orderStatus.textProperty(), "Cooking")));
			statusTimeline.getKeyFrames().add(
					new KeyFrame(Duration.seconds(16),
							new KeyValue(orderStatus.textProperty(), "Ready")));
			statusTimeline.playFromStart();
			//transition back to home screen
			window.setScene(homeScene);
		}
	});

		Button orderDetails = new Button("View Order Details");
		orderDetails.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				String t2 = toppings.substring(10); //remove "toppings"
				t2 = t2.replaceAll("\n", ", "); //replace newline characters with commas
				t2 = t2.substring(0, t2.length()-1); //remove comma at the end
				t2 = t2.toLowerCase();
				subtitle3.setText(userID + " ordered a " + selectedSize.toLowerCase() + " " + selectedType.toLowerCase() + " pizza with " + t2);
			}
		});

		hbox.getChildren().addAll(returnHome, orderDetails);
		
		return hbox;
}




//titleVbox used for top of Status Screen
public VBox titleVBox() {
	VBox vbox = new VBox();
	Text title = new Text("Thank you for ordering with Sun Devil Pizza!");
	title.setFont(Font.font("Roboto Blacak", FontWeight.BOLD, 36));
	Text subtitle = new Text("Your order has been sent to our store. ");
	Text subtitle2 = new Text("Check the progress on your pizza here, and come pick it up when it's ready.");
	subtitle3 = new Text("");
	subtitle3.setFont(Font.font("Roboto Blacak", FontWeight.NORMAL, 18));
	subtitle2.setFont(Font.font("Roboto Blacak", FontWeight.NORMAL, 18));
	subtitle.setFont(Font.font("Roboto Blacak", FontWeight.NORMAL, 18));
	vbox.getChildren().addAll(title, subtitle, subtitle2, subtitle3);
	vbox.setAlignment(Pos.TOP_CENTER);
	vbox.setSpacing(10);
	return vbox;
}

//status hbox used for progressbar
public HBox statusHBox() {
	HBox hbox = new HBox();
	hbox.setSpacing(30);
	Text status = new Text("Order Status:");
	status.setFont(Font.font("Roboto Blacak", FontWeight.BOLD, 18));
	orderStatus.setFont(Font.font("Roboto Blacak", FontWeight.BOLD, 18));

	
	orderProgress.progressProperty().bind(timeSeconds.divide(STARTTIME*100.0).subtract(1).multiply(-1));

	orderProgress.setPrefSize(300, 25);
	orderProgress.setStyle("-fx-accent: #8C1D40;");
	hbox.setAlignment(Pos.CENTER);
	hbox.getChildren().addAll(status, orderStatus, orderProgress);
	return hbox;
}


//dealsHBox used for graphic on home screen
public HBox dealsHBox() throws FileNotFoundException {
	HBox hbox = new HBox();

	FileInputStream inputstream = new FileInputStream("sparkyDeal.jpeg");

	Image img = new Image(inputstream);
	
	ImageView format = new ImageView(img);
	format.setFitWidth(760);
	format.setFitHeight(350);
	
	hbox.getChildren().add(format);

	hbox.setAlignment(Pos.CENTER);
	return hbox;
}

//homeHBox used for bottom of homescreen (order button)
public HBox homeHBox() {
	  HBox hbox = new HBox();
	  Button orderNow = new Button("Order Now");
	  orderNow.setPrefSize(200, 40);
	  orderNow.setOnAction(e -> window.setScene(logScene));
	  hbox.setAlignment(Pos.BOTTOM_RIGHT);
	  hbox.getChildren().add(orderNow);
	
	return hbox;
}

//homeVBox used for top of home screen (titles and text)
public VBox homeVBox() {
	 VBox vbox = new VBox();
	 vbox.setSpacing(10);
	 Text title = new Text("Welcome to Sun Devil Pizza!");
	 title.setFont(Font.font("Roboto Blacak", FontWeight.BOLD, 36));
	 title.setStrokeWidth(1);
	 vbox.setAlignment(Pos.TOP_CENTER);
	 
	 Text text = new Text("Click Order Now in the bottom right to get started");
	 text.setFont(Font.font("Roboto Blacak", FontWeight.NORMAL, 18));
	 
	 vbox.getChildren().addAll(title, text);
		
	return vbox;
}

//hMain used to organize order screen
public HBox hMain() {
	
	HBox hbox = new HBox();
 	//hbox.setStyle("-fx-background-color: #8b0000;");
 	hbox.setPrefWidth(SCREEN_WIDTH);
     	hbox.setPrefHeight(SCREEN_HEIGHT);

 	hbox.getChildren().add(hLeft());
 	hbox.getChildren().add(hRight());
	return hbox;
}

//hLeft used for left half of order screen
public VBox hLeft() {
	VBox vbox = new VBox();
 	vbox.setSpacing(10);
	vbox.setPadding(new Insets(10, 10, 10, 10));
     	//vbox.setStyle("-fx-background-color: #8C1D40;");
 	vbox.setPrefWidth(SCREEN_WIDTH/2);
     	vbox.setPrefHeight(SCREEN_HEIGHT/2);
	vbox.setAlignment(Pos.TOP_CENTER);

	HBox hboxSize = new HBox();
	hboxSize.setAlignment(Pos.CENTER);
	Text selectSize = new Text("Select Size:  ");
	selectSize.setStyle("-fx-font: 16 arial;");
	cbSize = new ChoiceBox();
	cbSize.getItems().addAll("Small", "Medium", "Large");
	cbSize.setPrefWidth(300);
	cbSize.getSelectionModel().selectFirst();
	hboxSize.getChildren().add(selectSize);
	hboxSize.getChildren().add(cbSize);

	HBox hboxType = new HBox();
	hboxType.setAlignment(Pos.CENTER);
	Text selectType = new Text("Select Type: ");
	selectType.setStyle("-fx-font: 16 arial;");
	cbType = new ChoiceBox();
	cbType.getItems().addAll("Pepperoni", "Cheese", "Vegetable");
	cbType.setPrefWidth(300);
	cbType.getSelectionModel().selectFirst();
	hboxType.getChildren().add(selectType);
	hboxType.getChildren().add(cbType);

	cbM = new CheckBox("Mushrooms");
	cbOl = new CheckBox("Olives");
	cbP = new CheckBox("Peppers");
	cbOn = new CheckBox("Onions");
	cbEx = new CheckBox("Extra Cheese");
	cbPa = new CheckBox("Pineapples");
	GridPane gridpane = new GridPane();
	gridpane.setPadding(new Insets(7, 7, 7, 7));
	gridpane.setAlignment(Pos.TOP_CENTER);
	gridpane.add(cbM, 0, 0);
	gridpane.add(cbOl, 0, 1);
	gridpane.add(cbOn, 0, 2);
	gridpane.add(cbP, 1, 0);
	gridpane.add(cbEx, 1, 1);
	gridpane.add(cbPa, 1, 2);
	
	Button select = new Button("Confirm Selection");
	
	//handling for confirm selection
	select.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				selectedSize =cbSize.getValue().toString();
				selectedType = cbType.getValue().toString();
				pizzaSize.setText("Size: " + selectedSize);
				if (selectedSize == "Small") {
					price = 5.99;
				}else if (selectedSize == "Medium") {
					price = 7.99;
				}else if (selectedSize == "Large") {
					price = 9.99;
				}

				pizzaType.setText("Type: " + selectedType);
				if (selectedType == "Vegetable") {
					price = price + 1;
				}

				toppings = "Toppings:\n";
				if(cbM.isSelected()) {toppings = toppings + "Mushrooms\n"; price = price + 1; }
				if(cbOl.isSelected()) {toppings = toppings + "Olives\n"; price = price + 1;}
				if(cbP.isSelected()) {toppings = toppings + "Peppers\n"; price = price + 1;}
				if(cbOn.isSelected()) {toppings = toppings + "Onions\n"; price = price + 1;}
				if(cbEx.isSelected()) {toppings = toppings + "Extra cheese\n"; price = price + 1;}
				if(cbPa.isSelected()) {toppings = toppings + "Pineapples"; price = price + 1;}
				
				pizzaToppings.setText(toppings);
				selected = true;
				
				total.setText("Your total is: " + price);
				hboxPrice.setAlignment(Pos.CENTER);
				hboxPrice.getChildren().addAll(total);
			}
	});

	//Text and hbox for price menu
	Text small = new Text("Small: $5.99");
	Text medium = new Text("Medium: $7.99");
	Text large = new Text("Large: $9.99");
	Text top = new Text("Each Topping: $1.00");
	Text veg = new Text("Vegetable Pizza: +$1.00 to size price");
	Text pepAndCheese = new Text("Pepperoni and Cheese Pizza: default size price");

	HBox hboxSmall = new HBox();
	hboxSmall.getChildren().add(small);
	HBox hboxMedium = new HBox();
	hboxMedium.getChildren().add(medium);
	HBox hboxLarge = new HBox();
	hboxLarge.getChildren().add(large);
	HBox hboxTop = new HBox();
	hboxTop.getChildren().add(top);
	HBox hboxVeg = new HBox();
	hboxVeg.getChildren().add(veg);
	HBox hboxPepAndCheese = new HBox();
	hboxPepAndCheese.getChildren().add(pepAndCheese);

 	vbox.getChildren().add(hboxSize);
	vbox.getChildren().add(hboxType);
	vbox.getChildren().add(gridpane);
	vbox.getChildren().add(select);
	vbox.getChildren().add(hboxSmall);
	vbox.getChildren().add(hboxMedium);
	vbox.getChildren().add(hboxLarge);
	vbox.getChildren().add(hboxTop);
	vbox.getChildren().add(hboxVeg);
	vbox.getChildren().add(hboxPepAndCheese);
	return vbox;
}

//hRight used for right side of order screen
public VBox hRight() {
	VBox vbox = new VBox();
	vbox.setSpacing(10);
	vbox.setPadding(new Insets(10, 10, 10, 10));
 	//vbox.setStyle("-fx-background-color: #FFC627;");
	vbox.setPrefWidth(SCREEN_WIDTH/2);
 	vbox.setPrefHeight(SCREEN_HEIGHT/2);
 	vbox.setAlignment(Pos.TOP_CENTER);

	Text yourPizza = new Text("Your Pizza: ");
	pizzaSize = new Text("Size: ");
	pizzaType = new Text("Type: ");
	total = new Text("Your total is: " );
	pizzaToppings = new Text("Toppings: ");

	// Date info and Date HBox
	Text enterDate = new Text("Pickup Date:");
	//datePicker = new DatePicker();
	HBox hboxDate = new HBox();
	hboxDate.setSpacing(10);
	hboxDate.setPrefWidth(SCREEN_WIDTH/2);
 	hboxDate.setPrefHeight(10);
	hboxDate.getChildren().addAll(enterDate, datePicker);
	//datePicker.setOnAction(new EventHandler() {
 		//public void handle(Event t) {
 			//LocalDate date = datePicker.getValue();
 			//System.err.println("Selected date: " + date);
			//}
	//});

	// Time section for spinner 
	Text enterTime = new Text("Pickup Time:");
	Text colon = new Text (":");
	minute = FXCollections.observableArrayList("00", "01", "02", "03", "04", "05", "06", "07", "08", "09",
			"10", "11", "12", "13", "14", "15", "16", "17", "18", "19","20", "21", "22", "23", "24", "25", "26", "27", "28", "29",
			"30", "31", "32", "33", "34", "35", "36", "37", "38", "39","40", "41", "42", "43", "44", "45", "46", "47", "48", "49",
			"50", "51", "52", "53", "54", "55", "56", "57", "58", "59");
	SpinnerValueFactory<Integer> valueHour = new SpinnerValueFactory.IntegerSpinnerValueFactory(1,12);
	SpinnerValueFactory<String> valueMinute = new SpinnerValueFactory.ListSpinnerValueFactory<String>(minute);
	spinnerHour.setValueFactory(valueHour);
	spinnerMinute.setValueFactory(valueMinute);
	valueHour.setWrapAround(true);
	valueMinute.setWrapAround(true);
	spinnerHour.editorProperty().get().setAlignment(Pos.CENTER);
	spinnerMinute.editorProperty().get().setAlignment(Pos.CENTER);
	spinnerHour.setPrefSize(75, 25);
	spinnerMinute.setPrefSize(75, 25);
	
	//Choice box for am/pm
	ChoiceBox dayOrNight = new ChoiceBox();
	dayOrNight.getItems().add("am");
	dayOrNight.getItems().add("pm");
	dayOrNight.setPrefWidth(20);
	dayOrNight.getSelectionModel().selectFirst();
	
	//HBox for time
	HBox hboxTime = new HBox();
	hboxTime.setSpacing(10);
	hboxTime.setPrefWidth(10);
 	hboxTime.setPrefHeight(10);
	hboxTime.getChildren().addAll(enterTime, spinnerHour, colon, spinnerMinute, dayOrNight);
	
	Button placeOrder = new Button("Order");
	
	//handling for order button
	placeOrder.setOnAction(new EventHandler<ActionEvent>() {
		@Override
		public void handle(ActionEvent event) {
		Alert a = new Alert(AlertType.NONE);
		LocalDate date = datePicker.getValue();
		if(selected == false){
			a.setAlertType(AlertType.ERROR);
        	a.setContentText("confirm selection");
        	a.show();
		}
		else if((dayOrNight.getValue().equals("am") && (spinnerHour.getValue() < 10 || spinnerHour.getValue() == 12)) 
				|| dayOrNight.getValue().equals("pm") && spinnerHour.getValue() == 11){
			a.setAlertType(AlertType.ERROR);
        	a.setContentText("Pickup time not available. Please choose a time between 10am-10pm.");
        	a.show();
		}
		else if(date == null || date.isBefore(LocalDate.now())){
			a.setAlertType(AlertType.ERROR);
        			a.setContentText("select a date");
      			        a.show();
		}
		else {
			//write to file
			String t2 = toppings.substring(10); //remove "toppings"
			if(t2.length() > 1){
				t2 = t2.replaceAll("\n", ", "); //replace newline characters with commas
				t2 = t2.substring(0, t2.length()-1); //remove comma at the end
				t2 = t2.toLowerCase();
				currentOrder = userID + delimiter + "FALSE" + delimiter + selectedSize + delimiter + selectedType + delimiter + t2 + "\n";
				previousOrders = previousOrders + currentOrder;
			}
			else{
				t2 = "";
				currentOrder = userID + delimiter + "FALSE" + delimiter + selectedSize + delimiter + selectedType + delimiter + t2 + "\n";
				previousOrders = previousOrders + currentOrder;
			}


			try {
				FileWriter r = new FileWriter(storageFileName);
				r.write(previousOrders);
				r.close();
				//System.out.println("Successfully wrote to the file.");
			} catch (IOException f) {
				//System.out.println("An error occurred.");
				f.printStackTrace();
			}
			//===
			window.setScene(statusScene);
			
			//setting up timeline for progress bar 
			timeSeconds.set((STARTTIME+1)*100);
            timeline = new Timeline();
            timeline.getKeyFrames().add(
            		new KeyFrame(Duration.seconds(STARTTIME+1),
                    new KeyValue(timeSeconds, 0)));
            timeline.playFromStart();
            
            statusTimeline = new Timeline();
            statusTimeline.getKeyFrames().add(
            		new KeyFrame(Duration.seconds(6), 
            		new KeyValue(orderStatus.textProperty(), "Ready to Cook")));
            statusTimeline.getKeyFrames().add(
            		new KeyFrame(Duration.seconds(11), 
            		new KeyValue(orderStatus.textProperty(), "Cooking")));
            statusTimeline.getKeyFrames().add(
            		new KeyFrame(Duration.seconds(16), 
            		new KeyValue(orderStatus.textProperty(), "Ready")));
            statusTimeline.playFromStart();
            
            
		}
		}
		
		
});

	vbox.getChildren().addAll(yourPizza, pizzaSize, pizzaType, pizzaToppings, hboxPrice, hboxDate, hboxTime, placeOrder);
return vbox;
}

}
