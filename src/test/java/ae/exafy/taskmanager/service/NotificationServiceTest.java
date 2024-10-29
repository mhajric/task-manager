package ae.exafy.taskmanager.service;

import ae.exafy.taskmanager.model.Priority;
import ae.exafy.taskmanager.model.Status;
import ae.exafy.taskmanager.model.Task;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDateTime;
import java.util.Collections;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.anyString;
import static org.mockito.Mockito.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class NotificationServiceTest {

    @Mock
    private SchedulerService schedulerService;

    @Mock
    private NotificationSettingsService notificationSettingsService;

    @Mock
    private EmailService emailService;

    @InjectMocks
    private NotificationService notificationService;

    private Task task;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        task = new Task();
        task.setId(1L);
        task.setTitle("Test Task");
        task.setDueDate(LocalDateTime.now().plusHours(1)); // Set due date 1 hour from now
        task.setAssignedUser("user@example.com");
        task.setStatus(Status.IN_PROGRESS);
        task.setPriority(Priority.HIGH);
    }

    @Test
    void notifyTaskAssignment_ShouldSendImmediateEmailAndScheduleNotifications() {
        // Arrange
        when(notificationSettingsService.getEnabledStatuses()).thenReturn(Collections.singletonList(Status.IN_PROGRESS));

        // Act
        notificationService.notifyTaskAssignment(task);

        // Assert
        verify(emailService).sendEmail(task.getAssignedUser(), "Task Assignment Notification",
                String.format("You have been assigned a new task: %s, due on %s", task.getTitle(), task.getDueDate()));
        verify(schedulerService, times(2)).scheduleTask(any(), any(), any());
    }

    @Test
    void rescheduleTaskNotifications_ShouldCancelAndRescheduleNotifications() {
        // Arrange
        when(notificationSettingsService.getEnabledStatuses()).thenReturn(Collections.singletonList(Status.IN_PROGRESS));

        // Act
        notificationService.rescheduleTaskNotifications(task);

        // Assert
        verify(schedulerService).cancelTask(task.getId());
        verify(schedulerService, times(2)).scheduleTask(any(), any(), any());
    }

    @Test
    void scheduleDeadlineNotification_ShouldScheduleEmail() {
        // Act
        notificationService.notifyTaskAssignment(task);

        // Assert
        verify(schedulerService).scheduleTask(eq(task.getId()), any(Runnable.class), anyString());
    }

    @Test
    void scheduleReminderNotification_ShouldScheduleEmail_WhenStatusIsInProgress() {
        // Arrange
        when(notificationSettingsService.getEnabledStatuses()).thenReturn(Collections.singletonList(Status.IN_PROGRESS));

        // Act
        notificationService.notifyTaskAssignment(task);

        // Assert
        verify(schedulerService, times(2)).scheduleTask(eq(task.getId()), any(Runnable.class), anyString());
    }
}
