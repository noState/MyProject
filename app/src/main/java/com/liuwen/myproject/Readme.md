
==============================================================项目架构描述======================================================================

Monkey测试:cmd指令>>>               adb -s emulator-5554 shell monkey -p com.liuwen.myproject -v 1000        (在模拟器编号5554上进行测试)
加密证书:cmd指令>>>                 cd .android --- keytool -list -v -keystore C:\Users\Administrator\Desktop\MyProject\app\myproject.jks



#base_app:>>>                       Application + Base基类 + Manager管理类 包

#base_constant:>>>                  Constant 全局变量类     IntentKey Intent跳转数据变量类

#base_entities:>>>                  数据请求体XXXReq 数据响应体XXXRes Bean(数据解析Modle)类 便于程序上线混淆

#modules_guide_splash:>>>           引导_闪屏界面

#modules_singIn_singUp:>>>          登入注册忘记密码

#modules_h5:>>>                     全局H5页面(腾讯X5内核WebView根据前端需求集成,尚未集成)


==========utils工具包==========

#httpUtil:>>>                       针对后台(RxJava+Retrofit+OkHttp3)网络访问框架包 + (RxBus)组件通讯 封装体

#securitySpUtil:>>>                 加密功能SP临时存储工具

#CommonUtils:>>>                    常用工具方法集合(N多个方法都在这里)

#CreateQRUtil:>>>                   二维码生成工具

#DensityUtil:>>>                    dp与px转换工具

#DoubleClickExitAppUtil:>>>         双击退出程序工具(进程保活)

#NetUtil:>>>                        设备网络信息工具

#ToastUtil:>>>                      Toast提示工具

#SoftKeyBord:>>>                    软键盘状态信息工具

#TimeUtil:>>>                       时间信息,时间格式化工具



===============views===============           自定义View包,较为复杂的建议在该包下新建包



==========firstpage==========       第一页(首页)

==========secondpage==========      第二页

==========thirdpage==========       第三页

==========fourthpage==========      第四页
