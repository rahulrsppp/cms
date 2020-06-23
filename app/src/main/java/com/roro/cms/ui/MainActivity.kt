package com.roro.cms.ui

import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.navigation.fragment.NavHostFragment
import com.roro.cms.MeetingRepository
import com.roro.cms.R
import com.roro.cms.databinding.ActivityMainBinding
import com.roro.cms.getDesiredDateFormat
import com.roro.cms.getNextPreviousDate
import com.roro.cms.webservice.NetworkResource


class MainActivity : AppCompatActivity(), View.OnClickListener {

    private val viewModel: MeetingsViewModel by viewModels {
        MeetingsViewModel.MeetingViewModelFactory(MeetingRepository.getInstance(NetworkResource()))
    }

    companion object {
        var  type: String = "MeetingData"
    }

    lateinit var bindingUtils :ActivityMainBinding
    private lateinit var navController: NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        bindingUtils = DataBindingUtil.setContentView(this, R.layout.activity_main)
        bindingUtils.viewModel = viewModel
        bindingUtils.toolbar.tvNext.setOnClickListener(this)
        bindingUtils.toolbar.tvPrev.setOnClickListener(this)
        navController = Navigation.findNavController(this,R.id.nav_host_fragment)
        setObserver()
    }

    private fun setObserver() {
        viewModel.date.observe(this, Observer {
            bindingUtils.toolbar.tvDate.text = it
        })

        viewModel.rightVisibility.observe(this, Observer {
            bindingUtils.toolbar.tvNext.visibility = it
        })

        viewModel.leftVisibility.observe(this, Observer {
            bindingUtils.toolbar.tvPrev.visibility = it
        })
        viewModel.leftText.observe(this, Observer {
            bindingUtils.toolbar.tvPrev.text = it
        })
    }

    override fun onClick(v: View?) {
        when(v!!.id){
            R.id.tvNext -> {
                val date = bindingUtils.toolbar.tvDate.text.toString().getDesiredDateFormat("dd/MM/yyyy", "dd-MM-yyyy")
                viewModel.getMeetingData(date.getNextPreviousDate(1))
            }
            R.id.tvPrev -> {
                if(type.equals("MeetingData")) {
                    val date = bindingUtils.toolbar.tvDate.text.toString().getDesiredDateFormat("dd/MM/yyyy", "dd-MM-yyyy")
                    viewModel.getMeetingData(date.getNextPreviousDate(-1))
                }else{
                    navController.navigateUp()
                }
            }
        }
    }
}
