package com.inno72.project.vo;

import com.inno72.project.model.Inno72Merchant;

import lombok.Data;

@Data
public class Inno72MerchantVo extends Inno72Merchant{
	
    private String status;

    private String merchantId;


}