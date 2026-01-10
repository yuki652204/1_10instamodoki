package com.example.blog.controller;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import com.example.blog.models.Post;
import com.example.blog.repository.PostRepository;

@Controller
public class BlogController {

    @Autowired
    private PostRepository postRepository;

    // 1. トップページを表示する
    @GetMapping("/")
    public String index(Model model) {
        // データベースからすべての投稿を取得します
        List<Post> post = postRepository.findAll();
        // リストの順番を逆（新着順）に並べ替えます
        java.util.Collections.reverse(post);
        // HTML（Thymeleaf）側に "posts" という名前でデータを渡します
        model.addAttribute("posts", post);
        return "home";
    }

    // 2. 新規投稿を受け取る
    @PostMapping("/post")
    public String createPost(Post post) {
        // 送られてきた内容をデータベースに新規保存します
        postRepository.save(post);
        // 保存後はトップページに画面を切り替えます（二重投稿防止）
        return "redirect:/";
    }
    
    // 3. 削除処理
    // URLの {id} の部分を @PathVariable で受け取ります
    @PostMapping("/post/delete/{id}")
    public String deletePost(@PathVariable Long id) {
        // 指定されたIDのデータをデータベースから削除します
        postRepository.deleteById(id);
        // 削除後はトップページに戻ります
        return "redirect:/";
    }
    
    // 4. 編集画面を表示する
    @GetMapping("/post/edit/{id}")
    public String editForm(@PathVariable Long id, Model model) {
        // IDで検索し、データがなければエラーを投げます（orElseThrow）
        Post post = postRepository.findById(id).orElseThrow();
        // 見つかった投稿データを、編集画面（edit.html）に渡します
        model.addAttribute("post", post);
        // edit.html を表示します
        return "edit";
    }

    // 5. 更新を実行する
    @PostMapping("/post/update")
    public String updatePost(Post post) {
        // saveメソッドは「IDが含まれている場合」は新規追加ではなく「上書き（更新）」を行います
        postRepository.save(post);
        // 更新後はトップページに戻ります
        return "redirect:/";
    }
    //いいね
    @PostMapping("/post/{id}/like") // ← HTMLの @{/post/{id}/like} と一致
    public String likePost(@PathVariable Long id) {
        postRepository.incrementLikes(id); // Repositoryの命令を呼び出す
        return "redirect:/"; // 処理が終わったらホームへ戻る
    }
}