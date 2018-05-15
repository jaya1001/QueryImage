package com.example.imagequery;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Url;

public interface RequestInterface {

    //@GET("services/rest/?method=flickr.photos.search&api_key=38fb0bce422a6406ed3860219e9c9619&tags=icecream&format=json&nojsoncallback=1&per_page=20&media=photos&extras=url_o")
    @GET
    Call<JSONResponse> getJSON(@Url String s);
}