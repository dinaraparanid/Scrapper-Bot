package com.paranid5.bot

import com.paranid5.bot.data.link.repository.LinkRepositoryInMemory
import com.paranid5.bot.data.link.sources.github.GitHubDataSourceMock
import com.paranid5.bot.data.link.sources.stack_overflow.StackOverflowDataSourceMock
import com.paranid5.bot.domain.utils.extend
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.TestScope
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest

typealias LinkMap = Map<Long, List<String>>

private const val vasyanId = 0L
private const val tolikId = 1L
private const val danilaId = 2L

private const val ghLink1 = "https://github.com/dinaraparanid/Crescendo"
private const val ghLink2 = "https://github.com/dinaraparanid"
private const val ghLink3 = "https://github.com/dinaraparanid/Scrapper-Bot"

private const val soLink1 = "https://stackoverflow.com/questions/39866676/retrofit-uploading-multiple-images-to-a-single-key"
private const val soLink2 = "https://stackoverflow.com/questions/32580257/tablayout-set-spacing-or-margin-each-tab"
private const val soLink3 = "https://stackoverflow.com/questions/40202294/set-selected-item-in-android-bottomnavigationview"

@SpringBootTest
class LinkRepTest {
    @Autowired
    private lateinit var githubSrc: GitHubDataSourceMock

    @Autowired
    private lateinit var stackSrc: StackOverflowDataSourceMock

    @Autowired
    private lateinit var linkResponseChannel: LinkResponseChannelMock

    private val linkRep by lazy {
        LinkRepositoryInMemory(githubSrc, stackSrc)
    }

    private val githubTest = mutableListOf<LinkMap>()
    private val stackTest = mutableListOf<LinkMap>()
    private val repTest = mutableListOf<LinkMap>()

    private var ghMap = emptyMap<Long, List<String>>()
    private var soMap = emptyMap<Long, List<String>>()
    private var repMap = emptyMap<Long, List<String>>()

    context(TestScope)
    @OptIn(ExperimentalCoroutinesApi::class)
    private fun launchCollectors() {
        backgroundScope.launch(UnconfinedTestDispatcher(testScheduler)) {
            githubSrc.usersTrackingsFlow.toList(githubTest)
        }

        backgroundScope.launch(UnconfinedTestDispatcher(testScheduler)) {
            stackSrc.usersTrackingsFlow.toList(stackTest)
        }

        backgroundScope.launch(UnconfinedTestDispatcher(testScheduler)) {
            linkRep.usersTrackingsFlow.toList(repTest)
        }

        backgroundScope.launch(UnconfinedTestDispatcher(testScheduler)) {
            githubSrc.launchLinksStoreMonitoring(linkResponseChannel)
        }

        backgroundScope.launch(UnconfinedTestDispatcher(testScheduler)) {
            githubSrc.launchLinksRemoveMonitoring(linkResponseChannel)
        }

        backgroundScope.launch(UnconfinedTestDispatcher(testScheduler)) {
            stackSrc.launchLinksStoreMonitoring(linkResponseChannel)
        }

        backgroundScope.launch(UnconfinedTestDispatcher(testScheduler)) {
            stackSrc.launchLinksRemoveMonitoring(linkResponseChannel)
        }
    }

    private fun assertVals() {
        assert(ghMap matches githubTest.last())
        assert(soMap matches stackTest.last())
        assert(repMap matches repTest.last())
    }

    @Test
    fun linkRepTest() = runTest {
        launchCollectors()

        // ------------- Track -------------

        githubSrc.trackLink(vasyanId, ghLink1)
        ghMap = mapOf(vasyanId to listOf(ghLink1))
        repMap = ghMap
        assertVals()

        githubSrc.trackLink(tolikId, ghLink2)
        ghMap = mapOf(vasyanId to listOf(ghLink1), tolikId to listOf(ghLink2))
        repMap = ghMap
        assertVals()

        githubSrc.trackLink(danilaId, ghLink1)
        githubSrc.trackLink(danilaId, ghLink2)
        githubSrc.trackLink(danilaId, ghLink3)
        ghMap = mapOf(
            vasyanId to listOf(ghLink1),
            tolikId to listOf(ghLink2),
            danilaId to listOf(ghLink1, ghLink2, ghLink3)
        )
        repMap = ghMap
        assertVals()

        stackSrc.trackLink(vasyanId, soLink1)
        soMap = mapOf(vasyanId to listOf(soLink1))
        repMap = ghMap extend soMap
        assertVals()

        stackSrc.trackLink(tolikId, soLink2)
        soMap = mapOf(vasyanId to listOf(soLink1), tolikId to listOf(soLink2))
        repMap = ghMap extend soMap
        assertVals()

        stackSrc.trackLink(danilaId, soLink1)
        stackSrc.trackLink(danilaId, soLink2)
        stackSrc.trackLink(danilaId, soLink3)
        soMap = mapOf(
            vasyanId to listOf(soLink1),
            tolikId to listOf(soLink2),
            danilaId to listOf(soLink1, soLink2, soLink3)
        )
        repMap = ghMap extend soMap
        assertVals()

        // ------------- Untrack -------------

        githubSrc.untrackLink(vasyanId, ghLink1)

        ghMap = mapOf(
            vasyanId to emptyList(),
            tolikId to listOf(ghLink2),
            danilaId to listOf(ghLink1, ghLink2, ghLink3)
        )
        repMap = ghMap extend soMap
        assertVals()

        githubSrc.untrackLink(tolikId, ghLink2)
        ghMap = mapOf(
            vasyanId to emptyList(),
            tolikId to emptyList(),
            danilaId to listOf(ghLink1, ghLink2, ghLink3)
        )
        repMap = ghMap extend soMap
        assertVals()

        githubSrc.untrackLink(danilaId, ghLink1)
        githubSrc.untrackLink(danilaId, ghLink2)

        ghMap = mapOf(
            vasyanId to emptyList(),
            tolikId to emptyList(),
            danilaId to listOf(ghLink3)
        )
        repMap = ghMap extend soMap
        assertVals()

        stackSrc.untrackLink(vasyanId, soLink1)
        soMap = mapOf(
            vasyanId to emptyList(),
            tolikId to listOf(soLink2),
            danilaId to listOf(soLink1, soLink2, soLink3)
        )
        repMap = ghMap extend soMap
        assertVals()

        stackSrc.untrackLink(tolikId, soLink2)
        soMap = mapOf(
            vasyanId to emptyList(),
            tolikId to emptyList(),
            danilaId to listOf(soLink1, soLink2, soLink3)
        )
        repMap = ghMap extend soMap
        assertVals()

        stackSrc.untrackLink(danilaId, soLink1)
        stackSrc.untrackLink(danilaId, soLink2)
        soMap = mapOf(
            vasyanId to emptyList(),
            tolikId to emptyList(),
            danilaId to listOf(soLink3)
        )
        repMap = ghMap extend soMap
        assertVals()
    }
}