package com.roro.cms.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.roro.cms.getDesiredTimeFormat

data class MeetingDataModel(
    var start_time : String,
    var end_time : String,
    var description: String
){

    fun getCombinedDate() = start_time.getDesiredTimeFormat() + " - " +  end_time.getDesiredTimeFormat()

}