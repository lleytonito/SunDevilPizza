import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.text.Text;
import javafx.stage.Stage;
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

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.time.LocalDate;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.RowConstraints;
import javafx.scene.Group;


//Main class
//test - hari 11/6, 8:39
public class Main extends Application implements EventHandler<ActionEvent> {
	
	
	Stage window, LogInStage;
	public Scene homeScene, statusScene, orderScene, logScene;
	double progress = .25;
	ProgressBar orderProgress = new ProgressBar(progress);
	Label orderStatus = new Label("Preparing");
	int SCREEN_WIDTH = 800;
	int SCREEN_HEIGHT = 600;

	Text pizzaToppings;
	Text pizzaType;
	Text pizzaSize;
	Text subtitle3;

	ChoiceBox cbType;
	ChoiceBox cbSize;

	boolean selected = false;

	String sizes[] = { "Small", "Medium", "Large"};
	String selectedSize;
	String selectedType;
	String toppings;
	
	
	public static void main(String[] args) {
		
		launch(args);
	}
	
	//initialize screens
	public void start(Stage primaryStage) throws Exception {
		window = primaryStage;
		
		//BorderPane for home screens
		BorderPane homeRoot = new BorderPane();
		homeRoot.setPadding(new Insets(20));
		homeRoot.setBottom(homeHBox());
		homeRoot.setTop(homeVBox());
		homeRoot.setCenter(dealsHBox());
		homeScene = new Scene(homeRoot, 800, 600);
		
		//BorderPane for status screen
		BorderPane statusRoot = new BorderPane();
		statusRoot.setPadding(new Insets(20));
		statusRoot.setBottom(returnHBox());
		statusRoot.setTop(titleVBox());
		statusRoot.setCenter(statusHBox());
		statusScene = new Scene(statusRoot, 800, 600);
		
		//BorderPane for order screen
		VBox orderRoot = new VBox();
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
	gridPaneLogIn.setStyle("-fx-background-color: WHITE;");
	gridPaneLogIn.setPrefWidth(600);
	gridPaneLogIn.setPrefHeight(300);
	gridPaneLogIn.getColumnConstraints().add(new ColumnConstraints(50));
	gridPaneLogIn.getRowConstraints().add(new RowConstraints(50));

	Text asuId = new Text();
	Text warningId = new Text();
	asuId.setText("ASURITE ID: ");
	asuId.setFont(Font.font("Impact", 20));
	warningId.setText("Please Enter Your ASURITE ID");
	warningId.setFont(Font.font("Impact", 20));
	TextField logInIdText = new TextField();
	

	Button logInButton = new Button("Log In");
	logInButton.setOnAction(new EventHandler<ActionEvent>() {
		@Override public void handle(ActionEvent e) {
			String text = "";
			boolean numbersOnly;
			if (logInIdText.getText().isEmpty() == false) {
				text = logInIdText.getText();
				numbersOnly = text.chars().allMatch(Character::isDigit);
				if (numbersOnly == true)
				{
					if (text.length() < 10) {
						warningId.setText("             Not Enough Digits            ");
					}
					if (text.length() > 10) {
						warningId.setText("             Too Many Digits              ");
					}
					if (text.length() == 9) {
						warningId.setText("             Logging In ...              ");
						window.setScene(statusScene);
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
	
	return gridPaneLogIn;
}

//returnHBox used for the bottom of the status screen
public HBox returnHBox() {
		HBox hbox = new HBox();
		hbox.setAlignment(Pos.BOTTOM_RIGHT);
		
		//make a new order button set to return to home screen
		Button returnHome = new Button("Make a New Order");
		returnHome.setOnAction(e -> window.setScene(homeScene));
		
		//test button for progress bar, needs to be replaced with timer but currently directs to handle class
		Button test = new Button("Test Order Progress");
		test.setOnAction(this);

		Button orderDetails = new Button("View Order Details");
		orderDetails.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				String t2 = toppings.substring(10); //remove "toppings"
				t2 = t2.replaceAll("\n", ", "); //replace newline characters with commas
				t2 = t2.substring(0, t2.length()-1); //remove comma at the end
				t2 = t2.toLowerCase();
				subtitle3.setText("You ordered a " + selectedSize.toLowerCase() + " " + selectedType.toLowerCase() + " pizza with " + t2);
			}
		});

		hbox.getChildren().addAll(returnHome, test, orderDetails);
		
		return hbox;
}

//handling for the progressbar on status screen
public void handle(ActionEvent event) {
	if (progress < 1) {
			progress+=.25;
	orderProgress.setProgress(progress);
	}
	if (progress == .5) {
		orderStatus.setText("Ready To Cook");
	}
	if (progress == .75) {
		orderStatus.setText("Cooking");
	}
	if (progress == 1) {
		orderStatus.setText("Ready");
	}
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

	orderProgress.setPrefSize(300, 25);
	orderProgress.setStyle("-fx-accent: #8C1D40;");
	hbox.setAlignment(Pos.CENTER);
	hbox.getChildren().addAll(status, orderStatus, orderProgress);
	return hbox;
}

//dealsHBox used for graphic on home screen
public HBox dealsHBox() throws FileNotFoundException {
	HBox hbox = new HBox();
	FileInputStream inputstream = new FileInputStream("C:\\Users\\harir\\Desktop\\sparkyDeal.jpeg");
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
	  orderNow.setOnAction(e -> window.setScene(orderScene));
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

	CheckBox cbM = new CheckBox("Mushrooms");
	CheckBox cbOl = new CheckBox("Olives");
	CheckBox cbP = new CheckBox("Peppers");
	CheckBox cbOn = new CheckBox("Onions");
	CheckBox cbEx = new CheckBox("Extra Cheese");
	CheckBox cbPa = new CheckBox("Pineapples");
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
				pizzaType.setText("Type: " + selectedType);

				toppings = "Toppings:\n";
				if(cbM.isSelected()) {toppings = toppings + "Mushrooms\n";}
				if(cbOl.isSelected()) {toppings = toppings + "Olives\n";}
				if(cbP.isSelected()) {toppings = toppings + "Peppers\n";}
				if(cbOn.isSelected()) {toppings = toppings + "Onions\n";}
				if(cbEx.isSelected()) {toppings = toppings + "Extra cheese\n";}
				if(cbPa.isSelected()) {toppings = toppings + "Pineapples";}
			
				pizzaToppings.setText(toppings);
				selected = true;
			}
	});

 	vbox.getChildren().add(hboxSize);
	vbox.getChildren().add(hboxType);
	vbox.getChildren().add(gridpane);
	vbox.getChildren().add(select);
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
	pizzaToppings = new Text("Toppings: ");

	final DatePicker datePicker = new DatePicker();
	//datePicker.setOnAction(new EventHandler() {
 		//public void handle(Event t) {
 			//LocalDate date = datePicker.getValue();
 			//System.err.println("Selected date: " + date);
			//}
	//});

	TextField enterTime = new TextField("Pickup Time");

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
		else if(enterTime.getText().equals("Pickup Time")){
			a.setAlertType(AlertType.ERROR);
        	a.setContentText("enter a pickup time");
        	a.show();
		}
		else if(date == null){
			a.setAlertType(AlertType.ERROR);
        			a.setContentText("select a date");
      			        a.show();
		}
		else {
			window.setScene(logScene);
		}
		}
});

	vbox.getChildren().addAll(yourPizza, pizzaSize, pizzaType, pizzaToppings, datePicker, enterTime, placeOrder);
return vbox;
}
}
