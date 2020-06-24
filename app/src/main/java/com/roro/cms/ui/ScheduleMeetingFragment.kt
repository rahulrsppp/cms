package com.roro.cms.ui

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.app.TimePickerDialog.OnTimeSetListener
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TimePicker
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import com.roro.cms.MeetingRepository
import com.roro.cms.R
import com.roro.cms.compareTime
import com.roro.cms.compareTwoDates
import com.roro.cms.databinding.FragmentScheduleMeetingBinding
import com.roro.cms.webservice.NetworkResource
import java.text.SimpleDateFormat
import java.util.*

class ScheduleMeetingFragment : Fragment(), View.OnClickListener {

    private var st_hr_Time: Int = 0
    private var st_min_Time: Int = 0
    private var et_hr_Time: Int = 0
    private var et_min_Time: Int = 0

    private var count: Int = 0;

    private  val viewModel: MeetingsViewModel by activityViewModels{
        MeetingsViewModel.MeetingViewModelFactory(MeetingRepository.getInstance(NetworkResource()))
    }

    private lateinit var bindingUtil : FragmentScheduleMeetingBinding
    private var cal = Calendar.getInstance()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        bindingUtil =  DataBindingUtil.inflate(inflater, R.layout.fragment_schedule_meeting, container,false)
        bindingUtil.tvDate.setOnClickListener(this)
        bindingUtil.tvStartTime.setOnClickListener(this)
        bindingUtil.tvEndTime.setOnClickListener(this)
        bindingUtil.btnSubmit.setOnClickListener(this)

        setObserver()
        return bindingUtil.root
    }

    private fun setObserver() {
        viewModel.meetingData.observe(viewLifecycleOwner, Observer {
            it.getData()?.let {

                val sTime = bindingUtil.tvStartTime.text.toString();
                val eTime = bindingUtil.tvEndTime.text.toString();
              if(viewModel.isSlotAvailable(sTime,eTime)){
                  Toast.makeText(requireContext(), "Slot Available.", Toast.LENGTH_SHORT).show()

              }else{
                  Toast.makeText(requireContext(), "Slot Not Available.", Toast.LENGTH_SHORT).show()
              }
            }
        })
    }

    override fun onResume() {
        super.onResume()
        MainActivity.type = "ScheduleMeeting"
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.setTitle(resources.getString(R.string.schedule_a_meeting))
        viewModel.setPreviousText("Back")
        viewModel.setNextOptionVisibility(View.GONE)
    }

    val dateSetListener =
        DatePickerDialog.OnDateSetListener { view, year, monthOfYear, dayOfMonth ->
            cal.set(Calendar.YEAR, year)
            cal.set(Calendar.MONTH, monthOfYear)
            cal.set(Calendar.DAY_OF_MONTH, dayOfMonth)

            val myFormat = "dd/MM/yyyy" // mention the format you need
            val sdf = SimpleDateFormat(myFormat, Locale.US).format(cal.getTime())

            if(compareTwoDates(sdf)!=1) {
                bindingUtil.tvDate.text =sdf
            }else{
                Toast.makeText(requireContext(), "Back date selection is not available", Toast.LENGTH_SHORT).show()
            }
        }


    override fun onClick(v: View?) {
        when(v!!.id){
            R.id.tvDate -> {
                DatePickerDialog(requireContext(),
                    dateSetListener,
                    // set DatePickerDialog to point to today's date when it loads up
                    cal.get(Calendar.YEAR),
                    cal.get(Calendar.MONTH),
                    cal.get(Calendar.DAY_OF_MONTH)).show()

            }
            R.id.tvStartTime -> {
                val timeDialog = TimePickerDialog(
                    context,
                    OnTimeSetListener { view: TimePicker?, hourOfDay: Int, minute: Int ->
                        st_hr_Time = hourOfDay
                        st_min_Time = minute
                        val time = hourOfDay.toString() + ":" + if (minute == 0) "00" else minute
                        bindingUtil.tvStartTime.setText(time)
                    },
                    st_hr_Time,
                    st_min_Time,
                    false
                )
                timeDialog.show()
            }

            R.id.tvEndTime -> {
                val timeDialog = TimePickerDialog(
                    context,
                    OnTimeSetListener { view: TimePicker?, hourOfDay: Int, minute: Int ->
                        et_hr_Time = hourOfDay
                        et_min_Time = minute
                        val time = hourOfDay.toString() + ":" + if (minute == 0) "00" else minute
                        bindingUtil.tvEndTime.setText(time)
                    },
                    et_hr_Time,
                    et_min_Time,
                    false
                )
                timeDialog.show()
            }

            R.id.btnSubmit -> {
                if(validate()) {
                  viewModel.getMeetingData(bindingUtil.tvDate.text.toString(), true)
                }
            }
        }
    }

    private fun validate(): Boolean {
        if(bindingUtil.tvDate.text.length==0){
            Toast.makeText(requireContext(), "Select Date", Toast.LENGTH_SHORT).show()
            return false
        }
        else if(bindingUtil.tvStartTime.text.length==0){
            Toast.makeText(requireContext(), "Select Start Time", Toast.LENGTH_SHORT).show()
            return false
        }
        else if(bindingUtil.tvEndTime.text.length==0) {
            Toast.makeText(requireContext(), "Select End Time", Toast.LENGTH_SHORT).show()
            return false
        }
        else if(compareTime(eTime = bindingUtil.tvEndTime.text.toString(), sTime = bindingUtil.tvStartTime.text.toString())==2) {
            Toast.makeText(requireContext(), "End time should not be lesser than end time.", Toast.LENGTH_SHORT).show()
            return false
        }
        return true
    }

    private fun initializeView() {
        bindingUtil.tvEndTime.text = ""
        bindingUtil.tvStartTime.text = ""
        bindingUtil.tvDate.text = ""
    }

}
