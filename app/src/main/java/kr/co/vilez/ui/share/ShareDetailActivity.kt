package kr.co.vilez.ui.share

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import androidx.viewpager2.widget.ViewPager2
import androidx.viewpager2.widget.ViewPager2.OnPageChangeCallback
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kr.co.vilez.R
import kr.co.vilez.databinding.ActivityShareDetailBinding
import kr.co.vilez.util.ApplicationClass
import me.relex.circleindicator.CircleIndicator3
import retrofit2.awaitResponse

private const val TAG = "빌리지_ShareDetailActivity"
class ShareDetailActivity : AppCompatActivity() {
    private lateinit var binding: ActivityShareDetailBinding
    private lateinit var mPager: ViewPager2
    private var pagerAdapter: FragmentStateAdapter? = null
    private var mIndicator: CircleIndicator3? = null
    private var boardId:Int? = 0

    private lateinit var mConext:FragmentActivity

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_share_detail)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_share_detail)
        binding.activity = this
        boardId = intent.getIntExtra("boardId", 0)
        Log.d(TAG, "onCreate: boardId: $boardId")
        setContentView(binding.root)
        mConext = this@ShareDetailActivity
        init()
    }



    fun init(){




        /*
        ******************************************************************************************************
        ******************************************************************************************************
        *******************************           모바일 디테일 뷰           ************************************
        ******************************************************************************************************
        ******************************************************************************************************
         */
        var count = 0
        CoroutineScope(Dispatchers.Main).launch {
            val result =
                ApplicationClass.retrofitShareService.detail(boardId!!).awaitResponse().body()
            val result2 =
                ApplicationClass.retrofitShareService.detail(boardId!!).awaitResponse()

            Log.d(TAG, "init: $result, result2: $result2")
            if (result?.flag =="success") {
                /*
      *****************************************************************************************************
      **************************************** 모바일 캐러셀(carousel) **************************************
      ****************************************      pageNavigator    **************************************
      *****************************************************************************************************
       */
                Log.d(TAG, "init: @@@공유 디테일 ${result.data[0]}")
                binding.article = result.data[0]


                // 해당 글을 작성한 작성자 데이터 가져오기
                val userResult = ApplicationClass.retrofitUserService.getUserDetail(result.data[0].userId).awaitResponse().body()
                if(userResult?.flag == "success") {
                    binding.writer = userResult.data[0]
                } else {
                    return@launch
                }

                Log.d(TAG, "init: success됨, result:$result")
                count = result!!.data[0].list.size
                val myData = result.data[0].list
                if(count == 0) {
                   Log.d(TAG, "init: 사진 없는 게시글임... 일단 종료")
                }

                mPager = binding.viewpager
                //Adapter
                pagerAdapter = MyAdapter(mConext, count, myData)
                Log.d(TAG, "init: pagerAdapter: ${pagerAdapter}")
                mPager.adapter = pagerAdapter
//Indicator
                mIndicator = binding.indicator
                mIndicator!!.setViewPager(mPager)
                mIndicator!!.createIndicators(count, 0);

                mPager.setOrientation(ViewPager2.ORIENTATION_HORIZONTAL);
                mPager!!.setCurrentItem(1000); //시작 지점
                mPager!!.setOffscreenPageLimit(5); //최대 이미지 수

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
                        mIndicator!!.animatePageSelected(position % count)
                    }
                })

                println("*******************************************************")
                println("*******************              **********************")
                println("${result?.data}")
                println("*******************              **********************")
                println("*******************************************************")
            } else {
                Log.d(TAG, "init: fail, result:$result")
            }
        }



    }


}