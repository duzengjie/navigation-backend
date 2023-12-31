package com.duzj.navigation.controller;

import com.duzj.navigation.entity.EnvironmentInfo;
import com.duzj.navigation.entity.dto.EnvironmentUrlListDTO;
import com.duzj.navigation.entity.base.ResultDTO;
import com.duzj.navigation.entity.request.EnvironmentInfoRequest;
import com.duzj.navigation.entity.response.EnvironmentUrlListResponse;
import com.duzj.navigation.service.EnvironmentInfoService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @Description
 * @Date 2023/11/9 10:25
 * @Created by duzengjie
 */
@RestController
@RequestMapping("/env")
public class EnvironmentInfoController {

    @Autowired
    private EnvironmentInfoService environmentInfoService;


    @GetMapping(value = "/api/list")
    public ResultDTO<List<EnvironmentUrlListResponse>> listApi() {
        List<EnvironmentUrlListResponse> responses = new ArrayList<>();
        List<EnvironmentUrlListDTO> environmentUrlListDTOS = environmentInfoService.selectAll();
        for (EnvironmentUrlListDTO environmentUrlListDTO : environmentUrlListDTOS) {
            EnvironmentUrlListResponse response = new EnvironmentUrlListResponse();
            response.setId(environmentUrlListDTO.getId());
            response.setName(environmentUrlListDTO.getName());
            response.setData(environmentUrlListDTO.getData());
            responses.add(response);
        }
        return ResultDTO.success(responses);
    }

    @GetMapping(value = "/api/delete")
    public ResultDTO<Boolean> deleteApi(int id) {
        return ResultDTO.success(environmentInfoService.deleteEnvironment(id));
    }

    @PostMapping(value = "/api/update")
    public ResultDTO<Boolean> updateApi(@RequestBody EnvironmentInfoRequest request) {
        EnvironmentInfo environmentInfo = new EnvironmentInfo();
        BeanUtils.copyProperties(request, environmentInfo);
        environmentInfo.setUpdateTime(new Date());
        return ResultDTO.success(environmentInfoService.updateById(environmentInfo));
    }

    @PostMapping(value = "/api/add")
    public ResultDTO<Boolean> addApi(@RequestBody EnvironmentInfoRequest request) {
        EnvironmentInfo environmentInfo = new EnvironmentInfo();
        BeanUtils.copyProperties(request, environmentInfo);
        environmentInfo.setCreateTime(new Date());
        return ResultDTO.success(environmentInfoService.save(environmentInfo));
    }

    @GetMapping(value = "/api/downloadAllByExcel")
    public void downloadAllByExcel(HttpServletResponse response) {
        environmentInfoService.downloadAllByExcel(response);
    }

    @PostMapping(value = "/api/backupRecoverByExcel")
    public ResultDTO<Boolean> backupRecoverByExcel(@RequestParam("file") MultipartFile file) {
        environmentInfoService.backupRecoverByExcel(file);
        return ResultDTO.success(true);
    }

}
