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
    private  String tickName;
    private float price;
    private float previousPrice;
    @OneToOne
    private Enterprise enterprise;
}