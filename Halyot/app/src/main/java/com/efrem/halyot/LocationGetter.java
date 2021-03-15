package com.efrem.halyot;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface LocationGetter {
        @GET("/json")
        Call<ResponseBody> getLocation(@Query("latlng") String loc,
                                       @Query("key") String apiKey);

        @GET("posts")
        Call<ResponseBody> getWhatever(@Query("userId") int id,
                                       @Query("_sort") String sort);

        @FormUrlEncoded
        @Headers("Authorization: AccessKey VCW02pVVLSGTCKDcKBPKMhzTp")
        @POST("/messages")
        Call<ResponseBody> sendSMS(@Field("recipients") String recipient,
                                   @Field("originator") String originator,
                                   @Field("body") String message);
}
