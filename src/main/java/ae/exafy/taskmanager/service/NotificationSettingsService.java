package ae.exafy.taskmanager.service;

import ae.exafy.taskmanager.controller.request.NotificationSettingsRequest;
import ae.exafy.taskmanager.model.NotificationSettings;
import ae.exafy.taskmanager.model.Status;
import ae.exafy.taskmanager.repository.NotificationSettingsRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Service
public class NotificationSettingsService {


    private final NotificationSettingsRepository notificationSettingsRepository;

    public List<Status> getEnabledStatuses() {
        NotificationSettings notificationSettings = notificationSettingsRepository.findFirst().orElseGet(() -> notificationSettingsRepository.save(new NotificationSettings()));

        return notificationSettings.getEnabledStatuses();
    }


    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public void saveNotificationSettings(NotificationSettingsRequest notificationSettingsRequest) {

        NotificationSettings notificationSettings = notificationSettingsRepository.findFirst().orElseGet(() -> notificationSettingsRepository.save(new NotificationSettings()));

        notificationSettings.setEnabledStatuses(notificationSettingsRequest.getEnabledStatuses());
        notificationSettingsRepository.save(notificationSettings);
    }
}
