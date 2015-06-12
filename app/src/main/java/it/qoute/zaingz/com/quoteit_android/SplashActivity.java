package it.qoute.zaingz.com.quoteit_android;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.facebook.AccessToken;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.parse.LogInCallback;
import com.parse.ParseException;
import com.parse.ParseFacebookUtils;
import com.parse.ParseUser;

import org.json.JSONObject;

import java.util.Arrays;
import java.util.List;


public class SplashActivity extends AppCompatActivity {

    TextView logo;
    ImageView im;
    Button facebookBtn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {


        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);


        Typeface type = Typeface.createFromAsset(getAssets(),"Geomanist-Regular.otf");
        logo = (TextView)findViewById(R.id.logo);
                 logo.setTypeface(type);

        im = (ImageView) findViewById(R.id.logoImage);


        facebookBtn = (Button) findViewById(R.id.facebookBtn);






         /* New Handler to start the Menu-Activity
         * and close this Splash-Screen after some seconds.*/
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {

                if (ParseUser.getCurrentUser()!=null){



                facebookBtn.setEnabled(false);

                Intent mainIntent = new Intent(SplashActivity.this, MainActivity.class);
                SplashActivity.this.startActivity(mainIntent);
                SplashActivity.this.finish();
                }else{

                Animation animation = new TranslateAnimation(0,0,0, -250);
                animation.setDuration(1000);
                animation.setFillAfter(true);
                logo.startAnimation(animation);
                im.startAnimation(animation);
                YoYo.with(Techniques.RubberBand)
                        .delay(50)
                        .duration(500)
                        .playOn(findViewById(R.id.logoImage));
                facebookBtn.setVisibility(View.VISIBLE);


            }//eof else


            }
        }, 2000);


        facebookBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                List<String> permissions = Arrays.asList("public_profile", "email");
                ParseFacebookUtils.logInWithReadPermissionsInBackground(SplashActivity.this, permissions, new LogInCallback() {
                    @Override
                    public void done(ParseUser user, ParseException err) {
                        if (user == null) {
                            Log.d("MyApp", "Uh oh. The user cancelled the Facebook login.");

                        } else {


                            Log.d("MyApp", "User logged in through Facebook!");
                            GraphRequest request = GraphRequest.newMeRequest(
                                    AccessToken.getCurrentAccessToken(),
                                    new GraphRequest.GraphJSONObjectCallback() {
                                        @Override
                                        public void onCompleted(
                                                JSONObject object,
                                                GraphResponse response) {
                                            Log.d("MyApp", response.toString());
                                            try {
                                                ParseUser.getCurrentUser().setUsername(object.getString("name"));
                                                ParseUser.getCurrentUser().setEmail(object.getString("email"));
                                                ParseUser.getCurrentUser().put("link", object.getString("link"));
                                                ParseUser.getCurrentUser().put("user_id", object.getString("id"));
                                            } catch (Exception e) {
                                            } finally {
                                                ParseUser.getCurrentUser().saveEventually();
                                            }


                                        }
                                    });
                            Bundle parameters = new Bundle();
                            parameters.putString("fields", "id,name,link,email");
                            request.setParameters(parameters);
                            request.executeAsync();


                            Toast.makeText(SplashActivity.this, "Succesfully loged in", Toast.LENGTH_SHORT).show();
                            Intent mainIntent = new Intent(SplashActivity.this, MainActivity.class);
                            SplashActivity.this.startActivity(mainIntent);
                            SplashActivity.this.finish();

                        }
                    }
                });

            }
        });
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        ParseFacebookUtils.onActivityResult(requestCode, resultCode, data);
      /*  Intent mainIntent = new Intent(SplashActivity.this, MainActivity.class);
        SplashActivity.this.startActivity(mainIntent);
        SplashActivity.this.finish();*/
    }
}
