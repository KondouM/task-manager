package com.example.backend.controller;

import com.example.backend.model.Task;
import com.example.backend.repository.TaskRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import com.example.backend.repository.UserRepository;
import com.example.backend.model.User;
import java.util.List;
import java.security.Principal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;



@Controller
public class TaskViewController {

    @Autowired
    private TaskRepository taskRepository;
    
    @Autowired
    private UserRepository userRepository;

    // ルートパスをタスク一覧ページにリダイレクト
    @GetMapping("/")
    public String home() {
        return "redirect:/tasks/view";
    }

    @GetMapping("/tasks/view")
    public String showTasks(Model model, Authentication authentication) {
        String username = authentication.getName();
        User user = userRepository.findByUsername(username); // ユーザー取得
    
        // ログインユーザーのタスクだけ取得
        List<Task> allTasks = taskRepository.findByUser(user);
    
        List<Task> activeTasks = allTasks.stream()
                                         .filter(task -> !task.isCompleted())
                                         .toList();
        List<Task> completedTasks = allTasks.stream()
                                            .filter(Task::isCompleted)
                                            .toList();
    
        model.addAttribute("tasks", activeTasks);
        model.addAttribute("completedTasks", completedTasks);
    
        // 管理者権限チェック
        boolean isAdmin = authentication.getAuthorities()
                          .contains(new SimpleGrantedAuthority("ROLE_ADMIN"));
        model.addAttribute("isAdmin", isAdmin);
    
        return "tasks";
    }

    // 新しいタスクを追加
    @PostMapping("/tasks/view")
    public String addTask(@RequestParam String title,
                          @RequestParam String description,

                          Principal principal){
        String username = principal.getName();
        User user = userRepository.findByUsername(username);
        

                          @RequestParam String deadline){

        Task task = new Task();
        task.setTitle(title);
        task.setDescription(description);
        task.setCompleted(false);
        task.setUser(user);


        //文字列をLocalDateに変換
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm");
        LocalDateTime parsedDeadline = LocalDateTime.parse(deadline, formatter);
        task.setDeadline(parsedDeadline);
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
