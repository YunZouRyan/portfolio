package com.ryanzou.taskmanager.controllers;

import com.ryanzou.taskmanager.beans.Task;
import com.ryanzou.taskmanager.beans.User;
import com.ryanzou.taskmanager.repositories.TaskRepository;
import lombok.AllArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping(value = {"/api"})
public class TaskController {
    private TaskRepository taskRepository;

    @GetMapping(value = "/tasks")
    public List<Task> getTasks() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = (User) authentication.getPrincipal();
        return taskRepository.findTasksByUser(user);
    }

    @PostMapping(value = "/tasks", consumes = "application/json")
    public void createTask(@RequestBody Task task) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = (User) authentication.getPrincipal();
        Task newTask = new Task(task.getTitle(), task.getDescription(), task.getDueDate(), user);
        taskRepository.save(newTask);
    }

    @PutMapping(value = "/tasks/{id}", consumes = "application/json")
    public void updateTask(@PathVariable Long id, @RequestBody Task task) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = (User) authentication.getPrincipal();

        Task updatedTask = taskRepository.findTaskById_AndUser(id, user);

        updatedTask.setTitle(task.getTitle());
        updatedTask.setDescription(task.getDescription());
        updatedTask.setDueDate(task.getDueDate());

        taskRepository.save(updatedTask);
    }

    @DeleteMapping(value = "/tasks/{id}")
    public void deleteTask(@PathVariable Long id) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Task task = taskRepository.findTaskById_AndUser(id, (User) authentication.getPrincipal());
        if (task != null) {
            taskRepository.delete(task);
        }
    }
}
