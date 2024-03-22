package Objects;

public class Asteroid extends FlyingObjects {
    public static final int SPEED = 350;

    public Asteroid(float x, float y, int w, int h) {
        super(x, y, w, h);

    }

    public int getSPEED(){
        return SPEED;
    }
}
