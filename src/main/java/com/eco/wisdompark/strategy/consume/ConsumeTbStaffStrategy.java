package com.eco.wisdompark.strategy.consume;

import com.eco.wisdompark.enums.DiningType;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

/**
 * 体育局职工消费策略
 * 早餐5元，没有次数限制
 * 午餐和晚餐前三次刷卡为20元，三次以上刷卡为29元，没有次数限制
 */
@Component
public class ConsumeTbStaffStrategy {

    /**
     * 根据用餐类型和消费次数获取消费金额
     *
     * @param diningType  用餐类型
     * @param consumeTime 消费次数
     * @return 消费金额
     */
    public BigDecimal consume(DiningType diningType, int consumeTime) {
        if (DiningType.BREAKFAST.equals(diningType))
            return new BigDecimal(5);
        else
            return calculateAmount(consumeTime);
    }

    /**
     * 根据消费次数获取消费金额
     *
     * @param consumeTime 消费次数
     * @return 消费金额
     */
    private BigDecimal calculateAmount(int consumeTime) {
        if (consumeTime < 3) {
            return new BigDecimal(20);
        } else {
            return new BigDecimal(29);
        }
    }

}