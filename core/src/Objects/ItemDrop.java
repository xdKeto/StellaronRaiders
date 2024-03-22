package Objects;

import com.badlogic.gdx.math.Rectangle;

public abstract class ItemDrop {
    public float x,y;
    public int width, height, effect;

    public Rectangle rectangle;

    public ItemDrop(float x, float y, int width, int height, int effect){
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.effect = effect;
    }

    public boolean overlaps (Rectangle rect){
        Rectangle thisRect = new Rectangle(x, y, width, height);
        return thisRect.overlaps(rect);
    }
}
