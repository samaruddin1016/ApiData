package com.contact_app.recyclerview

import com.google.gson.JsonObject
import retrofit2.Call
import retrofit2.http.GET

interface ApiInterface {

    @GET("cdn/12961/homepage/cedprev/en/1704726769.json")
    fun getDataList() : Call<JsonObject>
}