package kr.co.vilez.jwt;

public interface JwtProvider {
    public String createToken(String userId, String userNickname);
    public String createRefreshToken(String userId, String userNickname);
    public String createExpireToken();
    public String getUserId(String token);
    public String getUserNickname(String token);
    public String getExp(String token);
    public boolean validateToken(String jwtToken);
}
