package com.example.tomcat.bakingapp.json;

import com.example.tomcat.bakingapp.models.Recipe;
import java.util.List;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Headers;

public interface BakingService {

    String URL = "https://d17h27t6h515a5.cloudfront.net/topher/2017/May/59121517_baking/";
    @Headers("Content-Type: application/json")
    @GET("baking.json")
    Call<List<Recipe>> getRecipes();
}
