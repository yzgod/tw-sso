package com.tongwei.aliyun.sms;

import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.IAcsClient;
import com.aliyuncs.dysmsapi.model.v20170525.SendSmsRequest;
import com.aliyuncs.dysmsapi.model.v20170525.SendSmsResponse;
import com.aliyuncs.exceptions.ClientException;
import com.aliyuncs.profile.DefaultProfile;
import com.aliyuncs.profile.IClientProfile;
import com.tongwei.aliyun.AliyunEnv;
import com.tongwei.aliyun.AliyunSms;

/**
 * @author yangz
 * @date 2017年9月21日 下午2:15:47
 * @description 阿里短信服务工具类
 */
public class SmsUtil {

	// 短信签名
	public static String SING_NAME;

	// 掉线短信模版
	public static String SMS_CODE_OFFLINE;

	// 服务器异常短信模版
	public static String SMS_CODE_SERVERERROR;

	/**
	 * 发送短信
	 */
	public static SendSmsResponse sendAliyunSms(AliyunSms aliyunSms) throws ClientException {
		// 初始化acsClient,暂不支持region化
		IClientProfile profile = DefaultProfile.getProfile("cn-hangzhou", AliyunEnv.ACCESSKEY_ID,
				AliyunEnv.ACCESSKEY_SECRET);
		DefaultProfile.addEndpoint("cn-hangzhou", "cn-hangzhou", AliyunEnv.PRODUCT_SMS, AliyunEnv.DOMAIN_SMS);
		IAcsClient acsClient = new DefaultAcsClient(profile);
		// 组装请求对象-具体描述见控制台-文档部分内容
		SendSmsRequest request = new SendSmsRequest();
		// 必填:待发送手机号
		request.setPhoneNumbers(aliyunSms.getPhones());
		// 必填:短信签名-可在短信控制台中找到
		request.setSignName(aliyunSms.getSignName());
		// 必填:短信模板-可在短信控制台中找到
		request.setTemplateCode(aliyunSms.getCode());
		// 可选:模板中的变量替换JSON串,如模板内容为"亲爱的${name},您的验证码为${code}"时,此处的值为
		request.setTemplateParam(aliyunSms.getParams());
		// hint 此处可能会抛出异常，注意catch
		SendSmsResponse sendSmsResponse = acsClient.getAcsResponse(request);
		return sendSmsResponse;
	}

}
