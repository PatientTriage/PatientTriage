package repository;

import entity.Record;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * JpaRepository automatically generate SQL to call data
 *
 * Record: show that this is the repo for the Record Entity
 * Long: show that the primary key (id) is Long
 */
@Repository
public interface RecordRepository extends JpaRepository<Record, Long> {
  // TODO: add necessary signatures
}
