package dev.archimedes.springbootsecurity.Model;


import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Data
@ToString
@Builder
public class JwtRequest {
    private String email;
    private String password;
}
