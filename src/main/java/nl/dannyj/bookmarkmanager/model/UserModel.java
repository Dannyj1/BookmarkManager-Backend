package nl.dannyj.bookmarkmanager.model;

import jakarta.persistence.Column;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.DynamicUpdate;

@NoArgsConstructor
@AllArgsConstructor
@Data
@DynamicUpdate
@Builder
public class UserModel {

    @Id
    @GeneratedValue
    @NotNull
    private long id;

    @NotNull
    @Column(unique = true)
    private String username;

    @NotNull
    private String passwordHash;

}
