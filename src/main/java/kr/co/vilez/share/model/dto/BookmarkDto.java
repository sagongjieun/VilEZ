package kr.co.vilez.share.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Document("bookmark")
public class BookmarkDto {
    int boardId;
    int userId;
}
