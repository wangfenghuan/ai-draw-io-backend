package com.wfh.drawio.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wfh.drawio.model.entity.Conversion;
import com.wfh.drawio.service.ConversionService;
import com.wfh.drawio.mapper.ConversionMapper;
import org.springframework.stereotype.Service;

/**
* @author fenghuanwang
* @description 针对表【conversion(消息对话表)】的数据库操作Service实现
* @createDate 2025-12-21 09:19:15
*/
@Service
public class ConversionServiceImpl extends ServiceImpl<ConversionMapper, Conversion>
    implements ConversionService{

}




