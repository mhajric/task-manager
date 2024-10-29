package ae.exafy.taskmanager.service;

import ae.exafy.taskmanager.controller.request.TaskRequest;
import ae.exafy.taskmanager.controller.response.TaskResponse;
import ae.exafy.taskmanager.converter.TaskConverter;
import ae.exafy.taskmanager.exception.InvalidTaskException;
import ae.exafy.taskmanager.model.Category;
import ae.exafy.taskmanager.model.Priority;
import ae.exafy.taskmanager.model.Status;
import ae.exafy.taskmanager.model.Task;
import ae.exafy.taskmanager.repository.TaskRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class TaskServiceTest {

    @Mock
    private TaskRepository taskRepository;

    @Mock
    private NotificationService notificationService;

    @Mock
    private TaskConverter taskConverter;

    @InjectMocks
    private TaskService taskService;

    private TaskRequest taskRequest;
    private Task task;
    private TaskResponse taskResponse;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
        String title = "Sample Title";
        String description = "Sample Description";
        LocalDateTime dueDate = LocalDateTime.now().plusDays(1);
        Priority priority = Priority.MEDIUM;
        Status status = Status.IN_PROGRESS;
        Category category = Category.PERSONAL;
        String assignedUser = "user@example.com";
        taskRequest = new TaskRequest();
        taskRequest.setTitle(title);
        taskRequest.setDescription(description);
        taskRequest.setDueDate(dueDate);
        taskRequest.setPriority(priority);
        taskRequest.setStatus(status);
        taskRequest.setCategory(category);
        taskRequest.setAssignedUser(assignedUser);
        task = new Task(1L, title, description, dueDate, priority, status, category, assignedUser);
        taskResponse = new TaskResponse(1L, title, description, dueDate, priority, status, category, assignedUser);
    }

    @Test
    public void testCreateTask() {
        when(taskConverter.convertToTask(taskRequest)).thenReturn(task);
        when(taskRepository.save(task)).thenReturn(task);
        when(taskConverter.convertToResponse(task)).thenReturn(taskResponse);

        TaskResponse result = taskService.createTask(taskRequest);

        assertEquals(taskResponse, result);
        verify(taskRepository).save(task);
        verify(taskConverter).convertToTask(taskRequest);
        verify(taskConverter).convertToResponse(task);

        verify(notificationService).notifyTaskAssignment(task);
    }

    @Test
    public void testUpdateTask() {
        when(taskRepository.findById(1L)).thenReturn(Optional.of(task));
        when(taskRepository.save(task)).thenReturn(task);
        when(taskConverter.convertToResponse(task)).thenReturn(taskResponse);

        TaskResponse result = taskService.updateTask(1L, taskRequest);

        assertEquals(taskResponse, result);
        verify(taskRepository).findById(1L);
        verify(taskRepository).save(task);
        verify(taskConverter).convertToResponse(task);

        verify(notificationService, never()).notifyTaskAssignment(task);

        verify(notificationService).rescheduleTaskNotifications(task);
    }

    @Test
    public void testCreateTaskValidation() {
        when(taskConverter.convertToTask(taskRequest)).thenReturn(task);
        when(taskRepository.save(task)).thenReturn(task);
        when(taskConverter.convertToResponse(task)).thenReturn(taskResponse);

        task.setDueDate(LocalDateTime.now().minusDays(1));
        task.setStatus(Status.IN_PROGRESS);

        assertThrows(InvalidTaskException.class, () -> taskService.createTask( taskRequest));
    }

    @Test
    public void testUpdateTaskValidation() {
        when(taskRepository.findById(1L)).thenReturn(Optional.of(task));
        when(taskRepository.save(task)).thenReturn(task);
        when(taskConverter.convertToResponse(task)).thenReturn(taskResponse);

        taskRequest.setDueDate(LocalDateTime.now().minusDays(1));
        taskRequest.setStatus(Status.IN_PROGRESS);

        assertThrows(InvalidTaskException.class, () -> taskService.updateTask(1L, taskRequest));
    }

    @Test
    public void testDeleteTask() {
        taskService.deleteTask(1L);

        verify(taskRepository).deleteById(1L);
    }

    @Test
    public void testCompleteTask() {
        when(taskRepository.findById(1L)).thenReturn(Optional.of(task));
        task.setStatus(Status.COMPLETED);
        when(taskRepository.save(task)).thenReturn(task);
        when(taskConverter.convertToResponse(task)).thenReturn(taskResponse);

        TaskResponse result = taskService.completeTask(1L);

        assertEquals(taskResponse, result);
        verify(taskRepository).findById(1L);
        verify(taskRepository).save(task);
        verify(taskConverter).convertToResponse(task);
    }

    @Test
    public void testGetTasksWithStatusFilter() {
        List<Status> statuses = List.of(Status.IN_PROGRESS);
        PageRequest pageRequest = PageRequest.of(0, 10, Sort.by(Sort.Direction.ASC, "dueDate"));
        Page<Task> page = new PageImpl<>(List.of(task));

        when(taskRepository.findAllByStatusIn(statuses, pageRequest)).thenReturn(page);
        when(taskConverter.convertToResponse(any(Task.class))).thenReturn(taskResponse);

        Page<TaskResponse> result = taskService.getTasks(statuses, "dueDate", "ASC", 0, 10);

        assertEquals(1, result.getContent().size());
        assertEquals(taskResponse, result.getContent().get(0));
        verify(taskRepository).findAllByStatusIn(statuses, pageRequest);
    }

    @Test
    public void testGetTasksWithoutStatusFilter() {
        PageRequest pageRequest = PageRequest.of(0, 10, Sort.by(Sort.Direction.ASC, "dueDate"));
        Page<Task> page = new PageImpl<>(List.of(task));

        when(taskRepository.findAll(pageRequest)).thenReturn(page);
        when(taskConverter.convertToResponse(any(Task.class))).thenReturn(taskResponse);

        Page<TaskResponse> result = taskService.getTasks(null, "dueDate", "ASC", 0, 10);

        assertEquals(1, result.getContent().size());
        assertEquals(taskResponse, result.getContent().get(0));
        verify(taskRepository).findAll(pageRequest);
    }
}
