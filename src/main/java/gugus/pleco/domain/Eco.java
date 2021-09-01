package gugus.pleco.domain;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Eco {

    @Id @GeneratedValue
    @Column(name = "eco_id")
    private Long id;

    @Column(name = "eco_name")
    private String ecoName;

    @Column(name = "cool_time")
    private Long coolTime;


    @OneToMany(mappedBy = "eco")
    private List<UserEco> userEcoList = new ArrayList<>();

}
