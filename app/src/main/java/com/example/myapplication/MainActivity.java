package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.androidnetworking.AndroidNetworking;

import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONArrayRequestListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private String LINK_HTTP = "https://jsonplaceholder.typicode.com/photos";
    //private String LINK_HTTP = "https://jsonplaceholder.typicode.com/";

    private RecyclerView rv;
    private Button btnGet;
    private List<Item> items = new ArrayList<>();
    private CustomRecyclerView adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        AndroidNetworking.initialize(getApplicationContext());
        init();
    }

    private void init() {
        rv = findViewById(R.id.rv);
        btnGet = findViewById(R.id.btnGet);
        btnGet.setOnClickListener(this);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext(), RecyclerView.VERTICAL, false);
        adapter = new CustomRecyclerView(getApplicationContext(), items);
        rv.setLayoutManager(layoutManager);
        rv.setAdapter(adapter);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnGet:
                new BackgroundTaskHttpGet().execute(LINK_HTTP);
        }
    }

    private class BackgroundTaskHttpGet extends AsyncTask<String, String, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... strings) {
            //volleyJsonArrayRequest(strings[0]);
            //retrofitJsonArrayRequest(strings[0]);
            fastAndroidNetworkingJsonArrayRequest(strings[0]);
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            Log.e("onpost", "done");
        }
    }

    //Volley
    private void volleyJsonArrayRequest(String link) {
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, link, null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                try {
                    readJson(response);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });

        jsonArrayRequest.setShouldCache(false);
        VolleySingleton.getInstance(this).getmRequestQueue().add(jsonArrayRequest);
    }

    //Retrofit
    private void retrofitJsonArrayRequest(String link) {
        ItemService itemService = ItemRetrofit.getInstance(link).create(ItemService.class);
        itemService.getItem().enqueue(new Callback<List<Item>>() {
            @Override
            public void onResponse(Call<List<Item>> call, retrofit2.Response<List<Item>> response) {
                List<Item> responseItems = response.body();
                Log.e("size", response.body().size() + "");
                items.addAll(responseItems);
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onFailure(Call<List<Item>> call, Throwable t) {

            }
        });
    }

    //Fast Android Networking
    private void fastAndroidNetworkingJsonArrayRequest(String link) {
        AndroidNetworking.get(link)
                .setPriority(Priority.HIGH)
                .build()
                .getAsJSONArray(new JSONArrayRequestListener() {
                    @Override
                    public void onResponse(JSONArray response) {
                        try {
                            readJson(response);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onError(ANError anError) {

                    }
                });
    }

    private void readJson(JSONArray jsonArray) throws JSONException {
        for (int i = 0; i < jsonArray.length(); i++) {
            JSONObject object = jsonArray.getJSONObject(i);
            Item item = new Item(object.getInt("albumId"), object.getInt("id"), object.getString("title"), object.getString("url"), object.getString("thumbnailUrl"));
            items.add(item);
        }
        Log.e("adapter size", adapter.getItemCount() + "");
        adapter.notifyDataSetChanged();
    }

}