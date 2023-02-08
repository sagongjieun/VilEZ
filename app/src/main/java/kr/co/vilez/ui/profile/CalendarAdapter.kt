package kr.co.vilez.ui.profile

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import kr.co.vilez.data.dto.CalendarDto
import kr.co.vilez.databinding.CalendarListItemBinding

class CalendarAdapter(val list:ArrayList<CalendarDto>):
RecyclerView.Adapter<CalendarAdapter.CalendarHolder>(){
    inner class CalendarHolder(var binding: CalendarListItemBinding):
            RecyclerView.ViewHolder(binding.root) {
                fun bindingInfo(item: CalendarDto) {
                    binding.calendar = item
                }
            }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CalendarHolder {
        val binding = CalendarListItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return CalendarHolder(binding)
    }

    override fun onBindViewHolder(holder: CalendarHolder, position: Int) {
        holder.bindingInfo(list[position])
    }

    override fun getItemCount(): Int = list.size

}