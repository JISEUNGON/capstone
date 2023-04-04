package com.danram.server.domain;

import lombok.*;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "token_practice")
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Tokens {
    @Id
    @Column(name = "user_id", columnDefinition = "int")
    private Long userId;

    @Column(name = "access_token", columnDefinition = "text")
    private String accessToken;

    @Column(name = "refresh_token", columnDefinition = "text")
    private String refreshToken;
}
