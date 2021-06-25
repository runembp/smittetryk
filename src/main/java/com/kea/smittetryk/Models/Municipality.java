package com.kea.smittetryk.Models;

import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

/**
 * Core class of Municipality - has a One to Many relation with Parish, as there can be several Parishes in one Municipality
 */
@Entity
@NoArgsConstructor
@Getter
@Setter
public class Municipality
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long municipalityId;

    @Column(unique = true)
    private String municipalityName;

    @OneToMany(mappedBy = "municipality", cascade = CascadeType.ALL)
    @JsonBackReference
    private Set<Parish> parishes = new HashSet<>();
}
