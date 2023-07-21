package com.frhanklin.disastory

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.frhanklin.disastory.data.response.DisasterItems
import com.frhanklin.disastory.data.response.DisasterProperties
import com.frhanklin.disastory.databinding.ItemRowDisasterBinding

class ListDisasterAdapter(private val listDisasterItems: ArrayList<DisasterItems>) : RecyclerView.Adapter<ListDisasterAdapter.ListViewHolder>() {

    class ListViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val binding = ItemRowDisasterBinding.bind(itemView)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListViewHolder {
        val view: View = LayoutInflater
            .from(parent.context)
            .inflate(R.layout.item_row_disaster, parent, false)
        return ListViewHolder(view)
    }

    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
        val disaster = listDisasterItems[position]

        with(holder) {
            binding.tvDisasterTitle.text = disaster.disasterProperties?.disasterType
            binding.tvDisasterSubtitle.text = disaster.disasterProperties?.createdAt
            Glide.with(itemView.context)
                .load(disaster.disasterProperties?.imageUrl ?: "https://www.goto-impact.org/images/gotoimpact-logo.svg")
                .into(binding.ivDisasterImage)

        }
    }

    override fun getItemCount(): Int {
        return listDisasterItems.size
    }
}