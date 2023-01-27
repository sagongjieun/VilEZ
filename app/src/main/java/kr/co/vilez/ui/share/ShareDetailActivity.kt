package kr.co.vilez.ui.share

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import androidx.viewpager2.widget.ViewPager2
import androidx.viewpager2.widget.ViewPager2.OnPageChangeCallback
import kr.co.vilez.R
import kr.co.vilez.databinding.ActivityShareDetailBinding
import me.relex.circleindicator.CircleIndicator3

class ShareDetailActivity : AppCompatActivity() {
    private lateinit var binding: ActivityShareDetailBinding
    private var mPager: ViewPager2? = null
    private var pagerAdapter: FragmentStateAdapter? = null
    private val num_page = 4
    private var mIndicator: CircleIndicator3? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_share_detail)
        binding = ActivityShareDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        init()
    }

    fun init(){
        mPager = binding.viewpager

        //Adapter
        pagerAdapter = MyAdapter(this, 3)
        mPager!!.adapter = pagerAdapter
        //Indicator
        mIndicator = binding.indicator
        mIndicator!!.setViewPager(mPager);
        mIndicator!!.createIndicators(num_page,0);

        mPager!!.setOrientation(ViewPager2.ORIENTATION_HORIZONTAL);
        mPager!!.setCurrentItem(1000); //시작 지점
        mPager!!.setOffscreenPageLimit(3); //최대 이미지 수

        mPager!!.registerOnPageChangeCallback(object : OnPageChangeCallback() {
            override fun onPageScrolled(
                position: Int,
                positionOffset: Float,
                positionOffsetPixels: Int
            ) {
                super.onPageScrolled(position, positionOffset, positionOffsetPixels)
                if (positionOffsetPixels == 0) {
                    mPager!!.currentItem = position
                }
            }

            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                mIndicator!!.animatePageSelected(position % 3)
            }
        })
    }


}