package io.monitoring.model.payload;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RegisterRequest {
    @NonNull
    private String username;
    @NonNull
    private String password;
    private Set<String> roles;
}
