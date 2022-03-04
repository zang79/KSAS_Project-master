package KJcompany.KSAS.config;

import javax.sql.DataSource;

import KJcompany.KSAS.common.security.CustomAccessDeniedHandler;
import KJcompany.KSAS.common.security.CustomLoginSuccessHandler;
import KJcompany.KSAS.common.security.CustomUserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.actuate.autoconfigure.security.servlet.EndpointRequest;
import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.rememberme.JdbcTokenRepositoryImpl;
import org.springframework.security.web.authentication.rememberme.PersistentTokenRepository;

@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled=true, securedEnabled=true)
public class SecurityConfig extends WebSecurityConfigurerAdapter{
	
	@Autowired
	DataSource dataSource;
	
	@Override
	protected void configure(HttpSecurity http) throws Exception {
		
		http.authorizeRequests()
		.requestMatchers(PathRequest.toStaticResources().atCommonLocations()).permitAll()
		.requestMatchers(EndpointRequest.toAnyEndpoint()).hasAnyRole("ADMIN")
		.antMatchers("/").permitAll()
		.antMatchers("/auth/login").permitAll()
		.antMatchers("/user/setup", "/user/register", "/user/registerSuccess").permitAll()
		.antMatchers("/user/**").hasRole("ADMIN")
		.antMatchers("/codegroup/**").hasRole("ADMIN")
		.antMatchers("/codedetail/**").hasRole("ADMIN")
		.antMatchers("/board/list", "/board/read").permitAll()
		.antMatchers("/board/remove").hasAnyRole("MEMBER", "ADMIN")
		.antMatchers("/board/**").hasRole("MEMBER")
		.antMatchers("/notice/list", "/notice/read").permitAll()
		.antMatchers("/notice/**").hasRole("ADMIN")
		.antMatchers("/item/list", "/item/read", "/item/display").permitAll()
		.antMatchers("/item/picture").hasAnyRole("MEMBER", "ADMIN")
		.antMatchers("/item/buy", "/item/success").hasRole("MEMBER")
		.antMatchers("/item/**").hasRole("ADMIN")
		.antMatchers("/coin/**").hasRole("MEMBER")
		.antMatchers("/useritem/**").hasRole("MEMBER")
		.antMatchers("/pds/list", "/pds/read", "/pds/getAttach/**", "/pds/downloadFile").permitAll()
		.antMatchers("/pds/**").hasRole("ADMIN")
		.antMatchers("/error/**").permitAll()
		.anyRequest().authenticated();		
		
		http.formLogin()
		.loginPage("/auth/login")
		.loginProcessingUrl("/login")
		.successHandler(authenticationSuccessHandler());
		
		http.logout()
		.logoutUrl("/auth/logout")
		.logoutSuccessUrl("/");
		
		http.exceptionHandling()
		.accessDeniedHandler(accessDeniedHandler());
		
		http.rememberMe()
		.key("KsJjE4VzhhEW3juG")
		.tokenRepository(createJDBCRepository())
		.tokenValiditySeconds(60*60*24);
	}
	
	@Bean
	public UserDetailsService customUserDetailsService() {
		return new CustomUserDetailsService();
	}
	
	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}
	
	@Bean
	public AuthenticationSuccessHandler authenticationSuccessHandler() {
		return new CustomLoginSuccessHandler();
	}
	
	@Bean
	public AccessDeniedHandler accessDeniedHandler() {
		return new CustomAccessDeniedHandler();
	}
	
	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
		auth.userDetailsService(customUserDetailsService())
		.passwordEncoder(passwordEncoder());
	}
	
	private PersistentTokenRepository createJDBCRepository() {
		JdbcTokenRepositoryImpl repo = new JdbcTokenRepositoryImpl();
		repo.setDataSource(dataSource);
		
		return repo;
	}
	
}
