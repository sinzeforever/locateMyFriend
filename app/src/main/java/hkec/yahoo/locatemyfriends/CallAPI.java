package hkec.yahoo.locatemyfriends;

import android.app.Activity;
import android.os.AsyncTask;
import android.util.Log;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by sinze on 12/28/14.
 */
public class CallAPI extends AsyncTask<String, Integer, Long> {
    public String response;
    public boolean isProcessing = false;
    HttpURLConnection urlConnection;
    URL url;
    Activity main;

    public CallAPI(Activity main) {
        this.main = main;
    }

    protected Long doInBackground(String... urls) {
        isProcessing = true;
        response = "";
        Log.d("myLog", "Loading");
        try {
            url = new URL(urls[0]);
            Log.d("my url", url.toString());
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestMethod("GET");
            urlConnection.setDoOutput(true);
            urlConnection.setDoInput(true);
            urlConnection.connect();
            InputStream inStream = null;
            inStream = urlConnection.getInputStream();
            BufferedReader bReader = new BufferedReader(new InputStreamReader(inStream));
            String temp = "";
            while ((temp = bReader.readLine()) != null) {
                response += temp;
            }
            bReader.close();
            inStream.close();
            urlConnection.disconnect();;
        } catch (Exception e) {
            Log.d("myLog", "error call api");
        }
        return (long)0;
    }



    protected void onProgressUpdate(Integer... progress) {
        //setProgressPercent(progress[0]);
    }

    protected void onPostExecute(Long result) {
        isProcessing = false;
        Log.d("myLog", "Complete loading");
        //main.stopLoading();
        //main.postGetOfferList();
    }
}