package com.roro.cms

import com.roro.cms.ui.MeetingDataModel
import com.roro.cms.webservice.NetworkResource
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers

class MeetingRepository private constructor (
    val networkResource: NetworkResource,
    private val defaultDispatcher: CoroutineDispatcher = Dispatchers.Default
) {


    companion object {
        @Volatile var instance : MeetingRepository? = null

            fun getInstance(networkResource: NetworkResource) =
                instance?: synchronized(this){
                    instance ?: MeetingRepository(networkResource).also { instance = it }

                }

    }


    suspend fun fetchAllMeetingsForSpecificDate(date: String) : List<MeetingDataModel>  {
       return networkResource.getAllMeetings(date)
    }


}