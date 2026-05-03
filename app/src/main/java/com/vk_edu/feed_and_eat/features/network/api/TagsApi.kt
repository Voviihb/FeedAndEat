package com.vk_edu.feed_and_eat.features.network.api

import com.vk_edu.feed_and_eat.features.network.dto.TagDto
import retrofit2.http.GET

interface TagsApi {
    
    @GET("tags/")
    suspend fun getTags(): List<TagDto>
}