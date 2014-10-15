package com.runssnail.monolith.socket.mina;

import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.nio.charset.CharsetEncoder;

import org.apache.mina.core.buffer.IoBuffer;

import com.runssnail.monolith.lang.StringUtil;
import com.runssnail.monolith.socket.message.codec.CodecUtils;



/**
 * 模拟交易所发起请求
 * 
 * @author zhengwei
 */
public class CodecClientTest {

    private static final int port = CodecServerTest.port;

    /**
     * @param args
     * @throws Exception
     */
    public static void main(String[] args) throws Exception {

        test("6200");
    }

    /**
     * 设置D段固定长度部分的请求参数
     * 
     * @param transCode
     * @return
     */
    private static String buildFixedParams(String transCode) {
        String body = "";
        if ("6200".equals(transCode)) {
            body = build6200Params();
        }

        return body;
    }

    /**
     * E段参数
     * 
     * @param transCode
     * @return
     */
    private static String buildCBlockParams(String transCode) {

        String body = "";
        if ("6200".equals(transCode)) {
            body = build6200Params();
        }

        return body;
    }

    private static void test(String transCode) throws Exception {
        CodecServerTest server = new CodecServerTest();
        server.start(transCode);
        send(transCode);

        server.endServer();
    }

    private static void send(String transCode) throws Exception {
        Socket socket = new Socket();
        socket.connect(new InetSocketAddress("localhost", port));
        OutputStream out = socket.getOutputStream();
        writeBytes(transCode, out);
        InputStream in = socket.getInputStream();
        byte[] buffer = new byte[2048];
        int len = 0;
        while ((len = in.read(buffer)) != -1) {
            System.out.println("len=" + len + ", get==" + new String(buffer, com.runssnail.monolith.socket.Constants.DEFAULT_CHARSET));
        }

        System.out.println("received, " + new String(buffer));
        in.close();
        out.close();
        socket.close();
    }

    /**
     * a + b + c
     * 
     * @param transCode
     * @param out
     * @throws Exception
     */
    private static void writeBytes(String transCode, OutputStream out) throws Exception {

        String headerStr = buildHead(transCode);

//        String fixedParams = buildFixedParams(transCode);

        String cBlockParams = buildCBlockParams(transCode);

        IoBuffer bb = IoBuffer.allocate(256);
        bb.setAutoExpand(true);
        
       

        CharsetEncoder encoder = com.runssnail.monolith.socket.Constants.DEFAULT_CHARSET.newEncoder();

        int headerLen = headerStr.getBytes(com.runssnail.monolith.socket.Constants.DEFAULT_CHARSET).length;
        
        

        //int fixedLen = fixedParams.getBytes(com.runssnail.monolith.socket.Constants.DEFAULT_CHARSET).length;

        int cLen = cBlockParams.getBytes(com.runssnail.monolith.socket.Constants.DEFAULT_CHARSET).length;
        
        int len = 4 + headerLen + cLen; // a + b + c = 整个报文长度

//        int dLen = headerLen + fixedLen;
//        int bLen = headerLen ;

//        bb.putString(StringUtil.alignRight(String.valueOf(bLen), 8, "0"), encoder); // a
        
        bb.putInt(len);

//        bb.putString(StringUtil.alignRight(String.valueOf(cLen), 8, "0"), encoder); // b

//        bb.putString(StringUtil.alignRight("0", 16, "0"), encoder); // c

        bb.putString(headerStr, encoder);
//        bb.putString(fixedParams, encoder);

        if (StringUtil.isNotBlank(cBlockParams)) {
            bb.putString(cBlockParams, encoder);
        }

        // bb.order(ProtocolDTO.byteOrder);
        // bb.putInt(body.length());

        byte[] b = new byte[bb.position()];

        System.out.println("send bytes=" + b.length);

        bb.flip();
        bb.get(b);

        out.write(b);
    }

    private static String buildHead(String transCode) {
        String head = null;
        // if ("3100".equals(transCode)) {
        // head = build3100Head();
        // } else if ("6200".equals(transCode)) {
        // head = build6200Head();
        // }

        head = buildDefaultHead(transCode);

        return head;
    }

    private static String buildDefaultHead(String transCode) {
        // 版本号 2 目前默认01
        // 交易代码 4 0001
        // 交易所编号 6
        // 银行代码 6
        // 请求方流水号 20 不足补空
        // 交易日期 8 YYYYMMDD
        // 交易时间 6 HH24MiSS
        // 记录条数 8 前补0

        StringBuilder sb = new StringBuilder();
        sb.append("01");
        sb.append(transCode);// .append(com.runssnail.monolith.socket.Constants.DEFAULT_SPLIT_CHAR); // 交易代码
        sb.append("000001");// 交易所编号

        sb.append("001001");// 银行代码

        sb.append("00100100100100002222");// .append(com.runssnail.monolith.socket.Constants.DEFAULT_SPLIT_CHAR); // 前台流水号12

        sb.append("20130822");// .append(com.runssnail.monolith.socket.Constants.DEFAULT_SPLIT_CHAR); // 交易日期

        sb.append("153022");// .append(com.runssnail.monolith.socket.Constants.DEFAULT_SPLIT_CHAR); // 交易时间

        sb.append("00000001");// .append(com.runssnail.monolith.socket.Constants.DEFAULT_SPLIT_CHAR); // 记录条数

        return sb.toString();
    }

    private static String build6200Head() {
        StringBuilder sb = new StringBuilder();

        // bankAcc 银行账号 C(30) 银行客户结算账号 M
        // fundAcc 交易账号 C(30) 交易所的交易账号 M
        // moneyType 币种代码 C(3) CNY－人民币 HKD－港币 USD－美元 M
        // transAmount 转账金额 N(15) 单位精确到分 M
        sb.append("");

        return sb.toString();
    }

    private static String build6200Params() {

        // bankAcc 银行账号 C(30) 银行客户结算账号 M
        // fundAcc 交易账号 C(30) 交易所的交易账号 M
        // moneyType 币种代码 C(3) CNY－人民币 HKD－港币 USD－美元 M
        // transAmount 转账金额 N(15) 单位精确到分 M
        StringBuilder sb = new StringBuilder();
        sb.append(CodecUtils.alignLeft("22222222222222", "", 30, " "));

        sb.append(CodecUtils.alignLeft("33333333333333", "", 30, " "));

        sb.append("CNY");
        sb.append("000000000010000");
        return sb.toString();
    }

    private static String build3101Body() {
        // BankAcc 银行账号 22 银行客户的存折号 M
        // AccType 账号类型 1 0：银行卡；1：存折 M
        StringBuilder sb = new StringBuilder();
        sb.append(StringUtil.alignLeft("333333332222222222222", 22, " "));

        sb.append("0");

        return sb.toString();
    }

    public static String build3101Head() {

        // 交易代码 4 0001
        // 机构代码（银行机构代码） 6 811801 取自交易所维护表
        // 核算机构 6 811800 取自交易所维护表
        // 交易柜员号 6 980066取自交易所维护表
        // 前台流水号 12
        // 交易日期 8 YYYYMMDD
        // 交易时间 6 HH24MiSS
        // 渠道来源 2 Y，可以预留枚举
        // 文件名 12 后补空，是随机一个数字
        // 文件笔数 8 记录条数，前补0

        StringBuilder sb = new StringBuilder();

        sb.append("3101");// .append(com.runssnail.monolith.socket.Constants.DEFAULT_SPLIT_CHAR); // 交易代码
        sb.append("000001");// .append(com.runssnail.monolith.socket.Constants.DEFAULT_SPLIT_CHAR); // 机构代码
        sb.append("000301");// .append(com.runssnail.monolith.socket.Constants.DEFAULT_SPLIT_CHAR); // 核算机构

        sb.append("001001");// .append(com.runssnail.monolith.socket.Constants.DEFAULT_SPLIT_CHAR); // 柜员号

        sb.append("001001001001");// .append(com.runssnail.monolith.socket.Constants.DEFAULT_SPLIT_CHAR); // 前台流水号12

        sb.append("20130822");// .append(com.runssnail.monolith.socket.Constants.DEFAULT_SPLIT_CHAR); // 交易日期

        sb.append("153022");// .append(com.runssnail.monolith.socket.Constants.DEFAULT_SPLIT_CHAR); // 交易时间
        sb.append("Y ");

        sb.append("filename    ");// .append(com.runssnail.monolith.socket.Constants.DEFAULT_SPLIT_CHAR); // 文件名

        sb.append("00000001");// .append(com.runssnail.monolith.socket.Constants.DEFAULT_SPLIT_CHAR); // 记录条数

        return sb.toString();

    }

    private static String build3500Body() {

        // CompanyNo 交易所编号
        // BankAcc 银行账号
        // AccType 账号类型
        // FundAcc 交易账号
        // PinBlock 交易账号密码
        // AccType 账号类别 ??????
        // IDType 证件类别
        // IDNo 证件号码
        // CustName 客户名称
        // Sex 客户性别

        StringBuilder sb = new StringBuilder();

        sb.append("323232").append(com.runssnail.monolith.socket.Constants.DEFAULT_SPLIT_CHAR); // 交易所编号
        sb.append("100000033333"); // 银行帐号
        sb.append(com.runssnail.monolith.socket.Constants.DEFAULT_SPLIT_CHAR);

        sb.append("1").append(com.runssnail.monolith.socket.Constants.DEFAULT_SPLIT_CHAR); // 帐号类型

        sb.append("00000101"); // 交易帐号
        sb.append(com.runssnail.monolith.socket.Constants.DEFAULT_SPLIT_CHAR);

        sb.append("123456"); // 交易账号密码
        sb.append(com.runssnail.monolith.socket.Constants.DEFAULT_SPLIT_CHAR);

        // sb.append("1"); // 0:存折1：银行卡，目前默认为0
        // sb.append(com.runssnail.monolith.socket.Constants.DEFAULT_SPLIT_CHAR);

        sb.append("2");// 证件类别
        sb.append(com.runssnail.monolith.socket.Constants.DEFAULT_SPLIT_CHAR);

        sb.append("238383833338299292289"); // 证件号码
        sb.append(com.runssnail.monolith.socket.Constants.DEFAULT_SPLIT_CHAR);

        sb.append("zhengwei");// 客户名称

        sb.append(com.runssnail.monolith.socket.Constants.DEFAULT_SPLIT_CHAR);
        // 性别
        sb.append(com.runssnail.monolith.socket.Constants.DEFAULT_SPLIT_CHAR);

        // // 推荐人编号
        // sb.append(com.runssnail.monolith.socket.Constants.DEFAULT_SPLIT_CHAR);
        //
        // // 电话号码
        // sb.append(com.runssnail.monolith.socket.Constants.DEFAULT_SPLIT_CHAR);
        //
        // // 手机号码
        // sb.append(com.runssnail.monolith.socket.Constants.DEFAULT_SPLIT_CHAR);
        //
        // // 传真号码
        // sb.append(com.runssnail.monolith.socket.Constants.DEFAULT_SPLIT_CHAR);
        //
        // // 电子邮件
        // sb.append(com.runssnail.monolith.socket.Constants.DEFAULT_SPLIT_CHAR);
        //
        // // 邮政编码
        // sb.append(com.runssnail.monolith.socket.Constants.DEFAULT_SPLIT_CHAR);
        //
        // // 通讯地址
        // sb.append(com.runssnail.monolith.socket.Constants.DEFAULT_SPLIT_CHAR);

        return sb.toString();
    }

    private static String build3500Header() {

        // 交易代码
        // 交易机构
        // 核算机构
        // 柜员号
        // 前台流水号
        // 交易日期
        // 交易时间
        // 文件名
        // 文件笔数

        StringBuilder sb = new StringBuilder(); // 版本号

        sb.append("3500");// .append(com.runssnail.monolith.socket.Constants.DEFAULT_SPLIT_CHAR); // 交易代码
        sb.append("000001");// .append(com.runssnail.monolith.socket.Constants.DEFAULT_SPLIT_CHAR); // 交易所编号
        sb.append("000301");// .append(com.runssnail.monolith.socket.Constants.DEFAULT_SPLIT_CHAR); // 核算机构

        sb.append("001001");// .append(com.runssnail.monolith.socket.Constants.DEFAULT_SPLIT_CHAR); // 柜员号

        sb.append("001001");// .append(com.runssnail.monolith.socket.Constants.DEFAULT_SPLIT_CHAR); // 前台流水号

        sb.append("20130822");// .append(com.runssnail.monolith.socket.Constants.DEFAULT_SPLIT_CHAR); // 交易日期

        sb.append("153022");// .append(com.runssnail.monolith.socket.Constants.DEFAULT_SPLIT_CHAR); // 交易时间

        sb.append("filename    ");// .append(com.runssnail.monolith.socket.Constants.DEFAULT_SPLIT_CHAR); // 文件名

        sb.append("00000001");// .append(com.runssnail.monolith.socket.Constants.DEFAULT_SPLIT_CHAR); // 记录条数

        return sb.toString();
    }

    /**
     * 交易所发起，银行转交易所(人金)
     * 
     * @return
     */
    private static String buildBankToExchange() {

        // 银行账号 C(30) 银行客户结算账号
        // 交易账号 C(30) 交易所的交易账号
        // 币种代码 C(4) CNY－人民币 HKD－港币 USD－美元
        // 转账金额 N(15) 单位精确到分
        // 交易账号密码 C(16) 交易账号密码密码明文传输

        StringBuilder sb = new StringBuilder();
        sb.append("4444444333").append(com.runssnail.monolith.socket.Constants.DEFAULT_SPLIT_CHAR);
        sb.append("00000101"); // 交易帐号
        sb.append(com.runssnail.monolith.socket.Constants.DEFAULT_SPLIT_CHAR);
        sb.append("CNY"); // 人民币
        sb.append(com.runssnail.monolith.socket.Constants.DEFAULT_SPLIT_CHAR);

        sb.append("10000").append(com.runssnail.monolith.socket.Constants.DEFAULT_SPLIT_CHAR); // 金额

        sb.append("123456"); // 交易账号密码

        return sb.toString();
    }

    private static String buildBody() {

        StringBuilder sb = new StringBuilder();
        sb.append("100000033333"); // 银行帐号
        sb.append(com.runssnail.monolith.socket.Constants.DEFAULT_SPLIT_CHAR);

        sb.append("00000101"); // 交易帐号
        sb.append(com.runssnail.monolith.socket.Constants.DEFAULT_SPLIT_CHAR);

        sb.append("CNY"); // 人民币
        sb.append(com.runssnail.monolith.socket.Constants.DEFAULT_SPLIT_CHAR);

        sb.append("123456"); // 交易账号密码
        sb.append(com.runssnail.monolith.socket.Constants.DEFAULT_SPLIT_CHAR);

        sb.append("1"); // 0:存折1：银行卡，目前默认为0
        sb.append(com.runssnail.monolith.socket.Constants.DEFAULT_SPLIT_CHAR);

        sb.append("2");// 证件类别
        sb.append(com.runssnail.monolith.socket.Constants.DEFAULT_SPLIT_CHAR);

        sb.append("238383833338299292289"); // 证件号码
        sb.append(com.runssnail.monolith.socket.Constants.DEFAULT_SPLIT_CHAR);

        sb.append("zhengwei");// 客户名称

        sb.append(com.runssnail.monolith.socket.Constants.DEFAULT_SPLIT_CHAR);
        // 性别
        sb.append(com.runssnail.monolith.socket.Constants.DEFAULT_SPLIT_CHAR);

        // 推荐人编号
        sb.append(com.runssnail.monolith.socket.Constants.DEFAULT_SPLIT_CHAR);

        // 电话号码
        sb.append(com.runssnail.monolith.socket.Constants.DEFAULT_SPLIT_CHAR);

        // 手机号码
        sb.append(com.runssnail.monolith.socket.Constants.DEFAULT_SPLIT_CHAR);

        // 传真号码
        sb.append(com.runssnail.monolith.socket.Constants.DEFAULT_SPLIT_CHAR);

        // 电子邮件
        sb.append(com.runssnail.monolith.socket.Constants.DEFAULT_SPLIT_CHAR);

        // 邮政编码
        sb.append(com.runssnail.monolith.socket.Constants.DEFAULT_SPLIT_CHAR);

        // 通讯地址
        sb.append(com.runssnail.monolith.socket.Constants.DEFAULT_SPLIT_CHAR);

        return sb.toString();
    }

    private static String buildHeader() {

        // 版本号 2 目前默认01
        // 交易代码 4 0001
        // 交易所编号 6
        // 银行代码 6
        // 请求方流水号 20 不足补空
        // 交易日期 8 YYYYMMDD
        // 交易时间 6 HH24MiSS
        // 记录条数 8 前补0

        StringBuilder sb = new StringBuilder(); // 版本号
        sb.append("01");// .append(com.runssnail.monolith.socket.Constants.DEFAULT_SPLIT_CHAR);
        sb.append("6200");// .append(com.runssnail.monolith.socket.Constants.DEFAULT_SPLIT_CHAR); // 交易代码
        sb.append("000001");// .append(com.runssnail.monolith.socket.Constants.DEFAULT_SPLIT_CHAR); // 交易所编号

        sb.append("001001");// .append(com.runssnail.monolith.socket.Constants.DEFAULT_SPLIT_CHAR); // 银行代码

        sb.append("00100193939393939443");// .append(com.runssnail.monolith.socket.Constants.DEFAULT_SPLIT_CHAR); // 请求方流水号

        sb.append("20130822");// .append(com.runssnail.monolith.socket.Constants.DEFAULT_SPLIT_CHAR); // 交易日期

        sb.append("153022");// .append(com.runssnail.monolith.socket.Constants.DEFAULT_SPLIT_CHAR); // 交易时间

        sb.append("00000001");// .append(com.runssnail.monolith.socket.Constants.DEFAULT_SPLIT_CHAR); // 记录条数

        return sb.toString();
    }

}
