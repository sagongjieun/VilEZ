package kr.co.vilez.share.model.dto;

import lombok.Data;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Document("Interesting")
public class InterestingDto {
    String user_id;
    String board;

}
