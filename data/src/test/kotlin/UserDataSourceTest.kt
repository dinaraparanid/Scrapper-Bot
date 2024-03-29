import com.paranid5.core.entities.user.User
import com.paranid5.core.entities.user.UserState
import com.paranid5.data.user.UserDataSourceMock
import com.paranid5.utils.matches
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.TestScope
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest

private val vasyan = User(id = 0, chatId = 0, firstName = "Васян", secondName = "Петросян")
private val tolik = User(id = 1, chatId = 1, firstName = "Толик", secondName = "Алкоголик")
private val danila = User(id = 2, chatId = 2, firstName = "Данила", secondName = "Из Нижнего Тагила")

@SpringBootTest
class UserDataSourceTest {
    @Autowired
    private lateinit var userSrc: UserDataSourceMock

    private val usersTest = mutableListOf<Collection<User>>()
    private val statesTest = mutableListOf<Map<Long, UserState>>()

    context(TestScope)
    @OptIn(ExperimentalCoroutinesApi::class)
    private fun launchCollectors() {
        backgroundScope.launch(UnconfinedTestDispatcher(testScheduler)) {
            userSrc.usersFlow.toList(usersTest)
        }

        backgroundScope.launch(UnconfinedTestDispatcher(testScheduler)) {
            userSrc.userStatesFlow.toList(statesTest)
        }
    }

    private fun assertVals(
        requiredUsers: Collection<User>,
        requiredStates: Map<Long, UserState>
    ) {
        assert(requiredUsers matches usersTest.last())
        assert(requiredStates matches statesTest.last())
    }

    @Test
    fun userSrcTest() = runTest {
        launchCollectors()

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