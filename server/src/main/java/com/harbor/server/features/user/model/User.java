package com.harbor.server.features.user.model;

import com.harbor.server.features.organization.model.Organization;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name = "users")
@Getter
@NoArgsConstructor
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Setter
    @Column(length = 50, nullable = false)
    private String firstName;

    @Setter
    @Column(length = 50, nullable = false)
    private String lastName;

    @Setter
    @Column(length = 100, nullable = false)
    private String passwordHash;

    @Setter
    @Column(unique = true, nullable = false, length = 100)
    private String email;

    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Setter
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private OrganizationRole role;

    @Setter
    @ManyToOne(optional = false)
    @JoinColumn(name = "organization_id", nullable = false)
    private Organization organization;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
    }

    public User(
            String firstName,
            String lastName,
            String email,
            String passwordHash,
            OrganizationRole role,
            Organization organization
    ) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.passwordHash = passwordHash;
        this.role = role;
        this.organization = organization;
    }
}
