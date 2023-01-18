package kr.co.vilez.share.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Document("share")
public class ImgPath {
    int boardId;
    String path;
    String fileName;
}
