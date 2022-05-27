package com.george.springservermanager.service;

import com.george.springservermanager.domain.Server;
import com.george.springservermanager.exception.ServerNotFoundException;
import com.george.springservermanager.mapper.ServerMapper;
import com.george.springservermanager.model.ServerDTO;
import com.george.springservermanager.model.ServerListDTO;
import com.george.springservermanager.repository.ServerRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.transaction.Transactional;
import java.io.IOException;
import java.net.InetAddress;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

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
    private final ServerMapper serverMapper;
    private final Random random = new Random();

    @Override
    public ServerDTO create(ServerDTO serverDTO) {
        log.info("Saving new server: {}", serverDTO.getName());
        serverDTO.setImageUrl(setServerImageUrl());
        return saveAndReturnDTO(serverMapper.serverDTOToServer(serverDTO));
    }

    @Override
    public ServerDTO pingServer(String ipAddress) throws IOException {
        log.info("Pinging server IP: {}", ipAddress);
        ServerDTO serverDTO = serverRepository.findByIpAddress(ipAddress)
                                .map(serverMapper::serverToServerDTO)
                                .orElseThrow(() -> new ServerNotFoundException("Server was not found."));
        InetAddress address = InetAddress.getByName(ipAddress);
        serverDTO.setStatus(address.isReachable(10000) ? SERVER_UP : SERVER_DOWN);
        serverRepository.save(serverMapper.serverDTOToServer(serverDTO));
        return serverDTO;
    }

    @Override
    public ServerListDTO serverList(int limit) {
        log.info("Fetching all servers...");
        List<ServerDTO> serverDTOS = serverRepository
                .findAll(of(0, limit))
                .stream()
                .map(serverMapper::serverToServerDTO)
                .collect(Collectors.toList());
        return new ServerListDTO(serverDTOS);
    }

    @Override
    public ServerDTO getServer(Long id) {
        log.info("Fetching server by id: {}", id);
        return serverRepository.findById(id)
                .map(serverMapper::serverToServerDTO)
                .orElseThrow(() -> new ServerNotFoundException("Server was not found."));
    }

    @Override
    public ServerDTO updateServer(ServerDTO serverDTO) {
        log.info("Updating server: {}", serverDTO.getName());
        return saveAndReturnDTO(serverMapper.serverDTOToServer(serverDTO));
    }

    @Override
    public Boolean deleteServer(Long id) {
        log.info("Deleting server by ID: {}", id);
        serverRepository.deleteById(id);
        return TRUE;
    }

    private String setServerImageUrl() {
        String[] imageNames = {"Server1.png", "Server2.png", "Server3.png", "Server4.png"};
        return ServletUriComponentsBuilder
                .fromCurrentContextPath()
                .path("/server/image/" + imageNames[random.nextInt(4)]).toUriString();
    }

    private ServerDTO saveAndReturnDTO(Server server) {
        Server savedServer = serverRepository.save(server);

        return serverMapper.serverToServerDTO(savedServer);
    }
}
