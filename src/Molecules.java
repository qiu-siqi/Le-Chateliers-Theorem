import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;

/**
 * Created by Catz on 10/6/14.
 */
class Molecules extends Circle {

    private double velocityX;
    private double velocityY;

    Molecules(Color color){
        setFill(color);
        setRadius(5);
        velocityX = Math.random()*400-200;
        velocityY = Math.random()*400-200;
    }

    private void separateFrom(Molecules other){
        while (isTouching(other)){
            if (getTranslateX() > other.getTranslateX()){
                setTranslateX(getTranslateX()+1);
                other.setTranslateX(other.getTranslateX()-1);
                setTranslateY(getTranslateY()+1);
                other.setTranslateY(other.getTranslateY()-1);
            } else {
                setTranslateX(getTranslateX()-1);
                other.setTranslateX(other.getTranslateX()+1);
                setTranslateY(getTranslateY()-1);
                other.setTranslateY(other.getTranslateY()+1);
            }
        }
    }

    boolean isTouching(Molecules other){
        return (Math.pow(getTranslateX()-other.getTranslateX(),2)+Math.pow(getTranslateY()-other.getTranslateY(),2) < 100);
    }

    void collide(Molecules other){
        separateFrom(other);
        double ux = velocityX;
        double uy = velocityY;
        velocityX = other.velocityX;
        velocityY = other.velocityY;
        other.velocityX = ux;
        other.velocityY = uy;
    }

    double getVelocityX() {
        return velocityX;
    }

    double getVelocityY() {
        return velocityY;
    }

    void reflectX() {
        velocityX *= -1;
    }

    void reflectY() {
        velocityY *= -1;
    }
}
