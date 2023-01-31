package kr.co.vilez.ui.share.category

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


class CategoryFragment : Fragment() {
    private lateinit var binding:FragmentCategoryBinding
    lateinit var gridAdapter: GridRecyclerViewAdapter
    private lateinit var mContext: Context
    private lateinit var activity: MenuCategoryActivity

    companion object {
        private val categoryList = mutableListOf<Category>(
            Category("화장품/미용", R.drawable.user_default),
            Category("생활/건강", R.drawable.user_default),
            Category("식품", R.drawable.user_default),
            Category("스포츠/레저", R.drawable.user_default),
            Category("가구/인테리어", R.drawable.user_default),
            Category("패션잡화", R.drawable.user_default),
            Category("패션의류", R.drawable.user_default),
            Category("여가/생활편의", R.drawable.user_default),
            Category("도서", R.drawable.user_default),
        )

    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
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
                Toast.makeText(mContext, "클릭 ${categoryList[position].name}",Toast.LENGTH_SHORT).show()
                activity.changeFragment(categoryList[position].name)
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
        binding.title = "카테고리"
    }

    fun onBackPressed(view: View) {
        activity.finish()
    }

}
