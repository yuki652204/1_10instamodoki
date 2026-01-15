package com.example.blog.controller;

import java.util.Collections;
import java.util.List;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.example.blog.models.Follow;
import com.example.blog.models.Post;
import com.example.blog.repository.FollowRepository;
import com.example.blog.repository.PostRepository;

@Controller
@RequestMapping("/") // トップページは / に統一
public class BlogController {

    private final PostRepository postRepository;
    private final FollowRepository followRepository;

    public BlogController(PostRepository postRepository,
                          FollowRepository followRepository) {
        this.postRepository = postRepository;
        this.followRepository = followRepository;
    }

 // トップページ（ホーム）
    @GetMapping
    public String index(Model model, @AuthenticationPrincipal OAuth2User principal) {

        // 1. ログインユーザー名とアイコンを取得
        String me = (principal != null) ? principal.getAttribute("name") : "GuestUser";
        String userIcon = (principal != null) ? principal.getAttribute("picture") : "";
        
        // 2. ターゲット設定（現状のロジックで必要な変数）
        String target = "Admin"; 

        // 3. 投稿一覧の取得
        List<Post> posts = postRepository.findAll();
        Collections.reverse(posts); // 新着順

        // 4. 各投稿に対して自分がフォローしているかチェック
        for (Post post : posts) {
            // ここで target 変数を使用しているため、上記2番での定義が必要です
            boolean following =
                    followRepository.existsByFollowerNameAndFollowingName(me, target);
            post.setFollowing(following);
        }

        // 5. Modelに値をセット
        model.addAttribute("posts", posts);
        model.addAttribute("profileName", me);
        model.addAttribute("userIcon", userIcon); // アイコンURLを渡す
        model.addAttribute("userIcon", principal.getAttribute("picture"));
        model.addAttribute("followingCount", followRepository.countByFollowerName(me));
        model.addAttribute("followerCount", followRepository.countByFollowingName(me));

        // ここでも target 変数を使用
        boolean isFollowingAdmin =
                followRepository.existsByFollowerNameAndFollowingName(me, target);
        model.addAttribute("isFollowingAdmin", isFollowingAdmin);

        return "home"; // templates/home.html
    }
    // いいね（AJAX）
    @PostMapping("/post/{id}/like")
    @ResponseBody
    public Integer likePost(@PathVariable Long id) {
        postRepository.incrementLikes(id);
        return postRepository.findById(id)
                .map(Post::getLikesCount)
                .orElse(0);
    }

    // フォロー / アンフォロー（AJAX）
    @PostMapping("/follow/{name}")
    @ResponseBody
    public String toggleFollow(@PathVariable String name) {

        String me = "GuestUser";

        if (followRepository.existsByFollowerNameAndFollowingName(me, name)) {
            followRepository.deleteByFollowerNameAndFollowingName(me, name);
            return "unfollowed";
        }

        Follow follow = new Follow();
        follow.setFollowerName(me);
        follow.setFollowingName(name);
        followRepository.save(follow);

        return "followed";
    }

    // プロフィール表示
    @GetMapping("/profile/{name}")
    public String showProfile(@PathVariable String name, Model model) {

        String me = "GuestUser";

        model.addAttribute("profileName", name);
        model.addAttribute("followingCount",
                followRepository.countByFollowerName(name));
        model.addAttribute("followerCount",
                followRepository.countByFollowingName(name));

        boolean isFollowing =
                followRepository.existsByFollowerNameAndFollowingName(me, name);
        model.addAttribute("isFollowing", isFollowing);

        return "home"; // templates/home.html
    }
}
