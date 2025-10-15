package com._Blog.Backend.faker;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import com._Blog.Backend.model.*;
import com._Blog.Backend.repository.*;
import com._Blog.Backend.utils.Role;

import net.datafaker.Faker;
import org.springframework.security.crypto.password.PasswordEncoder;

@Component
public class DatabaseSeeder implements CommandLineRunner {

    private final UserRepository userRepository;
    private final UserRoleRepository roleRepository;
    private final PostRepository postRepository;
    private final PostEngagementRepository engagementRepository;
    private final CommentRepository commentRepository;
    private final CommentEngagementRepository commentEngagementRepository;
    private final FollowRepository followRepository;
    private final NotificationRepository notificationRepository;
    private final ReportUserRepository reportUserRepository;
    private final ReportPostRepository reportPostRepository;
    private final PasswordEncoder passwordEncoder;

    private final Faker faker = new Faker();
    private final Random random = new Random();

    public DatabaseSeeder(
            UserRepository userRepository,
            UserRoleRepository roleRepository,
            PostRepository postRepository,
            PostEngagementRepository engagementRepository,
            CommentRepository commentRepository,
            CommentEngagementRepository commentEngagementRepository,
            FollowRepository followRepository,
            NotificationRepository notificationRepository,
            ReportUserRepository reportUserRepository,
            ReportPostRepository reportPostRepository,
            PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.postRepository = postRepository;
        this.engagementRepository = engagementRepository;
        this.commentRepository = commentRepository;
        this.commentEngagementRepository = commentEngagementRepository;
        this.followRepository = followRepository;
        this.notificationRepository = notificationRepository;
        this.reportUserRepository = reportUserRepository;
        this.reportPostRepository = reportPostRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    @Transactional
    public void run(String... args) {

        // 1️⃣ Users & Roles
        List<User> users = new ArrayList<>();
        for (int i = 0; i < 100; i++) {
            User user = new User();
            user.setUsername(faker.name().username());
            user.setEmail(faker.internet().emailAddress());
            user.setPassword(passwordEncoder.encode("password123"));
            user.setBio(faker.lorem().sentence());
            user.setIsBanned(random.nextBoolean());

            User savedUser = userRepository.save(user);

            UserRole role = new UserRole();
            role.setRole(random.nextBoolean() ? Role.USER : Role.ADMIN);
            role.setUser(savedUser);
            roleRepository.save(role);

            savedUser.getRoles().add(role);
            users.add(savedUser);
        }

        // 2️⃣ Posts & PostEngagements
        List<Post> posts = new ArrayList<>();
        for (User user : users) {
            int postCount = random.nextInt(5) + 1;
            for (int i = 0; i < postCount; i++) {
                Post post = new Post();
                post.setTitle(faker.lorem().sentence(3, 5));
                post.setContent(faker.lorem().paragraph(2));
                post.setUser(user);
                Post savedPost = postRepository.save(post);
                posts.add(savedPost);

                // Engagements
                int likes = random.nextInt(users.size() / 2);
                for (int j = 0; j < likes; j++) {
                    User liker = users.get(random.nextInt(users.size()));
                    PostEngagement engagement = new PostEngagement(savedPost, liker);
                    engagementRepository.save(engagement);
                    savedPost.getEngagements().add(engagement);
                }
            }
        }

        // 3️⃣ Comments & CommentEngagements
        List<Comment> comments = new ArrayList<>();
        for (Post post : posts) {
            int commentCount = random.nextInt(200);
            for (int i = 0; i < commentCount; i++) {
                User commenter = users.get(random.nextInt(users.size()));
                Comment comment = new Comment();
                comment.setContent(faker.lorem().sentence());
                comment.setUser(commenter);
                comment.setPost(post);
                Comment savedComment = commentRepository.save(comment);
                post.getComments().add(savedComment);
                comments.add(savedComment);

                // Comment engagements
                int ceCount = random.nextInt(users.size() / 2);
                for (int j = 0; j < ceCount; j++) {
                    User engager = users.get(random.nextInt(users.size()));
                    CommentEngagement ce = new CommentEngagement(engager, savedComment);
                    commentEngagementRepository.save(ce);
                    savedComment.getEngagements().add(ce);
                }
            }
        }

        // 4️⃣ Follows
        Set<String> existingFollows = new HashSet<>();
        for (User user : users) {
            int followingCount = random.nextInt(users.size() / 3);
            for (int i = 0; i < followingCount; i++) {
                User followed = users.get(random.nextInt(users.size()));
                if (!followed.equals(user)) {
                    String key = user.getId() + "-" + followed.getId();
                    if (!existingFollows.contains(key)) {
                        Follow follow = new Follow();
                        follow.setFollower(user);
                        follow.setFollowed(followed);
                        followRepository.save(follow);
                        existingFollows.add(key);

                        // maintain bidirectional mapping
                        user.getFollowing().add(follow);
                        followed.getFollowers().add(follow);
                    }
                }
            }
        }

        // 5️⃣ Notifications
        for (int i = 0; i < 50; i++) {
            User sender = users.get(random.nextInt(users.size()));
            User receiver = users.get(random.nextInt(users.size()));
            Post post = posts.get(random.nextInt(posts.size()));

            Notification notification = new Notification();
            notification.setSender(sender);
            notification.setReceiver(receiver);
            notification.setPost(post);
            notification.setDescription(faker.lorem().sentence());
            notificationRepository.save(notification);

            sender.getSentNotifications().add(notification);
            receiver.getReceivedNotifications().add(notification);
        }

        // 6️⃣ Report Users & Posts
        for (int i = 0; i < 50; i++) {
            User reporter = users.get(random.nextInt(users.size()));
            User reportedUser = users.get(random.nextInt(users.size()));
            ReportUser reportUser = new ReportUser(reporter, reportedUser, faker.lorem().sentence());
            reportUserRepository.save(reportUser);

            User rpReporter = users.get(random.nextInt(users.size()));
            Post reportedPost = posts.get(random.nextInt(posts.size()));
            ReportPost reportPost = new ReportPost(rpReporter, reportedPost.getUser(), reportedPost,
                    faker.lorem().sentence());
            reportPostRepository.save(reportPost);
        }

        System.out.println("✅ Database seeded successfully!");
    }
}
