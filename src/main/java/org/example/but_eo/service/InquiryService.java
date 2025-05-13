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
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
@RequiredArgsConstructor
public class InquiryService {

    private final InquiryRepository inquiryRepository;
    private final InquiryAnswerRepository answerRepository;
    private final UsersRepository usersRepository;

    @Transactional
    public void createInquiry(String userId, String title, String content, String password, Inquiry.Visibility visibility) {
        Users user = usersRepository.findByUserHashId(userId);
        if (user == null) throw new IllegalArgumentException("유저가 존재하지 않습니다.");

        Inquiry inquiry = new Inquiry();
        inquiry.setInquiryId(UUID.randomUUID().toString());
        inquiry.setUser(user);
        inquiry.setTitle(title);
        inquiry.setContent(content);
        inquiry.setPassword(password);
        inquiry.setVisibility(visibility);
        inquiry.setCreatedAt(LocalDateTime.now());

        inquiryRepository.save(inquiry);
    }

    public List<Inquiry> getPublicAndOwnInquiries(String userId) {
        if (userId == null) {
            return inquiryRepository.findByVisibility(Inquiry.Visibility.PUBLIC);
        }

        Users user = usersRepository.findByUserHashId(userId);
        List<Inquiry> publicInquiries = inquiryRepository.findByVisibility(Inquiry.Visibility.PUBLIC);
        List<Inquiry> myPrivate = inquiryRepository.findByUser(user);

        return Stream.concat(publicInquiries.stream(), myPrivate.stream())
                .distinct()
                .collect(Collectors.toList());
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

        if (userId == null) {
            throw new SecurityException("비공개 글은 로그인 후 열람 가능합니다.");
        }

        Users user = usersRepository.findByUserHashId(userId);
        if (user == null) throw new IllegalArgumentException("유저가 존재하지 않습니다.");

        boolean isAdmin = user.getDivision() == Users.Division.ADMIN;
        boolean isOwner = inquiry.getUser().getUserHashId().equals(userId);

        if (!isAdmin && !isOwner) {
            throw new SecurityException("해당 비공개 글에 접근할 권한이 없습니다.");
        }

        if (!isAdmin && inquiry.getPassword() != null && !inquiry.getPassword().equals(passwordFromRequest)) {
            throw new SecurityException("비밀번호가 일치하지 않습니다.");
        }

        return inquiry;
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
