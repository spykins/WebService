package com.andela.webservice;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {
    private TextView output;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        output = (TextView) findViewById(R.id.textView);
        output.setMovementMethod(new ScrollingMovementMethod());
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        MyTask task = new MyTask();
        task.execute("param1", "param2", "param3");
        return true;
    }

    private void updateDisplay(String s) {
        output.append(s + "\n");
    }

    private class MyTask extends AsyncTask<String, String, String> {
        @Override
        protected void onPreExecute() {
            updateDisplay("Starting task");
        }

        @Override
        protected String doInBackground(String... params) {
            for(int i = 0; i< params.length; i++) {
                publishProgress("working with " + params[i]);

                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

            return "Task Complete";
        }

        @Override
        protected void onPostExecute(String result) {
            updateDisplay(result);
        }

        @Override
        protected void onProgressUpdate(String... values) {
            updateDisplay(values[0]);
        }
    }


}
