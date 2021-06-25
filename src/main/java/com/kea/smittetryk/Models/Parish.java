package com.kea.smittetryk.Models;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDate;

/**
 * Core class of Parish - has a Many to One relation with Municipality, as several Parishes can be in one Municipality, but
 * one Parish can only ever be one Municipality at a time.
 */
@Entity
@NoArgsConstructor
@Getter
@Setter
public class Parish
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long parishId;

    @Column(unique = true, nullable = false)
    private int parishCode;

    @Column(unique = true, nullable = false)
    private String parishName;

    private double infectionPercent;
    private LocalDate shutdownDate;

    @ManyToOne
    @JoinColumn(name = "municipality_id")
    private Municipality municipality;

    /**
     * When a Parish is created, it is considered to not have a Lockdown date from the beginning - this can be set in the
     * Edit-options of the parish afterwards.
     * @param parishCode
     * @param parishName
     * @param infectionPercent
     */
    public Parish(int parishCode, String parishName, double infectionPercent)
    {
        this.parishCode = parishCode;
        this.parishName = parishName;
        this.infectionPercent = infectionPercent;
        this.shutdownDate = null;
    }
}