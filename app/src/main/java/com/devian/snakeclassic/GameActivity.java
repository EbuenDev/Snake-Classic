package com.devian.snakeclassic;

import android.content.Intent;
import android.graphics.Point;
import android.os.Bundle;
import android.view.Display;
import android.view.View;
import android.util.Log;
import android.view.ViewTreeObserver;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.devian.snakeclassic.controller.GameController;
import com.devian.snakeclassic.view.GameView;

public class GameActivity extends AppCompatActivity {

    private GameView gameView;
    private GameController gameController;
    private Button restartButton,  mainMenuButton;
    private View gameOverButtonsLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        gameView = findViewById(R.id.gameView);
        final View dpadLayout = findViewById(R.id.dpadLayout);
        restartButton = findViewById(R.id.restartButton);
        mainMenuButton = findViewById(R.id.mainMenuButton);
        gameOverButtonsLayout = findViewById(R.id.gameOverButtonsLayout);

        dpadLayout.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                dpadLayout.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                int dpadHeight = dpadLayout.getHeight();

                Display display = getWindowManager().getDefaultDisplay();
                Point size = new Point();
                display.getSize(size);

                gameController = new GameController(GameActivity.this, gameView, size.x, size.y - dpadHeight);
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
                gameOverButtonsLayout.setVisibility(View.GONE);
            }
        });

        mainMenuButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                gameOverButtonsLayout.setVisibility(View.GONE);
                Intent intent = new Intent(GameActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }

    public void showRestartButton() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Log.d("GameActivity", "Showing game over buttons.");
                gameOverButtonsLayout.setVisibility(View.VISIBLE);
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
