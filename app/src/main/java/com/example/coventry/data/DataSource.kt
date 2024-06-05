package com.example.coventry.data

import com.example.coventry.R

object DataSource {

    private val coffeeShops = listOf(
        Place(
            id = 1, titleResourceId = R.string.default_place_name,
            openingTimeResourceId = R.string.default_place_name,
            imageResourceId = R.drawable.sovrano_front,
            placeDetails = R.string.default_subtitle
        ),
        Place(
            id = 2, titleResourceId = R.string.default_place_name,
            openingTimeResourceId = R.string.default_place_name,
            imageResourceId = R.drawable.sovrano_front,
            placeDetails = R.string.default_subtitle
        ),
        Place(
            id = 3, titleResourceId = R.string.default_place_name,
            openingTimeResourceId = R.string.default_place_name,
            imageResourceId = R.drawable.sovrano_front,
            placeDetails = R.string.default_subtitle
        )

    )
    private val parks = listOf(
        Place(
            id = 1, titleResourceId = R.string.default_place_name,
            openingTimeResourceId = R.string.default_place_name,
            imageResourceId = R.drawable.sovrano_front,
            placeDetails = R.string.default_subtitle
        ),
        Place(
            id = 2, titleResourceId = R.string.default_place_name,
            openingTimeResourceId = R.string.default_place_name,
            imageResourceId = R.drawable.sovrano_front,
            placeDetails = R.string.default_subtitle
        ),
        Place(
            id = 3, titleResourceId = R.string.default_place_name,
            openingTimeResourceId = R.string.default_place_name,
            imageResourceId = R.drawable.sovrano_front,
            placeDetails = R.string.default_subtitle
        )
    )
    private val bars = listOf(
        Place(
            id = 1, titleResourceId = R.string.default_place_name,
            openingTimeResourceId = R.string.default_place_name,
            imageResourceId = R.drawable.sovrano_front,
            placeDetails = R.string.default_subtitle
        ),
        Place(
            id = 2, titleResourceId = R.string.default_place_name,
            openingTimeResourceId = R.string.default_place_name,
            imageResourceId = R.drawable.sovrano_front,
            placeDetails = R.string.default_subtitle
        ),
        Place(
            id = 3, titleResourceId = R.string.default_place_name,
            openingTimeResourceId = R.string.default_place_name,
            imageResourceId = R.drawable.sovrano_front,
            placeDetails = R.string.default_subtitle
        )
    )
    private val restaurants = listOf(
        Place(
            id = 1, titleResourceId = R.string.default_place_name,
            openingTimeResourceId = R.string.default_place_name,
            imageResourceId = R.drawable.sovrano_front,
            placeDetails = R.string.default_subtitle
        ),
        Place(
            id = 2, titleResourceId = R.string.default_place_name,
            openingTimeResourceId = R.string.default_place_name,
            imageResourceId = R.drawable.sovrano_front,
            placeDetails = R.string.default_subtitle
        ),
        Place(
            id = 3, titleResourceId = R.string.default_place_name,
            openingTimeResourceId = R.string.default_place_name,
            imageResourceId = R.drawable.sovrano_front,
            placeDetails = R.string.default_subtitle
        )
    )
    private val museums = listOf(
        Place(
            id = 1, titleResourceId = R.string.default_place_name,
            openingTimeResourceId = R.string.default_place_name,
            imageResourceId = R.drawable.sovrano_front,
            placeDetails = R.string.default_subtitle
        ),
        Place(
            id = 2, titleResourceId = R.string.default_place_name,
            openingTimeResourceId = R.string.default_place_name,
            imageResourceId = R.drawable.sovrano_front,
            placeDetails = R.string.default_subtitle
        ),
        Place(
            id = 3, titleResourceId = R.string.default_place_name,
            openingTimeResourceId = R.string.default_place_name,
            imageResourceId = R.drawable.sovrano_front,
            placeDetails = R.string.default_subtitle
        )
    )
    private val sportsTeams = listOf(
        Place(
            id = 1, titleResourceId = R.string.default_place_name,
            openingTimeResourceId = R.string.default_place_name,
            imageResourceId = R.drawable.sovrano_front,
            placeDetails = R.string.default_subtitle
        ),
        Place(
            id = 2, titleResourceId = R.string.default_place_name,
            openingTimeResourceId = R.string.default_place_name,
            imageResourceId = R.drawable.sovrano_front,
            placeDetails = R.string.default_subtitle
        ),
        Place(
            id = 3, titleResourceId = R.string.default_place_name,
            openingTimeResourceId = R.string.default_place_name,
            imageResourceId = R.drawable.sovrano_front,
            placeDetails = R.string.default_subtitle
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