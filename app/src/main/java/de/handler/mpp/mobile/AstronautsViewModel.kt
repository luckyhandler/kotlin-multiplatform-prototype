package de.handler.mpp.mobile

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import de.handler.mpp.mobile.data.Repository
import de.handler.mpp.mobile.network.Astronaut
import kotlinx.coroutines.InternalCoroutinesApi
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@InternalCoroutinesApi
class AstronautsViewModel : ViewModel() {
    val viewStateLiveData: LiveData<State>
        get() = _astronautsLiveData
    private val repository = Repository()
    private val _astronautsLiveData = MutableLiveData<State>()

    sealed class State {
        object Loading : State()
        data class DataReady(val astronauts: List<Astronaut>) : State()
        data class Error(val errorMessage: String) : State()
    }

    init {
        _astronautsLiveData.postValue(State.Loading)
    }

    fun fetchPeople() {
        repository.fetchPeople { astronauts ->
            viewModelScope.launch {
                if (astronauts.isEmpty()) {
                    _astronautsLiveData.postValue(State.Error("The astronauts list is empty"))
                } else {
                    delay(2000)
                    _astronautsLiveData.postValue(State.DataReady(astronauts = astronauts))
                }
            }
        }
    }
}