package com.studydocs.modules.academic.entity;

import com.studydocs.shared.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "universities")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UniversityEntity extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 200)
    private String name;

    @Column(length = 50)
    private String code;

    @Column(length = 500)
    private String logoUrl;

    @Column(length = 500)
    private String address;
}
