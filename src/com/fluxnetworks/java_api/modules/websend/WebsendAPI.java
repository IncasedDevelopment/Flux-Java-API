package com.fluxnetworks.java_api.modules.websend;

import com.fluxnetworks.java_api.FluxException;
import com.fluxnetworks.java_api.RequestHandler;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

public class WebsendAPI {

	private final @NotNull RequestHandler requests;

	public WebsendAPI(@NotNull RequestHandler requests) {
		this.requests = Objects.requireNonNull(requests, "Request handler is null");
	}

	public @NotNull List<WebsendCommand> getCommands(int serverId) throws FluxException {
		JsonObject response = this.requests.get("websend/commands","server_id", serverId);
		JsonArray commandsJson = response.getAsJsonArray("commands");
		List<WebsendCommand> commands = new ArrayList<>(commandsJson.size());
		for (JsonElement e : commandsJson) {
			JsonObject commandJson = e.getAsJsonObject();
			int commandId = commandJson.get("id").getAsInt();
			String commandLine = commandJson.get("command").getAsString();
			commands.add(new WebsendCommand(commandId, commandLine));
		}
		return Collections.unmodifiableList(commands);
	}

	public void sendConsoleLog(int serverId, @NotNull Collection<String> lines) throws FluxException {
		JsonObject body = new JsonObject();
		body.addProperty("server_id", serverId);
		JsonArray content = new JsonArray();
		for (String line : lines) {
			content.add(line);
		}
		body.add("content", content);
		this.requests.post("websend/console", body);
	}

}
