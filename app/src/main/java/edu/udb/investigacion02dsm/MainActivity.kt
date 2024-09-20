package edu.udb.investigacion02dsm

import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import edu.udb.investigacion02dsm.databinding.ActivityMainBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.Callback
import retrofit2.Response

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var weatherAdapter: WeatherAdapter
    private val weatherList = mutableListOf<WeatherResponse>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initRecyclerView()

        binding.svPais.setOnQueryTextListener(object : androidx.appcompat.widget.SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                query?.let {
                    searchByName(it)
                    binding.svPais.clearFocus()
                }
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                return false
            }
        })
    }

    private fun initRecyclerView() {
        weatherAdapter = WeatherAdapter(weatherList)
        binding.recyclerView.layoutManager = LinearLayoutManager(this)
        binding.recyclerView.adapter = weatherAdapter
    }

    private fun getRetrofit(): Retrofit {
        return Retrofit.Builder()
            .baseUrl("https://api.openweathermap.org/data/2.5/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    private fun searchByName(city: String) {
        val apiKey = "4303a31ee6a45d8b573977a3d38b49be"
        getRetrofit().create(ApiService::class.java).getWeather(city, apiKey).enqueue(object : Callback<WeatherResponse> {
            override fun onResponse(call: Call<WeatherResponse>, response: Response<WeatherResponse>) {
                if (response.isSuccessful) {
                    response.body()?.let { weatherResponse ->
                        weatherList.clear()
                        weatherList.add(weatherResponse)
                        weatherAdapter.notifyDataSetChanged()
                    }
                } else {
                    // Muestra un mensaje al usuario si la ciudad no se encuentra
                    Toast.makeText(this@MainActivity, "Ciudad no encontrada. Por favor verifica el nombre.", Toast.LENGTH_SHORT).show()
                    Log.e("MainActivity", "Error: ${response.errorBody()?.string()}")
                }
            }

            override fun onFailure(call: Call<WeatherResponse>, t: Throwable) {
                Log.e("MainActivity", "Failure: ${t.message}")
            }
        })
    }
}
