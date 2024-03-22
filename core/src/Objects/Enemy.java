package Objects;

import java.awt.*;

public class Enemy extends SpaceShip {
    public static final int SPEED = 150;
    private long lastBulletTime;
    private Rectangle enemyRect;
    public Enemy(float x, float y, int w, int h, int hp){
        super(x, y, w, h, hp);
        this.enemyRect = new Rectangle();
        this.lastBulletTime = 0L;
    }

    public int getSPEED() {
        return SPEED;
    }
}
