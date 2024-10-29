package ae.exafy.taskmanager.controller.request;

import ae.exafy.taskmanager.model.Status;
import lombok.Data;

import java.util.List;

@Data
public class NotificationSettingsRequest {

    private List<Status> enabledStatuses;
}
