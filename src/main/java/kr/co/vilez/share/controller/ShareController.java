package kr.co.vilez.share.controller;

import io.swagger.annotations.ApiOperation;
import kr.co.vilez.data.HttpVO;
import kr.co.vilez.share.model.dto.BookmarkDto;
import kr.co.vilez.share.model.dto.PageNavigator;
import kr.co.vilez.share.model.dto.ShareDto;
import kr.co.vilez.share.model.service.ShareService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RequestMapping("/shareboard")
@RestController
@RequiredArgsConstructor
@Log4j2
public class ShareController {
    HttpVO httpVO = null;
    final ShareService shareService;

    @GetMapping("/best")
    @ApiOperation(value = "북마크 수와 카테고리에 따른 베스트 게시글 3개의 정보를 주는 API")
    public ResponseEntity<?> bestBoardList(@PathVariable String category,
                                           @PathVariable int userId,
                                           @PathVariable int boardId){
        httpVO = new HttpVO();

        try{
            shareService.getBestList(category, userId, boardId);
        } catch (Exception e){
            e.printStackTrace();
        }

        return new ResponseEntity<HttpVO>(httpVO, HttpStatus.OK);
    }

    @GetMapping
    @ApiOperation(value = "전체 글 리스트를 보내준다" ,
            notes = "List에 dto 담아서 리턴" +
                    "\n \t 현재 받고자 하는 page number를 넣어 보내줘야한다." +
                    "\n \t cnt에는 보고싶은 page 번호" +
                    "\n \t high에는 보고싶은 리스트의 총 갯수" +
                    "\n \t 그리고 검색하고자 하는 목록을 word를 통해 보내면 해당 키워드를 통해 검색이 가능하다" +
                    "\n \t 그리고 검색하고자 하는 카테고리 목록을 category를 통해 보내면 해당 키워드를 통해 검색이 가능하다" +
                    "\n \t 원하지 않은 검색은 그대로 null로 보내면 된다." +
                    "\n \t 좌표 정보 꼭 보내주셔야합니다. 이제는 좌표 정보를 토대로 해당 범위 안에 있는 게시글만 보여줍니다.")
    public ResponseEntity<?> boardList(PageNavigator pageNavigator){
        httpVO = new HttpVO();
        try {
            httpVO = shareService.loadShareList(pageNavigator);
            httpVO.setFlag("success");
        } catch (Exception e){
            e.printStackTrace();
            log.warn("목록 조회중 오류 발생");
        }
        return new ResponseEntity<HttpVO>(httpVO, HttpStatus.OK);
    }

    @PostMapping
    @ApiOperation(value = "글을 작성 한다. (이미지 필수X)" ,
            notes = "{\n \t userId : Number," +
                    "\n \t category : String," +
                    "\n \t title : String," +
                    "\n \t hopeAreaLat : String," +
                    "\n \t hopeAreaLng : String," +
                    "\n \t startDay : String," +
                    "\n \t endDay : String" +
                    "\n \t address : String}")
    public ResponseEntity<?> insert(@RequestPart(value ="board") ShareDto shareDto,
                                    @RequestPart(value="image") List<MultipartFile> files){
        httpVO = new HttpVO();
        try {
            httpVO = shareService.insert(shareDto, files);
            httpVO.setFlag("success");
        } catch (Exception e){
            e.printStackTrace();
        }
        return new ResponseEntity<HttpVO>(httpVO, HttpStatus.OK);
    }

    @PutMapping
    @ApiOperation(value = "글 수정을 한다. (이미지 필수X)" ,
            notes = "{\n \t boardId : Number," +
                    "\n \t category : String," +
                    "\n \t title : String," +
                    "\n \t hopeAreaLat : String," +
                    "\n \t hopeAreaLng : String," +
                    "\n \t startDay : String," +
                    "\n \t endDay : String" +
                    "\n \t }")
    public ResponseEntity<?> update(@RequestPart(value ="board") ShareDto shareDto,
                                    @RequestPart(value="image") List<MultipartFile> files){
        httpVO = new HttpVO();
        try {
            httpVO = shareService.update(shareDto, files);
            httpVO.setFlag("success");
        } catch (Exception e){
            e.printStackTrace();
        }
        return new ResponseEntity<HttpVO>(httpVO, HttpStatus.OK);
    }

    @DeleteMapping("/{boardId}")
    @ApiOperation(value = "글을 삭제 한다.", notes = "Path로 boardId을 넣어준다. \n " +
            "성공시 success")
    public ResponseEntity<?> delete(@PathVariable int boardId){
        httpVO = new HttpVO();
        try{
            shareService.delete(boardId);
            httpVO.setFlag("success");
        } catch (Exception e){
            e.printStackTrace();
        }
        return new ResponseEntity<HttpVO>(httpVO, HttpStatus.OK);
    }

    @GetMapping("/detail/{boardId}")
    @ApiOperation(value = "글의 상세 정보를 불러온다.", notes = "Path로 boardId을 넣어준다. \n " +
            "board 정보를 List로 리턴한다.")
    public ResponseEntity<?> detail(@PathVariable int boardId){
        httpVO = new HttpVO();
        try{
            httpVO = shareService.detail(boardId);
            httpVO.setFlag("success");
        } catch(Exception e){
            e.printStackTrace();
        }
        return new ResponseEntity<HttpVO>(httpVO, HttpStatus.OK);
    }

    @GetMapping("/my/{userId}")
    @ApiOperation(value = "내가 올린 글 목록을 불러온다.", notes = "Path로 userId을 넣어준다." +
            "\n board 정보들을 List로 리턴한다.")
    public ResponseEntity<?> myDetail(@PathVariable int userId){
        httpVO = new HttpVO();
        try {
            httpVO = shareService.loadMyShareList(userId);
            httpVO.setFlag("success");
        } catch(Exception e){
            e.printStackTrace();
        }
        return new ResponseEntity<HttpVO>(httpVO, HttpStatus.OK);
    }

    @GetMapping("/bookmark/my/{userId}")
    @ApiOperation(value = "글쓴이의 북마크 리스트를 불러온다.", notes = "Path로 userId를 넣어준다." +
            "\n 북마크 정보들을 List로 리턴한다.")
    public ResponseEntity<?> myBookmarkList(@PathVariable int userId){
        httpVO = new HttpVO();
        try {
            System.out.println("ddd");
            httpVO = shareService.myBookmarkList(userId);
        }catch (Exception e){
            e.printStackTrace();
        }
        return new ResponseEntity<HttpVO>(httpVO, HttpStatus.OK);
    }
    //bookmark
    @GetMapping("/bookmark/{boardId}")
    @ApiOperation(value = "글번호의 북마크 리스트를 불러온다.", notes = "Path로 boardId를 넣어준다." +
            "\n 북마크 정보들을 List로 리턴한다.")
    public ResponseEntity<?> bookmarkList(@PathVariable int boardId){
        httpVO = new HttpVO();
        try {
            httpVO = shareService.bookmarkList(boardId);
            httpVO.setFlag("success");
        }catch (Exception e){
            e.printStackTrace();
        }
        return new ResponseEntity<HttpVO>(httpVO, HttpStatus.OK);
    }


    @GetMapping("/bookmark/{boardId}/{userId}")
    @ApiOperation(value = "북마크를 눌렀는지 확인한다.", notes = "Path로 boardId를 넣어준다." +
            "\n 북마크 정보들을 List로 리턴한다.")
    public ResponseEntity<?> isBookmark(@PathVariable("boardId") int boardId,
                                        @PathVariable("userId") int userId){
        httpVO = new HttpVO();
        try{
            httpVO = shareService.isBookmark(boardId, userId);
            httpVO.setFlag("success");
        } catch (Exception e){
            e.printStackTrace();
        }
        return new ResponseEntity<HttpVO>(httpVO, HttpStatus.OK);
    }

    @ApiOperation(value = "bookmark 등록", notes = "{boardId : Number , userId : Number}")
    @PostMapping("/bookmark")
    public ResponseEntity<?> addBookmark(@RequestBody BookmarkDto bookmarkDto){
        httpVO = new HttpVO();
        try {
            httpVO = shareService.addBookmark(bookmarkDto.getBoardId(), bookmarkDto.getUserId());
        } catch (Exception e){
            e.printStackTrace();
        }
        return new ResponseEntity<HttpVO>(httpVO, HttpStatus.OK);
    }
    @ApiOperation(value = "bookmark 삭제", notes = "Path로 boardId userId 순으로 넣어준다.")
    @DeleteMapping("/bookmark/{boardId}/{userId}")
    public ResponseEntity<?> deleteBookmark(@PathVariable("boardId") int boardId, @PathVariable("userId") int userId){
        httpVO = new HttpVO();
        try {
            httpVO = shareService.deleteBookmark(boardId, userId);
        } catch (Exception e){
            e.printStackTrace();
        }
        return new ResponseEntity<HttpVO>(httpVO, HttpStatus.OK);
    }

}
