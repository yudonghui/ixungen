package com.ruihuo.ixungen;

/**
 * @ author yudonghui
 * @ date 2017/3/28
 * @ describe May the Buddha bless bug-free！！
 */
public class Url {
    //更改决定是上线还是调试状态
    public static Env ENV = Env.DEBUG;

    public static enum Env {
        DEBUG,   //测试
        RELEASE  //正式
    }

    public static String getHttp() {
        switch (ENV) {
            case DEBUG:
                return "http://api.dev.ixungen.cn";
            case RELEASE:
                return "https://api.ixungen.cn";
            default:
                return "https://api.ixungen.cn";
        }

    }

    public static String getHttpRes() {
        switch (ENV) {
            case DEBUG:
                return "https://res.ixungen.cn";
            case RELEASE:
                return "https://res.ixungen.cn";
            default:
                return "https://res.ixungen.cn";
        }
    }

    public static String getH5Http() {
        switch (ENV) {
            case DEBUG:
                return "http://m.dev.ixungen.cn";
            case RELEASE:
                return "https://m.ixungen.cn";
            default:
                return "https://m.ixungen.cn";
        }
    }

    public static String getPayHttp() {
        switch (ENV) {
            case DEBUG:
                return "http://pay.dev.ixungen.cn";
            case RELEASE:
                return "https://pay.ixungen.cn";
            default:
                return "https://pay.ixungen.cn";
        }
    }

    //http 或者https   目前正式的是https  测试为http；
    public static String HTTP = getHttp();
    //h5根地址
    public static String H5_HTTP = getH5Http();
    //寻根资源中心
    public static String HTTPRES = getHttpRes();
    //支付接口
    public static String HTTPPAY = getPayHttp();
    //app下载链接
    public static String APP_URL = H5_HTTP + "/Glean/glean/jumpDownloadPage.html";
    //添加名人
    public static String ADD_STAR = H5_HTTP + "/surnameculture/surnameculture/sendCelebrity/cultureId/";
    //轮播图
    public final static String ROLL_URL = HTTP + "/v1/base/ad";
    //首页上下滚动图上面
    public final static String RED_GV_URL = HTTP + "/Homeactitityad/getHomeColumns";
    //登录接口（登录即注册）
    public final static String LOGIN_URL = HTTP + "/v1/user/login";
    //获取验证码
    public final static String SMS_CODE_URL = HTTP + "/v1/base/send_sms";
    //验证码校验
    public final static String CHECK_SMS_CODE_URL = HTTP + "/v1/user/checkSmsCode";
    //修改手机号
    public final static String CHANGE_PHONE_URL = HTTP + "/v1/user/resetPhone";
    //获取新闻列表
    public final static String NEWS_URL = HTTP + "/v1/cms/getlist";
    //获取新闻详情
    public final static String NEWS_DETAIL_URL = HTTP + "/v1/cms/getById";
    //获取城市列表
    public final static String CITY_URL = HTTP + "/v1/base/region";
    //个人信息查询
    public final static String USER_INFO_URL = HTTP + "/v1/user/setInfo";
    //个人信息修改
    public final static String USER_INFO_CHANG_URL = HTTP + "/v1/user/bind";
    //身份证信息修改
    public final static String IDCARD_CHANG_URL = HTTP + "/v1/user/authentication";
    //图片的根地址
    public final static String IMAGE_ROOT_URL = "";
    //密码修改
    public final static String RESETPASSWORD_URL = HTTP + "/v1/user/resetPassword";
    //设置密码
    public final static String SETPASSWORD_URL = HTTP + "/v1/user/setPassword";
    //查询用户信息
    public final static String GETUSERINFO_URL = HTTP + "/v1/user/getInfo";
    //头像上传
    public final static String CHANGE_HEADER_URL = HTTP + "/user/upload";
    //宗亲会头像上传
    public final static String AGNATION_HEADER_URL = HTTP + "/clanassociation/upload";
    //宗亲会图片上传
    public final static String PHOTO_UPLOAD_URL = HTTP + "/clanalbum/upload";
    //宗亲会列表
    public final static String AGNATION_FORM_URL = HTTP + "/clanassociation/getlist";
    //根据rid获取宗亲会列表
    public final static String AGNATION_FORM_BYID_URL = HTTP + "/clanassociation/getByRid";
    //加入宗亲会
    public final static String JOIN_AGNATION_URL = HTTP + "/userassociation/add";
    //退出宗亲会, 会长删除宗亲会成员
    public final static String QUIT_AGNATION_URL = HTTP + "/userassociation/delete";
    //修改宗亲会
    public final static String CHANG_AGNATION_INFO_URL = HTTP + "/clanassociation/update";
    //创建宗亲会
    public final static String CREATE_AGNATION_URL = HTTP + "/clanassociation/add";
    //宗亲会成员列表
    public final static String AGNATION_MEMEBER_URL = HTTP + "/userassociation/getlist";
    //获取宗亲会公告
    public final static String AGNATION_AD_URL = HTTP + "/cmsannouncement/getlist";
    //发布宗亲会公告
    public final static String AGNATION_AD_ADD_URL = HTTP + "/cmsannouncement/add";
    //宗亲会相册列表
    public final static String AGNATION_PHOTO_FORM_URL = HTTP + "/clanalbum/getlist";
    //相册详细信息
    public final static String AGNATION_PHOTO_DETAIL_URL = HTTP + "/clanalbum/getAlbumImages";
    //创建相册
    public final static String ADD_AGNATION_PHOTO_URL = HTTP + "/clanalbum/add";
    //删除相册
    public final static String DELETE_AGNATION_PHOTO_URL = HTTP + "/clanalbum/delete";
    //编辑相册
    public final static String EDITOR_AGNATION_PHOTO_URL = HTTP + "/clanalbum/update";
    //删除图片
    public final static String DELETE_AGNATION_PHOTO_DETAIL_URL = HTTP + "/clanalbum/deleteImage";
    //获取好友列表
    public final static String OBTAIN_FRIEND_FORM_URL = HTTP + "/userfriend/getlist";
    //删除好友
    public final static String DELETE_FRIEND_URL = HTTP + "/userfriend/delete";
    //查询所有人
    public final static String FIND_ALL_FRIEND_URL = HTTP + "/v1/user/getUser";
    //判断是否是好友关系
    public final static String ISFRIEND_URL = HTTP + "/userfriend/isFriend";
    //宗亲技能（能工巧匠）
    public final static String CLANSKILL_URL = HTTP + "/skill/getlist";
    //获取消息列表（帖子消息，活动消息）
    public final static String USERMSG_URL = HTTP + "/usermsg/getlist";
    //获取系统消息详情
    public final static String USERMSG_DETAIL_URL = HTTP + "/usermsg/getDetail";
    //帖子消息
    public final static String POSTS_SMS_URL = HTTP + "/cmsreply/getlist";
    //活动消息列表
    public final static String ACTION_SMS_URL = HTTP + "/useractivity/getlist";
    //活动消息详情
    public final static String ACTION_SMS_DETAIL_URL = HTTP + "/cmsactivity/getById";
    //活动参与者列表
    public final static String ACTION_JOINER_URL = HTTP + "/useractivity/getUserInfos";
    //退出活动
    public final static String ACTION_QUIT_URL = HTTP + "/useractivity/update";
    //回复帖子
    public final static String POSTS_REPLY_URL = HTTP + "/cmsreply/add";
    //获取系统消息列表
    public final static String SYSTEM_SMS_URL = HTTP + "/rymsg/getlist";
    //系统消息详情
    public final static String SYSTEM_SMS_DETAIL_URL = HTTP + "/usermsg/getDetail";
    //添加好友申请
    public final static String ADD_FRIEND_URL = HTTP + "/userfriend/add";
    //获取新的朋友列表
    public final static String NEW_FRIEND_FORM_URL = HTTP + "/userfriend/getNewFriends";
    //处理添加好友申请
    public final static String DEAL_FRIEND_URL = HTTP + "/userfriend/deal";
    //保存讨论组到自己服务器
    public final static String SAVE_DISCUSSION = HTTP + "/userassociation/joinDiscussionGroup";
    //从自己服务器移除讨论组
    public final static String REMOVE_DISCUSSION = HTTP + "/userassociation/deleteDiscussionGroup";
    //获取本服务器上讨论组的列表
    public final static String DISCUSSION_FORM = HTTP + "/userassociation/getDiscussionGroups";
    //判断讨论组是否已经在本服务器上保存
    public final static String DISCUSSION_EXIST = HTTP + "/userassociation/isExistDiscussionGroup";
    //姓氏id转换成汉字
    public final static String GET_BYNAME = HTTP + "/surname/getByName";
    //刷新token
    public final static String REFRESH_TOKEN = HTTP + "/user/refreshToken";
    //在未登录的情况下根据设备号获取融云token
    public final static String YUNTOKEN_URL = HTTP + "/user/getYunToken";
    //图片连接
    public final static String PHOTO_URL = HTTPRES + "/img/";
    //h5寻根问祖
    public final static String H5_XGWZ_URL = H5_HTTP + "/findroots/topic/findRootsTopicHomepage/rid/";
    //h5姓氏百科
    public final static String H5_XSWH_URL = H5_HTTP + "/surnameculture/surnameculture/familyculture/cultureId/";
    //h5姓氏百科（未登陆状态下）
    public final static String H5_XSWH_FROM_URL = H5_HTTP + "/surnameculture/surnameculture/searchPage.html";
    //帖子消息需要跳转的详情H5界面
    public final static String H5_DETAIL = H5_HTTP + "/findroots/questions/question/";
    //H5家规家训
    public final static String H5_JGJX_URL = H5_HTTP + "/findroots/Familyrules/index/id/";
    //H5宗亲互助  id 宗亲会id   type  标识 0全部,1最新,2最热,3精华
    public final static String H5_ZZHZ_URL = H5_HTTP + "/findroots/Association/index/id/";
    //H5宗亲商业
    public final static String H5_ZZSY_URL = "";
    //H5宗亲活动  type  标识 0全部,1最新,2最热,3精华
    public final static String H5_ZZHD_URL = H5_HTTP + "/clanacticity/clanactivity/index/type/";
    //H5我的家谱  createId：当前登录的用户Id（创建者）
    public final static String H5_MYGLEAN_URL = H5_HTTP + "/glean/glean/myGlean/createId/";
    //H5我的提问  userid 用户的根号
    public final static String H5_MYQUESTION_URL = H5_HTTP + "/findroots/Myquestion/index/userid/";
    //H5新闻详情  id 新闻编号id
    public final static String H5_ARTICLE_DETAIL_URL = H5_HTTP + "/findroots/questions/news/id/";
    //H5姓氏家谱
    public final static String H5_SURNAME_TREE_URL = H5_HTTP + "/Glean/glean/searchGleanByName";
    //h5清理数据
    public final static String H5_clan = H5_HTTP + "/api/base/cleanSession";
    //h5视频播放
    public final static String H5_VIDEO = H5_HTTP + "/index/video/index/id/";
    //h5通过id查看家谱
    public final static String H5_CLAN_GLEAN = H5_HTTP + "/Glean/glean/findOneMyGlean/id";
    //检查版本更新
    public final static String VERSION_URL = HTTP + "/appversion/getVersion";
    //获取首页活动的内容
    public final static String ACTION_URL = HTTP + "/Activity/getlist";
    //活动 个人空间
    public final static String INTRODUCE_URL = HTTP + "/user/getUserDetaileInfo";
    //活动 个人相册列表
    public final static String ACTION_PHOTO_FORM_URL = HTTP + "/homealbum/getlist";
    //活动 添加个人相册
    public final static String ACTION_ADD_PHOTO = HTTP + "/homealbum/add";
    //活动 删除相册
    public final static String ACTION_DELETE_PHOTO = HTTP + "/homealbum/delete";
    //查看单个相册信息
    public final static String ACTION_PHOTO_DETAIL = HTTP + "/homealbum/getById";
    //查看个人相册
    public final static String ACTION_PHOTO_DETAIL_FORM = HTTP + "/homealbum/getAlbumImages";
    //批量删除个人相片
    public final static String ACTION_DELETE_PHOTO_DETAIL = HTTP + "/homealbum/deleteImages";
    //添加相片
    public final static String ACTION_ADD_PHOTO_DETAIL = HTTP + "/homealbum/upload";
    //编辑个人中心相册
    public final static String ACTION_EDIT_PHOTO_INFO = HTTP + "/homealbum/update";
    //个人视频列表
    public final static String ACTION_VIDEO_FORM = HTTP + "/homevideo/getlist";
    //上传个人视频
    public final static String UPLOAD_VIDEO = HTTP + "/homevideo/upload";
    //批量删除个人视频
    public final static String DELETE_VIDEO = HTTP + "/homevideo/delete";
    //查看视频详情
    public final static String VIDEO_DETAIL = HTTP + "/homevideo/getDatail";
    //视频根地址
    public final static String ROOT_VIDEO = HTTP + "/uploads/video/";
    //评论视频
    public final static String COMMENT_VIDEO = HTTP + "/homevideocomment/add";
    //回复评论视频
    public final static String REPLAY_COMMENT_VIDEO = HTTP + "/homevideoreply/add";
    //点赞视频评论
    public final static String PRAISE_COMMENT = HTTP + "/homevideocomment/like";
    //点赞视频
    public final static String PRAISE_VIDEO = HTTP + "/homevideocomment/videoLike";
    //投票
    public final static String VOTE_URL = HTTP + "/vote/add";
    //图片上传
    public final static String PHOTO_UPLOAD = "https://res.ixungen.cn/upload";
    //发布能工巧匠
    public final static String SEND_SKILL = HTTP + "/skill/add";
    //获取景区列表
    public final static String ENVIRON_FORM = HTTP + "/tour/getTourAreas";
    //商家入驻接口
    public final static String MERCHANT_ENTER = HTTP + "/tour/addShop";
    //根据token获取商铺详情的接口
    public final static String SHOPS_DETAIL = HTTP + "/tour/getShopByToken";
    //麻城影视
    public final static String MOVIES = HTTP + "/movie/getHomepage";
    //影视演员库
    public final static String ACTORS = HTTP + "/movie/getPerformers";
    //演员报名
    public final static String ACTOR_APPLY = HTTP + "/movie/join";
    //视频列表
    public final static String MOVIE_FORM = HTTP + "/activityvideo/getlist";
    //查看报名的状态
    public final static String ACTOR_APPLY_STATE = HTTP + "/movie/isJoin";
    //获取报名规则
    public final static String ACTOR_APPLY_RULE = HTTP + "/movie/getRule";
    //微信支付
    public final static String RECHARGE_WX_URL = HTTPPAY + "/weixin/weixin/placeOrder";
    //支付宝支付
    public final static String RECHARGE_ALIPAY_URL = HTTPPAY + "/alipay/ali/placeOrder";
    //发帖
    public final static String SEND_TIEZI = HTTP + "/v1/cms/add";
    //帖子详情
    //public final static String TIEZI_DETAIL = HTTP + "/v1/cms/getById";
    //帖子详情评论
    //public final static String TIEZI_DETAIL="http://phpapi.ixungen.cn/cms/getDetail";
    public final static String TIEZI_DETAIL = HTTP + "/cms/getDetail";
    //帖子评论
    public final static String SMS_COMMENT = HTTP + "/cmsreply/comment";
    //帖子回复
    public final static String SMS_REPLY = HTTP + "/cmsreply/add";
    //帖子点赞
    public final static String SMS_PRAISE = HTTP + "/cmsreply/update";
    //上传图片到商店相册
    public final static String SHOP_ADDIMAGE = HTTP + "/shop/addImage";
    //查看相册列表
    public final static String SHOP_PHOTO_FORM = HTTP + "/shop/getImages";
    //店铺详情
    public final static String SHOP_INFO = HTTP + "/shop/getShopInfoByToken";
    //删除商店相册中的图片
    public final static String DELETE_SHOP_PHOTO = HTTP + "/shop/deleteImages";
    //商家添加商品
    public final static String SHOP_ADD_SHOPS = HTTP + "/shop/addGoods";
    //商品列表
    public final static String SHOPS_FORM = HTTP + "/shop/getGoodsList";
    //删除商品
    public final static String DELETE_SHOPS = HTTP + "/shop/deleteGoods";
    //商家修改商品信息
    public final static String UPDATE_GOODS = HTTP + "/shop/updateGoods";
    //修改商铺信息
    public final static String SHOPS_INFO_UPDATE = HTTP + "/tour/updateByToken";
    //店铺首页
    public final static String SHOPS_HOME = HTTP + "/shop/getHomePage";
    //创建订单
    public final static String SHOP_PLACE_ORDER = HTTP + "/shop/placeOrder";
    //获取商铺评论列表
    public final static String SHOP_COMMENT_FORM = HTTP + "/shop/getComment";
    //附近商铺
    public final static String SHOP_NEARBY_SHOP = HTTP + "/shop/getShopList";
    //查看订单是否支付成功
    public final static String ORDER_IS_SUCCESS = HTTP + "/shop/isPay";
    //查看订单列表
    public final static String ORDER_FORM = HTTP + "/shop/getOrderList";
    //查看订单详情
    public final static String ORDER_FORM_DETAIL = HTTP + "/shop/getOrderDetail";
    //商铺评论
    public final static String SHOP_COMMENT = HTTP + "/shop/comment";
    //商铺回复评论
    public final static String SHOP_REPLY = HTTP + "/shop/reply";
    //删除订单
    public final static String DELETE_ORDER = HTTP + "/shop/deleteOrder";
    //处理退款
    public final static String DEAL_REFUND = HTTP + "/shop/dealRefund";
    //拒单
    public final static String REFUSE_ORDER = HTTP + "/shop/refuseOrder";
    //取消订单,申请退款，
    public final static String CANCEL_ORDER = HTTP + "/shop/cancelOrder";
    //修改价格
    public final static String UPDATE_ORDER_PRICE = HTTP + "/shop/updateOrderPrice";
    //取消退款申请
    public final static String CANCEL_REFUND = HTTP + "/shop/cancelRefund";
    //已使用订单
    public final static String CONSUME_ORDER = HTTP + "/shop/consumeOrder";
    //麻城旅游首页
    public final static String TRAVEL_HOME = HTTP + "/tour/getHomePage";
    //二维码数据加密
    public final static String TWO_CODE_JIA = HTTP + "/shop/getEncryptCode";
    //二维码数据解密
    public final static String TWO_CODE_JIE = HTTP + "/shop/getDecryptCode";
    //宗是否加入该宗亲会
    public final static String AGNATION_ISJOIN = HTTP + "/clanassociation/isJoin";
    //会长处理入会的申请
    public final static String AGNATION_UPDATE = HTTP + "/userassociation/update";
    //邀请加入宗亲会
    public final static String AGNATION_APPLY = HTTP + "/userassociation/inviteJoin";
    //拒绝加入宗亲会
    public final static String REFUSE_JOIN = HTTP + "/userassociation/refuseJoin";
    //处理家谱邀请
    public final static String CLAN_GLEAN_DEAL = HTTP + "/clanstemma/dealJoin";
    //根据关键字搜索家谱信息或家谱树成员
    public final static String SEARCH_CLAN_GLEAN = HTTP + "/glean/searchByKeyword";
    //姓氏家谱
    public final static String SURNAME_CLAN = HTTP + "/glean/recommend";
    //家谱树
    public final static String TREE_URL = HTTP + "/glean/";
    //展开家谱
    public final static String EXPAND_URL = HTTP + "/glean/";
    //添加家谱树成员
    public final static String TREE_ADD = HTTP + "/glean/add";
    public final static String TREE_UPDATE = HTTP + "/glean/update";
    //删除家谱树成员
    public final static String TREE_DELETE = HTTP + "/glean/deleteById";
    //家谱列表查询
    public final static String GLEAN_FORM = HTTP + "/glean/getlist";
    //获取已经购买的家谱的列表
    public final static String BUY_STEMMA_FORM = HTTP + "/Clanstemma/getMyStemma";
    //创建家谱
    public final static String TREE_CREATE = HTTP + "/glean/create";
    //根据id查看后台录入家谱详情
    public final static String STEMMA_SYSTEM_DETAIL = HTTP + "/Clanstemma/getById";
    //根据id查看前段录入家谱详情
    public final static String STEMMA_APP_DETAIL = HTTP + "/glean/getGleanStemma";
    //是否已经购买家谱
    public final static String ISACCESS = HTTP + "/glean/isAccess";
    //根据id查询成员信息（家谱）
    public final static String STEMMA_MEMEBER_APP_INFO = HTTP + "/glean/getById";
    //根据id查询成员信息(族谱)
    public final static String STEMMA_MEMEBER_SYSTME_INFO = HTTP + "/glean/getByTreeId";
    //邀请朋友加入家谱,通过手机号
    public final static String INVITE_BY_PHONE = HTTP + "/clanstemma/inviteJoin";
    //所有族谱
    public final static String CLANSTEMMA_FORM = HTTP + "/Clanstemma/getlist";
    //获取家谱套餐价格
    public final static String STEMMA_PRICE_LIST = HTTP + "/clanstemma/getpricelist";
    //分页获取族谱目录
    public final static String STEMMA_CATALOGUE = HTTP + "/glean/getCatalog";
    //分页获取家谱目录
    public final static String STEMMA_CATALOGUETWO = HTTP + "/glean/getCatalogTwo";
    //编辑自创的家谱
    public final static String EDITOR_STEMMA = HTTP + "/glean/updateGleanStemma";
    //删除家谱
    public final static String DELETE_STEMMA = HTTP + "/glean/deleteGleanStemma";
    //根據id查詢成員配偶信息
    public final static String SPOUSE_FORM = HTTP + "/glean/getSpouses";
    //获取活动首页轮播图
    public final static String ACTION_SCROLL = HTTP + "/homeactitityad/getlist";
    //首页我的家谱
    public final static String HOME_STEMMA = HTTP + "/glean/getHomePageStemma";
    //朋友动态
    public final static String FRIEND_STATUS = HTTP + "/clan/getlist";
    //宗亲圈发布动态，或者评论
    public final static String ADD_COMMENT = HTTP + "/clan/add";
    //获取评论
    public final static String COMMENT_DETAIL = HTTP + "/clan/getComments";
    //帖子点赞
    public final static String PRAISE = HTTP + "/clan/like";
    //帖子回复
    public final static String REPLY_URL = HTTP + "/clan/reply";
    //帖子删除
    public final static String DELETE_POSTS = HTTP + "/clan/deleteById";
    //修改帖子的权限
    public final static String UPDATE_PRIVATE = HTTP + "/clan/updatePrivate";
    //推荐界面
    public final static String RECOMMEND = HTTP + "/clan/recommend";
    //发现界面的相关资讯
    public final static String DISCOVER = HTTP + "/cms/getDiscoverNews";
}
