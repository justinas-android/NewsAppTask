package com.example.newsapp.ui.news.details

import com.example.newsapp.runFlowTest
import com.example.newsapp.utils.analytics.AnalyticsTracker
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.delay
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain
import org.assertj.core.api.Assertions.assertThat
import org.junit.After
import org.junit.Before
import org.junit.Test

class NewsDetailsViewModelTest {
    private lateinit var analyticsTracker: AnalyticsTracker

    private val testDispatcher = StandardTestDispatcher()

    @OptIn(ExperimentalCoroutinesApi::class)
    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)
        analyticsTracker = mockk()
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    private val vm: NewsDetailsViewModel by lazy {
        NewsDetailsViewModel(
            analyticsTracker = analyticsTracker
        )
    }

    @Test
    fun `onLoad sets selected article values`() = runFlowTest {
        vm.onLoad(
            author = "fakeAuthor",
            title = "fakeTitle",
            description = "fakeDescription",
            url = "fakeUrl",
            urlToImage = "fakeUrlToImage",
            publishedAt = "fakePublishedAt"
        )

        val stateTest = vm.uiState.test()
        delay(1000L)

        val lastState = stateTest.values.last()

        assertThat(lastState.author).isEqualTo("fakeAuthor")
        assertThat(lastState.title).isEqualTo("fakeTitle")
        assertThat(lastState.description).isEqualTo("fakeDescription")
        assertThat(lastState.url).isEqualTo("fakeUrl")
        assertThat(lastState.urlToImage).isEqualTo("fakeUrlToImage")
        assertThat(lastState.publishedAt).isEqualTo("fakePublishedAt")
    }

    @Test
    fun `onReadFullArticleClicked logs analytics event and emits ShowFullArticle action`() = runFlowTest {
        val fakeUrl = "fakeUrl"
        val fakeTitle = "fakeTitle"
        every { analyticsTracker.logEvent(any(), any(), any()) } returns Unit

        vm.onLoad(
            author = "fakeAuthor",
            title = fakeTitle,
            description = "fakeDescription",
            url = "fakeUrl",
            urlToImage = "fakeUrlToImage",
            publishedAt = "fakePublishedAt"
        )
        delay(1000L)
        vm.onReadFullArticleClicked(fakeUrl)

        val actionTest = vm.actions.test()
        delay(1000L)

        verify(exactly = 1) {
            analyticsTracker.logEvent(
                category = "article_details",
                action = "click_read_more",
                label = fakeTitle
            )
        }
        assertThat(actionTest.values.last()).isEqualTo(
            NewsDetailsViewAction.ShowFullArticle(fakeUrl)
        )
    }
}
