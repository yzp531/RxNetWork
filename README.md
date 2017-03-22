# RxNetWork
this is android network ,and RxBus

基于rxjava1.x and retrofit2.x 版本的一个网络请求简化库


[rxJsoupNetWork](https://github.com/7449/RxNetWork/tree/RxJsoupNetWork):简化Jsoup网络请求
> gradle:

	compile 'com.ydevelop:rxNetWork:0.0.4'


RxNetWork依赖以下类库：

    compile 'io.reactivex:rxjava:1.2.6'
    compile 'io.reactivex:rxandroid:1.2.1'
    compile 'com.squareup.retrofit2:retrofit:2.1.0'
    compile 'com.squareup.retrofit2:converter-gson:2.1.0'
    compile 'com.squareup.retrofit2:adapter-rxjava:2.1.0'
    compile 'com.android.support:support-v4:25.0.0'


如果不想使用RxNetWork依赖的类库，请用`exclude`排除掉

> 项目试用：

[https://github.com/7449/ZLSimple](https://github.com/7449/ZLSimple)


> 建议初始化:

	public class App extends Application {
	
	    @Override
	    public void onCreate() {
	        super.onCreate();
	        
	        RxNetWork
	                .getInstance()
	                .setBaseUrl("your base url")
					...;
	    }
	
	}


> 支持一下自定义（如果不想使用内置类库可自定义）：

Gson,

OkHttpClient

Converter.Factory

CallAdapter.Factory


> 使用方法：

这里假设在`Application`里已经自定义好`BaseUrl`;

`getApi` 需要两个参数，一个`Observable`还有一个网络回调`RxNetWorkListener`


        RxNetWork.getInstance().getApi(

                RxNetWork.observable(Api.ZLService.class).getList("", 20, 0),

                new RxNetWorkListener<List<ListModel>>() {
                    @Override
                    public void onNetWorkStart() {
                        
                    }

                    @Override
                    public void onNetWorkError(Throwable e) {

                    }

                    @Override
                    public void onNetWorkCompleted() {

                    }

                    @Override
                    public void onNetWorkSuccess(List<ListModel> data) {

                    }
                }

        );



> 缓存：

如果使用默认的`okhttp`,缓存需要如下配置

        RxNetWork
                .getInstance()
                .setCache()
                .setCacheInterceptor();

具体可参考：[https://github.com/7449/AndroidDevelop/blob/master/codeKK/src/main/java/framework/network/NetWork.java](https://github.com/7449/AndroidDevelop/blob/master/codeKK/src/main/java/framework/network/NetWork.java)


> 配置Header:

如果使用默认的`okhttp`,配置Header需要如下操作：

        RxNetWork
                .getInstance()
                .setHeaderInterceptor();
		

> 配置Log

如果使用默认的`okhttp`,配置Log需要如下操作：

        RxNetWork
                .getInstance()
                .setLogInterceptor();



> 取消当前订阅

	RxSubscriptionManager.getInstance().unSubscription();

> 取消所有订阅

	RxSubscriptionManager.getInstance().clearSubscription();

> RxBus使用：


#### 发送消息：

        RxBus.getInstance().send("tag","message");
        RxBus.getInstance().send("tag");

#### 接受消息：

        RxBus.getInstance().toSubscription("tag", String.class, new RxBusCallBack<String>() {
            @Override
            public void onNext(String data) {
                
            }

            @Override
            public void onError(Throwable throwable) {

            }
        });

#### 解绑：

	RxBus.getInstance().unregister("tag");




