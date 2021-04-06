package com.pulsefire.seefood.recipes

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.google.android.material.snackbar.Snackbar
import com.pulsefire.seefood.databinding.ActivityInfoBinding
import com.pulsefire.seefood.recipes.api.Common
import com.pulsefire.seefood.recipes.api.RetrofitService
import com.pulsefire.seefood.recipes.api.data.Recipe
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class InfoActivity : AppCompatActivity() {
    lateinit var binding: ActivityInfoBinding
    lateinit var mService: RetrofitService
    private lateinit var ingredient: String
    private lateinit var adapter: ViewPagerAdapter
    private lateinit var fragmentList: ArrayList<Fragment>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Binding
        binding = ActivityInfoBinding.inflate(layoutInflater)
        val view = binding.root

        // View Pager
        fragmentList = arrayListOf()
        adapter = ViewPagerAdapter(fragmentList, supportFragmentManager, lifecycle)
        binding.vp.adapter = adapter

        // ActionBar
        supportActionBar?.title = "Recipes"
        supportActionBar?.hide()

        mService = Common.retrofitService
        ingredient = intent.getStringExtra(EXTRA).toString()

        setContentView(view)
    }

    override fun onResume() {
        super.onResume()
        if (ingredient != "") {
            getData()
            binding.loader.visibility = View.VISIBLE
        } else {
            fragmentList.add(InfoFragment.newInstance("", 1))
            adapter.notifyDataSetChanged()
        }
    }

    private fun getData() {
        mService.getRecipesByIngredients(5, 1, ingredient, true).enqueue(object : Callback<MutableList<Recipe>> {
            override fun onResponse(call: Call<MutableList<Recipe>>, response: Response<MutableList<Recipe>>) {
                if (response.isSuccessful) {
                    for (recipe in response.body()!!.listIterator()) {
                        fragmentList.add(InfoFragment.newInstance(recipe.image.toString(), recipe.id!!.toInt()))
                    }
                    adapter.notifyDataSetChanged()
                } else {
                    showError(response.message())
                }

                binding.loader.visibility = View.GONE
            }

            override fun onFailure(call: Call<MutableList<Recipe>>, t: Throwable) {
                Log.e("Error", t.message.toString())
                showError(t.message.toString())
                binding.loader.visibility = View.GONE
            }
        })
    }

    private fun showError(message: String) {
        Snackbar.make(findViewById(android.R.id.content), message, Snackbar.LENGTH_SHORT).show()
    }

    companion object {
        const val EXTRA = "ingredients"
    }
}