package org.example.but_eo.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.example.but_eo.dto.StadiumRequest;
import org.example.but_eo.entity.Stadium;
import org.example.but_eo.entity.Users;
import org.example.but_eo.repository.StadiumRepository;
import org.example.but_eo.repository.UsersRepository;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class StadiumService {

    private final StadiumRepository stadiumRepository;
    private final UsersRepository usersRepository;

    private Users validateManager(String userId) {
        Users user = usersRepository.findByUserHashId(userId);
        if (user.getDivision() != Users.Division.ADMIN && user.getDivision() != Users.Division.BUSINESS) {
            throw new AccessDeniedException("관리자 또는 사업자만 수행 가능합니다.");
        }
        return user;
    }

    private void validateOwnerOrAdmin(Users user, Stadium stadium) {
        if (user.getDivision() == Users.Division.ADMIN) return;

        if (!stadium.getOwner().getUserHashId().equals(user.getUserHashId())) {
            throw new AccessDeniedException("해당 경기장에 대한 권한이 없습니다.");
        }
    }

    @Transactional
    public void createStadium(StadiumRequest req, String userId) {
        Users user = validateManager(userId);

        Stadium stadium = new Stadium();
        stadium.setStadiumId(UUID.randomUUID().toString());
        stadium.setStadiumName(req.getStadiumName());
        stadium.setStadiumRegion(req.getStadiumRegion());
        stadium.setAvailableDays(req.getAvailableDays());
        stadium.setAvailableHours(req.getAvailableHours());
        stadium.setStadiumCost(req.getStadiumCost());
        stadium.setStadiumMany(req.getStadiumMany());
        stadium.setStadiumTel(req.getStadiumTel());
        stadium.setOwner(user);

        stadiumRepository.save(stadium);
    }

    @Transactional
    public void updateStadium(String stadiumId, StadiumRequest req, String userId) {
        Users user = validateManager(userId);
        Stadium stadium = stadiumRepository.findById(stadiumId)
                .orElseThrow(() -> new IllegalArgumentException("해당 경기장이 존재하지 않습니다."));

        validateOwnerOrAdmin(user, stadium);

        stadium.setStadiumName(req.getStadiumName());
        stadium.setStadiumRegion(req.getStadiumRegion());
        stadium.setAvailableDays(req.getAvailableDays());
        stadium.setAvailableHours(req.getAvailableHours());
        stadium.setStadiumCost(req.getStadiumCost());
        stadium.setStadiumMany(req.getStadiumMany());
        stadium.setStadiumTel(req.getStadiumTel());

        stadiumRepository.save(stadium);
    }

    @Transactional
    public void deleteStadium(String stadiumId, String userId) {
        Users user = validateManager(userId);
        Stadium stadium = stadiumRepository.findById(stadiumId)
                .orElseThrow(() -> new IllegalArgumentException("해당 경기장이 존재하지 않습니다."));

        validateOwnerOrAdmin(user, stadium);

        stadiumRepository.delete(stadium);
    }

    public List<Stadium> getAll() {
        return stadiumRepository.findAll();
    }
}
