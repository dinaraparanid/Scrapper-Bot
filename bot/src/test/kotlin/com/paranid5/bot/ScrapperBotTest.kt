package com.paranid5.bot

import com.paranid5.bot.user_state_patch.*
import com.paranid5.core.entities.link.LinkResponse
import com.paranid5.core.entities.user.User
import com.paranid5.core.entities.user.UserState
import com.paranid5.data.link.repository.LinkRepository
import com.paranid5.data.link.sources.github.GitHubDataSourceInMemory
import com.paranid5.data.link.sources.stack_overflow.StackOverflowDataSourceInMemory
import com.paranid5.data.user.UserDataSourceMock
import com.paranid5.data.user.user_state_patch.*
import com.paranid5.utils.matches
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.test.TestScope
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest

private val vasyan = User(id = 0, chatId = 0, firstName = "Васян", secondName = "Петросян")
private val tolik = User(id = 1, chatId = 1, firstName = "Толик", secondName = "Алкоголик")
private val danila = User(id = 2, chatId = 2, firstName = "Данила", secondName = "Из Нижнего Тагила")

private const val ghLink1 = "https://github.com/dinaraparanid/Crescendo"
private const val ghLink2 = "https://github.com/dinaraparanid"
private const val ghLink3 = "https://github.com/dinaraparanid/Scrapper-Bot"

private const val soLink1 = "https://stackoverflow.com/questions/39866676/retrofit-uploading-multiple-images-to-a-single-key"
private const val soLink2 = "https://stackoverflow.com/questions/32580257/tablayout-set-spacing-or-margin-each-tab"
private const val soLink3 = "https://stackoverflow.com/questions/40202294/set-selected-item-in-android-bottomnavigationview"

@SpringBootTest
class ScrapperBotTest {
    @Autowired
    private lateinit var userSrc: UserDataSourceMock

    private val responseTest = mutableListOf<LinkResponse>()
    private val statesTest = mutableListOf<Map<Long, UserState>>()

    context(TestScope)
    @OptIn(ExperimentalCoroutinesApi::class)
    private fun launchCollectors(bot: ScrapperBotMock, linkRep: LinkRepository) {
        linkRep.launchSourcesLinksMonitoring(LinkResponseChannelMock(bot))

        backgroundScope.launch(UnconfinedTestDispatcher(testScheduler)) {
            bot.linkResponseFlow.toList(responseTest)
        }

        backgroundScope.launch(UnconfinedTestDispatcher(testScheduler)) {
            userSrc.userStatesFlow.toList(statesTest)
        }
    }

    private fun assertVals(
        requiredResponse: LinkResponse?,
        requiredStates: Map<Long, UserState>
    ) {
        requiredResponse?.let { assert(it matches responseTest.last()) }
        assert(requiredStates matches statesTest.last())
    }

    @Test
    fun scrapperTest() = runTest {
        val linkRep = LinkRepositoryMock(
            GitHubDataSourceInMemory(),
            StackOverflowDataSourceInMemory(),
            this
        )

        val interactor = BotInteractorMock(
            linkRep,
            HelpStatePatchMock(userSrc),
            ListStatePatchMock(userSrc),
            StartStatePatchMock(userSrc),
            TrackStatePatchMock(userSrc),
            UntrackStatePatchMock(userSrc),
            TrackLinkStatePatchMock(userSrc),
            UntrackLinkStatePatchMock(userSrc),
            this
        )

        val bot = ScrapperBotMock(userSrc, linkRep, interactor, this).apply {
            launchBot()
            launchCollectors(this, linkRep)
        }

        bot.sendStartMessage(vasyan)

        assertVals(
            requiredResponse = null,
            requiredStates = mapOf(vasyan.id to UserState.StartSentState(vasyan))
        )

        bot.sendHelpMessage(tolik)

        assertVals(
            requiredResponse = null,
            requiredStates = mapOf(
                vasyan.id to UserState.StartSentState(vasyan),
                tolik.id to UserState.HelpSentState(tolik)
            )
        )

        bot.sendListMessage(danila)

        assertVals(
            requiredResponse = null,
            requiredStates = mapOf(
                vasyan.id to UserState.StartSentState(vasyan),
                tolik.id to UserState.HelpSentState(tolik),
                danila.id to UserState.LinkListSentState(danila)
            )
        )

        bot.sendTrackMessage(vasyan)

        assertVals(
            requiredResponse = null,
            requiredStates = mapOf(
                vasyan.id to UserState.TrackSentState(vasyan),
                tolik.id to UserState.HelpSentState(tolik),
                danila.id to UserState.LinkListSentState(danila)
            )
        )

        bot.sendTextMessage(vasyan, "bebra")

        assertVals(
            requiredResponse = null,
            requiredStates = mapOf(
                vasyan.id to UserState.TrackSentState(vasyan),
                tolik.id to UserState.HelpSentState(tolik),
                danila.id to UserState.LinkListSentState(danila)
            )
        )

        bot.sendTextMessage(vasyan, ghLink1)
        withContext(Dispatchers.IO) { delay(300) }

        assertVals(
            requiredResponse = LinkResponse.TrackResponse.New(vasyan.id, ghLink1),
            requiredStates = mapOf(
                vasyan.id to UserState.TrackLinkSentState(vasyan),
                tolik.id to UserState.HelpSentState(tolik),
                danila.id to UserState.LinkListSentState(danila)
            )
        )

        bot.sendTrackMessage(tolik)

        assertVals(
            requiredResponse = null,
            requiredStates = mapOf(
                vasyan.id to UserState.TrackLinkSentState(vasyan),
                tolik.id to UserState.TrackSentState(tolik),
                danila.id to UserState.LinkListSentState(danila)
            )
        )

        bot.sendTextMessage(tolik, soLink1)
        withContext(Dispatchers.IO) { delay(300) }

        assertVals(
            requiredResponse = LinkResponse.TrackResponse.New(tolik.id, soLink1),
            requiredStates = mapOf(
                vasyan.id to UserState.TrackLinkSentState(vasyan),
                tolik.id to UserState.TrackLinkSentState(tolik),
                danila.id to UserState.LinkListSentState(danila)
            )
        )

        bot.sendTrackMessage(tolik)

        assertVals(
            requiredResponse = null,
            requiredStates = mapOf(
                vasyan.id to UserState.TrackLinkSentState(vasyan),
                tolik.id to UserState.TrackSentState(tolik),
                danila.id to UserState.LinkListSentState(danila)
            )
        )

        bot.sendTextMessage(tolik, soLink1)
        withContext(Dispatchers.IO) { delay(300) }

        assertVals(
            requiredResponse = LinkResponse.TrackResponse.Present(tolik.id, soLink1),
            requiredStates = mapOf(
                vasyan.id to UserState.TrackLinkSentState(vasyan),
                tolik.id to UserState.TrackLinkSentState(tolik),
                danila.id to UserState.LinkListSentState(danila)
            )
        )

        bot.sendTrackMessage(danila)

        assertVals(
            requiredResponse = null,
            requiredStates = mapOf(
                vasyan.id to UserState.TrackLinkSentState(vasyan),
                tolik.id to UserState.TrackLinkSentState(tolik),
                danila.id to UserState.TrackSentState(danila)
            )
        )

        bot.sendTextMessage(danila, ghLink3)
        withContext(Dispatchers.IO) { delay(300) }

        assertVals(
            requiredResponse = LinkResponse.TrackResponse.New(danila.id, ghLink3),
            requiredStates = mapOf(
                vasyan.id to UserState.TrackLinkSentState(vasyan),
                tolik.id to UserState.TrackLinkSentState(tolik),
                danila.id to UserState.TrackLinkSentState(danila)
            )
        )

        bot.sendUntrackMessage(danila)

        assertVals(
            requiredResponse = LinkResponse.TrackResponse.New(danila.id, ghLink3),
            requiredStates = mapOf(
                vasyan.id to UserState.TrackLinkSentState(vasyan),
                tolik.id to UserState.TrackLinkSentState(tolik),
                danila.id to UserState.UntrackSentState(danila)
            )
        )

        bot.sendTextMessage(danila, ghLink3)
        withContext(Dispatchers.IO) { delay(300) }

        assertVals(
            requiredResponse = LinkResponse.UntrackResponse.Present(danila.id, ghLink3),
            requiredStates = mapOf(
                vasyan.id to UserState.TrackLinkSentState(vasyan),
                tolik.id to UserState.TrackLinkSentState(tolik),
                danila.id to UserState.UntrackLinkSentState(danila)
            )
        )
    }
}