package com.example.talklicious.Adapter;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.example.talklicious.Fragments.ChatFragment;
import com.example.talklicious.Fragments.HomeFragment;
import com.example.talklicious.Fragments.UserFragment;

public class FragmentAdapter extends FragmentPagerAdapter {

    String [] names={"Home","user","Chat"};

    public FragmentAdapter(@NonNull FragmentManager fm) {
        super(fm);
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {

        switch (position)
        {
            case 0: return new HomeFragment();
            case 1: return new UserFragment();
            case 2: return new ChatFragment();
        }
        return null;
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        return names[position];
    }

    @Override
    public int getCount() {
        return names.length;
    }
}
