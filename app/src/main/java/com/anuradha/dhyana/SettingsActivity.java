package com.anuradha.dhyana;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import java.util.Locale;

/**
 * This method is called when 'settings' option in the menu is clicked from SettingsDone activity.
 */
public class SettingsActivity extends AppCompatActivity {
    public int timer = 5;
    SharedPreferences preferences;
    private ArrayAdapter<String> dataAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.settings_layout);
        preferences = getSharedPreferences("timerValues", MODE_PRIVATE);
        addItemsOnTuneSpinner();
        TextView txt = (TextView) findViewById(R.id.timer_text_view);
        try {
            timer = Integer.parseInt(preferences.getString("time", "5"));
        } catch (NumberFormatException nfe) {
            System.out.println("Could not parse " + nfe);
        }
        txt.setText(preferences.getString("time", "5"));
        Spinner spnr = (Spinner) findViewById(R.id.tune_spinner);
        String value = preferences.getString("tune", "bell");
        spnr.setSelection((dataAdapter).getPosition(value));
        ToggleButton tglButton = (ToggleButton) findViewById(R.id.toggleButton);
        tglButton.setChecked(preferences.getBoolean("mode", false));
    }

    /**
     * This method is to add items into spinner
     */
    public void addItemsOnTuneSpinner() {
        Spinner spinner = (Spinner) findViewById(R.id.tune_spinner);
        // Creating an ArrayAdapter using the string array and a default spinner layout
        dataAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, getResources().getStringArray(R.array.tune_array));
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(dataAdapter);
    }

    /**
     * This method is called when the + button is clicked.
     */
    public void increment(View view) {
        Button btn = (Button) findViewById(R.id.increment_button);
        if (timer >= 60) {
            btn.setEnabled(false);
            CharSequence text = getString(R.string.increment_toast);
            Toast.makeText(this, text, Toast.LENGTH_SHORT).show();
        } else {
            findViewById(R.id.decrement_button).setEnabled(true);
            timer = timer + 5;
            displayTime();
        }
    }

    /**
     * This method is called when the - button is clicked.
     */
    public void decrement(View view) {
        Button btn = (Button) findViewById(R.id.decrement_button);
        if (timer <= 5) {
            btn.setEnabled(false);
            CharSequence text = getString(R.string.decrement_toast);
            Toast.makeText(this, text, Toast.LENGTH_SHORT).show();
        } else {
            findViewById(R.id.increment_button).setEnabled(true);
            timer = timer - 5;
            displayTime();
        }
    }

    /**
     * This method displays the given time on the screen.
     */
    private void displayTime() {
        TextView quantityTextView = (TextView) findViewById(R.id.timer_text_view);
        quantityTextView.setText(String.format(Locale.US,"%d", timer));
    }

    /**
     * This method is called when 'OK' button is clicked from this activity.
     */
    public void settingsDone(View v) {
        Intent intent = getIntent();
        TextView timerTxt = (TextView) findViewById(R.id.timer_text_view);
        String timerVal = timerTxt.getText().toString();
        int value = 0;
        try {
            value = Integer.parseInt(timerVal);
        } catch (NumberFormatException nfe) {
            System.out.println("Could not parse " + nfe);
        }
        intent.putExtra("Val1", value);
        Spinner mySpinner = (Spinner) findViewById(R.id.tune_spinner);
        String text = mySpinner.getSelectedItem().toString();
        intent.putExtra("Val2", text);
        ToggleButton mode = (ToggleButton) findViewById(R.id.toggleButton);
        intent.putExtra("Val3", mode.isChecked());
        setResult(RESULT_OK, intent);
        finish();
    }

    /**
     * additional storing of preferences that are set on this activity is added to the super onPause().
     */
    @Override
    protected void onPause() {
        super.onPause();
        // Store values between instances
        preferences = getSharedPreferences("timerValues", MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        TextView timerTxt = (TextView) findViewById(R.id.timer_text_view);
        String timerValue = timerTxt.getText().toString();
        Spinner tuneSelected = (Spinner) findViewById(R.id.tune_spinner);
        String tuneValue = tuneSelected.getSelectedItem().toString();
        ToggleButton mode = (ToggleButton) findViewById(R.id.toggleButton);
        boolean modeValue = mode.isChecked();
        editor.putString("time", timerValue);
        editor.putString("tune", tuneValue);
        editor.putBoolean("mode", modeValue);
        editor.apply();
    }
}
