package com.fluxnetworks.java_api;

import com.google.common.base.Preconditions;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.fluxnetworks.java_api.exception.*;
import com.fluxnetworks.java_api.modules.websend.WebsendAPI;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.math.BigInteger;
import java.net.URL;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

public final class FluxAPI {

	static final Gson GSON = new Gson();

	@NotNull
	private final RequestHandler requests;

	FluxAPI(@NotNull final RequestHandler requests) {
		this.requests = Objects.requireNonNull(requests, "Request handler is null");
	}

	@NotNull
	RequestHandler getRequestHandler() {
		return this.requests;
	}

	@NotNull
	public URL getApiUrl() {
		return this.getRequestHandler().getApiUrl();
	}

	@NotNull
	public String getApiKey() {
		return getApiKey(this.getApiUrl().toString());
	}

	@NotNull
	static String getApiKey(@NotNull final String url) {
		if (url.endsWith("/")) {
			return getApiKey(url.substring(0, url.length() - 1));
		}

		return url.substring(url.lastIndexOf('/'));
	}

	// TODO Add back when Flux Networks has reimplemented it
//	/**
//	 * Get announcements visible to guests. Use {@link #getAnnouncements(FluxUser)} for non-guest announcements.
//	 * @return list of current announcements
//	 * @throws FluxException if there is an error in the request
//	 */
//	@NotNull
//	public List<@NotNull Announcement> getAnnouncements() throws FluxException {
//		final JsonObject response = this.requests.get();
//
//		return getAnnouncements(response);
//	}

	/**
	 * Get all announcements visible for the player with the specified uuid
	 *
	 * @param user player to get visible announcements for
	 * @return list of current announcements visible to the player
	 * @throws FluxException if there is an error in the request
	 */
	@NotNull
	public List<@NotNull Announcement> getAnnouncements(@NotNull final FluxUser user) throws FluxException {
		final JsonObject response = this.requests.get("users/" + user.getId() + "/announcements");

		return getAnnouncements(response);
	}

	@NotNull
	private static Set<@NotNull String> toStringSet(@NotNull final JsonArray jsonArray) {
		return StreamSupport.stream(jsonArray.spliterator(), false).map(JsonElement::getAsString).collect(Collectors.toSet());
	}

	@NotNull
	private List<@NotNull Announcement> getAnnouncements(@NotNull final JsonObject response) {
		return StreamSupport.stream(response.getAsJsonArray("announcements").spliterator(), false)
					.map(JsonElement::getAsJsonObject)
					.map(Announcement::new)
					.collect(Collectors.toList());
	}

	public void submitServerInfo(final @NotNull JsonObject jsonData) throws FluxException {
		this.requests.post("minecraft/server-info", jsonData);
	}

	public Website getWebsite() throws FluxException {
		final JsonObject json = this.requests.get("info");
		return new Website(json);
	}

	public FilteredUserListBuilder getRegisteredUsers() {
		return new FilteredUserListBuilder(this);
	}

	public @NotNull Optional<FluxUser> getUser(final int id) throws FluxException {
		final FluxUser user = getUserLazy(id);
		if (user.exists()) {
			return Optional.of(user);
		} else {
			return Optional.empty();
		}
	}

	public @NotNull Optional<FluxUser> getUser(@NotNull final String username) throws FluxException {
		final FluxUser user = getUserLazy(username);
		if (user.exists()) {
			return Optional.of(user);
		} else {
			return Optional.empty();
		}
	}

	public @NotNull Optional<FluxUser> getUser(@NotNull final UUID uuid) throws FluxException {
		final FluxUser user = getUserLazy(uuid);
		if (user.exists()) {
			return Optional.of(user);
		} else {
			return Optional.empty();
		}
	}

	public @NotNull Optional<FluxUser> getUserByDiscordId(final long discordId) throws FluxException {
		final FluxUser user = getUserLazyDiscord(discordId);
		if (user.exists()) {
			return Optional.of(user);
		} else {
			return Optional.empty();
		}
	}

	/**
	 * Construct a FluxUser object without making API requests (so without checking if the user exists)
	 * @param id Flux Networks user id
	 * @return Flux user object, never null
	 */
	public @NotNull FluxUser getUserLazy(final int id) {
		return new FluxUser(this, id, null, false, null, false, -1L);
	}

	/**
	 * Construct a FluxUser object without making API requests (so without checking if the user exists)
	 * @param username Flux Networks user
	 * @return Flux user object, never null
	 */
	public @NotNull FluxUser getUserLazy(final @NotNull String username) {
		return new FluxUser(this, -1, username, false, null, false, -1L);
	}

	/**
	 * Construct a FluxUser object without making API requests (so without checking if the user exists)
	 * @param uuid Minecraft UUID
	 * @return Flux user object, never null
	 */
	public @NotNull FluxUser getUserLazy(@NotNull final UUID uuid) {
		return new FluxUser(this, -1, null, true, uuid, false, -1L);
	}

	/**
	 * Construct a FluxUser object without making API requests (so without checking if the user exists)
	 * @param username The user's username
	 * @param uuid The user's Mojang UUID
	 * @return Flux user object, never null
	 */
	public FluxUser getUserLazy(@NotNull final String username, @NotNull final UUID uuid) {
		return new FluxUser(this, -1, username, true, uuid, false,-1L);
	}

	/**
	 * Construct a FluxUser object without making API requests (so without checking if the user exists)
	 * @param id Flux Networks user id
	 * @return Flux user object, never null
	 */
	public FluxUser getUserLazy(final int id, final @NotNull String username, final @NotNull UUID uuid) {
		return new FluxUser(this, id, username, true, uuid, false, -1L);
	}

	/**
	 * Construct a FluxUser object without making API requests (so without checking if the user exists)
	 * @param discordId Discord user id
	 * @return Flux user object, never null
	 */
	public FluxUser getUserLazyDiscord(final long discordId) {
		Preconditions.checkArgument(discordId > 0, "Discord id must be a positive long");
		return new FluxUser(this, -1, null, false, null, true, discordId);
	}

	/**
	 * Get Flux Networks group by ID
	 * @param id Group id
	 * @return Optional with a group if the group exists, empty optional if it doesn't
	 */
	@NotNull
	public Optional<@NotNull Group> getGroup(final int id) throws FluxException {
		final JsonObject response = this.requests.get("groups", "id", id);
		final JsonArray jsonArray = response.getAsJsonArray("groups");
		if (jsonArray.size() != 1) {
			return Optional.empty();
		} else {
			return Optional.of(new Group(jsonArray.get(0).getAsJsonObject()));
		}
	}

	/**
	 * Get Flux Networks groups by name
	 * @param name Flux Networks groups name
	 * @return List of groups with this name, empty if there are no groups with this name.
	 */
	@NotNull
	public List<@NotNull Group> getGroup(@NotNull final String name) throws FluxException {
		Objects.requireNonNull(name, "Group name is null");
		final JsonObject response = this.requests.get("groups", "name", name);
		return groupListFromJsonArray(response.getAsJsonArray("groups"));
	}

	/**
	 * Get a list of all groups on the website
	 * @return list of groups
	 */
	public @NotNull List<Group> getAllGroups() throws FluxException {
		final JsonObject response = this.requests.get("groups");
		return groupListFromJsonArray(response.getAsJsonArray("groups"));

	}

	public int @NotNull[] getAllGroupIds() throws FluxException {
		final JsonObject response = this.requests.get("groups");
		return StreamSupport.stream(response.getAsJsonArray("groups").spliterator(), false)
				.map(JsonElement::getAsJsonObject)
				.mapToInt(o -> o.get("id").getAsInt())
				.toArray();
	}

	private @NotNull List<Group> groupListFromJsonArray(@NotNull final JsonArray array) {
		return StreamSupport.stream(array.spliterator(), false)
				.map(JsonElement::getAsJsonObject)
				.map(Group::new)
				.collect(Collectors.toList());
	}

	/**
	 * Registers a new account. The user will be emailed to set a password.
	 *
	 * @param username Username (this should match the user's in-game username when specifying a UUID)
	 * @param email Email address
	 * @param uuid Mojang UUID, if you wish to use the Minecraft integration. Nullable.
	 * @return Email verification disabled: A link which the user needs to click to complete registration
	 * <br>Email verification enabled: An empty string (the user needs to check their email to complete registration)
	 * @see #registerUser(String, String)
	 */
	public @NotNull Optional<String> registerUser(@NotNull final String username,
												  @NotNull final String email,
												  @Nullable final UUID uuid)
			throws FluxException, InvalidUsernameException, UsernameAlreadyExistsException, CannotSendEmailException, UuidAlreadyExistsException {
		Objects.requireNonNull(username, "Username is null");
		Objects.requireNonNull(email, "Email address is null");

		final JsonObject post = new JsonObject();
		post.addProperty("username", username);
		post.addProperty("email", email);
		if (uuid != null) {
			post.addProperty("uuid", uuid.toString());
		}

		try {
			final JsonObject response = this.requests.post("users/register", post);

			if (response.has("link")) {
				return Optional.of(response.get("link").getAsString());
			} else {
				return Optional.empty();
			}
		} catch (final ApiError e) {
			if (e.getError() == ApiError.INVALID_USERNAME) {
				throw new InvalidUsernameException();
			} else if (e.getError() == ApiError.USERNAME_ALREADY_EXISTS) {
				throw new UsernameAlreadyExistsException();
			} else if (e.getError() == ApiError.UNABLE_TO_SEND_REGISTRATION_EMAIL) {
				throw new CannotSendEmailException();
			} else if (e.getError() == ApiError.UUID_ALREADY_EXISTS) {
				throw new UuidAlreadyExistsException();
			} else {
				throw e;
			}
		}
	}

	/**
	 * Register user without UUID {@link #registerUser(String, String, UUID)}
	 * WARNING: This will fail if the website has Minecraft integration enabled!
	 * @param username New username for this user
	 * @param email New email address for this user
	 * @return Verification URL if email verification is disabled.
	 */
	public @NotNull Optional<String> registerUser(@NotNull final String username,
												  @NotNull final String email)
			throws FluxException, InvalidUsernameException, UsernameAlreadyExistsException, CannotSendEmailException {
		try {
			return registerUser(username, email, null);
		} catch (final UuidAlreadyExistsException e) {
			throw new IllegalStateException("Website said duplicate uuid but we haven't specified a uuid?", e);
		}
	}

	/**
	 * Set Discord bot URL (Flux-Link internal webserver)
	 * @param url Discord bot URL
	 */
	public void setDiscordBotUrl(@NotNull final URL url) throws FluxException {
		Objects.requireNonNull(url, "Bot url is null");

		final JsonObject json = new JsonObject();
		json.addProperty("url", url.toString());
		this.requests.post("discord/update-bot-settings", json);
	}

	/**
	 * Set Discord guild (server) id
	 * @param guildId Discord guild (server) id
	 */
	public void setDiscordGuildId(final long guildId) throws FluxException {
		final JsonObject json = new JsonObject();
		json.addProperty("guild_id", guildId + "");
		this.requests.post("discord/update-bot-settings", json);
	}

	/**
	 * Set discord bot username and user id
	 * @param username Bot username#tag
	 * @param userId Bot user id
	 * @see #setDiscordBotSettings(URL, long, String, long)
	 */
	public void setDiscordBotUser(@NotNull final String username, final long userId) throws FluxException {
		Objects.requireNonNull(username, "Bot username is null");

		final JsonObject json = new JsonObject();
		json.addProperty("bot_username", username);
		json.addProperty("bot_user_id", userId + "");
		this.requests.post("discord/update-bot-settings", json);
	}

	/**
	 * Update all Discord bot settings.
	 * @param url Discord bot URL
	 * @param guildId Discord guild (server) id
	 * @param username Discord bot username#tag
	 * @param userId Discord bot user id
	 * @see #setDiscordBotUrl(URL)
	 * @see #setDiscordGuildId(long)
	 * @see #setDiscordBotUser(String, long)
	 */
	public void setDiscordBotSettings(@NotNull final URL url, final long guildId, @NotNull final String username, final long userId) throws FluxException {
		Objects.requireNonNull(url, "Bot url is null");
		Objects.requireNonNull(username, "Bot username is null");

		final JsonObject json = new JsonObject();
		json.addProperty("url", url.toString());
		json.addProperty("guild_id", guildId + "");
		json.addProperty("bot_username", username);
		json.addProperty("bot_user_id", userId + "");
		this.requests.post("discord/update-bot-settings", json);
	}

	/**
	 * Send list of Discord roles to the website for populating the dropdown in StaffCP > API > Group sync
	 * @param discordRoles Map of Discord roles, key is role id, value is role name
	 */
	public void submitDiscordRoleList(@NotNull final Map<Long, String> discordRoles) throws FluxException {
		final JsonArray roles = new JsonArray();
		discordRoles.forEach((id, name) -> {
			final JsonObject role = new JsonObject();
			role.addProperty("id", id);
			role.addProperty("name", name);
			roles.add(role);
		});
		final JsonObject json = new JsonObject();
		json.add("roles", roles);
		this.requests.post("discord/submit-role-list", json);
	}

	/**
	 * Update Discord username for a Flux Networks user associated with the provided Discord user id
	 * @param discordUserId Discord user id
	 * @param discordUsername New Discord [username#tag]s
	 * @see #updateDiscordUsernames(long[], String[])
	 */
	public void updateDiscordUsername(final long discordUserId,
									  final @NotNull String discordUsername)
			throws FluxException {
		Objects.requireNonNull(discordUsername, "Discord username is null");

		final JsonObject user = new JsonObject();
		user.addProperty("id", discordUserId);
		user.addProperty("name", discordUsername);
		final JsonArray users = new JsonArray();
		users.add(user);
		final JsonObject json = new JsonObject();
		json.add("users", users);
		this.requests.post("discord/update-usernames", json);
	}

	/**
	 * Update Discord usernames in bulk
	 * @param discordUserIds Discord user ids
	 * @param discordUsernames New Discord [username#tag]s
	 * @see #updateDiscordUsername(long, String)
	 */
	public void updateDiscordUsernames(final long@NotNull[] discordUserIds,
									   final  @NotNull String@NotNull[] discordUsernames)
			throws FluxException {
		Objects.requireNonNull(discordUserIds, "User ids array is null");
		Objects.requireNonNull(discordUsernames, "Usernames array is null");
		Preconditions.checkArgument(discordUserIds.length == discordUsernames.length,
				"discord user ids and discord usernames must be of same length");

		if (discordUserIds.length == 0) {
			return;
		}

		final JsonArray users = new JsonArray();

		for (int i = 0; i < discordUserIds.length; i++) {
			final JsonObject user = new JsonObject();
			user.addProperty("id", discordUserIds[i]);
			user.addProperty("name", discordUsernames[i]);
			users.add(user);
		}

		final JsonObject json = new JsonObject();
		json.add("users", users);
		this.requests.post("discord/update-usernames", json);
	}

	private void verifyIntegration(final @NotNull IntegrationType type,
								  final @NotNull String verificationCode,
								  final @NotNull String identifier,
								  final @NotNull String username) throws FluxException, InvalidValidateCodeException {
		JsonObject data = new JsonObject();
		data.addProperty("integration", type.apiValue());
		data.addProperty("code", Objects.requireNonNull(verificationCode, "Verification code is null"));
		data.addProperty("identifier", Objects.requireNonNull(identifier, "Identifier is null"));
		data.addProperty("username", Objects.requireNonNull(username, "Username is null"));
		try {
			this.requests.post("integration/verify", data);
		} catch (ApiError e) {
			if (e.getError() == ApiError.INVALID_VALIDATE_CODE) {
				throw new InvalidValidateCodeException();
			} else {
				throw e;
			}
		}
	}

	public void verifyMinecraft(final @NotNull String verificationCode,
								final @NotNull UUID uuid,
								final @NotNull String username) throws FluxException, InvalidValidateCodeException {
		this.verifyIntegration(IntegrationType.MINECRAFT, verificationCode, uuid.toString(), username);
	}

	public void verifyDiscord(final @NotNull String verificationCode,
							  final long id,
							  final String username) throws FluxException, InvalidValidateCodeException {
		this.verifyIntegration(IntegrationType.DISCORD, verificationCode, String.valueOf(id), username);
	}

	public @NotNull WebsendAPI websend() {
		return new WebsendAPI(this.requests);
	}


	/**
	 * Adds back dashes to a UUID string and converts it to a Java UUID object
	 * @param uuid UUID without dashes
	 * @return UUID with dashes
	 */
	static @NotNull UUID websiteUuidToJavaUuid(@NotNull final String uuid) {
		Objects.requireNonNull(uuid, "UUID string is null");
		// Website sends UUIDs without dashes, so we can't use UUID#fromString
		// https://stackoverflow.com/a/30760478
		try {
			final BigInteger a = new BigInteger(uuid.substring(0, 16), 16);
			final BigInteger b = new BigInteger(uuid.substring(16, 32), 16);
			return new UUID(a.longValue(), b.longValue());
		} catch (final IndexOutOfBoundsException e) {
			throw new IllegalArgumentException("Invalid uuid: '" + uuid + "'", e);
		}
	}

	@NotNull
	public static FluxApiBuilder builder(@NotNull URL apiUrl, @NotNull String apiKey) {
		return new FluxApiBuilder(apiUrl, apiKey);
	}

}
