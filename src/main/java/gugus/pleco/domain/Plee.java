package gugus.pleco.domain;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Plee {
    @Id
    @GeneratedValue
    @Column(name = "plee_id")
    private Long id;

    @Column(name = "plee_name")
    private String pleeName;

    @Column(name = "eco_count")
    private Long ecoCount;

    @Enumerated(EnumType.STRING)
    private PleeStatus pleeStatus;

    @JoinColumn(name="user_id")
    @ManyToOne(fetch = FetchType.LAZY)
    private User user;


    //이름 설정
    private void setPleeName(String pleeName){
        this.pleeName=pleeName;
    }

    //연관 관계 로직
    public void setUser(User user){
        this.user=user;
        user.pleeList.add(this);
    }

    //생성 로직
    public static Plee createPlee(User user, String pleeName){
        Plee plee = new Plee();
        plee.setUser(user);
        plee.setPleeName(pleeName);
        return plee;
    }

}
