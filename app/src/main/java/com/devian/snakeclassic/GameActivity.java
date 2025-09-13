package com.devian.snakeclassic;

import android.content.Intent;
import android.graphics.Point;
import android.os.Bundle;
import android.view.Display;
import android.view.View;
import android.util.Log;
import android.view.ViewTreeObserver;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.devian.snakeclassic.controller.GameController;
import com.devian.snakeclassic.view.GameView;

public class GameActivity extends AppCompatActivity {

    private GameView gameView;
    private GameController gameController;
    private Button restartButton, mainMenuButton;
    private LinearLayout gameOverLayout; // Changed from View to LinearLayout
    private TextView finalScoreTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        gameView = findViewById(R.id.gameView);
        final View dpadLayout = findViewById(R.id.dpadLayout);
        restartButton = findViewById(R.id.restartButton);
        mainMenuButton = findViewById(R.id.mainMenuButton);

        // Fixed: Remove 'LinearLayout' declaration to use the class field
        gameOverLayout = findViewById(R.id.gameOverLayout);
        finalScoreTextView = findViewById(R.id.finalScoreTextView);

        // Debug: Check if findViewById worked
        if (gameOverLayout == null) {
            Log.e("GameActivity", "Failed to find gameOverLayout in layout!");
        }
        if (finalScoreTextView == null) {
            Log.e("GameActivity", "Failed to find finalScoreTextView in layout!");
        }

        dpadLayout.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                dpadLayout.getViewTreeObserver().removeOnGlobalLayoutListener(this);

                View topSpacer = findViewById(R.id.topSpacer);
                int dpadHeight = dpadLayout.getHeight();
                int topSpacerHeight = topSpacer.getHeight();
                int margins = 16; // Account for 8dp margin on each side

                Display display = getWindowManager().getDefaultDisplay();
                Point size = new Point();
                display.getSize(size);

                // Calculate available game area
                int gameWidth = size.x - margins;
                int gameHeight = size.y - dpadHeight - topSpacerHeight - margins;

                gameController = new GameController(GameActivity.this, gameView, gameWidth, gameHeight);
                gameView.setGameController(gameController);
                gameView.resume();

                Button upButton = findViewById(R.id.upButton);
                Button downButton = findViewById(R.id.downButton);
                Button leftButton = findViewById(R.id.leftButton);
                Button rightButton = findViewById(R.id.rightButton);

                upButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        gameController.setDirection(0);
                    }
                });

                downButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        gameController.setDirection(2);
                    }
                });

                leftButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        gameController.setDirection(3);
                    }
                });

                rightButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        gameController.setDirection(1);
                    }
                });
            }
        });

        restartButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                gameController.restartGame();
                gameOverLayout.setVisibility(View.GONE);
            }
        });

        mainMenuButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                gameOverLayout.setVisibility(View.GONE);
                Intent intent = new Intent(GameActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
            }
        });
    }

    public void showRestartButton(final int finalScore) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Log.d("GameActivity", "Showing game over screen.");
                finalScoreTextView.setText("Final Score: " + finalScore);
                gameOverLayout.setVisibility(View.VISIBLE);
            }
        });
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (gameController != null) {
            gameView.pause();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
    }
}