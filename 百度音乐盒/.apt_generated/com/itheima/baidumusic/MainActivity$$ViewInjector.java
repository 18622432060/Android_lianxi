// Generated code from Butter Knife. Do not modify!
package com.itheima.baidumusic;

import android.view.View;
import butterknife.ButterKnife.Finder;

public class MainActivity$$ViewInjector {
  public static void inject(Finder finder, final com.itheima.baidumusic.MainActivity target, Object source) {
    View view;
    view = finder.findRequiredView(source, 2131230720, "method 'start'");
    view.setOnClickListener(
      new android.view.View.OnClickListener() {
        @Override public void onClick(
          android.view.View p0
        ) {
          target.start();
        }
      });
    view = finder.findRequiredView(source, 2131230721, "method 'pause'");
    view.setOnClickListener(
      new android.view.View.OnClickListener() {
        @Override public void onClick(
          android.view.View p0
        ) {
          target.pause();
        }
      });
    view = finder.findRequiredView(source, 2131230722, "method 'resume'");
    view.setOnClickListener(
      new android.view.View.OnClickListener() {
        @Override public void onClick(
          android.view.View p0
        ) {
          target.resume();
        }
      });
  }

  public static void reset(com.itheima.baidumusic.MainActivity target) {
  }
}
