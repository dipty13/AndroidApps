package com.dnerd.dipty.retrofitexample.realm_configuration;

import android.app.Application;
import android.content.Context;

import com.dnerd.dipty.retrofitexample.MainActivity;

import io.realm.Realm;
import io.realm.RealmConfiguration;

public class ConfigRealm  {
    private  static  Realm mRealm;
    private static RealmConfiguration mRealmConfig;

    public static Realm getRealmCongfiguration()
    {

        mRealmConfig = new RealmConfiguration.Builder()
                .name("gitrepo.realm")
                .schemaVersion(1)
                .deleteRealmIfMigrationNeeded()
                .build();
        mRealm = Realm.getInstance(mRealmConfig);
        return mRealm;
    }
}
