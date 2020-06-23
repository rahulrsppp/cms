package com.roro.cms.webservice

import com.roro.cms.ui.MeetingDataModel
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query

class NetworkResource {

    private val retrofit = Retrofit.Builder()
        .baseUrl("http://fathomless-shelf-5846.herokuapp.com/")
        .client(OkHttpClient())
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    private val meetingService = retrofit.create(MeetingService::class.java)

    suspend fun getAllMeetings(date: String): List<MeetingDataModel> = withContext(Dispatchers.IO) {
         meetingService.getMeetingList(date)
    }


    interface MeetingService {
        @GET("/api/schedule?")
     suspend fun getMeetingList(@Query("date") date : String) : List<MeetingDataModel>

    }

}