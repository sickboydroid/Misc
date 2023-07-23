package com.tangledbytes.nothing;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    private int tapCount =0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        TextView tvNothing = findViewById(R.id.tv_nothing);
        tvNothing.setOnClickListener((View view) -> {
            tapCount++;
            Toast.makeText(MainActivity.this, R.string.nothing_unlocked, Toast.LENGTH_SHORT).show();
            if(tapCount == 15)
                launchSecretActivity();
        });
    }

    private void launchSecretActivity() {
        Intent secretActivity = new Intent(this, SecretActivity.class);
        startActivity(secretActivity);
    }
}