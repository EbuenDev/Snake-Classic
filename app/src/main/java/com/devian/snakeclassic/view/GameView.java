package com.devian.snakeclassic.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import com.devian.snakeclassic.controller.GameController;

import java.util.ArrayList;

public class GameView extends SurfaceView {

    private GameController gameController;
    private Paint paint;
    private SurfaceHolder holder;
    private final int cellSize = 50;

    public GameView(Context context, android.util.AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public GameView(Context context, android.util.AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        holder = getHolder();
        paint = new Paint();
        paint.setTextSize(60);
        paint.setColor(Color.WHITE);
    }

    public void setGameController(GameController gameController) {
        this.gameController = gameController;
    }

    private int mapWidth, mapHeight, topOffset;

    public void draw(ArrayList<Point> snake, Point food, int score, int mapWidth, int mapHeight, GameController.GameState gameState, int countdownTimer) {
        if (holder.getSurface().isValid()) {
            this.mapWidth = mapWidth;
            this.mapHeight = mapHeight;
            this.topOffset = (getHeight() - (mapHeight * cellSize)) / 2;
            Canvas canvas = holder.lockCanvas();
            if (canvas == null) return;

            canvas.drawColor(Color.DKGRAY);

            int mapLeft = (getWidth() - (mapWidth * cellSize)) / 2;
            int mapTop = topOffset;
            int mapRight = mapLeft + (mapWidth * cellSize);
            int mapBottom = mapTop + (mapHeight * cellSize);
            paint.setColor(Color.BLACK);
            canvas.drawRect(mapLeft, mapTop, mapRight, mapBottom, paint);

            switch (gameState) {
                case COUNTDOWN:
                    paint.setColor(Color.WHITE); // Explicitly set color to white
                    paint.setTextSize(200);
                    paint.setTextAlign(Paint.Align.CENTER);
                    canvas.drawText(String.valueOf(countdownTimer), getWidth() / 2, getHeight() / 2, paint);
                    break;
                case RUNNING:
                    paint.setColor(Color.GREEN);
                    for (Point p : snake) {
                        canvas.drawRect(mapLeft + p.x * cellSize, mapTop + p.y * cellSize,
                                mapLeft + (p.x + 1) * cellSize, mapTop + (p.y + 1) * cellSize, paint);
                    }

                    paint.setColor(Color.RED);
                    canvas.drawRect(mapLeft + food.x * cellSize, mapTop + food.y * cellSize,
                            mapLeft + (food.x + 1) * cellSize, mapTop + (food.y + 1) * cellSize, paint);

                    paint.setColor(Color.WHITE);
                    paint.setTextSize(60);
                    paint.setTextAlign(Paint.Align.LEFT);
                    canvas.drawText("Score: " + score, 50, 150, paint);
                    break;
                case GAME_OVER:
                    break;
            }

            holder.unlockCanvasAndPost(canvas);
        }
    }

    

    public void pause() {
        if (gameController != null) {
            gameController.pause();
        }
    }

    public void resume() {
        if (gameController != null) {
            gameController.resume();
        }
    }
}
