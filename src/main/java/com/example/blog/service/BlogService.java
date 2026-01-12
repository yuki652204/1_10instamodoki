package com.example.blog.service;

import java.util.List;
import java.util.Collections;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.blog.repository.PostRepository;
import com.example.blog.models.Post;    // データベース保存用のEntity
import com.example.blog.form.PostForm;

@Service
public class BlogService {

    @Autowired
    private PostRepository postRepository;

    // 全投稿を取得
    public List<Post> getAllPostsNewestFirst() {
        List<Post> posts = postRepository.findAll();
        Collections.reverse(posts);
        return posts;
    }

    // ★ここがControllerの29行目で呼ばれているメソッドです
    public void savePost(PostForm postForm) {
        Post post = new Post();
        // Form(入力データ)からEntity(保存データ)へ詰め替える
     // Title → ImageUrl に、Content → Comment に合わせる
        post.setImageUrl(postForm.getTitle()); // Form側の名前がTitleなら一旦これ
        post.setComment(postForm.getContent());
        postRepository.save(post);
    }

    // IDで1件取得
    public Post getPostById(Long id) {
        return postRepository.findById(id).orElseThrow();
    }

    // 削除
    public void deletePost(Long id) {
        postRepository.deleteById(id);
    }
    
    // 更新
    public void updatePost(PostForm postForm) {
        Post post = postRepository.findById(postForm.getId()).orElseThrow();
     // Title → ImageUrl に、Content → Comment に合わせる
        post.setImageUrl(postForm.getTitle()); // Form側の名前がTitleなら一旦これ
        post.setComment(postForm.getContent());
        postRepository.save(post);
    }
}