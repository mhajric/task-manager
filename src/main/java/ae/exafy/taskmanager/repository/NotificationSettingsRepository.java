package ae.exafy.taskmanager.repository;


import ae.exafy.taskmanager.model.NotificationSettings;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface NotificationSettingsRepository extends JpaRepository<NotificationSettings, Long> {

    @Query("SELECT ns FROM NotificationSettings ns ORDER BY ns.id ASC")
    Optional<NotificationSettings> findFirst();
}
