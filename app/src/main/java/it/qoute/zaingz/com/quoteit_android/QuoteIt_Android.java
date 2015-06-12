package it.qoute.zaingz.com.quoteit_android;

import android.app.Application;

import com.parse.Parse;
import com.parse.ParseFacebookUtils;


/**
 * Created by Zain on 5/30/2015.
 */
public class QuoteIt_Android extends Application {


    @Override
    public void onCreate() {
        super.onCreate();

        Parse.enableLocalDatastore(this);
        Parse.initialize(this, getResources().getString(R.string.parse_app_id),  getResources().getString(R.string.parse_app_id2));
        ParseFacebookUtils.initialize(this);





    }
}
