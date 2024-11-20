package com.webrtc.member.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class LoginService {

//    public void insertMember(MemberVO memberVO) throws Exception {
//        try {
//            if (memberVO == null ||
//                    Objects.isNull(memberVO.getMember_email())) {
//                throw new Exception("아이디는 필수 정보입니다");
//            }
//            MemberVO existMember = memberDAO.findByEmail(memberVO.getMember_email());
//            if (existMember != null && !Objects.isNull(memberVO.getMember_email())) {
//                throw new ExistMemberException(memberVO.getMember_email());
//            }
//        }
//    }
//    //private final JWTUtil jwtUtil;

//    @Value("${kakao.client.id}")
//    private String clientId;
//
//    @Value("${kakao.redirect.uri}")
//    private String redirectUri;
//
//    @Value("${kakao.client.secret}")
//    private String clientSecret;



//    @Autowired
//    private MemberService memberService; // MemberService를 통해 DB 액세스

//    public MemberDTO findMemberEmail(String email) {
//        // MemberService를 통해 이메일로 회원 정보를 조회
//        return memberService.findMemberEmail(email);
//    }

    // 액세스 토큰으로 사용자 정보를 가져오는 메서드
//    public KakaoInfo getKakaoInfo(String accessToken) throws JsonProcessingException {
//        HttpHeaders headers = new HttpHeaders();
//        headers.add("Authorization", "Bearer " + accessToken);
//        headers.add("Content-type", "application/x-www-form-urlencoded;charset=utf-8");
//
//        HttpEntity<MultiValueMap<String, String>> kakaoUserInfoRequest = new HttpEntity<>(headers);
//        RestTemplate rt = new RestTemplate();
//        ResponseEntity<String> response = rt.exchange(
//                "https://kapi.kakao.com/v2/user/me",
//                HttpMethod.POST,
//                kakaoUserInfoRequest,
//                String.class
//        );
//
//        String responseBody = response.getBody();
//        ObjectMapper objectMapper = new ObjectMapper();
//        JsonNode jsonNode = objectMapper.readTree(responseBody);
//
//        String id = jsonNode.get("id").asText();
//        String email = jsonNode.get("kakao_account").get("email").asText();
//        String nickname = jsonNode.get("properties").get("nickname").asText();
//
//        return new KakaoInfo(id, nickname, email);
//    }

    // 카카오 사용자 정보를 바탕으로 가입된 내역 확인 및 필요 시 회원가입 처리
//    public MemberDTO ifNeedKakaoInfo(KakaoInfo kakaoInfo) {
//        String email = kakaoInfo.getEmail();  // KakaoInfo에서 email을 가져옵니다.
//        MemberDTO memberDTO = this.findMemberEmail(email);  // 이메일로 회원 정보 조회
//
//        // 회원 정보가 없는 경우 회원가입 처리
//        if (memberDTO == null) {
//            String nickname = kakaoInfo.getNickname();  // KakaoInfo에서 nickname을 가져옵니다.
//            int idx = email.indexOf("@");  // 이메일에서 @ 앞부분을 사용자 ID로 사용
//            String kakaoId = email.substring(0, idx);  // 사용자 ID 추출
//            String tempPassword = UUID.randomUUID().toString();  // 임시 비밀번호 생성
//
//            MemberDTO registerMember = new MemberDTO();
//            registerMember.setId(kakaoId);  // 카카오 사용자 ID
//            registerMember.setPassword(tempPassword);  // 임시 비밀번호
//            registerMember.setNickname(nickname);  // 카카오 사용자 닉네임
//            registerMember.setEmail(email);  // 카카오 사용자 이메일
//
//            memberService.saveMember(registerMember);  // 회원가입 처리
//            memberDTO = memberService.findMemberEmail(email);  // 새로운 회원 정보 조회
//        }
//
//        return memberDTO;  // 회원 정보 반환
//    }

    // 카카오 로그인 처리를 위한 메서드
//    public MemberDTO processOAuthLogin(String provider, String code, String redirectUri) throws JsonProcessingException {
//        // 1. 인가 코드로 액세스 토큰 요청
//        String accessToken = getAccessToken(code);
//
//        // 2. 액세스 토큰으로 카카오 사용자 정보 요청
//        KakaoInfo kakaoInfo = getKakaoInfo(accessToken);
//
//        // 3. 사용자 정보 기반으로 DB 조회 및 필요 시 회원가입
//        MemberResponse memberResponse = ifNeedKakaoInfo(kakaoInfo);
//
//        // 4. 사용자 정보를 DTO로 반환
//        return new MemberDTO(
//                memberResponse.getMemberId(),
//                memberResponse.getMemberName(),
//                memberResponse.getMemberNickname(),
//                memberResponse.getMemberEmail(),
//                LocalDateTime.now(),
//                0,
//                LocalDateTime.now(),
//                LocalDateTime.now()
//        );
//    }

//    public String getAuthorizationUrl(String provider, String redirectUri) {
//        // 각 provider별로 인증 URL을 생성하여 반환
//        if ("kakao".equals(provider)) {
//            return "https://kauth.kakao.com/oauth/authorize?client_id=YOUR_CLIENT_ID&redirect_uri=" + redirectUri + "&response_type=code";
//        } else if ("google".equals(provider)) {
//            return "https://accounts.google.com/o/oauth2/v2/auth?client_id=YOUR_CLIENT_ID&redirect_uri=" + redirectUri + "&response_type=code&scope=openid%20profile";
//        }
//        // 기본적으로는 예외나 null 반환
//        throw new IllegalArgumentException("Unsupported provider: " + provider);
//    }
}