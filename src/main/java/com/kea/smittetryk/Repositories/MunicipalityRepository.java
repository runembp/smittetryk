package com.kea.smittetryk.Repositories;

import com.kea.smittetryk.Models.Municipality;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * The repository of Municipalities - the only job of this interface is to handle operations directly with the database
 * through the extended JpaRepository class.
 */
public interface MunicipalityRepository extends JpaRepository<Municipality, Long>
{
}
