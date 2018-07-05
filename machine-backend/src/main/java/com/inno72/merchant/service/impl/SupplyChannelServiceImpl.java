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
import com.inno72.merchant.vo.SupplyChannelVo;
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

    @Override
    public Result merge(Inno72SupplyChannel supplyChannel) {
        String fromCode = supplyChannel.getFromCode();
        String toCode = supplyChannel.getToCode();
        String merchantId = supplyChannel.getMerchantId();
        if(StringUtil.isEmpty(fromCode) || StringUtil.isEmpty(toCode) || StringUtil.isEmpty(merchantId)){
            return ResultGenerator.genFailResult("参数有误");
        }
        int from = Integer.parseInt(fromCode);
        int to = Integer.parseInt(toCode);
        if(from%2==0 && to==from-1){
            Map<String,Object> map = new HashMap<>();
            map.put("merchantId",merchantId);
            String[] codes = new String[2];
            codes[0] = fromCode;
            codes[1] = toCode;
            map.put("codes",codes);
            List<Inno72SupplyChannel> list = inno72SupplyChannelMapper.selectListByParam(map);
            if(list != null && list.size()==2){
                for(Inno72SupplyChannel channel : list){
                    int goodsCount = channel.getGoodsCount();
                    if(goodsCount != 0){
                        return ResultGenerator.genFailResult("当前货道未清0不能合并");
                    }
                }
                Inno72SupplyChannel channel = new Inno72SupplyChannel();
                channel.setCode(fromCode);
                channel.setMerchantId(merchantId);
                channel.setParentCode(toCode);
                channel.setStatus(1);
                inno72SupplyChannelMapper.updateByParam(channel);
                return ResultGenerator.genSuccessResult();
            }else{
                return ResultGenerator.genFailResult("货道数据有误");
            }

        }else{
            return ResultGenerator.genFailResult("当前货道不能合并");
        }
    }

    @Override
    public Result split(Inno72SupplyChannel supplyChannel) {
        String code = supplyChannel.getCode();
        String merchantId = supplyChannel.getMerchantId();
        if(StringUtil.isEmpty(code) || StringUtil.isEmpty(merchantId)){
            return ResultGenerator.genFailResult("参数有误");
        }
        Map<String,Object> map = new HashMap<>();
        map.put("merchantId",merchantId);
        map.put("code",code);
        supplyChannel = inno72SupplyChannelMapper.selectByParam(map);
        if(supplyChannel != null){
            int goodsCount = supplyChannel.getGoodsCount();
            if(goodsCount == 0){
                Inno72SupplyChannel upChildChannel = new Inno72SupplyChannel();
                upChildChannel.setMerchantId(merchantId);
                upChildChannel.setParentCode(code);
                upChildChannel.setStatus(0);
                int count = inno72SupplyChannelMapper.updateChild(upChildChannel);
                if(count == 1){
                    return ResultGenerator.genSuccessResult();
                }else{
                    return ResultGenerator.genFailResult("货道未合并不能拆分");
                }

            }else{
                return ResultGenerator.genFailResult("货道未清0不能拆分");
            }
        }else{
            return ResultGenerator.genFailResult("操作货道有误");
        }
    }

    @Override
    public Result clear(Inno72SupplyChannel supplyChannel) {
        String[] codes = supplyChannel.getCodes();
        String merchantId = supplyChannel.getMerchantId();
        if(codes == null || StringUtil.isEmpty(merchantId)){
            return ResultGenerator.genFailResult("参数有误");
        }
        inno72SupplyChannelMapper.updateByParam(supplyChannel);
        return null;
    }
}
