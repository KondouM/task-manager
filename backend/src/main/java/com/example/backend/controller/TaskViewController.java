package com.example.backend.controller;

import com.example.backend.model.Task;
import com.example.backend.repository.TaskRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;



@Controller
public class TaskViewController {

    @Autowired
    private TaskRepository taskRepository;

    // ルートパスをタスク一覧ページにリダイレクト
    @GetMapping("/")
    public String home() {
        return "redirect:/tasks/view";
    }

    // タスク一覧ページを表示（アクティブタスクと完了タスクを分離）
    @GetMapping("/tasks/view")
    public String showTasks(Model model){
        // データベースから全タスクを取得
        List<Task> allTasks = taskRepository.findAll();
        // アクティブ（未完了）タスクをフィルタリング
        List<Task> activeTasks = allTasks.stream()
                                            .filter(task -> !task.isCompleted())
                                            .toList();
        // 完了タスクをフィルタリング
        List<Task> completedTasks = allTasks.stream()
                                            .filter(Task::isCompleted)
                                            .toList();
        // Thymeleafテンプレート用にモデルにタスクを追加
        model.addAttribute("tasks", activeTasks);
        model.addAttribute("completedTasks", completedTasks);
        return "tasks";
    }

    // 新しいタスクを追加
    @PostMapping("/tasks/view")
    public String addTask(@RequestParam String title,
                          @RequestParam String description){
        Task task = new Task();
        task.setTitle(title);
        task.setDescription(description);
        task.setCompleted(false);
        taskRepository.save(task);
        return "redirect:/tasks/view";
    }
    
    // タスクを削除
    @PostMapping("/tasks/delete")
    public String deleteTask(@RequestParam Long id) {
        taskRepository.deleteById(id);
        return "redirect:/tasks/view";
    }
    
    // タスクの完了状態を更新
    @PostMapping("/tasks/update")
    public String updateTaskCompletion(@RequestParam Long id, 
                                       @RequestParam (required = false) Boolean completed){
        Task task = taskRepository.findById(id).orElseThrow();
        task.setCompleted(completed != null);
        taskRepository.save(task);
        return "redirect:/tasks/view";
    }
    
}
