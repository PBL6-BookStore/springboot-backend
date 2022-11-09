package com.pbl6.bookstore.domain.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.List;

/**
 * @author lkadai0801
 * @since 30/09/2022
 */

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "users")
public class UserEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "first_name", nullable = false)
    private String firstName;

    @Column(name = "last_name", nullable = false)
    private String lastName;

    @Column(name = "phone_number")
    private String phoneNumber;

    @Column(name = "avatar")
    private String avatar;

    @OneToOne(mappedBy = "user", fetch = FetchType.LAZY)
    private AccountEntity account;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "user")
    private List<ChatEntity> chats;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "user")
    private List<OrderEntity> orders;
}
