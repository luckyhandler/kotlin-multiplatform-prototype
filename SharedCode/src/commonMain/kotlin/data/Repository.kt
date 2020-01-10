package data

import dispatcher
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.InternalCoroutinesApi
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch
import kotlinx.serialization.UnstableDefault
import network.Astronaut
import network.Service

@UnstableDefault
@InternalCoroutinesApi
@ExperimentalCoroutinesApi
@ExperimentalUnsignedTypes
class Repository {
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
        MainScope().launch(dispatcher()) {
            success(fetchPeople())
        }
    }
}