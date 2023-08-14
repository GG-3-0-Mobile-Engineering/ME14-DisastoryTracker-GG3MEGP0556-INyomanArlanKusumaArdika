package com.frhanklin.disastory.presentation.ui.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagedListAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.FitCenter
import com.frhanklin.disastory.data.local.entity.DisasterModel
import com.frhanklin.disastory.databinding.ItemRowDisasterBinding
import com.frhanklin.disastory.utils.DisasterUtils
import com.frhanklin.disastory.utils.TimeAndDateUtils

class DisasterAdapter(
    private val disasterUtils : DisasterUtils
): PagedListAdapter<DisasterModel, DisasterAdapter.DisasterListViewHolder>(DIFF_CALLBACK) {

    private lateinit var onItemClickCallback: OnItemClickCallback

    interface OnItemClickCallback {
        fun onItemClicked(latitude: Double, longitude: Double)
    }

    fun setOnItemClickCallback(onItemClickCallback: OnItemClickCallback) {
        this.onItemClickCallback = onItemClickCallback
    }

    inner class DisasterListViewHolder(private val binding: ItemRowDisasterBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(disaster: DisasterModel) {
            val disasterRegion = disasterUtils.getRegionString(disaster.regionCode)
            val disasterType = disasterUtils.getDisasterType(disaster.type)

            val title = "$disasterType \n($disasterRegion)"
            val subtitle = TimeAndDateUtils.convertTimeStamp(disaster.createdAt)

            binding.tvDisasterTitle.text = title
            binding.tvDisasterSubtitle.text = subtitle
            Glide.with(itemView.context)
                .load(disasterUtils.getDisasterDefaultImg(disaster.type))
                .transform(FitCenter())
                .into(binding.ivDisasterImage)

            binding.itemDisaster.setOnClickListener {
                onItemClickCallback.onItemClicked(disaster.latitude, disaster.longitude)

            }

        }
    }

    companion object {
        private val DIFF_CALLBACK = object: DiffUtil.ItemCallback<DisasterModel>() {
            override fun areItemsTheSame(oldItem: DisasterModel, newItem: DisasterModel): Boolean {
                return oldItem.key == newItem.key
            }

            override fun areContentsTheSame(
                oldItem: DisasterModel,
                newItem: DisasterModel
            ): Boolean {
                return oldItem == newItem
            }

        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DisasterListViewHolder {
        val itemDisasterBinding = ItemRowDisasterBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return DisasterListViewHolder(itemDisasterBinding)
    }

    override fun onBindViewHolder(holder: DisasterListViewHolder, position: Int) {
        val disaster = getItem(position)
        if (disaster != null) {
            holder.bind(disaster)
        }
    }
}