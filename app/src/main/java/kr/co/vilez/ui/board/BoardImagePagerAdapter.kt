package kr.co.vilez.ui.board

import android.util.Log
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import kr.co.vilez.data.model.ImgPath

private const val TAG = "빌리지_BoardImagePagerAdapter"
class BoardImagePagerAdapter(val fa:FragmentActivity, var count: Int = 0, var list:List<ImgPath>) : FragmentStateAdapter(fa) {

    override fun createFragment(position: Int): Fragment {
        Log.d(TAG, "createFragment: count:$count, position:$position")
        return when(val index = getRealPosition(position)) {
            0 -> FirstImagePagerFragment(list[index].path)
            1-> SecondImagePagerFragment(list[index].path)
            2-> ThirdImagePagerFragment(list[index].path)
            else -> FourthImagePagerFragment(list[index].path)
        }
    }

    override fun getItemCount(): Int {
        return 2000
    }

    fun getRealPosition(position: Int): Int {
        return position % count
    }

}