package app.ruiz;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.widget.ListView;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;

import org.apache.http.NameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

public class TopTen extends Activity{

    List<Integer> id = new ArrayList<>();
    List<Integer> likes = new ArrayList<>();
    List<String> name = new ArrayList<>();
    LazyImageLoadAdapter adapter;
    Activity activity;
    ListView list;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.listview);
        activity = this;

        new AsyncTask<String, String, String>(){
            ProgressDialog pDialog = new ProgressDialog(activity);

            @Override
            protected void onPreExecute(){
                super.onPreExecute();
                pDialog.setMessage("Request processing\nPlease wait...");
                pDialog.setIndeterminate(false);
                pDialog.setCancelable(true);
                pDialog.show();
            }
            @Override
            protected String doInBackground(String... args) {
                String url = "http://baas.hol.es/tops.php";
                List<NameValuePair> params = new ArrayList<>();
                JSONParser jParser = new JSONParser();
                JSONObject json = jParser.makeHttpRequest(url, "POST", params);
                try {
                    JSONArray jArray = json.getJSONArray("members");
                    for(int i = 0; i < jArray.length(); i++)
                    {
                        JSONObject j = jArray.getJSONObject(i);
                        Log.d("JSONObject", j.toString());
                        id.add(j.getInt("id"));
                        name.add(j.getString("name"));
                        likes.add(j.getInt("likes"));
                    }
                } catch (JSONException e) {e.printStackTrace();}
                return null;
            }
            @Override
            protected void onPostExecute(String file_url) {
//                list = (ListView)findViewById(R.id.list);
//                adapter = new LazyImageLoadAdapter(activity, id, name, likes);
//                list.setAdapter(adapter);
                CustomList adapter = new CustomList(activity, name, id, likes);
                list = (ListView) findViewById(R.id.list);
                list.setAdapter(adapter);
                pDialog.dismiss();
            }
        }.execute();
    }

    public void makeHTTPCall() {
        String url = "http://baas.hol.es/tops.php";
        AsyncHttpClient client = new AsyncHttpClient();
        client.post(url, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, cz.msebera.android.httpclient.Header[] headers, byte[] responseBody) {
                try {
                    String s = new String(responseBody, "UTF-8");
                    JSONObject json = new JSONObject(s);
                    JSONArray jArray = json.getJSONArray("members");
                    for (int i = 0; i < jArray.length(); i++) {
                        JSONObject j = jArray.getJSONObject(i);
                        Log.d("JSONObject", j.toString());
                        id.add(j.getInt("id"));
                        name.add(j.getString("name"));
                        likes.add(j.getInt("likes"));
                    }
                    Log.d("SUCCESS", s);
                } catch (UnsupportedEncodingException | JSONException e) {
                    e.printStackTrace();
                }

                CustomList adapter = new CustomList(TopTen.this, name, id, likes);
                list = (ListView) findViewById(R.id.list);
                list.setAdapter(adapter);
            }

            @Override
            public void onFailure(int statusCode, cz.msebera.android.httpclient.Header[] headers, byte[] responseBody, Throwable error) {
                Log.d("ERROR", "No connection");
                // When Http response code is '404'
                if (statusCode == 404) {
                    Toast.makeText(getApplicationContext(), "Requested resource not found", Toast.LENGTH_LONG).show();
                }
                // When Http response code is '500'
                else if (statusCode == 500) {
                    Toast.makeText(getApplicationContext(), "Something went wrong at server end", Toast.LENGTH_LONG).show();
                }
                // When Http response code other than 404, 500
                else {
                    Toast.makeText(getApplicationContext(), "Error Occurred \n Most Common Error: \n1. Device not connected to Internet\n" +
                            "2. Web App is not deployed in App server\n3. App server is not running\n HTTP Status code : " + statusCode, Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    @Override
    public void onDestroy() {
        // Remove adapter refference from list
        list.setAdapter(null);
        super.onDestroy();
    }
}