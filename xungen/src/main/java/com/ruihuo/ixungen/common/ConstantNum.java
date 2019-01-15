package com.ruihuo.ixungen.common;

/**
 * @author yudonghui
 * @date 2017/4/22
 * @describe May the Buddha bless bug-free!!!
 */
public class ConstantNum {
    public final static String SERVICE = "10000";
    public final static String XGWZ = "11";//寻根问祖
    public final static String XSWH = "12";//姓氏文化
    public final static String MY_AGNATION = "13";//我的宗亲
    public final static String ZQSY = "14";//宗亲商业
    public final static String ZQHD = "15";//宗亲活动
    public final static String JGJX = "16";//家规家训
    public final static String ZQHZ = "17";//宗亲互助
    public final static String XSJP = "18";//姓氏家谱
    public final static int MYGENEALOGY = 508;//我的家谱
    public final static int MYQUESTION = 509;//我的提问
    public final static int ACITON_DETAIL = 510;//活动详情
    public final static int ARTICLE_DETAIL = 511;//新闻详情
    public final static int VIDEO_PLAY = 512;//视频播放
    public final static int ACTION_URL = 512;//活动界面
    //登录成功用到的action
    public final static String LOGIN_SUCCESS = "com.login.success";
    public final static int RESULT_2345=2345;

    public final static int REQUEST_CAMERA = 800;//调取相机的请求码
    public final static int REQUEST_PHOTO = 801;//调取系统相册的请求码
    public final static int PERMISSION_CAMERA = 802;//调取相机的请求权限码；
    public final static int REQUEST_DEAL_PHOTO = 803;//请求裁剪图片码；
    public final static int RESULT_DEAL_PHOTO = 804;//裁剪结果吗；
    public final static int REQUEST_DEVICEID = 805;//请求获取设备id的时候请求权限码
    public final static int REQUEST_1876 = 1876;
    public final static int REQUEST_1877 = 1877;
    public final static int REQUEST_1878 = 1878;
    public final static String PAYMENT_SUCEESS = "com.payment.success";//微信支付成功后发送的广播。aciton
    public final static String TREE_TITLE = "com.treeactivity.title";//TreeActivity的标题
    public final static String CONSUME_SUCEESS = "com.consume.success";//扫码完成支付完成之后。
    public final static String CONNECT_SUCEESS = "com.connect.success";//融云链接成功
    public final static String ADDFRIEND_SUCEESS = "com.addfriend.success";//添加好友成功
    public final static String ACTION_USERINFO = "com.activity.actionUserInfoActivity";//个人中心
    public final static String QUITE_SUCEESS = "com.quitdiscussion.success";//退出讨论组
    public final static String WX_APP_ID = "wx421375206ae23aad";
    public final static int HOTEL_ALL = 9;//酒店全部照片
    public final static int HOTEL_WG = 1;//酒店外观
    public final static int HOTEL_DT = 2;//酒店大堂
    public final static int HOTEL_KF = 3;//酒店客房
    public final static int HOTEL_GG = 4;//酒店公共设施
    public final static int FOOD_CP = 5;//餐厅菜品
    public final static int FOOD_HJ = 6;//餐厅环境
    public final static int FOOD_ALL = 10;//餐厅全部照片

    public final static int TOUR_ALL = 11;
    public final static int TOUR_FJ = 7;//旅游风景
    public final static int TOUR_ZB = 8;//旅游周边


    public final static int ORDER_ALLN = 0;//订单全部
    public final static int ORDER_WAIT_PAYN = 1;//待付款
    public final static int ORDER_WAIT_USEDN = 2;//待使用
    public final static int ORDER_WAIT_RECOMMENDN = 3;//待评价
    public final static int ORDER_QUITN = 4;//退款/售后

    public final static int ORDER_ALLS = 0;//
    public final static int ORDER_WAIT_PAYS = 1;//待付款
    public final static int ORDER_WAIT_QUITS = 2;//待退款
    public final static int ORDER_WAIT_REPLYS = 3;//待回复
    public final static int ORDER_WAIT_USEDS = 4;//待使用
    public final static int ORDER_FINISHEDS = 5;//已完成

    public final static String ORDER_TYPE = "1";//生成二维码名片  订单二维码
    public final static String FRIEND_TYPE = "2";//生成二维码名片  添加好友二维码
    public final static String AGNATION_TYPE = "3";//生成二维码名片  加入宗亲会二维码
    public final static String DISCUSSION_TYPE = "4";//生成二维码名片  讨论组二维码
    public final static String PUCLIC_KEY = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQDnfibe83c2BvwpD5xk8rANfdQN+xY0PG857C/Em6liqGYHHlVGGFfDcV0xcyGJy/2ahcbIDp7ueEuDyoeB+WI6vhhhK0EB5tyKN9esi/6RETeHFdd3bUopeo9qTxAvsdVb681QtFOrAeE/3Af4JS5AP0Ir5/MqNQOLuDtgphG1CwIDAQAB";
    public final static String PRIVATE_KEY = "MIICdgIBADANBgkqhkiG9w0BAQEFAASCAmAwggJcAgEAAoGBAOd+Jt7zdzYG/CkPnGTysA191A37FjQ8bznsL8SbqWKoZgceVUYYV8NxXTFzIYnL/ZqFxsgOnu54S4PKh4H5Yjq+GGErQQHm3Io316yL/pERN4cV13dtSil6j2pPEC+x1VvrzVC0U6sB4T/cB/glLkA/Qivn8yo1A4u4O2CmEbULAgMBAAECgYALIx2Zolr85W8iRpb+oFJqA8C8 /3R9BN9RCRTvP5Hxpipqc0IlAizOwVh7dY+Kgp7r3T3ICDQLQwxXBdppHWdlVq1CxbcrC8ub7D9ui3EOLwbHuBIYHg41D6WCqbf3vCohqrAgBR57ISU3RL855jbXRtoqdb2ocnEZuUkENnLIaQJBAPUBVBoH4C1n25jfWmQ/m6A6cb03pDgRcVMQX7gtXQULYWEhfvVn9ML/Ro/hGGWnGQ/5Y+xOErwcYLg9wwKh8+cCQQDx4ZcD6ZUaGlZ1IIcvKrtNBfQ7VDgzAMSreAifR2I2a88eS8rTGNWyjdMj1xcquSC21wFP/ozzImzQ1sPsbdE9AkEA3SOa1nf/VtxkMCKBQvTKsh+uY5xiRB0yTUf9LY78Y7424eXK4xQ2rv6coOcKD054Z5uxHiwF6vYuMn3Ek468RwJABDUcX2EMyutyXY83Ssa/g1N7MB0C7UAyK5lm7P5c4v11GN3QpWNlNDnrVlBDgua9fvC2gSG4afJLWkaiGda/MQJAaW/YOW6puUyW9TgCfiT7ljSOK0/yJwz4LPg8XEJ3UeGzalQw5Vl4vSah9jhn3j9G/ShfZpYLOGe/AulNFtqVnA==";
}
