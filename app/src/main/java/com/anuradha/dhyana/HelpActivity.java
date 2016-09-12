package com.anuradha.dhyana;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

/**
 * This activity is called when the help is pressed from menu.
 */
public class HelpActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.help_layout);
        String[] help_array = getResources().getStringArray(R.array.help_array);
        String[] features_array = getResources().getStringArray(R.array.features_array);
        ListView help_list = (ListView) findViewById(R.id.help_list);
        ListView features_list = (ListView) findViewById(R.id.features_list);
        help_list.setAdapter(new ArrayAdapter<>(this, R.layout.custom_bulleted_list, help_array));
        features_list.setAdapter(new ArrayAdapter<>(this, R.layout.custom_bulleted_list, features_array));
    }

    /**
     * This method creates a menu bar for the app.
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.menu_help, menu);
        return super.onCreateOptionsMenu(menu);
    }

    /**
     * This method is called when the menu options are clicked.
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.contact_id:
                String recepientEmail = "anoo_radha@outlook.com";
                Intent emailIntent = new Intent(Intent.ACTION_SENDTO);
                emailIntent.setData(Uri.parse("mailto:" + recepientEmail));
                emailIntent.putExtra(Intent.EXTRA_EMAIL, new String[]{"anoo_radha@outlook.com"});
                emailIntent.putExtra(Intent.EXTRA_SUBJECT, getString(R.string.app_name));
                emailIntent.putExtra(Intent.EXTRA_TEXT, getString(R.string.email_body));
                if (emailIntent.resolveActivity(getPackageManager()) != null) {
                    startActivity(Intent.createChooser(emailIntent, getString(R.string.choose_one)));
                } else {
                    Toast.makeText(this, getString(R.string.no_mail_apps), Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.rating_id: {
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setData(Uri.parse("https://play.google.com/store/apps/details?id=com.anuradha.dhyana"));
                startActivity(intent);
                break;
            }
            default:
                return super.onOptionsItemSelected(item);
        }
        return super.onOptionsItemSelected(item);
    }
}
