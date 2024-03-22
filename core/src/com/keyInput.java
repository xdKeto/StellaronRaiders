package com;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;

public class keyInput{
    public boolean isKeyPressedW(){
        return Gdx.input.isKeyPressed(Input.Keys.W);
    }

    public boolean isKeyPressedA(){
        return Gdx.input.isKeyPressed(Input.Keys.A);
    }

    public boolean isKeyPressedS(){
        return Gdx.input.isKeyPressed(Input.Keys.S);
    }

    public boolean isKeyPressedD(){
        return Gdx.input.isKeyPressed(Input.Keys.D);
    }

    public boolean isKeyPressedSPACE(){
        return Gdx.input.isKeyPressed(Input.Keys.SPACE);
    }
}
