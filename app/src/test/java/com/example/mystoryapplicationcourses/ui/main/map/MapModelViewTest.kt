package com.example.mystoryapplicationcourses.ui.main.map

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import com.example.mystoryapplicationcourses.data.utils.respositor.Result
import com.example.mystoryapplicationcourses.data.utils.StoriesResponse
import com.example.mystoryapplicationcourses.data.utils.StoryRepository
import com.example.mystoryapplicationcourses.settings.DataDummy
import com.example.mystoryapplicationcourses.ui.story.map.MapViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.TestCoroutineDispatcher
import kotlinx.coroutines.test.runBlockingTest
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito.`when`
import org.mockito.Mockito.mock
import org.mockito.Mockito.verify

@ExperimentalCoroutinesApi
class ActivityMapViewModelTesting {
    private val testDispatcher = TestCoroutineDispatcher()

    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    @Mock
    private lateinit var storyRepository: StoryRepository

    private lateinit var mapViewModel: MapViewModel
    private val dummy = DataDummy()
    private val dummyStoriesResponse = dummy.generateDummyStories()

    @Before
    fun setUp() {
        storyRepository = mock(StoryRepository::class.java)
        mapViewModel = MapViewModel(storyRepository)
    }

    @Test
    fun `when getStoriesWithLocation Should Not Null and return success`() = testDispatcher.runBlockingTest {
        val expectedStoryResponse = Result.Success(dummyStoriesResponse)
        val liveData = MutableLiveData<Result<StoriesResponse>>()
        liveData.value = expectedStoryResponse
        `when`(storyRepository.getStoriesWithLocation()).thenReturn(liveData)

        val observer = mock(Observer::class.java) as Observer<Result<StoriesResponse>>
        mapViewModel.getStoriesWithLocation().observeForever(observer)

        verify(observer).onChanged(expectedStoryResponse)

        assertEquals(dummyStoriesResponse.listStory.size, (expectedStoryResponse.data as StoriesResponse).listStory.size)
    }

    @Test
    fun `when Network Error Should Return Error`() = testDispatcher.runBlockingTest {
        val expectedStoryResponse = Result.Error("network error")
        val liveData = MutableLiveData<Result<StoriesResponse>>()
        liveData.value = expectedStoryResponse
        `when`(storyRepository.getStoriesWithLocation()).thenReturn(liveData)

        val observer = mock(Observer::class.java) as Observer<Result<StoriesResponse>>
        mapViewModel.getStoriesWithLocation().observeForever(observer)

        verify(observer).onChanged(expectedStoryResponse)

        assertTrue(expectedStoryResponse is Result.Error)
    }

    @After
    fun tearDown() {
        testDispatcher.cleanupTestCoroutines()
    }
}
