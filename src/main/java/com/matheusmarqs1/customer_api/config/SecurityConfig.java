package com.matheusmarqs1.customer_api.config;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.security.KeyFactory;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.security.oauth2.jwt.NimbusJwtEncoder;
import org.springframework.security.web.SecurityFilterChain;
import com.matheusmarqs1.customer_api.security.CustomBearerTokenAccessDeniedHandler;
import com.matheusmarqs1.customer_api.security.CustomBearerTokenAuthenticationEntryPoint;
import com.nimbusds.jose.jwk.JWKSet;
import com.nimbusds.jose.jwk.RSAKey;
import com.nimbusds.jose.jwk.source.ImmutableJWKSet;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity(prePostEnabled = true)
public class SecurityConfig {


	@Value("${jwt.public.key}")
	private String publicKey;
	@Value("${jwt.private.key}")
	private String privateKey;

	private final CustomBearerTokenAuthenticationEntryPoint customBearerTokenAuthenticationEntryPoint;
	private final CustomBearerTokenAccessDeniedHandler customBearerTokenAccessDeniedHandler;

	public SecurityConfig(CustomBearerTokenAuthenticationEntryPoint customBearerTokenAuthenticationEntryPoint, CustomBearerTokenAccessDeniedHandler customBearerTokenAccessDeniedHandler) {
		this.customBearerTokenAuthenticationEntryPoint = customBearerTokenAuthenticationEntryPoint;
		this.customBearerTokenAccessDeniedHandler = customBearerTokenAccessDeniedHandler;
	}
	
	@Bean
	RSAPublicKey rsaPublicKey() throws IOException, GeneralSecurityException {
		String key;
		if (publicKey.startsWith("classpath:")) {
			key = new String(new ClassPathResource(publicKey.replace("classpath:", "")).getInputStream().readAllBytes());
					
			key = key.replaceAll("-----BEGIN PUBLIC KEY-----", "")
						.replaceAll("-----END PUBLIC KEY-----", "")
						.replaceAll("\\s", "");
		}
		else {
			key = publicKey;
		}
		byte[] keyBytes = Base64.getDecoder().decode(key);
		X509EncodedKeySpec spec = new X509EncodedKeySpec(keyBytes);
		KeyFactory kf = KeyFactory.getInstance("RSA");
		return (RSAPublicKey) kf.generatePublic(spec);
	}

	@Bean
	RSAPrivateKey rsaPrivateKey() throws IOException, GeneralSecurityException {
		String key;
		if (privateKey.startsWith("classpath:")) {
			
			key = new String(new ClassPathResource(privateKey.replace("classpath:", "")).getInputStream().readAllBytes());
			
			key = key.replaceAll("-----BEGIN PRIVATE KEY-----", "")
					 .replaceAll("-----END PRIVATE KEY-----", "")
					 .replaceAll("\\s", "");
		}
		else {
			key = privateKey;
		}
		byte[] keyBytes = Base64.getDecoder().decode(key);
		PKCS8EncodedKeySpec spec = new PKCS8EncodedKeySpec(keyBytes);
		KeyFactory kf = KeyFactory.getInstance("RSA");
		return (RSAPrivateKey) kf.generatePrivate(spec);
	}

	@Bean
	SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
		http.csrf(csrf -> csrf.disable()).headers(headers -> headers.frameOptions(frame -> frame.sameOrigin()))
				.sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
				.authorizeHttpRequests(auth -> auth.requestMatchers("/customers/login").permitAll()
						.requestMatchers(HttpMethod.POST, "/customers").permitAll().requestMatchers("/h2-console/**")
						.permitAll().requestMatchers("/error").permitAll()
						.requestMatchers("/v3/api-docs/**", "/swagger-ui/**", "/swagger-ui.html").permitAll()
						.requestMatchers(HttpMethod.GET, "/customers").hasAuthority("SCOPE_ROLE_ADMIN")
						.anyRequest().authenticated())
				.oauth2ResourceServer(oauth2 -> oauth2.jwt(Customizer.withDefaults())
						.authenticationEntryPoint(customBearerTokenAuthenticationEntryPoint)
						.accessDeniedHandler(customBearerTokenAccessDeniedHandler));

		return http.build();
	}

	@Bean
	AuthenticationManager authenticationManager(AuthenticationConfiguration authConfig) throws Exception {
		return authConfig.getAuthenticationManager();
	}

	@Bean
	JwtDecoder jwtDecoder(RSAPublicKey publicKey) {
		return NimbusJwtDecoder.withPublicKey(publicKey).build();
	}

	@Bean
	JwtEncoder jwtEncoder(RSAPublicKey publicKey, RSAPrivateKey privateKey) {
		var jwk = new RSAKey.Builder(publicKey).privateKey(privateKey).build();
		var jwks = new ImmutableJWKSet<>(new JWKSet(jwk));
		return new NimbusJwtEncoder(jwks);
	}

	@Bean
	PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}
}