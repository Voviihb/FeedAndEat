package com.vk_edu.feed_and_eat.features.recipe.domain



class RecipeForm {
    val picture : Int
    val cooked : Int
    val pictureHeight : Int
    val rating : Double
    val inFavor : Boolean
    val name : String
    val description : List<String>
    val ingredients : List<String>
    val steps : List<String>
    val tags : List<String>
    val energyData : List<Int>

    constructor(
        Picture : Int,
        Rating : Double,
        Cooked : Int,
        Description : List<String>,
        InFavor : Boolean,
        Name : String,
        Ingredients : List<String>,
        Steps : List<String>,
        Tags : List<String>,
        EnergyData : List<Int>,
        PictureHeight : Int,
    ){
        picture = Picture
        rating = Rating
        cooked = Cooked
        description = Description
        inFavor = InFavor
        name = Name
        ingredients = Ingredients
        steps = Steps
        tags = Tags
        energyData = EnergyData
        pictureHeight = PictureHeight
    }
}