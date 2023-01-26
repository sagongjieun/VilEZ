package kr.co.vilez.appointment.model.vo;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.data.mongodb.core.mapping.Document;

@Getter
@Setter
@ToString
@Document("KakaoMap")
public class MapVO {
    private String roomId;
    private int type;
    private double lat;
    private double lng;
    private int zoomLevel;
    public boolean isMarker;
}
