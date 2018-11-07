package company.bigger.web.config
import company.bigger.service.UserService
import company.bigger.service.UserService.Companion.ROLE
import company.bigger.web.filter.TokenAuthenticationFilter
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter

@Configuration
@EnableWebSecurity
open class WebSecurityConfig(
    private val userService: UserService
) : WebSecurityConfigurerAdapter() {

    @Bean
    @Throws(Exception::class)
    open fun jwtAuthenticationTokenFilter(): TokenAuthenticationFilter {
        return TokenAuthenticationFilter(userService)
    }

    override fun configure(http: HttpSecurity) {

        http
                .addFilterBefore(jwtAuthenticationTokenFilter(), BasicAuthenticationFilter::class.java)
                .authorizeRequests()
                .antMatchers("/swagger-ui.html").permitAll()
                .antMatchers("/swagger-ui.html").permitAll()
                .antMatchers("/webjars/**").permitAll()
                .antMatchers("/session/**").permitAll()
                .antMatchers("/graphiql/**").permitAll()
                .antMatchers("/swagger-resources/**").permitAll()
                .antMatchers("/subscriptions/**").permitAll()
                .antMatchers("/vendor/**").permitAll()
                .antMatchers("/actuator/**").permitAll()

                .anyRequest().authenticated()
                .anyRequest().hasAuthority(ROLE)

                // From https://github.com/bfwg/springboot-jwt-starter
                .and().csrf().disable()
    }
}