package com.dnerd.dipty.mychatapplication;

import android.content.Intent;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;

public class Feed extends AppCompatActivity {
    private SectionsPagerAdapter mSectionsPagerAdapter;
    private ViewPager mViewPager;
    private DatabaseReference mUserRef;

    TabLayout tablayout;

    private int[] tabicons =
            {
                    R.drawable.inbox_icon,
                    R.drawable.friends_icon,
                    R.drawable.add_friend_icon

            };

    private FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feed);

        auth = FirebaseAuth.getInstance();
        Toolbar toolbar =  findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        ActionBar ab = getSupportActionBar();
        ab.setDisplayHomeAsUpEnabled(false);
        ab.setHomeButtonEnabled(false);

        mViewPager =  findViewById(R.id.container);
        setUpPager(mViewPager);

        tablayout =  findViewById(R.id.tabLayout);
        tablayout.setupWithViewPager(mViewPager);

        setUpTabIcons();

    }

    private void setUpTabIcons() {
        tablayout.getTabAt(0).setIcon(tabicons[0]);
        tablayout.getTabAt(1).setIcon(tabicons[1]);
        tablayout.getTabAt(2).setIcon(tabicons[2]);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int ItemId = item.getItemId();

        if (ItemId == R.id.action_logout) {

            auth.signOut();
            Intent intent = new Intent(this,MainActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
        }
        if(ItemId == R.id.action_edit_friends)
        {
            Intent intent = new Intent(this,UsersActivity.class);
            startActivity(intent);
        }
        if(ItemId == R.id.action_account_setting)
        {
            Intent intent = new Intent(this,SettingsActivity.class);
            startActivity(intent);
        }

        return super.onOptionsItemSelected(item);
    }

    private void setUpPager(ViewPager viewPager) {
        SectionsPagerAdapter adapter = new SectionsPagerAdapter(getSupportFragmentManager());
        mViewPager.setAdapter(adapter);
    }
}
