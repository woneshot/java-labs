public class Car {
        private final double position;
        private final double speed;

        Car(double position, double speed) {
            this.position = position >= 0 ? position : 0;
            this.speed = speed >= 0 ? speed : 0.1;
        }

    public double getPosition() {
        return position;
    }

    public double getSpeed() {
        return speed;
    }

    @Override
    public String toString() {
        return "Car{" +
                "position=" + position +
                ", speed=" + speed +
                '}';
    }
}
