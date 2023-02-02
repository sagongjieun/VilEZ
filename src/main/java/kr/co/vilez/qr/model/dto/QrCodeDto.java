package kr.co.vilez.qr.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class QrCodeDto {
    int userId;
    String access_token;
    String refresh_token;
}
