package kr.co.vilez.fcm.model.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;

@Builder
@AllArgsConstructor
@Getter
@Setter
public class FCMMessage {
    private ArrayList<String> tokenList;
    private String title;
    private String body;
}
