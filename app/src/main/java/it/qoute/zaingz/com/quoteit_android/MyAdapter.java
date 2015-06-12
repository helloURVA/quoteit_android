package it.qoute.zaingz.com.quoteit_android;

import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.support.v7.widget.RecyclerView;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.github.curioustechizen.ago.RelativeTimeTextView;
import com.pkmmte.view.CircularImageView;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;


public class MyAdapter extends RecyclerView.Adapter<MyAdapter.ViewHolder> {
    private ArrayList<QuotePojo> mDataset;

    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder
    static Context con;

    // Provide a suitable constructor (depends on the kind of dataset)
    public MyAdapter(ArrayList<QuotePojo> myDataset) {
        mDataset = myDataset;
    }

    // Create new views (invoked by the layout manager)
    @Override
    public MyAdapter.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                   int viewType) {
        // create a new view
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());


        con = parent.getContext();
        View v = inflater.inflate(R.layout.quote_card, parent, false);



        ViewHolder vh = new ViewHolder( v );

        return vh;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        // - get element from your dataset at this position
        // - replace the contents of the view with that element

       /* int mLastPosition=0;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR1) {
            float initialTranslation = (mLastPosition <= position ? 150f
                    : -150f);

            holder.convertView.setTranslationY(initialTranslation);
            holder.convertView.animate()
                    .setInterpolator(new DecelerateInterpolator(1.0f))
                    .translationY(0f).setDuration(900l).setListener(null);
        }
        // Keep track of the last position we loaded
        mLastPosition = position;*/


        holder.author.setText("- "+mDataset.get(position).getAuthor());
        holder.quote.setText(mDataset.get(position).getQuote());
        holder.mTextView.setText(mDataset.get(position).getUserName());
        holder.timeStamp.setReferenceTime(mDataset.get(position).getTime());

        final String username= mDataset.get(position).getUserID();
        if (!username.equals("")) {
            try{
            Picasso.with(con).load("https://graph.facebook.com/" + username + "/picture?type=large&width=100&height=100")
                    .resize(90, 90)
                    .centerCrop()
                    .into(holder.userImage);}
            catch (Exception e){}
        }
        final int x = position;
        holder.userImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent mainIntent = new Intent(con, UserInfoActivity.class);
                mainIntent.putExtra("usernameSocial", username);
                mainIntent.putExtra("username",mDataset.get(x).getUserName() );

                con.startActivity(mainIntent);




            }
        });

        holder.mTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {





                Intent intent = new Intent(con, UserInfoActivity.class);
                intent.putExtra("usernameSocial", username);
                intent.putExtra("username",mDataset.get(x).getUserName() );
                Pair<View, String> p1 = Pair.create((View)holder.mTextView, "userName");
                Pair<View, String> p2 = Pair.create((View)holder.userImage, "userImage");

               // ActivityOptionsCompat options = ActivityOptionsCompat.
                      //  makeSceneTransitionAnimation(, p1, p2);
                con.startActivity(intent);









            }
        });


    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return mDataset.size();
    }
    public static class ViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        public TextView mTextView,quote,author;
        public CircularImageView userImage;
        RelativeTimeTextView timeStamp;

        View convertView;
        public View convertViewview;

        public ViewHolder(View v) {
            super(v);
            mTextView = (TextView) v.findViewById(R.id.nameText);
            userImage = (CircularImageView)v.findViewById(R.id.userImage);
            timeStamp = (RelativeTimeTextView)v.findViewById(R.id.timestamp);
            author = (TextView)v.findViewById(R.id.author);
             //Or just use Butterknife!
            quote = (TextView) v.findViewById(R.id.quoteText);
            convertView = v;



            Typeface typeLight = Typeface.createFromAsset(con.getAssets(),"Geomanist-Regular.otf");
            mTextView.setTypeface(typeLight);
            author.setTypeface(typeLight);
            author.setTypeface(typeLight);
            quote.setTypeface(typeLight);




        }
    }
}