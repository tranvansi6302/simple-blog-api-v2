package com.simpleblogapi.simpleblogapiv2.entities;

import com.simpleblogapi.simpleblogapiv2.enums.PostStatus;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "posts")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Post extends BaseEntity{
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;
    @Column(nullable = false)
    private String title;

    private String thumbnail;
    private String content;

    @Enumerated(EnumType.STRING)
    private PostStatus status;
    @ManyToOne
    private Category category;

    @ManyToOne
    private User author;
}
