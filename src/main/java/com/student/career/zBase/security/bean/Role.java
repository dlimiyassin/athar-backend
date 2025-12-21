package com.student.career.zBase.security.bean;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Objects;

@Getter
@Setter
@NoArgsConstructor
@Document(collection = "roles")
public class Role {

    @Id
    private String id;

    @NotBlank
    @Size(min = 1, max = 45)
    private String name;


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Role role = (Role) o;
        return id.equals(role.id) && Objects.equals(name, role.name);
    }

    @Override
    public String toString() {
        return "Role{" +
                "roleId=" + id +
                ", name='" + name + '\'' +
                '}';
    }

    public Role(String roleName) {
        this.name = roleName;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name);
    }

}
