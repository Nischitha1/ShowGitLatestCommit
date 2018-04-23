package com.example.nisch.gitlatestcommit;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.example.nisch.gitlatestcommit.DataModel;

public class GitListActivity extends AppCompatActivity {

    JSONArray items;
    String total_count,incomplete_results;
    ArrayList<DataModel> dataModels;
    ListView listView;
    private static CustomAdapter adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_git_list);

    }
    @Override
    protected void onResume(){
        super.onResume();
        listView=(ListView)findViewById(R.id.git_list);

        dataModels= new ArrayList<>();


        new Atask().execute("Dagger&sort=updated&order=desc&per_page=25");
    }
    class Atask extends AsyncTask<String,Void,Void> {
        private ProgressDialog pDialog;
        boolean apiLimitExceeded = false;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(GitListActivity.this);
            pDialog.setMessage("Getting Data ...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(true);
            pDialog.show();
        }

        @Override
        protected Void doInBackground(String... params) {
            HttpURLConnection urlConnection;
            URL url;
            InputStream inputStream;
            String response="";



            try{
                url = new URL("https://api.github.com/search/repositories?q="+params[0]);
                Log.e("url valeu", url.toString());
                urlConnection = (HttpURLConnection) url.openConnection();



                //set request type
                urlConnection.setRequestMethod("GET");



                urlConnection.setDoInput(true);
                urlConnection.connect();
                //check for HTTP response
                int httpStatus = urlConnection.getResponseCode();
                Log.e("httpstatus", "The response is: " + httpStatus);

                //if HTTP response is 200 i.e. HTTP_OK read inputstream else read errorstream
                if (httpStatus != HttpURLConnection.HTTP_OK) {
                    inputStream = urlConnection.getErrorStream();
                    Map<String, List<String>> map = urlConnection.getHeaderFields();
                    System.out.println("Printing Response Header...\n");
                    for (Map.Entry<String, List<String>> entry : map.entrySet()) {
                        System.out.println(entry.getKey()
                                + " : " + entry.getValue());
                    }
                }
                else {
                    inputStream = urlConnection.getInputStream();
                }

                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
                String temp;
                while((temp = bufferedReader.readLine())!=null){
                    response+=temp;
                }
                Log.e("webapi json object",response);


                if(response.contains("API rate limit exceeded")){
//                    items= new JSONArray();
//                    total_count = "0";
                    apiLimitExceeded =true;
                }else {
                    //convert data string into JSONObject
                    JSONObject obj = (JSONObject) new JSONTokener(response).nextValue();
                    items = obj.getJSONArray("items");

                    total_count = obj.getString("total_count");
                    incomplete_results = obj.getString("incomplete_results");
                }

                urlConnection.disconnect();
            } catch (MalformedURLException | ProtocolException | JSONException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            if(!apiLimitExceeded){
                //apiLimitError.setVisibility(View.INVISIBLE);
                setResultListView();
            }else{
                dataModels.add(new DataModel("NoCommit", "NoCommit", "NoCommit"));
                adapter= new CustomAdapter(dataModels,getApplicationContext());

                listView.setAdapter(adapter);
                //count.setText("API rate Limit Error!!Try after some time!");
                Log.d("MainActivity","API rate Limit Error!!Try after some time!");
            }
            //result.setText("Success");
            Log.d("MainActivity","Success");
            pDialog.dismiss();
        }
    }

    private void setResultListView(){
        if(items.length()==0){
            return;
        }
        Log.d("some more data","item.length"+String.valueOf(items.length()));
        for (int i=0 ; i<items.length();i++){
            JSONObject jo;
            try {
                jo = items.getJSONObject(i);
//                Iterator<?> keys = jo.keys();
//
//                while( keys.hasNext() ) {
//                    String key = (String)keys.next();
//                    Log.d("MainActivity","JasonKey:"+key);
//                }

//                Log.d("some more data","updated at"+jo.getString("updated_at"));
                String name = jo.getString("full_name");
                String commit = jo.getString("commits_url");
                String commit_message = jo.getString("comments_url");
                dataModels.add(new DataModel(name, commit,commit_message));
                adapter= new CustomAdapter(dataModels,getApplicationContext());

                listView.setAdapter(adapter);
                //dataModels.add("name","35277898101","hello");

//                adapterList.add(String.valueOf(page*5+i+1)+". Repo Name: "+jo.getString("full_name")+"\n"
//                        +"   Size: "+jo.getString("size")+"KB"+"\n"
//                        +"   Forks: "+jo.getString("forks")+"\n"
//                        +"   Language: "+jo.getString("language")+"\n"
//                        +"   Watch Count: "+jo.getString("watchers_count")+"\n"
//                        +"   Updated At: "+jo.getString("updated_at")+"\n"
//                );

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }
}
