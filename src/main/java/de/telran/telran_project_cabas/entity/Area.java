package de.telran.telran_project_cabas.entity;

import lombok.*;

import javax.persistence.*;

@Entity
@Table(name = "area")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class Area {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "area_id")
    private Long areaId;

    @Column(name = "area_name")
    private String areaName;
}
