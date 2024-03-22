package Objects;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;

public class Bullets extends FlyingObjects{
    public static final int SPEED = 1100;
    private static Texture texture;
    public boolean remove = false;
    public Rectangle hitBox;
    long lastBulletTime;

    public Bullets(float x, float y, int w, int h){
        super(x, y, w, h);
        this.hitBox = new Rectangle(x,y,w,h);
        this.lastBulletTime = 0L;
    }


    public Rectangle getHitBox(){
        return hitBox;
    }
    public void update (float deltaTime){
        x += SPEED * deltaTime;
        if(x > Gdx.graphics.getWidth()){
            remove = true;
        }
    }

    public void updateEnemy (float deltaTime){
        x -= SPEED * deltaTime;
        if(x > Gdx.graphics.getWidth()){
            remove = true;
        }
    }

    public Texture getTexture(){
        return texture = new Texture("Objects/red-laser.png");
    }

}