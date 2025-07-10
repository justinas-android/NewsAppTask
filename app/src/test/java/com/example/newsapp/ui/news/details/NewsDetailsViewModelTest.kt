package com.example.newsapp.ui.news.details

import com.example.newsapp.runFlowTest
import com.example.newsapp.ui.news.models.Article
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
        val fakeArticle = Article(
            author = "fakeAuthor",
            title = "fakeTitle",
            description = "fakeDescription",
            url = "fakeUrl",
            urlToImage = "fakeUrlToImage",
            publishedAt = "fakeDate"
        )

        vm.onLoad(fakeArticle)

        val stateTest = vm.uiState.test()
        delay(1000L)

        val lastState = stateTest.values.last()

        assertThat(lastState.author).isEqualTo(fakeArticle.author)
        assertThat(lastState.title).isEqualTo(fakeArticle.title)
        assertThat(lastState.description).isEqualTo(fakeArticle.description)
        assertThat(lastState.url).isEqualTo(fakeArticle.url)
        assertThat(lastState.urlToImage).isEqualTo(fakeArticle.urlToImage)
        assertThat(lastState.publishedAt).isEqualTo(fakeArticle.publishedAt)
    }

    @Test
    fun `onReadFullArticleClicked logs analytics event and emits ShowFullArticle action`() = runFlowTest {
        every { analyticsTracker.logEvent(any(), any(), any()) } returns Unit

        val fakeArticle = Article(
            author = "fakeAuthor",
            title = "fakeTitle",
            description = "fakeDescription",
            url = "fakeUrl",
            urlToImage = "fakeUrlToImage",
            publishedAt = "fakeDate"
        )

        vm.onLoad(fakeArticle)
        delay(1000L)
        vm.onReadFullArticleClicked(fakeArticle.url)

        val actionTest = vm.actions.test()
        delay(1000L)

        verify(exactly = 1) {
            analyticsTracker.logEvent(
                category = "article_details",
                action = "click_read_more",
                label = fakeArticle.title
            )
        }
        assertThat(actionTest.values.last()).isEqualTo(
            NewsDetailsViewAction.ShowFullArticle(fakeArticle.url)
        )
    }
}
