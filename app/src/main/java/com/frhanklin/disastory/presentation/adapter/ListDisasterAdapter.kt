package com.frhanklin.disastory.presentation.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.FitCenter
import com.frhanklin.disastory.R
import com.frhanklin.disastory.data.response.DisasterItems
import com.frhanklin.disastory.databinding.ItemRowDisasterBinding
import com.frhanklin.disastory.utils.DisasterUtils
import com.frhanklin.disastory.utils.ResourceProvider
import com.frhanklin.disastory.utils.TimeAndDateUtils
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.material.bottomsheet.BottomSheetBehavior

class ListDisasterAdapter(
    private val listDisasterItems: ArrayList<DisasterItems>,
    private val mapFragment: SupportMapFragment,
    private val bottomBehavior: BottomSheetBehavior<LinearLayout>,
    private val rp: ResourceProvider
) : RecyclerView.Adapter<ListDisasterAdapter.ListViewHolder>() {

    private val disasterUtils = DisasterUtils(rp)

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
        val disasterRegion = disasterUtils.getRegionString(disaster.disasterProperties?.tags?.instanceRegionCode ?: "")
        val disasterType = disasterUtils.getDisasterType(disaster.disasterProperties?.disasterType)

        val title = "$disasterType \n($disasterRegion)"
        val subtitle = TimeAndDateUtils.convertTimeStamp(disaster.disasterProperties?.createdAt ?: "")
        val imgString = disaster.disasterProperties?.imageUrl



        with(holder) {

            binding.tvDisasterTitle.text = title
            binding.tvDisasterSubtitle.text = subtitle
            Glide.with(itemView.context)
                .load(disasterUtils.getDisasterDefaultImg(disaster.disasterProperties?.disasterType ?: "")
//                    if (imgString.isNullOrBlank() || imgString.isEmpty()) {
//                        imgString
//                    } else {
//                        disasterUtils.getDisasterDefaultImg(disaster.disasterProperties.disasterType)
//                    }
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
                            ), 10f)
                    )
                    bottomBehavior.state = BottomSheetBehavior.STATE_COLLAPSED
                }
            }

        }
    }


    override fun getItemCount(): Int {
        return listDisasterItems.size
    }
}