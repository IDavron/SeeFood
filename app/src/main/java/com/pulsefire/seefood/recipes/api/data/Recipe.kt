package com.pulsefire.seefood.recipes.api.data

import com.google.gson.annotations.SerializedName

data class Recipe(

	@field:SerializedName("image")
	val image: String? = null,

	@field:SerializedName("usedIngredients")
	val usedIngredients: List<UsedIngredientsItem?>? = null,

	@field:SerializedName("missedIngredients")
	val missedIngredients: List<MissedIngredientsItem?>? = null,

	@field:SerializedName("missedIngredientCount")
	val missedIngredientCount: Int? = null,

	@field:SerializedName("id")
	val id: Int? = null,

	@field:SerializedName("title")
	val title: String? = null,

	@field:SerializedName("imageType")
	val imageType: String? = null,

	@field:SerializedName("usedIngredientCount")
	val usedIngredientCount: Int? = null,

	@field:SerializedName("likes")
	val likes: Int? = null
)

data class UsedIngredientsItem(

	@field:SerializedName("image")
	val image: String? = null,

	@field:SerializedName("amount")
	val amount: Double? = null,

	@field:SerializedName("original")
	val original: String? = null,

	@field:SerializedName("unitLong")
	val unitLong: String? = null,

	@field:SerializedName("aisle")
	val aisle: String? = null,

	@field:SerializedName("originalName")
	val originalName: String? = null,

	@field:SerializedName("unit")
	val unit: String? = null,

	@field:SerializedName("unitShort")
	val unitShort: String? = null,

	@field:SerializedName("meta")
	val meta: List<String?>? = null,

	@field:SerializedName("name")
	val name: String? = null,

	@field:SerializedName("originalString")
	val originalString: String? = null,

	@field:SerializedName("id")
	val id: Int? = null,

	@field:SerializedName("metaInformation")
	val metaInformation: List<String?>? = null
)

data class MissedIngredientsItem(

	@field:SerializedName("image")
	val image: String? = null,

	@field:SerializedName("amount")
	val amount: Double? = null,

	@field:SerializedName("original")
	val original: String? = null,

	@field:SerializedName("unitLong")
	val unitLong: String? = null,

	@field:SerializedName("aisle")
	val aisle: String? = null,

	@field:SerializedName("originalName")
	val originalName: String? = null,

	@field:SerializedName("unit")
	val unit: String? = null,

	@field:SerializedName("unitShort")
	val unitShort: String? = null,

	@field:SerializedName("meta")
	val meta: List<String?>? = null,

	@field:SerializedName("name")
	val name: String? = null,

	@field:SerializedName("originalString")
	val originalString: String? = null,

	@field:SerializedName("id")
	val id: Int? = null,

	@field:SerializedName("metaInformation")
	val metaInformation: List<String?>? = null
)
