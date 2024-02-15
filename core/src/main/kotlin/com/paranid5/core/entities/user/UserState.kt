package com.paranid5.core.entities.user

sealed interface UserState {
    val user: User

    data class NoneState(override val user: User) : UserState
    data class StartSentState(override val user: User) : UserState
    data class HelpSentState(override val user: User) : UserState
    data class TrackSentState(override val user: User) : UserState
    data class TrackLinkSentState(override val user: User) : UserState
    data class UntrackSentState(override val user: User) : UserState
    data class UntrackLinkSentState(override val user: User) : UserState
    data class LinkListSentState(override val user: User) : UserState
}