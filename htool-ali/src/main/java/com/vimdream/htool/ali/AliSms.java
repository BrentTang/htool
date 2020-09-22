package com.vimdream.htool.ali;

import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.IAcsClient;
import com.aliyuncs.dysmsapi.model.v20170525.SendSmsRequest;
import com.aliyuncs.dysmsapi.model.v20170525.SendSmsResponse;
import com.aliyuncs.exceptions.ClientException;
import com.aliyuncs.http.MethodType;
import com.aliyuncs.profile.DefaultProfile;
import com.aliyuncs.profile.IClientProfile;
import com.vimdream.htool.string.StringUtil;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

/**
 * @Title: AliSms
 * @Author vimdream
 * @ProjectName htool
 * @Date 2020/9/22 10:15
 */
@Slf4j
public class AliSms {

    private final String accessKeyId;
    private final String accessKeySecret;

    public AliSms(String accessKeyId, String accessKeySecret) {
        this.accessKeyId = accessKeyId;
        this.accessKeySecret = accessKeySecret;
    }

    public SendSmsResponse sendSms(String signName, String templateCode, String phone, String templateParam, String smsUpExtendCode, String outId) throws ClientException {
        // 设置超时时间-可自行调整
        System.setProperty("sun.net.client.defaultConnectTimeout", "10000");
        System.setProperty("sun.net.client.defaultReadTimeout", "10000");

        //初始化ascClient需要的几个参数
        //  短信API产品名称（短信产品名固定）
        final String product = "Dysmsapi";
        //  短信API产品域名（接口地址固定）
        final String domain = "dysmsapi.aliyuncs.com";

        //初始化ascClient,暂时不支持多region
        IClientProfile profile = DefaultProfile.getProfile("cn-hangzhou", accessKeyId,
                accessKeySecret);
        DefaultProfile.addEndpoint("cn-hangzhou", "cn-hangzhou", product, domain);
        IAcsClient acsClient = new DefaultAcsClient(profile);

        //组装请求对象
        SendSmsRequest request = new SendSmsRequest();
        //使用post提交
        request.setMethod(MethodType.POST);
        //必填:待发送手机号。支持以逗号分隔的形式进行批量调用，批量上限为1000个手机号码,
        // 批量调用相对于单条调用及时性稍有延迟,
        // 验证码类型的短信推荐使用单条调用的方式；
        // 发送国际/港澳台消息时，接收号码格式为国际区号+号码，如“85200000000”
        request.setPhoneNumbers(phone);

        //必填:短信签名-可在短信控制台中找到
        request.setSignName(signName);
        //必填:短信模板-可在短信控制台中找到，发送国际/港澳台消息时，请使用国际/港澳台短信模版
        request.setTemplateCode(templateCode);

        //友情提示:如果JSON中需要带换行符,请参照标准的JSON协议对换行符的要求,比如短信内容中包含\r\n的情况在JSON中需要表示成\\r\\n,否则会导致JSON在服务端解析失败
        //参考：request.setTemplateParam("{\"变量1\":\"值1\",\"变量2\":\"值2\",\"变量3\":\"值3\"}")
        if (StringUtil.isNotBlank(templateParam)) {
            request.setTemplateParam(templateParam);
        }

        //可选-上行短信扩展码(扩展码字段控制在7位或以下，无特殊需求用户请忽略此字段)
        if (StringUtil.isNotBlank(smsUpExtendCode)) {
            request.setSmsUpExtendCode(smsUpExtendCode);
        }

        //可选:outId为提供给业务方扩展字段,最终在短信回执消息中将此值带回给调用者
        if (StringUtil.isNotBlank(outId)) {
            request.setOutId(outId);
        }

        SendSmsResponse response = acsClient.getAcsResponse(request);
        return response;
    }

    public SmsResponse send(String signName, String templateCode, String phone, String templateParam) {
        try {
            SendSmsResponse response = sendSms(signName, templateCode, phone, templateParam, null, null);
            String code = response.getCode();
            if (StringUtil.isNotBlank(code)) {
                switch (code) {
                    case "isp.RAM_PERMISSION_DENY": return SmsResponse.fail("RAM权限DENY。");
                    case "isv.OUT_OF_SERVICE": return SmsResponse.fail("业务停机。");
                    case "isv.PRODUCT_UN_SUBSCRIPT": return SmsResponse.fail("未开通云通信产品的阿里云客户。");
                    case "isv.PRODUCT_UNSUBSCRIBE": return SmsResponse.fail("产品未开通。");
                    case "isv.ACCOUNT_NOT_EXISTS": return SmsResponse.fail("账户不存在。");
                    case "isv.ACCOUNT_ABNORMAL": return SmsResponse.fail("账户异常。");
                    case "isv.SMS_TEMPLATE_ILLEGAL": return SmsResponse.fail("短信模板不合法。");
                    case "isv.SMS_SIGNATURE_ILLEGAL": return SmsResponse.fail("短信签名不合法。");
                    case "isv.INVALID_PARAMETERS": return SmsResponse.fail("参数异常。");
                    case "isp.SYSTEM_ERROR": return SmsResponse.fail("系统错误。");
                    case "isv.MOBILE_NUMBER_ILLEGAL": return SmsResponse.fail("非法手机号。");
                    case "isv.MOBILE_COUNT_OVER_LIMIT": return SmsResponse.fail("手机号码数量超过限制。");
                    case "isv.TEMPLATE_MISSING_PARAMETERS": return SmsResponse.fail("模板缺少变量。");
                    case "isv.BUSINESS_LIMIT_CONTROL": return SmsResponse.fail("业务限流。");
                    case "isv.INVALID_JSON_PARAM": return SmsResponse.fail("JSON参数不合法，只接受字符串值。");
                    case "isv.BLACK_KEY_CONTROL_LIMIT": return SmsResponse.fail("黑名单管控。");
                    case "isv.PARAM_LENGTH_LIMIT": return SmsResponse.fail("参数超出长度限制。");
                    case "isv.PARAM_NOT_SUPPORT_URL": return SmsResponse.fail("不支持URL。");
                    case "isv.AMOUNT_NOT_ENOUGH": return SmsResponse.fail("账户余额不足。");
                    default:
                        return SmsResponse.success();
                }
            }
            return SmsResponse.fail("响应失败");
        } catch (ClientException e) {
            log.error(e.getErrMsg(), e);
            return SmsResponse.error(e.getErrMsg());
        }
    }

    @Getter
    static class SmsResponse {

        private SmsResponse(int status, String message) {
            this.status = status;
            this.message = message;
        }

        private int status;
        private String message;

        public static SmsResponse success() {
            return new SmsResponse(200, "OK");
        }

        public static SmsResponse fail(String message) {
            return new SmsResponse(400, message);
        }

        public static SmsResponse error(String message) {
            return new SmsResponse(500, message);
        }
    }

}
