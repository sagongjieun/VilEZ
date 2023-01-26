package kr.co.vilez.ask.model.dto;

import lombok.Data;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Document("ask")
public class ImgPath2 {
    private int boardId;
    private String fileName;
    private String path;
}
