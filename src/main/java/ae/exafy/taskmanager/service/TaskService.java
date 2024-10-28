package ae.exafy.taskmanager.service;

import ae.exafy.taskmanager.controller.request.TaskRequest;
import ae.exafy.taskmanager.controller.response.TaskResponse;
import ae.exafy.taskmanager.converter.TaskConverter;
import ae.exafy.taskmanager.model.Status;
import ae.exafy.taskmanager.model.Task;
import ae.exafy.taskmanager.repository.TaskRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Service
public class TaskService {

    private final TaskRepository taskRepository;

    private final TaskConverter taskConverter;

    public TaskResponse createTask(final TaskRequest taskRequest) {
        final Task task = taskConverter.convertToTask(taskRequest);
        final Task savedTask = taskRepository.save(task);
        return taskConverter.convertToResponse(savedTask);
    }

    public TaskResponse updateTask(final Long id, final TaskRequest taskRequest) {
        final Task task = taskRepository.findById(id).orElseThrow();
        task.setTitle(taskRequest.getTitle());
        task.setDescription(taskRequest.getDescription());
        task.setDueDate(taskRequest.getDueDate());
        task.setPriority(taskRequest.getPriority());
        task.setStatus(taskRequest.getStatus());
        task.setCategory(taskRequest.getCategory());
        task.setAssignedUser(taskRequest.getAssignedUser());
        final Task savedTask = taskRepository.save(task);
        return taskConverter.convertToResponse(savedTask);
    }

    public void deleteTask(final Long id) {
        taskRepository.deleteById(id);
    }

    public TaskResponse completeTask(final Long id) {
        final Task task = taskRepository.findById(id).orElseThrow();
        task.setStatus(Status.COMPLETED);
        final Task savedTask = taskRepository.save(task);
        return taskConverter.convertToResponse(savedTask);
    }

    public Page<TaskResponse> getTasks(final List<Status> statusList,
                                       final String sortBy,
                                       final String direction,
                                       final Integer pageNumber,
                                       final Integer pageSize) {
        Sort sort = Sort.by(Sort.Direction.fromString(direction), sortBy);
        PageRequest pageRequest = PageRequest.of(pageNumber, pageSize, sort);

        if (statusList == null || statusList.isEmpty()) {
            return taskRepository.findAll(pageRequest).map(taskConverter::convertToResponse);
        } else {
            return taskRepository.findAllByStatusIn(statusList, pageRequest).map(taskConverter::convertToResponse);
        }
    }
}
