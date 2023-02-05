package kr.co.vilez.ui.share.write

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import kr.co.vilez.R
import kr.co.vilez.databinding.ActivityPlacePickerBinding

private const val TAG = "빌리지_지도마커_PlacePickerActivity"
class PlacePickerActivity : AppCompatActivity(), HopePlaceInterface {
    private lateinit var binding:ActivityPlacePickerBinding
    private lateinit var fragment:HopePlaceFragment
    private lateinit var pickedAddress: String
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_place_picker)

        fragment = HopePlaceFragment().apply {
            this.listener = this@PlacePickerActivity
        }

        supportFragmentManager.beginTransaction()
            .add(R.id.frame_layout_place, fragment)
            .commit()
    }

    override fun setAddress(address: String) {
        pickedAddress = address
    }

    override fun getAddress(): String {
        return pickedAddress
    }


}