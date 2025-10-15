import net.datafaker.Faker;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.Random;
import java.util.Set;

import org.springframework.security.crypto.password.PasswordEncoder;

import com._Blog.Backend.model.Notification;
import com._Blog.Backend.model.Post;
import com._Blog.Backend.model.PostEngagement;
import com._Blog.Backend.model.ReportPost;
import com._Blog.Backend.model.ReportUser;
import com._Blog.Backend.model.User;
import com._Blog.Backend.model.UserRole;
import com._Blog.Backend.repository.NotificationRepository;
import com._Blog.Backend.repository.PostEngagementRepository;
import com._Blog.Backend.repository.PostRepository;
import com._Blog.Backend.repository.ReportPostRepository;
import com._Blog.Backend.repository.ReportUserRepository;
import com._Blog.Backend.repository.UserRepository;
import com._Blog.Backend.repository.UserRoleRepository;
import com._Blog.Backend.utils.Role;
import net.datafaker.*;;

public class DatabaseSeeder implements CommandLineRunner {
    @Autowired
    private final UserRepository userRepository;
    @Autowired
    private final UserRoleRepository roleRepository;
    @Autowired
    private final PostRepository postRepository;
    @Autowired
    private final PostEngagementRepository engagementRepository;
    @Autowired
    private final ReportUserRepository reportUserRepository;
    @Autowired
    private final ReportPostRepository reportPostRepository;
    @Autowired
    private final NotificationRepository notificationRepository;
    @Autowired
    private final PasswordEncoder passwordEncoder;
    
    private final Faker faker = new Faker();
    private final Random random = new Random();

    public DatabaseSeeder(
            UserRepository userRepository,
            UserRoleRepository roleRepository,
            PostRepository postRepository,
            PostEngagementRepository engagementRepository,
            ReportUserRepository reportUserRepository,
            ReportPostRepository reportPostRepository,
            NotificationRepository notificationRepository,
            PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.postRepository = postRepository;
        this.engagementRepository = engagementRepository;
        this.reportUserRepository = reportUserRepository;
        this.reportPostRepository = reportPostRepository;
        this.notificationRepository = notificationRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    @Transactional
    public void run(String... args) {

        // 1️⃣ Users
        Set<User> users = new HashSet<>();
        for (int i = 0; i < 20; i++) {
            User user = new User();
            user.setUsername(faker.name().username());
            user.setEmail(faker.internet().emailAddress());
            user.setPassword(this.passwordEncoder.encode("123456789"));
            user.setBio(faker.lorem().sentence());
            user.setIsBanned(random.nextBoolean());

            // Roles
            UserRole role = new UserRole();
            role.setRole(random.nextBoolean() ? Role.ADMIN : Role.USER);
            role.setUser(user);
            user.getRoles().add(role);
            roleRepository.save(role);
            users.add(userRepository.save(user));
        }

        // 2️⃣ Posts
        Set<Post> posts = new HashSet<>();
        for (User user : users) {
            int userPosts = random.nextInt(5) + 1; // 1-5 posts per user
            for (int i = 0; i < userPosts; i++) {
                Post post = new Post();
                post.setTitle(faker.lorem().sentence(5, 10));
                post.setContent(faker.lorem().paragraph(5));
                post.setUser(user);
                posts.add(postRepository.save(post));
            }
        }

        // 3️⃣ Post Engagements
        for (Post post : posts) {
            int likes = random.nextInt(10);
            for (int i = 0; i < likes; i++) {
                PostEngagement engagement = new PostEngagement();
                engagement.setPost(post);
                engagement
                        .setUser(users.stream().skip(random.nextInt(users.size())).findFirst().orElse(post.getUser()));
                engagementRepository.save(engagement);
            }
        }

        // 4️⃣ Report Users
        for (int i = 0; i < 5; i++) {
            ReportUser reportUser = new ReportUser();
            reportUser.setReporter(users.stream().skip(random.nextInt(users.size())).findFirst().orElse(null));
            reportUser.setReportedUser(users.stream().skip(random.nextInt(users.size())).findFirst().orElse(null));
            reportUser.setDescription(faker.lorem().sentence());
            reportUserRepository.save(reportUser);
        }

        // 5️⃣ Report Posts
        for (int i = 0; i < 5; i++) {
            ReportPost reportPost = new ReportPost();
            reportPost.setReporter(users.stream().skip(random.nextInt(users.size())).findFirst().orElse(null));
            reportPost.setReportedUser(users.stream().skip(random.nextInt(users.size())).findFirst().orElse(null));
            reportPost.setReportedPost(posts.stream().skip(random.nextInt(posts.size())).findFirst().orElse(null));
            reportPost.setDescription(faker.lorem().sentence());
            reportPostRepository.save(reportPost);
        }

        // 6️⃣ Notifications
        for (int i = 0; i < 10; i++) {
            Notification notification = new Notification();
            notification.setSender(users.stream().skip(random.nextInt(users.size())).findFirst().orElse(null));
            notification.setReceiver(users.stream().skip(random.nextInt(users.size())).findFirst().orElse(null));
            notification.setPost(posts.stream().skip(random.nextInt(posts.size())).findFirst().orElse(null));
            notification.setDescription(faker.lorem().sentence());
            notificationRepository.save(notification);
        }

        System.out.println("✅ Database seeded with fake data!");
    }
}
