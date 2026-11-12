import java.awt.Color;

public abstract class Car implements Movable {
    private final int nrDoors;
    private final double enginePower;
    private double currentSpeed;
    private Color color;
    private final String modelName;

    private double x;
    private double y;
    private int direction;

    public Car(int nrDoors, double enginePower, Color color, String modelName) {
        this.nrDoors = nrDoors;
        this.enginePower = enginePower;
        this.color = color;
        this.modelName = modelName;
        stopEngine();
    }

    public int getNrDoors() {
        return nrDoors;
    }

    public double getEnginePower() {
        return enginePower;
    }

    public double getCurrentSpeed() {
        return currentSpeed;
    }

    public Color getColor() {
        return color;
    }

    public void setColor(Color color) {
        this.color = color;
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    public int getDirection() {
        return direction;
    }

    public void startEngine() {
        currentSpeed = 0.1;
    }

    public void stopEngine() {
        currentSpeed = 0;
    }

    protected abstract double speedFactor();

     protected void incrementSpeed(double amount) {
        currentSpeed += (amount > 0 && amount <=1) ? speedFactor() * amount : 0;
        if (currentSpeed > enginePower) {
            currentSpeed = enginePower;
        }
    }

    protected void decrementSpeed(double amount) {
        currentSpeed -= (amount > 0 && amount <=1) ? speedFactor() * amount : 0;
        if (currentSpeed < 0) {
            currentSpeed = 0;
        }
    }

    public void gas(double amount) {
        incrementSpeed(amount);
    }

    public void brake(double amount) {
        decrementSpeed(amount);
    }

    @Override 
    public void move(){
        switch (direction) {
            case 0 -> x += currentSpeed;
            case 90 -> y += currentSpeed;
            case 180 -> x -= currentSpeed;
            case 270 -> y -= currentSpeed;
            default -> {
                x += Math.cos(Math.toRadians(direction)) * currentSpeed;
                y += Math.sin(Math.toRadians(direction)) * currentSpeed;
            }}}

    @Override 
    public void turnLeft(){
        direction += 90;
        if (direction >= 360) {
            direction -= 360;
        }
    } 
    
    @Override
    public void turnRight(){
        direction -= 90;
        if (direction < 0) {
            direction += 360;
        }
    }
    
}
