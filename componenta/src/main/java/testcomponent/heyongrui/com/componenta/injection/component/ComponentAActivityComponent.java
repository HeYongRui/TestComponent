package testcomponent.heyongrui.com.componenta.injection.component;


import dagger.Component;
import testcomponent.heyongrui.com.base.injection.annotation.PerActivity;
import testcomponent.heyongrui.com.base.injection.component.BaseAppComponent;
import testcomponent.heyongrui.com.componenta.ComponentActivityA;
import testcomponent.heyongrui.com.componenta.injection.module.ComponentAActivityModule;
import testcomponent.heyongrui.com.componenta.ui.mono.view.MonoCategoryActivity;
import testcomponent.heyongrui.com.componenta.ui.mono.view.MonoMusicPlayActivity;
import testcomponent.heyongrui.com.componenta.ui.mono.view.MonoTabActivity;
import testcomponent.heyongrui.com.componenta.ui.qingmang.view.QingMangArticleFragment;
import testcomponent.heyongrui.com.componenta.ui.qingmang.view.QingMangCategoriesActivity;
import testcomponent.heyongrui.com.componenta.ui.unsplash.view.UnsplashActivity;

/**
 * lambert
 * 2018/10/22 17:44
 * <p>
 * This component inject dependencies to all Activities across the application
 * 每个组件模块单独设置ActivityComponent，因为在底层(base)无法获取上层哪些Activity会使用
 * 底层(base)只是提供者，组件上层才是索取者
 */
@PerActivity
@Component(dependencies = BaseAppComponent.class, modules = {ComponentAActivityModule.class})
public interface ComponentAActivityComponent {

    void inject(ComponentActivityA baseActivity);

    void inject(UnsplashActivity unsplashActivity);

    void inject(QingMangCategoriesActivity qingMangCategoriesActivity);

    void inject(QingMangArticleFragment qingMangArticleFragment);

    void inject(MonoCategoryActivity monoCategoryActivity);

    void inject(MonoTabActivity monoTabActivity);

    void inject(MonoMusicPlayActivity monoMusicPlayActivity);
}
