package gugus.pleco.domain.user.domain;

import gugus.pleco.domain.plee.domain.Plee;
import gugus.pleco.domain.eco.domain.UserEco;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;



@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class User implements UserDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "user_id")
    private Long id;

    @Column(name = "email")
    private String username;

    @Column(name = "password")
    private String password;

    @OneToMany(mappedBy = "user",orphanRemoval = true)
    List<Plee> pleeList = new ArrayList<>();

    @OneToMany(mappedBy = "user",orphanRemoval = true)
    List<UserEco> userEcoList = new ArrayList<>();

    @ElementCollection(fetch = FetchType.EAGER)
    private List<String> roles= new ArrayList<>();

    @Column(name = "refresh_token")
    private String refreshToken;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return roles.stream()
                .map(SimpleGrantedAuthority::new)
                .collect(Collectors.toList());
    }

    public void setRefreshToken(String token){
        this.refreshToken=token;
    }

    public void addPlee(Plee plee){
        this.pleeList.add(plee);
    }

    public void addUserEco(UserEco userEco){
        this.userEcoList.add(userEco);
    }

    @Builder
    public User(String username, String password, List<String> roles){
        this.username=username;
        this.password=password;
        this.roles=roles;
    }
    @Override
    public boolean isAccountNonExpired() {
        return false;
    }

    @Override
    public boolean isAccountNonLocked() {
        return false;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return false;
    }

    @Override
    public boolean isEnabled() {
        return false;
    }
}
