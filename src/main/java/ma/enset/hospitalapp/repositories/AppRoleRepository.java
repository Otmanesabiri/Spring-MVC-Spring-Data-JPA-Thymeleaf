package ma.enset.hospitalapp.repositories;

import ma.enset.hospitalapp.entities.AppRole;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AppRoleRepository extends JpaRepository<AppRole, String> {
}