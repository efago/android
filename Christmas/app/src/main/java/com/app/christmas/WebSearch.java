package com.app.christmas;

import android.os.AsyncTask;
import android.util.Log;

import com.microsoft.azure.cognitiveservices.search.websearch.BingWebSearchAPI;
import com.microsoft.azure.cognitiveservices.search.websearch.BingWebSearchManager;
import com.microsoft.azure.cognitiveservices.search.websearch.models.SearchResponse;
import com.microsoft.azure.cognitiveservices.search.websearch.models.WebPage;

public class WebSearch {

    public static StringBuilder temp= new StringBuilder();

    public static String search(final String tag) {

        new AsyncTask<Void, Void, String>() {
            @Override
            protected String doInBackground(Void... voids) {

                BingWebSearchAPI client = BingWebSearchManager.authenticate("888089c09cb74c3da27df52e55255891");

                SearchResponse webData = client.bingWebs().search()
                        .withQuery(tag)
                        .withMarket("en-us")
                        .withCount(10)
                        .execute();

                Log.d("inside", "size is: "+webData.webPages().value().size());
                if (webData != null && webData.webPages() != null && webData.webPages().value() != null &&
                        webData.webPages().value().size() > 0) {
                    // find the first web page
                    WebPage firstWebPagesResult = webData.webPages().value().get(0);
                    for (WebPage page : webData.webPages().value()) {
                        temp.append( page.name());
                        Log.d("inside", page.name());
                    }
                }
                return null;
            }
        }.execute();
        Log.d("Inside", temp.toString());
        return "efrem";
    }
}
