package com.dptablo.straview.dto.entity;

import com.dptablo.straview.dto.converter.RoleSetToStringConverter;
import com.dptablo.straview.dto.enumtype.Role;
import lombok.*;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "\"user\"")
@NoArgsConstructor
@AllArgsConstructor
@DynamicInsert
@DynamicUpdate
@Builder
@Getter
@Setter
public class User {
    @Id
    @Column(name = "user_id", unique = true, nullable = false)
    private String userId;

    @Column(name = "password", nullable = false)
    private String password;

    @Column(name = "roles", nullable = false)
    @Convert(converter = RoleSetToStringConverter.class)
    @Builder.Default
    private final Set<Role> roles = new HashSet<>();
}
