package com.devian.snakeclassic;

import android.animation.ValueAnimator;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class SplashActivity extends AppCompatActivity {

    private View progressBarFill;
    private View progressGlow;
    private TextView progressText;
    private TextView loadingText;
    private ValueAnimator progressAnimator;

    private int totalProgressWidth;
    private Handler handler = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        // Initialize views
        progressBarFill = findViewById(R.id.progressBarFill);
        progressGlow = findViewById(R.id.progressGlow);
        progressText = findViewById(R.id.progressText);
        loadingText = findViewById(R.id.loadingText);

        // Get the total width of progress bar after layout
        progressBarFill.post(new Runnable() {
            @Override
            public void run() {
                totalProgressWidth = ((View) progressBarFill.getParent()).getWidth();
                startProgressAnimation();
            }
        });
    }

    private void startProgressAnimation() {
        // Create progress animation
        progressAnimator = ValueAnimator.ofInt(0, 100);
        progressAnimator.setDuration(3000); // 3 seconds loading time
        progressAnimator.setInterpolator(new AccelerateDecelerateInterpolator());

        progressAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                int progress = (int) animation.getAnimatedValue();
                updateProgress(progress);
            }
        });

        // Start the animation after a short delay
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                progressAnimator.start();
                animateLoadingText();

                // Launch MainActivity when animation completes
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        launchMainActivity();
                    }
                }, 3200); // Slight delay after progress completes
            }
        }, 500);
    }

    private void updateProgress(int progress) {
        // Update progress bar width
        int newWidth = (totalProgressWidth * progress) / 100;
        ViewGroup.LayoutParams params = progressBarFill.getLayoutParams();
        params.width = newWidth;
        progressBarFill.setLayoutParams(params);

        // Update progress text
        progressText.setText(progress + "%");

        // Update glow position
        if (progress > 0) {
            progressGlow.setVisibility(View.VISIBLE);
            progressGlow.setX(progressBarFill.getX() + newWidth - 10);
        }

        // Change loading text based on progress
        updateLoadingText(progress);
    }

    private void updateLoadingText(int progress) {
        if (progress < 30) {
            loadingText.setText("Initializing...");
        } else if (progress < 60) {
            loadingText.setText("Loading assets...");
        } else if (progress < 90) {
            loadingText.setText("Preparing game...");
        } else {
            loadingText.setText("Almost ready!");
        }
    }

    private void animateLoadingText() {
        // Create a subtle pulsing animation for the loading text
        loadingText.animate()
                .alpha(0.5f)
                .setDuration(800)
                .withEndAction(new Runnable() {
                    @Override
                    public void run() {
                        loadingText.animate()
                                .alpha(1f)
                                .setDuration(800)
                                .withEndAction(new Runnable() {
                                    @Override
                                    public void run() {
                                        if (!isFinishing()) {
                                            animateLoadingText(); // Repeat animation
                                        }
                                    }
                                })
                                .start();
                    }
                })
                .start();
    }

    private void launchMainActivity() {
        Intent intent = new Intent(SplashActivity.this, MainActivity.class);
        startActivity(intent);
        finish();

        // Add smooth transition
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (progressAnimator != null) {
            progressAnimator.cancel();
        }
        handler.removeCallbacksAndMessages(null);
    }
}