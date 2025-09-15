package com.devian.snakeclassic.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.RectF;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import androidx.core.content.res.ResourcesCompat;

import com.devian.snakeclassic.MusicManager;
import com.devian.snakeclassic.R;
import com.devian.snakeclassic.controller.GameController;

import java.util.ArrayList;

public class GameView extends SurfaceView {

    private GameController gameController;
    private Paint paint;
    private SurfaceHolder holder;
    private Typeface customFont, countdownFont;
    private final int cellSize = 50;

    private MusicManager musicManager;

    // ➡ Animation fields
//    private ArrayList<PointF> smoothSnake; // Smooth positions for snake segments
//    private PointF smoothFood; // Smooth position for food
    private float animationProgress = 0f; // 0.0 to 1.0
    private boolean isAnimating = false;
//    private ArrayList<Point> lastSnakePositions;
//    private Point lastFoodPosition;

    // Custom PointF class for smooth positions
    private static class PointF {
        float x, y;
        PointF(float x, float y) {
            this.x = x;
            this.y = y;
        }
    }

    // ➡ Added paint objects for custom drawing
    private Paint snakePaint;
    private Paint eyePaint;
    private Paint foodPaint;
    private RectF rectF; // For drawing rounded rectangles

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
        paint.setAntiAlias(true);

        // Initialize custom paint objects
        snakePaint = new Paint();
        snakePaint.setAntiAlias(true);

        eyePaint = new Paint();
        eyePaint.setAntiAlias(true);
        eyePaint.setColor(Color.BLACK);

        foodPaint = new Paint();
        foodPaint.setAntiAlias(true);

        rectF = new RectF();

        // Initialize animation objects
//        smoothSnake = new ArrayList<>();
//        smoothFood = new PointF(0, 0);
//        lastSnakePositions = new ArrayList<>();

        try {
            countdownFont = ResourcesCompat.getFont(getContext(), R.font.pixel_countdown);
        } catch (Exception e) {
            countdownFont = Typeface.DEFAULT_BOLD;
        }

        try {
            customFont = ResourcesCompat.getFont(getContext(), R.font.solderwood_regular);
        } catch (Exception e) {
            customFont = Typeface.DEFAULT_BOLD;
        }
    }

    // Method to update animation progress (call this from your game loop)
    public void updateAnimation(float deltaTime) {
        if (isAnimating) {
            animationProgress += deltaTime * 3.0f; // Adjust speed (3.0f = 3x faster)
            if (animationProgress >= 1.0f) {
                animationProgress = 1.0f;
                isAnimating = false;
            }
        }
    }

    // Method to start animation when snake moves
//    private void startAnimation(ArrayList<Point> newSnake, Point newFood) {
//        if (lastSnakePositions.isEmpty()) {
//            // First frame, no animation
//            initializeSmoothPositions(newSnake, newFood);
//            lastSnakePositions = new ArrayList<>(newSnake);
//            lastFoodPosition = new Point(newFood.x, newFood.y);
//            return;
//        }
//
//        // Check if positions changed
//        boolean snakeChanged = !newSnake.equals(lastSnakePositions);
//        boolean foodChanged = !newFood.equals(lastFoodPosition);
//
//        if (snakeChanged || foodChanged) {
//            animationProgress = 0f;
//            isAnimating = true;
//            lastSnakePositions = new ArrayList<>(newSnake);
//            lastFoodPosition = new Point(newFood.x, newFood.y);
//        }
//    }

//    private void initializeSmoothPositions(ArrayList<Point> snake, Point food) {
//        smoothSnake.clear();
//        for (Point p : snake) {
//            smoothSnake.add(new PointF(p.x, p.y));
//        }
//        smoothFood.x = food.x;
//        smoothFood.y = food.y;
//    }

//    private void updateSmoothPositions(ArrayList<Point> targetSnake, Point targetFood) {
//        // Ensure we have the right number of segments
//        while (smoothSnake.size() < targetSnake.size()) {
//            Point lastTarget = targetSnake.get(smoothSnake.size());
//            smoothSnake.add(new PointF(lastTarget.x, lastTarget.y));
//        }
//        while (smoothSnake.size() > targetSnake.size()) {
//            smoothSnake.remove(smoothSnake.size() - 1);
//        }
//
//        // Interpolate snake positions
//        for (int i = 0; i < smoothSnake.size(); i++) {
//            PointF current = smoothSnake.get(i);
//            Point target = targetSnake.get(i);
//
//            // Smooth interpolation using easing function
//            float t = easeInOutQuad(animationProgress);
//            current.x = lerp(current.x, target.x, t);
//            current.y = lerp(current.y, target.y, t);
//        }
//
//        // Interpolate food position
//        float t = easeInOutQuad(animationProgress);
//        smoothFood.x = lerp(smoothFood.x, targetFood.x, t);
//        smoothFood.y = lerp(smoothFood.y, targetFood.y, t);
//    }

    // Linear interpolation
//    private float lerp(float start, float end, float t) {
//        return start + (end - start) * t;
//    }

    // Easing function for smoother animation
//    private float easeInOutQuad(float t) {
//        return t < 0.5f ? 2f * t * t : -1f + (4f - 2f * t) * t;
//    }

    private void drawSnakeHead(Canvas canvas, float x, float y, int direction) {
        float left = x * cellSize;
        float top = y * cellSize;
        float right = left + cellSize;
        float bottom = top + cellSize;

        // Draw head as rounded rectangle
        snakePaint.setColor(Color.rgb(34, 139, 34)); // Forest Green
        rectF.set(left + 2, top + 2, right - 2, bottom - 2);
        canvas.drawRoundRect(rectF, 8, 8, snakePaint);

        // Draw eyes based on direction
        eyePaint.setColor(Color.WHITE);
        int eyeSize = 6;
        int eyeOffset = 12;

        switch (direction) {
            case 0: // Moving right
                canvas.drawCircle(right - eyeOffset, top + eyeOffset, eyeSize, eyePaint);
                canvas.drawCircle(right - eyeOffset, bottom - eyeOffset, eyeSize, eyePaint);
                eyePaint.setColor(Color.BLACK);
                canvas.drawCircle(right - eyeOffset + 2, top + eyeOffset, 2, eyePaint);
                canvas.drawCircle(right - eyeOffset + 2, bottom - eyeOffset, 2, eyePaint);
                break;
            case 1: // Moving down
                canvas.drawCircle(left + eyeOffset, bottom - eyeOffset, eyeSize, eyePaint);
                canvas.drawCircle(right - eyeOffset, bottom - eyeOffset, eyeSize, eyePaint);
                eyePaint.setColor(Color.BLACK);
                canvas.drawCircle(left + eyeOffset, bottom - eyeOffset + 2, 2, eyePaint);
                canvas.drawCircle(right - eyeOffset, bottom - eyeOffset + 2, 2, eyePaint);
                break;
            case 2: // Moving left
                canvas.drawCircle(left + eyeOffset, top + eyeOffset, eyeSize, eyePaint);
                canvas.drawCircle(left + eyeOffset, bottom - eyeOffset, eyeSize, eyePaint);
                eyePaint.setColor(Color.BLACK);
                canvas.drawCircle(left + eyeOffset - 2, top + eyeOffset, 2, eyePaint);
                canvas.drawCircle(left + eyeOffset - 2, bottom - eyeOffset, 2, eyePaint);
                break;
            case 3: // Moving up
                canvas.drawCircle(left + eyeOffset, top + eyeOffset, eyeSize, eyePaint);
                canvas.drawCircle(right - eyeOffset, top + eyeOffset, eyeSize, eyePaint);
                eyePaint.setColor(Color.BLACK);
                canvas.drawCircle(left + eyeOffset, top + eyeOffset - 2, 2, eyePaint);
                canvas.drawCircle(right - eyeOffset, top + eyeOffset - 2, 2, eyePaint);
                break;
        }
    }

    private void drawSnakeBody(Canvas canvas, float x, float y) {
        float left = x * cellSize;
        float top = y * cellSize;
        float right = left + cellSize;
        float bottom = top + cellSize;

        // Draw body as rounded rectangle with gradient effect
        snakePaint.setColor(Color.rgb(50, 205, 50)); // Lime Green
        rectF.set(left + 3, top + 3, right - 3, bottom - 3);
        canvas.drawRoundRect(rectF, 6, 6, snakePaint);

        // Add inner highlight for 3D effect
        snakePaint.setColor(Color.rgb(144, 238, 144)); // Light Green
        rectF.set(left + 8, top + 8, right - 8, bottom - 8);
        canvas.drawRoundRect(rectF, 3, 3, snakePaint);
    }

    private void drawFood(Canvas canvas, float x, float y) {
        float left = x * cellSize;
        float top = y * cellSize;
        float centerX = left + cellSize / 2f;
        float centerY = top + cellSize / 2f;

        // Add subtle pulsing animation to food
        float pulseScale = 1.0f + 0.1f * (float)Math.sin(System.currentTimeMillis() * 0.005f);
        float radius = (cellSize / 3f) * pulseScale;

        // Draw apple-like food
        foodPaint.setColor(Color.rgb(255, 69, 0)); // Red Orange
        canvas.drawCircle(centerX, centerY + 2, radius, foodPaint);

        // Highlight
        foodPaint.setColor(Color.rgb(255, 160, 122)); // Light Salmon
        canvas.drawCircle(centerX - 4, centerY - 2, radius * 0.5f, foodPaint);

        // Stem (small brown rectangle)
        foodPaint.setColor(Color.rgb(139, 69, 19)); // Saddle Brown
        canvas.drawRect(centerX - 2, top + 8, centerX + 2, top + 16, foodPaint);

        // Leaf (small green oval)
        foodPaint.setColor(Color.rgb(34, 139, 34)); // Forest Green
        rectF.set(centerX + 2, top + 6, centerX + 8, top + 14);
        canvas.drawOval(rectF, foodPaint);
    }

    private int getDirection(ArrayList<Point> snake) {
        if (snake.size() < 2) return 0; // Default right

        Point head = snake.get(0);
        Point neck = snake.get(1);

        // Calculate the difference to determine direction
        int dx = head.x - neck.x;
        int dy = head.y - neck.y;

        // Determine direction based on the difference
        if (dx > 0) return 0; // Moving right
        else if (dx < 0) return 2; // Moving left
        else if (dy > 0) return 1; // Moving down
        else if (dy < 0) return 3; // Moving up

        return 0; // Default right
    }

    public void setGameController(GameController gameController) {
        this.gameController = gameController;
    }

    private int mapWidth, mapHeight, topOffset;

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
            // Draw yellow checkerboard background
            for (int x = 0; x < mapWidth; x++) {
                for (int y = 0; y < mapHeight; y++) {
                    // Alternate colors based on grid position - yellow theme
                    if ((x + y) % 2 == 0) {
                        paint.setColor(Color.rgb(238, 203, 122)); // Light golden yellow
                    } else {
                        paint.setColor(Color.rgb(218, 188, 107)); // Darker golden yellow
                    }

                    // Draw each grid cell
                    int left = mapLeft + x * cellSize;
                    int top = mapTop + y * cellSize;
                    int right = left + cellSize;
                    int bottom = top + cellSize;
                    canvas.drawRect(left, top, right, bottom, paint);
                }
            }

            switch (gameState) {
                case COUNTDOWN:
                    String countdownText;
                    if (gameController.currentCountdownDisplay > 0) {
                        countdownText = String.valueOf(gameController.currentCountdownDisplay);
                    } else {
                        countdownText = "GO!";
                    }

                    paint.setColor(Color.WHITE);
                    paint.setTextSize(200);
                    paint.setTypeface((countdownFont != null) ? countdownFont : Typeface.DEFAULT_BOLD);
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
                    // Get snake direction for head orientation
                    int direction = getDirection(snake);

                    // Draw snake using actual positions (no animation for now)
                    canvas.save();
                    canvas.translate(mapLeft, mapTop);

                    for (int i = 0; i < snake.size(); i++) {
                        Point p = snake.get(i);

                        if (i == 0) {
                            // Draw snake head with eyes
                            drawSnakeHead(canvas, p.x, p.y, direction);
                        } else {
                            // Draw snake body
                            drawSnakeBody(canvas, p.x, p.y);
                        }
                    }

                    // Draw food
                    drawFood(canvas, food.x, food.y);

                    canvas.restore();

                    // Draw score
                    paint.setColor(Color.WHITE);
                    paint.setTextSize(60);
                    paint.setTextAlign(Paint.Align.LEFT);
                    canvas.drawText("Score: " + score, 50, 80, paint);
                    break;

                case GAME_OVER:
                    // add game over state drawing here if needed
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