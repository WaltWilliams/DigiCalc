package com.woodlandcoders.digicalc;

import android.os.Build;
import android.os.Bundle;
import android.os.StrictMode;

import com.google.android.material.tabs.TabLayout;
import androidx.viewpager.widget.ViewPager;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
//        StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
//            builder.detectNonSdkApiUsage();
//        }
//        StrictMode.setVmPolicy(builder.penaltyLog().build());

        setContentView(R.layout.activity_main);

        SectionsPagerAdapter sectionsPagerAdapter = new SectionsPagerAdapter(this, getSupportFragmentManager());
        ViewPager viewPager = findViewById(R.id.view_pager);
        viewPager.setAdapter(sectionsPagerAdapter);

        TabLayout tabs = findViewById(R.id.tabs);
        tabs.setupWithViewPager(viewPager);
    }

}