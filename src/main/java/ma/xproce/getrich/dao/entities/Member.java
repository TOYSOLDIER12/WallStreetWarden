package ma.xproce.getrich.dao.entities;


import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.Collection;

@ToString
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Member {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private String name;
    private String password;
    private String username;
    private String profile;
    private int Numb_Of_Interactions;
    @ManyToMany (fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private Collection<Enterprise> enterprises = new ArrayList<>();
    public boolean addEnterprise(Enterprise enterprise) {
        return this.enterprises.add(enterprise);
    }
}
