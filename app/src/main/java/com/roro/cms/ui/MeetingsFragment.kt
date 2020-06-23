package com.roro.cms.ui

import android.app.Activity
import android.database.DatabaseUtils
import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.roro.cms.MeetingRepository

import com.roro.cms.R
import com.roro.cms.databinding.FragmentMeetingsBinding
import com.roro.cms.getCurrentDate
import com.roro.cms.webservice.NetworkResource

class MeetingsFragment : Fragment(), View.OnClickListener {

    private val viewModel: MeetingsViewModel by activityViewModels {
        MeetingsViewModel.MeetingViewModelFactory(MeetingRepository.getInstance(NetworkResource()))
    }

    private lateinit var bindingUtil : FragmentMeetingsBinding
    private lateinit var meetingAdapter : MeetingsAdapter
    private  var items: MutableList<MeetingDataModel> = mutableListOf()
    private lateinit var navController: NavController

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        bindingUtil =  DataBindingUtil.inflate(inflater, R.layout.fragment_meetings, container,false)
        bindingUtil.viewModel = viewModel
        setObserver()

        return bindingUtil.root
    }

    override fun onResume() {
        super.onResume()
        MainActivity.type = "MeetingData"
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        navController = Navigation.findNavController(view)

        viewModel.setPreviousOptionVisibility(View.VISIBLE)
        viewModel.setPreviousText(resources.getString(R.string.prev))
        viewModel.setNextOptionVisibility(View.VISIBLE)

        // Set adapter
        meetingAdapter = MeetingsAdapter(items)
        bindingUtil.rvMeetings.adapter = meetingAdapter
        bindingUtil.rvMeetings.layoutManager = LinearLayoutManager(activity)

        // setting listener
        bindingUtil.btnSchedule.setOnClickListener(this)

        // calling meeting list
        viewModel.getMeetingData(getCurrentDate())
    }

    private fun setObserver() {
        viewModel.meetingData.observe(viewLifecycleOwner, Observer {
            it?.let {
                items.clear()
                items.addAll(it)
                meetingAdapter.notifyDataSetChanged()
            }
        })

        viewModel.spinner.observe(viewLifecycleOwner, Observer {
            bindingUtil.spinner.visibility = if (it) View.VISIBLE else View.GONE
        })
    }

    override fun onClick(v: View?) {
        when (v!!.id) {
            R.id.btnSchedule -> navController.navigate(R.id.action_meetingsFragment_to_scheduleMeetingFragment)
        }
    }
}


