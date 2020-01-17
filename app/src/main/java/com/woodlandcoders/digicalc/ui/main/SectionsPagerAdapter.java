package com.woodlandcoders.digicalc.ui.main;

import android.content.Context;

import androidx.annotation.Nullable;
import androidx.annotation.StringRes;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.woodlandcoders.digicalc.AddBin;
import com.woodlandcoders.digicalc.AddHex;
import com.woodlandcoders.digicalc.AddOct;
import com.woodlandcoders.digicalc.Convert;
import com.woodlandcoders.digicalc.R;


/**
 * A [FragmentPagerAdapter] that returns a fragment corresponding to
 * one of the sections/tabs/pages.
 */
public class SectionsPagerAdapter extends FragmentPagerAdapter {

    @StringRes
    private static final int[] TAB_TITLES = new int[]{R.string.tab_text_1, R.string.tab_text_2, R.string.tab_text_3, R.string.convert};
    private final Context mContext;

    public SectionsPagerAdapter(Context context, FragmentManager fm) {
        super(fm);
        mContext = context;
    }

    @Override
    public Fragment getItem(int position) {
        Fragment frag = null;

        switch(position){
            case 0:
                frag = new AddBin();
                break;
            case 1:
                frag = new AddOct();
                break;
            case 2:
                frag = new AddHex();
                break;
            case 3:
                frag = new Convert();
        }
        return frag;
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        return mContext.getResources().getString(TAB_TITLES[position]);
    }

    @Override
    public int getCount() {
        // Show 3 total pages.
        return 4;
    }
}