package com.github.quanqinle.readexcel;

import java.util.HashMap;
import java.util.Map;

/**
 * @author quanqinle
 * @date
 */
public class Constants {

    /**
     * 发函方式
     */
    public final static Map <String, Integer> SEND_TYPE_MAP = new HashMap<>() {
        {
            put("邮寄", 1);
            put("电子邮件", 2);
            put("跟函", 3);
        }
    };

    /**
     * 选取样本特征
     * 注意：只key有效，value实际上是无意义的值
     */
    public final static Map <String, Integer> SAMPLE_FEATURE_MAP = new HashMap<>() {
        {
            put("大额", -1);
            put("异常", -1);
            put("账龄长", -1);
            put("零账户", -1);
            put("随机", -1);
            put("有纠纷", -1);
            put("重要客户", -1);
        }
    };

    /**
     * 往来明细表 往来账项列示  会计科目
     */
    public final static Map<String, Integer> CONTACT_SUBJECT_MAP = new HashMap<>() {
        {
            put("应收账款",16);
            put("预收款项",17);
            put("应付账款",18);
            put("预付款项",19);
            put("其他应收款",20);
            put("其他应付款",21);
            put("长期应付款",24);
            put("长期应收款",25);
        }
    };

    /**
     * 往来明细表 交易列示 交易类型
     */
    public final static Map <String, Integer> DEAL_TYPE_MAP = new HashMap<>() {
        {
            put("销售商品/提供劳务（营业收入）",26);
            put("采购商品/接受劳务（存货）",27);
            put("采购长期资产",28);
            put("其他",29);
        }
    };

    private Constants() {
        
    }

}
