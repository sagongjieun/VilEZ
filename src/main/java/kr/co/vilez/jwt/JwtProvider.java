package kr.co.vilez.jwt;

public interface JwtProvider {
    public String createExpireRefreshToken(int userId, String userNickname);
    public String createToken(int userId, String userNickname);
    public String createRefreshToken(int userId, String userNickname);
    public String createExpireToken(int userId, String userNickname);
    public String getUserId(String token);
    public String getUserNickname(String token);
    public String getExp(String token);
    public boolean validateToken(String jwtToken);
}
