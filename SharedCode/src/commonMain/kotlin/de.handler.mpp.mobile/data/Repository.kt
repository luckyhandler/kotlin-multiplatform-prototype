package de.handler.mpp.mobile.data

import de.handler.mpp.mobile.dispatcher
import kotlinx.serialization.UnstableDefault
import de.handler.mpp.mobile.network.Astronaut
import de.handler.mpp.mobile.network.Service
import kotlinx.coroutines.*

@UnstableDefault
@InternalCoroutinesApi
@ExperimentalCoroutinesApi
@ExperimentalUnsignedTypes
class Repository {
    private val service = Service()

    private val db = createDatabase()
    private val astronautQueries = db?.astronautModelQueries

    suspend fun fetchPeople(forceNetwork: Boolean = false): List<Astronaut> {
        val localAstronauts = astronautQueries?.selectAll { _, craft, name -> Astronaut(craft, name) }?.executeAsList()

        return if (localAstronauts.isNullOrEmpty() || forceNetwork) {
            val remoteAstronauts = service.fetchPeople().people
            remoteAstronauts.forEach { astronaut: Astronaut ->
                astronautQueries?.insertItem(astronaut.craft, astronaut.name)
            }
            remoteAstronauts
        } else {
            localAstronauts
        }
    }

    // For iOS
    fun fetchPeople(success: (List<Astronaut>) -> Unit) {
        MainScope().launch(dispatcher()) {
            success(fetchPeople())
        }
    }
}