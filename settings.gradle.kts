plugins {
    id("org.gradle.toolchains.foojay-resolver-convention") version "0.5.0"
}
rootProject.name = "tinkoff_bot"
include("bot")
include("data")
include("core")
include("utils")
