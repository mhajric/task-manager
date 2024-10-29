package ae.exafy.taskmanager.service;

import ae.exafy.taskmanager.controller.request.NotificationSettingsRequest;
import ae.exafy.taskmanager.model.NotificationSettings;
import ae.exafy.taskmanager.model.Status;
import ae.exafy.taskmanager.repository.NotificationSettingsRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class NotificationSettingsServiceTest {

    @Mock
    private NotificationSettingsRepository notificationSettingsRepository;

    @InjectMocks
    private NotificationSettingsService notificationSettingsService;

    private NotificationSettings notificationSettings;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        notificationSettings = new NotificationSettings();
        notificationSettings.setEnabledStatuses(Collections.emptyList());
    }

    @Test
    void getEnabledStatuses_ShouldReturnEnabledStatuses_WhenExists() {
        // Arrange
        when(notificationSettingsRepository.findFirst()).thenReturn(Optional.of(notificationSettings));

        // Act
        List<Status> enabledStatuses = notificationSettingsService.getEnabledStatuses();

        // Assert
        verify(notificationSettingsRepository).findFirst();
        assertEquals(Collections.emptyList(), enabledStatuses);
    }

    @Test
    void getEnabledStatuses_ShouldCreateNewSettings_WhenNoSettingsExist() {
        // Arrange
        when(notificationSettingsRepository.findFirst()).thenReturn(Optional.empty());
        when(notificationSettingsRepository.save(any(NotificationSettings.class))).thenReturn(notificationSettings);

        // Act
        List<Status> enabledStatuses = notificationSettingsService.getEnabledStatuses();

        // Assert
        verify(notificationSettingsRepository).findFirst();
        verify(notificationSettingsRepository).save(any(NotificationSettings.class));
        assertEquals(Collections.emptyList(), enabledStatuses);
    }

    @Test
    void saveNotificationSettings_ShouldSaveStatuses() {
        // Arrange
        NotificationSettingsRequest request = new NotificationSettingsRequest();
        request.setEnabledStatuses(Collections.singletonList(Status.IN_PROGRESS));
        when(notificationSettingsRepository.findFirst()).thenReturn(Optional.of(notificationSettings));

        // Act
        notificationSettingsService.saveNotificationSettings(request);

        // Assert
        verify(notificationSettingsRepository).findFirst();
        verify(notificationSettingsRepository).save(notificationSettings);
        assertEquals(Collections.singletonList(Status.IN_PROGRESS), notificationSettings.getEnabledStatuses());
    }

    @Test
    void saveNotificationSettings_ShouldCreateNewSettings_WhenNoSettingsExist() {
        // Arrange
        NotificationSettingsRequest request = new NotificationSettingsRequest();
        request.setEnabledStatuses(Collections.singletonList(Status.IN_PROGRESS));
        when(notificationSettingsRepository.findFirst()).thenReturn(Optional.empty());
        when(notificationSettingsRepository.save(any(NotificationSettings.class))).thenReturn(notificationSettings);

        // Act
        notificationSettingsService.saveNotificationSettings(request);

        // Assert
        verify(notificationSettingsRepository).findFirst();
        verify(notificationSettingsRepository, times(2)).save(any(NotificationSettings.class));
        assertEquals(Collections.singletonList(Status.IN_PROGRESS), notificationSettings.getEnabledStatuses());
    }
}