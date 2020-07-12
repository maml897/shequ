package com.config.security;

import javax.annotation.Resource;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.access.expression.SecurityExpressionHandler;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.FilterInvocation;
import org.springframework.security.web.authentication.logout.SimpleUrlLogoutSuccessHandler;

import com.service.UserService;

@Configuration
public class HttpSecurityConfigDefault extends WebSecurityConfigurerAdapter
{
	@Resource
	private UserService useService;

	@Bean
	public SecurityExpressionHandler<FilterInvocation> expressionHandler()
	{
		SecurityExpressionHandler<FilterInvocation> expressionHandler = new CustomWebSecurityExpressionHandler();
		return expressionHandler;
	}

	@Override
	public void configure(WebSecurity web) throws Exception
	{
		web.ignoring().antMatchers("/")
		.antMatchers("/images/**")
		.antMatchers("/css/**")
		.antMatchers("/js/**")
		.antMatchers("/svg/**")
		.antMatchers("/pdf/**")
		.antMatchers("/component/**")
		.antMatchers("/error/**")
		.antMatchers("/login/login-fail")
		.antMatchers("/register")
		.antMatchers("/register/**");
	}

	@Override
	protected void configure(HttpSecurity http) throws Exception
	{
		// http.exceptionHandling().accessDeniedPage("/error/403");
		http.csrf().disable();
		http.authorizeRequests().expressionHandler(expressionHandler());
		http.headers().frameOptions().disable();
		http.sessionManagement().invalidSessionUrl("/").maximumSessions(10).expiredUrl("/error/sessionexpired");

		//登录
		http.formLogin().loginPage("/").loginProcessingUrl("/SystemLogin").usernameParameter("username").passwordParameter("password").defaultSuccessUrl("/login/login-success", true).failureUrl("/login/login-fail");

		// 注销
		http.logout().logoutUrl("/SystemLogout").logoutSuccessHandler(logoutSuccessHandler());
		configAuthorizeRequests(http);
	}

	private void configAuthorizeRequests(HttpSecurity http) throws Exception
	{
		http.authorizeRequests().antMatchers("/").permitAll(); // , "/index.shtml","/speed-test.shtml"
		
		http.authorizeRequests()
			.antMatchers("/system/**").hasAnyAuthority("ROLE_ADMIN")
			.antMatchers("/system/**").access("hasAnyAuthority(ADMIN)")
			.antMatchers("/demo/**").access("isAnonymous() or isFullyAuthenticated()")
			.antMatchers("/**").access("hasAnyAuthority(USER)");
		
		http.authorizeRequests().antMatchers("/r/**").access("isAnonymous() or isFullyAuthenticated()");
		http.authorizeRequests().anyRequest().fullyAuthenticated();
	}

	// -----------Bean--------------------------
	@Bean
	public SimpleUrlLogoutSuccessHandler logoutSuccessHandler()
	{
		SimpleUrlLogoutSuccessHandler logoutSuccessHandler = new SimpleUrlLogoutSuccessHandler();
		logoutSuccessHandler.setDefaultTargetUrl("/");
		logoutSuccessHandler.setTargetUrlParameter("logoutSuccessUrl");

		return logoutSuccessHandler;
	}
	
	@Bean  
    public static PasswordEncoder passwordEncoder(){  
        return NoOpPasswordEncoder.getInstance();  
   }  
}
