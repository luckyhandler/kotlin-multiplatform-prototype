package de.handler.mpp.mobile

import kotlinx.serialization.UnstableDefault

@UnstableDefault
object Repository {
    private val service = Service()
    private val astronauts = mutableListOf<Astronaut>()

    suspend fun fetchPeople(forceNetwork: Boolean = false): List<Astronaut> {
        if (astronauts.isEmpty() || forceNetwork) {
            astronauts.clear()
            astronauts.addAll(service.fetchPeople().people)
        }
        return astronauts
    }
}