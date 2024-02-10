package com.paranid5.bot

import com.paranid5.bot.data.user.UserDataSourceInMemory
import com.paranid5.bot.domain.user.User
import com.paranid5.bot.domain.user.UserState
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Test
import org.springframework.boot.test.context.SpringBootTest

@SpringBootTest
class UserDataSourceTest {
    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun userSrcTest() = runTest {
        val usersTest = mutableListOf<Collection<User>>()
        val statesTest = mutableListOf<Map<Long, UserState>>()
        val userSrc = UserDataSourceInMemory()

        backgroundScope.launch(UnconfinedTestDispatcher(testScheduler)) {
            userSrc.usersFlow.toList(usersTest)
        }

        backgroundScope.launch(UnconfinedTestDispatcher(testScheduler)) {
            userSrc.userStatesFlow.toList(statesTest)
        }

        fun assertVals(requiredUsers: Collection<User>, requiredStates: Map<Long, UserState>) {
            assert(requiredUsers matches usersTest.last())
            assert(requiredStates matches statesTest.last())
        }

        val vasyan = User(id = 0, chatId = 0, firstName = "Васян", secondName = "Петросян")
        val tolik = User(id = 1, chatId = 1, firstName = "Толик", secondName = "Алкоголик")
        val danila = User(id = 2, chatId = 2, firstName = "Данила", secondName = "Из Нижнего Тагила")

        userSrc.patchUser(vasyan)
        assertVals(
            requiredUsers = listOf(vasyan),
            requiredStates = mapOf(vasyan.id to UserState.NoneState(vasyan))
        )

        userSrc.patchUserState(UserState.HelpSentState(tolik))
        assertVals(
            requiredUsers = listOf(vasyan, tolik),
            requiredStates = mapOf(
                vasyan.id to UserState.NoneState(vasyan),
                tolik.id to UserState.HelpSentState(tolik)
            )
        )

        userSrc.patchUser(danila)
        assertVals(
            requiredUsers = listOf(vasyan, tolik, danila),
            requiredStates = mapOf(
                vasyan.id to UserState.NoneState(vasyan),
                tolik.id to UserState.HelpSentState(tolik),
                danila.id to UserState.NoneState(danila)
            )
        )

        userSrc.patchUserState(UserState.TrackSentState(danila))
        assertVals(
            requiredUsers = listOf(vasyan, tolik, danila),
            requiredStates = mapOf(
                vasyan.id to UserState.NoneState(vasyan),
                tolik.id to UserState.HelpSentState(tolik),
                danila.id to UserState.TrackSentState(danila)
            )
        )
    }
}