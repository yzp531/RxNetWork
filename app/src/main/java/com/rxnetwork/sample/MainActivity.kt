package com.rxnetwork.sample

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.AppCompatTextView
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.text.TextUtils
import android.util.Log
import android.view.View
import android.widget.ProgressBar
import android.widget.Toast
import com.google.gson.reflect.TypeToken
import com.rxnetwork.sample.samplebus.A
import com.socks.library.KLog
import io.reactivex.network.RxNetWork
import io.reactivex.network.RxNetWorkListener
import io.reactivex.network.bus.RxBus
import io.reactivex.network.bus.SimpleRxBusCallBack
import io.reactivex.network.cache.RxCache
import java.util.*


open class MainActivity : AppCompatActivity(), RxNetWorkListener<List<ListModel>?>, View.OnClickListener {

    private var adapter: MainAdapter? = null
    private var textView: AppCompatTextView? = null
    private var progressBar: ProgressBar? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val recyclerView = findViewById(R.id.recyclerView) as RecyclerView
        progressBar = findViewById(R.id.progress) as ProgressBar
        textView = findViewById(R.id.bus_message) as AppCompatTextView
        recyclerView.layoutManager = LinearLayoutManager(recyclerView.context)
        adapter = MainAdapter(ArrayList())
        recyclerView.adapter = adapter


        RxBus.instance.register(BUS_TAG,
                object : SimpleRxBusCallBack<String>() {
                    override fun onBusNext(s: String) {
                        super.onBusNext(s)
                        textView!!.text = TextUtils.concat("RxBus Message:", s)
                    }

                    override fun busOfType(): Class<String> = String::class.java
                })
        findViewById(R.id.btn_send).setOnClickListener(this)
        findViewById(R.id.btn_unregister).setOnClickListener(this)
        findViewById(R.id.btn_test_bus).setOnClickListener(this)
        findViewById(R.id.btn_start_network).setOnClickListener(this)
        findViewById(R.id.btn_cancel_network).setOnClickListener(this)


        //        Observable<CacheResult<Object>> daily = RxNetWork
        //                .observable(Api.ZLService.class)
        //                .getList("daily", 20, 0)
        //                .compose(RxCache.getInstance().customizeTransformer("", new CustomizeTransformerCall() {
        //                    @Override
        //                    public <T> ObservableSource<CacheResult<T>> applyCustomize(@NonNull Object key, Observable<T> upstream) {
        //                        return Observable.just(null);
        //                    }
        //                }));
    }

    override fun onClick(v: View) {
        when (v.id) {
            R.id.btn_send -> RxBus.instance.post(BUS_TAG, "传递对象")
            R.id.btn_unregister -> KLog.i(RxBus.instance.unregister(BUS_TAG))
            R.id.btn_test_bus -> startActivity(A::class.java)
            R.id.btn_start_network -> {
                val daily = RxNetWork
                        .observable(Api.ZLService::class.java)
                        .getList("daily", 20, 0)
                        .compose(RxCache.instance.transformerCN("cache", true, object : TypeToken<List<ListModel>>() {

                        }))
                        .map { listCacheResult ->
                            Log.i("RxCache", listCacheResult.type.toString() + "")
                            listCacheResult.result
                        }
                RxNetWork
                        .instance
                        .setLogInterceptor(SimpleLogInterceptor())
                        .getApi(javaClass.simpleName, daily, this)
            }
            R.id.btn_cancel_network -> RxNetWork.instance.cancel(javaClass.simpleName)
        }
        textView!!.text = ""
    }

    private fun startActivity(clz: Class<*>) {
        val intent = Intent(applicationContext, clz)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
        startActivity(intent)
    }

    override fun onNetWorkStart() {
        adapter!!.clear()
        progressBar!!.visibility = View.VISIBLE
    }

    override fun onNetWorkError(e: Throwable) {
        progressBar!!.visibility = View.GONE
        Toast.makeText(applicationContext, "net work error:" + e.toString(), Toast.LENGTH_SHORT).show()
    }

    override fun onNetWorkComplete() {
        progressBar!!.visibility = View.GONE
    }

    override fun onNetWorkSuccess(data: List<ListModel>?) {
        if (data != null) {
            adapter!!.addAll(data)
        }
    }

    override fun onBackPressed() {
        super.onBackPressed()
        KLog.i(RxBus.instance.unregisterAll())
    }

    companion object {

        private val BUS_TAG = "bus_tag"
    }
}
