package com.infosys.seatsync.service.impl;

import com.infosys.seatsync.dto.*;
import com.infosys.seatsync.entity.infra.Block;
import com.infosys.seatsync.repository.BlockRepository;
import com.infosys.seatsync.repository.WingRepository;
import com.infosys.seatsync.service.DCDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class DCDetailsServiceImpl implements DCDetailsService {

    @Autowired
    BlockRepository blockRepository;

    @Autowired
    WingRepository wingRepository;

    @Override
    public DCInfoResponseDto getAllDCInfo() {
        DCInfoResponseDto dcInfoResponseDto = new DCInfoResponseDto();

        List<Block> blocks = blockRepository.findAll();
        System.out.println(blocks);

        // STEP 1: Group by City Name
        Map<String, Map<Object, List<Block>>> grouped = blocks.stream()
                .collect(Collectors.groupingBy(
                        b -> b.getDeliveryCenter().getLocation().split(",")[0],
                        Collectors.groupingBy(b -> b.getDeliveryCenter().getDcId())
                ));

        // STEP 2: Build DTO structure
        List<CityDto> cityDTOs = new ArrayList<>();

        for (String cityName : grouped.keySet()) {

            CityDto cityDTO = new CityDto();
            cityDTO.setCityName(cityName);

            List<DataCenterDto> dcDTOList = new ArrayList<>();

            for (Map.Entry<Object, List<Block>> dcEntry : grouped.get(cityName).entrySet()) {

                Object dcId = dcEntry.getKey();
                List<Block> dcBlocks = dcEntry.getValue();

                DataCenterDto dcDTO = new DataCenterDto();
                dcDTO.setDcId(dcId.toString());
                dcDTO.setDcName(dcBlocks.get(0).getDeliveryCenter().getDcName());

                // Map blocks into DTO
                List<BlockDto> blockDTOList = dcBlocks.stream().map(block -> {
                    BlockDto dto = new BlockDto();
                    dto.setBlockId(block.getBlockId());
                    dto.setBlockName(block.getBlockName());
                    List<WingDto> wingList = block.getWings().stream().map(wing -> {
                        WingDto wingDto = new WingDto();
                        wingDto.setWingId(wing.getWingId());
                        wingDto.setWingName(wing.getWingName());
                        wingDto.setAccessType(wing.getAccessType());
                        return wingDto;
                    }).collect(Collectors.toList());
                    dto.setWings(wingList);
                    return dto;
                }).collect(Collectors.toList());
                dcDTO.setBlocks(blockDTOList);
                dcDTOList.add(dcDTO);
            }

            cityDTO.setDataCenters(dcDTOList);
            cityDTOs.add(cityDTO);
        }

        // STEP 3: Build the final APIResponseDTO
        dcInfoResponseDto.setCityList(cityDTOs);

        dcInfoResponseDto.setStatus("SUCCESS");
        dcInfoResponseDto.setTimestamp(Instant.now());

        return dcInfoResponseDto;
    }
}
