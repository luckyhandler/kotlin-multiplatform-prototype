package de.handler.mpp.mobile

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch
import kotlinx.serialization.UnstableDefault

@UnstableDefault
object Repository {
    private val service = Service()
    private val astronauts = mutableListOf<Astronaut>()

    // For Android
    suspend fun fetchPeople(forceNetwork: Boolean = false): List<Astronaut> {
        if (astronauts.isEmpty() || forceNetwork) {
            astronauts.clear()
            astronauts.addAll(service.fetchPeople().people)
        }
        return astronauts
    }

    // For iOS
    fun fetchPeople(success: (List<Astronaut>) -> Unit) {
        MainScope().launch(Dispatchers.Main) {
            success(fetchPeople())
        }
    }
}