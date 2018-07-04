import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import javax.swing.*;

/**
 * Created by Catz on 10/6/14.
 */
public class Main extends Application {

    private Button addReactants;
    private Button addProducts;
    private Button editReaction;
    private Button toggleCatalyst;

    private TextField addRedText, addBlueText, addYellowText, addGreenText, setKpText;

    private Slider setTemperature, setVolume;

    private RadioButton selectExo;

    private CheckBox redCheck, blueCheck, yellowCheck, greenCheck;

    private Text enthalpyText,KpText;

    private Controller controller;

    @Override
    public void start(final Stage primaryStage) {
        BorderPane root = new BorderPane();
        primaryStage.setTitle("Hello World");

        VBox right = new VBox();
        right.setPadding(new Insets(20, 20, 20, 20));

        GridPane grid = new GridPane();
        grid.setPadding(new Insets(10,10,10,10));
        grid.setVgap(10);
        grid.setHgap(50);
        grid.setStyle("-fx-border-color: black;-fx-border-width: 1px;");
        grid.add(new Text("Molecules:"),0,0);
        Color color[] = {Color.RED,Color.BLUE,Color.YELLOW,Color.GREEN};
        Text[] particleNo = new Text[4];
        for (int i = 0; i < 2; i++){
            HBox temp = new HBox();
            temp.getChildren().add(new Circle(10,color[i]));
            particleNo[i] = new Text("50");
            temp.getChildren().add(particleNo[i]);
            grid.add(temp,i,1);
        }
        for (int i = 2; i < 4; i++){
            HBox temp = new HBox();
            temp.getChildren().add(new Circle(10,color[i]));
            particleNo[i] = new Text("0");
            temp.getChildren().add(particleNo[i]);
            grid.add(temp,i-2,2);
        }
        Button removeAllMolecules = new Button("Remove all molecules");
        removeAllMolecules.setOnAction(actionEvent -> controller.removeAllMolecules());
        grid.add(removeAllMolecules,0,3);
        right.getChildren().add(grid);

        GridPane grid2 = new GridPane();
        grid2.setPadding(new Insets(10,10,10,10));
        grid2.setVgap(10);
        grid2.setHgap(10);
        grid2.setStyle("-fx-border-color: black;-fx-border-width: 1px;");
        grid2.add(new Text("Temperature:"), 0, 0);
        setTemperature = new Slider();
        setTemperature.setMin(200);
        setTemperature.setMax(1000);
        setTemperature.setValue(298);
        setTemperature.valueProperty().addListener((observableValue, number, number2) -> {
            double Kp = controller.setTemperature(setTemperature.getValue());
            KpText.setText("Kp = "+String.format("%.1f",Kp));
        });
        grid2.add(setTemperature, 1, 0);
        grid2.add(new Text("Volume:"),0,1);
        setVolume = new Slider();
        setVolume.setMin(15);
        setVolume.setMax(100);
        setVolume.setValue(100);
        setVolume.valueProperty().addListener(
                (observableValue, number, number2) -> controller.setVolume(setVolume.getValue()));
        grid2.add(setVolume,1,1);
        grid2.add(new Text("Catalyst:"),0,2);
        toggleCatalyst = new Button("Toggle Catalyst On");
        toggleCatalyst.setOnAction(actionEvent -> {
            controller.toggleCatalyst();
            if (toggleCatalyst.getText().equals("Toggle Catalyst On")){
                toggleCatalyst.setText("Toggle Catalyst Off");
            } else if (toggleCatalyst.getText().equals("Toggle Catalyst Off")){
                toggleCatalyst.setText("Toggle Catalyst On");
            }
        });
        grid2.add(toggleCatalyst,1,2);
        right.getChildren().add(grid2);

        VBox box = new VBox();
        box.setPadding(new Insets(10,10,10,10));
        box.setStyle("-fx-border-color: black;-fx-border-width: 1px;");
        box.setSpacing(10);
        final HBox temp = new HBox();
        temp.getChildren().add(new Circle(10,Color.RED));
        temp.getChildren().add(new Text(" + "));
        temp.getChildren().add(new Circle(10,Color.BLUE));
        temp.getChildren().add(new Text(" ⇌ "));
        temp.getChildren().add(new Circle(10,Color.YELLOW));
        temp.getChildren().add(new Text(" + "));
        temp.getChildren().add(new Circle(10,Color.GREEN));
        box.getChildren().add(temp);
        enthalpyText = new Text("ΔH < 0");
        box.getChildren().add(enthalpyText);
        KpText = new Text("Kp = 20");
        box.getChildren().add(KpText);
        right.getChildren().add(box);
        final VBox box1 = new VBox();
        final HBox addReactantBox = new HBox();
        addReactantBox.setStyle("-fx-border-color: black;-fx-border-width: 1px;");
        addReactantBox.setSpacing(5);
        addReactantBox.setPadding(new Insets(10, 10, 10, 10));
        addReactantBox.getChildren().add(new Circle(10, Color.RED));
        addRedText = new TextField("0");
        addRedText.setPrefWidth(50);
        addReactantBox.getChildren().add(addRedText);
        addReactantBox.getChildren().add(new Circle(10,Color.BLUE));
        addBlueText = new TextField("0");
        addBlueText.setPrefWidth(50);
        addReactantBox.getChildren().add(addBlueText);
        Button confirmAddReactants = new Button("Confirm");
        confirmAddReactants.setOnAction(actionEvent -> {
            int red = Integer.parseInt(addRedText.getText());
            int blue = Integer.parseInt(addBlueText.getText());
            if (red > 100) red = 100;
            if (blue > 100) blue = 100;
            controller.addMolecules(Color.RED,red);
            controller.addMolecules(Color.BLUE,blue);
        });
        addReactantBox.getChildren().add(confirmAddReactants);
        addReactants = new Button("Add more reactants");
        addReactants.setOnAction(actionEvent -> {
            if (addReactants.getText().equals("Add more reactants")){
                box1.getChildren().add(addReactantBox);
                addReactants.setText("Cancel");
            } else {
                box1.getChildren().remove(addReactantBox);
                addReactants.setText("Add more reactants");
            }
        });
        box1.getChildren().add(addReactants);
        box.getChildren().add(box1);
        final VBox box2 = new VBox();
        final HBox addProductBox = new HBox();
        addProductBox.setStyle("-fx-border-color: black;-fx-border-width: 1px;");
        addProductBox.setSpacing(5);
        addProductBox.setPadding(new Insets(10, 10, 10, 10));
        addProductBox.getChildren().add(new Circle(10, Color.YELLOW));
        addYellowText = new TextField("0");
        addYellowText.setPrefWidth(50);
        addProductBox.getChildren().add(addYellowText);
        addProductBox.getChildren().add(new Circle(10,Color.GREEN));
        addGreenText = new TextField("0");
        addGreenText.setPrefWidth(50);
        addProductBox.getChildren().add(addGreenText);
        Button confrimAddProducts = new Button("Confirm");
        confrimAddProducts.setOnAction(actionEvent -> {
            int yellow = Integer.parseInt(addYellowText.getText());
            int green = Integer.parseInt(addGreenText.getText());
            if (yellow > 100) yellow = 100;
            if (green > 100) green = 100;
            controller.addMolecules(Color.YELLOW,yellow);
            controller.addMolecules(Color.GREEN,green);
        });
        addProductBox.getChildren().add(confrimAddProducts);
        addProducts = new Button("Add more products");
        addProducts.setOnAction(actionEvent -> {
            if (addProducts.getText().equals("Add more products")){
                box2.getChildren().add(addProductBox);
                addProducts.setText("Cancel");
            } else {
                box2.getChildren().remove(addProductBox);
                addProducts.setText("Add more products");
            }
        });
        box2.getChildren().add(addProducts);
        box.getChildren().add(box2);
        final VBox box3 = new VBox();
        final VBox editReactionBox = new VBox();
        editReactionBox.setStyle("-fx-border-color: black;-fx-border-width: 1px;");
        editReactionBox.setSpacing(5);
        editReactionBox.setPadding(new Insets(10,10,10,10));
        editReactionBox.getChildren().add(new Text("Reactants:"));
        HBox box5 = new HBox();
        box5.setSpacing(5);
        box5.setStyle("-fx-border-color: black;-fx-border-width: 1px;");
        redCheck = new CheckBox();
        redCheck.setSelected(true);
        blueCheck = new CheckBox();
        blueCheck.setSelected(true);
        box5.getChildren().add(new Circle(10,Color.RED));
        box5.getChildren().add(redCheck);
        box5.getChildren().add(new Circle(10,Color.BLUE));
        box5.getChildren().add(blueCheck);
        editReactionBox.getChildren().add(box5);
        editReactionBox.getChildren().add(new Text("Products:"));
        HBox box6 = new HBox();
        box6.setSpacing(5);
        box6.setStyle("-fx-border-color: black;-fx-border-width: 1px;");
        yellowCheck = new CheckBox();
        yellowCheck.setSelected(true);
        greenCheck = new CheckBox();
        greenCheck.setSelected(true);
        box6.getChildren().add(new Circle(10,Color.YELLOW));
        box6.getChildren().add(yellowCheck);
        box6.getChildren().add(new Circle(10,Color.GREEN));
        box6.getChildren().add(greenCheck);
        editReactionBox.getChildren().add(box6);
        editReactionBox.getChildren().add(new Text("Enthalpy change:"));
        VBox box4 = new VBox();
        box4.setStyle("-fx-border-color: black;-fx-border-width: 1px;");
        ToggleGroup enthalpy1 = new ToggleGroup();
        selectExo = new RadioButton("Exothermic");
        selectExo.setSelected(true);
        selectExo.setToggleGroup(enthalpy1);
        RadioButton selectEndo = new RadioButton("Endothermic");
        selectEndo.setToggleGroup(enthalpy1);
        box4.getChildren().addAll(selectExo, selectEndo);
        editReactionBox.getChildren().add(box4);
        HBox temp2 = new HBox();
        temp2.setSpacing(5);
        temp2.getChildren().add(new Text("Kp:"));
        setKpText = new TextField();
        setKpText.setPrefWidth(50);
        setKpText.setText("20");
        temp2.getChildren().add(setKpText);
        editReactionBox.getChildren().add(temp2);
        Button confirmEditReaction = new Button("Change");
        confirmEditReaction.setOnAction(actionEvent -> {
            if (!redCheck.isSelected() && !blueCheck.isSelected()){
                controller.pauseSimulation();
                JOptionPane.showMessageDialog(null, "Need at least one reactant!", "Error", JOptionPane.ERROR_MESSAGE);
                controller.playSimulation();
            } else if (!yellowCheck.isSelected() && !greenCheck.isSelected()){
                controller.pauseSimulation();
                JOptionPane.showMessageDialog(null, "Need at least one product!", "Error", JOptionPane.ERROR_MESSAGE);
                controller.playSimulation();
            } else {
                double reactionKp = Double.parseDouble(setKpText.getText());
                if (reactionKp > 100) reactionKp = 100;
                int product, reactant;
                temp.getChildren().clear();
                if (redCheck.isSelected() && blueCheck.isSelected()){
                    reactant = 12;
                    temp.getChildren().add(new Circle(10,Color.RED));
                    temp.getChildren().add(new Text(" + "));
                    temp.getChildren().add(new Circle(10,Color.BLUE));
                } else if (redCheck.isSelected()){
                    reactant = 1;
                    temp.getChildren().add(new Circle(10,Color.RED));
                } else {
                    reactant = 2;
                    temp.getChildren().add(new Circle(10,Color.BLUE));
                }
                temp.getChildren().add(new Text(" ⇌ "));
                if (yellowCheck.isSelected() && greenCheck.isSelected()){
                    product = 34;
                    temp.getChildren().add(new Circle(10,Color.YELLOW));
                    temp.getChildren().add(new Text(" + "));
                    temp.getChildren().add(new Circle(10,Color.GREEN));
                } else if (yellowCheck.isSelected()){
                    product = 3;
                    temp.getChildren().add(new Circle(10,Color.YELLOW));
                } else {
                    product = 4;
                    temp.getChildren().add(new Circle(10,Color.GREEN));
                }
                int enthalpy;
                if (selectExo.isSelected()){
                    enthalpy = -1;
                    enthalpyText.setText("ΔH < 0");
                } else {
                    enthalpy = 1;
                    enthalpyText.setText("ΔH > 0");
                }
                KpText.setText("Kp = "+(int)reactionKp);
                controller.changeReaction(reactant,product,enthalpy,reactionKp);
            }
        });
        editReactionBox.getChildren().add(confirmEditReaction);
        editReaction = new Button("Edit reaction");
        editReaction.setOnAction(actionEvent -> {
            if (editReaction.getText().equals("Edit reaction")){
                box3.getChildren().add(editReactionBox);
                editReaction.setText("Cancel");
            } else {
                box3.getChildren().remove(editReactionBox);
                editReaction.setText("Edit reaction");
            }
        });
        box3.getChildren().add(editReaction);
        box.getChildren().add(box3);

        root.setRight(right);

        controller = new Controller();
        controller.setToUpdate(particleNo[0], particleNo[1], particleNo[2], particleNo[3]);
        controller.initializeSimulation();
        root.setCenter(controller.getSimulationArea());

        Scene scene = new Scene(root);
        scene.getStylesheets().add("stylesheet.css");
        primaryStage.setScene(scene);
        primaryStage.sizeToScene();
        primaryStage.setResizable(false);
        primaryStage.setTitle("Le Chatelier's Principle");
        primaryStage.show();
    }


    public static void main(String[] args) {
        launch(args);
    }
}
