package com.george.springservermanager.service;

import com.george.springservermanager.domain.Server;

import java.io.IOException;
import java.util.Collection;

public interface ServerService {

    Server create(Server server);
    Server pingServer(String ipAddress) throws IOException;
    Collection<Server> serverList(int limit);
    Server getServer(Long id);
    Server updateServer(Server server);
    Boolean deleteServer(Long id);
}
