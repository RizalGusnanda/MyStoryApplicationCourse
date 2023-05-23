package com.example.mystoryapplicationcourses.settings

import com.example.mystoryapplicationcourses.data.utils.*
import java.util.*
import kotlin.collections.ArrayList

class DataDummy {
    fun generateDummyStories(): StoriesResponse {
        val listStory = ArrayList<Story>()
        val currentTime = "2023-05-18T15:00:00.000Z"
        for (i in 1..20) {
            val story = Story(
                createdAt = currentTime,
                description = "Description ${UUID.randomUUID()}",
                id = "id_${UUID.randomUUID()}",
                lati = Random().nextDouble() * 100,
                long = Random().nextDouble() * 100,
                name = "Name ${UUID.randomUUID()}",
                photoUrl = "https://picsum.photos/200?random=$i"
            )
            listStory.add(story)
        }

        return StoriesResponse(
            error = false,
            message = "Stories fetched successfully",
            listStory = listStory
        )
    }

    fun generateDummyCreateStory(): PostStoryResponse {
        return PostStoryResponse(
            error = false,
            message = "success"
        )
    }

    fun generateDummyRegister(): RegisterResponse {
        return RegisterResponse(
            error = false,
            message = "User Created"
        )
    }

    fun generateDummyLogin(): LoginResponse {
        return LoginResponse(
            error = false,
            message = "success",
            loginResult = LoginResult(
                userId = "user-${UUID.randomUUID()}",
                name = "atmayanti",
                token = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJ1c2VySWQiOiJ1c2VyLXlqNXBjX0xBUkNfQWdLNjEiLCJpYXQiOjE2NDE3OTk5NDl9.flEMaQ7zsdYkxuyGbiXjEDXO8kuDTcI__3UjCwt6R_IeyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJ1c2VySWQiOiJ1c2VyLXFYcGJpWldxU2lGWGJFTkkiLCJpYXQiOjE2ODQxNzU0NTN9.6WUbex1b8iNR5hvKBM200dLDN4fP308zuVogLlyKKU8"
            )
        )
    }
}