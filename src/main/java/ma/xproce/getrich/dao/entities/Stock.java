package ma.xproce.getrich.dao.entities;


import jakarta.persistence.*;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@ToString
@Getter
@Setter
@Entity

public class Stock {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private  String name;
    private float price;
    @OneToOne
    private Enterprise enterprise;
    @OneToOne (mappedBy = "stock", fetch = FetchType.EAGER)
    private Chart chart;
}