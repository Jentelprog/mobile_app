package com.example.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.widget.ProgressBar;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class loader extends AppCompatActivity {
    final int[] progress = {0};
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
        final ProgressBar bar = (ProgressBar) findViewById(R.id.progress);
        final Handler handler = new Handler();
        Thread worker = new Thread(new Runnable() {
            public void run() {
                while (progress[0] < 100) {
                    try {
// simule un traitement long

                        Thread.sleep(100);

                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                    handler.post(new Runnable() {
                        public void run() {
                            bar.setProgress(++progress[0]);
                        }
                    });
                }
                // Create an Intent to switch activities
                Intent intent = new Intent(loader.this, response.class);

                // Start the new activity
                startActivity(intent);
            }
        });
        worker.start();
    }
}