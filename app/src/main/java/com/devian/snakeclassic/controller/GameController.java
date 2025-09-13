package com.devian.snakeclassic.controller;

import android.graphics.Point;
import android.view.MotionEvent;

import com.devian.snakeclassic.GameActivity;
import android.util.Log;

import com.devian.snakeclassic.view.GameView;

import java.util.ArrayList;
import java.util.Random;

public class GameController implements Runnable {

    public enum GameState {
        COUNTDOWN, RUNNING, GAME_OVER
    }

    private Thread thread;
    private boolean isPlaying;
    private GameState gameState;
    private int countdownTimer;
    private GameView gameView;
    private GameActivity gameActivity;

    private int screenX, screenY, mapWidth, mapHeight;
    private final int cellSize = 50;
    private ArrayList<Point> snake;
    private Point food;
    private int direction = 3; // 0=up,1=right,2=down,3=left
    private int score = 0;

    public GameController(GameActivity gameActivity, GameView gameView, int screenX, int screenY) {
        this.gameActivity = gameActivity;
        this.gameView = gameView;
        this.screenX = screenX;
        this.screenY = screenY;
        this.mapWidth = screenX / cellSize;
        this.mapHeight = screenY / cellSize;
        initGame();
    }

    private void initGame() {
        snake = new ArrayList<>();
        snake.add(new Point(5, 5));
        spawnFood();
        score = 0;
        gameState = GameState.COUNTDOWN;
        countdownTimer = 3;
    }

    private void spawnFood() {
        Random random = new Random();
        food = new Point(random.nextInt(mapWidth), random.nextInt(mapHeight));
    }

    @Override
    public void run() {
        while (isPlaying) {
            switch (gameState) {
                case COUNTDOWN:
                    draw();
                    sleep(1000); // 1 second delay for countdown
                    countdownTimer--;
                    if (countdownTimer == 0) {
                        gameState = GameState.RUNNING;
                    }
                    break;
                case RUNNING:
                    update();
                    draw();
                    sleep(150);
                    break;
                case GAME_OVER:
                    draw();
                    sleep(1000); // Keep drawing game over screen
                    break;
            }
        }
    }

    private void update() {
        if (gameState != GameState.RUNNING) return;

        Point head = snake.get(0);
        Point newHead = new Point(head.x, head.y);
        switch (direction) {
            case 0: newHead.y--; break;
            case 1: newHead.x++; break;
            case 2: newHead.y++; break;
            case 3: newHead.x--; break;
        }
        snake.add(0, newHead);

        if (newHead.equals(food)) {
            score++;
            spawnFood();
        } else {
            snake.remove(snake.size() - 1);
        }

        if (newHead.x < 0 || newHead.y < 0 ||
                newHead.x >= mapWidth || newHead.y >= mapHeight) {
            gameState = GameState.GAME_OVER;
            isPlaying = false;
            gameActivity.showRestartButton();

            return;
        }

        for (int i = 1; i < snake.size(); i++) {
            if (newHead.equals(snake.get(i))) {
                gameState = GameState.GAME_OVER;
                isPlaying = false;
                Log.d("GameController", "Game Over! Calling showRestartButton.");
                gameActivity.showRestartButton();
                return;
            }
        }
    }

    private void draw() {
        gameView.draw(snake, food, score, mapWidth, mapHeight, gameState, countdownTimer);
    }

    private void sleep(long millis) {
        try {
            Thread.sleep(millis);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void pause() {
        isPlaying = false;
        try {
            thread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void resume() {
        isPlaying = true;
        thread = new Thread(this);
        thread.start();
    }

    public void setDirection(int newDirection) {
        if (Math.abs(direction - newDirection) != 2) {
            direction = newDirection;
        }
    }

    public void restartGame() {
        initGame();
        resume();
    }
}
