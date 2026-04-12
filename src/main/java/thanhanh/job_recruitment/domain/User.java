package thanhanh.job_recruitment.domain;

import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.persistence.*;
import lombok.*;
import thanhanh.job_recruitment.util.SecurityUtil;
import thanhanh.job_recruitment.util.constant.GenderEnum;

import java.security.Security;
import java.time.Instant;

@Entity
@Table(name = "users")
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@Builder
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(name = "name")
    private String name;

    @Column(name = "email")
    private String email;

    @Column(name = "password")
    private String password;

    @Column(name = "age")
    private int age;

    @Column(name = "gender")
    @Enumerated(EnumType.STRING)
    private GenderEnum gender;

    @Column(name = "address")
    private String address;

    @Column(name = "refreshToken")
    private String refreshToken;

    @Column(name = "createdAt")
    Instant createdAt;

    @Column(name = "updatedAt")
    Instant updatedAt;

    @Column(name = "createdBy")
    String createdBy;

    @Column(name = "updatedBy")
    String updatedBy;

    @PrePersist
    public void handleCreateUser() {
        this.createdBy = SecurityUtil.getCurrentUserLogin().isPresent()
                ? SecurityUtil.getCurrentUserLogin().get() : "";

        this.createdAt = Instant.now();
    }

    @PreUpdate
    public void handleUpdateUser() {
        this.updatedBy = SecurityUtil.getCurrentUserLogin().isPresent()
                ? SecurityUtil.getCurrentUserLogin().get() : "";

        this.updatedAt = Instant.now();
    }
}
