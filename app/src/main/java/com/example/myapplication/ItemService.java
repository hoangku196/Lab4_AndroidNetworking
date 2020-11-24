package com.example.myapplication;

import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.GET;

public interface ItemService {
    @GET("photos")
    Call<List<Item>> getItem();

}
