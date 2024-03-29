package com.duzj.navigation.mapper;

import com.duzj.navigation.entity.UrlInfo;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

/**
* @author duzengjie
* @description 针对表【url_info(链接信息)】的数据库操作Mapper
* @createDate 2024-01-10 11:11:46
* @Entity com.duzj.navigation.entity.UrlInfo
*/
public interface UrlInfoMapper extends BaseMapper<UrlInfo> {
    boolean useNumIncrease(int id);
}




