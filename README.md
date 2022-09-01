## 人人投票

### 简介

基于`SSM`的仿[人人微投票](https://www.rrwtp.com/)前后端分离Web实训项目

> 注：为什么不用`Spring Boot`呢？因为干不过带实训的老师，没办法只能折磨自己用了`SSM`来写后端的部分。
>
> 前端写的不是很好，大佬轻喷。。。

### 技术栈

| 名称    | 版本   | 备注 |
| ------- | ------ | ---- |
| `JDK`   | 11     |      |
| `Mysql` | 5.7.28 |      |
| `redis` | 4.0.14 |      |

### 项目运行

#### 后端

`mysql`中新建数据库`everybody_votes`，导入项目`resources`中的`sql`文件后，运行`redis`，在`Idea`中添加`tomcat`，就可运行了项目后端了。

> 注：关于阿里的短信服务和邮箱服务、`UClound`对像存储，请改成自己的公钥与私钥。

##### 具体需要修改配置位置

![](https://note.youdao.com/yws/public/resource/bea36f3fd8174d76a492cb5a78f86a71/xmlnote/WEBRESOURCEb2e9bff59bffcc49b3f78d1a3b0bc273/7085)

![](https://note.youdao.com/yws/public/resource/bea36f3fd8174d76a492cb5a78f86a71/xmlnote/WEBRESOURCE27c836ddbfaff163a37a976a4f0c520f/7088)

#### 前端

项目地址: https://github.com/Cubexp/EverybodyVotes 

安装项目依赖

```
npm install
```

运行项目

```
npm run serve
```

### 项目效果图

#### 首页

![](https://z3.ax1x.com/2021/05/24/gxrRjs.png)

#### 帮助中心

![](https://z3.ax1x.com/2021/05/24/gxrc9g.png)

![](https://z3.ax1x.com/2021/05/24/gxsfRe.png)

#### 创建活动

![](https://z3.ax1x.com/2021/05/24/gxsKPS.png)

![](https://z3.ax1x.com/2021/05/24/gxs3Ks.png)



![](https://z3.ax1x.com/2021/05/24/gxsYV0.png)

#### 个人资料

![](https://z3.ax1x.com/2021/05/24/gxsDM9.png)

活动管理

![](https://z3.ax1x.com/2021/05/24/gxsHdP.png)

活动页面

![](https://z3.ax1x.com/2021/05/24/gxy9Zq.png)

![](https://z3.ax1x.com/2021/05/24/gxyPoV.png)

### 关于项目

​	因为实训时间比较紧加上自己前期有点摸鱼和对后端增删改查的**厌烦**，再就是出于对自己身心健康的考虑，有些功能像选手的显示方式……等一些功能，自己就没写了。

### 联系方式

有问题联系QQ: 2317537731