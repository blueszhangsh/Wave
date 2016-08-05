package com.wave.im.core;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

public class ConnectedClients {

	private ConcurrentHashMap<Long, List<Client>> connectedClients = new ConcurrentHashMap<>();
		
	public boolean ownClient(long userId) {
		return connectedClients.containsKey(userId);
	}
	
	public List<Client> getClients(long userId) {
		synchronized(this) {
			return new ArrayList<>(connectedClients.get(userId));
		}
	}
	
	public void addClient(Client client) {
		long userId = client.getUserId();
		List<Client> clients = connectedClients.get(userId);
		if (clients == null) {
			clients = new ArrayList<Client>();
			connectedClients.putIfAbsent(userId, clients);
		}
		synchronized(this) {
			clients.add(client);
		}
	}
	
	public void removeClient(Client client) {
		long userId = client.getUserId();
		List<Client> clients = connectedClients.get(userId);
		if (clients == null) 
			return;
		synchronized(this) {
			clients.remove(client);
		}
	}
	
}
