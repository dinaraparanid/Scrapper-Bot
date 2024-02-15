import com.paranid5.core.entities.link.LinkType
import com.paranid5.core.entities.link.parseLink
import kotlin.test.Test

private val GH_LINK = "https://github.com/dinaraparanid/Crescendo"
private val SO_LINK = "https://stackoverflow.com/questions/39866676/retrofit-uploading-multiple-images-to-a-single-key"

class LinkParserTest {
    @Test
    fun linkParserTest() {
        assert(parseLink(GH_LINK) == LinkType.GitHubLink(GH_LINK))
        assert(parseLink(SO_LINK) == LinkType.StackOverflowLink(SO_LINK))
    }
}