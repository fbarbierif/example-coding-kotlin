package com.example.examplecodingkotlin.service

import com.example.examplecodingkotlin.dto.Result
import retrofit2.http.GET
import retrofit2.http.Query
import rx.Observable

/**
 * Created by fbarbieri on 2019-08-28.
 */
interface RecipesService {

    /**
     * @return the recipes list
     */
    @GET(URL_RECIPES)
    fun getRecipesData(@Query("p") page: String, @Query("i") ingredients: String): Observable<Result>

    companion object {

        const val URL_RECIPES = "api/?"
    }
}
