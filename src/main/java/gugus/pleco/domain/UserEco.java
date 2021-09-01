package gugus.pleco.domain;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class UserEco {

    @Id
    @GeneratedValue
    @Column(name = "user_eco_id")
    private Long id;

    @Column(name = "perform_tme")
    private LocalDateTime performTime;


    @JoinColumn(name="user_id")
    @ManyToOne(fetch = FetchType.LAZY)
    private User user;

    @JoinColumn(name="eco_id")
    @ManyToOne(fetch = FetchType.LAZY)
    private Eco eco;

    //연관 관계 로직
    public void setUser(User user){
        this.user=user;
        user.userEcoList.add(this);
    }

    public void setEco(Eco eco){
        this.eco=eco;
    }

    public void setPerformTime(LocalDateTime performTime){
        this.performTime=performTime;
    }

    // 생성 로직  user가 회원 가입하면 바로 Eco들을(미션 리스트) 모두 저장해서 바로 서비스가 가능하게 짜야 될 듯??
    public static UserEco createEco(User user, Eco eco){
        UserEco userEco = new UserEco();
        userEco.setEco(eco);
        userEco.setUser(user);
        return userEco;
    }
}
