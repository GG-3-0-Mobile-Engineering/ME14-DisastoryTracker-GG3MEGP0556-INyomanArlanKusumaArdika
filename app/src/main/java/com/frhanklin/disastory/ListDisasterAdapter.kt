package com.frhanklin.disastory

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.FitCenter
import com.frhanklin.disastory.data.Region
import com.frhanklin.disastory.data.response.DisasterItems
import com.frhanklin.disastory.databinding.ItemRowDisasterBinding
import com.frhanklin.disastory.utils.TimeAndDateUtils
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng

class ListDisasterAdapter(
    private val listDisasterItems: ArrayList<DisasterItems>,
    private val mapFragment: SupportMapFragment,
    private val c: Context
    ) : RecyclerView.Adapter<ListDisasterAdapter.ListViewHolder>() {



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
        val disasterRegion = Region.getRegionString(c, disaster.disasterProperties?.tags?.instanceRegionCode ?: "")
        val disasterType =
            when (disaster.disasterProperties?.disasterType) {
                c.getString(R.string.flood) -> c.getString(R.string.type_flood)
                c.getString(R.string.haze) -> c.getString(R.string.type_haze)
                c.getString(R.string.wind) -> c.getString(R.string.type_wind)
                c.getString(R.string.earthquake) -> c.getString(R.string.type_earthquake)
                c.getString(R.string.volcano) -> c.getString(R.string.type_volcano)
                c.getString(R.string.fire) -> c.getString(R.string.type_fire)
                else -> c.getString(R.string.unknown)
            }
        val title = "$disasterType \n($disasterRegion)"
        val subtitle = TimeAndDateUtils.convertTimeStamp(disaster.disasterProperties?.createdAt ?: "")



        with(holder) {

            binding.tvDisasterTitle.text = title
            binding.tvDisasterSubtitle.text = subtitle
            Glide.with(itemView.context)
                .load(
                    when(disaster.disasterProperties?.disasterType) {
                        "flood" -> R.drawable.img_illustration_flood
                        "haze" -> R.drawable.img_illustration_haze
                        "fire" -> R.drawable.img_illustration_fire
                        "wind" -> R.drawable.img_illustration_wind
                        "earthquake" -> R.drawable.img_illustration_earthquake
                        "volcano" -> R.drawable.img_illustration_volcano
                        else -> R.drawable.ic_not_listed_location_24
                    }
                )
                .transform(FitCenter())
                .into(binding.ivDisasterImage)

            binding.itemDisaster.setOnClickListener {
                mapFragment.getMapAsync {
                    it.animateCamera(CameraUpdateFactory.zoomOut())
                    it.animateCamera(
                        CameraUpdateFactory.newLatLngZoom(
                            LatLng(
                                disaster.coordinates?.get(0) ?: 0.0,
                                disaster.coordinates?.get(1) ?: 0.0
                            ), 4f)
                    )
                }
            }

        }
    }


    override fun getItemCount(): Int {
        return listDisasterItems.size
    }
}