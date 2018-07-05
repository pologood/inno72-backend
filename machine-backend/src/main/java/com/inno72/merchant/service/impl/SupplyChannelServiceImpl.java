package com.inno72.merchant.service.impl;

import com.inno72.common.Result;
import com.inno72.common.ResultGenerator;
import com.inno72.common.StringUtil;
import com.inno72.merchant.mapper.Inno72SupplyChannelMapper;
import com.inno72.merchant.model.Inno72SupplyChannel;
import com.inno72.merchant.model.Inno72SupplyChannelDict;
import com.inno72.merchant.service.SupplyChannelDictService;
import com.inno72.merchant.service.SupplyChannelService;
import com.inno72.common.AbstractService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * Created by CodeGenerator on 2018/07/04.
 */
@Service
@Transactional
public class SupplyChannelServiceImpl extends AbstractService<Inno72SupplyChannel> implements SupplyChannelService {
    @Resource
    private Inno72SupplyChannelMapper inno72SupplyChannelMapper;

    @Resource
    private SupplyChannelDictService supplyChannelDictService;

    @Override
    public Result subCount(Inno72SupplyChannel supplyChannel) {
        String merchantId = supplyChannel.getMerchantId();
        String code = supplyChannel.getCode();
        if(StringUtil.isEmpty(merchantId) || StringUtil.isEmpty(code)){
            return ResultGenerator.genFailResult("参数有误");
        }
        inno72SupplyChannelMapper.subCount(supplyChannel);
        Map<String,Object> map = new HashMap<>();
        map.put("merchantId",merchantId);
        map.put("code",code);
        supplyChannel = inno72SupplyChannelMapper.selectByParam(map);
        return ResultGenerator.genSuccessResult(supplyChannel);
    }

    @Override
    public Result getSupplyChannel(Inno72SupplyChannel supplyChannel) {
        String merchantId = supplyChannel.getMerchantId();
        String[] goodsCodes = supplyChannel.getGoodsCodes();
        System.out.print(goodsCodes);
        goodsCodes = new String[]{"1111111","88511198f5214404beb1cd8a3a29359e"};
        if(StringUtil.isEmpty(merchantId) || goodsCodes == null){
            return ResultGenerator.genFailResult("参数有误");
        }
        Map<String,Object> map = new HashMap<>();
        map.put("merchantId",merchantId);
        map.put("goodsCodes",goodsCodes);
        List<Inno72SupplyChannel> list = inno72SupplyChannelMapper.selectListByParam(map);
        return ResultGenerator.genSuccessResult(list);
    }

    @Override
    public Result init(String merchantId) {
        Inno72SupplyChannel channel = new Inno72SupplyChannel();
        channel.setMerchantId(merchantId);
        List<Inno72SupplyChannel> supplyChannelList = inno72SupplyChannelMapper.select(channel);
        if(supplyChannelList == null || supplyChannelList.size()<= 0 ){
            List<Inno72SupplyChannelDict> dictList = supplyChannelDictService.getAll();
            if(dictList != null && dictList.size()>0){
                supplyChannelList = new ArrayList<>();
                for(Inno72SupplyChannelDict dict:dictList){
                    Inno72SupplyChannel supplyChannel = new Inno72SupplyChannel();
                    supplyChannel.setMerchantId(merchantId);
                    supplyChannel.setId(StringUtil.getUUID());
                    supplyChannel.setName(dict.getName());
                    supplyChannel.setCode(dict.getCode());
                    supplyChannel.setCreateId("0");
                    supplyChannelList.add(supplyChannel);
                    super.save(supplyChannel);
                }
            }
        }
        return null;
    }
}
