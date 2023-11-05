package nl.dannyj.bookmarkmanager.transformer;

import lombok.NonNull;
import nl.dannyj.bookmarkmanager.dto.UserDTO;
import nl.dannyj.bookmarkmanager.model.UserModel;

public class UserTransformer implements Transformer<UserModel, UserDTO> {
    @Override
    public UserModel toModel(@NonNull UserDTO dto) {
        return UserModel.builder()
                .username(dto.getUsername())
                .build();
    }

    @Override
    public UserDTO toDto(@NonNull UserModel model) {
        return new UserDTO(model.getUsername());
    }
}
