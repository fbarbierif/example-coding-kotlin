package com.example.examplecodingkotlin.adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.examplecodingkotlin.R
import com.example.examplecodingkotlin.activity.DetailActivity
import com.example.examplecodingkotlin.dto.Recipe
import com.facebook.drawee.view.SimpleDraweeView
import io.realm.Realm
import java.util.ArrayList

/**
 * Created by fbarbieri on 2019-08-28.
 */
class RecipesAdapter(private val recipes: ArrayList<Recipe>, internal val context: Context,
                     private val showFavoriteImage: Boolean?) : RecyclerView.Adapter<RecipesAdapter.RecipesViewHolder>() {

    class RecipesViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        var tvName: TextView
        var tvIngredients: TextView
        var tvHasLactose: TextView
        var imageView: SimpleDraweeView
        var ivFavorite: ImageView

        init {
            tvName = view.findViewById(R.id.tvName)
            tvIngredients = view.findViewById(R.id.tvIngredients)
            tvHasLactose = view.findViewById(R.id.tvHasLactose)
            imageView = view.findViewById(R.id.imageView)
            ivFavorite = view.findViewById(R.id.ivFavorite)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecipesViewHolder {
        val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.recipe_item, parent, false)

        return RecipesViewHolder(view)
    }

    override fun onBindViewHolder(holder: RecipesViewHolder, position: Int) {
        holder.tvName.text = recipes[position].title
        holder.tvIngredients.text = recipes[position].ingredients
        holder.imageView.setImageURI(recipes[position].thumbnail)

        if (recipes[position].ingredients!!.contains(MILK) || recipes[position].ingredients!!.contains(CHEESE)) {
            holder.tvHasLactose.visibility = View.VISIBLE
        } else {
            holder.tvHasLactose.visibility = View.GONE
        }
        val adapterRecipe = recipes[position]

        val realm = Realm.getDefaultInstance()
        val resultRecipe = realm.where(Recipe::class.java)
                .contains(TITLE, adapterRecipe.title).findFirst()

        if (showFavoriteImage!!) {
            holder.ivFavorite.visibility = View.VISIBLE

            if (resultRecipe != null) {
                holder.ivFavorite.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.favorite_filled))
            } else {
                holder.ivFavorite.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.favorite_empty))
            }

            holder.ivFavorite.setOnClickListener {
                val adapterRecipe = recipes[position]

                val realm = Realm.getDefaultInstance()
                val resultRecipe = realm.where(Recipe::class.java)
                        .contains(TITLE, adapterRecipe.title).findFirst()

                if (resultRecipe == null) {
                    saveRecipeToRealm(adapterRecipe)
                    holder.ivFavorite
                            .setImageDrawable(ContextCompat.getDrawable(context, R.drawable.favorite_filled))
                } else {
                    deleteRecipeFromRealm(adapterRecipe)
                    holder.ivFavorite
                            .setImageDrawable(ContextCompat.getDrawable(context, R.drawable.favorite_empty))
                }
            }
        } else {
            holder.ivFavorite.visibility = View.GONE
        }

        holder.itemView.setOnClickListener {
            val intent = Intent(context, DetailActivity::class.java)
            intent.putExtra(TITLE, recipes[position].title)
            intent.putExtra(URL, recipes[position].href)
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            context.startActivity(intent)
        }
    }

    private fun deleteRecipeFromRealm(adapterRecipe: Recipe) {

        val realm = Realm.getDefaultInstance()

        realm.executeTransaction { realm ->
            val result = realm.where(Recipe::class.java).equalTo(TITLE, adapterRecipe.title).findAll()
            result.deleteAllFromRealm()
        }

        realm.close()
    }

    private fun saveRecipeToRealm(adapterRecipe: Recipe) {
        val realm = Realm.getDefaultInstance()
        realm.beginTransaction()

        val recipe = realm.createObject(Recipe::class.java)
        recipe.title = adapterRecipe.title
        recipe.ingredients = adapterRecipe.ingredients
        recipe.thumbnail = adapterRecipe.thumbnail
        recipe.href = adapterRecipe.href

        realm.commitTransaction()
        realm.close()
    }

    override fun getItemCount(): Int {
        return recipes.size
    }

    companion object {
        private val URL = "url"
        private val TITLE = "title"
        private val MILK = "milk"
        private val CHEESE = "cheese"
    }
}
