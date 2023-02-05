package kr.co.vilez.util

import android.annotation.SuppressLint
import android.widget.ImageView
import android.widget.TextView
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.bumptech.glide.request.RequestOptions
import kr.co.vilez.R
import java.text.SimpleDateFormat
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

object BindingAdapter {
    @JvmStatic
    @BindingAdapter("imageFromUrl")
    fun bindImageFromUrl(view: ImageView, imageUrl: String?) {
        if (!imageUrl.isNullOrEmpty()) {
            Glide.with(view.context)
                .load(imageUrl).apply(RequestOptions().placeholder(R.drawable.loading_animation))
                .transition(DrawableTransitionOptions.withCrossFade())
                .into(view)
        }
    }

    @JvmStatic
    @BindingAdapter("boardImageFromUrl")
    fun bindBoardImageFromUrl(view: ImageView, imageUrl: String?) {
        if (!imageUrl.isNullOrEmpty()) {
            Glide.with(view.context)
                .load(imageUrl).apply(RequestOptions().placeholder(R.drawable.loading_animation))
                .transition(DrawableTransitionOptions.withCrossFade())
                .into(view)
        }
    }

    @JvmStatic
    @BindingAdapter("circleImageFromUrl")
    fun bindCircleImageFromUrl(view: ImageView, imageUrl: String?) {
        if (!imageUrl.isNullOrEmpty()) {
            Glide.with(view.context)
                .load(imageUrl).apply(RequestOptions().placeholder(R.drawable.loading_animation))
                .transition(DrawableTransitionOptions.withCrossFade())
                .circleCrop()
                .into(view)
        }
    }

    @JvmStatic
    @SuppressLint("SetTextI18n", "SimpleDateFormat")
    @BindingAdapter("dateToElapsedTime")
    fun elapsedTime(view: TextView, date:String?) {  // 시간을 경과시간으로 바꿔줌!
        if (!date.isNullOrEmpty()) {
            val dateFormat = SimpleDateFormat("yyyy-MM-dd hh:mm:ss")
            val nowDate: LocalDateTime = LocalDateTime.now()
            val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd hh:mm:ss")
            val formatted = nowDate.format(formatter)

            val endDate = dateFormat.parse(formatted.toString()).time
            val startDate = dateFormat.parse(date).time

            val resultTime = (endDate - startDate) / 1000

            val arr = arrayOf(60*60*24*365, 60*60*24*30, 60*60*24, 60*60, 60)
            val date = arrayOf("년", "개월", "일", "시간", "분")
            for(i in 0..4){
                val time = (resultTime / arr[i]).toInt()
                if(time > 0){
                    view.text = time.toString()+date[i]+"전"
                    return
                }
            }
            view.text = "방금 전"
        }
    }
}