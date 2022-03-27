package edu.cuit.lushan.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.HashSet;
import java.util.Set;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Authorization {
    private Integer userId;
    private Set<String> roles;

    private void addRole(String role) {
        if (this.getRoles() == null) {
            Set set = new HashSet<String>();
            set.add(role);
            this.setRoles(set);
        } else {
            this.getRoles().add(role);
        }
    }

    private void addRoles(Set<String> roles) {
        if (roles == null) {
            return;
        } else {
            roles.forEach(role -> {
                this.getRoles().add(role);
            });
        }
    }
}
