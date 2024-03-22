package Screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.pbo.game.StellaronRaiders;

public class MenuScreen implements Screen {
    private static final int playButtonWidth = 350;
    private static final int playButtonHeigth = 200;
    private static final int quitButtonWidth = 300;
    private static final int quitButtonHeigth = 200;
    private static final int quitButtonY = 180;
    private static final int playButtonY = 360;
    StellaronRaiders game;
    Texture quitButton, playButton, gameBackground, gameTitle;
    private int backGroundOFFset = 0;
    Music lobbyMusic, buttonClick;

    public MenuScreen (StellaronRaiders game)  {
        this.game = game;
        playButton = new Texture("Button/play_button.png");
        quitButton = new Texture("Button/quit_button.png");
        gameBackground = new Texture(Gdx.files.internal("Screen/spaceBG2.png"));
        gameTitle = new Texture("Screen/StellaronRaiders.png");
        lobbyMusic = Gdx.audio.newMusic(Gdx.files.internal("Music/LobbyMusic.mp3"));
        buttonClick = Gdx.audio.newMusic((Gdx.files.internal("Music/ButtonClick.mp3")));
        buttonClick.setVolume(0.5f);
        lobbyMusic.setVolume(0.5f);
        lobbyMusic.setLooping(true);
        lobbyMusic.play();
    }
    @Override
    public void show() {
    }
    @Override
    public void render(float delta) {
        ScreenUtils.clear(1, 1, 0, 1);

        backGroundOFFset++;
        if(backGroundOFFset % game.WIDTH == 0){
            backGroundOFFset = 0;
        }

        game.batch.begin();
        //Gambar background game & game title
        game.batch.draw(gameBackground, -backGroundOFFset, 0, game.WIDTH, game.HEIGHT );
        game.batch.draw(gameBackground, -backGroundOFFset + game.WIDTH, 0, game.WIDTH, game.HEIGHT );
        game.batch.draw(gameTitle, -150, 120, 2000, 1000);

        //Gambar play button dan quit button
        game.batch.draw(playButton, game.WIDTH/2 - playButtonWidth/2, game.HEIGHT/2 - playButtonHeigth/2, playButtonWidth, playButtonHeigth);
        game.batch.draw(quitButton, game.WIDTH/2 - quitButtonWidth/2, game.HEIGHT/2 - quitButtonHeigth/2 - 170, quitButtonWidth, quitButtonHeigth);

        // untuk mendeteksi mouse hover di quit button
        int x = game.WIDTH / 2 - quitButtonWidth / 2;
        if (Gdx.input.getX() < x + quitButtonWidth && Gdx.input.getX() > x && game.HEIGHT - Gdx.input.getY() < quitButtonY + quitButtonHeigth && game.HEIGHT - Gdx.input.getY() > quitButtonY) {
            if (Gdx.input.isTouched()) {
                buttonClick.play();
                Gdx.app.exit();
            }
        }

        // untuk mendeteksi mouse hover di play button
        x = game.WIDTH / 2 - playButtonWidth / 2;
        if (Gdx.input.getX() < x + playButtonWidth && Gdx.input.getX() > x && game.HEIGHT - Gdx.input.getY() < playButtonY + playButtonHeigth && game.HEIGHT - Gdx.input.getY() > playButtonY) {
            if (Gdx.input.isTouched()) {
                game.setScreen(new GameScreen(game));
                buttonClick.play();
                lobbyMusic.stop();
                this.dispose();
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
