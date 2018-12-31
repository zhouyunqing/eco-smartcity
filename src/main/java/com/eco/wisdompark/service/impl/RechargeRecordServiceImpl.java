package com.eco.wisdompark.service.impl;

import com.eco.wisdompark.domain.model.RechargeRecord;
import com.eco.wisdompark.enums.RechargeType;
import com.eco.wisdompark.mapper.RechargeRecordMapper;
import com.eco.wisdompark.service.RechargeRecordService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * <p>
 * CPU卡-充值记录表 服务实现类
 * </p>
 *
 * @author litao
 * @since 2018-12-28
 */
@Service
public class RechargeRecordServiceImpl extends ServiceImpl<RechargeRecordMapper, RechargeRecord> implements RechargeRecordService {

    @Override
    public boolean saveRechargeRecord(String cardId, String cardSerialNo, BigDecimal amount, RechargeType rechargeType,
                                  String importSerialNo, Integer userId) {
        RechargeRecord rechargeRecord = new RechargeRecord();
        rechargeRecord.setCardId(cardId);
        rechargeRecord.setCardSerialno(cardSerialNo);
        rechargeRecord.setAmount(amount);
        rechargeRecord.setRechargeType(rechargeType.getCode());
        if (StringUtils.isEmpty(importSerialNo)){
            rechargeRecord.setImportSerialno(importSerialNo);
        }
        rechargeRecord.setUserId(userId);
        rechargeRecord.setCreateTime(LocalDateTime.now());
        return save(rechargeRecord);
    }

}