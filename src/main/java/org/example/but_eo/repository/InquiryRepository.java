package org.example.but_eo.repository;

import org.example.but_eo.entity.Inquiry;
import org.example.but_eo.entity.Users;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface InquiryRepository extends JpaRepository<Inquiry, String> {
    List<Inquiry> findByVisibility(Inquiry.Visibility visibility);
    List<Inquiry> findByUser(Users user);
    Optional<Inquiry> findByInquiryId(String inquiryId);
    List<Inquiry> findByUserAndVisibility(Users user, Inquiry.Visibility visibility);
}