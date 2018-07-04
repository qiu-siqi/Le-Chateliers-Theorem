import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.util.Duration;

import java.util.ArrayList;

/**
 * Created by Catz on 10/6/14.
 */
class Controller {

    private int height, width;
    private int reactants;
    private int products;
    private int enthalpy; // -1: exo 1: endo
    private double temperature;
    private double reactionKp;
    private boolean catalyst;
    private Text countRed, countBlue, countYellow, countGreen;
    private StackPane simulationArea;
    private Timeline timeline;
    private ArrayList<Molecules> moleculeList;

    Controller(){
        height = 1000;
        width = 1500;
        reactants = 12;
        products = 34;
        temperature = 298;
        reactionKp = 20;
        enthalpy = -1;
        catalyst = false;
        moleculeList = new ArrayList<>();
        simulationArea = new StackPane();
        simulationArea.setStyle("-fx-border-color: black;-fx-border-width: 1px;");
    }

    void initializeSimulation(){
        simulationArea.setPrefWidth(width);
        simulationArea.setPrefHeight(height);
        Color colors[] = {Color.RED, Color.BLUE};
        for (int i = 0; i < 2; i++){
            for (int j = 0; j < 50; j++){
                addMolecule(colors[i]);
            }
        }
        timeline = new Timeline();
        timeline.setCycleCount(Timeline.INDEFINITE);

        KeyFrame keyFrame = new KeyFrame(Duration.millis(20), actionEvent -> {
            updateParticles();
            checkCollisions();
            countMolecules();
        });
        timeline.getKeyFrames().add(keyFrame);
        timeline.play();
    }

    private void addMolecule(Color color){
        Molecules newMolecule= new Molecules(color);
        newMolecule.setTranslateX(Math.random()*(width-10)-(width-10)/2);
        newMolecule.setTranslateY(Math.random()*(height-10)-(height-10)/2);
        simulationArea.getChildren().add(newMolecule);
        moleculeList.add(newMolecule);
    }

    private void addMoleculeCloseTo(Color color, Molecules m){
        Molecules newMolecule = new Molecules(color);
        int direction1,direction2;
        if (Math.random()*2-1>0){
            direction1 = 1;
        } else {
            direction1 = -1;
        }
        if (Math.random()*2-1>0){
            direction2 = 1;
        } else {
            direction2 = -1;
        }
        newMolecule.setTranslateX(m.getTranslateX()+direction1*(Math.random()*15+5));
        newMolecule.setTranslateY(m.getTranslateY()+direction2*(Math.random()*15+5));
        simulationArea.getChildren().add(newMolecule);
        moleculeList.add(newMolecule);
    }

    void addMolecules(Color color, int i){
        for (int j = 0; j < i; j++){
            addMolecule(color);
        }
    }

    void setToUpdate(Text t1, Text t2, Text t3, Text t4){
        countRed = t1;
        countBlue = t2;
        countYellow = t3;
        countGreen = t4;
    }

    private void updateParticles(){
        for (Molecules molecule : moleculeList) {
            molecule.setTranslateX(molecule.getTranslateX() + molecule.getVelocityX() / 60 * Math.sqrt(temperature) / 18);
            molecule.setTranslateY(molecule.getTranslateY() + molecule.getVelocityY() / 60 * Math.sqrt(temperature) / 18);
        }
    }

    private void checkCollisions(){
        for (int i = 0; i < moleculeList.size();i++){
            react(moleculeList.get(i));
            for (int j = i+1; j < moleculeList.size(); j++){
                if (i==j)continue;
                if (moleculeList.get(i).isTouching(moleculeList.get(j))){
                    moleculeList.get(i).collide(moleculeList.get(j));
                    react(moleculeList.get(i),moleculeList.get(j));
                }
            }
        }

        for (Molecules molecule : moleculeList) {
            if ((molecule.getTranslateX() >= width / 2 - 5 && molecule.getVelocityX() > 0) ||
                    (molecule.getTranslateX() <= -width / 2 + 5 && molecule.getVelocityX() < 0)) {
                molecule.reflectX();
            }
            if ((molecule.getTranslateY() >= height / 2 - 5 && molecule.getVelocityY() > 0) ||
                    (molecule.getTranslateY() <= -height / 2 + 5 && molecule.getVelocityY() < 0)) {
                molecule.reflectY();
            }
        }
    }

    private void react(Molecules m){
        if (reactants == 1 && m.getFill() == Color.RED){
            boolean state = true;
            if (!catalyst){
                if (Math.random()*2 < 1){
                    state = false;
                }
            }
            updateYellowAndGreenMolecules(m, state);
        }
        if (reactants == 2 && m.getFill() == Color.BLUE){
            boolean state = true;
            if (!catalyst){
                if (Math.random()*2 < 1){
                    state = false;
                }
            }
            updateYellowAndGreenMolecules(m, state);
        }
        if (products == 3 && m.getFill() == Color.YELLOW){
            boolean state = true;
            if (!catalyst){
                if (Math.random()*2 < 1){
                    state = false;
                }
            }
            updateRedAndBlueMolecules(m, state);
        }
        if (products == 4 && m.getFill() == Color.GREEN){
            boolean state = true;
            if (!catalyst){
                if (Math.random()*2 < 1){
                    state = false;
                }
            }
            updateRedAndBlueMolecules(m, state);
        }
    }

    private void updateRedAndBlueMolecules(Molecules m, boolean state) {
        if (Math.random()*500 > reactionKp && state){
            if (reactants == 12){
                m.setFill(Color.RED);
                addMoleculeCloseTo(Color.BLUE,m);
            } else if (reactants == 1){
                m.setFill(Color.RED);
            } else if (reactants == 2){
                m.setFill(Color.BLUE);
            }
        }
    }

    private void updateYellowAndGreenMolecules(Molecules m, boolean state) {
        if (Math.random()*1000 < reactionKp && state){
            if (products == 34){
                m.setFill(Color.YELLOW);
                addMoleculeCloseTo(Color.GREEN,m);
            } else if (products == 3){
                m.setFill(Color.YELLOW);
            } else if (products == 4){
                m.setFill(Color.GREEN);
            }
        }
    }

    private void react(Molecules m1, Molecules m2){
        if (reactants == 12){
            if ((m1.getFill() == Color.RED && m2.getFill() == Color.BLUE) || (m1.getFill() == Color.BLUE && m2.getFill() == Color.RED)){
                boolean state = true;
                if (!catalyst){
                    if (Math.random()*2 < 1){
                        state = false;
                    }
                }
                if (Math.random()*100 < reactionKp && state){
                    if (products == 34){
                        m1.setFill(Color.YELLOW);
                        m2.setFill(Color.GREEN);
                    } else if (products == 3){
                        m1.setFill(Color.YELLOW);
                        moleculeList.remove(m2);
                        simulationArea.getChildren().remove(m2);
                    } else if (products == 4){
                        m1.setFill(Color.GREEN);
                        moleculeList.remove(m2);
                        simulationArea.getChildren().remove(m2);
                    }
                }
            }
        }
        if (products == 34){
            if ((m1.getFill() == Color.YELLOW && m2.getFill() == Color.GREEN) || (m1.getFill() == Color.GREEN && m2.getFill() == Color.YELLOW)){
                boolean state = true;
                if (!catalyst){
                    if (Math.random()*2 < 1){
                        state = false;
                    }
                }
                if (Math.random()*100 > reactionKp && state){
                    if (reactants == 12){
                        m1.setFill(Color.RED);
                        m2.setFill(Color.BLUE);
                    } else if (reactants == 1){
                        m1.setFill(Color.RED);
                        moleculeList.remove(m2);
                        simulationArea.getChildren().remove(m2);
                    } else if (reactants == 2){
                        m1.setFill(Color.BLUE);
                        moleculeList.remove(m2);
                        simulationArea.getChildren().remove(m2);
                    }
                }
            }
        }

    }

    private void countMolecules(){
        int[] colors = {0,0,0,0};
        for (Molecules molecule : moleculeList) {
            if (molecule.getFill() == Color.RED) {
                colors[0]++;
            } else if (molecule.getFill() == Color.BLUE) {
                colors[1]++;
            } else if (molecule.getFill() == Color.YELLOW) {
                colors[2]++;
            } else {
                colors[3]++;
            }
        }
        countRed.setText(""+colors[0]);
        countBlue.setText(""+colors[1]);
        countYellow.setText(""+colors[2]);
        countGreen.setText(""+colors[3]);
    }

    StackPane getSimulationArea(){
        return simulationArea;
    }

    void pauseSimulation(){
        timeline.pause();
    }

    void playSimulation(){
        timeline.play();
    }

    void setVolume(double volume){
        height = (int) (volume/100*1000);
        width = (int) (volume/100*1500);
        simulationArea.setMinHeight(height);
        simulationArea.setMaxHeight(height);
        simulationArea.setPrefHeight(height);
        simulationArea.setMinWidth(width);
        simulationArea.setMaxWidth(width);
        simulationArea.setPrefWidth(width);

        for (Molecules molecule : moleculeList) {
            if (molecule.getTranslateX() <= -width / 2 || molecule.getTranslateX() >= width / 2) {
                molecule.setTranslateX(Math.random() * (width - 10) - (width - 10) / 2);
            }
            if (molecule.getTranslateY() <= -height / 2 || molecule.getTranslateY() >= height / 2) {
                molecule.setTranslateY(Math.random() * (height - 10) - (height - 10) / 2);
            }
        }
    }

    double setTemperature(double temperature){
        double previousTemp = this.temperature;
        this.temperature = temperature;
        reactionKp = Math.pow(Math.E,Math.log(reactionKp) - enthalpy*700*(1/temperature-1/previousTemp));

        return reactionKp;
    }

    void changeReaction(int reactants, int products, int enthalpy, double reactionKp){
        this.reactants = reactants;
        this.products = products;
        this.enthalpy = enthalpy;
        this.reactionKp = reactionKp;
    }

    void toggleCatalyst(){
        catalyst = !catalyst;
        if (catalyst){
            simulationArea.setStyle("-fx-background-color: peachpuff");
        } else {
            simulationArea.setStyle("-fx-background-color: white");
        }
    }

    void removeAllMolecules(){
        moleculeList = new ArrayList<>();
        simulationArea.getChildren().clear();
    }
}
