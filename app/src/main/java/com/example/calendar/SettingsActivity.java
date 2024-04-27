package com.example.calendar;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
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
        findViewById(R.id.saveSettingsButton).setOnClickListener(l ->
        {
            saveSettings();
            goToMainActivity();
        });
        findViewById(R.id.deleteAllEventsButton).setOnClickListener(l ->
        {
            MainActivity.deleteAllEvents(this);
            Toast.makeText(this, "Deleted all events", Toast.LENGTH_SHORT).show();
        });
    }

    private void goToMainActivity()
    {
        Intent intent = new Intent(this, MainActivity.class);
        this.startActivity(intent);
    }

    private void saveSettings()
    {
        SharedPreferences sharedPref = getPreferences(Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putBoolean(getString(R.string.enable_notifications_settings_key), true);
        editor.apply();
    }
}