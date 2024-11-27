package com.webrtc.member.repository;

import java.util.List;
import java.util.Optional;

import org.apache.ibatis.annotations.Mapper;
import com.webrtc.member.model.dto.MemberDTO;
import org.apache.ibatis.annotations.Param;


@Mapper
public interface AdminRepository {

    // 회원 상태 변경 처리
    void updateMemberStatus(@Param("member_id") String member_id, @Param("newStatus") int newStatus);

   // 모든 회원 정보 조회
//    List<MemberDTO> findAllMembers();

    // 모든 회원 정보 조회 (페이지네이션 추가)
    List<MemberDTO> findAllMembersWithPagination(@Param("size") int size, @Param("offset") int offset);

    // 총 회원 수 조회
    int countAllMembers();




    // 특정 회원 정보 조회
    Optional<MemberDTO> findMemberById(String member_id);


}
