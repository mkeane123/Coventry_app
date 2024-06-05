package com.example.coventry.data

import com.example.coventry.R

object DataSource {

    private val coffeeShops = listOf(
        Place(
            id = 1, titleResourceId = R.string.sovrano_place_name,
            openingTimeResourceId = R.string.sovrano_opening_times,
            imageResourceId = R.drawable.sovrano_front,
            placeDetails = R.string.sovrano_details
        ),
        Place(
            id = 2, titleResourceId = R.string.gorety_place_name,
            openingTimeResourceId = R.string.gorety_opening_times,
            imageResourceId = R.drawable.gorety,
            placeDetails = R.string.gorety_details
        ),
        Place(
            id = 3, titleResourceId = R.string.lucky_lounge_cafe_place_name,
            openingTimeResourceId = R.string.lucky_lounge_cafe_opening_times,
            imageResourceId = R.drawable.lucky_lounge_interior,
            placeDetails = R.string.lucky_lounge_cafe_details
        )

    )

    private val parks = listOf(
        Place(
            id = 1, titleResourceId = R.string.war_memorial_place_name,
            openingTimeResourceId = null,
            imageResourceId = R.drawable.war_memorial,
            placeDetails = R.string.war_memorial_details
        ),
        Place(
            id = 2, titleResourceId = R.string.coombe_abbey_place_name,
            openingTimeResourceId = R.string.coombe_abbey_opening_times,
            imageResourceId = R.drawable.coombe_abbey,
            placeDetails = R.string.coombe_abbey_details
        ),
        Place(
            id = 3, titleResourceId = R.string.allesley_park_place_name,
            openingTimeResourceId = null,
            imageResourceId = R.drawable.allesley_park,
            placeDetails = R.string.allesley_park_details
        )
    )

    private val bars = listOf(
        Place(
            id = 1, titleResourceId = R.string.boom_battle_bar_place_name,
            openingTimeResourceId = R.string.boom_battle_bar_opening_times,
            imageResourceId = R.drawable.boom_battle_bar,
            placeDetails = R.string.boom_battle_bar_place_details
        ),
        Place(
            id = 2, titleResourceId = R.string.yard_place_name,
            openingTimeResourceId = R.string.yard_opening_times,
            imageResourceId = R.drawable.yard,
            placeDetails = R.string.yard_place_details
        ),
        Place(
            id = 3, titleResourceId = R.string.samoan_joes_place_name,
            openingTimeResourceId = R.string.samoan_joes_times,
            imageResourceId = R.drawable.samoan_joes,
            placeDetails = R.string.samoan_joes_details
        )
    )

    private val restaurants = listOf(
        Place(
            id = 1, titleResourceId = R.string.hickory_place_name,
            openingTimeResourceId = R.string.hickory_times,
            imageResourceId = R.drawable.hickory,
            placeDetails = R.string.hickory_details
        ),
        Place(
            id = 2, titleResourceId = R.string.cogs_place_name,
            openingTimeResourceId = R.string.cogs_times,
            imageResourceId = R.drawable.cogs,
            placeDetails = R.string.cogs_details
        ),
        Place(
            id = 3, titleResourceId = R.string.farmhouse_place_name,
            openingTimeResourceId = R.string.farmhouse_times,
            imageResourceId = R.drawable.farmhouse,
            placeDetails = R.string.farmhouse_details
        )
    )

    private val museums = listOf(
        Place(
            id = 1, titleResourceId = R.string.blitz_museum_place_name,
            openingTimeResourceId = R.string.blitz_museum_times,
            imageResourceId = R.drawable.blitz,
            placeDetails = R.string.blitz_museum_details
        ),
        Place(
            id = 2, titleResourceId = R.string.transport_museum_place_name,
            openingTimeResourceId = R.string.transport_museum_times,
            imageResourceId = R.drawable.transport,
            placeDetails = R.string.transport_museum_details
        ),
        Place(
            id = 3, titleResourceId = R.string.herbert_name,
            openingTimeResourceId = R.string.herbert_times,
            imageResourceId = R.drawable.herbert,
            placeDetails = R.string.herbert_details
        )
    )

    private val sportsTeams = listOf(
        Place(
            id = 1, titleResourceId = R.string.cov_fc_name,
            openingTimeResourceId = null,
            imageResourceId = R.drawable.football_removebg_preview,
            placeDetails = R.string.cov_fc_details
        ),
        Place(
            id = 2, titleResourceId = R.string.cov_rubgy_name,
            openingTimeResourceId = null,
            imageResourceId = R.drawable.rugby,
            placeDetails = R.string.cov_rubgy_details
        ),
        Place(
            id = 3, titleResourceId = R.string.blaze_name,
            openingTimeResourceId = null,
            imageResourceId = R.drawable.blaze,
            placeDetails = R.string.blaze_details
        )
    )

    // All available categories, used as content of lazy column
    // might store list of places for each category within the category object
    val categories =  listOf(
        Category(id = 1, titleResourceId = R.string.coffee_shops, imageResourceId = R.drawable.coffee_cropped, listOfPlaces = coffeeShops),
        Category(id = 2, titleResourceId = R.string.parks, imageResourceId = R.drawable.park_cropped, listOfPlaces = parks),
        Category(id = 3, titleResourceId = R.string.bars, imageResourceId = R.drawable.bar_cropped, listOfPlaces = bars),
        Category(id = 4, titleResourceId = R.string.restaurants, imageResourceId = R.drawable.restaurant_cropped, listOfPlaces = restaurants),
        Category(id = 5, titleResourceId = R.string.museums, imageResourceId = R.drawable.museum_cropped, listOfPlaces = museums),
        Category(id = 6, titleResourceId = R.string.sports, imageResourceId = R.drawable.sports_cropped, listOfPlaces = sportsTeams),
    )


}