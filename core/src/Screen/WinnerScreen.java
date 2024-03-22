package Screen;

import Objects.Player;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.ScreenUtils;
import com.pbo.game.StellaronRaiders;

import java.awt.*;

public class WinnerScreen implements Screen {
    private static final int menuButtonWidth = 350;
    private static final int menuButtonHeigth = 200;
    private static final int menuButtonY = 360;
    private static final int quitButtonWidth = 300;
    private static final int quitButtonHeigth = 200;
    private static final int quitButtonY = 180;
    private static final int BANNER_WIDTH = 1600;
    private static final int BANNER_HEIGHT = 1200;
    Texture quitButton, menuButton, gameBackground, victory, stellarJade;
    private int backGroundOFFset = 0;
    Music victoryMusic, buttonClick;
    BitmapFont scoreFont;
    BitmapFont textFont;
    StellaronRaiders game;
    int score, highscore;
    String text, scoreText, highscoreText;
    public WinnerScreen (StellaronRaiders game, int score) {
        this.game = game;
        this.score = score;

        //Get highscore from save file
        Preferences prefs = Gdx.app.getPreferences("StellaronRaiders");
        this.highscore = prefs.getInteger("highscore", 0);

        gameBackground = new Texture(Gdx.files.internal("Screen/spaceBG2.png"));
        victoryMusic = Gdx.audio.newMusic(Gdx.files.internal("Music/VICTORY_MUSIC.mp3"));
        victoryMusic.setVolume(0.5f);
        victoryMusic.setLooping(true);
        victoryMusic.play();

        victory = new Texture("Screen/victory.png");
        menuButton = new Texture("Button/menu_button.png");
        quitButton = new Texture("Button/quit_button.png");
        buttonClick = Gdx.audio.newMusic((Gdx.files.internal("Music/ButtonClick.mp3")));

        scoreFont = new BitmapFont(Gdx.files.internal("Font/score.fnt"));
        textFont = new BitmapFont(Gdx.files.internal("Font/score.fnt"));
        text = "Thank You For Playing!!!";
        scoreText = "Score:";
        highscoreText = "Highscore:";

        stellarJade = new Texture("Objects/stellar_jade.png");

        if (score > highscore) {
            prefs.putInteger("highscore", score);
            prefs.flush();
        }
    }
    @Override
    public void render(float delta) {
        ScreenUtils.clear(1, 1, 0, 1);

        backGroundOFFset++;
        if(backGroundOFFset % game.WIDTH == 0){
            backGroundOFFset = 0;
        }

        game.batch.begin();
        // menggambar background game & tulisan game over
        game.batch.draw(gameBackground, -backGroundOFFset, 0, game.WIDTH, game.HEIGHT );
        game.batch.draw(gameBackground, -backGroundOFFset + game.WIDTH, 0, game.WIDTH, game.HEIGHT );
        game.batch.draw(victory, 5, 100 , BANNER_WIDTH, BANNER_HEIGHT);

        // menggambar play button dan quit button
        game.batch.draw(menuButton, game.WIDTH/2 - menuButtonWidth/2, game.HEIGHT/2 - menuButtonHeigth/2 + 50, menuButtonWidth, menuButtonHeigth);
        game.batch.draw(quitButton, game.WIDTH/2 - quitButtonWidth/2, game.HEIGHT/2 - quitButtonHeigth/2 - 150, quitButtonWidth, quitButtonHeigth);

        // Menulis score game dan highscore game
        GlyphLayout scoreLayout = new GlyphLayout(scoreFont, "Score: \n" + score, Color.WHITE, 0, Align.left, false);
        GlyphLayout highscoreLayout = new GlyphLayout(scoreFont, "Highscore: \n" + highscore, Color.WHITE, 0, Align.left, false);
        scoreFont.draw(game.batch, scoreLayout, 60, 500);
        scoreFont.draw(game.batch, highscoreLayout, 60, 400);

        // Menulis thank you text
        textFont.setColor(Color.WHITE);
        textFont.draw(game.batch, text, 435, 140);

        // Menggambar stellar jade
        game.batch.draw(stellarJade, 1200, 300, 250, 250);

        // untuk mendeteksi mouse hover di quit button
        int x = game.WIDTH / 2 - quitButtonWidth / 2;
        if (Gdx.input.getX() < x + quitButtonWidth && Gdx.input.getX() > x && game.HEIGHT - Gdx.input.getY() < quitButtonY + quitButtonHeigth && game.HEIGHT - Gdx.input.getY() > quitButtonY) {
            if (Gdx.input.isTouched()) {
                buttonClick.play();
                Gdx.app.exit();
            }
        }

        // untuk mendeteksi mouse hover di menu button
        x = game.WIDTH / 2 - menuButtonWidth / 2;
        if (Gdx.input.getX() < x + menuButtonWidth && Gdx.input.getX() > x && game.HEIGHT - Gdx.input.getY() < menuButtonY + menuButtonHeigth && game.HEIGHT - Gdx.input.getY() > menuButtonY) {
            if (Gdx.input.isTouched()) {
                buttonClick.play();
                game.setScreen(new MenuScreen(game));
                victoryMusic.stop();
                this.dispose();
            }
        }
        game.batch.end();

    }

    @Override
    public void show() {

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
