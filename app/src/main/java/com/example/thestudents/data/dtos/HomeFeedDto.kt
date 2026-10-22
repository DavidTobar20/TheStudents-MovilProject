package com.example.thestudents.data.dtos

import com.example.thestudents.data.HomeFeed

abstract class HomeFeedGenericDto {
    abstract fun toHomeFeed(): HomeFeed
}

data class HomeFeedDto(
    val todasResenas: List<ReviewDto> = emptyList(),
    val resenasSeguidos: List<ReviewDto> = emptyList()
) : HomeFeedGenericDto() {
    override fun toHomeFeed(): HomeFeed {
        return HomeFeed(
            allReviews = todasResenas.map { it.toReview() },
            followedReviews = resenasSeguidos.map { it.toReview() }
        )
    }
}
