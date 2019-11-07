package com.example.workouttracker.ui.main;

import android.content.Context;

import androidx.annotation.Nullable;
import androidx.annotation.StringRes;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.example.workouttracker.AllWorkoutsFragment;
import com.example.workouttracker.R;
import com.example.workouttracker.SearchWorkoutsFragment;

import java.lang.ref.WeakReference;
import java.util.Hashtable;

public class SectionsPagerAdapter extends FragmentPagerAdapter {

    @StringRes
    private static final int[] TAB_TITLES = new int[]{R.string.tab_text_1, R.string.tab_text_2};
    private final Context mContext;

    public Hashtable<Integer, WeakReference<Fragment>> fragmentReferences = new Hashtable<>();

    public SectionsPagerAdapter(Context context, FragmentManager fm) {
        super(fm);
        mContext = context;
    }

    @Override
    public Fragment getItem(int position) {

        Fragment fragment;

        switch (position) {
            case 0:
                fragment = new AllWorkoutsFragment();
                break;
            case 1:
                fragment = new SearchWorkoutsFragment();
                break;
            default:
                throw new RuntimeException("Tabbed activity pager out of bounds");
        }

        fragmentReferences.put(position, new WeakReference<>(fragment));

        return fragment;
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        return mContext.getResources().getString(TAB_TITLES[position]);
    }

    @Override
    public int getCount() {
        // Show 2 total pages.
        return 2;
    }
}