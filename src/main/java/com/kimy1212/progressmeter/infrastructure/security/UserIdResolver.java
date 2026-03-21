package com.kimy1212.progressmeter.infrastructure.security;

import java.nio.charset.StandardCharsets;
import java.util.UUID;

import org.springframework.security.oauth2.core.user.OAuth2User;

import com.kimy1212.progressmeter.domain.model.UserId;

public final class UserIdResolver {

	private UserIdResolver() {
	}

	/**
	 * Google OAuth2 の subject (sub) を UUID に変換して UserId を返す。
	 * 同じ Google アカウントは常に同じ UUID にマッピングされる。
	 */
	public static UserId resolve(OAuth2User principal) {
		String sub = principal.getAttribute("sub");
		UUID uuid = UUID.nameUUIDFromBytes(sub.getBytes(StandardCharsets.UTF_8));
		return UserId.of(uuid.toString());
	}

}
