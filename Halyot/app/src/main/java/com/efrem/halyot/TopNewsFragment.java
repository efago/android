package com.efrem.halyot;


import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.paging.PagedList;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Handler;
import android.util.JsonReader;
import android.util.JsonToken;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.firebase.ui.database.paging.DatabasePagingOptions;
import com.firebase.ui.database.paging.FirebaseRecyclerPagingAdapter;
import com.firebase.ui.database.paging.LoadingState;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ServerValue;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Array;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class TopNewsFragment extends Fragment {

    DatabaseReference database;
    RecyclerView recyclerView;
    Handler handler;
    String tag= "inside top fragment";
    FirebaseRecyclerPagingAdapter<News, NewsViewHolder> adapter;


    public TopNewsFragment() {}


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view= inflater.inflate(R.layout.fragment_top_news, container, false);
        recyclerView= view.findViewById(R.id.recyclerview_topNews);
        recyclerView.setLayoutManager( new LinearLayoutManager( getContext()));
        database= FirebaseDatabase.getInstance().getReference();
        handler= new Handler();
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        checkUpdates();
    }
    public void checkUpdates(){
        database.child("news_category/top_news/timestamp").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if ( (long) dataSnapshot.getValue() < System.currentTimeMillis() - 1000*60*60*4)
                    notifyServer();
                else
                    populatePage();
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                populatePage();
            }
        });
    }
    @SuppressLint("StaticFieldLeak")
    public void notifyServer() {
        new AsyncTask<Void, Void, Boolean>() {

            protected ArrayList<String> readSnippet( JsonReader reader) throws IOException{
                reader.beginObject();
                ArrayList<String> vals= new ArrayList<>();
                while (reader.hasNext()){
                    switch( reader.nextName()) {
                        case "source":
                            reader.beginObject();
                            while (reader.hasNext()){
                                if (reader.nextName().equals("name"))
                                    if(reader.peek() != JsonToken.NULL) {
                                        String a= reader.nextString();
                                        vals.add( a);
                                    }
                                    else{
                                            vals.add("");
                                            //reader.skipValue();
                                        }
                                else
                                    reader.skipValue();
                            }
                            reader.endObject();
                            break;
                        case "author":
                            if(reader.peek() != JsonToken.NULL)
                                vals.add( reader.nextString());
                            else{
                                vals.add("");
                                reader.skipValue();
                            }
                            break;
                        case "title":
                            if(reader.peek() != JsonToken.NULL)
                                vals.add( reader.nextString());
                            else{
                                vals.add("");
                                reader.skipValue();
                            }
                            break;
                        case "url":
                            if(reader.peek() != JsonToken.NULL)
                                vals.add( reader.nextString());
                            else{
                                vals.add("");
                                reader.skipValue();
                            }
                            break;
                        case "urlToImage":
                            if(reader.peek() != JsonToken.NULL)
                                vals.add( reader.nextString());
                            else{
                                vals.add("");
                                reader.skipValue();
                            }
                            break;
                        case "publishedAt":
                            if(reader.peek() != JsonToken.NULL)
                                vals.add( reader.nextString());
                            else{
                                vals.add("");
                                reader.skipValue();
                            }
                            break;
                        default:
                            reader.skipValue();
                    }
                }
                reader.endObject();
                return vals;
            }
            @Override
            protected Boolean doInBackground(Void... voids) {
                try {
                    URL url = new URL("https://newsapi.org/v2/top-headlines?country=us&apiKey=9ff3cadbd9af41cdae92e78490d1086f");
                    URLConnection connection = url.openConnection();
                    HttpURLConnection httpConnection = (HttpURLConnection) connection;
                    int responseCode = httpConnection.getResponseCode();
                    if (responseCode == HttpURLConnection.HTTP_OK){
                        ArrayList<String> sources= new ArrayList<>();
                        ArrayList<String> authors= new ArrayList<>();
                        ArrayList<String> title= new ArrayList<>();
                        ArrayList<String> urlNews= new ArrayList<>();
                        ArrayList<String> urlImage= new ArrayList<>();
                        ArrayList<String> publishedAt= new ArrayList<>();

                        InputStream in = httpConnection.getInputStream();
                        JsonReader reader =
                                new JsonReader(new InputStreamReader(in, "UTF-8"));
                        try {
                            reader.beginObject();
                            while (reader.hasNext()) {
                                if (reader.nextName().equals("articles"))
                                    break;
                                else
                                    reader.skipValue();
                            }
                            reader.beginArray();
                            while( reader.hasNext()) {
                                ArrayList<String> vals= readSnippet(reader);
                                if ( !vals.get(2).isEmpty()){
                                    sources.add(vals.get(0));
                                    authors.add(vals.get(1));
                                    title.add(vals.get(2));
                                    urlNews.add(vals.get(3));
                                    urlImage.add(vals.get(4));
                                }
                            }
                            reader.endArray();
                            Map allSnippets= new HashMap();
                            for (int i= 0; i< sources.size(); i++){
                                Map snippet= new HashMap();
                                snippet.put("source", sources.get(i));
                                snippet.put("title", title.get(i));
                                snippet.put("urlImage", urlImage.get(i));
                                snippet.put("urlNews", urlNews.get(i));
                                allSnippets.put( Integer.toString( i), snippet);
                            }
                            Map update= new HashMap();
                            update.put("news_category/top_news/news", allSnippets);
                            update.put( "news_category/top_news/timestamp", ServerValue.TIMESTAMP);
                            database.updateChildren(update).addOnCompleteListener(new OnCompleteListener() {
                                @Override
                                public void onComplete(@NonNull Task task) {
                                    if (task.isSuccessful())
                                        if ( recyclerView.getAdapter() != null)
                                            adapter.refresh();
                                        else
                                            populatePage();
                                }
                            });
                        }
                        finally{
                            httpConnection.disconnect();
                            reader.close();
                        }
                    }
                    else {
                        httpConnection.disconnect();
                        return false;
                    }
                }
                catch (MalformedURLException e) { e.printStackTrace(); }
                catch (IOException e) { e.printStackTrace(); }
                return false;
            }
        }.execute();
    }

    public void populatePage(){
        Query baseQuery = database.child("news_category/top_news/news");
        PagedList.Config config = new PagedList.Config.Builder()
                .setEnablePlaceholders(false).setPrefetchDistance(10)
                .setPageSize(20).build();
        DatabasePagingOptions<News> options = new DatabasePagingOptions.Builder<News>()
                .setLifecycleOwner(this)
                .setQuery(baseQuery, config, News.class)
                .build();

        adapter = new FirebaseRecyclerPagingAdapter<News, NewsViewHolder>(options) {
                @NonNull
                @Override
                public NewsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                    View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_top_news, parent, false);
                    return new NewsViewHolder(view);
                }
                @Override
                protected void onBindViewHolder(@NonNull NewsViewHolder holder,
                                                int position,
                                                @NonNull final News model) {
                    if ( !model.getUrlImage().isEmpty())
                        Picasso.get().load( model.getUrlImage())
                                .resize(150,150).centerCrop().into(holder.picture);

                    holder.title.setText( model.getTitle());
                    holder.source.setText( model.getSource());
                    holder.constraintLayout.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            Intent intent= new Intent( getContext(), WebViewActivity.class);
                            intent.putExtra( "url", model.getUrlNews());
                            intent.putExtra( "actionBarTitle", "Top News");
                            startActivity( intent);
                        }
                    });
                }
                @Override
                protected void onLoadingStateChanged(@NonNull LoadingState state) {}
            };
        recyclerView.setAdapter(adapter);
    }
    public static class NewsViewHolder extends RecyclerView.ViewHolder {

        public final TextView title;
        public final ConstraintLayout constraintLayout;
        public final ImageView picture;
        public final TextView source;

        public NewsViewHolder(@NonNull View itemView) {
            super(itemView);
            picture= itemView.findViewById(R.id.news_picture);
            title= itemView.findViewById(R.id.news_title);
            constraintLayout= itemView.findViewById(R.id.news_constraint_layout);
            source= itemView.findViewById(R.id.news_source);
        }
    }
    private Runnable timerTask = new Runnable() {
        @Override
        public void run() {
            handler.postDelayed(timerTask, 6000);
        }
    };
}
