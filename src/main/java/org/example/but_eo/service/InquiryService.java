package org.example.but_eo.service;

import lombok.RequiredArgsConstructor;
import org.example.but_eo.entity.Inquiry;
import org.example.but_eo.entity.InquiryAnswer;
import org.example.but_eo.entity.Users;
import org.example.but_eo.repository.InquiryAnswerRepository;
import org.example.but_eo.repository.InquiryRepository;
import org.example.but_eo.repository.UsersRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class InquiryService {

    private final InquiryRepository inquiryRepository;
    private final InquiryAnswerRepository answerRepository;
    private final UsersRepository usersRepository;

    @Transactional
    public void createInquiry(String userId, String title, String content, String password) {
        Users user = usersRepository.findByUserHashId(userId);
        if (user == null) throw new IllegalArgumentException("유저가 존재하지 않습니다.");

        Inquiry inquiry = new Inquiry();
        inquiry.setInquiryId(UUID.randomUUID().toString());
        inquiry.setUser(user);
        inquiry.setTitle(title);
        inquiry.setContent(content);
        inquiry.setPassword(password);
        inquiry.setVisibility(password != null && !password.isBlank() ? Inquiry.Visibility.PRIVATE : Inquiry.Visibility.PUBLIC);
        inquiry.setCreatedAt(LocalDateTime.now());

        inquiryRepository.save(inquiry);
    }

    public List<Inquiry> getAccessibleInquiries(String userId) {
        Users user = usersRepository.findByUserHashId(userId);
        if (user == null) throw new IllegalArgumentException("유저가 존재하지 않습니다.");

        if (user.getDivision() == Users.Division.ADMIN) {
            return inquiryRepository.findAll(); // 관리자: 전체 조회 가능
        }

        return inquiryRepository.findAll().stream()
                .filter(i -> i.getVisibility() == Inquiry.Visibility.PUBLIC || i.getUser().getUserHashId().equals(userId))
                .toList();
    }

    public List<Inquiry> getPublicInquiries() {
        return inquiryRepository.findByVisibility(Inquiry.Visibility.PUBLIC);
    }

    public List<Inquiry> getMyInquiries(String userId) {
        Users user = usersRepository.findByUserHashId(userId);
        if (user == null) throw new IllegalArgumentException("유저가 존재하지 않습니다.");
        return inquiryRepository.findByUser(user);
    }

    public Inquiry getInquiryDetail(String inquiryId, String userId, String passwordFromRequest) {
        Inquiry inquiry = inquiryRepository.findByInquiryId(inquiryId)
                .orElseThrow(() -> new IllegalArgumentException("문의글이 존재하지 않습니다."));

        if (inquiry.getVisibility() == Inquiry.Visibility.PUBLIC) {
            return inquiry;
        }

        Users user = null;
        if (userId != null) {
            user = usersRepository.findByUserHashId(userId);
        }

        boolean isAdmin = user != null && user.getDivision() == Users.Division.ADMIN;
        boolean isOwner = user != null && inquiry.getUser().getUserHashId().equals(userId);

        if (isAdmin || isOwner) {
            return inquiry;
        }

        if (inquiry.getPassword() != null && inquiry.getPassword().equals(passwordFromRequest)) {
            return inquiry;
        }

        throw new SecurityException("비공개 글 접근 권한 없음");
    }

    @Transactional
    public void answerInquiry(String inquiryId, String adminId, String content) {
        Inquiry inquiry = inquiryRepository.findByInquiryId(inquiryId)
                .orElseThrow(() -> new IllegalArgumentException("문의글이 존재하지 않습니다."));

        Users admin = usersRepository.findByUserHashId(adminId);
        if (admin == null || admin.getDivision() != Users.Division.ADMIN) {
            throw new SecurityException("관리자 권한이 없습니다.");
        }

        InquiryAnswer answer = new InquiryAnswer();
        answer.setAnswerId(UUID.randomUUID().toString());
        answer.setInquiry(inquiry);
        answer.setAdmin(admin);
        answer.setContent(content);
        answer.setAnsweredAt(LocalDateTime.now());

        answerRepository.save(answer);
    }
}
