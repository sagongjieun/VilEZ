package kr.co.vilez.ui.share.category

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import kr.co.vilez.R
import kr.co.vilez.databinding.ActivityMenuCategoryBinding

class MenuCategoryActivity : AppCompatActivity() {
    private lateinit var binding:ActivityMenuCategoryBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_menu_category)


        supportFragmentManager.beginTransaction()
            .replace(R.id.frame_layout_menu_category, CategoryFragment())
            .commit()
    }

    fun changeFragment(category: String) {
        supportFragmentManager.beginTransaction()
            .add(R.id.frame_layout_menu_category, CategorySearchFragment.newInstance(category))
            .addToBackStack(null)
            .commit()
    }
}