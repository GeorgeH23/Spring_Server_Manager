package com.george.springservermanager.service;

import com.george.springservermanager.domain.Server;
import com.george.springservermanager.repository.ServerRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.io.IOException;
import java.net.InetAddress;
import java.util.Collection;

import static com.george.springservermanager.enumeration.Status.SERVER_DOWN;
import static com.george.springservermanager.enumeration.Status.SERVER_UP;
import static java.lang.Boolean.*;
import static org.springframework.data.domain.PageRequest.*;

@RequiredArgsConstructor
@Service
@Transactional
@Slf4j
public class ServerServiceImpl implements ServerService {

    private final ServerRepository serverRepository;

    @Override
    public Server create(Server server) {
        log.info("Saving new server: {}", server.getName());
        server.setImageUrl(setServerImageUrl());
        return serverRepository.save(server);
    }

    @Override
    public Server pingServer(String ipAddress) throws IOException {
        log.info("Pinging server IP: {}", ipAddress);
        Server server = serverRepository.findByIpAddress(ipAddress);
        InetAddress address = InetAddress.getByName(ipAddress);
        server.setStatus(address.isReachable(10000) ? SERVER_UP : SERVER_DOWN);
        serverRepository.save(server);
        return server;
    }

    @Override
    public Collection<Server> serverList(int limit) {
        log.info("Fetching all servers...");
        return serverRepository.findAll(of(0, limit)).toList();
    }

    @Override
    public Server getServer(Long id) {
        log.info("Fetching server by id: {}", id);
        return serverRepository.findById(id).stream().findFirst().get();
    }

    @Override
    public Server updateServer(Server server) {
        log.info("Updating server: {}", server.getName());
        return serverRepository.save(server);
    }

    @Override
    public Boolean deleteServer(Long id) {
        log.info("Deleting server by ID: {}", id);
        serverRepository.deleteById(id);
        return TRUE;
    }

    private String setServerImageUrl() {
        return null;
    }
}
