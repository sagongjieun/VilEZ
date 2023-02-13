package kr.co.vilez.ui.ask

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kr.co.vilez.R
import kr.co.vilez.data.dto.AskData
import kr.co.vilez.databinding.FragmentAskBinding
import kr.co.vilez.ui.MainActivity
import kr.co.vilez.ui.search.SearchActivity
import kr.co.vilez.ui.search.category.MenuCategoryActivity
import kr.co.vilez.ui.share.write.ShareWriteActivity
import kr.co.vilez.util.ApplicationClass
import kr.co.vilez.util.Common
import kr.co.vilez.util.Common.Companion.BOARD_TYPE_ASK
import retrofit2.awaitResponse


private const val TAG = "빌리지_요청_AskFragment"
class AskFragment : Fragment() {

    //private lateinit var viewModel: AskViewModel
    //private val items = MutableLiveData<ArrayList<BoardListDto>>()

    private var _binding: FragmentAskBinding? = null
    private val binding get() = _binding!!

    private lateinit var activity: MainActivity
    private lateinit var askAdapter: AskListAdapter

    private var items = ArrayList<AskData>()
    private var index = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // context가 필요할때는 이거 갖다쓰면 됨
        activity = context as MainActivity
        setHasOptionsMenu(true)
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = DataBindingUtil.inflate(inflater, R.layout.fragment_ask, container, false)
        binding.activity = activity
        binding.fragment = this
        //initViewModel()
        initToolBar()
        initView()
        if (ApplicationClass.prefs.getLng() != "0.0" && ApplicationClass.prefs.getLat() != "0.0") {
            binding.userLocationValid = true
            initData()
        } else {
            binding.userLocationValid = false
        }
        return binding.root
    }

    @Deprecated("Deprecated in Java")
    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.top_app_bar, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    @Deprecated("Deprecated in Java")
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.option_menu_menu -> {
                Log.d(TAG, "onContextItemSelected: ${item.title} clicked")
                val intent = Intent(activity, MenuCategoryActivity::class.java)
                intent.putExtra("type", BOARD_TYPE_ASK)
                startActivity(intent)
            }
            R.id.option_menu_search -> {
                val intent = Intent(activity, SearchActivity::class.java)
                intent.putExtra("type", BOARD_TYPE_ASK)
                startActivity(intent)
            }
        }
        return super.onOptionsItemSelected(item)
    }
    private fun initToolBar() {
        activity.setSupportActionBar(binding.toolbar)
        activity.supportActionBar?.setDisplayShowTitleEnabled(false) // 기본 타이틀 제거
    }
    private fun initView() {
        binding.lifecycleOwner = this
        binding.fragment = this

        askAdapter = AskListAdapter(items)
        askAdapter.setItemClickListener(object: AskListAdapter.OnItemClickListener{
            override fun onClick(view: View, position: Int) {
                Log.d(TAG, "onClick: ${items[position]}클릭")
            }

        })
        binding.rv.apply {
            adapter = askAdapter
            layoutManager = LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false)
        }
    }

    private fun initData() {
        var num = 0
        var max = 10
        CoroutineScope(Dispatchers.Main).launch {
            val result = ApplicationClass.hAskApi.boardList(num++, 0, max, ApplicationClass.prefs.getId()).awaitResponse().body()
            Log.d(TAG, "initData: result: $result")
            if (result?.flag == "success") {
                Log.d(TAG, "initList: result: $result")
                if(result.data.isEmpty()) {
                    Log.d(TAG, "onViewCreated: 데이터 0개")
                    binding.tvNoArticleMsg.visibility = View.VISIBLE
                }
                for (data in result.data[0]) {
                    val askData = AskData(
                        data.askDto.id,
                        if (data.askDto.list.isNullOrEmpty()) Common.DEFAULT_PROFILE_IMG else data.askDto.list[0].path,
                        data.askDto.title,
                        data.askDto.date,
                        "", // 이거 안쓰기로함
                        data.askDto.startDay+" ~ "+data.askDto.endDay,
                        data.askDto.state,
                        data.askDto.userId
                    )
                    Log.d(TAG, "추가?: $askData")
                    items.add(askData)
                }
            } else {
                Log.d(TAG, "initData: 실패!!")
            }
            Log.d(TAG, "추가완료: items: $items")
            askAdapter.notifyItemInserted(index - 1)
        }

        binding.rv.setOnScrollChangeListener { v, _, _, _, _ ->
            if (!v.canScrollVertically(1)) {
                CoroutineScope(Dispatchers.Main).launch {
                    val result = ApplicationClass.hAskApi.boardList(num++, 0, max, ApplicationClass.prefs.getId()).awaitResponse().body()
                    Log.d(TAG, "initData: result: $result")
                    if (result?.flag == "success") {
                        Log.d(TAG, "initList: result: $result")
                        if(result.data.isEmpty()) {
                            Log.d(TAG, "onViewCreated: 데이터 0개")
                            binding.tvNoArticleMsg.visibility = View.VISIBLE
                        }
                        for (data in result.data[0]) {
                            val askData = AskData(
                                data.askDto.id,
                                if (data.askDto.list.isNullOrEmpty()) Common.DEFAULT_PROFILE_IMG else data.askDto.list[0].path,
                                data.askDto.title,
                                data.askDto.date,
                                "", // 이거 안쓰기로함
                                data.askDto.startDay+" ~ "+data.askDto.endDay,
                                data.askDto.state,
                                data.askDto.userId
                            )
                            Log.d(TAG, "추가?: $askData")
                            items.add(askData)
                        }
                    } else {
                        Log.d(TAG, "initData: 실패!!")
                    }
                }
            }
        }
    }


    fun moveToAskWrite(view: View) {
        val intent = Intent(activity, ShareWriteActivity::class.java)
        intent.putExtra("type", BOARD_TYPE_ASK)
        activity.startActivity(intent)
    }


    private fun initViewModel() {
        //viewModel = ViewModelProvider(requireActivity())[AskViewModel::class.java]
//        viewModel = AskViewModel()
//        binding.model = viewModel
//        binding.lifecycleOwner = this
//        binding.fragment = this
//
//        askAdapter = AskListAdapter(viewModel.itemList)
//        binding.rv.adapter = askAdapter
//
//        val dataObserver: Observer<ArrayList<BoardListDto>> = Observer {
//            items.value = it
//            val adapter = AskListAdapter(items)
//            binding.rv.adapter = adapter
//        }
//        viewModel.itemList.observe(viewLifecycleOwner, dataObserver)
    }


}