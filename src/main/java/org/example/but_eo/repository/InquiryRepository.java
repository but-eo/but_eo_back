package org.example.but_eo.repository;

import org.example.but_eo.entity.Inquiry;
import org.example.but_eo.entity.Users;
import org.springframework.data.jpa.repository.JpaRepository;
// import org.springframework.data.jpa.repository.Query; // 필요시 JPQL 사용
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface InquiryRepository extends JpaRepository<Inquiry, String> {
    // 특정 사용자의 모든 문의 목록 (기본 정렬 - ID순 또는 DB 설정 따름)
    List<Inquiry> findByUser(Users user);

    // 특정 사용자의 모든 문의 목록 (생성일자 내림차순 - 최신순)
    List<Inquiry> findByUserOrderByCreatedAtDesc(Users user); // 정렬을 위해 사용

    // 공개된 모든 문의 목록 (생성일자 내림차순 - 최신순)
    List<Inquiry> findByVisibilityOrderByCreatedAtDesc(Inquiry.Visibility visibility);

    // 관리자가 모든 문의를 최신순으로 볼 때 사용 (선택 사항)
    List<Inquiry> findAllByOrderByCreatedAtDesc();

    Optional<Inquiry> findByInquiryId(String inquiryId);
    List<Inquiry> findByUserAndVisibility(Users user, Inquiry.Visibility visibility);
}