package com.paranid5.bot.data.link.response

sealed interface LinkResponse {
    val userId: Long
    val link: String

    sealed interface TrackResponse : LinkResponse {
        data class New(
            override val userId: Long,
            override val link: String
        ) : LinkResponse

        data class Present(
            override val userId: Long,
            override val link: String
        ) : LinkResponse
    }

    sealed interface UntrackResponse : LinkResponse {
        data class New(
            override val userId: Long,
            override val link: String
        ) : LinkResponse

        data class Present(
            override val userId: Long,
            override val link: String
        ) : LinkResponse
    }

    data class Invalid(
        override val userId: Long,
        override val link: String
    ) : LinkResponse
}