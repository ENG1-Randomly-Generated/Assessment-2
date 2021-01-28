package com.randomlygenerated.tests;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.backends.headless.HeadlessApplication;
import com.badlogic.gdx.backends.headless.HeadlessApplicationConfiguration;
import com.badlogic.gdx.graphics.GL20;
import com.hardgforgif.dragonboatracing.Game;

import static org.mockito.Mockito.mock;

public class HeadlessInstance {

    public Game game;
    public Application application;

    public HeadlessInstance() throws InterruptedException {
        this.game = new Game();
        this.application = new HeadlessApplication(game, new HeadlessApplicationConfiguration());
        Gdx.gl = mock(GL20.class);
        Thread.sleep(1000);
    }

}
