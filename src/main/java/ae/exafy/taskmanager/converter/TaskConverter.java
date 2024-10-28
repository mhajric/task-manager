package ae.exafy.taskmanager.converter;

import ae.exafy.taskmanager.controller.request.TaskRequest;
import ae.exafy.taskmanager.controller.response.TaskResponse;
import ae.exafy.taskmanager.model.Task;
import org.springframework.stereotype.Component;

@Component
public class TaskConverter {

    public TaskResponse convertToResponse(final Task task) {
        return TaskResponse.builder()
                .id(task.getId())
                .title(task.getTitle())
                .description(task.getDescription())
                .dueDate(task.getDueDate())
                .priority(task.getPriority())
                .status(task.getStatus())
                .category(task.getCategory())
                .assignedUser(task.getAssignedUser())
                .build();
    }

    public Task convertToTask(final TaskRequest taskRequest) {
        return Task.builder()
                .title(taskRequest.getTitle())
                .description(taskRequest.getDescription())
                .dueDate(taskRequest.getDueDate())
                .priority(taskRequest.getPriority())
                .status(taskRequest.getStatus())
                .category(taskRequest.getCategory())
                .assignedUser(taskRequest.getAssignedUser())
                .build();
    }
}
