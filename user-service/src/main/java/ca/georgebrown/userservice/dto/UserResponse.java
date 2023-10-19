package ca.georgebrown.userservice.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserResponse {

        private String id;
        private String username;
        private String email;
        private String fullName;

        public UserResponse(String username) {
                this.username = username;
        }
}
