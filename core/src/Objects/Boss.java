package Objects;

import java.awt.*;

public class Boss extends SpaceShip{
    public static final int SPEED = 150;
    public Boss(float x, float y, int w, int h, int hp) {
        super(x, y, w, h, hp);
    }

    public int getSPEED() {
        return SPEED;
    }
}
