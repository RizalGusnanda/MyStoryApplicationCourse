package com.example.mystoryapplicationcourses.ui.main.create

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.MutableLiveData
import com.example.mystoryapplicationcourses.data.utils.PostStoryResponse
import com.example.mystoryapplicationcourses.data.utils.StoryRepository
import com.example.mystoryapplicationcourses.settings.DataDummy
import com.example.mystoryapplicationcourses.settings.getOrAwaitValue
import com.example.mystoryapplicationcourses.ui.story.create.CreateStoryViewModel
import junit.framework.Assert.*
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import org.junit.Assert
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.junit.MockitoJUnitRunner
import java.io.File
import com.example.mystoryapplicationcourses.data.utils.respositor.Result
@RunWith(MockitoJUnitRunner::class)
class CreateViewModelTest {
    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    @Mock
    private lateinit var storyRepository: StoryRepository

    private lateinit var createStoryViewModel: CreateStoryViewModel
    private val dummy = DataDummy()
    private val dummyResponse = dummy.generateDummyCreateStory()

    @Before
    fun setUp() {
        createStoryViewModel = CreateStoryViewModel(storyRepository)
    }

    private fun setupPostScenario(expectedResponse: Result<PostStoryResponse>): Triple<MultipartBody.Part, okhttp3.RequestBody, MutableLiveData<Result<PostStoryResponse>>> {
        val descriptionText = "Description text"
        val description = descriptionText.toRequestBody("text/plain".toMediaType())

        val file = Mockito.mock(File::class.java)
        val requestImageFile = file.asRequestBody("image/jpg".toMediaTypeOrNull())
        val imageMultipart: MultipartBody.Part = MultipartBody.Part.createFormData(
            "photo name",
            "photo.jpg",
            requestImageFile
        )

        val expectedPostResponse = MutableLiveData<Result<PostStoryResponse>>()
        expectedPostResponse.value = expectedResponse

        Mockito.`when`(storyRepository.postStory(imageMultipart, description)).thenReturn(expectedPostResponse)

        return Triple(imageMultipart, description, expectedPostResponse)
    }

    @Test
    fun `when Upload Story Should Not Null and return success`() {
        val (imageMultipart, description, _) = setupPostScenario(Result.Success(dummyResponse))

        val actualResponse = createStoryViewModel.postStory(imageMultipart, description).getOrAwaitValue()
        Mockito.verify(storyRepository).postStory(imageMultipart, description)
        Assert.assertNotNull(actualResponse)
        Assert.assertTrue(actualResponse is Result.Success)
        Assert.assertEquals(dummyResponse.error, (actualResponse as Result.Success).data.error)
    }

    @Test
    fun `when Network Error Should Return Error`() {
        val (imageMultipart, description, _) = setupPostScenario(Result.Error("network error"))

        val actualResponse = createStoryViewModel.postStory(imageMultipart, description).getOrAwaitValue()
        Mockito.verify(storyRepository).postStory(imageMultipart, description)
        Assert.assertNotNull(actualResponse)
        Assert.assertTrue(actualResponse is Result.Error)
    }
}