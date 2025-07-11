package com.example.newsapp.ui.news.list

import com.example.newsapp.runFlowTest
import com.example.newsapp.ui.news.list.domain.GetTopHeadlinesInteractor
import com.example.newsapp.ui.news.models.Article
import com.example.newsapp.ui.news.models.News
import com.example.newsapp.utils.analytics.AnalyticsTracker
import com.example.newsapp.utils.toFlowBaseResultError
import com.example.newsapp.utils.toFlowBaseResultSuccess
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

class NewsMainListViewModelTest {

    private lateinit var getTopHeadlinesInteractor: GetTopHeadlinesInteractor

    private lateinit var analyticsTracker: AnalyticsTracker

    private val testDispatcher = StandardTestDispatcher()

    @OptIn(ExperimentalCoroutinesApi::class)
    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)
        getTopHeadlinesInteractor = mockk()
        analyticsTracker = mockk()
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    private val vm: NewsMainListViewModel by lazy {
        NewsMainListViewModel(
            getTopHeadlinesInteractor = getTopHeadlinesInteractor,
            analyticsTracker = analyticsTracker
        )
    }

    @Test
    fun `on init fetches top headlines data with failure`() = runFlowTest {
        every { getTopHeadlinesInteractor(any(), any()) } returns "Error".toFlowBaseResultError()

        vm

        val actionTest = vm.actions.test()
        delay(1000L)

        assertThat(actionTest.values.last()).isInstanceOfSatisfying(NewsMainListViewAction.ShowError::class.java) {
            assertThat(it.message).isEqualTo("Error")
        }
    }

    @Test
    fun `on init fetches top headlines data with success`() = runFlowTest {
        val fakeArticle = Article(
            author = "fakeAuthor",
            title = "fakeTitle",
            description = "fakeDescription",
            url = "fakeUrl",
            urlToImage = "fakeUrlToImage",
            publishedAt = "fakeDate"
        )
        val fakeData = News(status = "-1", articles = listOf(fakeArticle))
        every { getTopHeadlinesInteractor(any(), any()) } returns
            fakeData.toFlowBaseResultSuccess()

        vm

        val stateTest = vm.uiState.test()
        delay(1000L)

        val lastState = stateTest.values.last()

        assertThat(lastState.articles).hasSize(1)
        assertThat(lastState.articles.first().author).isEqualTo(fakeArticle.author)
        assertThat(lastState.articles.first().title).isEqualTo(fakeArticle.title)
        assertThat(lastState.articles.first().description).isEqualTo(fakeArticle.description)
        assertThat(lastState.articles.first().url).isEqualTo(fakeArticle.url)
        assertThat(lastState.articles.first().urlToImage).isEqualTo(fakeArticle.urlToImage)
        assertThat(lastState.articles.first().publishedAt).isEqualTo(fakeArticle.publishedAt)
    }

    @Test
    fun `on init sets isLoading to true then false after success`() = runFlowTest {
        val fakeArticle = Article(
            author = "fakeAuthor",
            title = "fakeTitle",
            description = "fakeDescription",
            url = "fakeUrl",
            urlToImage = "fakeUrlToImage",
            publishedAt = "fakeDate"
        )
        val fakeData = News(status = "-1", articles = listOf(fakeArticle))

        every { getTopHeadlinesInteractor(any(), any()) } returns fakeData.toFlowBaseResultSuccess()

        vm

        val stateTest = vm.uiState.test()
        delay(1000L)

        val loadingStates = stateTest.values.map { it.isLoading }

        assertThat(loadingStates.first()).isTrue()
        assertThat(loadingStates.last()).isFalse()
    }

    @Test
    fun `onRefresh sets isRefreshing to true then false after success`() = runFlowTest {
        val fakeArticle = Article(
            author = "fakeAuthor",
            title = "fakeTitle",
            description = "fakeDescription",
            url = "fakeUrl",
            urlToImage = "fakeUrlToImage",
            publishedAt = "fakeDate"
        )
        val fakeData = News(status = "-1", articles = listOf(fakeArticle))

        every { getTopHeadlinesInteractor(any(), any()) } returns fakeData.toFlowBaseResultSuccess()

        vm.onRefresh()

        val uiStateTest = vm.uiState.test()
        delay(1000L)

        assertThat(uiStateTest.values.any { it.isRefreshing }).isTrue()
        assertThat(uiStateTest.values.last().isRefreshing).isFalse()
        assertThat(uiStateTest.values.last().articles).containsExactly(fakeArticle)
        verify(exactly = 2) { getTopHeadlinesInteractor(any(), any()) }
    }

    @Test
    fun `onArticleClicked logs analytics event and emits ShowArticleDetails action`() = runFlowTest {
        val fakeArticle = Article(
            author = "fakeAuthor",
            title = "fakeTitle",
            description = "fakeDescription",
            url = "fakeUrl",
            urlToImage = "fakeUrlToImage",
            publishedAt = "fakeDate"
        )
        every { getTopHeadlinesInteractor(any(), any()) } returns "Error".toFlowBaseResultError()
        every { analyticsTracker.logEvent(any(), any(), any()) } returns Unit

        vm.onArticleClicked(fakeArticle)

        val actionTest = vm.actions.test()
        delay(1000L)

        verify(exactly = 1) {
            analyticsTracker.logEvent(
                category = "articles",
                action = "click",
                label = fakeArticle.title
            )
        }

        assertThat(actionTest.values.last()).isEqualTo(
            NewsMainListViewAction.ShowArticleDetails(fakeArticle)
        )
    }
}
