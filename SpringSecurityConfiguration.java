/*
 * The MIT License (MIT)
 * Copyright (C) 2012 Jason Ish
 *
 * Permission is hereby granted, free of charge, to any person
 * obtaining a copy of this software and associated documentation
 * files (the Software), to deal in the Software without
 * restriction, including without limitation the rights to use, copy,
 * modify, merge, publish, distribute, sublicense, and/or sell copies
 * of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be
 * included in all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED AS IS, WITHOUT WARRANTY OF ANY KIND,
 * EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF
 * MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND
 * NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS
 * BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN
 * ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN
 * CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package uwyo.cs.uwreg.config;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.web.csrf.CsrfFilter;
import org.springframework.security.web.csrf.CsrfTokenRepository;
import org.springframework.security.web.csrf.HttpSessionCsrfTokenRepository;


@Configuration
@EnableWebSecurity
public class SpringSecurityConfiguration extends WebSecurityConfigurerAdapter {

	@Autowired DataSource dataSource;

	@Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth)
            throws Exception {
		// TODO 2 (10 pts) - Add queries for authentication (students only)
		// The first query should return three items:
		//     1. Username (the W#)
		//     2. Password (the student's last name)
		//     3. Enabled (a boolean, which should be true)
		//     This query has one parameter (i.e., a "?") which is the Username (i.e., the W#)
		// The second query should return two items:
		//     1. Username (the W#)
		//     2. Role (should be "ROLE_STUDENT" for students)
		
		
		
        auth.jdbcAuthentication().dataSource(dataSource)
        	.usersByUsernameQuery("SELECT WNum AS username, Last_Name AS password, IF(COUNT(*)=1, TRUE, FALSE) AS enabled from registration.students where WNum=?")
        	.authoritiesByUsernameQuery("SELECT WNum AS username, IF(COUNT(*)=1, 'ROLE_STUDENT', 'UNKNOWN') AS role from registration.students where WNum=?");
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.authorizeRequests()	
        		.antMatchers("/**").hasRole("STUDENT")
        		.and()
        			.formLogin()
        			.permitAll()
                .and()
                	.addFilterAfter(new CsrfHeaderFilter(), CsrfFilter.class)
                	.csrf().csrfTokenRepository(csrfTokenRepository());

        http.formLogin().and().httpBasic();
    }

	private CsrfTokenRepository csrfTokenRepository() {
		HttpSessionCsrfTokenRepository repository = new HttpSessionCsrfTokenRepository();
		repository.setHeaderName("X-XSRF-TOKEN");
		return repository;
	}

}
