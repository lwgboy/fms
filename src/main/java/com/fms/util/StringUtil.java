package com.fms.util;

import com.alibaba.fastjson.JSONObject;

import javax.servlet.*;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpServletResponseWrapper;
import java.io.*;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by xujiawei on 2016/4/28.
 */
public class StringUtil {
    /**
     * ����Ƿ��������ַ�
     * @param text
     * @return
     */
    public static boolean chkSpecialString(String text) {
        String str = "[`~!@#$%^&*()+=|{}':;',\\[\\].<>/?~��@#��%����&*��������+|{}������������������������]";
        Pattern pattern = Pattern.compile(str);
        Matcher match = pattern.matcher(text);
        if (match.find())
            return true;
        else
            return false;
    }

    /**
     * ���SQL�ؼ��ַ�ֹSQLע��
     * @param text �ı�����
     * @return
     */
    public static String chkSQLString(String text) {
        String strResult = text;
        String strKeyword = "DELETE|UPDATE|DROP|UNION|SELECT|EXEC|XP_CMDSHELL|XP_REGREAD|CHAR(|TRUNCATE";
        strResult = strResult.replace("'", "''");
        strResult = strResult.replace(";", "");
        String[] arr_str = strKeyword.split("\\|");
        for (String str : arr_str) {
            if (strResult.indexOf(str) >= 0) {
                strResult = "";
                break;
            }
        }
        return strResult;
    }

    /**
     * Get hex string from byte array
     */
    public static String toHexString(byte[] res) {
        StringBuffer sb = new StringBuffer(res.length << 1);
        for (int i = 0; i < res.length; i++) {
            String digit = Integer.toHexString(0xFF & res[i]);
            if (digit.length() == 1) {
                digit = '0' + digit;
            }
            sb.append(digit);
        }
        return sb.toString().toUpperCase();
    }

    public static boolean isEmpty(String str){
        if(null==str || "".equals(str.trim())){
            return true;
        }
        return false;
    }

    public static boolean isNotEmpty(String str){
        if(null!=str && !"".equals(str.trim())){
            return true;
        }
        return false;
    }

    public static boolean isInteger(String str){
        Pattern pattern = Pattern.compile("^[-\\+]?[\\d]*$");
        return pattern.matcher(str).matches();
    }

    public static boolean isDouble(String str){
        Pattern pattern = Pattern.compile("^[-\\+]?[\\d]*[.]?[\\d]+$");
        return pattern.matcher(str).matches();
    }

    public static boolean isNumber(String str){
        return isInteger(str) || isDouble(str);
    }

    /**
     * ��double���ݽ���ȡ����.
     * @param value  double����.
     * @param scale  ����λ��(������С��λ��).
     * @param roundingMode  ����ȡֵ��ʽ.[BigDecimal.ROUND_DOWN����ȡ��;BigDecimal.ROUND_UP����ȡ��;BigDecimal.ROUND_HALF_UP��������]
     * @return ���ȼ���������.
     */
    public static double round(double value, int scale,int roundingMode) {
        BigDecimal bd = new BigDecimal(value);
        bd = bd.setScale(scale, roundingMode);
        double d = bd.doubleValue();
        bd = null;
        return d;
    }

    public static String roundToString(double value, int scale,int roundingMode){
        BigDecimal bd = new BigDecimal(value);
        bd = bd.setScale(scale, roundingMode);
        double d = bd.doubleValue();
        bd = null;
        DecimalFormat df=new DecimalFormat("#,###,###,##0.00");
        return df.format(d);
    }


    /**
     * double ���
     * @param d1
     * @param d2
     * @return
     */
    public static double sum(double d1,double d2){
        BigDecimal bd1 = new BigDecimal(Double.toString(d1));
        BigDecimal bd2 = new BigDecimal(Double.toString(d2));
        return bd1.add(bd2).doubleValue();
    }


    /**
     * double ���
     * @param d1
     * @param d2
     * @return
     */
    public static double sub(double d1,double d2){
        BigDecimal bd1 = new BigDecimal(Double.toString(d1));
        BigDecimal bd2 = new BigDecimal(Double.toString(d2));
        return bd1.subtract(bd2).doubleValue();
    }

    /**
     * double �˷�
     * @param d1
     * @param d2
     * @return
     */
    public static double mul(double d1,double d2){
        BigDecimal bd1 = new BigDecimal(Double.toString(d1));
        BigDecimal bd2 = new BigDecimal(Double.toString(d2));
        return bd1.multiply(bd2).doubleValue();
    }


    /**
     * double ����
     * @param d1
     * @param d2
     * @param scale �������� С����λ��
     * @return
     */
    public static double div(double d1,double d2,int scale){
        //  ��Ȼ�ڴ�֮ǰ����Ҫ�жϷ�ĸ�Ƿ�Ϊ0��
        //  Ϊ0����Ը���ʵ����������Ӧ�Ĵ���

        BigDecimal bd1 = new BigDecimal(Double.toString(d1));
        BigDecimal bd2 = new BigDecimal(Double.toString(d2));
        return bd1.divide
                (bd2, scale, BigDecimal.ROUND_HALF_UP).doubleValue();
    }

    /**
     * @return ���ذ�ֱ� 50%
     */
    public static String numDivide(double d1,int scale){
        NumberFormat nFromat = NumberFormat.getPercentInstance();
        nFromat.setMinimumFractionDigits(scale);
        return nFromat.format(d1);
    }

    public static String getDateTimeStr(String format){
        SimpleDateFormat sdf=new SimpleDateFormat(format);
        return sdf.format(new Date());
    }
    public static String getDateTimeStr(String format,Date date){
        SimpleDateFormat sdf=new SimpleDateFormat(format);
        return sdf.format(date);
    }

    /**
     * ��ȡcookie
     * @param request
     * @param cookieName
     * @return
     */
    public static Cookie getCookie(HttpServletRequest request, String cookieName) {
        Cookie retCookie = null;
        if (null==cookieName || "".equals(cookieName.trim())) {
            return retCookie;
        }
        Cookie[] cookies = request.getCookies();
        if (null != cookies) {
            for (Cookie cookie : cookies) {
                if (cookie.getName().equals(cookieName)) {
                    retCookie = cookie;
                    break;
                }
            }
        }
        return retCookie;
    }

    /**
     * дCookie
     *
     * @param request
     * @param response
     * @param cookieKey
     * @param cookieValue
     * @param maxAgeByDays Cookie���������
     */
    public static void writeCookie(HttpServletRequest request, HttpServletResponse response,boolean httpOnly, String cookieKey, String cookieValue, int maxAgeByDays) {
        int day = 24 * 60 * 60;
        Cookie cookie = getCookie(request, cookieKey);
        if (cookie == null) {
            Cookie newCookie = new Cookie(cookieKey, cookieValue);
            newCookie.setMaxAge(maxAgeByDays * day);
            newCookie.setHttpOnly(true);
            newCookie.setPath("/");
            newCookie.setHttpOnly(httpOnly);
            response.addCookie(newCookie);
        } else {
            cookie.setValue(cookieValue);
            cookie.setMaxAge(maxAgeByDays * day);
            cookie.setHttpOnly(true);
            cookie.setPath("/");
            cookie.setHttpOnly(httpOnly);
            response.addCookie(cookie);
        }

    }

    /**
     * ���ļ���ȡΪString
     * @param filePath
     * @return
     */
    public static String ReadFileToString(String filePath){
        String resultStr="";
        if(null == filePath || "".equals(filePath.trim())){
            return resultStr;
        }
        File file=new File(filePath);
        if(!file.exists()){
            return resultStr;
        }
        try {
            StringBuffer sb=new StringBuffer();
            InputStreamReader read = new InputStreamReader(new FileInputStream(file), "UTF-8");
            BufferedReader bufferedReader = new BufferedReader(read);
            String lineTxt = null;
            while ((lineTxt = bufferedReader.readLine()) != null) {
                sb.append(lineTxt);
            }
            read.close();
            resultStr=sb.toString();
        }catch (Exception e){
            e.printStackTrace();
            return resultStr;
        }
        return resultStr;
    }

    /**
     * ���ж�ȡ�ļ�Ϊ�ַ���
     * @param filePath
     * @return
     */
    public static List<String> ReadFileToLineList(String filePath){
        String resultStr="";
        List<String> result=null;
        if(null == filePath || "".equals(filePath.trim())){
            return result;
        }
        File file=new File(filePath);
        if(!file.exists()){
            return result;
        }
        try {
            result=new ArrayList<>();
            StringBuffer sb=new StringBuffer();
            InputStreamReader read = new InputStreamReader(new FileInputStream(file), "UTF-8");
            BufferedReader bufferedReader = new BufferedReader(read);
            String lineTxt = null;
            while (StringUtil.isNotEmpty((lineTxt = bufferedReader.readLine())) ) {
                result.add(lineTxt.trim());
            }
            read.close();
            return result;
        }catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }

    /**
     * ���ɾ�̬HTMLҳ��ķ���
     *
     * @param request
     *            �������
     * @param response
     *            ��Ӧ����
     * @param servletContext
     *            Servlet������
     * @param fileName
     *            �ļ�����
     * @param fileFullPath
     *            �ļ�����·��
     * @param jspPath
     *            ��Ҫ���ɾ�̬�ļ���JSP·��(��Լ���)
     * @throws IOException
     * @throws ServletException
     */
    public static void createHtml(HttpServletRequest request, HttpServletResponse response,
                       ServletContext servletContext, String fileName, String fileFullPath, String jspPath)
            throws ServletException, IOException {
//        response.setContentType("text/html;charset=gb2312");// ����HTML���������(��HTML�ļ�����)
        RequestDispatcher rd = servletContext.getRequestDispatcher(jspPath);// �õ�JSP��Դ
        final ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();// ���ڴ�ServletOutputStream�н�����Դ
        final ServletOutputStream servletOuputStream = new ServletOutputStream() {
            @Override
            public boolean isReady() {
                return false;
            }

            @Override
            public void setWriteListener(WriteListener writeListener) {

            }// ���ڴ�HttpServletResponse�н�����Դ
            public void write(byte[] b, int off, int len) {
                byteArrayOutputStream.write(b, off, len);
            }

            public void write(int b) {
                byteArrayOutputStream.write(b);
            }
        };
        final PrintWriter printWriter = new PrintWriter(new OutputStreamWriter(byteArrayOutputStream));// ��ת���ֽ���ת�����ַ���
        HttpServletResponse httpServletResponse = new HttpServletResponseWrapper(response) {// ���ڴ�response��ȡ�������Դ(��д����������)
            public ServletOutputStream getOutputStream() {
                return servletOuputStream;
            }

            public PrintWriter getWriter() {
                return printWriter;
            }
        };
        rd.include(request, httpServletResponse);// ���ͽ����
        printWriter.flush();// ˢ�»��������ѻ��������������
        FileOutputStream fileOutputStream = new FileOutputStream(fileFullPath);
        OutputStreamWriter osw=new OutputStreamWriter(fileOutputStream,"utf-8");
        osw.write(byteArrayOutputStream.toString());
//        byteArrayOutputStream.writeTo(fileOutputStream);// ��byteArrayOuputStream�е���Դȫ��д�뵽fileOuputStream��
        byteArrayOutputStream.close();
        fileOutputStream.close();// �ر�����������ͷ������Դ
//        response.sendRedirect(fileName);// ����ָ���ļ������ͻ���
    }

}
