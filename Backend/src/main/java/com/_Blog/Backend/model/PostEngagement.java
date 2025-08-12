import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "post_engagement")
public class PostEngagement {
    @Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    private Long id;
    private Long postId;
    private Long userId;
    private Boolean likeType;
}