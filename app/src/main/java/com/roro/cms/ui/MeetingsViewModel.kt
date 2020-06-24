package com.roro.cms.ui

import android.view.View
import androidx.lifecycle.*
import com.roro.cms.*
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

class MeetingsViewModel internal constructor(
    private val meetingRepo: MeetingRepository
) : ViewModel() {


    private val _meetingData = MutableLiveData<Event<List<MeetingDataModel>>>()
    val meetingData :LiveData<Event<List<MeetingDataModel>>> = _meetingData

    private val _spinner = MutableLiveData<Boolean>(false)
    val spinner: LiveData<Boolean>
        get() = _spinner

    private val _snackBar = MutableLiveData<String?>()
    val snackBar: LiveData<String?>
        get() = _snackBar

    private val _date = MutableLiveData<String>()
    val date: LiveData<String>
        get() = _date

    private val _leftVisibility = MutableLiveData<Int>(View.VISIBLE)
    val leftVisibility: LiveData<Int>
        get() = _leftVisibility

    private val _rightVisibility = MutableLiveData<Int>(View.VISIBLE)
    val rightVisibility: LiveData<Int>
        get() = _rightVisibility

    private val _leftText = MutableLiveData<String>("Prev")
    val leftText: LiveData<String>
        get() = _leftText

    private val meetingList = mutableListOf<MeetingDataModel>()

    fun getMeetingData(date: String, forSchedulingData :Boolean =false) {

        if(!forSchedulingData)
            _date.value = date.getDesiredDateFormat()
        fetchMeetingData(date)

    }

    private fun fetchMeetingData(date: String) {
        launchDataLoad {
            val fetchedData =meetingRepo.fetchAllMeetingsForSpecificDate(date)
            _meetingData.value = Event(fetchedData)
            meetingList.clear()
            meetingList.addAll(fetchedData)
        }
    }


    fun isSlotAvailable(sTime: String, eTime: String) :Boolean {
        var slotAvailable = false

        for (data in meetingList){
            if(compareTime(data.start_time,data.end_time,sTime,eTime,true)==1){
                slotAvailable =true
            }
        }
        return   slotAvailable
    }

    private fun launchDataLoad(block: suspend () -> Unit): Job {
        return viewModelScope.launch {
            try {
                _spinner.value = true
                block()
            } catch (error: Throwable) {
                _snackBar.value = error.message
            } finally {
                _spinner.value = false
            }
        }
    }

    fun setTitle(title: String){
        _date.value = title
    }
    fun setPreviousOptionVisibility(visibility: Int) {
        _leftVisibility.value = visibility
    }

    fun setNextOptionVisibility(visibility: Int) {
        _rightVisibility.value = visibility
    }

    fun setPreviousText(text: String) {
        _leftText.value = text
    }


    class MeetingViewModelFactory(
        private val repository: MeetingRepository
    ) : ViewModelProvider.NewInstanceFactory() {

        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel> create(modelClass: Class<T>) = MeetingsViewModel(repository) as T
    }


}
