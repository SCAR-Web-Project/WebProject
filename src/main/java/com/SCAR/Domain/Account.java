package com.SCAR.Domain;

import com.SCAR.Account.RoleType;
import com.SCAR.BaseTimeEntity;
import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Getter @EqualsAndHashCode(of = "id", callSuper = false)
@NoArgsConstructor @AllArgsConstructor @Builder
public class Account extends BaseTimeEntity {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true)
    private String SKKEmail;

    @Column(unique = true)
    private String email;

    @Column(unique = true)
    private String nickname;

    private String password;

    @Enumerated(EnumType.STRING)
    private RoleType Role;

    private boolean emailChecked;

    private String checkEmailToken;

    private LocalDateTime emailCheckTokenGeneratedAt;

    @Lob @Basic(fetch = FetchType.EAGER)
    private String ProfileImage;

}
