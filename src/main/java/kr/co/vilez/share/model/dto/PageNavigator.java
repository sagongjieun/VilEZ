package kr.co.vilez.share.model.dto;

import lombok.*;

@Data
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class PageNavigator {
    String category;
    String word;
    int cnt;
    int low;
    int high;
}
