package com.andela.webservice;

import android.app.ListActivity;
import android.content.Context;
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

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends ListActivity {
    private TextView output;
    private ProgressBar progressBar;
    private List<MyTask> tasks;
    private List<Flower> flowerList;

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

    private class MyTask extends AsyncTask<String, String, String> {
        @Override
        protected void onPreExecute() {
            //updateDisplay("Starting task");
            if(tasks.size() == 0) {
                progressBar.setVisibility(View.VISIBLE);
            }
            tasks.add(this);
        }

        @Override
        protected String doInBackground(String... params) {
            String content = HttpManager.getData(params[0],"feeduser","feedpassword");
            return content;
        }

        @Override
        protected void onPostExecute(String result) {
            tasks.remove(this);
            if(tasks.size() == 0) {
                progressBar.setVisibility(View.INVISIBLE);
            }

            //flowerList = FlowerXmlParser.parseFeed(result);
            if(result == null) {
                Toast.makeText(MainActivity.this, "Can't connect to webservice", Toast.LENGTH_LONG).show();
                return;
            }
            flowerList = FlowerJsonParser.parseFeed(result);
            updateDisplay();

        }

        @Override
        protected void onProgressUpdate(String... values) {
            //updateDisplay(values[0]);
        }
    }


}
