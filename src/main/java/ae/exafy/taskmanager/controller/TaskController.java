package ae.exafy.taskmanager.controller;

import ae.exafy.taskmanager.controller.request.TaskRequest;
import ae.exafy.taskmanager.controller.response.TaskResponse;
import ae.exafy.taskmanager.model.Status;
import ae.exafy.taskmanager.service.TaskService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/tasks")
public class TaskController {

    private final TaskService taskService;


    @PostMapping
    public ResponseEntity<TaskResponse> createTask(final @RequestBody TaskRequest taskRequest) {
        final TaskResponse taskResponse = taskService.createTask(taskRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body(taskResponse);
    }

    @GetMapping
    public ResponseEntity<Page<TaskResponse>> getTasks(@RequestParam(name = "status", required = false) List<Status> statusList,
                                                       @RequestParam(defaultValue = "priority") String sortBy,
                                                       @RequestParam(defaultValue = "asc") String order,
                                                       @RequestParam(defaultValue = "0") Integer pageNumber,
                                                       @RequestParam(defaultValue = "10") Integer pageSize) {
        final Page<TaskResponse> tasks = taskService.getTasks(statusList, sortBy, order, pageNumber, pageSize);
        return ResponseEntity.ok(tasks);
    }

    @PutMapping("/{id}")
    public ResponseEntity<TaskResponse> updateTask(@PathVariable Long id, @RequestBody TaskRequest taskRequest) {
        final TaskResponse taskResponse = taskService.updateTask(id, taskRequest);
        return ResponseEntity.ok(taskResponse);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTask(@PathVariable Long id) {
        taskService.deleteTask(id);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{id}/complete")
    public ResponseEntity<TaskResponse> completeTask(@PathVariable Long id) {
        final TaskResponse taskResponse = taskService.completeTask(id);
        return ResponseEntity.ok(taskResponse);
    }
}
