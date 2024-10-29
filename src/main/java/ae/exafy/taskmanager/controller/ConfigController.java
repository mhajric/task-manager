package ae.exafy.taskmanager.controller;

import ae.exafy.taskmanager.controller.request.NotificationSettingsRequest;
import ae.exafy.taskmanager.service.NotificationService;
import ae.exafy.taskmanager.service.NotificationSettingsService;
import ae.exafy.taskmanager.service.TaskService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/config")
public class ConfigController {

    private final NotificationSettingsService notificationSettingsService;

    private final NotificationService notificationService;

    private final TaskService taskService;

    @PostMapping("/status-notifications")
    public ResponseEntity<Void> updateNotifications(@RequestBody NotificationSettingsRequest request) {
        notificationSettingsService.saveNotificationSettings(request);
        //TODO: emit event instead of calling service
        taskService.getAllTasks().forEach(notificationService::rescheduleTaskNotifications);
        return ResponseEntity.ok().build();
    }
}
