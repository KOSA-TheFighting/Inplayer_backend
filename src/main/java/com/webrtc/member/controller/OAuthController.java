package com.webrtc.member.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.webrtc.member.model.dto.MemberDTO;
import com.webrtc.member.model.vo.KakaoProfile;
import com.webrtc.member.model.vo.OAuthToken;
import com.webrtc.member.service.MemberService;
import com.webrtc.util.JWTUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDateTime;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
@Slf4j
public class OAuthController {

	//    private final LoginService loginService;
	//    private final KakaoOAuth2Util kakaoOAuth2Util;
	//    private static final Logger logger = LoggerFactory.getLogger(OAuthController.class);
	private final JWTUtil jwtUtil; // JWTUtil 주입
	private final MemberService memberService;

	//    @Value("${kakao.client.id}")
	//    private String clientId;
	//
	//  @Value("${kakao.redirect.uri}")
	//    private String redirectUri;
	//
	//    @Value("${kakao.client.secret}")
	//    private String clientSecret;

	/**
	 * 카카오 로그인 요청
	 */
	//    @GetMapping("/kakao")
	//    public String kakaoConnect() {
	//        StringBuilder url = new StringBuilder();
	//        url.append("https://kauth.kakao.com/oauth/authorize?");
	//        url.append("client_id=").append(clientId);
	//        url.append("&redirect_uri=").append(redirectUri);
	//        url.append("&response_type=code");
	//
	//        logger.info("Redirecting to Kakao OAuth URL: {}", url);
	//
	//        return "redirect:" + url.toString();
	//    }

	/**
	 * 카카오 로그인 콜백 처리
	 */
	@GetMapping("/kakao/oauth/callback")
	public ResponseEntity<Map<String, String>> kakaoCallback(@RequestParam String code) {
		System.out.println("카카오 코드: " + code);

		// POST방식으로 key=value 데이터를 요청 (카카오쪽으로)
		// Retrofit2
		// OkHttp
		// RestTemplate
		RestTemplate restTemplate = new RestTemplate();

		// HttpHeader 오브젝트 생성
		HttpHeaders headers = new HttpHeaders();
		headers.add("Content-type", "application/x-www-form-urlencoded;charset=utf-8");

		// HttpBody 오브젝트 생성
		MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
		params.add("grant_type", "authorization_code");
		params.add("client_id", "1f41a5343db15932a5f2d5c4e5c81222");
		params.add("redirect_uri", "http://localhost:5173/oauth/kakao");
		params.add("code", code);

		// HttpHeader와 HttpBody를 하나의 오브젝트에 담기
		HttpEntity<MultiValueMap<String, String>> kakaoTokenRequest = new HttpEntity<>(params, headers);

		// Http 요청하기 - Post방식으로 - 그리고 response 변수의 응답 받음.
		ResponseEntity<String> response = restTemplate.exchange(
				"https://kauth.kakao.com/oauth/token",
				HttpMethod.POST,
				kakaoTokenRequest,
				String.class
				);

		//응답 결과 body 출력
		log.info("https://kauth.kakao.com/oauth/token의 body = {}", response.getBody());

		// Gson, Json Simple, ObjectMapper
		ObjectMapper objectMapper = new ObjectMapper();
		OAuthToken oauthToken = null;
		try {
			oauthToken = objectMapper.readValue(response.getBody(), OAuthToken.class);
		} catch (JsonMappingException e) {
			e.printStackTrace();
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		}

		log.info("카카오 엑세스 토큰 : {}", oauthToken.getAccess_token());
		// 2. POST 방식으로 사용자 정보 요청
		RestTemplate rt2 = new RestTemplate();

		// HttpHeader 오브젝트 생성
		HttpHeaders headers2 = new HttpHeaders();
		headers2.add("Authorization", "Bearer " + oauthToken.getAccess_token());
		headers2.add("Content-type", "application/x-www-form-urlencoded;charset=utf-8");

		// HttpHeader와 HttpBody를 하나의 오브젝트에 담기
		HttpEntity<MultiValueMap<String, String>> kakaoProfileRequest2 = new HttpEntity<>(headers2);

		// Http 요청하기 - Post방식으로 - 그리고 response 변수의 응답 받음.
		ResponseEntity<String> response2 = rt2.exchange(
				"https://kapi.kakao.com/v2/user/me",
				HttpMethod.POST,
				kakaoProfileRequest2,
				String.class
				);
		log.info("getBody = {}", response2.getBody());

		ObjectMapper objectMapper2 = new ObjectMapper();
		KakaoProfile kakaoProfile = null;
		try {
			kakaoProfile = objectMapper2.readValue(response2.getBody(), KakaoProfile.class);
		} catch (JsonMappingException e) {
			e.printStackTrace();
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		}
		log.info("kakaoProfile = {}", kakaoProfile);

		// User 오브젝트 : username, password, email
		//System.out.println("카카오 아이디(번호) : "+kakaoProfile.getId());
		//System.out.println("카카오 이메일 : " + kakaoMember.getMember_Email());


		//JWT에 추가할 정보로 아이디가 있는 Map 객체를 생성한다
		final String email = kakaoProfile.kakao_account.email;
		final Map<String, Object> claim = Map.of("email", email);

		//가입자 혹은 비가입자 체크 해서 처리
		try {
			MemberDTO kakaoMember = MemberDTO.builder()
					.member_id(String.valueOf(kakaoProfile.getId()))
					.member_name(kakaoProfile.kakao_account.profile.nickname)
					.member_nickname(kakaoProfile.kakao_account.profile.nickname)
					.member_email(email)
					.member_created_date(String.valueOf(LocalDateTime.now()))
//                    .member_delete_yn(0)
					.member_last_login(String.valueOf(LocalDateTime.now()))
//                    .member_last_logout(null)
					.build();
			if ( null == memberService.findMemberEmail(email)) {
				memberService.saveMember(kakaoMember);
				System.out.println("기존 회원이 아니기에 자동 회원가입을 진행함");
			} else {
				System.out.println("기존에 회원 가입되어있으므로 다음으로 진행함");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		Map<String, String> keyMap = Map.of("accessToken", jwtUtil.generateToken(claim, 1), //Access Token 유효기간 1일로 생성
				"refreshToken", jwtUtil.generateToken(claim, 10),  //Refresh Token 유효기간 10일로 생성
				"member_id", String.valueOf(kakaoProfile.getId())
				);

		System.out.println("서버에서 생성한 토큰: " + keyMap);
		return  ResponseEntity.ok(keyMap);
	}
}

