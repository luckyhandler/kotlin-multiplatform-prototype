package de.handler.mpp.mobile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.CoroutineScope

actual open class BaseViewModel actual constructor(): ViewModel() {
    actual val clientScope: CoroutineScope = viewModelScope

    actual override fun onCleared() {
        super.onCleared()
    }
}
