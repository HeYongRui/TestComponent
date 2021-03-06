package testcomponent.heyongrui.com.componenta;

import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.util.Log;

import com.billy.cc.core.component.CC;
import com.billy.cc.core.component.CCResult;
import com.billy.cc.core.component.CCUtil;
import com.billy.cc.core.component.IComponent;
import com.billy.cc.core.component.IMainThread;
import com.tencent.smtt.sdk.QbSdk;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicBoolean;

import testcomponent.heyongrui.com.componenta.ui.mono.view.MonoTabActivity;
import testcomponent.heyongrui.com.componenta.ui.mono.view.MonoTeaActivity;
import testcomponent.heyongrui.com.componenta.ui.qingmang.view.QingMangArticleActivity;
import testcomponent.heyongrui.com.componenta.ui.qingmang.view.QingMangCategoriesActivity;
import testcomponent.heyongrui.com.componenta.ui.unsplash.view.UnsplashActivity;

/**
 * Created by lambert on 2018/9/28.
 */

public class ComponentA implements IComponent, IMainThread {

    private AtomicBoolean initialized = new AtomicBoolean(false);
    private final HashMap<String, BaseInterceptor> map = new HashMap<>();

    public ComponentA() {//无参构造方法
        //作为组件时的初始化操作，只会执行一次(类似在Application中初始化)
        QbSdk.PreInitCallback cb = new QbSdk.PreInitCallback() {
            @Override
            public void onViewInitFinished(boolean arg0) {
                //x5內核初始化完成的回调，为true表示x5内核加载成功，否则表示x5内核加载失败，会自动切换到系统内核。
                Log.d("X5", " onViewInitFinished is " + arg0);
            }

            @Override
            public void onCoreInitFinished() {
            }
        };
        //x5内核初始化接口
        QbSdk.initX5Environment(CC.getApplication(), cb);
    }

    private void initProcessors() {
    }

    private void add(BaseInterceptor processor) {
        map.put(processor.getActionName(), processor);
    }

    @Override
    public String getName() {
        //组件的名称，调用此组件的方式：
        // CC.obtainBuilder("ComponentA")...build().callAsync()
        return "ComponentA";
    }

    /**
     * 组件被调用时的入口
     * 要确保每个逻辑分支都会调用到CC.sendCCResult，
     * 包括try-catch,if-else,switch-case-default,startActivity
     *
     * @param cc 组件调用对象，可从此对象中获取相关信息
     * @return true:将异步调用CC.sendCCResult(...),用于异步实现相关功能，例如：文件加载、网络请求等
     * false:会同步调用CC.sendCCResult(...),即在onCall方法return之前调用，否则将被视为不合法的实现
     */
    @Override
    public boolean onCall(CC cc) {
        if (initialized.compareAndSet(false, true)) {
            synchronized (map) {
                initProcessors();
            }
        }
        String actionName = cc.getActionName();
        if (TextUtils.equals("ComponentActivityA", actionName)) {
            Context context = cc.getContext();
            Map<String, Object> params = cc.getParams();
            Intent intent = new Intent(context, ComponentActivityA.class);
            if (params != null) {
                if (params instanceof Serializable) {
                    intent.putExtra("parms", (Serializable) params);
                }
            }
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(intent);
            CC.sendCCResult(cc.getCallId(), CCResult.success());
            return false;
        } else if (TextUtils.equals("LoginInterceptor", actionName)) {
            BaseInterceptor baseInterceptor = map.get(actionName);
            if (baseInterceptor != null) {
                return baseInterceptor.onActionCall(cc);
            }
            CC.sendCCResult(cc.getCallId(), CCResult.error("has not support for action:" + cc.getActionName()));
        } else if (TextUtils.equals("openUnsplash", actionName)) {
            CCUtil.navigateTo(cc, UnsplashActivity.class);
            CC.sendCCResult(cc.getCallId(), CCResult.success());
        } else if (TextUtils.equals("openQingMang", actionName)) {
            CCUtil.navigateTo(cc, QingMangCategoriesActivity.class);
            CC.sendCCResult(cc.getCallId(), CCResult.success());
        } else if (TextUtils.equals("openQingMangArticle", actionName)) {
            CCUtil.navigateTo(cc, QingMangArticleActivity.class);
            CC.sendCCResult(cc.getCallId(), CCResult.success());
        } else if (TextUtils.equals("openMonoTea", actionName)) {
            CCUtil.navigateTo(cc, MonoTeaActivity.class);
            CC.sendCCResult(cc.getCallId(), CCResult.success());
        } else if (TextUtils.equals("openMonoTab", actionName)) {
            CCUtil.navigateTo(cc, MonoTabActivity.class);
            CC.sendCCResult(cc.getCallId(), CCResult.success());
        }
        return false;
    }

    @Override
    public Boolean shouldActionRunOnMainThread(String actionName, CC cc) {
        return null;
    }
}
