package com.example.calendar;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class SettingsActivity extends AppCompatActivity
{
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        findViewById(R.id.settingsCancelButton).setOnClickListener(l -> goToMainActivity());
        findViewById(R.id.deleteAllEventsButton).setOnClickListener(l ->
        {
            EventManager.clearEvents();
            Toast.makeText(this, "Deleted all events", Toast.LENGTH_SHORT).show();
        });
    }

    private void goToMainActivity()
    {
        Intent intent = new Intent(this, MainActivity.class);
        this.startActivity(intent);
    }
}