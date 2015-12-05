package app.ruiz;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class MainActivity extends Activity {

    //WRGFpiTb77
    int max = 10, member1, member2, index = 1, amount;
    List<Integer> ids = new ArrayList<>();
    JSONParser jParser = new JSONParser();
    ProgressDialog pDialog;
    ImageView im1, im2;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        pDialog = new ProgressDialog(MainActivity.this);

        amount = getIntent().getIntExtra("amount", 0);
        if(amount < 0)
            Toast.makeText(this, "RESTART THIS APPLICATION", Toast.LENGTH_LONG).show();
        if(amount < max)
            max = amount-1;

        im1 = (ImageView) findViewById(R.id.img1);
        im1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                like(member1);
            }
        });
        im2 = (ImageView) findViewById(R.id.img2);
        im2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                like(member2);
            }
        });

        f();
    }
    private void like(final int id){
        index++;
        Log.d("Selected", String.valueOf(id));
        new AsyncTask<String, String, String>(){
            int success;
            String url = "http://baas.hol.es/increase.php";
            @Override
            protected String doInBackground(String... args) {
                List<NameValuePair> params = new ArrayList<>();
                params.add(new BasicNameValuePair("id", String.valueOf(id)));
                JSONObject json = jParser.makeHttpRequest(url, "POST", params);
                try {
                    Log.d("JSON", json.toString());
                    success = json.getInt("success");
                } catch (JSONException e) {e.printStackTrace();}
                return null;
            }
            @Override
            protected void onPostExecute(String file_url) {
                if (success == 1) {
                    Log.d("Increased for", String.valueOf(id));
                    ids.add(id); f();
                } else Toast.makeText(getApplicationContext(), "An error occurred", Toast.LENGTH_LONG).show();
                if(index == max) {
                    startActivity(new Intent("RatingTable"));
                    finish();
                }
            }
        }.execute();
    }
    private void f(){
        member1 = firstIdGenerator();
        member2 = secondIdGenerator(member1);

        Log.d("ROUND#" + index, member1 + " vs " + member2);

        im1.setVisibility(View.INVISIBLE);
        im2.setVisibility(View.INVISIBLE);

        getImage(member1, im1);
        getImage(member2, im2);

    }
    private void getImage(int id, ImageView imageView){
        String url = "http://baas.hol.es/images/" + id + ".jpg";
        ImageManager man = new ImageManager();
        man.fetchImage(this, 3600, url, imageView);
        imageView.setVisibility(View.VISIBLE);
    }
    private int firstIdGenerator(){
        int id = new Random().nextInt(amount) + 1;
        if(!ids.contains(id) && id != 0)
            return id;
        return firstIdGenerator();
    }
    private int secondIdGenerator(int id1){
        int id2 = new Random().nextInt(amount) + 1;
        if(!ids.contains(id2) && id2 != 0 && id2 != id1)
            return id2;
        return secondIdGenerator(id1);
    }
}