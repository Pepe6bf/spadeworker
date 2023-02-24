package site.devtown.spadeworker.domain.auth.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import site.devtown.spadeworker.domain.auth.service.JwtService;
import site.devtown.spadeworker.global.factory.YamlPropertySourceFactory;
import site.devtown.spadeworker.global.response.CommonResult;
import site.devtown.spadeworker.global.response.ResponseService;
import site.devtown.spadeworker.global.response.SingleResult;
import site.devtown.spadeworker.global.util.CookieUtil;
import site.devtown.spadeworker.global.util.HeaderUtil;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

import static org.springframework.http.HttpStatus.OK;

@RequiredArgsConstructor
@PropertySource(
        value = "classpath:/jwt.yml",
        factory = YamlPropertySourceFactory.class
)
@RequestMapping("/api/auth")
@RestController
public class AuthController {

    private final JwtService jwtService;
    private final ResponseService responseService;

    @Value("${jwt.expiry.refresh-token-expiry}")
    private long refreshTokenExpiry;

    private final static String REFRESH_TOKEN = "refresh_token";

    @GetMapping("/refresh")
    public SingleResult<String> reissueToken(
            HttpServletRequest request,
            HttpServletResponse response
    ) {
        // access token 확인
        String accessToken = HeaderUtil.getAccessToken(request);
        // refresh token 확인
        String refreshToken = CookieUtil.getCookie(request, REFRESH_TOKEN)
                .map(Cookie::getValue)
                .orElse((null));

        Map<String, String> newTokens = jwtService.tokenReissue(accessToken, refreshToken);

        int cookieMaxAge = (int) refreshTokenExpiry / 60;
        CookieUtil.deleteCookie(request, response, REFRESH_TOKEN);
        CookieUtil.addCookie(
                response,
                REFRESH_TOKEN,
                newTokens.get("refreshToken"),
                cookieMaxAge
        );

        return responseService.getSingleResult(
                OK.value(),
                "재발급되었습니다.",
                newTokens.get("accessToken")
        );
    }

    @GetMapping("/logout")
    public CommonResult logout(
            HttpServletRequest request
    ) {
        // 쿠키에 Refresh Token 삭제
        jwtService.deleteRefreshToken(
                HeaderUtil.getAccessToken(request)
        );

        return responseService.getSuccessResult(
                OK.value(),
                "로그아웃 되었습니다."
        );
    }
}
