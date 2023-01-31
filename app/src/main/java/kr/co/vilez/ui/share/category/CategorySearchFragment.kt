package kr.co.vilez.ui.share.category

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import kr.co.vilez.R
import kr.co.vilez.databinding.FragmentCategorySearchBinding

private const val ARG_PARAM1 = "category"


class CategorySearchFragment : Fragment() {
    private var category: String? = null
    private lateinit var binding: FragmentCategorySearchBinding
    private lateinit var activity: MenuCategoryActivity
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            category = it.getString(ARG_PARAM1)
        }
        activity = context as MenuCategoryActivity
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_category_search, container, false)
        binding.fragment = this
        initToolBar()
        return binding.root
    }

    private fun initToolBar() {
        activity.setSupportActionBar(binding.toolbar)
        activity.supportActionBar?.setDisplayShowTitleEnabled(false) // 기본 타이틀 제거
        binding.title = category
    }

    fun onBackPressed(view: View) {
        activity.supportFragmentManager.popBackStack()
    }

    companion object {
        @JvmStatic
        fun newInstance(category: String) =
            CategorySearchFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, category)
                }
            }
    }
}