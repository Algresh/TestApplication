package ru.tulupov.alex.testapplication;

import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private static final String URL_TEXT = "searchLineText";
    private static final String HTML_TEXT = "HTMLText";

    TextView tvHTMLText;
    NetworkConnection networkConnection;

    private EditText edtUrl;
    private ConnectTask connectTask;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        String strUrl = null;
        String htmlText = null;

        if (savedInstanceState != null) {
            strUrl = savedInstanceState.getString(URL_TEXT);
            htmlText = savedInstanceState.getString(HTML_TEXT);
        }

        connectTask = (ConnectTask) getLastCustomNonConfigurationInstance();
        if (connectTask == null) {
            connectTask = new ConnectTask();
        }
        connectTask.setActivity(this);
        String cache = connectTask.getCache();

        tvHTMLText = (TextView) findViewById(R.id.tv_HTML_text);
        if (cache != null) {
            tvHTMLText.setText(cache);
        } else if (htmlText != null) {
            tvHTMLText.setText(htmlText);
        }
        edtUrl = (EditText) findViewById(R.id.edt_URL);
        if (strUrl != null) {
            edtUrl.setText(strUrl);
        }

        Button btnShowText = (Button) findViewById(R.id.btn_show_text);
        btnShowText.setOnClickListener(this);
        networkConnection = new NetworkConnectManager();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        String url = edtUrl.getText().toString();
        String html = tvHTMLText.getText().toString();
        outState.putString(URL_TEXT, url);
        outState.putString(HTML_TEXT, html);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        connectTask.onDestroyActivity();
    }

    @Override
    public void onClick(View view) {
        String url = edtUrl.getText().toString();

        if (checkConnection()) {
            if (networkConnection.checkUrl(url)) {
                connectTask = new ConnectTask();
                connectTask.setActivity(this);
                connectTask.execute(url);
            } else {
                Toast.makeText(this, R.string.wrongUrl, Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(this, R.string.noInternetAccess, Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public Object onRetainCustomNonConfigurationInstance() { // сохранение ссылки на AsyncTask
        return connectTask;
    }

    public static class ConnectTask extends AsyncTask<String, Void, String> {

        private MainActivity activity;
        private String cache = null;

        void setActivity(MainActivity activity) {
            this.activity = activity;
        }

        void onDestroyActivity() {
            activity = null;
        }

        String getCache() {
            return cache;
        }

        @Override
        protected String doInBackground(String... strings) {
            String url = strings[0];
            return activity.networkConnection.getHtmlText(url);
        }

        @Override
        protected void onPostExecute(String res) {
            super.onPostExecute(res);
            if (activity != null) {
                if (res != null) {
                    activity.tvHTMLText.setText(res);
                } else {
                    Toast.makeText(activity, R.string.connectError, Toast.LENGTH_SHORT).show();
                }

            }
            cache = res;
        }
    }

    protected boolean checkConnection() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        return activeNetwork != null;
    }

}
