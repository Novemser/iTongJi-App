iTongJi 同济小助手
==============================
![main](https://github.com/Novemser/iTongJi-App/blob/master/Img/love.jpg)

iTongJi是一款为上海同济大学的同学定制的校园助手类软件

集合若干查询功能，包括:
- 获取选课网通知
- 校园卡余额查询
- 寝室用电情况查询
- 课表查询
- 免验证码查询绩点
- 校园卡低余额自动提醒

旨在帮助同济大学的在校生更方便快捷地获取平时较为常用的信息

如果你对本应用有任何的意见或建议，请发送邮件至Novemser@gmail.com，我会努力让iTongJi变的更好:)


<img src="https://raw.githubusercontent.com/Novemser/iTongJi-App/master/Img/Screenshot_2016-04-04-19-10-48.jpg" width="300px" />
<img src="https://raw.githubusercontent.com/Novemser/iTongJi-App/master/Img/Screenshot_2016-04-04-19-13-13.png" width="300px" />

<img src="https://raw.githubusercontent.com/Novemser/iTongJi-App/master/Img/Screenshot_2016-04-06-10-43-14.png" width="300px" />
<img src="https://raw.githubusercontent.com/Novemser/iTongJi-App/master/Img/Screenshot_2016-04-04-19-14-18.png" width="300px" />

<img src="https://raw.githubusercontent.com/Novemser/iTongJi-App/master/Img/Screenshot_2016-04-04-19-14-29.png" width="300px" />
<img src="https://raw.githubusercontent.com/Novemser/iTongJi-App/master/Img/Screenshot_2016-04-04-19-14-47.png" width="300px" />

<img src="https://raw.githubusercontent.com/Novemser/iTongJi-App/master/Img/Screenshot_2016-04-04-19-13-44.png" width="300px" />
<img src="https://raw.githubusercontent.com/Novemser/iTongJi-App/master/Img/Screenshot_2016-04-04-19-14-00.png" width="300px" />
<br>
<br>
<br>

v0.5版本更新:
完成了App的重构：
1. Singleton
  - HTTP请求集中化管理
  - 观察者，订阅者集中化管理
  - 全局安卓系统资源的管理
2. Observer
  - 从网络层会获取到多种数据并显示在主界面的UI上，考虑使用最大化并行获取数据的方式，当任何数据生成之后对其订阅者进行通知
  - 更新数据也是一样的思路
3. Prototype
  - 近期通知模块中，每次新闻文本的获取都会消耗大量的网络IO资源，考虑第一次请求一个新闻后就对其进行持有，当第二次请求的时候直接拷贝已经持有的object进行呈现
4. IoC/Dependency Injection
  - 主界面是RecyclerView实现的，创建它的时候本来需要他自己创建许多其他新的object，需要各种findViewById()，LineDataSet等，考虑通过注解和构造方法进行依赖注入，就不需要高层去关注低层的创建，实现控制反转
5. Chain of Responsibility 责任链模式
  - 将有依赖的网络请求一层一层传递下去，比如爬取课表，绩点等等网络请求，后一步都是紧紧依赖前一步的结果。以前版本的实现方式是嵌套回调，代码非常难以维护，现在考虑用CoR进行重构
  - eg. ```this.operations.add(new Operation1(new RequestA(), new CallbackA()));
    this.operations.add(new Operation2(new RequestB(), new CallbackB()));
    this.operations.add(new Operation3(new RequestC(), new CallbackC()));
    this.operations.add(new Operation4(new RequestD(), new CallbackD()));```
6. Factory Method
  - 对1-6中可能用到的显式new Operation进行封装

更新日志：
- 修复了登录后退出的bug
- 查询速度提升数倍
- 增强了稳定性


v0.3.5版本更新：
---------------------
- 修复部分BUG

v0.3.4版本更新:
----------------------
- 修复了下节课程显示错误的问题

v0.3.3版本更新:
----------------------
- 修复了通知无法停止的问题

v0.3.2版本更新:
--------------------
- 修复了校园卡余额null的问题
- 返回键逻辑优化

v0.3.1版本更新:
--------------------
- 增加自动更新功能
- 代码优化



v0.3版本更新:
--------------------
- 增加分享功能
- 细节改进
- 安装包优化


v0.2版本更新:
--------------------
- 改进注销逻辑
- 下节课程显示改进
- Bug修复
- 适配更多机型



v0.1 Beta 测试版正式上线
--------------------
- 下载地址 http://zhushou.360.cn/detail/index/soft_id/3252384



闲言碎语
- 15.03.09 辛苦多日 终于完成了最难的部分——绩点免验证码查询
- 纪念一下 >.<
