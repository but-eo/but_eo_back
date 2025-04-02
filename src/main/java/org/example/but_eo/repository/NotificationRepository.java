package org.example.but_eo.repository;

import org.example.but_eo.entity.Notification;
import org.example.but_eo.entity.Users;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface NotificationRepository extends JpaRepository<Notification, String> {
    void deleteAllBySenderUser(Users senderUser);
    void deleteAllByReceiverUser(Users receiverUser);
}

