package org.example.but_eo.repository;

import org.example.but_eo.entity.InquiryAnswer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface InquiryAnswerRepository extends JpaRepository<InquiryAnswer, String> {
}