package com.timecat.middle.block.im;

/**
 * @author 林学渊
 * @email linxy59@mail2.sysu.edu.cn
 * @date 2020-02-03
 * @description null
 * @usage null
 * 定义 IM 内部联系人实体类，用来获取头像昵称等简单属性进行展示
 */
public class IMContact {
    // 账户 id
    public String mId;
    // 账户用户名
    public String mUsername;
    // 账户昵称
    public String mNickname;
    // 账户头像
    public String mAvatar;

    public IMContact(String id) {
        mId = id;
    }

    public IMContact(String mId, String mUsername, String mNickname, String mAvatar) {
        this.mId = mId;
        this.mUsername = mUsername;
        this.mNickname = mNickname;
        this.mAvatar = mAvatar;
    }
}
