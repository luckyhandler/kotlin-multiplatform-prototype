import kotlinx.coroutines.CoroutineScope

expect open class BaseViewModel() {
    val clientScope: CoroutineScope
    protected open fun onCleared()
}