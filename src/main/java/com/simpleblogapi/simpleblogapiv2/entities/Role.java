package com.simpleblogapi.simpleblogapiv2.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.*;

@Entity
@Table(name = "roles")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Role {
    @Id
    private String name;
    private String description;
    public static String USER = "USER";
    public static String ADMIN = "ADMIN";
}
