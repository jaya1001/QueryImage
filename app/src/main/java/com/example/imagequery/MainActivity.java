package com.example.imagequery;

import android.content.Intent;
import android.database.Cursor;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Toast;

import com.jakewharton.retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;

import java.util.ArrayList;

import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private ApiImage data;
    private DataAdapter adapter;
    private String tag = "cream";
    private Spinner spin;
    int s = 2;
    boolean isLoading;
    boolean isLastPage;
    long currentPage = 1;
    int totalPage = 15;

    private DBHelper newHelper;
    ArrayList<ReqImage> imagesReq = new ArrayList<ReqImage>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Intent i = getIntent();
        tag = i.getStringExtra("query");
        spin = (Spinner) findViewById(R.id.spinner);
        adapter = new DataAdapter();

        spin.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                s = Integer.parseInt(String.valueOf(spin.getSelectedItem()));
                recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
                recyclerView.setLayoutManager(new GridLayoutManager(getApplicationContext(), s));
                recyclerView.setAdapter(adapter);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });

        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new GridLayoutManager(this, s));
        recyclerView.setAdapter(adapter);

        newHelper = new DBHelper(this);
        Cursor cursor = null;
        cursor = newHelper.getData(tag);

        if (cursor.getCount() != 0) {
            imagesReq.clear();

            Toast.makeText(this, "loading from database", Toast.LENGTH_SHORT).show();
            Log.e("cursor", String.valueOf(cursor.getCount()));
            if (cursor.moveToFirst()) {
                do {
                    String data = cursor.getString(cursor.getColumnIndex("photo_url"));
                    String querytag = cursor.getString(cursor.getColumnIndex("photo_tag"));

                    Log.e("tag", querytag);
                    ReqImage reqImage = new ReqImage(data, querytag);
                    imagesReq.add(reqImage);

                } while (cursor.moveToNext());
            }
            cursor.close();
            adapter = new DataAdapter(imagesReq);
            recyclerView.setAdapter(adapter);
        } else {
            Toast.makeText(this, "loading from json", Toast.LENGTH_SHORT).show();
            loadJSON();
        }

        recyclerView.addOnScrollListener(recyclerViewOnScrollListener);
    }

    private RecyclerView.OnScrollListener recyclerViewOnScrollListener = new RecyclerView.OnScrollListener() {
        @Override
        public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
            super.onScrollStateChanged(recyclerView, newState);
        }

        @Override
        public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
            int visibleItemCount = recyclerView.getLayoutManager().getChildCount();
            int totalItemCount = recyclerView.getLayoutManager().getItemCount();
            int firstVisibleItemPosition = ((LinearLayoutManager) recyclerView.getLayoutManager()).findFirstVisibleItemPosition();

            Log.e("itemcount", String.valueOf(visibleItemCount));
            Log.e("totalcount", String.valueOf(totalItemCount));
            Log.e("firstvisible", String.valueOf(firstVisibleItemPosition));

            if (!isLoading && !isLastPage) {
                if (dy > 0) {
                    Log.e("in if", "stilll");
                    if ((visibleItemCount + firstVisibleItemPosition) >= totalItemCount) {
                        Log.e("in if if", "loading");
                        loadMoreItems();
                    }
                }
            }
        }
    };

    protected void loadMoreItems() {
        LinearLayout lm = (LinearLayout) findViewById(R.id.linearLayout);
        isLoading = true;
        currentPage += 1; //Increment page index to load the next one
        Snackbar snackbar = Snackbar
                .make(lm, "Loading More Images", Snackbar.LENGTH_SHORT);
        snackbar.show();
        loadNextPage();
    }

    private void loadJSON() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://api.flickr.com/")
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        final RequestInterface request = retrofit.create(RequestInterface.class);

        String s = "https://api.flickr.com/" +
                "services/rest/?method=flickr.photos.search&api_key=38fb0bce422a6406ed3860219e9c9619&tags=" + tag + "&" +
                "format=json&nojsoncallback=1&per_page=20&media=photos&extras=url_o&page=" + currentPage;
        Call<JSONResponse> call = request.getJSON(s);

        call.enqueue(new Callback<JSONResponse>() {
            @Override
            public void onResponse(Call<JSONResponse> call, Response<JSONResponse> response) {
                if (response.isSuccessful()) {
                    if (response.body() != null) {
                        JSONResponse jsonResponse = response.body();
                        data = jsonResponse.getImage();

                        Log.e("LoadJson", String.valueOf(data.getPhotos().size()));

                        for (int j = 0; j < data.getPhotos().size(); j++) {
                            ReqImage req = new ReqImage(data.getPhotos().get(j).getUrl_o(), tag);
                            boolean x = newHelper.insertImage(req.getImageUrl(), req.getQueryTag());
                            Log.e("load first", String.valueOf(x));
                            imagesReq.add(req);
                        }

                        if (data != null) {
                            adapter.addAll(imagesReq);
                        }
                        recyclerView.addOnScrollListener(recyclerViewOnScrollListener);

                        if (currentPage <= totalPage) {
                            //adapter.addLoadingFooter();
                        } else isLastPage = true;

                    } else {
                        Log.e("error jsonresponse", response.errorBody().toString());
                    }
                } else {
                    Log.e("error ---- response", "in else ");
                }
            }

            @Override
            public void onFailure(Call<JSONResponse> call, Throwable t) {
                Log.e("Error", t.getMessage());
            }
        });
    }

    private void loadNextPage() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://api.flickr.com/")
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        final RequestInterface request = retrofit.create(RequestInterface.class);
        String s = "https://api.flickr.com/" +
                "services/rest/?method=flickr.photos.search&api_key=38fb0bce422a6406ed3860219e9c9619&tags=" + tag + "&" +
                "format=json&nojsoncallback=1&per_page=20&media=photos&extras=url_o&page=" + currentPage;
        Call<JSONResponse> call = request.getJSON(s);
        Toast.makeText(this, "in load next page", Toast.LENGTH_SHORT).show();
        call.enqueue(new Callback<JSONResponse>() {
            @Override
            public void onResponse(Call<JSONResponse> call, Response<JSONResponse> response) {
                adapter.removeLoadingFooter();
                isLoading = false;

                JSONResponse jsonResponse = response.body();
                data = jsonResponse.getImage();
                Log.e("LoadNextPage", String.valueOf(data.getPhotos().size()));

                for (int j = 0; j < data.getPhotos().size(); j++) {
                    ReqImage req = new ReqImage(data.getPhotos().get(j).getUrl_o(), tag);
                    boolean x = newHelper.insertImage(req.getImageUrl(), req.getQueryTag());
                    Log.e("load next", String.valueOf(x));
                    imagesReq.add(req);
                }

                if (data != null) {
                    adapter.addAll(imagesReq);
                }
                recyclerView.addOnScrollListener(recyclerViewOnScrollListener);
                if (currentPage != totalPage) {
                    //
                } else isLastPage = true;
            }

            @Override
            public void onFailure(Call<JSONResponse> call, Throwable t) {
                // handle failure
            }
        });
    }
}
