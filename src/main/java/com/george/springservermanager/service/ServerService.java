package com.george.springservermanager.service;

import com.george.springservermanager.model.ServerDTO;
import com.george.springservermanager.model.ServerListDTO;

import java.io.IOException;

public interface ServerService {

    ServerDTO create(ServerDTO serverDTO);
    ServerDTO pingServer(String ipAddress) throws IOException;
    ServerListDTO serverList(int limit);
    ServerDTO getServer(Long id);
    ServerDTO updateServer(ServerDTO serverDTO);
    Boolean deleteServer(Long id);
}
