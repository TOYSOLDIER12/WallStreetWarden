package ma.xproce.getrich.dao.entities;

import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.Collection;

@AllArgsConstructor
@NoArgsConstructor
@ToString
@Getter
@Setter
@Entity

public class Enterprise {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private String name;
    @OneToOne (mappedBy = "enterprise", fetch = FetchType.EAGER)
    private Stock stock;
    @ManyToMany( mappedBy = "enterprises", fetch = FetchType.LAZY)
    private Collection<Member> members = new ArrayList<>();
    public boolean addUser(Member member) {
        return this.members.add(member);
    }
}
