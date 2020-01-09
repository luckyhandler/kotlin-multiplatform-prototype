import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.serialization.UnstableDefault

@UnstableDefault
@ExperimentalCoroutinesApi
class AstronautViewModel : BaseViewModel() {
    suspend fun astronautFlow(repository: Repository): Flow<List<Astronaut>> = flow {
        val astronauts = repository.fetchPeople()
        emit(astronauts)
    }.flowOn(Dispatchers.Main)
}