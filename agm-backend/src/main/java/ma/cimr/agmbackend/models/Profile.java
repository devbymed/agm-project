// package ma.cimr.agmbackend.models;

// import java.time.LocalDateTime;
// import java.util.List;

// import org.springframework.data.annotation.CreatedDate;
// import org.springframework.data.annotation.LastModifiedDate;

// import jakarta.persistence.CascadeType;
// import jakarta.persistence.Column;
// import jakarta.persistence.Entity;
// import jakarta.persistence.GeneratedValue;
// import jakarta.persistence.GenerationType;
// import jakarta.persistence.Id;
// import jakarta.persistence.OneToMany;
// import jakarta.persistence.Table;
// import jakarta.validation.constraints.NotBlank;
// import lombok.AllArgsConstructor;
// import lombok.Data;
// import lombok.NoArgsConstructor;

// @Data
// @Entity
// @NoArgsConstructor
// @AllArgsConstructor
// @Table(name = "profiles")
// public class Profile {
// @Id
// @GeneratedValue(strategy = GenerationType.IDENTITY)
// private Long id;

// @Column(nullable = false, unique = true)
// @NotBlank(message = "Le nom du profil est obligatoire")
// private String name;

// @OneToMany(mappedBy = "profile", cascade = CascadeType.ALL, orphanRemoval =
// true)
// private List<User> users;

// @CreatedDate
// @Column(name = "created_at", nullable = false, updatable = false)
// private LocalDateTime createdAt;

// @LastModifiedDate
// @Column(name = "updated_at", insertable = false)
// private LocalDateTime updatedAt;
// }
