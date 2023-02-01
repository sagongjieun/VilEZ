package kr.co.vilez.ui.user

import android.widget.ImageView
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.bumptech.glide.request.RequestOptions
import kr.co.vilez.R

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
}