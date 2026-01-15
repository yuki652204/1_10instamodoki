package com.example.blog.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import com.example.blog.models.Post;
import com.example.blog.repository.PostRepository;

import java.util.Collections;
import java.util.List;

@Controller
@RequestMapping("/posts") // ← トップページではなく /posts に統一
public class PostController {

    private final PostRepository postRepository;

    public PostController(PostRepository postRepository) {
        this.postRepository = postRepository;
    }

    // 投稿一覧ページ
    @GetMapping
    public String index(Model model) {
        List<Post> posts = postRepository.findAll();
        Collections.reverse(posts); // 新着順
        model.addAttribute("posts", posts);
        return "home"; // templates/posts.html を表示
    }

    // 新規投稿
    @PostMapping("/create")
    public String createPost(Post post) {
        postRepository.save(post);
        return "redirect:/posts";
    }

    // 投稿編集画面
    @GetMapping("/edit/{id}")
    public String editForm(@PathVariable Long id, Model model) {
        Post post = postRepository.findById(id).orElseThrow();
        model.addAttribute("post", post);
        return "edit";
    }

    // 投稿更新
    @PostMapping("/update")
    public String updatePost(Post post) {
        postRepository.save(post);
        return "redirect:/posts";
    }

    // 投稿削除
    @PostMapping("/delete/{id}")
    public String deletePost(@PathVariable Long id) {
        postRepository.deleteById(id);
        return "redirect:/posts";
    }
}
