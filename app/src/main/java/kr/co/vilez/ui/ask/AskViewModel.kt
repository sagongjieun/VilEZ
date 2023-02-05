package kr.co.vilez.ui.ask

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kr.co.vilez.data.dto.BoardListDto
import kr.co.vilez.util.ApplicationClass
import kr.co.vilez.util.Common
import retrofit2.awaitResponse
//
//private const val TAG = "빌리지_요청_AskViewModel"
//class AskViewModel : ViewModel() {
//    private val _itemList = MutableLiveData<ArrayList<BoardListDto>>()
//
//    val itemList: LiveData<ArrayList<BoardListDto>> get() = _itemList
//
//    init {
//        val recyclerViewItems: ArrayList<BoardListDto> = ArrayList()
//        var num = 0
//        var max = 10
//        CoroutineScope(Dispatchers.Main).launch {
//            val result = ApplicationClass.retrofitAskService.boardList(
//                    num++,
//                    0,
//                    max,
//                    ApplicationClass.prefs.getId()
//                ).awaitResponse().body()
//            if (result?.flag == "success") {
//                Log.d(TAG, "initList: result: $result")
//                if(result.data.isEmpty()) {
//                    // TODO : 데이터 없다고 띄우기
//                    return@launch
//                }
//                for (data in result.data) {
//                    val askData = BoardListDto(
//                        data.askDto.id,
//                         if (data.askDto.list.isNullOrEmpty()) Common.DEFAULT_PROFILE_IMG else data.askDto.list[0].path,
//                        data.askDto.title,
//                         data.askDto.date,
//                         "",
//                         data.askDto.startDay+"~"+data.askDto.endDay,
//                         Integer.toString(data.listCnt),
//                         data.askDto.state,
//                         data.askDto.userId
//                    )
//                    Log.d(TAG, "추가?: $askData")
//                    recyclerViewItems.add(askData)
//                }
//            }
//            Log.d(TAG, "추가완료: items: $recyclerViewItems")
//            _itemList.value = recyclerViewItems
//        }
//
//    }
//
//    fun setItemList(recyclerViewItems: ArrayList<BoardListDto>) {
//        _itemList.value = recyclerViewItems
//    }
//}