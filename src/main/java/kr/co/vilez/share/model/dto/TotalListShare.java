package kr.co.vilez.share.model.dto;

import lombok.Data;

import java.util.List;

@Data
public class TotalListShare {
    ShareListDto shareListDto;
    List<BookmarkDto> list;

}
