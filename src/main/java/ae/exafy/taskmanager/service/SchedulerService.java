package ae.exafy.taskmanager.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.scheduling.support.CronTrigger;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ScheduledFuture;

@Slf4j
@RequiredArgsConstructor
@Service
public class SchedulerService {

    private final TaskScheduler taskScheduler;
    private final Map<Long, ScheduledFuture<?>> taskFutures = new ConcurrentHashMap<>();

    public void scheduleTask(Long taskId, Runnable task, String cronExpression) {
        CronTrigger trigger = new CronTrigger(cronExpression);
        ScheduledFuture<?> future = taskScheduler.schedule(task, trigger);
        taskFutures.put(taskId, future);
    }

    public void cancelTask(Long taskId) {
        ScheduledFuture<?> future = taskFutures.get(taskId);
        if (future != null && !future.isCancelled()) {
            future.cancel(true); // Cancels the task if it’s running
            taskFutures.remove(taskId);
        }
    }

}
