package com.george.springservermanager.mapper;

import com.george.springservermanager.domain.Server;
import com.george.springservermanager.model.ServerDTO;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface ServerMapper {

    ServerMapper INSTANCE = Mappers.getMapper(ServerMapper.class);

    ServerDTO serverToServerDTO(Server server);

    Server serverDTOToServer(ServerDTO serverDTO);
}
