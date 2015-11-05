#Adding a client
To add a security client or resource server.

1.  Add below depenency to pom
<dependency>
	<groupId>org.springframework.boot</groupId>
	<artifactId>spring-boot-starter-redis</artifactId>
</dependency>
2.  Add @EnableRedisHttpSession to the application
3.  Have application extend WebSecurityConfigurerAdapter and add the configuration method
@Override
	protected void configure(HttpSecurity http) throws Exception {
		// We need this to prevent the browser from popping up a dialog on a 401
		http.httpBasic().disable();
		http.authorizeRequests().anyRequest().authenticated();		
	}
4.  Add below config to application.yml
security:
  sessions: NEVER
  
