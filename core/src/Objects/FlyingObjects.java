package Objects;

import com.badlogic.gdx.math.Rectangle;

public abstract class FlyingObjects {
    public float x, y;
    public int width;
    public int height;

    public Rectangle rect;

    public FlyingObjects(float x, float y, int w, int h){
        this.x = x;
        this.y = y;
        this.width = w;
        this.height = h;
    }

    public boolean overlaps(Rectangle other) {
        Rectangle thisRect = new Rectangle(x, y, width, height);
        return thisRect.overlaps(other);
    }





}
