package com.example.examplecodingkotlin;

import android.app.Application;
import com.facebook.drawee.backends.pipeline.Fresco;
import io.realm.Realm;
import io.realm.RealmConfiguration;

/**
 * Created by fbarbieri on 2019-08-28.
 */
public class MainApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        Fresco.initialize(this);

        Realm.init(this);
        final RealmConfiguration config = new RealmConfiguration
            .Builder()
            .deleteRealmIfMigrationNeeded()
            .build();
        Realm.setDefaultConfiguration(config);
    }
}
