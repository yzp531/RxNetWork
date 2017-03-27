# RxNetWork
this is android network ,and RxBus

android 网络请求简化库

## 分支

#### master

> rxJava1.x and retrofit2 

###### gradle

	compile 'com.ydevelop:rxNetWork:0.0.5'

#### rx2NetWork

> rxJava2.x and retrofit2 

###### gradle

	compile 'com.ydevelop:rx2NetWork:0.0.1'

#### rxJsoupNetWork

> rxJava1.x and jsoup

###### gradle

	compile 'com.ydevelop:rxJsoupNetWork:0.0.1'

#### rx2JsoupNetWork

> rxJava2.x and jsoup

###### gradle

	compile 'com.ydevelop:rx2JsoupNetWork:0.0.1'

> RxNetWork项目试用：

[https://github.com/7449/ZLSimple](https://github.com/7449/ZLSimple)

> RxJsoupNetWork项目试用

[https://github.com/7449/JsoupSample](https://github.com/7449/JsoupSample)

#### [rx2NetWork README](https://github.com/7449/RxNetWork/blob/rx2NetWork/README.md)

#### [rxJsoupNetWork README](https://github.com/7449/RxNetWork/blob/RxJsoupNetWork/README.md)

#### [rx2JsoupNetWork README](https://github.com/7449/RxNetWork/blob/rx2JsoupNetWork/README.md):

#### rxNetWork README:

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




