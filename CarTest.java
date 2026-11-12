import java.awt.Color;

public class CarTest {
    private static final double EPS = 1e-9;

    private static void reportResult(String testName, boolean success) {
        System.out.println((success ? "SUCCESS: " : "FAILURE: ") + testName);
    }

    private static boolean close(double a, double b) {
        return Math.abs(a - b) < EPS;
    }

    private static void testInitialState() {
        Car[] cars = {new Volvo240(), new Saab95()};
        boolean ok = true;
        for (Car car : cars) {
            ok &= car.getCurrentSpeed() == 0;
            ok &= car.getX() == 0 && car.getY() == 0;
            ok &= car.getDirection() == 0;
        }
        reportResult("New cars stand still at the origin", ok);
    }

    private static void testCarSpecs() {
        Volvo240 volvo = new Volvo240();
        Saab95 saab = new Saab95();
        boolean ok = volvo.getNrDoors() == 4
                && close(volvo.getEnginePower(), 100)
                && volvo.getColor().equals(Color.black)
                && saab.getNrDoors() == 2
                && close(saab.getEnginePower(), 125)
                && saab.getColor().equals(Color.red);
        reportResult("Cars have the specified doors, power and color", ok);
    }

    private static void testSetColor() {
        Volvo240 volvo = new Volvo240();
        volvo.setColor(Color.blue);
        reportResult("setColor changes color", volvo.getColor().equals(Color.blue));
    }

    private static void testStartStopEngine() {
        Volvo240 volvo = new Volvo240();
        volvo.startEngine();
        boolean started = close(volvo.getCurrentSpeed(), 0.1);
        volvo.stopEngine();
        reportResult("startEngine gives speed 0.1, stopEngine gives 0",
                started && volvo.getCurrentSpeed() == 0);
    }

    private static void testVolvoGasAmount() {
        Volvo240 volvo = new Volvo240();
        volvo.gas(0.5);
        reportResult("Volvo240 gas(0.5) from standstill gives speed 0.625",
                close(volvo.getCurrentSpeed(), 0.625));
    }

    private static void testSaabGasAmount() {
        Saab95 saab = new Saab95();
        saab.setTurboOff();
        saab.gas(0.5);
        reportResult("Saab95 gas(0.5) without turbo gives speed 0.625",
                close(saab.getCurrentSpeed(), 0.625));
    }

    private static void testTurbo() {
        Saab95 slowSaab = new Saab95();
        Saab95 fastSaab = new Saab95();
        fastSaab.setTurboOn();
        slowSaab.setTurboOff();
        fastSaab.gas(0.5);
        slowSaab.gas(0.5);
        reportResult("Turbo yields more speed",
                slowSaab.getCurrentSpeed() < fastSaab.getCurrentSpeed());
    }

    private static void testTurboFactor() {
        Saab95 fastSaab = new Saab95();
        Saab95 slowSaab = new Saab95();
        fastSaab.setTurboOn();
        fastSaab.gas(1);
        slowSaab.gas(1);
        reportResult("Turbo multiplies speed gain by 1.3",
                close(fastSaab.getCurrentSpeed() / slowSaab.getCurrentSpeed(), 1.3));
    }

    private static void testTurboOff() {
        Saab95 turboSaab = new Saab95();
        Saab95 regularSaab = new Saab95();
        turboSaab.setTurboOn();
        turboSaab.setTurboOff();
        turboSaab.gas(1);
        regularSaab.gas(1);
        reportResult("Turning turbo off restores normal acceleration",
                close(turboSaab.getCurrentSpeed(), regularSaab.getCurrentSpeed()));
    }

    private static void testGasIncreasesSpeed() {
        Volvo240 volvo = new Volvo240();
        volvo.startEngine();
        double before = volvo.getCurrentSpeed();
        volvo.gas(0.3);
        reportResult("gas increases speed", volvo.getCurrentSpeed() > before);
    }

    private static void testBrakeDecreasesSpeed() {
        Saab95 saab = new Saab95();
        saab.gas(1);
        double before = saab.getCurrentSpeed();
        saab.brake(0.2);
        reportResult("brake decreases speed", saab.getCurrentSpeed() < before);
    }

    private static void testMoveEastByDefault() {
        Volvo240 volvo = new Volvo240();
        volvo.startEngine();
        volvo.move();
        reportResult("move goes +x initially, by currentSpeed",
                close(volvo.getX(), 0.1) && close(volvo.getY(), 0));
    }

    private static void testMoveWithZeroSpeed() {
        Saab95 saab = new Saab95();
        saab.move();
        reportResult("move at speed 0 does not change position",
                saab.getX() == 0 && saab.getY() == 0);
    }

    private static void testTurnLeftThenMove() {
        Volvo240 volvo = new Volvo240();
        volvo.startEngine();
        volvo.turnLeft();
        volvo.move();
        reportResult("After turnLeft, move goes +y",
                close(volvo.getX(), 0) && close(volvo.getY(), 0.1));
    }

    private static void testTurnRightThenMove() {
        Volvo240 volvo = new Volvo240();
        volvo.startEngine();
        volvo.turnRight();
        volvo.move();
        reportResult("After turnRight, move goes -y",
                close(volvo.getX(), 0) && close(volvo.getY(), -0.1));
    }

    private static void testTurnTwiceMovesBackwards() {
        Saab95 saab = new Saab95();
        saab.startEngine();
        saab.turnLeft();
        saab.turnLeft();
        saab.move();
        reportResult("Two left turns then move goes -x",
                close(saab.getX(), -0.1) && close(saab.getY(), 0));
    }

    private static void testFourTurnsFullCircle() {
        Volvo240 volvo = new Volvo240();
        int start = volvo.getDirection();
        for (int i = 0; i < 4; i++) {
            volvo.turnLeft();
        }
        boolean leftTurnsRestoreDirection = volvo.getDirection() == start;
        for (int i = 0; i < 4; i++) {
            volvo.turnRight();
        }
        reportResult("Four turns in the same direction restore the heading",
                leftTurnsRestoreDirection && volvo.getDirection() == start);
    }

    private static void testLeftThenRightCancels() {
        Volvo240 volvo = new Volvo240();
        volvo.turnLeft();
        volvo.turnRight();
        reportResult("turnLeft followed by turnRight cancels out",
                volvo.getDirection() == 0);
    }

    private static void testSquarePath() {
        Saab95 saab = new Saab95();
        saab.startEngine();
        for (int i = 0; i < 4; i++) {
            saab.move();
            saab.turnLeft();
        }
        reportResult("Driving a square returns to the start",
                close(saab.getX(), 0) && close(saab.getY(), 0));
    }

    public static void main(String[] args) {
        testInitialState();
        testCarSpecs();
        testSetColor();
        testStartStopEngine();
        testVolvoGasAmount();
        testSaabGasAmount();
        testTurbo();
        testTurboFactor();
        testTurboOff();
        testGasIncreasesSpeed();
        testBrakeDecreasesSpeed();
        testMoveEastByDefault();
        testMoveWithZeroSpeed();
        testTurnLeftThenMove();
        testTurnRightThenMove();
        testTurnTwiceMovesBackwards();
        testFourTurnsFullCircle();
        testLeftThenRightCancels();
        testSquarePath();
    }
}
