package com.idea.tools.dto;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import lombok.Data;
import org.jetbrains.annotations.NotNull;

@Data
public class ServerDto implements Comparable<ServerDto> {

	private static final Comparator<String> COMPARATOR = Comparator.nullsLast(String::compareToIgnoreCase);

	private String id;
	private String name;
	private ServerType type;
	private ConnectionType connectionType;
	private String host;
	private Integer port;
	private String login;
	private String password;
	private boolean isLocal;
	private List<DestinationDto> destinations = new ArrayList<>();

	private ZookeeperConfiguration zookeeperConfiguration = new ZookeeperConfiguration();

	/**
	 * RabbitMQ
	 */
	private String virtualHost;

	private boolean nameIsAutogenerated = true;

	private SSLConfiguration sslConfiguration = new SSLConfiguration();

	public String getLogin() {
		return login;
	}

	public void setLogin(String login) {
		this.login = login;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	@Override
	public int compareTo(@NotNull ServerDto o) {
		return COMPARATOR.compare(name, o.name);
	}
}
