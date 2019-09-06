package com.example.examplecodingkotlin.dto

import io.realm.RealmModel
import io.realm.annotations.RealmClass

/**
 * Created by fbarbieri on 2019-08-28.
 */
@RealmClass
open class Recipe : RealmModel {

    var title: String? = null
    var href: String? = null
    var ingredients: String? = null
    var thumbnail: String? = null

    /**
     * @param title the recipe title
     * @param href the recipe link
     * @param ingredients the recipe ingredients
     * @param thumbnail the recipe image
     */
    constructor(title: String, href: String, ingredients: String,
                thumbnail: String) {
        this.title = title
        this.href = href
        this.ingredients = ingredients
        this.thumbnail = thumbnail
    }

    constructor() {}
}
