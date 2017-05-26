# RxNetWork
this is android network ,and RxBus

android 网络请求简化库

> RxJsoupNetWork项目试用

[https://github.com/7449/JsoupSample](https://github.com/7449/JsoupSample)

	compile 'com.ydevelop:rxJsoupNetWork:0.0.7'



> sample


        DisposableObserver<String> api =
                RxJsoupNetWork.getInstance().getApi(NET_TAG, "http://www.baidu.com",
                        new RxJsoupNetWorkListener<String>() {
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
                            public void onNetWorkSuccess(String s) {

                            }

                            @Override
                            public String getT(Document document) {
                        	//Jsoup Document
                                return document.toString();
                            }
                        });
                        


> 取消网络请求

	RxJsoupNetWork.getInstance().cancel(NET_TAG);

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




