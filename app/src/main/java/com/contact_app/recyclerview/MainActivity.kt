package com.contact_app.recyclerview

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.JsonObject
import com.google.gson.JsonParser
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


class MainActivity : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: MyAdapter

    private val BASE_URL = "https://d2idqwn63w1bgz.cloudfront.net/"
    private val TAG = "RetrofitExample"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)



        val retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        val apiService = retrofit.create(ApiInterface::class.java)
        val call = apiService.getDataList()

        val dataList = mutableListOf<String>()

        call.enqueue(object : Callback<JsonObject> {
            override fun onResponse(call: Call<JsonObject>, response: Response<JsonObject>) {
                if (response.isSuccessful) {
                    val jsonObject = response.body()



                    // Log all key-value pairs
                    jsonObject?.entrySet()?.forEach { entry ->
                        Log.d(TAG, "${entry.key}: ${entry.value}")
                    }



//
//                    // Extract the values and update the RecyclerView
//                    val dataList = jsonObject?.entrySet()?.map { "${it.key}: ${it.value}" } ?: emptyList()
//                    runOnUiThread {
//                        adapter.setData(dataList)
//                    }


                    // Extract the values and update the RecyclerView dataset
                    dataList.clear()
                    jsonObject?.entrySet()?.forEach { entry ->
                        val unescapedValue = unescapeJson("${entry.key}: ${entry.value}")
                        dataList.add(unescapedValue)
                    }




                    recyclerView = findViewById(R.id.recyclerView)
                    recyclerView.layoutManager = LinearLayoutManager(this@MainActivity)
                    adapter = MyAdapter(dataList)
                    recyclerView.adapter = adapter

                } else {
                    Log.e(TAG, "Request failed with code: ${response.code()}")
                }
            }

            override fun onFailure(call: Call<JsonObject>, t: Throwable) {
                Log.e(TAG, "Request failed: ${t.message}")
            }
        })
    }

    private fun unescapeJson(input: String): String {
        return input.replace("\\", "")
    }
}