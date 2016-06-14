package com.andela.webservice;

import android.app.ListActivity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.andela.webservice.model.Flower;
import com.andela.webservice.parser.FlowerJsonParser;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends ListActivity {
    private TextView output;
    private ProgressBar progressBar;
    private List<MyTask> tasks;
    private List<Flower> flowerList;
    private static final String PHOTO_BASE_URL = "http://services.hanselandpetal.com/photos/";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        progressBar = (ProgressBar) findViewById(R.id.progressBar1);
        progressBar.setVisibility(View.INVISIBLE);
        tasks = new ArrayList<>();
        //output = (TextView) findViewById(R.id.textView);
        //output.setMovementMethod(new ScrollingMovementMethod());
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == R.id.action_do_task) {
            if(isOnline()) {
                requestData("http://services.hanselandpetal.com/secure/flowers.json");
            } else {
                Toast.makeText(this, "Network isn't available", Toast.LENGTH_LONG).show();
            }

        }
        return true;
    }

    private void requestData(String uri) {
        MyTask task = new MyTask();
        //This makes serial request
        task.execute(uri);
        //For parrallel processing
        //task.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, "param1", "param2", "param3");
    }

    private void updateDisplay() {
        /*if(flowerList != null) {
            for (Flower flower : flowerList) {
                output.append(flower.getName() + "\n");
            }

        }*/

        FlowerAdapter adapter = new FlowerAdapter(this, R.layout.item_flower, flowerList);
        setListAdapter(adapter);
    }

    protected boolean isOnline() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        if(netInfo != null && netInfo.isConnectedOrConnecting()) {
            return true;
        } else {
            return false;
        }
    }

    private class MyTask extends AsyncTask<String, String, List<Flower>> {
        //Async task now returns a List of Flower objects
        @Override
        protected void onPreExecute() {
            //updateDisplay("Starting task");
            if(tasks.size() == 0) {
                progressBar.setVisibility(View.VISIBLE);
            }
            tasks.add(this);
        }

        /*
            Doin the background parsing in the background thread
         */

        @Override
        protected List<Flower> doInBackground(String... params) {
            String content = HttpManager.getData(params[0],"feeduser","feedpassword");
            flowerList = FlowerJsonParser.parseFeed(content); //getting data from JSon feed
            //Getting image for each flower name in the list
            for(Flower flower : flowerList) {
                String imageUrl = PHOTO_BASE_URL + flower.getPhoto();
                try {
                    InputStream in = (InputStream) new URL(imageUrl).getContent();
                    //This retrieves the entire content of the location in one request
                    //It comes back as a blub of data n we can access it through the input stream
                    Bitmap bitmap = BitmapFactory.decodeStream(in);
                    flower.setBitmap(bitmap);
                    in.close();

                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            return flowerList;
        }

        @Override
        protected void onPostExecute(List<Flower> result) {
            tasks.remove(this);
            if(tasks.size() == 0) {
                progressBar.setVisibility(View.INVISIBLE);
            }

            //flowerList = FlowerXmlParser.parseFeed(result);
            if(result == null) {
                Toast.makeText(MainActivity.this, "Can't connect to webservice", Toast.LENGTH_LONG).show();
                return;
            }
            flowerList = result;
            updateDisplay();

        }

        @Override
        protected void onProgressUpdate(String... values) {
            //updateDisplay(values[0]);
        }
    }


}
