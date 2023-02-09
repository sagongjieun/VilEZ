package kr.co.vilez.ui.search.category

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.GridLayoutManager
import kr.co.vilez.R
import kr.co.vilez.databinding.FragmentCalendarBinding
import kr.co.vilez.databinding.FragmentCategoryBinding
import kr.co.vilez.util.Common.Companion.BOARD_TYPE_SHARE

private const val ARG_TYPE = "type"

class CategoryFragment : Fragment() {
    private var type:Int ? = 0

    private lateinit var binding:FragmentCategoryBinding
    lateinit var gridAdapter: GridRecyclerViewAdapter
    private lateinit var mContext: Context
    private lateinit var activity: MenuCategoryActivity

    companion object {

        private val categoryList = mutableListOf<Category>(
            Category("화장품/미용", R.drawable.ic_category_cosmetics),
            Category("생활/건강", R.drawable.ic_category_comfort),
            Category("식품", R.drawable.ic_category_food),
            Category("스포츠/레저", R.drawable.ic_category_sports),
            Category("가구/인테리어", R.drawable.ic_category_furniture),
            Category("패션잡화", R.drawable.ic_category_fashion_stuff),
            Category("패션의류", R.drawable.ic_category_clothing),
            Category("여가/생활편의", R.drawable.ic_category_life),
            Category("도서", R.drawable.ic_category_book),
            Category("디지털/가전", R.drawable.ic_category_digital),
            Category("기타", R.drawable.ic_category_etc),
        )

        @JvmStatic
        fun newInstance(type: Int) =
            CategoryFragment().apply {
                arguments = Bundle().apply {
                    putInt(ARG_TYPE, type)
                }
            }

    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            type = it.getInt(ARG_TYPE)
        }
        activity = context as MenuCategoryActivity
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        mContext = context
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_category, container, false)
        binding.fragment = this
        gridAdapter = GridRecyclerViewAdapter(mContext, categoryList)
        gridAdapter.onItemClickListener = object: GridRecyclerViewAdapter.OnItemClickListener {
            override fun onClick(view: View, position: Int) {
                activity.changeFragment(type!!, categoryList[position].name)
            }
        }
        binding.gridRvCategory.apply {
            adapter = gridAdapter
            layoutManager = GridLayoutManager(mContext, 3)
        }

        initToolBar()
        return binding.root
    }

    private fun initToolBar() {
        activity.setSupportActionBar(binding.toolbar)
        activity.supportActionBar?.setDisplayShowTitleEnabled(false) // 기본 타이틀 제거
        if(type == BOARD_TYPE_SHARE) binding.title = "공유 카테고리" else binding.title = "요청 카테고리"
    }

    fun onBackPressed(view: View) {
        activity.finish()
    }



}
