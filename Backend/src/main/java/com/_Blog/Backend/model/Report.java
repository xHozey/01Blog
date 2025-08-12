import java.sql.Timestamp;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "report")
public class Report {
    @Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    private Long id;
    private Long reporterId;
    private Long reportedId;
    private String description;
    private Timestamp date;
}