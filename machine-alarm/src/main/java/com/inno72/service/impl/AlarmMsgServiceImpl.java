package com.inno72.service.impl;

import com.inno72.common.AbstractService;
import com.inno72.model.Inno72AlarmMsg;
import com.inno72.service.AlarmMsgService;
import com.inno72.mapper.Inno72AlarmMsgMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;


/**
 * Created by CodeGenerator on 2018/07/19.
 */
@Service
@Transactional
public class AlarmMsgServiceImpl extends AbstractService<Inno72AlarmMsg> implements AlarmMsgService {
    @Resource
    private Inno72AlarmMsgMapper inno72AlarmMsgMapper;

    @Override
    public List<Inno72AlarmMsg> findByPage(Object condition) {
        return null;
    }
}
