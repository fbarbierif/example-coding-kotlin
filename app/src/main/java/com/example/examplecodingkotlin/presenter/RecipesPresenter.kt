package com.example.examplecodingkotlin.presenter

import android.util.Log
import com.example.examplecodingkotlin.dto.Result
import com.example.examplecodingkotlin.model.RecipesModel
import com.example.examplecodingkotlin.view.RecipesView
import rx.Observable
import rx.Subscriber
import rx.android.schedulers.AndroidSchedulers
import rx.schedulers.Schedulers

/**
 * Created by fbarbieri on 2019-08-28.
 */
class RecipesPresenter(internal val recipesView: RecipesView) {

    fun getRecipesData(page: String, ingredients: String) {

        val observable = RecipesModel.instance.getRecipesData(page, ingredients)
        observable.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(object : Subscriber<Result>() {
                    override fun onCompleted() {
                        //Nothing to do
                    }

                    override fun onError(e: Throwable) {
                        recipesView.hideProgressBar()
                        recipesView.showErrorView()
                        Log.e(REQUEST_ERROR, e.message)
                    }

                    override fun onNext(result: Result?) {
                        if (result == null || result.results.isEmpty()) {
                            recipesView.hideProgressBar()
                            recipesView.showEmptyView()
                        } else {
                            recipesView.showRecipesData(result.results)
                        }
                    }
                })
    }

    companion object {
        private val REQUEST_ERROR = "Request error"
    }
}
