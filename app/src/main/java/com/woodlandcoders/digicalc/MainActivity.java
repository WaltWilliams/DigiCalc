package com.woodlandcoders.digicalc;

import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.os.StrictMode;
import com.google.android.material.tabs.TabLayout;
import androidx.viewpager.widget.ViewPager;
import androidx.appcompat.app.AppCompatActivity;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.OutputStreamWriter;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        

        StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            builder.detectNonSdkApiUsage();
            StrictMode.setVmPolicy(builder.penaltyLog().build());
        }


        setContentView(R.layout.activity_main);

        SectionsPagerAdapter sectionsPagerAdapter = new SectionsPagerAdapter(this, getSupportFragmentManager());
        ViewPager viewPager = findViewById(R.id.view_pager);
        viewPager.setAdapter(sectionsPagerAdapter);

        TabLayout tabs = findViewById(R.id.tabs);
        tabs.setupWithViewPager(viewPager);


        // This bit of code writes the initial json file which stores the
        // choice of upper or lower case of the hexadecimal alpha digits.
        JSONObject jo = new JSONObject();
        File choiceStorageFile = getBaseContext().getFileStreamPath(getResources().getString(R.string.jsonFilename));
        OutputStreamWriter osw = null;
        if(!choiceStorageFile.exists()){
            try {
                osw = new OutputStreamWriter(openFileOutput(getResources().getString(R.string.jsonFilename), Context.MODE_PRIVATE));
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
            try {
                jo.put("caps", false);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            try {
                osw.write(jo.toString());
                osw.flush();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}