package kr.co.vilez.ui.ask

import androidx.paging.PagingSource
import androidx.paging.PagingState
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import kr.co.vilez.data.dto.BoardListDto
import kr.co.vilez.service.RetrofitAskService
import retrofit2.HttpException
import java.io.IOException

//class AskBoardInfo(
//    private val searchString: String,
//    private val ioDispatcher: CoroutineDispatcher,
//    private val retrofitAskService: RetrofitAskService,
//): PagingSource<Int, BoardListDto>() {
//
//    override fun getRefreshKey(state: PagingState<Int, BoardListDto>): Int? {
//        return state.anchorPosition?.let { anchorPosition ->
//            state.closestPageToPosition(anchorPosition)?.prevKey?.plus(1)
//                ?: state.closestPageToPosition(anchorPosition)?.nextKey?.minus(1)
//        }
//    }
//
//    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, BoardListDto> {
//        return try {
//            val page = params.key ?: 1
//
//            val response = withContext(ioDispatcher) {
//                retrofitAskService.boardList()
//
//
//            }
//
//            val bookList = response.body()?.documents?.toBookInfoList() ?: listOf()
//            val prevKey = if (page == 1) null else page - 1
//            val nextKey = if (bookList.isEmpty() || response.body()?.meta?.isEnd == true) null else page + 1
//
//            LoadResult.Page(
//                data = bookList,
//                prevKey = prevKey,
//                nextKey = nextKey
//            )
//
//        } catch (exception: IOException) {
//            return LoadResult.Error(exception)
//        } catch (exception: HttpException) {
//            return LoadResult.Error(exception)
//        }
//    }
//    }
//
//}