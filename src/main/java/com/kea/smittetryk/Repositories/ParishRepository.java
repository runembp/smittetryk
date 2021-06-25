package com.kea.smittetryk.Repositories;

import com.kea.smittetryk.Models.Parish;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * The repository of Parishes - the only job of this interface is to handle operations directly with the database
 * through the extended JpaRepository class.
 */
public interface ParishRepository extends JpaRepository<Parish, Long>
{
}
