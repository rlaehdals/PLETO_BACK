package gugus.pleco.domain.eco.domain;

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

    @Id @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "eco_id")
    private Long id;

    @Column(name = "eco_name")
    private String ecoName;

    @Column(name = "cool_time")
    private Long coolTime;


    @OneToMany(mappedBy = "eco")
    private List<UserEco> userEcoList = new ArrayList<>();


    private void setEcoName(String ecoName) {
        this.ecoName = ecoName;
    }

    private void setCoolTime(Long coolTime) {
        this.coolTime = coolTime;
    }

    // 생성 로직
    public static Eco createEco(String ecoName, Long coolTime){
        Eco eco = new Eco();
        eco.setEcoName(ecoName);
        eco.setCoolTime(coolTime);
        return eco;
    }

}
