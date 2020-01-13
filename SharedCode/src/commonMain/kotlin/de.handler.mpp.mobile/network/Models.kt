package de.handler.mpp.mobile.network

import kotlinx.serialization.Serializable

@Serializable
data class AstronautWrapper(val number: Int, val people: List<Astronaut>)

@Serializable
data class Astronaut(val craft: String, val name: String)

