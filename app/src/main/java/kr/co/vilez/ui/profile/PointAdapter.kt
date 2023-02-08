package kr.co.vilez.ui.profile

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import kr.co.vilez.data.model.PointData
import kr.co.vilez.databinding.PointRecordItemBinding

class PointAdapter(val list:ArrayList<PointData>) :
    RecyclerView.Adapter<PointAdapter.PointHolder>()
{
    inner class PointHolder(var binding: PointRecordItemBinding):
    RecyclerView.ViewHolder(binding.root) {
        fun bindingInfo(item: PointData) {
            binding.point = item
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PointHolder {
        val binding = PointRecordItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return PointHolder(binding)
    }

    override fun onBindViewHolder(holder: PointHolder, position: Int) {
        holder.bindingInfo(list[position])
    }

    override fun getItemCount(): Int  = list.size

}