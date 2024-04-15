package ma.cimr.agmbackend.services.impl;

import java.security.Key;
import java.util.Date;
import java.util.function.Function;

import javax.crypto.SecretKey;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import ma.cimr.agmbackend.services.JwtService;

@Service
public class JwtServiceImpl implements JwtService {

	public String generateToken(UserDetails userDetails) {
		return Jwts.builder().subject(userDetails.getUsername()).issuedAt(new Date(System.currentTimeMillis()))
				.expiration(new Date(System.currentTimeMillis() + 1000 * 60 * 24))
				.signWith(getSignInKey()).compact();
	}

	public String extractUsername(String token) {
		return extractClaim(token, Claims::getSubject);
	}

	private <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
		final Claims claims = extractAllClaims(token);
		return claimsResolver.apply(claims);
	}

	private SecretKey getSignInKey() {
		byte[] key = Decoders.BASE64.decode("2c8374962100bce9a1bf947a6f93df8d3fc49fba83d519d60fbf0bf75965c39e");
		return Keys.hmacShaKeyFor(key);
	}

	private Claims extractAllClaims(String token) {
		return Jwts.parser().verifyWith(getSignInKey()).build().parseSignedClaims(token).getPayload();
	}

	public boolean isTokenValid(String token, UserDetails userDetails) {
		final String username = extractUsername(token);
		return (username.equals(userDetails.getUsername()) && !isTokenExpired(token));
	}

	private boolean isTokenExpired(String token) {
		return extractClaim(token, Claims::getExpiration).before(new Date());
	}
}
