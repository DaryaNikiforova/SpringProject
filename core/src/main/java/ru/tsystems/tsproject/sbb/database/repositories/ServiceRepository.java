package ru.tsystems.tsproject.sbb.database.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.tsystems.tsproject.sbb.database.entity.Service;

/**
 * Created by apple on 11.11.14.
 */
public interface ServiceRepository extends JpaRepository<Service, Integer> {
    Service findByName(String name);
}
