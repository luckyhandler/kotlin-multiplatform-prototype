package de.handler.mpp.mobile

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.Composable
import androidx.compose.state
import androidx.compose.unaryPlus
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.ui.animation.Crossfade
import androidx.ui.core.Clip
import androidx.ui.core.Text
import androidx.ui.core.dp
import androidx.ui.core.setContent
import androidx.ui.foundation.DrawImage
import androidx.ui.foundation.VerticalScroller
import androidx.ui.foundation.shape.corner.RoundedCornerShape
import androidx.ui.graphics.Color
import androidx.ui.graphics.Image
import androidx.ui.layout.*
import androidx.ui.material.CircularProgressIndicator
import androidx.ui.material.ListItem
import androidx.ui.material.MaterialTheme
import androidx.ui.res.imageResource
import de.handler.mpp.mobile.network.Astronaut
import kotlinx.coroutines.InternalCoroutinesApi

@InternalCoroutinesApi
class AstronautActivity : AppCompatActivity() {
    private lateinit var viewModel: AstronautsViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProviders.of(this)[AstronautsViewModel::class.java]
        viewModel.fetchPeople()

        setContent {
            MaterialTheme {
                AstronautWrapper(viewModel.viewStateLiveData)
            }
        }
    }


    @Composable
    fun AstronautWrapper(viewStateLiveData: LiveData<AstronautsViewModel.State>) {
        val state = +state { viewStateLiveData.value }
        viewStateLiveData.observe(this@AstronautActivity, Observer {
            state.value = it
        })

        val image = +imageResource(R.drawable.iss)
        Crossfade(current = AstronautsViewModel.State.Loading) {
            when (state.value) {
                is AstronautsViewModel.State.Loading -> LoadingIndicator(image = image)
                is AstronautsViewModel.State.DataReady -> AstronautsList(
                    astronauts = (state.value as AstronautsViewModel.State.DataReady).astronauts,
                    image = image
                )
                is AstronautsViewModel.State.Error -> Text((state.value as AstronautsViewModel.State.Error).errorMessage)
            }
        }
    }

    @Composable
    fun AstronautsList(
        astronauts: List<Astronaut>,
        image: Image
    ) {
        Column {
            Container(modifier = Height(180.dp) wraps Expanded) {
                Clip(shape = RoundedCornerShape(8.dp)) {
                    DrawImage(image = image)
                }
            }
            Padding(16.dp) {
                VerticalScroller {
                    Column {
                        astronauts.forEach { astronaut ->
                            Row {
                                ListItem(text = astronaut.name, secondaryText = astronaut.craft)
                            }
                        }
                    }
                }
            }
        }
    }

    @Composable
    fun LoadingIndicator(image: Image) {
        Column {
            DrawImage(image = image)
            Center {
                Container(modifier = ExpandedHeight) {
                    CircularProgressIndicator(color = Color.Yellow)
                }
            }
        }
    }
}