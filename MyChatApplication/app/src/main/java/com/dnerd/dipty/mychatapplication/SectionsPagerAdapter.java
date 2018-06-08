package com.dnerd.dipty.mychatapplication;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.dnerd.dipty.mychatapplication.FriendsFragment;

import java.util.ArrayList;
import java.util.List;

public class SectionsPagerAdapter extends FragmentStatePagerAdapter {
    public final List<Fragment> listOfFragment = new ArrayList<>();
    public final List<String> titleFragment = new ArrayList<>();


    public SectionsPagerAdapter(FragmentManager fm) {
        super(fm);

    }

    @Override
    public Fragment getItem(int position) {
        switch(position) {
            case 0:
                ChatsFragment chatsFragment = new ChatsFragment();
                return  chatsFragment;
            case 1:

                FriendsFragment friendsFragment = new FriendsFragment();
                return friendsFragment;

            case 2:

                RequestFragment requestsFragment = new RequestFragment();
                return requestsFragment;

            default:
                return  null;
        }
    }

    @Override
    public int getCount() {
        return 3;
    }

    public CharSequence getPageTitle(int position){

        switch (position) {
            case 0:
                return "INBOX";

            case 1:

                return "FRIENDS";

            case 2:

                return "REQUESTS";

            default:
                return null;
        }

    }
}
