package it.qoute.zaingz.com.quoteit_android;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.animation.OvershootInterpolator;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.Toast;

import com.facebook.appevents.AppEventsLogger;
import com.mikepenz.materialdrawer.Drawer;
import com.mikepenz.materialdrawer.DrawerBuilder;
import com.mikepenz.materialdrawer.accountswitcher.AccountHeader;
import com.mikepenz.materialdrawer.accountswitcher.AccountHeaderBuilder;
import com.mikepenz.materialdrawer.model.DividerDrawerItem;
import com.mikepenz.materialdrawer.model.PrimaryDrawerItem;
import com.mikepenz.materialdrawer.model.ProfileDrawerItem;
import com.mikepenz.materialdrawer.model.SecondaryDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.IDrawerItem;
import com.mikepenz.materialdrawer.util.DrawerImageLoader;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.software.shell.fab.ActionButton;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import jp.wasabeef.recyclerview.animators.adapters.ScaleInAnimationAdapter;
import jp.wasabeef.recyclerview.animators.adapters.SlideInBottomAnimationAdapter;


public class MainActivity extends AppCompatActivity {

    Toolbar toolbar;

    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private List<ParseObject> previosuUserList= null;
    SwipeRefreshLayout mSwipeRefreshLayout;
    ArrayList<QuotePojo> dataset =new ArrayList<>();
    private boolean firstTime = true;
    private ArrayList<QuotePojo> temp;

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        toolbar = (Toolbar) findViewById(R.id.toolbar);

        if (toolbar != null) {
            setSupportActionBar(toolbar);
        }
        getSupportActionBar().setTitle("Quote It");


        DrawerImageLoader.init(new DrawerImageLoader.IDrawerImageLoader() {
            @Override
            public void set(ImageView imageView, Uri uri, Drawable placeholder) {

                Picasso.with(imageView.getContext()).load(uri).placeholder(placeholder).into(imageView);
                Log.d("draw", "hi");
            }

            @Override
            public void cancel(ImageView imageView) {
                Picasso.with(imageView.getContext()).cancelRequest(imageView);
            }

            @Override
            public Drawable placeholder(Context ctx) {
                return null;
            }
        });

        mSwipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.activity_main_swipe_refresh_layout);


        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                if (isConnectedViaWifi())
                    fetchQuotes();
                else
                    Toast.makeText(MainActivity.this, "No Internet Conection", Toast.LENGTH_SHORT).show();
            }});

        mSwipeRefreshLayout.setColorScheme(android.R.color.holo_blue_bright,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_red_light);
        mSwipeRefreshLayout.setProgressViewOffset(false,0,240);


        mRecyclerView = (RecyclerView) findViewById(R.id.my_recycler_view);

        // use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView
        mRecyclerView.setHasFixedSize(true);

        // use a linear layout manager
        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);









        // specify an adapter (see also next example)
        //get all the quotes from parse




        if (isConnectedViaWifi()) {
            fetchQuotes();
        }else{
            Toast.makeText(MainActivity.this, "No Internet Conection", Toast.LENGTH_SHORT).show();
        }





        mRecyclerView.setOnScrollListener(new MyScrollListener(this) {
            @Override
            public void onMoved(int distance) {
                toolbar.setTranslationY(-distance);
            }
        });


        String username= ParseUser.getCurrentUser().getString("user_id");


        AccountHeader headerResult = new AccountHeaderBuilder()
                .withActivity(this)
                .withHeaderBackground(R.drawable.header)
                .addProfiles(
                        new ProfileDrawerItem().withName(ParseUser.getCurrentUser().getUsername())
                                .withEmail(ParseUser.getCurrentUser().getEmail()).withIcon("https://graph.facebook.com/" + username + "/picture?type=large")
                )
               /* .withOnAccountHeaderListener(new AccountHeader.OnAccountHeaderListener() {
                    @Override
                    getResources().getDrawable(R.drawable.user)
                    public boolean onProfileChanged(View view, IProfile profile, boolean currentProfile) {
                        return false;
                    }
                })*/
                .build();


//Now create your drawer and pass the AccountHeader.Result
        Drawer result = new DrawerBuilder()
                .withActivity(this)
                .withToolbar(toolbar)
                .withAccountHeader(headerResult)
                .addDrawerItems(
                        new PrimaryDrawerItem().
                                withName("Home"),
                        new PrimaryDrawerItem().
                                withDescription("Best quotes selected for you")
                                .withName("Editor Choice"),
                        new PrimaryDrawerItem().
                                withBadge("9").
                                withName("Favourite"),

                        new PrimaryDrawerItem().

                                withBadge("7")
                                .withName("Saved"),

                        new DividerDrawerItem(),
                        new SecondaryDrawerItem().withIcon(android.R.drawable.ic_menu_preferences).withName("Setting"),
                        new SecondaryDrawerItem().withIcon(android.R.drawable.ic_menu_info_details).withName("About")
                )
                .withOnDrawerItemClickListener(new Drawer.OnDrawerItemClickListener() {
                    @Override
                    public boolean onItemClick(AdapterView<?> parent, View view, int position, long id, IDrawerItem drawerItem) {
                        // do something with the clicked item :D
                        return true;
                    }
                })

                .build();





        final ActionButton actionButton = (ActionButton) findViewById(R.id.action_button);
        actionButton.setType(ActionButton.Type.DEFAULT);
        actionButton.setButtonColor(getResources().getColor(R.color.accent));

        actionButton.setButtonColorPressed(getResources().getColor(R.color.primary_dark));
        actionButton.setImageResource(R.drawable.fab_plus_icon);
        actionButton.removeShadow();


        actionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

               /* Intent mainIntent = new Intent(MainActivity.this, CreateQuoteActivity.class);
                MainActivity.this.startActivity(mainIntent);*/

                Intent intent = new Intent(MainActivity.this, CreateQuoteActivity.class);
// Pass data object in the bundle and populate details activity.
              //  intent.putExtra(CreateQuoteActivity.EXTRA_CONTACT, contact);
                ActivityOptionsCompat options = ActivityOptionsCompat.
                        makeSceneTransitionAnimation(MainActivity.this, (View)actionButton, "float");
                startActivity(intent, options.toBundle());



            }
        });

    }

    private boolean isConnectedViaWifi() {
        ConnectivityManager connManager = (ConnectivityManager) getSystemService(getBaseContext().CONNECTIVITY_SERVICE);
        NetworkInfo mWifi = connManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
        NetworkInfo wWifi = connManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);

        return mWifi.isConnected()||wWifi.isConnected();
    }

    private void fetchQuotes(){



        mSwipeRefreshLayout.setRefreshing(true);

        ParseQuery<ParseObject> query = ParseQuery.getQuery("Quote");
        query.whereExists("quote");
        query.orderByDescending("createdAt");
        query.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> list, ParseException e) {



               if (firstTime){
                   Log.d("Slow", "prev list is null");
                   previosuUserList = list;
                   dataset.clear();
                   new GetUsernames().execute(list);
                   firstTime = false;


               }else {
                   if(compareList(previosuUserList,list)){
                       /* Toast.makeText(MainActivity.this, "Up to date", Toast.LENGTH_SHORT).show();*/
                       if (mSwipeRefreshLayout.isRefreshing())
                           mSwipeRefreshLayout.setRefreshing(false);
                       previosuUserList=list;
                       Log.d("Slow", "prev list is up to date "+ compareList(previosuUserList,list));
                   }else{
                       dataset.clear();
                       previosuUserList=list;
                       new GetUsernames().execute(list);
                       Log.d("Slow", "prev list is *not* equal to new");

                   }


               }



            }
        });




    }







    private boolean compareList(List<ParseObject> prev, List<ParseObject> first){

        boolean result = false;
        if (prev.size()== first.size()){

            for (int i= 0; i<=prev.size()-1;i++){
                if ( prev.get(i).getString("quote").equals(first.get(i).getString("quote"))
                        && prev.get(i).getString("author").equals(first.get(i).getString("author"))
                        && prev.get(i).getString("user").equals(first.get(i).getString("user"))
                        ){
                    result = true;
                }
                else{
                    result = false;
                    break;
                }
            }

        }
        return result;
    }

    @Override
    protected void onResume() {
        super.onResume();

        // Logs 'install' and 'app activate' App Events.
        AppEventsLogger.activateApp(this);
        if (isConnectedViaWifi())
            fetchQuotes();
        else
            Toast.makeText(MainActivity.this, "No Internet Conection", Toast.LENGTH_SHORT).show();

    }
    @Override
    protected void onPause() {
        super.onPause();

        // Logs 'app deactivate' App Event.
        AppEventsLogger.deactivateApp(this);
    }


    class GetUsernames extends AsyncTask<List <ParseObject>, Void, Void> {

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);

            mAdapter = new MyAdapter(dataset);

            ScaleInAnimationAdapter sm = new ScaleInAnimationAdapter(mAdapter);
            sm.setDuration(500);
            sm.setFirstOnly(false);


            mRecyclerView.setAdapter(new SlideInBottomAnimationAdapter(sm));



            if (mSwipeRefreshLayout.isRefreshing())
                mSwipeRefreshLayout.setRefreshing(false);

        }

        @Override
        protected Void doInBackground(List<ParseObject>... lists) {
            List<ParseObject> list = lists[0];
            previosuUserList = list;
            for (int i=0;i<list.size();i++){
                dataset.add(new QuotePojo(list.get(i).getString("quote"),
                        list.get(i).getString("author"),
                        list.get(i).getString("user_id"),
                        list.get(i).getString("user_name"),
                        list.get(i).getLong("time")
                ));//end of constructor


            }

            ParseObject.create(String.valueOf(dataset)).pinInBackground();

            return null;
        }





    }




    //eof main class
}