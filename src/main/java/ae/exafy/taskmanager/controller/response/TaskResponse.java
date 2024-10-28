package ae.exafy.taskmanager.controller.response;

import ae.exafy.taskmanager.model.Category;
import ae.exafy.taskmanager.model.Priority;
import ae.exafy.taskmanager.model.Status;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.time.LocalDateTime;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
@ToString
public class TaskResponse {

    private Long id;

    private String title;

    private String description;

    private LocalDateTime dueDate;

    private Priority priority;

    private Status status;

    private Category category;

    private String assignedUser;
}
