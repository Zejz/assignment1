void move() {
        x += Math.cos(Math.toRadians(direction)) * currentSpeed;
        y += Math.sin(Math.toRadians(direction)) * currentSpeed;
    }
void turnLeft() {
        direction += 90;
        if direction >= 360;{
        direction -= 360;}
    }
void turnRight() {
        direction -= 90;
        if direction <= 0;{
        direction += 360;}
    }