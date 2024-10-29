package ae.exafy.taskmanager.service;

import ae.exafy.taskmanager.model.Priority;
import ae.exafy.taskmanager.model.Status;
import ae.exafy.taskmanager.model.Task;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Service
public class NotificationService {

    private final SchedulerService schedulerService;

    private final NotificationSettingsService notificationSettingsService;

    private final EmailService emailService;

    public void notifyTaskAssignment(Task task) {
        // Send immediate email notification to assigned user
        String message = String.format("You have been assigned a new task: %s, due on %s", task.getTitle(), task.getDueDate());
        emailService.sendEmail(task.getAssignedUser(), "Task Assignment Notification", message);

        // Schedule deadline notification and priority-based reminders
        scheduleDeadlineNotification(task);
        scheduleReminderNotification(task);
    }

    public void rescheduleTaskNotifications(Task task) {
        // Schedule deadline notification and priority-based reminders
        schedulerService.cancelTask(task.getId());
        scheduleDeadlineNotification(task);
        scheduleReminderNotification(task);
    }

    private void scheduleDeadlineNotification(Task task) {
        // Calculate the date-time 30 minutes before the due time
        LocalDateTime reminderTime = task.getDueDate().minusMinutes(30);

        // Convert to cron expression format (minute, hour, day, month)
        String cronExpression = String.format("%d %d %d %d ?",
                reminderTime.getMinute(),
                reminderTime.getHour(),
                reminderTime.getDayOfMonth(),
                reminderTime.getMonthValue());
        // Schedule a notification on the due date
        schedulerService.scheduleTask(
                task.getId(),
                () -> emailService.sendEmail(
                        task.getAssignedUser(),
                        "Task Due Notification",
                        String.format("Task %s is due soon on %s!", task.getTitle(), task.getDueDate())
                ),
                cronExpression
        );
    }

    private void scheduleReminderNotification(Task task) {
// Get enabled statuses from the configuration service
        List<Status> enabledStatuses = notificationSettingsService.getEnabledStatuses();

        // Check if the task's status is enabled for notifications
        if (enabledStatuses.contains(task.getStatus())) {
            // Determine frequency by priority (e.g., every hour, every six hours)
            String cronExpression = buildCronExpression(task.getPriority());

            // Schedule reminder based on the cron expression
            schedulerService.scheduleTask(
                    task.getId(),
                    () -> emailService.sendEmail(
                            task.getAssignedUser(),
                            "Task Reminder",
                            String.format("Reminder: Task %s needs attention!", task.getTitle())
                    ),
                    cronExpression
            );
        }
    }

    private String buildCronExpression(Priority priority) {
        // Example frequencies based on priority, could be adjusted based on requirements
        return switch (priority) {
            case HIGH -> "0 0/60 * * * ?"; // every hour
            case MEDIUM -> "0 0 0/6 * * ?"; // every 6 hours
            case LOW -> "0 0 12 * * ?"; // every day at noon
        };
    }
}
