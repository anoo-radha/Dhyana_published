package com.anuradha.dhyana;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Html;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

/**
 * This method is called when 'benefits' option in the menu is clicked.
 */
public class BenefitsActivity extends AppCompatActivity {
    ListView list;
    Integer[] imageId = {
            R.drawable.benefits1,
            R.drawable.benefits2,
            R.drawable.benefits3,
            R.drawable.benefits4,
            R.drawable.benefits5,
            R.drawable.benefits6};
    String[] benefits;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.benefits_layout);
        benefits = getResources().getStringArray(R.array.benefits_title);
        com.anuradha.dhyana.CustomList adapter = new com.anuradha.dhyana.CustomList(BenefitsActivity.this, benefits, imageId);
        list = (ListView) findViewById(R.id.list);
        list.setAdapter(adapter);
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                benefitsDetail(position);
            }
        });
    }

    /**
     * This method pops a AlertDialog for each list when it is clicked.
     *
     * @param position of the list item
     */
    public void benefitsDetail(int position) {
        AlertDialog alertDialog = new AlertDialog.Builder(this).create();
        String[] benefits_desc = getResources().getStringArray(R.array.benefits_description);
        String[] benefits_title = getResources().getStringArray(R.array.benefits_title);
        TextView tv = new TextView(this);
        CharSequence styledTitle = Html.fromHtml("<b>"+benefits_title[position]+"</b>");
        tv.setTextSize(TypedValue.COMPLEX_UNIT_SP, 12);
        tv.setTextColor(Color.parseColor("#000000"));
        tv.setGravity(Gravity.CENTER);
        tv.setText(styledTitle);
        alertDialog.setCustomTitle(tv);
        CharSequence styledText = Html.fromHtml(benefits_desc[position]);
        alertDialog.setMessage(styledText);
        alertDialog.setButton(DialogInterface.BUTTON_NEUTRAL, "OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int i) {
                dialog.cancel();
            }
        });
        alertDialog.show();
    }

    /**
     * This method creates a menu bar for the app.
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    /**
     * This method is called when the menu options are clicked.
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.howto_id:
                Intent intent1 = new Intent(this, HowtoActivity.class);
                startActivity(intent1);
                break;
            case R.id.benefits_id:
                Intent intent2 = new Intent(this, SettingsDoneActivity.class);
                startActivity(intent2);
                break;
            case R.id.help_id:
                Intent intent3 = new Intent(this, HelpActivity.class);
                startActivity(intent3);
                break;
            case R.id.links_id:
                Intent intent4 = new Intent(this, LinksActivity.class);
                startActivity(intent4);
                break;
            case R.id.share_id:
                Intent intent_share = new Intent(Intent.ACTION_SEND);
                intent_share.setType("text/plain");
                intent_share.putExtra(Intent.EXTRA_SUBJECT, getString(R.string.app_name));
                intent_share.putExtra(Intent.EXTRA_TEXT, getString(R.string.share_body));
                startActivity(Intent.createChooser(intent_share, getString(R.string.choose_one)));
                break;
            default:
                return super.onOptionsItemSelected(item);
        }
        return super.onOptionsItemSelected(item);
    }
}
