package kr.co.vilez.ui.share

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import kr.co.vilez.R
import kr.co.vilez.databinding.FragmentShareBinding
import kr.co.vilez.ui.MainActivity

private const val TAG = "빌리지_ShareFragment"
class ShareFragment : Fragment() {
    private lateinit var binding: FragmentShareBinding
    private lateinit var mainActivity: MainActivity
    private lateinit var shareAdapter: FragmentShareAdapter

    private lateinit var shareDatas: ArrayList<ShareData>
    private var index = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // context가 필요할때는 이거 갖다쓰면 됨
        mainActivity = context as MainActivity
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // findViewById 할 필요없이 binding.버튼Id, binding.텍스트뷰Id 로 바로 접근해서 사용 가능!
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_share, container, false)
        binding.fragment = this
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        
        // 뷰 생성 후 초기화
        initView()
    }

    fun initView() {
        // 데이터 가져오기
        shareDatas = arrayListOf()

        // 어댑터 생성
        shareAdapter = FragmentShareAdapter(shareDatas)
        shareAdapter.setItemClickListener(object: FragmentShareAdapter.OnItemClickListener{
            // listview item 클릭시 실행할 메소드
            override fun onClick(view: View, position: Int) {
                Log.d(TAG, "onClick: ${shareDatas[position].tv_name} clicked!")
            }
        })

        // 리사이클러뷰에 어댑터 등록
        binding.rv.apply {
            adapter = shareAdapter
            layoutManager = LinearLayoutManager(mainActivity, LinearLayoutManager.VERTICAL, false)
        }

        binding.btnAdd.setOnClickListener { // 방법1 : 버튼에 직접 클릭이벤트 리스너 달기 (이거 쓰려면 xml에 onClick 지워야함)
            Log.d(TAG, "initView: 클릭 ")
            val shareData = ShareData(R.drawable.img_default_profile, "test${index++}")
            shareDatas.add(shareData)
             shareAdapter.notifyItemInserted(index-1) // 이게 아래코드보다 오버헤드 더 적음
            // shareAdapter.notifyDataSetChanged()
        }

    }

    
    fun addItem(view:View) { // 방법 2 : xml의 버튼에 onClick속성으로 addItem 메소드 넣기
        // 클릭 리스너 달아줘도 되고, xml에 선언해놓은 이 메소드에서 버튼 클릭 이벤트 동작 됨
        Log.d(TAG, "addItem: 버튼 클릭")
        val shareData = ShareData(R.drawable.img_default_profile, "test${index++}")
        shareDatas.add(shareData)
        shareAdapter.notifyItemInserted(index-1) // 이게 아래코드보다 오버헤드 더 적음
    }
}