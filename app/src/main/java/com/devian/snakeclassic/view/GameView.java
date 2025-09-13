package com.devian.snakeclassic.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.Typeface;
import android.os.Handler;
import android.os.Looper;
import android.util.AttributeSet;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import androidx.core.content.res.ResourcesCompat;

import com.devian.snakeclassic.R;
import com.devian.snakeclassic.controller.GameController;

import java.util.ArrayList;

public class GameView extends SurfaceView {

    private GameController gameController;
    private Paint paint;
    private SurfaceHolder holder;
    private Typeface customFont;
    private final int cellSize = 50;

    // ➡ Added fields for GO! handling
    private boolean goShown = false;
    private final Handler handler = new Handler(Looper.getMainLooper());

    public GameView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public GameView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        holder = getHolder();
        paint = new Paint();
        paint.setTextSize(60);
        paint.setColor(Color.WHITE);
        customFont = ResourcesCompat.getFont(getContext(), R.font.solderwood_regular);
    }

    public void setGameController(GameController gameController) {
        this.gameController = gameController;
    }

    private int mapWidth, mapHeight, topOffset;

    // ➡ New helper method to update countdown state and handle "GO!"
    public void updateCountdownState(int countdownTimer, GameController.GameState gameStateRef[]) {
        if (gameStateRef[0] == GameController.GameState.COUNTDOWN) {
            if (countdownTimer == 0 && !goShown) {
                goShown = true;
                // Hold the GO! text for 500ms before switching to RUNNING
                handler.postDelayed(() -> {
                    gameStateRef[0] = GameController.GameState.RUNNING;
                    goShown = false;
                }, 500);
            }
        }
    }

    public void draw(ArrayList<Point> snake, Point food, int score,
                     int mapWidth, int mapHeight,
                     GameController.GameState gameState, int countdownTimer) {

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
                    // Draw countdown or GO!
                    String countdownText = (countdownTimer == 0) ? "GO!" : String.valueOf(countdownTimer);
                    paint.setColor(Color.WHITE);
                    paint.setTextSize(200);
                    paint.setTypeface((customFont != null) ? customFont : Typeface.DEFAULT_BOLD);
                    paint.setTextAlign(Paint.Align.CENTER);
                    canvas.drawText(countdownText, getWidth() / 2f, getHeight() / 2f, paint);
                    break;

                case SHOW_GO:
                    paint.setColor(Color.WHITE);
                    paint.setTextSize(200);
                    paint.setTypeface((customFont != null) ? customFont : Typeface.DEFAULT_BOLD);
                    paint.setTextAlign(Paint.Align.CENTER);
                    canvas.drawText("GO!", getWidth() / 2f, getHeight() / 2f, paint);
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
                    // Draw Game Over message
                    paint.setColor(Color.WHITE);
                    paint.setTextSize(120);
                    paint.setTextAlign(Paint.Align.CENTER);
                    paint.setTypeface((customFont != null) ? customFont : Typeface.DEFAULT_BOLD);
                    canvas.drawText("GAME OVER", getWidth() / 2f, getHeight() / 2f, paint);
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
