package com.ruihuo.ixungen.entity;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * @author yudonghui
 * @date 2017/5/12
 * @describe May the Buddha bless bug-free!!!
 */
public class TestBean {

    /**
     * code : 0
     * msg : 成功
     * totalPage : 2
     * searchType : 2
     * data : {"from_app":[{"id":"1442","name":"于冬辉","avatar_url":null,"sex":"1","birth":"","former_name":null,"death":null,"generation":null,"region_name":null,"family_id":"11"}],"from_system":{"9":{"id":"217380","name":"李之于","address_suffix":"","former_name":"德成","generation":"","stemma_id":"94","stemma_name":"李氏家谱","surname":"李","stemma_generation":""},"12":{"id":"78627","name":"杨于庭","address_suffix":"","former_name":"念训","generation":"","stemma_id":"92","stemma_name":"杨氏族谱","surname":"杨","stemma_generation":"伯仲福添     知亲从功   作邦之辅     耀国光宗     俊美江右    普庆元豊      时临有道    际会亨通    庭祥焕彩\r\n\r\n春本水共     盛德大业    禄在其中    敬昭诒训    英华汉东    义厚宽裕     弈祉才聪     泽显朝佐    万代昌隆\r\n\r\n民嘉世汝    士志恢宏     敦伦敏学    守礼和雍    永承先绪    仁慈广容     修为定达    聘举齐逢\r\n\r\n    前代宗派四十字后继取新派六十四子凡属吾宗者照派取名载谱世系自不紊乱"},"14":{"id":"67233","name":"梅于城","address_suffix":"","former_name":"字公武","generation":"","stemma_id":"93","stemma_name":"梅氏家谱","surname":"梅","stemma_generation":"锦 治 本 光 基 鑑 济 枝  辉 室 拒 泽 业 耀 至 铨 法 集 烈 执 锡 泰 棣 炎 增 铭 洪 楚 坤 锺 渊 森  烺 坦 鎭 溥 楷  炘 均 铣 洁 柏 炜 壮 钰 汉 桓 耿 坪 钊 洛 桂 灼 壬 银 涛 林 焰 声"}}}
     * serverTime : 2017-11-28 11:57:23
     */

    private int code;
    private String msg;
    private int totalPage;
    private int searchType;
    private DataBean data;
    private String serverTime;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public int getTotalPage() {
        return totalPage;
    }

    public void setTotalPage(int totalPage) {
        this.totalPage = totalPage;
    }

    public int getSearchType() {
        return searchType;
    }

    public void setSearchType(int searchType) {
        this.searchType = searchType;
    }

    public DataBean getData() {
        return data;
    }

    public void setData(DataBean data) {
        this.data = data;
    }

    public String getServerTime() {
        return serverTime;
    }

    public void setServerTime(String serverTime) {
        this.serverTime = serverTime;
    }

    public static class DataBean {
        /**
         * from_app : [{"id":"1442","name":"于冬辉","avatar_url":null,"sex":"1","birth":"","former_name":null,"death":null,"generation":null,"region_name":null,"family_id":"11"}]
         * from_system : {"9":{"id":"217380","name":"李之于","address_suffix":"","former_name":"德成","generation":"","stemma_id":"94","stemma_name":"李氏家谱","surname":"李","stemma_generation":""},"12":{"id":"78627","name":"杨于庭","address_suffix":"","former_name":"念训","generation":"","stemma_id":"92","stemma_name":"杨氏族谱","surname":"杨","stemma_generation":"伯仲福添     知亲从功   作邦之辅     耀国光宗     俊美江右    普庆元豊      时临有道    际会亨通    庭祥焕彩\r\n\r\n春本水共     盛德大业    禄在其中    敬昭诒训    英华汉东    义厚宽裕     弈祉才聪     泽显朝佐    万代昌隆\r\n\r\n民嘉世汝    士志恢宏     敦伦敏学    守礼和雍    永承先绪    仁慈广容     修为定达    聘举齐逢\r\n\r\n    前代宗派四十字后继取新派六十四子凡属吾宗者照派取名载谱世系自不紊乱"},"14":{"id":"67233","name":"梅于城","address_suffix":"","former_name":"字公武","generation":"","stemma_id":"93","stemma_name":"梅氏家谱","surname":"梅","stemma_generation":"锦 治 本 光 基 鑑 济 枝  辉 室 拒 泽 业 耀 至 铨 法 集 烈 执 锡 泰 棣 炎 增 铭 洪 楚 坤 锺 渊 森  烺 坦 鎭 溥 楷  炘 均 铣 洁 柏 炜 壮 钰 汉 桓 耿 坪 钊 洛 桂 灼 壬 银 涛 林 焰 声"}}
         */

        private FromSystemBean from_system;
        private List<FromAppBean> from_app;

        public FromSystemBean getFrom_system() {
            return from_system;
        }

        public void setFrom_system(FromSystemBean from_system) {
            this.from_system = from_system;
        }

        public List<FromAppBean> getFrom_app() {
            return from_app;
        }

        public void setFrom_app(List<FromAppBean> from_app) {
            this.from_app = from_app;
        }

        public static class FromSystemBean {
            /**
             * 9 : {"id":"217380","name":"李之于","address_suffix":"","former_name":"德成","generation":"","stemma_id":"94","stemma_name":"李氏家谱","surname":"李","stemma_generation":""}
             * 12 : {"id":"78627","name":"杨于庭","address_suffix":"","former_name":"念训","generation":"","stemma_id":"92","stemma_name":"杨氏族谱","surname":"杨","stemma_generation":"伯仲福添     知亲从功   作邦之辅     耀国光宗     俊美江右    普庆元豊      时临有道    际会亨通    庭祥焕彩\r\n\r\n春本水共     盛德大业    禄在其中    敬昭诒训    英华汉东    义厚宽裕     弈祉才聪     泽显朝佐    万代昌隆\r\n\r\n民嘉世汝    士志恢宏     敦伦敏学    守礼和雍    永承先绪    仁慈广容     修为定达    聘举齐逢\r\n\r\n    前代宗派四十字后继取新派六十四子凡属吾宗者照派取名载谱世系自不紊乱"}
             * 14 : {"id":"67233","name":"梅于城","address_suffix":"","former_name":"字公武","generation":"","stemma_id":"93","stemma_name":"梅氏家谱","surname":"梅","stemma_generation":"锦 治 本 光 基 鑑 济 枝  辉 室 拒 泽 业 耀 至 铨 法 集 烈 执 锡 泰 棣 炎 增 铭 洪 楚 坤 锺 渊 森  烺 坦 鎭 溥 楷  炘 均 铣 洁 柏 炜 壮 钰 汉 桓 耿 坪 钊 洛 桂 灼 壬 银 涛 林 焰 声"}
             */

            @SerializedName("9")
            private _$9Bean _$9;
            @SerializedName("12")
            private _$12Bean _$12;
            @SerializedName("14")
            private _$14Bean _$14;

            public _$9Bean get_$9() {
                return _$9;
            }

            public void set_$9(_$9Bean _$9) {
                this._$9 = _$9;
            }

            public _$12Bean get_$12() {
                return _$12;
            }

            public void set_$12(_$12Bean _$12) {
                this._$12 = _$12;
            }

            public _$14Bean get_$14() {
                return _$14;
            }

            public void set_$14(_$14Bean _$14) {
                this._$14 = _$14;
            }

            public static class _$9Bean {
                /**
                 * id : 217380
                 * name : 李之于
                 * address_suffix :
                 * former_name : 德成
                 * generation :
                 * stemma_id : 94
                 * stemma_name : 李氏家谱
                 * surname : 李
                 * stemma_generation :
                 */

                private String id;
                private String name;
                private String address_suffix;
                private String former_name;
                private String generation;
                private String stemma_id;
                private String stemma_name;
                private String surname;
                private String stemma_generation;

                public String getId() {
                    return id;
                }

                public void setId(String id) {
                    this.id = id;
                }

                public String getName() {
                    return name;
                }

                public void setName(String name) {
                    this.name = name;
                }

                public String getAddress_suffix() {
                    return address_suffix;
                }

                public void setAddress_suffix(String address_suffix) {
                    this.address_suffix = address_suffix;
                }

                public String getFormer_name() {
                    return former_name;
                }

                public void setFormer_name(String former_name) {
                    this.former_name = former_name;
                }

                public String getGeneration() {
                    return generation;
                }

                public void setGeneration(String generation) {
                    this.generation = generation;
                }

                public String getStemma_id() {
                    return stemma_id;
                }

                public void setStemma_id(String stemma_id) {
                    this.stemma_id = stemma_id;
                }

                public String getStemma_name() {
                    return stemma_name;
                }

                public void setStemma_name(String stemma_name) {
                    this.stemma_name = stemma_name;
                }

                public String getSurname() {
                    return surname;
                }

                public void setSurname(String surname) {
                    this.surname = surname;
                }

                public String getStemma_generation() {
                    return stemma_generation;
                }

                public void setStemma_generation(String stemma_generation) {
                    this.stemma_generation = stemma_generation;
                }
            }

            public static class _$12Bean {
                /**
                 * id : 78627
                 * name : 杨于庭
                 * address_suffix :
                 * former_name : 念训
                 * generation :
                 * stemma_id : 92
                 * stemma_name : 杨氏族谱
                 * surname : 杨
                 * stemma_generation : 伯仲福添     知亲从功   作邦之辅     耀国光宗     俊美江右    普庆元豊      时临有道    际会亨通    庭祥焕彩

                 春本水共     盛德大业    禄在其中    敬昭诒训    英华汉东    义厚宽裕     弈祉才聪     泽显朝佐    万代昌隆

                 民嘉世汝    士志恢宏     敦伦敏学    守礼和雍    永承先绪    仁慈广容     修为定达    聘举齐逢

                 前代宗派四十字后继取新派六十四子凡属吾宗者照派取名载谱世系自不紊乱
                 */

                private String id;
                private String name;
                private String address_suffix;
                private String former_name;
                private String generation;
                private String stemma_id;
                private String stemma_name;
                private String surname;
                private String stemma_generation;

                public String getId() {
                    return id;
                }

                public void setId(String id) {
                    this.id = id;
                }

                public String getName() {
                    return name;
                }

                public void setName(String name) {
                    this.name = name;
                }

                public String getAddress_suffix() {
                    return address_suffix;
                }

                public void setAddress_suffix(String address_suffix) {
                    this.address_suffix = address_suffix;
                }

                public String getFormer_name() {
                    return former_name;
                }

                public void setFormer_name(String former_name) {
                    this.former_name = former_name;
                }

                public String getGeneration() {
                    return generation;
                }

                public void setGeneration(String generation) {
                    this.generation = generation;
                }

                public String getStemma_id() {
                    return stemma_id;
                }

                public void setStemma_id(String stemma_id) {
                    this.stemma_id = stemma_id;
                }

                public String getStemma_name() {
                    return stemma_name;
                }

                public void setStemma_name(String stemma_name) {
                    this.stemma_name = stemma_name;
                }

                public String getSurname() {
                    return surname;
                }

                public void setSurname(String surname) {
                    this.surname = surname;
                }

                public String getStemma_generation() {
                    return stemma_generation;
                }

                public void setStemma_generation(String stemma_generation) {
                    this.stemma_generation = stemma_generation;
                }
            }

            public static class _$14Bean {
                /**
                 * id : 67233
                 * name : 梅于城
                 * address_suffix :
                 * former_name : 字公武
                 * generation :
                 * stemma_id : 93
                 * stemma_name : 梅氏家谱
                 * surname : 梅
                 * stemma_generation : 锦 治 本 光 基 鑑 济 枝  辉 室 拒 泽 业 耀 至 铨 法 集 烈 执 锡 泰 棣 炎 增 铭 洪 楚 坤 锺 渊 森  烺 坦 鎭 溥 楷  炘 均 铣 洁 柏 炜 壮 钰 汉 桓 耿 坪 钊 洛 桂 灼 壬 银 涛 林 焰 声
                 */

                private String id;
                private String name;
                private String address_suffix;
                private String former_name;
                private String generation;
                private String stemma_id;
                private String stemma_name;
                private String surname;
                private String stemma_generation;

                public String getId() {
                    return id;
                }

                public void setId(String id) {
                    this.id = id;
                }

                public String getName() {
                    return name;
                }

                public void setName(String name) {
                    this.name = name;
                }

                public String getAddress_suffix() {
                    return address_suffix;
                }

                public void setAddress_suffix(String address_suffix) {
                    this.address_suffix = address_suffix;
                }

                public String getFormer_name() {
                    return former_name;
                }

                public void setFormer_name(String former_name) {
                    this.former_name = former_name;
                }

                public String getGeneration() {
                    return generation;
                }

                public void setGeneration(String generation) {
                    this.generation = generation;
                }

                public String getStemma_id() {
                    return stemma_id;
                }

                public void setStemma_id(String stemma_id) {
                    this.stemma_id = stemma_id;
                }

                public String getStemma_name() {
                    return stemma_name;
                }

                public void setStemma_name(String stemma_name) {
                    this.stemma_name = stemma_name;
                }

                public String getSurname() {
                    return surname;
                }

                public void setSurname(String surname) {
                    this.surname = surname;
                }

                public String getStemma_generation() {
                    return stemma_generation;
                }

                public void setStemma_generation(String stemma_generation) {
                    this.stemma_generation = stemma_generation;
                }
            }
        }

        public static class FromAppBean {
            /**
             * id : 1442
             * name : 于冬辉
             * avatar_url : null
             * sex : 1
             * birth :
             * former_name : null
             * death : null
             * generation : null
             * region_name : null
             * family_id : 11
             */

            private String id;
            private String name;
            private Object avatar_url;
            private String sex;
            private String birth;
            private Object former_name;
            private Object death;
            private Object generation;
            private Object region_name;
            private String family_id;

            public String getId() {
                return id;
            }

            public void setId(String id) {
                this.id = id;
            }

            public String getName() {
                return name;
            }

            public void setName(String name) {
                this.name = name;
            }

            public Object getAvatar_url() {
                return avatar_url;
            }

            public void setAvatar_url(Object avatar_url) {
                this.avatar_url = avatar_url;
            }

            public String getSex() {
                return sex;
            }

            public void setSex(String sex) {
                this.sex = sex;
            }

            public String getBirth() {
                return birth;
            }

            public void setBirth(String birth) {
                this.birth = birth;
            }

            public Object getFormer_name() {
                return former_name;
            }

            public void setFormer_name(Object former_name) {
                this.former_name = former_name;
            }

            public Object getDeath() {
                return death;
            }

            public void setDeath(Object death) {
                this.death = death;
            }

            public Object getGeneration() {
                return generation;
            }

            public void setGeneration(Object generation) {
                this.generation = generation;
            }

            public Object getRegion_name() {
                return region_name;
            }

            public void setRegion_name(Object region_name) {
                this.region_name = region_name;
            }

            public String getFamily_id() {
                return family_id;
            }

            public void setFamily_id(String family_id) {
                this.family_id = family_id;
            }
        }
    }
}
