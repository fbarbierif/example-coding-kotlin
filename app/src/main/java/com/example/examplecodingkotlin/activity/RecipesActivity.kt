package com.example.examplecodingkotlin.activity

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.text.Editable
import android.text.TextWatcher
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.ProgressBar
import android.widget.TextView
import androidx.appcompat.app.ActionBar
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.example.examplecodingkotlin.R
import com.example.examplecodingkotlin.adapter.RecipesAdapter
import com.example.examplecodingkotlin.dto.Recipe
import com.example.examplecodingkotlin.presenter.RecipesPresenter
import com.example.examplecodingkotlin.util.EndlessRecyclerViewScrollListener
import com.example.examplecodingkotlin.view.RecipesView
import java.util.ArrayList
import java.util.Objects

class RecipesActivity : AppCompatActivity(), RecipesView {
    internal lateinit var recipesPresenter: RecipesPresenter
    internal lateinit var recyclerView: RecyclerView
    private var recipesAdapter: RecipesAdapter? = null
    private var layoutManager: LinearLayoutManager? = null
    private var progressBar: ProgressBar? = null
    internal lateinit var etSearch: EditText
    internal lateinit var ivClear: ImageView
    private var llErrorEmptyView: LinearLayout? = null
    private var tvMessage: TextView? = null
    internal val recipes = ArrayList<Recipe>()
    internal var ingredients = ""
    private var srLayout: SwipeRefreshLayout? = null
    internal var handler = Handler()
    internal lateinit var runnable: Runnable
    internal lateinit var listener: EndlessRecyclerViewScrollListener

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_recipes)

        setupActionBar()

        progressBar = findViewById(R.id.progressBar)
        recyclerView = findViewById(R.id.recyclerview_recipes)
        etSearch = findViewById(R.id.etSearch)
        ivClear = findViewById(R.id.ivClear)
        llErrorEmptyView = findViewById(R.id.llErrorEmptyView)
        tvMessage = findViewById(R.id.tvMessage)
        srLayout = findViewById(R.id.srLayout)

        recyclerView.setHasFixedSize(true)
        layoutManager = LinearLayoutManager(this)
        recyclerView.layoutManager = layoutManager

        listener = object : EndlessRecyclerViewScrollListener(layoutManager!!) {
            override fun onLoadMore(page: Int, totalItemsCount: Int,
                                    recyclerView: RecyclerView) {
                recipesPresenter.getRecipesData(page.toString(), ingredients)
            }
        }

        recyclerView.addOnScrollListener(listener)

        recipesPresenter = RecipesPresenter(this)
        showProgressBar()
        recipesPresenter.getRecipesData(FIRST_PAGE.toString(), ingredients)

        srLayout!!.setOnRefreshListener {
            recipes.clear()
            ingredients = EMPTY
            recipesPresenter.getRecipesData(FIRST_PAGE.toString(), ingredients)
            recyclerView.addOnScrollListener(listener)
        }

        runnable = Runnable {
            etSearch.clearFocus()
            showProgressBar()
            recipes.clear()
            recipesPresenter.getRecipesData(FIRST_PAGE.toString(), ingredients)
            recyclerView.addOnScrollListener(listener)
            ivClear.visibility = View.VISIBLE
        }

        etSearch.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(charSequence: CharSequence, i: Int,
                                           i1: Int, i2: Int) {

            }

            override fun onTextChanged(charSequence: CharSequence, i: Int, i1: Int,
                                       i2: Int) {

            }

            override fun afterTextChanged(editable: Editable) {
                if (editable.toString().length > 2) {
                    cancelTimer()
                    ingredients = getIngredientsList(editable.toString())
                    handler.postDelayed(runnable, DELAY_MILLIS.toLong())
                } else {
                    cancelTimer()
                }
            }
        })

        ivClear.setOnClickListener { resetSearch() }
    }

    internal fun getIngredientsList(input: String): String {
        val ingredientsList: String
        ingredientsList = input.replace(" ", ",")
        return ingredientsList.toLowerCase()
    }

    private fun hideKeyboard() {
        val imm = getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
        var view = currentFocus
        if (view == null) {
            view = View(this)
        }
        imm.hideSoftInputFromWindow(view.windowToken, 0)
    }

    internal fun cancelTimer() {
        ivClear.visibility = View.INVISIBLE
        handler.removeCallbacks(runnable)
        handler.removeCallbacksAndMessages(null)
    }

    private fun setupActionBar() {
        Objects.requireNonNull<ActionBar>(supportActionBar)
                .setTitle(R.string.recipes_list_activity_title)
        Objects.requireNonNull<ActionBar>(supportActionBar).setElevation(0f)
    }

    override fun showRecipesData(recipesResult: ArrayList<Recipe>) {
        recyclerView.visibility = View.VISIBLE
        hideKeyboard()
        llErrorEmptyView!!.visibility = View.GONE
        recipes.addAll(recipesResult)

        if (recipesAdapter == null) {
            recipesAdapter = RecipesAdapter(recipes, applicationContext, true)
            recyclerView.adapter = recipesAdapter
        } else {
            recipesAdapter!!.notifyDataSetChanged()
        }

        srLayout!!.isRefreshing = false
        hideProgressBar()
    }

    override fun showProgressBar() {
        progressBar!!.visibility = View.VISIBLE
    }

    override fun hideProgressBar() {
        progressBar!!.visibility = View.GONE
    }

    override fun showErrorView() {
        showErrorEmptyLayout(getString(R.string.error))
    }

    override fun showEmptyView() {
        showErrorEmptyLayout(getString(R.string.empty))
    }

    private fun showErrorEmptyLayout(message: String) {
        recyclerView.visibility = View.GONE
        hideKeyboard()
        recipesAdapter!!.notifyDataSetChanged()
        srLayout!!.isRefreshing = false
        tvMessage!!.text = message
        llErrorEmptyView!!.visibility = View.VISIBLE
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        val menuInflater = menuInflater
        menuInflater.inflate(R.menu.menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.menu_favorites) {
            val intent = Intent(applicationContext, FavoritesActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            applicationContext.startActivity(intent)
        }
        return true
    }

    override fun onBackPressed() {
        if (!ingredients.isEmpty()) {
            resetSearch()
        } else {
            super.onBackPressed()
        }
    }

    fun resetSearch() {
        recipes.clear()
        ingredients = EMPTY
        etSearch.clearFocus()
        etSearch.setText(EMPTY)
        hideKeyboard()
        showProgressBar()
        recipesPresenter.getRecipesData(FIRST_PAGE.toString(), ingredients)
        recyclerView.addOnScrollListener(listener)
    }

    companion object {

        val DELAY_MILLIS = 1000
        val EMPTY = ""
        private val FIRST_PAGE = 1
    }
}
