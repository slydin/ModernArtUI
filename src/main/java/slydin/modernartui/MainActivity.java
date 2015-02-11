package slydin.modernartui;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Display;
import android.view.Menu;
import android.view.MenuItem;
import android.view.Surface;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SeekBar;

/**
 * ModernArtUI Pulls inspiration from modern art paintings found from moma.org and transforms the
 * rectangle representations while keeping any shaded blocks constant.
 * Copyright (C) 2015  Jeric Derama
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

public class MainActivity extends ActionBarActivity implements DialogInterface{

    static private final String URL = "http://www.moma.org/";
    static private final String CHOOSER_TEXT = "Load " + URL + " with:";
    static private final String TAG = "modernArtUI-main";
    static private final String[] colors = {"#E8C351","#70ABCD","#F5F5F5","#7C3F6B","#A5B869"};

    LinearLayout mainLayout;
    SeekBar seek;
    ImageView[] imgs;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_2);

        // Get layout and be mindful of orientation
        Display display = ((WindowManager) this.getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay();
        int rotation = display.getRotation();
        switch(rotation){
            case Surface.ROTATION_0:
            case Surface.ROTATION_180:
                mainLayout = (LinearLayout) findViewById(R.id.main);
                break;
            case Surface.ROTATION_90:
            case Surface.ROTATION_270:
                mainLayout = (LinearLayout) findViewById(R.id.main_land);
                break;
        }
        // Get the imageviews
        imgs = new ImageView[5];
        int i = 0;
        while(i < 5){
            if(mainLayout.getChildAt(i) instanceof ImageView){
                imgs[i] = (ImageView) mainLayout.getChildAt(i);
                imgs[i].setBackgroundColor(Color.parseColor(colors[i]));
                i++;
            }
        }

        seek = (SeekBar) findViewById(R.id.seekBar);
        seek.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

                for(int i = 0; i < imgs.length; i++){
                    int colorInt = Color.parseColor(colors[i]);
                    int red = Color.red(colorInt);
                    int blue = Color.blue(colorInt);
                    int green = Color.green(colorInt);
                    int alpha = Color.alpha(colorInt);
                    // add to red if possible
                    int newRed = red;
                    int newBlue = blue;
                    int newGreen = green;
                    if(red + 100 < 255)
                        newRed += progress;
                    else
                        newRed -= progress;
                    // add to blue if possible
                    if(blue + 100 < 255)
                        newBlue += progress;
                    else
                        newBlue -= progress;
                    // remove from green if possible
                    if(green - 100 > 0)
                        newGreen -= progress;
                    else
                        newGreen += progress;
                    if(red != green || green != blue)
                        imgs[i].setBackgroundColor(Color.argb(alpha,newRed,newGreen,newBlue));
                    }
                }


            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.more_information) {
            moreInfoDialog();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void moreInfoDialog(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(R.string.more_info_dialog).setPositiveButton(R.string.more_info_accept, new DialogInterface.OnClickListener(){
            public void onClick(DialogInterface dialog, int id){
                dialog.dismiss();
                Intent browseWeb = new Intent(Intent.ACTION_VIEW, Uri.parse(URL));
                Intent chooseBrowserIntent = Intent.createChooser(browseWeb, CHOOSER_TEXT);
                startActivity(chooseBrowserIntent);

            }
        })
                .setNegativeButton(R.string.more_info_deny, new DialogInterface.OnClickListener(){
                    public void onClick(DialogInterface dialog, int id){
                        dialog.dismiss();
                    }
                });

        builder.create().show();
    }

    @Override
    public void cancel() {

    }

    @Override
    public void dismiss() {

    }
}
