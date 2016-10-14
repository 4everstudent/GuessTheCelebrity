package a4everstudent.guessthecelebrity;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.concurrent.ExecutionException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MainActivity extends AppCompatActivity {


    public class DownloadTask extends AsyncTask<String, Void, String>{
        @Override
        protected String doInBackground(String...urls){

            String result = "";
            URL url;
            HttpURLConnection urlConnection= null;

            try {
                url = new URL(urls[0]);

                urlConnection = (HttpURLConnection) url.openConnection();

                InputStream in = urlConnection.getInputStream();

                InputStreamReader reader = new InputStreamReader(in);

                int data = reader.read();

                while(data != -1){
                    char current = (char) data;

                    result += current;

                    data = reader.read();
                }
                return result;

            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        DownloadTask task = new DownloadTask();
        String result;

        try {
            result = task.execute("http://www.posh24.com/celebrities").get();

            //split the page so we don't get the images from the sideBar
            String[] splitResult = result.split("<div class=\"sidebarContainer\">");


            Pattern p = Pattern.compile("alt=\"(.*?)\"");
            Matcher m = p.matcher(splitResult[0]);

            while(m.find()){
                System.out.println(m.group(1));
            }


        } catch (InterruptedException e) {

            e.printStackTrace();
        }
        catch (ExecutionException e) {

            e.printStackTrace();
        }

    }
}
