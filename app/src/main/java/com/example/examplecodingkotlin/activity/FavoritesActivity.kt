package com.example.examplecodingkotlin.activity

import android.os.Bundle
import android.view.View
import android.widget.LinearLayout
import android.widget.ProgressBar
import android.widget.TextView
import androidx.appcompat.app.ActionBar
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.examplecodingkotlin.R
import com.example.examplecodingkotlin.adapter.RecipesAdapter
import com.example.examplecodingkotlin.dto.Recipe
import io.realm.Realm
import io.realm.RealmResults
import java.util.ArrayList
import java.util.Objects

/**
 * Created by fbarbieri on 2019-08-28.
 */
class FavoritesActivity : AppCompatActivity() {

    private var recyclerView: RecyclerView? = null
    private var progressBar: ProgressBar? = null
    private var llErrorEmptyView: LinearLayout? = null
    private var tvMessage: TextView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_favorites)

        setupActionBar()

        progressBar = findViewById(R.id.progressBar)
        recyclerView = findViewById(R.id.recyclerview_recipes)
        llErrorEmptyView = findViewById(R.id.llErrorEmptyView)
        tvMessage = findViewById(R.id.tvMessage)

        recyclerView!!.setHasFixedSize(true)
        val layoutManager = LinearLayoutManager(this)
        recyclerView!!.layoutManager = layoutManager

        showProgressBar()

        fetchAllFavorites()
    }


    private fun fetchAllFavorites() {
        val recipes = ArrayList<Recipe>()

        val realm = Realm.getDefaultInstance()
        val resultRecipes = realm.where(Recipe::class.java).findAll()

        if (resultRecipes.isEmpty()) {
            showEmptyView()
        } else {
            llErrorEmptyView!!.visibility = View.GONE
            recipes.addAll(realm.copyFromRealm(resultRecipes))
            setAllData(recipes)
        }

        realm.close()
    }

    private fun showEmptyView() {
        hideProgressBar()
        tvMessage!!.text = getString(R.string.empty)
        llErrorEmptyView!!.visibility = View.VISIBLE
    }

    private fun setupActionBar() {
        Objects.requireNonNull<ActionBar>(supportActionBar)
                .setTitle(R.string.favorites_activity_title)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        supportActionBar!!.setDisplayShowHomeEnabled(true)
    }

    private fun setAllData(recipes: ArrayList<Recipe>) {
        val recipesAdapter = RecipesAdapter(recipes, applicationContext, false)
        recyclerView!!.adapter = recipesAdapter

        hideProgressBar()
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }

    fun showProgressBar() {
        progressBar!!.visibility = View.VISIBLE
    }

    fun hideProgressBar() {
        progressBar!!.visibility = View.GONE
    }
}
