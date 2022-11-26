package com.example.tourpediaexample

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.capitalize
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.tourpediaexample.retrofit.Place
import com.example.tourpediaexample.retrofit.RetrofitService
import com.example.tourpediaexample.ui.theme.TourPediaExampleTheme
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.*

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            TourPediaExampleTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colors.background
                ) {
                    MainScreen()
                }
            }
        }
    }

    override fun onStart() {
        super.onStart()
        
    }
}

@Composable
fun MainScreen() {
    val context = LocalContext.current
    var placesList:MutableList<Place> by remember { mutableStateOf(mutableListOf()) }
    val items = listOf("Amsterdam", "Barcelona", "Berlin", "Dubai", "London", "Paris", "Rome", "Tuscany")
    var expanded by remember { mutableStateOf(false) }
    var selectedIndex by remember { mutableStateOf(0) }
    var changeScreen by remember { mutableStateOf(false) }

    val retrofitService = RetrofitService()
    fun handleClick(location:String) {
        retrofitService.tourApi.getPlaces(location).enqueue(object : Callback<List<Place>> {
            override fun onResponse(
                call: Call<List<Place>>,
                response: Response<List<Place>>
            ) {
                val places = response.body()
                placesList = places as MutableList<Place>
                changeScreen = true
            }

            override fun onFailure(
                call: Call<List<Place>>,
                t:Throwable
            ) {
                Toast.makeText(context, "[Error]", Toast.LENGTH_LONG).show()
            }
        })
    }

    if(!changeScreen) {
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth(0.8f)
                    .fillMaxHeight(0.8f)
                    .wrapContentSize(Alignment.TopStart)
            ) {
                Text(
                    items[selectedIndex],
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(50.dp)
                        .clickable(onClick = { expanded = true })
                        .background(
                            Color.Gray
                        ),
                    color = Color.White,
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 20.sp,
                    textAlign = TextAlign.Center
                )
                DropdownMenu(
                    expanded = expanded,
                    onDismissRequest = { expanded = false },
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(
                            Color.Gray
                        )
                ) {
                    items.forEachIndexed { index, s ->
                        DropdownMenuItem(onClick = {
                            selectedIndex = index
                            expanded = false
                        }) {
                            Text(text = s)
                        }
                    }
                }
            }

            Button(onClick = { handleClick(items.get(selectedIndex)) }) {
                Text(text = "Pesquisar Locais")
            }
        }
    } else {
        if(placesList.isEmpty()) {
            Column(
                modifier = Modifier.fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Text(text = "Nenhum local de interesse foi encontrado")
            }
        } else {
            LazyColumn (
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.White),
                contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
                verticalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                items(placesList) { place ->
                    PlaceRow(place = place)
                }
            }
        }
    }
}

@Composable
fun PlaceRow(place:Place) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(75.dp)
            .background(Color.Gray, RoundedCornerShape(8.dp))
            .padding(horizontal = 10.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth(0.6f)
                .fillMaxHeight(),
            verticalArrangement = Arrangement.SpaceAround,
            horizontalAlignment = Alignment.Start
        ) {
            Text(
                modifier = Modifier
                    .padding(end = 5.dp),
                text = place.name,
                fontWeight = FontWeight.Bold,
                color = Color.White
            )
            Text(
                text = place.address,
                fontSize = 12.sp,
                color = Color.White
            )
        }
        Text(
            text = place.category.capitalize(Locale.ENGLISH),
            fontWeight = FontWeight.SemiBold,
            color = Color.White
        )
    }
}