package com.duzj.navigation.excel;

import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.read.listener.ReadListener;
import com.alibaba.excel.util.ListUtils;
import com.duzj.navigation.entity.UrlInfo;
import com.duzj.navigation.entity.dto.UrlInfoExcelDTO;
import com.duzj.navigation.service.UrlInfoService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;

import java.util.List;

/**
 * @Description
 * @Date 2023/11/20 10:40
 * @Created by duzengjie
 */
@Slf4j
public class UrlInfoDataListener implements ReadListener<UrlInfoExcelDTO> {

    /**
     * 每隔5条存储数据库，实际使用中可以100条，然后清理list ，方便内存回收
     */
    private static final int BATCH_COUNT = 100;

    private UrlInfoService urlInfoService;

    public UrlInfoDataListener(UrlInfoService urlInfoService) {
        this.urlInfoService = urlInfoService;
    }

    private List<UrlInfo> cachedDataList = ListUtils.newArrayListWithExpectedSize(BATCH_COUNT);


    @Override
    public void invoke(UrlInfoExcelDTO urlInfoExcelDTO, AnalysisContext analysisContext) {
        log.info("解析到一条数据:{}", urlInfoExcelDTO);
        UrlInfo urlInfo = new UrlInfo();
        BeanUtils.copyProperties(urlInfoExcelDTO, urlInfo);
        cachedDataList.add(urlInfo);
        // 达到BATCH_COUNT了，需要去存储一次数据库，防止数据几万条数据在内存，容易OOM
        if (cachedDataList.size() >= BATCH_COUNT) {
            saveData();
            // 存储完成清理 list
            cachedDataList = ListUtils.newArrayListWithExpectedSize(BATCH_COUNT);
        }
    }


    /**
     * 所有数据解析完成了 都会来调用
     *
     * @param context
     */
    @Override
    public void doAfterAllAnalysed(AnalysisContext context) {
        // 这里也要保存数据，确保最后遗留的数据也存储到数据库
        saveData();
        log.info("所有数据解析完成！");
    }

    /**
     * 加上存储数据库
     */
    private void saveData() {
        log.info("{}条数据，开始存储数据库！", cachedDataList.size());
        urlInfoService.saveOrUpdateBatch(cachedDataList);
        log.info("存储数据库成功！");
    }
}
