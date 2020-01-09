package de.handler.mpp.mobile

import AstronautViewModel
import Repository
import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProviders
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.collect
import kotlin.coroutines.CoroutineContext

@InternalCoroutinesApi
class MainActivity : AppCompatActivity(), CoroutineScope {
    private val job = Job()
    override val coroutineContext: CoroutineContext
        get() = Dispatchers.Main + job

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val viewModel = ViewModelProviders.of(this).get(AstronautViewModel::class.java)
        val textView = findViewById<TextView>(R.id.main_text_view)

        launch {
            viewModel.astronautFlow(Repository).collect { value ->
                println(value)
                textView.text = value.joinToString(separator = "\n") { it.name }
            }
        }

    }
}
