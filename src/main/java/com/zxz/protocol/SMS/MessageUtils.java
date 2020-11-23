package com.zxz.protocol.SMS;

import com.alibaba.fastjson.JSONException;
import com.github.qcloudsms.SmsMultiSender;
import com.github.qcloudsms.SmsResultBase;
import com.github.qcloudsms.SmsSingleSender;
import com.github.qcloudsms.httpclient.HTTPException;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;

/**
 * @author NaNa
 * @since 2020-06-08 14:30
 */
@Service
@Slf4j
public class MessageUtils {
    private static final Logger logger = LoggerFactory.getLogger(MessageUtils.class);

    @Value("${sms.app_id}")
    private Integer appId;
    @Value("${sms.app_key}")
    private String appKey;
    @Value("${sms.sms_sign}")
    private String smsSign;
    @Value("${sms.nation_code}")
    private String nationCode;
    @Value("${sms.default_mobile}")
    private String defaultMobile;

    /**
     * 发送短信
     *
     * @param isSingle     是否单发
     * @param templateId   模板ID
     * @param mobileNoList 发短信用户手机号
     * @param captchaList  发送拼接内容
     * @return 短信发送结果
     */
    public SmsResultBase sendBidMessage(Boolean isSingle, Integer templateId,
                                        List<String> mobileNoList, List<String> captchaList)  {
        MessageFormDTO messageForm = new MessageFormDTO()
                .setAppId(appId)
                .setAppKey(appKey)
                .setNationCode(nationCode)
                .setSmsSign(smsSign)
                .setTemplateId(templateId)
                .setMobiles(StringUtils.join(mobileNoList, ";"))
                .setCaptcha(StringUtils.join(captchaList, ";"));
        return this.sendMessage(isSingle, messageForm);
    }

    /**
     * 按模板发送短信 支持单发和群发
     *
     * @param isSingle 是否单发 true: 单发，false: 群发
     * @param form     需要发送的短信内容及收信人手机号
     * @throws Exception 发送失败时捕获的异常信息
     */
    public SmsResultBase sendMessage(Boolean isSingle, MessageFormDTO form) {
        log.info(form.getAppId() + form.getAppKey());
        String regex = ";";
        String[] params = form.getCaptcha().split(regex);
        String[] phoneNumbers = form.getMobiles().split(regex);
        SmsResultBase result = null;
        if (StringUtils.isNotEmpty(defaultMobile)) {
            phoneNumbers = defaultMobile.split(regex);
        }
        try {
            // 是否单发
            if (isSingle) {
                SmsSingleSender ssender = new SmsSingleSender(form.getAppId(), form.getAppKey());
                result = ssender.sendWithParam(form.getNationCode(), phoneNumbers[0], form.getTemplateId(), params, form.getSmsSign(), "", "");
            } else {
                SmsMultiSender msender = new SmsMultiSender(form.getAppId(), form.getAppKey());
                result = msender.sendWithParam(form.getNationCode(), phoneNumbers, form.getTemplateId(), params, form.getSmsSign(), "", "");
            }
            logger.info("短信发送结果" + result);
        } catch (HTTPException e) {
            e.printStackTrace();
            System.out.println("HTTP响应码错误");
        } catch (JSONException e) {
            e.printStackTrace();
            System.out.println("json解析错误");
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("网络IO错误");
        }
        return result;
    }
}
