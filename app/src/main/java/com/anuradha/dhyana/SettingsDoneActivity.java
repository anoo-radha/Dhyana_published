package com.anuradha.dhyana;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v7.app.AppCompatActivity;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.ImageSpan;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.Locale;

/**
 * This method is called when 'Meditate Now' button is clicked from the Main activity.
 */
public class SettingsDoneActivity extends AppCompatActivity {
    MyCount timerCountDown;
    TextView timerText, tuneText, modeText;
    int value_timer = 0, mode_flag = 0;
    boolean value_silent = false;
    String value_tune = "0";
    AudioManager mode;
    SharedPreferences prefs;
    MediaPlayer mediaPlayer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.settings_done);
        prefs = getSharedPreferences("timerValues", MODE_PRIVATE);
        //set the timer to what was set previously
        value_timer = Integer.parseInt(prefs.getString("time", "5"));
        timerText = (TextView) findViewById(R.id.timer_text);
        setTimerText(value_timer);
        //set the tune to what was set previously
        tuneText = (TextView) findViewById(R.id.tune_text);
        value_tune = prefs.getString("tune", "bell");
        tuneText.setText(String.format(getResources().getString(R.string.tune_message), value_tune));
        //set the mode to what was set previously
        mode = (AudioManager) getBaseContext().getSystemService(Context.AUDIO_SERVICE);
        modeText = (TextView) findViewById(R.id.mode_text);
        value_silent = prefs.getBoolean("mode", false);
        if (value_silent) {
            modeText.setText(R.string.phone_silenced);
        } else {
            modeText.setText(R.string.phone_not_silenced);
        }
        TextView textView = (TextView) findViewById(R.id.hint_text);
        SpannableStringBuilder spantext = new SpannableStringBuilder(getString(R.string.settings_help));
        Bitmap settings_icon = BitmapFactory.decodeResource(getResources(), R.drawable.ic_settings);
        spantext.setSpan(new ImageSpan(this, settings_icon), 55, 56, Spannable.SPAN_INCLUSIVE_INCLUSIVE);
        textView.setText(spantext, TextView.BufferType.SPANNABLE);
    }

    /**
     * This method creates a menu bar for the app.
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.menu_settings, menu);
        return super.onCreateOptionsMenu(menu);
    }

    /**
     * This method is called when the menu options are clicked.
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.settings_timer_id:
                Intent intent = new Intent(this, SettingsActivity.class);
                intent.putExtra("Val1", 5);
                intent.putExtra("Val2", "tune");
                intent.putExtra("Val3", true);
                startActivityForResult(intent, 2404);
                break;
            case R.id.help_id:
                Intent intent3 = new Intent(this, HelpActivity.class);
                startActivity(intent3);
                break;
            default:
                return super.onOptionsItemSelected(item);
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * This method is called when the 'Stop' button is clicked.
     */
    public void stopTimer(View v) {
        String output = String.format(Locale.US, "%02d:%02d:%02d", 0, 0, 0);
        //release the countdowntimer
        if (timerCountDown != null) {
            timerCountDown.cancel();
        }
        timerText.setText(output);
        value_timer = Integer.parseInt(prefs.getString("time", "5"));
        setTimerText(value_timer);
        //check flag and set the ringmode to the original setup
        if (mode_flag != 0) {
            mode.setRingerMode(mode_flag);
            mode_flag = 0;
        }
        //release mediaplayer
        releaseMediaPlayer();
        Button start_button = (Button) findViewById(R.id.start_button);
        start_button.setEnabled(true);
    }

    /**
     * This method is called when the 'Start' button is clicked.
     */
    public void startTimer(View v) {
        //cancel the old countDownTimer if running
        if (timerCountDown != null) {
            timerCountDown.cancel();
        }
        Button start_button = (Button) findViewById(R.id.start_button);
        start_button.setEnabled(false);
        timerCountDown = new MyCount(value_timer * 60000, 1000);
        timerCountDown.start();
        if (value_silent) {
            if (mode.getRingerMode() != 0) {
                //set flag
                mode_flag = mode.getRingerMode();
                mode.setRingerMode(AudioManager.RINGER_MODE_SILENT);
            }
        }
    }

    /**
     * This method is called after the settings activity is finished and returns to this activity
     *
     * @param requestCode of the intent
     * @param resultCode  of the intent
     * @param data        of the intent
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && requestCode == 2404) {
            if (data != null) {
                value_timer = data.getIntExtra("Val1", 5);
                value_tune = data.getStringExtra("Val2");
                value_silent = data.getBooleanExtra("Val3", false);
            }
        }

        tuneText.setText(String.format(getResources().getString(R.string.tune_message), value_tune));
        if (!value_silent) {
            modeText.setText(R.string.phone_not_silenced);
        } else {
            modeText.setText(R.string.phone_silenced);
        }
        if (timerCountDown != null) {
            timerCountDown.cancel();
            Button start_button = (Button) findViewById(R.id.start_button);
            start_button.setEnabled(true);
        }
        setTimerText(value_timer);
    }

    /**
     * This method is to format the timer text according to what is selected in the settings
     *
     * @param value of the timer
     */
    private void setTimerText(int value) {
        if ((value >= 5) && (value <= 60)) {
            int hours = value / 60;
            int minutes = value % 60;
            timerText.setText(String.format(Locale.US,
                    "%s%d:%s%d:00", hours < 10 ? "0" : "", hours, minutes < 10 ? "0" : "", minutes));
        }
    }

    /**
     * This method is to display the timer value
     *
     * @param millis from the countdown timer
     */
    public void displayTimer(long millis) {
        String output;
        long seconds = millis / 1000;
        output = String.format(Locale.US,"%02d:%02d:%02d", seconds / 3600,
                (seconds % 3600) / 60, (seconds % 60));

        timerText.setText(output);
    }

    /**
     * This method releases the mediaplayer if not in use
     */
    private void releaseMediaPlayer() {
        try {
            if (mediaPlayer != null) {
                if (mediaPlayer.isPlaying())
                    mediaPlayer.stop();
                mediaPlayer.release();
                mediaPlayer = null;
            }
        } catch (Exception nfe) {
            System.out.println("Exception in releaseMediaPlayer: " + nfe);

        }
    }

    /**
     * This method release the countdowntimer and mediaplayer instance in addition to the super onDestroy
     */
    @Override
    public void onDestroy() {
        super.onDestroy();
        //release the countdowntimer
        if (timerCountDown != null) {
            timerCountDown.cancel();
        }
        //release mediaplayer
        releaseMediaPlayer();
        //check flag and set the ringmode to the original setup
        if (mode_flag != 0) {
            mode.setRingerMode(mode_flag);
            mode_flag = 0;
        }
    }

    /**
     * This class is for creating the countdowntimer
     */
    public class MyCount extends CountDownTimer {
        public MyCount(long millisInFuture, long countDownInterval) {
            super(millisInFuture, countDownInterval);
        }

        public void onTick(long millis) {
            displayTimer(millis);
        }

        public void onFinish() {
            String output = String.format(Locale.US, "%02d:%02d:%02d", 0, 0, 0);
            timerText.setText(output);
            if (mode_flag != 0) {
                //check flag and set the ringmode to the original
                mode.setRingerMode(mode_flag);
                mode_flag = 0;
            }
            try {
                switch (value_tune) {
                    case "bell":
                        releaseMediaPlayer();
                        mediaPlayer = MediaPlayer.create(SettingsDoneActivity.this, R.raw.bell);
                        break;
                    case "chime":
                        releaseMediaPlayer();
                        mediaPlayer = MediaPlayer.create(SettingsDoneActivity.this, R.raw.chime);
                        break;
                    case "om":
                        releaseMediaPlayer();
                        mediaPlayer = MediaPlayer.create(SettingsDoneActivity.this, R.raw.om);
                        break;
                    default:
                        releaseMediaPlayer();
                        mediaPlayer = MediaPlayer.create(SettingsDoneActivity.this, R.raw.bell);
                        break;
                }
                mediaPlayer.start();
            } catch (Exception nfe) {
                System.out.println("Could not parse " + nfe);

            }
            Button start_button = (Button) findViewById(R.id.start_button);
            start_button.setEnabled(true);
        }
    }
}
