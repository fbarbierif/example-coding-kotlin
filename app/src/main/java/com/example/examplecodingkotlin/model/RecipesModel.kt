package com.example.examplecodingkotlin.model

import com.example.examplecodingkotlin.dto.Result
import com.example.examplecodingkotlin.service.RecipesService
import retrofit2.Retrofit
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import rx.Observable

/**
 * Created by fbarbieri on 2019-08-28.
 */
class RecipesModel {

    private val service: RecipesService
        get() {
            val retrofitBuilder = Retrofit.Builder().baseUrl(BASE_URL)
            retrofitBuilder.addConverterFactory(GsonConverterFactory.create())
            retrofitBuilder.addCallAdapterFactory(RxJavaCallAdapterFactory.create())
            return retrofitBuilder.build().create(RecipesService::class.java)
        }

    /**
     * Call the endpoint to retrieve the recipes list
     *
     * @return the response containing the recipes
     */
    fun getRecipesData(page: String, ingredients: String): Observable<Result> {
        return service.getRecipesData(page, ingredients)
    }

    companion object {

        /**
         * Factory method
         *
         * @return the model's singleton
         */
        val instance = RecipesModel()
        private val BASE_URL = "http://www.recipepuppy.com/"
    }
}
