package com.pulsefire.seefood.recipes

import android.os.Build
import android.os.Bundle
import android.text.Html
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.google.android.material.snackbar.Snackbar
import com.pulsefire.seefood.databinding.FragmentInfoBinding
import com.pulsefire.seefood.recipes.api.Common
import com.pulsefire.seefood.recipes.api.data.ExtendedIngredientsItem
import com.pulsefire.seefood.recipes.api.data.RecipeInfo
import com.squareup.picasso.Picasso
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

private const val PHOTO_URL = "param1"
private const val ID = "param2"

class InfoFragment : Fragment() {
    private var photoUrl: String? = null
    private var id: Int? = null
    private var _binding: FragmentInfoBinding? = null
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            photoUrl = it.getString(PHOTO_URL)
            id = it.getInt(ID)
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View {
        _binding = FragmentInfoBinding.inflate(inflater, container, false)

        Picasso.get().load(photoUrl).into(binding.photo)
        getData()

        return binding.root
    }

    private fun getData() {
        val service = Common.retrofitService
        binding.loader.visibility = View.VISIBLE
        service.getRecipeById(id!!).enqueue(object : Callback<RecipeInfo> {
            override fun onResponse(call: Call<RecipeInfo>, response: Response<RecipeInfo>) {
                if (response.isSuccessful) {
                    val recipe: RecipeInfo = response.body()!!;
                    binding.title.text = recipe.title
                    binding.chipHealthy.visibility = if (recipe.veryHealthy!!) View.VISIBLE else View.GONE
                    binding.chipGlutenfree.visibility = if (recipe.glutenFree!!) View.VISIBLE else View.GONE
                    binding.chipSustainable.visibility = if (recipe.sustainable!!) View.VISIBLE else View.GONE
                    binding.chipVegan.visibility = if (recipe.vegan!!) View.VISIBLE else View.GONE
                    binding.chipVegetarian.visibility = if (recipe.vegetarian!!) View.VISIBLE else View.GONE
                    binding.summary.text = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                        Html.fromHtml(recipe.summary, Html.FROM_HTML_MODE_COMPACT)
                    } else {
                        Html.fromHtml(recipe.summary)
                    }
                    if (recipe.instructions != null) {
                        binding.instructions.text = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                            Html.fromHtml(recipe.instructions, Html.FROM_HTML_MODE_COMPACT)
                        } else {
                            Html.fromHtml(recipe.instructions)
                        }
                    } else {
                        binding.instructionsCard.visibility = View.GONE
                    }
                    if (recipe.extendedIngredients != null) {
                        var ingredients = StringBuilder();
                        for (ingredient: ExtendedIngredientsItem? in recipe.extendedIngredients!!) {
                            ingredients.append(ingredient?.original + "\n")
                        }
                        binding.ingredients.text = ingredients.toString()
                    } else {
                        binding.ingredientsCard.visibility = View.GONE
                    }
                } else {
                    showError(response.message())
                }
                binding.loader.visibility = View.GONE
            }

            override fun onFailure(call: Call<RecipeInfo>, t: Throwable) {
                showError(t.message.toString())
                binding.loader.visibility = View.VISIBLE
            }
        })
    }

    private fun showError(message: String) {
        Snackbar.make(requireActivity().findViewById(android.R.id.content), message, Snackbar.LENGTH_SHORT).show()
    }

    companion object {
        @JvmStatic
        fun newInstance(param1: String, param2: Int) =
                InfoFragment().apply {
                    arguments = Bundle().apply {
                        putString(PHOTO_URL, param1)
                        putInt(ID, param2)
                    }
                }
    }
}