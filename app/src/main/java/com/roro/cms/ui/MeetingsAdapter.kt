package com.roro.cms.ui

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.roro.cms.R
import com.roro.cms.databinding.RowMeetingsBinding

class MeetingsAdapter(items: List<MeetingDataModel>) : RecyclerView.Adapter<MeetingsAdapter.ViewHolder>(){

    private var items: List<MeetingDataModel> = emptyList()

    init {
        this.items = items
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(parent)
    }

    override fun getItemCount(): Int {
        return items.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) = holder.bindData(items.get(position))

    class ViewHolder(private val parent: ViewGroup,
                     private val binding: RowMeetingsBinding = DataBindingUtil.inflate(
                         LayoutInflater.from(parent.context),
                         R.layout.row_meetings,
                         parent,
                         false)
    ) : RecyclerView.ViewHolder(binding.root){

        fun bindData(data: MeetingDataModel){
            binding.dataModel = data
        }
    }
}