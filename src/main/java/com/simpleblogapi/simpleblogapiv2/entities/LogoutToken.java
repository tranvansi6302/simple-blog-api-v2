package com.simpleblogapi.simpleblogapiv2.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.*;

import java.util.Date;
@Entity
@Table(name = "logout_tokens")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LogoutToken {
    @Id
    private String id;
    private Date expiryDate;
}
