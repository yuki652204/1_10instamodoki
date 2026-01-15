package com.example.blog.service;

import java.util.Collections;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.blog.form.PostForm;
import com.example.blog.models.Post;
import com.example.blog.repository.PostRepository;

@Service
@Transactional
public class BlogService {

    private final PostRepository postRepository;

    // ✅ コンストラクタインジェクション
    public BlogService(PostRepository postRepository) {
        this.postRepository = postRepository;
    }

    // 全投稿を新着順で取得
    @Transactional(readOnly = true)
    public List<Post> getAllPostsNewestFirst() {
        List<Post> posts = postRepository.findAll();
        Collections.reverse(posts);
        return posts;
    }

    // 新規投稿
    public void savePost(PostForm postForm) {
        Post post = new Post();

        // 最新 Post.java に合わせて手動でセット
        post.setImageUrl(postForm.getTitle());   // ImageUrl にセット
        post.setComment(postForm.getContent());  // Comment にセット
        postRepository.save(post);
    }

    // IDで1件取得
    @Transactional(readOnly = true)
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

        // 最新 Post.java に合わせて手動でセット
        post.setImageUrl(postForm.getTitle());
        post.setComment(postForm.getContent());
        postRepository.save(post);
    }

    public PostRepository getPostRepository() {
        return postRepository;
    }
}
