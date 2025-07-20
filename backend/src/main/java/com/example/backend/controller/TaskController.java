package com.example.backend.controller;

import com.example.backend.model.Task;
import com.example.backend.repository.TaskRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;


import java.util.List;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;


@RestController
@RequestMapping("/tasks")
public class TaskController {

    @Autowired
    private TaskRepository taskRepository;

    // --- タスク一覧を取得 ---
    @GetMapping
    public List<Task> getAllTasks() {
        return taskRepository.findAll();
    }
    
    // 追加
    @PostMapping
    public Task createTask(@RequestBody Task task ){
        return taskRepository.save(task);
    }

    
}