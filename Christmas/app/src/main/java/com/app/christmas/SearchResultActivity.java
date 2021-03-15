package com.app.christmas;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.os.AsyncTask;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.speech.tts.UtteranceProgressListener;
import android.widget.TextView;

import com.microsoft.azure.cognitiveservices.search.websearch.BingWebSearchAPI;
import com.microsoft.azure.cognitiveservices.search.websearch.BingWebSearchManager;
import com.microsoft.azure.cognitiveservices.search.websearch.models.SearchResponse;
import com.microsoft.azure.cognitiveservices.search.websearch.models.WebPage;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.HashMap;
import java.util.Locale;

import static com.app.christmas.MainActivity.TTS_ID;

public class SearchResultActivity extends AppCompatActivity {

    TextView body, title;
    TextToSpeech tTS;
    HashMap<String, String> ttsMap;
    UtteranceProgressListener utteranceListener;

    String content;
    long totalLength;
    int chunk;
    int count;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_result);
        body= findViewById(R.id.body);
        title= findViewById(R.id.heading);
        tTS= new TextToSpeech(this, new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int i) {
                if ( i == TextToSpeech.SUCCESS){
                    tTS.setLanguage(Locale.getDefault());
                    tTS.setOnUtteranceProgressListener( utteranceListener);
                    ttsMap= new HashMap<>();
                    ttsMap.put(TextToSpeech.Engine.KEY_PARAM_UTTERANCE_ID, TTS_ID);
                    chunk= TextToSpeech.getMaxSpeechInputLength() - 1;
                    webSearch();
                }
            }
        });
        utteranceListener= new UtteranceProgressListener() {
            @Override
            public void onStart(String s) { count++;}
            @Override
            public void onDone(String s) {
                if ( ( count + 1) * chunk < totalLength) {
                    tTS.speak(content.substring(count * chunk, (count + 1) * chunk), TextToSpeech.QUEUE_FLUSH, ttsMap);
                }
                else {
                    tTS.speak( content.substring( count * chunk), TextToSpeech.QUEUE_FLUSH, null);
                }
            }
            @Override
            public void onError(String s) { }
        };
    }

    @SuppressLint("StaticFieldLeak")
    public void webSearch() {
        new AsyncTask<Void, Void, String[]>() {
            @Override
            protected String[] doInBackground(Void... voids) {

                //get this one from azure free cognitive web search account
                String subscriptionKey= "888089c09cb74c3da27df52e55255891";
                BingWebSearchAPI client = BingWebSearchManager.authenticate(subscriptionKey);

                SearchResponse webData = client.bingWebs().search()
                        .withQuery(getIntent().getStringExtra("tag"))
                        .withMarket("en-us")
                        .withCount(10)
                        .execute();

                if ( webData.webPages() != null && webData.webPages().value() != null &&
                        webData.webPages().value().size() > 0) {
                    for (WebPage page : webData.webPages().value()) {
                        String passage= parseSite(page.url());
                        if ( !passage.isEmpty() && passage.split("\\s+").length > 200){
                            String[] display= {page.name(), passage};
                            return display;
                        }
                    }
                }
                return null;
            }

            @Override
            protected void onPostExecute(String[] s) {
                super.onPostExecute(s);
                if ( s != null){
                    title.setText(s[0]);
                    body.setText(s[1]);
                    count= 0;
                    content= s[1];
                    totalLength= content.length();
                    tTS.speak( s[1].substring( 0, chunk), TextToSpeech.QUEUE_FLUSH, ttsMap);
                }
                else {
                    title.setText( "");
                    body.setText(R.string.no_match);
                }
            }

            private String parseSite(String url){
                StringBuilder passage = new StringBuilder();
                try {
                    Document document = Jsoup.connect(url).get();
                    Elements paragraphs = document.getElementsByTag("p");
                    for (Element paragraph : paragraphs) {
                        if (paragraph.text().split(" ").length > 30) {
                            passage.append(paragraph.text() + "\n");
                        }
                    }
                }
                catch (IOException e) {
                    e.printStackTrace();
                }
                return passage.toString();
            }
        }.execute();
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        tTS.shutdown();
    }
}
