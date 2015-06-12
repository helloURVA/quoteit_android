package it.qoute.zaingz.com.quoteit_android;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.parse.ParseObject;
import com.parse.ParseUser;
import com.software.shell.fab.ActionButton;

import java.util.Date;


public class CreateQuoteActivity extends AppCompatActivity {

    Toolbar toolbar;
    EditText quote,author;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_quote);

        toolbar = (Toolbar) findViewById(R.id.toolbar);

        if (toolbar != null) {
            setSupportActionBar(toolbar);
        }
        getSupportActionBar().setTitle("");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);


        quote = (EditText) findViewById(R.id.quoteEt);
        author = (EditText) findViewById(R.id.authorEt);

        ActionButton actionButton = (ActionButton) findViewById(R.id.action_button);
        actionButton.setType(ActionButton.Type.DEFAULT);
        actionButton.setButtonColor(getResources().getColor(R.color.accent));

        actionButton.setButtonColorPressed(getResources().getColor(R.color.primary_dark));
        actionButton.setImageResource(R.drawable.send);
        actionButton.removeShadow();

        actionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                if (quote.getText().toString().length()<1){
                    YoYo.with(Techniques.Pulse)
                            .delay(10)
                            .duration(500)
                            .playOn(quote);

                }else if(author.getText().toString().length()<1 ){
                    YoYo.with(Techniques.Pulse)
                            .delay(10)
                            .duration(500)
                            .playOn(author);

                }else {

                    ParseObject gameScore = new ParseObject("Quote");
                    gameScore.put("time", new Date().getTime());
                    gameScore.put("quote", quote.getText().toString());
                    gameScore.put("author", author.getText().toString());
                    gameScore.put("user", ParseUser.getCurrentUser().getObjectId());
                    gameScore.put("user_name", ParseUser.getCurrentUser().getUsername());
                    gameScore.put("user_id", ParseUser.getCurrentUser().get("user_id"));
                    gameScore.saveInBackground();
                    Toast.makeText(CreateQuoteActivity.this, "Succesfully Posted", Toast.LENGTH_SHORT).show();
                    finish();
                }
            }
        });



    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

}
