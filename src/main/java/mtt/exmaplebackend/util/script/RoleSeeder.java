package mtt.exmaplebackend.util.script;

import lombok.RequiredArgsConstructor;
import mtt.exmaplebackend.model.Role;
import mtt.exmaplebackend.model.RoleDef;
import mtt.exmaplebackend.repository.RoleRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class RoleSeeder implements CommandLineRunner {
    final RoleRepository roleRepository;

    @Override
    public void run(String... args) throws Exception {
        for (RoleDef roleDef : RoleDef.values()) {
            roleRepository.findByName(roleDef.name()).orElseGet(() -> roleRepository.save(Role.builder().name(roleDef.name()).build()));
        }
    }
}
