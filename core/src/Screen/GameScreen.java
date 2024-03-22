package Screen;

import Objects.*;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.TimeUtils;
import com.keyInput;
import com.pbo.game.StellaronRaiders;

import java.util.ArrayList;
import java.util.Iterator;

public class GameScreen implements Screen {
    StellaronRaiders game;
    Sprite life, cross;
    Texture asteroidTXT, gameBackground, explostionTXT, enemyTXT, bossTXT, playerTXT, itemPointTXT, healthItem, stellarJade;
    Sound explosion, pewpew, bonusScore, extraLife;
    Music bgMusic;
    SpaceShip player, enemy, boss;
    public int playerLife = 5;
    public int enemyHP = 3;
    public int bossHP = 10;
    public int score = 0;
    BitmapFont textFont;
    private Rectangle hitBox, stellarHitBox;
    private Array<Rectangle> asteroids, enemies, bosses;
    Animation<TextureRegion> asteroidAnimation, explosionAnimation, playerAnimation, enemyAnimation, bossAnimation;
    private long lastSpawnTime, lastSpawnTime2, getLastSpawnTime3, bossSpawnTime;
    float stateTime;
    FlyingObjects asteroid;
    private ArrayList<FlyingObjects> bullets;
    private ArrayList<FlyingObjects> bulletsE;
    private ArrayList<FlyingObjects> bulletsEB;
    private ArrayList<Long> lastEnemyBulletTimes = new ArrayList<>();
    private ArrayList<Long> lastEnemyBossBulletTime = new ArrayList<>();
    public static final float ShootCooldown = 0.3f;
    float shootTimer;
    private int backGroundOffset;
    public keyInput playerInput = new keyInput();
    int count = 0;
    int count2 = 0;
    ArrayList<ItemDrop> itemPoint = new ArrayList<>();
    ArrayList<ItemDrop> healthPoint = new ArrayList<>();
    private boolean isBossAlive = true;
    boolean isBossMovingUp = true; // Initialize as true since the boss starts moving up


    public GameScreen (StellaronRaiders game) {
        this.game = game;
        bullets = new ArrayList<FlyingObjects>();
        bulletsE = new ArrayList<FlyingObjects>();
        bulletsEB = new ArrayList<FlyingObjects>();
        shootTimer = 0;
    }

    private void spawnEnemy(){
        enemy = new Enemy(game.WIDTH, MathUtils.random(0, 900 - 64), 62, 18, enemyHP);
        Rectangle enemyRect = new Rectangle();
        enemyRect.y = enemy.y;
        enemyRect.x = enemy.x;
        enemyRect.width = (float) (enemy.textureWIDTH * 2.5);
        enemyRect.height = (float) (enemy.textureHEIGHT * 2.5);

        enemies.add(enemyRect);
        lastSpawnTime2 = TimeUtils.nanoTime();
    }

    private void spawnBoss() {
        boss = new Boss(game.WIDTH, game.HEIGHT/2, 124, 36, bossHP);
        Rectangle bossRect = new Rectangle();
        bossRect.x = boss.x;
        bossRect.y = boss.y;
        bossRect.width = (float) (boss.textureWIDTH * 2.5);
        bossRect.height = (float) (boss.textureHEIGHT * 2.5);

        bosses.add(bossRect);
        bossSpawnTime = TimeUtils.nanoTime();
    }

    private void spawnAsteroid() {
        asteroid = new Asteroid(game.WIDTH, MathUtils.random(0, 900 - 64), 17, 14);
        Rectangle asteroidRect = new Rectangle();
        asteroidRect.y = asteroid.y;
        asteroidRect.x = asteroid.x;
        asteroidRect.width = asteroid.width * 4;
        asteroidRect.height = asteroid.height * 4;

        asteroids.add(asteroidRect);
        lastSpawnTime = TimeUtils.nanoTime();
    }

    //Bonus Item (point and life)
    private void spawnPointItem(Rectangle rect) {
        PointItem newItemPoint = new PointItem(rect.x, rect.y, 160, 160, 50);
        itemPoint.add(newItemPoint);
        getLastSpawnTime3 = TimeUtils.nanoTime();
    }
    private void spawnHealthItem(Rectangle rect) {
        HealthItem newHealthItem = new HealthItem(rect.x, rect.y, 160, 160, 50);
        healthPoint.add(newHealthItem);
        getLastSpawnTime3 = TimeUtils.nanoTime();
    }

    // Hitbox untuk player
    private void createHitBox(){
        hitBox.x = player.x + 5;
        hitBox.y = player.y + 5;
        hitBox.width = (float) ((float) player.textureWIDTH * 2.5);
        hitBox.height = (float) (player.textureHEIGHT * 2.5);
    }
    // Hitbox untuk stellar jade
    private void createStellarHitBox() {
        stellarHitBox.x = 1200;
        stellarHitBox.y = 400;
        stellarHitBox.width = stellarJade.getWidth();
        stellarHitBox.height = stellarJade.getWidth();
    }

    // Batasan gerakan player agar tidak keluar screen
    private void maxMovement() {
        if(hitBox.x < 0) hitBox.x = 0;
        if(hitBox.x > 1600 - 64) hitBox.x = 1600 - 64;
        if(hitBox.y < 0) hitBox.y = 0;
        if(hitBox.y > 900 - 64) hitBox.y = 900 - 64;
    }

    public void playerMovement() {
        //memanggil function dari class keyInput yang menerima input keyboard
        if(playerInput.isKeyPressedA()) hitBox.x -= ((Player)player).getSPEED() * Gdx.graphics.getDeltaTime();
        if(playerInput.isKeyPressedD()) hitBox.x  += ((Player)player).getSPEED() * Gdx.graphics.getDeltaTime();
        if(playerInput.isKeyPressedW()) hitBox.y += ((Player)player).getSPEED()* Gdx.graphics.getDeltaTime();
        if(playerInput.isKeyPressedS()) hitBox.y -= ((Player)player).getSPEED()* Gdx.graphics.getDeltaTime();
    }

    @Override
    public void show() {
        gameBackground = new Texture(Gdx.files.internal("Screen/spaceBG2.png"));
        bgMusic = Gdx.audio.newMusic(Gdx.files.internal("Music/bgMusic.mp3"));
        life = new Sprite(new Texture(Gdx.files.internal("Objects/life.png")));
        life.setPosition(10, 900-90);
        cross = new Sprite(new Texture(Gdx.files.internal("Objects/cross.png")));
        cross.setPosition(life.getX()+100, 900-56);
        textFont = new BitmapFont(Gdx.files.internal("Font/score.fnt"));

        pewpew = Gdx.audio.newSound(Gdx.files.internal("Music/laserpew.ogg"));
        pewpew.setVolume(1,5);

        player = new Player(10, 400, 62, 18, playerLife);
        playerTXT = new Texture(Gdx.files.internal("Objects/playerShip.png"));
        TextureRegion[][] temp3 = TextureRegion.split(playerTXT, playerTXT.getWidth() / 3, playerTXT.getHeight() / 1);
        int index1 = 0;
        TextureRegion[] playerFrame = new TextureRegion[3];
        for (int i = 0; i < 1; i++) {
            for (int j = 0; j < 3; j++) {
                playerFrame[index1++] = temp3[i][j];
            }
        }
        playerAnimation = new Animation<TextureRegion>(0.5f, playerFrame);
        hitBox = new Rectangle();
        createHitBox();

        enemyTXT = new Texture(Gdx.files.internal("Objects/enemyShip.png"));
        TextureRegion[][] temp1 = TextureRegion.split(enemyTXT, enemyTXT.getWidth() / 3, enemyTXT.getHeight() / 1);
        int index2 = 0;
        TextureRegion[] enemyFrame = new TextureRegion[3];
        for (int i = 0; i < 1; i++) {
            for (int j = 0; j < 3; j++) {
                enemyFrame[index2++] = temp1[i][j];
            }
        }
        enemyAnimation = new Animation<TextureRegion>(0.5f, enemyFrame);
        enemies = new Array<Rectangle>();
        spawnEnemy();

        bossTXT = new Texture(Gdx.files.internal("Objects/bossShip.png"));
        TextureRegion[][] temp4 = TextureRegion.split(bossTXT, bossTXT.getWidth()/3, bossTXT.getHeight() / 1);
        int index3 = 0;
        TextureRegion[] bossFrame = new TextureRegion[3];
        for (int i = 0; i < 1; i++) {
            for (int j = 0; j < 3; j++) {
                bossFrame[index3++] = temp4[i][j];
            }
        }
        bossAnimation = new Animation<TextureRegion>(0.5f, bossFrame);
        bosses = new Array<Rectangle>();
        spawnBoss();

        explostionTXT = new Texture(Gdx.files.internal("Effect/explosion2.png"));
        TextureRegion[][] temp2 = TextureRegion.split(explostionTXT, explostionTXT.getWidth()/4, explostionTXT.getHeight()/1);
        int indexx = 0;
        TextureRegion[] exploFrame = new TextureRegion[4];
        for (int i = 0; i < 1; i++) {
            for (int j = 0; j < 4; j++) {
                exploFrame[indexx++] = temp2[i][j];
            }
        }
        explosionAnimation = new Animation<TextureRegion>(100f, exploFrame);
        explosion = Gdx.audio.newSound(Gdx.files.internal("Music/explosion.wav"));
        explosion.setVolume(0, 0.5f);

        asteroidTXT = new Texture(Gdx.files.internal("Objects/asteroid.png"));
        TextureRegion[][] temp = TextureRegion.split(asteroidTXT, asteroidTXT.getWidth() / 5, asteroidTXT.getHeight() / 1);
        int index = 0;
        TextureRegion[] asterFrame = new TextureRegion[5];
        for (int i = 0; i < 1; i++) {
            for (int j = 0; j < 5; j++) {
                asterFrame[index++] = temp[i][j];
            }
        }
        asteroidAnimation = new Animation<TextureRegion>(0.5f, asterFrame);
        stateTime = 0f;
        asteroids = new Array<Rectangle>();
        spawnAsteroid();

        // memulai lagu background
        bgMusic.setVolume(0.05f);
        bgMusic.setLooping(true);
        bgMusic.play();

        itemPointTXT = new Texture(Gdx.files.internal("Objects/point_crystal.png"));
        bonusScore = Gdx.audio.newSound(Gdx.files.internal("Music/bonusScore.mp3"));
        bonusScore.setVolume(1, 5);
        healthItem = new Texture(Gdx.files.internal("Objects/life.png"));
        extraLife = Gdx.audio.newSound(Gdx.files.internal("Music/extra_life_sound.mp3"));
        extraLife.setVolume(1, 5);
        stellarJade = new Texture(Gdx.files.internal("Objects/stellar_jade.png"));
        stellarHitBox = new Rectangle();
        createStellarHitBox();
    }

    @Override
    public void render(float delta) {
        bgMusic.play();
        // Mengatur background
        backGroundOffset++;
        if(backGroundOffset % game.WIDTH == 0){
            backGroundOffset = 0;
        }

        game.batch.begin();

        // menggambar background
        game.batch.draw(gameBackground, -backGroundOffset, 0, game.WIDTH, game.HEIGHT );
        game.batch.draw(gameBackground, -backGroundOffset + game.WIDTH, 0, game.WIDTH, game.HEIGHT );

        stateTime += Gdx.graphics.getDeltaTime();
        TextureRegion currentFramePlayer = playerAnimation.getKeyFrame(stateTime, true);
        game.batch.draw(currentFramePlayer, hitBox.x, hitBox.y, hitBox.width, hitBox.height);

        // membuat player life di pojok kiri
        game.batch.draw(life, life.getX(), life.getY(), 100, 100);
        game.batch.draw(cross, cross.getX(), cross.getY(), 45, 35);
        textFont.setColor(Color.WHITE);
        String lifeRemaining = String.valueOf(playerLife);
        textFont.draw(game.batch, lifeRemaining, cross.getX()+65, 900-25);
        String scores = String.valueOf(score);
        textFont.draw(game.batch, scores, 1400, 900-25 );

        playerMovement();
        maxMovement();

        shootTimer += delta;
        // fitur menembak untuk player
        for (FlyingObjects bullets1 : bullets) {
            game.batch.draw(((Bullets)bullets1).getTexture(), bullets1.x + 90, bullets1.y, bullets1.width, bullets1.height);
        }
        if(playerInput.isKeyPressedSPACE() && shootTimer >= ShootCooldown){
            shootTimer = 0;
            FlyingObjects bullet = new Bullets(hitBox.x, hitBox.y - 5,90,60);
            bullets.add((Bullets) bullet);
            pewpew.play();
        }

        // fitur menembak untuk musuh
        for (FlyingObjects bullets1E : bulletsE) {
            game.batch.draw(((Bullets)bullets1E).getTexture(), bullets1E.x - 90, bullets1E.y, bullets1E.width, bullets1E.height);
        }
        ArrayList<Bullets> bulletsRemoveE = new ArrayList<Bullets>();
        for(FlyingObjects bulletsE1 : bulletsE){
            ((Bullets) bulletsE1).updateEnemy(delta);
            ((Bullets) bulletsE1).hitBox.setPosition(bulletsE1.x, bulletsE1.y);
            if(((Bullets) bulletsE1).remove){
                bulletsRemoveE.add((Bullets)bulletsE1);
            }
        }

        // fitur menembak untuk musuh boss
        for (FlyingObjects bullets2E : bulletsEB) {
            game.batch.draw(((Bullets) bullets2E).getTexture(), bullets2E.x - 90, bullets2E.y, bullets2E.width, bullets2E.height);
        }
        ArrayList<Bullets> bulletsRemoveEB = new ArrayList<Bullets>();
        for(FlyingObjects bulletsEB1 : bulletsEB){
            ((Bullets) bulletsEB1).updateEnemy(delta);
            ((Bullets) bulletsEB1).hitBox.setPosition(bulletsEB1.x, bulletsEB1.y);
            if(((Bullets) bulletsEB1).remove){
                bulletsRemoveEB.add((Bullets)bulletsEB1);
            }
        }


        // buat peluru bergerak
        ArrayList<Bullets> bulletsRemove = new ArrayList<Bullets>();
        for(FlyingObjects bullets1: bullets){
            ((Bullets) bullets1).update(delta);
            ((Bullets)bullets1).hitBox.setPosition(bullets1.x,bullets1.y);
            if(((Bullets) bullets1).remove){
                bulletsRemove.add((Bullets) bullets1);
            }
        }

        // mengammbar bonus item
        for (ItemDrop item : itemPoint) {
            Rectangle itemRect = new Rectangle(item.x, item.y, item.width - 100, item.height - 100);
            game.batch.draw(itemPointTXT, itemRect.x, itemRect.y, itemRect.width, itemRect.height);
        }
        for (ItemDrop item : healthPoint) {
            Rectangle healthRect = new Rectangle(item.x, item.y, item.width - 100, item.height - 100);
            game.batch.draw(healthItem, healthRect.x, healthRect.y, healthRect.width, healthRect.height);
        }

        // menggambar asteroid
        for(Rectangle ast: asteroids) {
            stateTime += Gdx.graphics.getDeltaTime();
            TextureRegion currentFrame = asteroidAnimation.getKeyFrame(stateTime, true);
            game.batch.draw(currentFrame, ast.x, ast.y, ast.width, ast.height);
        }
        if(TimeUtils.nanoTime() - lastSpawnTime > 710000000) spawnAsteroid();

        // jika score > 200, spawn enemy
        if(score >= 200){
            // enemy
            long spawnTIME = 5000000000L;
            if (score >= 2000){
                spawnTIME = 8000000000L;
            }
            if(TimeUtils.nanoTime() - lastSpawnTime2 > (spawnTIME)) spawnEnemy();

            for(Rectangle enmy: enemies){
                stateTime += Gdx.graphics.getDeltaTime();
                TextureRegion currentFrameEnemy = enemyAnimation.getKeyFrame(stateTime, true);
                game.batch.draw(currentFrameEnemy, enmy.x, enmy.y, enmy.width, enmy.height);
                lastEnemyBulletTimes.add(TimeUtils.nanoTime());
                int enemyIndex = enemies.indexOf(enmy, true);
                Long lastBulletTime = lastEnemyBulletTimes.get(enemyIndex);
                if (TimeUtils.nanoTime() - lastBulletTime > 5000000000L) {
                    FlyingObjects bulletE = new Bullets(enmy.x, enmy.y - 5, 90, 60);
                    bulletsE.add((Bullets) bulletE);
                    pewpew.play();
                    lastEnemyBulletTimes.set(enemyIndex, TimeUtils.nanoTime());
                }
            }

            for(Iterator<Rectangle> iter = enemies.iterator(); iter.hasNext();){
                Rectangle enemyRect = iter.next();
                enemyRect.x -= ((Enemy) enemy).getSPEED() * Gdx.graphics.getDeltaTime();
                if(enemyRect.x + 64 < 0) iter.remove();
                // pengecekan jika player menabrak enemy
                if(enemyRect.overlaps(hitBox)) {
                    //animasi enemy meledak
                    game.batch.draw(enemyTXT, enemyRect.x, enemyRect.y, enemyRect.width, enemyRect.height);
                    explosion.play();

                    iter.remove();
                    playerLife--;
                    continue;
                }

                if (playerLife<0) {
                    // animasi player meledak
                    stateTime += Gdx.graphics.getDeltaTime();
                    TextureRegion currentFrame = explosionAnimation.getKeyFrame(stateTime, true);
                    game.batch.draw(currentFrame, enemyRect.getX(), enemyRect.getY(), 16 * 5, 16 * 5);
                    bgMusic.stop();
                    this.dispose();
                    game.setScreen(new GameOverScreen(game, score));
                }
                //nested loop untuk mengecek setiap enemy adakah yang overlap dengan setiap bullet
                for(FlyingObjects b : bullets) {
                    if (enemyRect.overlaps(((Bullets) b).getHitBox())) {
                        count++;
                        // animasi enemy meledak
                        stateTime += Gdx.graphics.getDeltaTime();
                        TextureRegion currentFrame = explosionAnimation.getKeyFrame(stateTime, true);
                        game.batch.draw(currentFrame, enemyRect.getX(), enemyRect.getY(), 16 * 5, 16 * 5);
                        explosion.play();
                        bulletsRemove.add((Bullets) b);

                        // remove enemy jika terkena hit 3 kali
                        if (count >= 3){
                            stateTime += Gdx.graphics.getDeltaTime();
                            currentFrame = explosionAnimation.getKeyFrame(stateTime, true);
                            game.batch.draw(currentFrame, enemyRect.getX(), enemyRect.getY(), 16 * 5, 16 * 5);

                            score += 100;
                            explosion.play();
                            iter.remove();
                            bulletsRemove.add((Bullets) b);
                            count = 0;

                            // spawn bonus item drop from enemy
                            int random = MathUtils.random(1, 10);
                            if(random == 1 || random == 3 || random == 5){
                                enemyRect = new Rectangle(enemyRect.getX(), enemyRect.getY(), enemyRect.getWidth(), enemyRect.getHeight());
                                spawnHealthItem(enemyRect);
                            }else if (random == 2 || random == 4|| random == 6){
                                enemyRect = new Rectangle(enemyRect.getX(), enemyRect.getY(), enemyRect.getWidth(), enemyRect.getHeight());
                                spawnPointItem(enemyRect);
                            }else{
                                continue;
                            }

                        }
                    }
                }
            }
        }
        // boss
        if (score >= 2000    ) {

            // gambar boss
            for (Rectangle boss : bosses) {
                stateTime += Gdx.graphics.getDeltaTime();
                TextureRegion currentBossFrame = bossAnimation.getKeyFrame(stateTime, true);
                game.batch.draw(currentBossFrame, boss.x, boss.y, boss.width, boss.height);

                lastEnemyBossBulletTime.add(TimeUtils.nanoTime());
                int enemyBIndex = bosses.indexOf(boss, true);
                Long lastBulletTime = lastEnemyBossBulletTime.get(enemyBIndex);
                if (TimeUtils.nanoTime() - lastBulletTime > 1500000000L) {
                    FlyingObjects bulletE = new Bullets(boss.x, boss.y - 5, 90, 60);
                    bulletsEB.add((Bullets) bulletE);
                    pewpew.play();
                    lastEnemyBossBulletTime.set(enemyBIndex, TimeUtils.nanoTime());
                }


            }

            for(Iterator<Rectangle> iter = bosses.iterator(); iter.hasNext();) {
                Rectangle bossRect = iter.next();

                if (isBossAlive) {
                    bossRect.x -= ((Boss) boss).getSPEED() * Gdx.graphics.getDeltaTime();
                    if (bossRect.x < 1000) bossRect.x = 1000;

                    // Logika naik-turun bos
                    if (isBossMovingUp) {
                        bossRect.y += ((Boss) boss).getSPEED() * Gdx.graphics.getDeltaTime();
                        if (bossRect.y >= 800) {
                            isBossMovingUp = false; // Change direction to down
                        }
                    } else {
                        bossRect.y -= ((Boss) boss).getSPEED() * Gdx.graphics.getDeltaTime();
                        if (bossRect.y <= 200) {
                            isBossMovingUp = true; // Change direction to up
                        }
                    }
                }


//                if (isBossAlive) {
//                    bossRect.x -= ((Boss) boss).getSPEED() * Gdx.graphics.getDeltaTime();
//                    if (bossRect.x < 1000) bossRect.x = 1000;
//
//                    // Logika naik-turun bos
//                    if (bossRect.y >= 800) {
//                        // Bos sedang turun
//                        bossRect.y -= ((Boss) boss).getSPEED() * Gdx.graphics.getDeltaTime();
//                    } else if (bossRect.y <= 200) {
//                        // Bos sedang naik
//                        bossRect.y += ((Boss) boss).getSPEED() * Gdx.graphics.getDeltaTime();
//                    } else {
//                        // Memilih secara acak apakah bos akan naik atau turun
//                        boolean shouldGoUp = MathUtils.randomBoolean();
//                        if (shouldGoUp) {
//                            bossRect.y += ((Boss) boss).getSPEED() * Gdx.graphics.getDeltaTime();
//                        } else {
//                            bossRect.y -= ((Boss) boss).getSPEED() * Gdx.graphics.getDeltaTime();
//                        }
//                    }
//                }


                    for(FlyingObjects b : bullets) {
                    if (bossRect.overlaps(((Bullets) b).getHitBox())) {
                        count2++;
                        // animasi enemy meledak
                        stateTime += Gdx.graphics.getDeltaTime();
                        TextureRegion currentFrame = explosionAnimation.getKeyFrame(stateTime, true);
                        game.batch.draw(currentFrame, bossRect.getX(), bossRect.getY(), 16 * 5, 16 * 5);
                        explosion.play();
                        bulletsRemove.add((Bullets) b);

                        // remove enemy jika terkena hit 3 kali
                        if (count2 >= 10){
                            stateTime += Gdx.graphics.getDeltaTime();
                            currentFrame = explosionAnimation.getKeyFrame(stateTime, true);
                            game.batch.draw(currentFrame, bossRect.getX(), bossRect.getY(), 16 * 5, 16 * 5);

                            score += 100;
                            explosion.play();
                            iter.remove();
                            bulletsRemove.add((Bullets) b);
                            count2 = 0;
                            isBossAlive = false;
                        }
                    }
                }


                // pengecekan jika player menabrak enemy
                if (bossRect.overlaps(hitBox)) {
                    //animasi enemy meledak
                    game.batch.draw(bossTXT, bossRect.x, bossRect.y, bossRect.width, bossRect.height);
                    explosion.play();

                    playerLife = -1;
                    continue;
                }

                if (playerLife < 0) {
                    // animasi player meledak
                    stateTime += Gdx.graphics.getDeltaTime();
                    TextureRegion currentFrame = explosionAnimation.getKeyFrame(stateTime, true);
                    game.batch.draw(currentFrame, bossRect.getX(), bossRect.getY(), 16 * 5, 16 * 5);
                    bgMusic.stop();
                    this.dispose();
                    game.setScreen(new GameOverScreen(game, score));
                }

            }

        }
        if (!isBossAlive){

            game.batch.draw(stellarJade, 1200, 400, 160,160);

            // jika player mengambil stellar jade, maka menang
            if (hitBox.overlaps(stellarHitBox)) {
                bgMusic.stop();
                game.setScreen(new WinnerScreen(game, score));
            }
        }

        // Pengecekan apakah player mengenai asteroid atau bullet mengenai asteroid
        for (Iterator<Rectangle> iter = asteroids.iterator(); iter.hasNext(); ) {
            Rectangle asteroidRect = iter.next();
            asteroidRect.x -= ((Asteroid) asteroid).getSPEED() * Gdx.graphics.getDeltaTime();
            if(asteroidRect.x + 64 < 0) iter.remove();
            if(asteroidRect.overlaps(hitBox)) {
                //animasi meteor meledak
                stateTime += Gdx.graphics.getDeltaTime();
                TextureRegion currentFrame = explosionAnimation.getKeyFrame(stateTime, true);
                game.batch.draw(currentFrame, asteroidRect.getX(), asteroidRect.getY(), 16 * 5, 16 * 5);

                explosion.play();
                iter.remove();
                playerLife--;
                continue;
            }
            if (playerLife<0) {
                // animasi player meledak
                stateTime += Gdx.graphics.getDeltaTime();
                TextureRegion currentFrame = explosionAnimation.getKeyFrame(stateTime, true);
                game.batch.draw(currentFrame, asteroidRect.getX(), asteroidRect.getY(), 16 * 5, 16 * 5);
                bgMusic.stop();
                this.dispose();
                game.setScreen(new GameOverScreen(game, score));
            }
            //nested loop untuk mengecek setiap asteroid adakah yang overlap dengan setiap bullet
            for(FlyingObjects b : bullets){
                if(asteroidRect.overlaps(((Bullets)b).getHitBox())){
                    // animasi meteor meledak
                    stateTime += Gdx.graphics.getDeltaTime();
                    TextureRegion currentFrame = explosionAnimation.getKeyFrame(stateTime, true);
                    game.batch.draw(currentFrame, asteroidRect.getX(), asteroidRect.getY(), 16 * 5, 16 * 5);
                    score+=25;
                    explosion.play();
                    iter.remove();
                    bulletsRemove.add((Bullets) b);
                }
            }

        }


        // jika player terkena bullet enemy (kena damage)
        for(Iterator<FlyingObjects> iter = bulletsE.iterator(); iter.hasNext();) {
            FlyingObjects bulletEnRect = iter.next();
            bulletEnRect.x -= ((Enemy) enemy).getSPEED() * Gdx.graphics.getDeltaTime();
            if (bulletEnRect.x + 64 < 0) iter.remove();
            if (bulletEnRect.overlaps(hitBox)) {
                game.batch.draw(explostionTXT, bulletEnRect.x, bulletEnRect.y, bulletEnRect.width, bulletEnRect.height);
                iter.remove();
                playerLife--;
                explosion.play();
            }
        }

        //jika player terkena bullet boss
        for(Iterator<FlyingObjects> iter = bulletsEB.iterator(); iter.hasNext();) {
            FlyingObjects bulletEnBRect = iter.next();
            bulletEnBRect.x -= ((Boss) boss).getSPEED() * Gdx.graphics.getDeltaTime();
            if (bulletEnBRect.x + 64 < 0) iter.remove();
            if (bulletEnBRect.overlaps(hitBox)) {
                game.batch.draw(explostionTXT, bulletEnBRect.x, bulletEnBRect.y, bulletEnBRect.width, bulletEnBRect.height);
                iter.remove();
                playerLife-= 3;
                explosion.play();
            }
        }

        // remove bullet enemy
        while(bulletsRemoveE.size() != 0){
            bulletsE.remove(bulletsRemoveE.get(0));
            bulletsRemoveE.remove(0);
        }

        //remove bullet boss
        while(bulletsRemoveEB.size() != 0){
            bulletsEB.remove(bulletsRemoveEB.get(0));
            bulletsRemoveEB.remove(0);
        }

        // remove bullet player
        while(bulletsRemove.size() != 0){
            bullets.remove(bulletsRemove.get(0));
            bulletsRemove.remove(0);
        }

        // pengecekan untuk bonus score
        Iterator<ItemDrop> iter = itemPoint.iterator();
        while (iter.hasNext()) {
            ItemDrop item = iter.next();
            Rectangle itemRect = new Rectangle(item.x, item.y, item.width, item.height);

            if (TimeUtils.nanoTime() - getLastSpawnTime3 > 6000000000L){
                iter.remove();
            }

            if (itemRect.overlaps(hitBox)) {
                iter.remove();
                bonusScore.play();
                score += 50;
            }
        }

        // pengecekan untuk bonus life
        Iterator<ItemDrop> iterr = healthPoint.iterator();
        while (iterr.hasNext()) {
            ItemDrop health = iterr.next();
            Rectangle healthRect = new Rectangle(health.x, health.y, health.width, health.height);

            if (TimeUtils.nanoTime() - getLastSpawnTime3 > 6000000000L) {
                iterr.remove();
            }

            if (healthRect.overlaps(hitBox)) {
                iterr.remove();
                extraLife.play();
                long id = extraLife.play();
                extraLife.setPitch(id, 2);
                playerLife+=1;
            }
        }

        game.batch.end();
    }

    @Override
    public void resize(int width, int height) {

    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {

    }

    @Override
    public void dispose() {

    }
}
