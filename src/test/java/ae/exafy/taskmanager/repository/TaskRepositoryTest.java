package ae.exafy.taskmanager.repository;

import ae.exafy.taskmanager.model.Category;
import ae.exafy.taskmanager.model.Priority;
import ae.exafy.taskmanager.model.Status;
import ae.exafy.taskmanager.model.Task;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
public class TaskRepositoryTest {

    @Autowired
    private TaskRepository taskRepository;

    @BeforeEach
    public void setup() {
        Task task1 = new Task(1L, "Task 1", "Description 1", LocalDateTime.now().plusDays(1), Priority.LOW, Status.IN_PROGRESS, Category.PERSONAL, "user@example.com");
        Task task2 = new Task(2L, "Task 2", "Description 2", LocalDateTime.now().plusDays(2), Priority.MEDIUM, Status.PENDING, Category.WORK, "user@example.com");
        Task task3 = new Task(3L, "Task 3", "Description 3", LocalDateTime.now().plusDays(3), Priority.HIGH, Status.COMPLETED, Category.PERSONAL, "user@example.com");

        taskRepository.saveAll(List.of(task1, task2, task3));
    }

    @Test
    public void testFindAllByStatusIn() {
        List<Status> statuses = List.of(Status.IN_PROGRESS);
        Pageable pageable = PageRequest.of(0, 10);

        Page<Task> result = taskRepository.findAllByStatusIn(statuses, pageable);

        assertThat(result.getContent()).hasSize(1);
        assertThat(result.getContent()).allMatch(task -> task.getStatus().equals(Status.IN_PROGRESS));
    }

    @Test
    public void testFindAllByStatusIn_EmptyResult() {
        List<Status> statuses = List.of(Status.COMPLETED);
        Pageable pageable = PageRequest.of(0, 10);

        Page<Task> result = taskRepository.findAllByStatusIn(statuses, pageable);

        assertThat(result.getContent()).hasSize(1);
        assertThat(result.getContent()).allMatch(task -> task.getStatus().equals(Status.COMPLETED));
    }
}
