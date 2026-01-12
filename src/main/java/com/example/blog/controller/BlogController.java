package com.example.blog.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.example.blog.models.Follow;
//import com.example.blog.models.Follow;
import com.example.blog.models.Post;
import com.example.blog.repository.FollowRepository;
//import com.example.blog.repository.FollowRepository;
import com.example.blog.repository.PostRepository;

@Controller
public class BlogController {

    @Autowired
    private PostRepository postRepository;
    
    @Autowired 
    private FollowRepository followRepository;

    // 1. トップページを表示する
    @GetMapping("/")
    public String index(Model model) {
        // データベースからすべての投稿を取得します
        List<Post> post = postRepository.findAll();
        // リストの順番を逆（新着順）に並べ替えます
        java.util.Collections.reverse(post);
        // HTML（Thymeleaf）側に "posts" という名前でデータを渡します
        model.addAttribute("posts", post);
        
        String me = "GuestUser";
        model.addAttribute("profileName", me);
        model.addAttribute("followingCount", followRepository.countByFollowerName(me));
        model.addAttribute("followerCount", followRepository.countByFollowingName(me));

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
 // いいね（AJAX版）
    @PostMapping("/post/{id}/like")
    @ResponseBody // ← これをつけると、リダイレクトせずに「値」だけを返せるようになります
    public Integer likePost(@PathVariable Long id) {
        // 1. カウントを増やす
        postRepository.incrementLikes(id);
        
        // 2. 最新のいいね数を取得して、その「数字」だけを返す
        return postRepository.findById(id)
                .map(Post::getLikesCount)
                .orElse(0);
    }
    
    @PostMapping("/follow/{name}")
    @ResponseBody
    public String toggleFollow(@PathVariable String name) {
        String me = "GuestUser"; // 本来はログインユーザー
        
        if (followRepository.existsByFollowerNameAndFollowingName(me, name)) {
            followRepository.deleteByFollowerNameAndFollowingName(me, name);
            return "unfollowed";
        } else {
            Follow follow = new Follow();
            follow.setFollowerName(me);
            follow.setFollowingName(name);
            followRepository.save(follow);
            return "followed";
        }
    }
    
    @GetMapping("/profile/{name}")
    public String showProfile(@PathVariable String name, Model model) {
        // 1. その人が「誰を」フォローしているか数える
        long followingCount = followRepository.countByFollowerName(name);
        
        // 2. その人が「誰に」フォローされているか（フォロワー）数える
        long followerCount = followRepository.countByFollowingName(name);

        // HTMLに渡す
        model.addAttribute("profileName", name); // 画面に表示する名前
        model.addAttribute("followingCount", followingCount);
        model.addAttribute("followerCount", followerCount);
        
        return "profile";
    }
}