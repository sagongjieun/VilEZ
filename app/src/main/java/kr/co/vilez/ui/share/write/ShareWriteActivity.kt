package kr.co.vilez.ui.share.write

import android.annotation.SuppressLint
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.provider.Settings
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.datepicker.CalendarConstraints
import com.google.android.material.datepicker.MaterialDatePicker
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kr.co.vilez.R
import kr.co.vilez.data.dto.WriteBoard
import kr.co.vilez.databinding.ActivityShareWriteBinding
import kr.co.vilez.service.RESTShareBoardDetail
import kr.co.vilez.ui.ask.AskDetailActivity
import kr.co.vilez.ui.dialog.ConfirmDialog
import kr.co.vilez.ui.dialog.ConfirmDialogInterface
import kr.co.vilez.ui.dialog.ProgressDialog
import kr.co.vilez.ui.share.ShareDetailActivity
import kr.co.vilez.util.ApplicationClass
import kr.co.vilez.util.ChangeMultipartUtil
import kr.co.vilez.util.Common.Companion.BOARD_TYPE_ASK
import kr.co.vilez.util.Common.Companion.BOARD_TYPE_SHARE
import kr.co.vilez.util.PermissionUtil
import kr.co.vilez.util.setOnSingleClickListener
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.awaitResponse
import java.io.File
import java.text.SimpleDateFormat
import java.util.*

private const val TAG = "빌리지_ShareWriteActivity"
class ShareWriteActivity : AppCompatActivity() {
    private lateinit var binding:ActivityShareWriteBinding
    
    private var boardType:Int = 0 // BOARD_TYPE_SHARE 이면 공유글, BOARD_TYPE_ASK 면 요청글

    private var imgMultiPart = mutableListOf<MultipartBody.Part>()
    private var sDay:String?= null
    private var eDay:String?= null
    private var category:String? = null
    private var lat:String? = null
    private var lng:String? = null
    private var place:String?=null // 주소 한글 확인용

    private var editBoardId = 0

    val imgList = mutableListOf<Uri>()

    lateinit var imageAdapter: BoardWriteImageAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
         binding = DataBindingUtil.setContentView(this, R.layout.activity_share_write)
        binding.activity = this

        initBoardType()
        initToolBar()
        initView()
    }

    // 게시글 작성인지, 요청글 작성인지, 게시글 수정인지, 요청글 수정인지 구분
    private fun initBoardType() {
        binding.title = intent.getStringExtra("title") // toolbar title
        editBoardId = intent.getIntExtra("boardId", 0)
        boardType = intent.getIntExtra("type", 0)
        when(boardType) {
            0 -> {
                Toast.makeText(this@ShareWriteActivity, "불러올 수 없습니다.", Toast.LENGTH_SHORT).show()
                finish()
            }
            BOARD_TYPE_SHARE -> {
                if(editBoardId != 0) {
                    binding.title = "공유글 수정하기"
                    initEditView(BOARD_TYPE_SHARE) // 게시글 수정인 경우 : 기존에 입력된 내용 가져오기
                } else binding.title = "공유글 작성하기"
            }
            BOARD_TYPE_ASK -> {
                if(editBoardId != 0) {
                    binding.title = "요청글 수정하기"
                    initEditView(BOARD_TYPE_ASK) // 게시글 수정인 경우 : 기존에 입력된 내용 가져오기
                } else binding.title = "요청글 작성하기"
            }
        }
    }

    private fun initToolBar() {
        this.setSupportActionBar(binding.toolbar)
        this.supportActionBar?.setDisplayShowTitleEnabled(false) // 기본 타이틀 제거
    }

    private fun initEditView(type: Int) {
        Log.d(TAG, "initEditView: 수정하기!!!!")

        when(type) {
            BOARD_TYPE_SHARE -> {
                CoroutineScope(Dispatchers.Main).launch {
                    val result = ApplicationClass.hShareApi.getBoardDetail(editBoardId).awaitResponse().body()
                    if(result?.flag == "success") { // 기존 데이터 불러오기 성공
                        val detail = result.data[0]
                        Log.d(TAG, "initEditView: 기존 데이터 불러오기 성공 : result: $detail")
                        sDay = detail.startDay
                        eDay = detail.endDay
                        category = detail.category
                        lat = detail.hopeAreaLat
                        lng = detail.hopeAreaLng
                        place = detail.address

                        binding.etTitle.setText(detail.title)
                        binding.etContent.setText(detail.content)
                        binding.tvCategory.text = detail.category
                        binding.tvAddr.text = detail.address
                        binding.tvDate.text = "$sDay ~ $eDay"

                    } else {
                        Toast.makeText(this@ShareWriteActivity, "불러올 수 없는 게시글입니다.", Toast.LENGTH_SHORT).show()
                        finish()
                    }
                }
            }
            BOARD_TYPE_ASK -> {
                CoroutineScope(Dispatchers.Main).launch {
                    val result = ApplicationClass.hAskApi.getBoardDetail(editBoardId).awaitResponse().body()
                    if(result?.flag == "success") { // 기존 데이터 불러오기 성공
                        val detail = result.data[0]
                        Log.d(TAG, "initEditView: 기존 데이터 불러오기 성공 : result: $detail")
                        sDay = detail.startDay
                        eDay = detail.endDay
                        category = detail.category
                        lat = detail.hopeAreaLat
                        lng = detail.hopeAreaLng
                        place = detail.address

                        binding.etTitle.setText(detail.title)
                        binding.etContent.setText(detail.content)
                        binding.tvCategory.text = detail.category
                        binding.tvAddr.text = detail.address
                        binding.tvDate.text = "$sDay ~ $eDay"
                    } else {
                        Toast.makeText(this@ShareWriteActivity, "불러올 수 없는 게시글입니다.", Toast.LENGTH_SHORT).show()
                        finish()
                    }
                }
            }
        }


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

        binding.btnFinish.setOnClickListener {
            savePost(it)
        }
//        binding.btnFinish.setOnSingleClickListener { // 3초 이내 재클릭 불가
//            savePost(it)
//        }
    }

    @SuppressLint("SetTextI18n")
    fun addImage(view: View) {
        if(imgList.size < 4) {
            PermissionUtil().galleryPermission(true, this@ShareWriteActivity, imageResult)
        } else { // 이미지4개 넘어가면 불가
            Toast.makeText(this@ShareWriteActivity, "더이상 사진을 추가할 수 없습니다.", Toast.LENGTH_SHORT).show()
        }
    }

    private fun openGallery() {
        val intent = Intent(Intent.ACTION_PICK)
        intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true)
        intent.setDataAndType(
            MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*"
        )
        imageResult.launch(intent)
    }

    @SuppressLint("MissingPermission")
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == PermissionUtil.REQ_GALLERY) { // 갤러리 접근 권한
            if (grantResults.isEmpty()) {
                // 권한 창 떴을 때 그냥 끈 경우
            } else if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                openGallery() // 갤러리 열어서 사진 가져오기
            } else { // 사용자가 거부 누른 경우
                for (permission in permissions) {
                    if ("android.permission.ACCESS_FINE_LOCATION" == permission) {
                        val dialog = ConfirmDialog(object: ConfirmDialogInterface {
                            override fun onYesButtonClick(id: String) {
                                val intent = Intent()
                                intent.action = Settings.ACTION_APPLICATION_DETAILS_SETTINGS
                                intent.data = Uri.fromParts("package", packageName, null)
                                startActivity(intent)
                            }
                        }, "동네 설정을 위해 위치 접근을 권한이 필요합니다.\n휴대폰 [설정]-[애플리케이션]-[vilez]에서 위치 권한을 켜주세요.", "")
                        dialog.show(supportFragmentManager, "LocationPermission")
                    }
                }
            }
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
            category = categories[pos]
            binding.tvCategory.text = category
        }
        categoryPicker.show()
    }

    fun onDateClick(view: View) {
        val calendar = Calendar.getInstance(TimeZone.getTimeZone("Asia/Seoul"), Locale.KOREA)
        calendar.time = Date()
        val startToday = calendar.timeInMillis
        calendar.add(Calendar.DATE, 1)
        val endTomorrow = calendar.timeInMillis

        // Build constraints.
        val constraintsBuilder =
            CalendarConstraints.Builder()
                .setStart(startToday)

        val datePicker =
            MaterialDatePicker.Builder.dateRangePicker()
                .setTitleText("공유기간 선택")
                .setSelection(androidx.core.util.Pair(
                    startToday
                    , endTomorrow))
                .setCalendarConstraints(constraintsBuilder.build())
                .build()
        datePicker.addOnPositiveButtonClickListener {
            val pickedDate = getDate(it.first, it.second)
            sDay = pickedDate.first
            eDay = pickedDate.second
        }
        datePicker.show(supportFragmentManager, "Date")
    }

    @SuppressLint("SetTextI18n")
    fun getDate(startMills: Long, endMills: Long): Pair<String, String> {
        val dateFormat = "yyyy-MM-dd"
        // Create a DateFormatter object for displaying date in specified format.
        val formatter = SimpleDateFormat(dateFormat)
        // Create a calendar object that will convert the date and time value in milliseconds to date.
        val calendar = Calendar.getInstance(TimeZone.getTimeZone("Asia/Seoul"))
        calendar.timeInMillis = startMills
        val startDay = formatter.format(calendar.time)
        calendar.timeInMillis = endMills
        val endDay = formatter.format(calendar.time)

        binding.tvDate.text = "$startDay ~ $endDay"
        return Pair(startDay, endDay)
    }

    fun onPlaceClick(view: View) {
        val intent = Intent(this@ShareWriteActivity, PlacePickerActivity::class.java)
        if(editBoardId != 0) { // 수정하기인 경우 : 이전 희망 장소 찍어둬야 함
            intent.putExtra("lat", lat?.toDouble())
            intent.putExtra("lng", lng?.toDouble())
        }
        requestLauncher.launch(intent)
    }

    val getContent = registerForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
        // Handle the returned Uri
    }

    @SuppressLint("ResourceAsColor")
    private val requestLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
        if(it.resultCode== RESULT_OK) {
            place = it.data?.getStringExtra("address") as String
            lat = it.data?.getStringExtra("lat") as String
            lng = it.data?.getStringExtra("lng") as String
            //Toast.makeText(this@ShareWriteActivity, "$place 가져오기 성공!", Toast.LENGTH_SHORT).show()
            binding.tvAddr.text = place
        }
    }

    fun savePost(view: View) {
        Log.d(TAG, "savePost: 완료버튼 클릭)")
        Toast.makeText(this@ShareWriteActivity, "완료 벝느 클릭", Toast.LENGTH_SHORT).show()
        view.isClickable = false
        view.isEnabled = false
        if (binding.etTitle.text.toString().isEmpty()) {
        } else if (binding.etContent.text.toString().isEmpty()) {
            Snackbar.make(view, "내용을 입력해주세요.", Snackbar.LENGTH_SHORT).show();
        } else if (imgList.size < 1) {
            Snackbar.make(view, "이미지를 1개 이상 등록해주세요.", Snackbar.LENGTH_SHORT).show()
        } else if(category == null) {
            Snackbar.make(view, "카테고리를 선택해주세요.", Snackbar.LENGTH_SHORT).show();
        } else if ((sDay== null) or (eDay== null)) {
            Snackbar.make(view, "희망 공유 기간을 선택해주세요.", Snackbar.LENGTH_SHORT).show();
        } else if ((lat == null) or (lng == null)) {
            Snackbar.make(view, "희망 공유 장소를 선택해주세요.", Snackbar.LENGTH_SHORT).show()
        } else {
            Log.d(TAG, "savePost: 이제 글 POST/PUT 요청 시작하기")

            // 저장중 다이얼로그 띄우기
            val progressDialog = ProgressDialog(this@ShareWriteActivity, "게시글을 저장하고 있습니다.")
            progressDialog.isCancelable = false // 임의로 닫을 수 없다
            progressDialog.show(supportFragmentManager, "BoardPostStart")

            // 이미지 리스트 multipart list에 넣기
            makeMultiPartList()

            Log.d(TAG, "savePost: startDay : $sDay , endDay: $eDay")

            val writeBoard = WriteBoard(id=editBoardId, category= category!!, startDay = sDay!!, endDay = eDay!!,
                hopeAreaLat = lat!!, hopeAreaLng = lng!!, title = binding.etTitle.text.toString(),
                content = binding.etContent.text.toString(), userId = ApplicationClass.prefs.getId(),
                address = place!!)

            when(boardType) { // 공유 글쓰기, 요청 글쓰기 구분
                BOARD_TYPE_SHARE -> { // 공유 글쓰기인 경우
                    CoroutineScope(Dispatchers.Main).launch {
                        val result = if(editBoardId != 0) {
                            Log.d(TAG, "savePost: 수정 시작합니닷 editBoardId: $editBoardId , 수정한 보드 정보 : $writeBoard")
                            ApplicationClass.hShareApi.putShareBoard(writeBoard, imgMultiPart).awaitResponse().body()
                        } else {
                            ApplicationClass.hShareApi.postShareBoard(writeBoard, imgMultiPart).awaitResponse().body()
                        }
                        Log.d(TAG, "savePost: 게시글 작성/수정 결과 : $result")

                        if(result?.flag == "success") {
                            progressDialog.dismiss()
                            Toast.makeText(this@ShareWriteActivity, if(editBoardId!=0) "게시글 수정을 완료했습니다." else "게시글 작성을 완료했습니다.", Toast.LENGTH_SHORT).show()
                            val boardId = if(editBoardId != 0) editBoardId else result.data[0].id
                            Log.d(TAG, "savePost: 작성/수정 성공!!!! 새로 작성/수정된 게시글 id: ${boardId}")
                            moveToDetailActivity(BOARD_TYPE_SHARE, boardId) // 게시글 상세보기로 이동
                        } else {
                            progressDialog.dismiss()
                            Toast.makeText(this@ShareWriteActivity, if(editBoardId!=0) "게시글 수정을 실패했습니다." else "게시글 작성을을 실패했습니다.", Toast.LENGTH_SHORT).show()
                        }
                    }
                }
                BOARD_TYPE_ASK -> { // 요청 글쓰기인 경우
                    CoroutineScope(Dispatchers.Main).launch {
                        val result = if(editBoardId != 0) {
                            Log.d(TAG, "savePost: 수정 시작합니닷 editBoardId: $editBoardId , 수정한 보드 정보 : $writeBoard")
                            ApplicationClass.hAskApi.putBoard(writeBoard, imgMultiPart).awaitResponse().body()
                        } else {
                            Log.d(TAG, "savePost: 게시글 작성 시작한닷")
                            ApplicationClass.hAskApi.postBoard(writeBoard, imgMultiPart).awaitResponse().body()
                        }
                        Log.d(TAG, "savePost: 게시글 작성/수정 결과 : $result")
                        if(result?.flag == "success") {
                            progressDialog.dismiss()
                            Toast.makeText(this@ShareWriteActivity, if(editBoardId!=0) "게시글 수정을 완료했습니다." else "게시글 작성을 완료했습니다.", Toast.LENGTH_SHORT).show()
                            val boardId = if(editBoardId != 0) editBoardId else result.data[0].id
                            Log.d(TAG, "savePost: 작성/수정 성공!!!! 새로 작성/수정된 게시글 id: ${boardId}")
                            moveToDetailActivity(BOARD_TYPE_ASK, boardId) // 게시글 상세보기로 이동
                        } else {
                            progressDialog.dismiss()
                            Toast.makeText(this@ShareWriteActivity, if(editBoardId!=0) "게시글 수정을 실패했습니다." else "게시글 작성을을 실패했습니다.", Toast.LENGTH_SHORT).show()
                        }
                    }
                }
            }
        }
        view.isClickable = true
        view.isEnabled = true
    }

    /**
     * @param type : 공유게시글(BOARD_TYPE_SHARE), 요청게시글(BOARD_TYPE_ASK)
     * @param boardId : 작성한 게시글 id
     */
    fun moveToDetailActivity(type:Int, boardId: Int) {
        // 게시글 상세보기로 이동
        var intent: Intent? = null
        when(type) {
            BOARD_TYPE_SHARE -> {
                intent = Intent(this@ShareWriteActivity, ShareDetailActivity::class.java)
            }
            BOARD_TYPE_ASK -> {
                intent = Intent(this@ShareWriteActivity, AskDetailActivity::class.java)
            }
        }
        intent?.putExtra("userId", ApplicationClass.prefs.getId())
        intent?.putExtra("boardId", boardId)

        startActivity(intent)
        finish()
    }


    private fun makeMultiPartList() {
        for (img in imgList) {
            val file = File(ChangeMultipartUtil().changeAbsolutelyPath(img, this@ShareWriteActivity))
            val requestFile = RequestBody.create("image/*".toMediaTypeOrNull(), file)
            val body = MultipartBody.Part.createFormData("image", file.name, requestFile)
            imgMultiPart.add(body)
        }

    }
}
