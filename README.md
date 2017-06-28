# RxNetWork
this is android network ,and RxBus

android 网络请求简化库

> RxNetWork项目试用：

[https://github.com/7449/ZLSimple](https://github.com/7449/ZLSimple)

> RxJsoupNetWork项目试用

[https://github.com/7449/JsoupSample](https://github.com/7449/JsoupSample)

> compile 'com.ydevelop:rxNetWork:0.1.3'


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

参数一： tag，用于取消网络请求
参数二：observable
参数三： 回调，网络请求开始，异常，结束，成功之类的状态


* 取消网络请求：

	RxNetWork.getInstance().cancel(tag);


        Disposable api = RxNetWork
                .getInstance()
                .getApi(getClass().getSimpleName(),
                	RxNetWork.observable(),
                new RxNetWorkListener<List<ListModel>>() {
                    @Override
                    public void onNetWorkStart() {

                    }

                    @Override
                    public void onNetWorkError(Throwable e) {

                    }

                    @Override
                    public void onNetWorkComplete() {

                    }

                    @Override
                    public void onNetWorkSuccess(List<ListModel> data) {

                    }
                  
                });



> 缓存：

如果使用默认的`okhttp`,缓存需要如下配置

        RxNetWork
                .getInstance()
                .setCache()
                .setCacheInterceptor();


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

> RxBus使用：


#### 发送消息：

        RxBus.getInstance().post("tag","message");
        RxBus.getInstance().post("tag");

#### 注册消息体：

        RxBus.getInstance().register(getClass().getSimpleName(), new RxBusCallBack<String>() {
            @Override
            public void onBusNext(String s) {

            }

            @Override
            public void onBusError(Throwable throwable) {

            }

            @Override
            public Class<String> busOfType() {
                return String.class;
            }
        });

#### 解绑：

	RxBus.getInstance().unregister(tag);
	
	
## License

    Copyright (C) 2017 yuebigmeow@gamil.com

    Licensed under the Apache License, Version 2.0 (the "License");
    you may not use this file except in compliance with the License.
    You may obtain a copy of the License at

    http://www.apache.org/licenses/LICENSE-2.0

    Unless required by applicable law or agreed to in writing, software
    distributed under the License is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
    See the License for the specific language governing permissions and
    limitations under the License.




