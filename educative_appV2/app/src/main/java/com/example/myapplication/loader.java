package com.example.myapplication;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.widget.ProgressBar;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class loader extends AppCompatActivity {
    int progress = 0;
    ProgressBar bar;
    Handler handler = new Handler();
    Thread worker;

    private static final String PREFS_NAME = "ProgressPrefs";
    private static final String KEY_PROGRESS = "progress_value";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_loader);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main2), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        bar = findViewById(R.id.progress);

        // Load saved progress
        SharedPreferences prefs = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        progress = prefs.getInt(KEY_PROGRESS, 0);
        bar.setProgress(progress);

        startWorker();
    }

    private void startWorker() {
        worker = new Thread(() -> {
            while (progress < 100) {
                try {
                    Thread.sleep(300);
                } catch (InterruptedException e) {
                    return; // exit thread if interrupted
                }

                handler.post(() -> {
                    progress++;
                    bar.setProgress(progress);

                    if (progress >= 100) {
                        // Move to response activity
                        Intent intent = new Intent(loader.this, response.class);
                        startActivity(intent);
                        finish();
                    }
                });
            }
        });
        worker.start();
    }

    @Override
    protected void onPause() {
        super.onPause();

        // Save progress when app is minimized or closed
        SharedPreferences prefs = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putInt(KEY_PROGRESS, progress);
        editor.apply();

        // Stop the thread safely
        if (worker != null && worker.isAlive()) {
            worker.interrupt();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

        // Reload progress and restart thread if not finished
        SharedPreferences prefs = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        progress = prefs.getInt(KEY_PROGRESS, progress);
        bar.setProgress(progress);

        if (progress < 100) {
            startWorker();
        }
    }
}
