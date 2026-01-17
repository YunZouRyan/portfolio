package com.ryanzou.taskmanager.repositories;

import com.ryanzou.taskmanager.beans.Task;
import com.ryanzou.taskmanager.beans.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TaskRepository extends JpaRepository<Task, Long> {
    List<Task> findTasksByUser(User user);

    Task findTaskById_AndUser(Long id, User user);
}
