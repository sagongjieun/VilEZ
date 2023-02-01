package kr.co.vilez.ui.share.write

import android.annotation.SuppressLint
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.datepicker.CalendarConstraints
import com.google.android.material.datepicker.MaterialDatePicker
import kr.co.vilez.R
import kr.co.vilez.databinding.ActivityShareWriteBinding
import kr.co.vilez.util.PermissionUtil
import java.text.SimpleDateFormat
import java.util.*

private const val TAG = "빌리지_ShareWriteActivity"
class ShareWriteActivity : AppCompatActivity() {
    private lateinit var binding:ActivityShareWriteBinding

    val imgList = mutableListOf<Uri>()
    lateinit var imageAdapter: BoardWriteImageAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
         binding = DataBindingUtil.setContentView(this, R.layout.activity_share_write)
        binding.activity = this
        initToolBar()

        initView()
    }

    private fun initToolBar() {
        this.setSupportActionBar(binding.toolbar)
        this.supportActionBar?.setDisplayShowTitleEnabled(false) // 기본 타이틀 제거
        binding.title = "공유 글쓰기"
    }


    private fun initView() {
        imageAdapter = BoardWriteImageAdapter(this@ShareWriteActivity, imgList)
        imageAdapter.deleteClickListener = object:BoardWriteImageAdapter.OnItemClickListener {
            @SuppressLint("SetTextI18n")
            override fun onClick(view: View, position: Int) { // 삭제 후 사진 개수 변경
                binding.tvPhotoCnt.text = "${imgList.size} / 4"
            }
        }
        binding.horRecyclerViewImg.apply {
            adapter = imageAdapter
            layoutManager = LinearLayoutManager(this@ShareWriteActivity, LinearLayoutManager.HORIZONTAL, false)
        }
    }

    @SuppressLint("SetTextI18n")
    fun addImage(view: View) {
        if(imgList.size < 4) {
            PermissionUtil().galleryPermission(true, this@ShareWriteActivity, imageResult)
        } else { // 이미지4개 넘어가면 불가
            Toast.makeText(this@ShareWriteActivity, "더이상 사진을 추가할 수 없습니다.", Toast.LENGTH_SHORT).show()
        }
    }

    // 이미지 갤러리에서 선택할 시 콜백
    private val imageResult = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == RESULT_OK) {
            if(result.data == null) { // 이미지 하나도 선택안함
                Log.d(TAG, "갤러리: 아무것도 선택 안함")
                return@registerForActivityResult
            } else if(result.data!!.clipData == null) { // 이미지 하나 선택함
                val imageUri = result.data?.data ?: return@registerForActivityResult
                imgList.add(imageUri)
                imageAdapter.notifyItemInserted(imgList.size-1)
                binding.tvPhotoCnt.text = "${imgList.size} / 4"
            } else { // 이미지 여러개 선택함
                val clipData = result.data!!.clipData!!
                if(clipData.itemCount+imgList.size <= 4) { // 원래 선택된 이미지 + 새로 선택된 이미지 합이 4 이하여야 함
                    for (i in 0 until clipData.itemCount) {
                        val imageUri = clipData.getItemAt(i).uri // 선택한 이미지들의 uri를 가져온다.
                        try {
                            imgList.add(imageUri) //uri를 list에 담는다.
                        } catch (e: Exception) {
                            Log.e(TAG, "File select error", e)
                        }
                    }
                    imageAdapter.notifyDataSetChanged()
                    binding.tvPhotoCnt.text = "${imgList.size} / 4"
                } else { // 이미지4개 넘어가면 불가
                    Toast.makeText(this@ShareWriteActivity, "더이상 사진을 추가할 수 없습니다.", Toast.LENGTH_SHORT).show()
                    return@registerForActivityResult
                }
            }
        }
    }

    fun onBackPressed(view: View) {
        finish()
    }


    fun onCategoryClick(view: View) {
        val categoryPicker = androidx.appcompat.app.AlertDialog.Builder(this)
        val categories = resources.getStringArray(R.array.category)
        categoryPicker.setTitle("카테고리 선택")
        categoryPicker.setItems(categories
        ) { _, pos ->
            Toast.makeText(this@ShareWriteActivity, "${categories[pos]} 선택", Toast.LENGTH_SHORT).show()
        }
        categoryPicker.show()
    }

    fun onDateClick(view: View) {
        val today = MaterialDatePicker.todayInUtcMilliseconds()
        val calendar = Calendar.getInstance(TimeZone.getTimeZone("Asia/Seoul"))

        calendar.timeInMillis = today
        val startToday = calendar.timeInMillis

        calendar.timeInMillis = today
        calendar[Calendar.MONTH] = Calendar.DECEMBER
        val decThisYear = calendar.timeInMillis

        // Build constraints.
        val constraintsBuilder =
            CalendarConstraints.Builder()
                .setStart(startToday)
                .setEnd(decThisYear)

        val datePicker =
            MaterialDatePicker.Builder.dateRangePicker()
                .setTitleText("공유기간 선택")
                .setSelection(androidx.core.util.Pair(MaterialDatePicker.thisMonthInUtcMilliseconds(), MaterialDatePicker.todayInUtcMilliseconds())) // 디폴트 날짜
                .setCalendarConstraints(constraintsBuilder.build())
                .build()
        datePicker.addOnPositiveButtonClickListener {
            Toast.makeText(this@ShareWriteActivity, getDate(it.first, it.second), Toast.LENGTH_SHORT).show()
        }
        datePicker.show(supportFragmentManager, "Date")
    }

    fun getDate(startMills: Long, endMills: Long): String {
        val dateFormat = "yyyy-MM-dd"
        // Create a DateFormatter object for displaying date in specified format.
        val formatter = SimpleDateFormat(dateFormat)
        // Create a calendar object that will convert the date and time value in milliseconds to date.
        val calendar = Calendar.getInstance(TimeZone.getTimeZone("Asia/Seoul"))
        calendar.timeInMillis = startMills
        val startDay = formatter.format(calendar.time)
        calendar.timeInMillis = endMills
        val endDay = formatter.format(calendar.time)
        return "$startDay ~ $endDay"
    }

    fun onPlaceClick(view: View) {

    }

    fun savePost(view: View) {

    }

}
