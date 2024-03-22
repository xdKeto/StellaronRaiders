package Objects;

public abstract class SpaceShip {
    public float x;
    public float y;
    public int shipHP;
    public int textureWIDTH;
    public int textureHEIGHT;

    public SpaceShip(float x, float y, int w, int h, int hp){
        this.x = x;
        this.y = y;
        this.textureWIDTH = w;
        this.textureHEIGHT = h;
        this.shipHP = hp;
    }
}
