package com.zxz.protocol.SMS;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class MessageFormDTO {

    /**
     * 短信模板id
     */
    private Integer templateId;

    /**
     * 验证码,短信内容;  多个内容用";"隔开
     */
    private String captcha;

    /**
     * 手机号码  多个手机用;隔开
     */
    private String mobiles;
    private Integer appId;
    private String appKey;
    private String smsSign;
    private String nationCode;

}
