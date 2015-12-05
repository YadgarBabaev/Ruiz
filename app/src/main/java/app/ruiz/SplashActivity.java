package app.ruiz;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

public class SplashActivity extends Activity {
    int amount = 0;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash);

        timer();
        request();
    }
    private void timer() {
        Thread logoTimer = new Thread()
        {
            public void run() {
                try {
                    int logoTimer = 0;
                    while(logoTimer < 2000)
                    {
                        sleep(100);
                        logoTimer = logoTimer +100;
                    }
                    startActivity(new Intent("MainActivity").putExtra("amount", amount));
                } catch (InterruptedException e) {e.printStackTrace();}
                finally {finish();}
            }
        }; logoTimer.start();
    }
    private void request(){
        new AsyncTask<String, String, String>() {
            @Override
            protected String doInBackground(String... args) {
                try{
                    String url = "http://baas.hol.es/counter.php";
                    HttpClient httpClient = new DefaultHttpClient();
                    HttpPost httpPost = new HttpPost(url);
                    HttpResponse httpResponse = httpClient.execute(httpPost);
                    HttpEntity httpEntity = httpResponse.getEntity();
                    JSONObject json = new JSONObject(EntityUtils.toString(httpEntity));
                    httpClient.getConnectionManager().shutdown();
                    amount = json.getInt("result");
                    Log.d("HttpResponse", String.valueOf(amount));
                } catch (IOException | JSONException e) {e.printStackTrace();}
                return null;
            }
        }.execute();
    }
//    public void makeHTTPCall() {
//        String url = "http://baas.hol.es/ruiz.php";
//        AsyncHttpClient client = new AsyncHttpClient();
//        client.post(url, new AsyncHttpResponseHandler() {
//
//            @Override
//            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
//                startActivity(new Intent(getApplicationContext(), MainActivity.class));
//                String str = null;
//                try {
//                    str = new String(responseBody, "UTF-8");
//                } catch (UnsupportedEncodingException e) {
//                    e.printStackTrace();
//                }
//                Log.d("AsyncHttpClient", str);
//            }
//            @Override
//            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
//                Log.d("AsyncHttpClient", "No connection");
//                // When Http response code is '404'
//                if (statusCode == 404) {
//                    Toast.makeText(getApplicationContext(), "Requested resource not found", Toast.LENGTH_LONG).show();
//                }
//                // When Http response code is '500'
//                else if (statusCode == 500) {
//                    Toast.makeText(getApplicationContext(), "Something went wrong at server end", Toast.LENGTH_LONG).show();
//                }
//                // When Http response code other than 404, 500
//                else {
//                    Toast.makeText(getApplicationContext(), "Error Occurred \n Most Common Error: \n1. Device not connected to Internet\n" +
//                            "2. Web App is not deployed in App server\n3. App server is not running\n HTTP Status code : " + statusCode, Toast.LENGTH_LONG).show();
//                }
//            }
//        });
//    }
}
