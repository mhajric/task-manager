package ae.exafy.taskmanager.controller.request;

import ae.exafy.taskmanager.model.Category;
import ae.exafy.taskmanager.model.Priority;
import ae.exafy.taskmanager.model.Status;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class TaskRequest {

    private String title;

    private String description;

    private LocalDateTime dueDate;

    private Priority priority;

    private Status status;

    private Category category;

    private String assignedUser;
}
