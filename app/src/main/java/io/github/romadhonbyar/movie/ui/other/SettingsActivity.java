package io.github.romadhonbyar.movie.ui.other;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.RadioButton;
import android.widget.Switch;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import java.util.Locale;
import java.util.Objects;

import io.github.romadhonbyar.movie.MainActivity;
import io.github.romadhonbyar.movie.R;
import io.github.romadhonbyar.movie.helper.LocaleHelper;
import io.github.romadhonbyar.movie.helper.SharedPrefManager;

import static io.github.romadhonbyar.movie.api.Api.SHARED_PREF_NAME;
import static io.github.romadhonbyar.movie.api.Api.SHARED_SETTING_DAILY;
import static io.github.romadhonbyar.movie.api.Api.SHARED_SETTING_RELEASE;

public class SettingsActivity extends AppCompatActivity {
    private static final String TAG = "SettingsActivity :::";
    private Switch reRelease;
    private Switch reDaily;

    private AlarmReceiver alarmReceiver;

    @Override
    public void onSaveInstanceState(Bundle outState) {
        String lang = SharedPrefManager.getInstance(this).getLang();
        outState.putString("message", lang);
        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);

        String repeatTimeDaily = "07:00";
        String repeatTimeRelease = "06:20";

        if (savedInstanceState != null) {
            String message = savedInstanceState.getString("message");
            LocaleHelper.setLocale(SettingsActivity.this, message);

            SharedPrefManager.getInstance(this).setLang(message);
        } else {
            String lang = SharedPrefManager.getInstance(this).getLang();
            LocaleHelper.setLocale(SettingsActivity.this, lang);

            SharedPrefManager.getInstance(this).setLang(lang);
        }

        setTitle(R.string.settings);
        ActionBar actionBar = getSupportActionBar();
        Objects.requireNonNull(actionBar).setDisplayHomeAsUpEnabled(true);

        alarmReceiver = new AlarmReceiver();

        RadioButton rb_g1 = findViewById(R.id.radio_indonesian);
        RadioButton rb_g2 = findViewById(R.id.radio_english);

        String lang = SharedPrefManager.getInstance(this).getLang();

        if (Objects.equals(lang, "in")) {
            rb_g1.setChecked(true);
            Log.e("1 SettingsActivity", "01 - " + lang + " - " + Locale.getDefault().getLanguage());
        } else {
            rb_g2.setChecked(true);
            Log.e("1 SettingsActivity", "02 - " + lang + " - " + Locale.getDefault().getLanguage());
        }



        /* Reminder */
        SharedPreferences preferences = this.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);

        String isVibrate = preferences.getString(SHARED_SETTING_RELEASE, "off");
        String isSound = preferences.getString(SHARED_SETTING_DAILY, "off");
        Log.wtf(TAG, isSound);
        Log.wtf(TAG, isVibrate);

        reRelease = findViewById(R.id.switchRelease);

        /* Reminder Release */
        if (Objects.equals(isVibrate, "on")) {
            reRelease.setChecked(true);
        } else {
            reRelease.setChecked(false);
        }

        reRelease.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                SharedPreferences.Editor editor = preferences.edit();
                editor.putString(SHARED_SETTING_RELEASE, "on");
                editor.apply();

                alarmReceiver.setRepeatingAlarm(this, AlarmReceiver.TYPE_REPEATING, repeatTimeRelease, "AAA miss you!");
            } else {
                SharedPreferences.Editor editor = preferences.edit();
                editor.putString(SHARED_SETTING_RELEASE, "off");
                editor.apply();

                alarmReceiver.cancelAlarm(this, AlarmReceiver.TYPE_REPEATING);
            }
        });

        if (reRelease.isChecked()) {
            SharedPreferences.Editor editor = preferences.edit();
            editor.putString(SHARED_SETTING_RELEASE, "on");
            editor.apply();

                alarmReceiver.setRepeatingAlarm(this, AlarmReceiver.TYPE_REPEATING, repeatTimeRelease, "AAA miss you!");
        } else {
            SharedPreferences.Editor editor = preferences.edit();
            editor.putString(SHARED_SETTING_RELEASE, "off");
            editor.apply();

            alarmReceiver.cancelAlarm(this, AlarmReceiver.TYPE_REPEATING);
        }

        reDaily = findViewById(R.id.switchDaily);

        /* Daily Reminder  */
        if (Objects.equals(isSound, "on")) {
            reDaily.setChecked(true);
        } else {
            reDaily.setChecked(false);
        }

        reDaily.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                SharedPreferences.Editor editor = preferences.edit();
                editor.putString(SHARED_SETTING_DAILY, "on");
                editor.apply();

                alarmReceiver.setRepeatingAlarm(this, AlarmReceiver.TYPE_REPEATING, repeatTimeDaily, "I miss you!");
            } else {
                SharedPreferences.Editor editor = preferences.edit();
                editor.putString(SHARED_SETTING_DAILY, "off");
                editor.apply();

                alarmReceiver.cancelAlarm(this, AlarmReceiver.TYPE_REPEATING);
            }
        });

        if (reDaily.isChecked()) { //check the current state before we display the screen
            SharedPreferences.Editor editor = preferences.edit();
            editor.putString(SHARED_SETTING_DAILY, "on");
            editor.apply();

            alarmReceiver.setRepeatingAlarm(this, AlarmReceiver.TYPE_REPEATING, repeatTimeDaily, "I miss you!");
        } else {
            SharedPreferences.Editor editor = preferences.edit();
            editor.putString(SHARED_SETTING_DAILY, "off");
            editor.apply();

            alarmReceiver.cancelAlarm(this, AlarmReceiver.TYPE_REPEATING);
        }
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        super.onOptionsItemSelected(item);
        if (item.getItemId() == android.R.id.home) {
            finish();
        }

        return true;
    }

    public void onRadioButtonClicked(View view) {
        boolean checked = ((RadioButton) view).isChecked();
        String languagePref = "in";

        switch (view.getId()) {
            case R.id.radio_indonesian:
                if (checked)
                    languagePref = "in";
                break;
            case R.id.radio_english:
                if (checked)
                    languagePref = "en";
                break;
        }

        reload(languagePref);
    }

    public void reload(String languagePref) {
        LocaleHelper.setLocale(SettingsActivity.this, languagePref);
        recreate();

        SharedPrefManager.getInstance(this).setLang(languagePref);
        Intent refresh = new Intent(this, MainActivity.class);
        startActivity(refresh);
    }
}