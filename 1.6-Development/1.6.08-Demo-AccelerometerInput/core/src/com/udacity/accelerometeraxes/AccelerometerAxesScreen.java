package com.udacity.accelerometeraxes;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.ScreenViewport;


/**
 * Created by silver on 10/3/15.
 */
public class AccelerometerAxesScreen extends ScreenAdapter {

    public static final String TAG = AccelerometerAxesScreen.class.getName();

    private static final float WORLD_SIZE = 100.0f;
    private static final float TEXT_SCALE = 5.0f;
    private static final Color AXIS_COLOR = Color.RED;
    private static final float AXIS_WIDTH = 1.0f;

    ShapeRenderer renderer;
    FitViewport axisViewport;

    SpriteBatch batch;
    ScreenViewport textViewport;
    BitmapFont font;

    float maxAcceleration;
    float minAcceleration;


    @Override
    public void show() {
        axisViewport = new FitViewport(WORLD_SIZE, WORLD_SIZE);
        renderer = new ShapeRenderer();
        renderer.setAutoShapeType(true);
        batch = new SpriteBatch();
        textViewport = new ScreenViewport();
        font = new BitmapFont();
        font.getData().setScale(TEXT_SCALE);
        font.getRegion().getTexture().setFilter(TextureFilter.Linear, TextureFilter.Linear);
        maxAcceleration = 0;
        minAcceleration = Float.MAX_VALUE;

    }

    @Override
    public void resize(int width, int height) {
        axisViewport.update(width, height, true);
        textViewport.update(width, height, true);
    }

    @Override
    public void dispose() {
        renderer.dispose();
        batch.dispose();
    }

    @Override
    public void render(float delta) {
        textViewport.apply();
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl20.glClear(GL20.GL_COLOR_BUFFER_BIT);

        float xAxis = Gdx.input.getAccelerometerX();
        float yAxis = Gdx.input.getAccelerometerY();
        float zAxis = Gdx.input.getAccelerometerZ();


        float totalAcceleration = (float) Math.sqrt(xAxis * xAxis + yAxis * yAxis + zAxis * zAxis);

        maxAcceleration = Math.max(maxAcceleration, totalAcceleration);
        minAcceleration = Math.min(minAcceleration, totalAcceleration);


        batch.setProjectionMatrix(textViewport.getCamera().combined);

        batch.begin();


        String message = String.format("Accelerometer reads:\nx = %.2f\ny = %.2f\nz = %.2f\ntotal = %.2f\nmax = %.2f\nmin = %.2f", xAxis, yAxis, zAxis, totalAcceleration, maxAcceleration, minAcceleration);
        font.draw(batch, message, 40, textViewport.getWorldHeight() - 40);
        batch.end();

        axisViewport.apply();
        renderer.setProjectionMatrix(axisViewport.getCamera().combined);
        renderer.begin(ShapeType.Line);
        renderer.setColor(AXIS_COLOR);

        renderer.circle(WORLD_SIZE / 2,
                WORLD_SIZE / 2,
                WORLD_SIZE / 4, 64);

        renderer.setColor(Color.GREEN);


        renderer.circle(WORLD_SIZE / 2,
                WORLD_SIZE / 2,
                WORLD_SIZE / 40,
                64);

        renderer.set(ShapeType.Filled);
        renderer.circle(
                WORLD_SIZE * (.5f - .25f * yAxis / 9.8f),
                WORLD_SIZE * (.5f + .25f * xAxis / 9.8f),
                WORLD_SIZE / 50,
                64
        );

//        renderer.rectLine(
//                WORLD_SIZE / 2,
//                WORLD_SIZE / 2,
//                WORLD_SIZE * (.5f - .25f * yAxis / 9.8f),
//                WORLD_SIZE / 2,
//                AXIS_WIDTH);
//
//        renderer.rectLine(
//                WORLD_SIZE / 2,
//                WORLD_SIZE / 2,
//                WORLD_SIZE / 2 ,
//                WORLD_SIZE * (.5f + .25f * xAxis/9.8f),
//                AXIS_WIDTH);


//
////        float xAxis = Gdx.input.getAccelerometerX();
////        float yAxis = Gdx.input.getAccelerometerY();
////        float zAxis = Gdx.input.getAccelerometerZ();
////
////        Gdx.app.log(TAG, "Accelerometer reads x = " + xAxis + ", y = " + yAxis + ", z = " + zAxis);
//
//
//
        renderer.end();


    }
}
