package Objects;

public class Player extends SpaceShip {
    public static final int SPEED = 500;

    public Player(float x, float y, int w, int h, int hp) {
        super(x, y, w, h, hp);
    }

    public int getSPEED(){
        return SPEED;
    }

}
