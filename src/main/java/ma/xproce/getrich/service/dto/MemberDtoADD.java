package ma.xproce.getrich.service.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class MemberDtoADD {
    private String username;
    private String name;
    private String password;
    private String profile;
    private UUID token;
}
