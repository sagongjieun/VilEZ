package kr.co.vilez.configuration.interceptor;

import kr.co.vilez.jwt.JwtProvider;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Component
@Log4j2
public class UserInterceptor implements HandlerInterceptor {
    private static final String HEADER_ACCESS = "access-token";
    private static final String HEADER_REFRESH = "refresh-token";

    @Autowired
    public JwtProvider jwtProvider;

    @Override
    public boolean preHandle(HttpServletRequest request,
                             HttpServletResponse response,
                             Object handler) throws Exception{

        if(request.getMethod().equals("OPTIONS")) {
            return true;
        }

        String access_token = request.getHeader(HEADER_ACCESS);
        System.out.println("access_token = " + access_token);
        String refresh_token = request.getHeader(HEADER_REFRESH);
        System.out.println("refresh_token = " + refresh_token);

        // access 토큰 만료로 refresh 토큰이 날아온 경우 controller에서 처리
        if(refresh_token != null){
            String date = jwtProvider.getExp(refresh_token);
            // refresh 토큰이 조작된 경우
            if(date.equals("be manipulated")){
                log.warn("토큰 조작");
                return false;
            }
            //refresh 토큰이 만료되었거나 조작되었다면 access 토큰 재갱신을 막는다.
            if(System.currentTimeMillis() > Long.parseLong(date)) {
                System.out.println("리프레시 토큰 만료!!!!!");
                return false;
            }

            System.out.println("리프레시 토큰 만료 안됨 성공임!!!!!");
            return true;
        }

        // 토큰의 기간이 만료될 경우
        String date = jwtProvider.getExp(access_token);

        // 만료되지 않은 토큰
        if(System.currentTimeMillis() < Long.parseLong(date)){
            System.out.println("에세스 토큰 만료 안됨 성공임!!!!!");
            return true;
        }

        // 토큰이 조작된 경우
        if(date.equals("be manipulated")){
            log.warn("토큰 조작");
            return false;
        }


        System.out.println("에세스 토큰 만료!!!!!");
        response.sendError(401, "UNAUTHORIZATION_ACCESS");
        return false;
    }
}
