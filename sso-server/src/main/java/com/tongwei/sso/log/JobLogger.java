package com.tongwei.sso.log;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Date;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.text.StrBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.tongwei.auth.log.LogBean;
import com.tongwei.auth.log.TextLogBean;
import com.tongwei.auth.log.TextUserLogBean;
import com.tongwei.sso.util.DateUtils;

/**
 * @author yangz
 * @date 2018年2月26日 上午10:32:15
 * @description 应用文件日志logger
 */
public class JobLogger {
    private static Logger logger = LoggerFactory.getLogger("job logger");

    private static final String basePath = "/data/";

    public static void logDetail(LogBean logBean) {
        String appCode = logBean.getAppCode();
        Date date = logBean.getDate();
        if (StringUtils.isBlank(appCode)) {
            logger.error("log appcode is blank");
            return;
        }
        if (date == null) {
            logger.error("log date is null");
            return;
        }
        String log = logHandle(logBean);
        StringBuilder sb = new StringBuilder();
        sb.append(basePath).append(appCode).append(DateUtils.formatDate(date, "/yyyy/MM/"));
        String path = sb.toString();
        File p = new File(path);
        if (!p.exists()) {
            p.mkdirs();
        }
        sb.append(DateUtils.formatDate(date, "MM-dd")).append(".log");
        String fileName = sb.toString();
        appendLog(fileName, log);
    }

    private static String logHandle(LogBean logBean) {
        StrBuilder sb = new StrBuilder();
        Date createDate = logBean.getDate();
        if (logBean instanceof TextUserLogBean) {
            sb.append("[user] ").append(((TextUserLogBean) logBean).getLoginName()).append(" ");
        }
        sb.append(DateUtils.formatDateTime(createDate)).append(" [");
        TextLogBean textBean = (TextLogBean) logBean;
        sb.append(textBean.getTh()).append("] ");
        Class<?> clz = textBean.getClz();
        if (clz != null) {
            sb.append(clz).append(" ");
        }
        String msg = textBean.getMsg();
        if (msg != null) {
            sb.append("[msg] ").append(msg).append(" ");
        }
        String error = textBean.getErrorMsg();
        if (error != null) {
            sb.append("[errorMsg] ").append(error);
        }
        return sb.toString();
    }

    private static void appendLog(String logFileName, String appendLog) {

        if (logFileName == null || logFileName.trim().length() == 0) {
            return;
        }
        File logFile = new File(logFileName);

        if (!logFile.exists()) {
            try {
                logFile.createNewFile();
            } catch (IOException e) {
                logger.error(e.getMessage(), e);
                return;
            }
        }

        if (appendLog == null) {
            appendLog = "";
        }
        appendLog += "\r\n";

        try {
            FileOutputStream fos = null;
            try {
                fos = new FileOutputStream(logFile, true);
                fos.write(appendLog.getBytes("utf-8"));
                fos.flush();
            } finally {
                if (fos != null) {
                    try {
                        fos.close();
                    } catch (IOException e) {
                        logger.error(e.getMessage(), e);
                    }
                }
            }
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }

    }

}