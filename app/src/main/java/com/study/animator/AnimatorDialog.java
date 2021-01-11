package com.study.animator;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.Keyframe;
import android.animation.ObjectAnimator;
import android.animation.PropertyValuesHolder;
import android.animation.ValueAnimator;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Path;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.interpolator.view.animation.FastOutSlowInInterpolator;

import com.study.customview.R;
import com.study.utils.DimenUtils;

/**
 * 1. ValueAnimator 不会对控件做任何操作，我们可以给它设定从哪个值运动到哪个值，通过监听这些值的渐变过程来自己操作控件。
 */
public class AnimatorDialog extends Dialog {
    private WindowManager.LayoutParams params;
    private ImageView imageView;

    public AnimatorDialog(@NonNull Context context) {
        super(context, R.style.homePageAdvDialog);
        init();
    }

    private void init() {
        View view = LayoutInflater.from(getContext()).inflate(R.layout.dialog_animator, null);
        setContentView(view);
        Window dialogWindow = getWindow();
        params = dialogWindow.getAttributes();
        params.width = DimenUtils.getScreenWidth();
        params.height = DimenUtils.getScreenHeight() + DimenUtils.getStatusBarHeight(getContext());
        dialogWindow.setGravity(Gravity.CENTER);
        setCanceledOnTouchOutside(false);

        imageView = view.findViewById(R.id.iv_animator);
    }

    public void startHbAnimate() {
//        startBgAnimator();
//        startIvAnimator();
        startShakeByPropertyAnim(imageView,0.8f, 2.0f, 15f, 1500);
        startPathAnimator(path(imageView));
    }

    private void startPathAnimator(Path path) {
        ObjectAnimator objectAnimator = ObjectAnimator.ofFloat(imageView, imageView.X, imageView.Y,path);
        objectAnimator.setInterpolator(new FastOutSlowInInterpolator());
        objectAnimator.setDuration(3000);
        objectAnimator.start();
        objectAnimator.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {

            }

            @Override
            public void onAnimationEnd(Animator animation) {
                dismiss();
            }

            @Override
            public void onAnimationCancel(Animator animation) {
                dismiss();
            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });
    }

    /**
     * 圆弧动画
     *
     * @return
     */
    private Path path(View view) {
        Path path = new Path();
        int viewWidth = getViewWidth(view);
        int d_width = DimenUtils.getScreenWidth() - viewWidth;
        path.moveTo(DimenUtils.getScreenWidth(), DimenUtils.getScreenHeight());	//移动下一次操作的起点位置
        /*
            二阶贝塞尔曲线
            (x1,y1)是控制点坐标，(x2,y2)是终点坐标
            public void quadTo(float x1, float y1, float x2, float y2)
         */
        path.quadTo(0, d_width, DimenUtils.getScreenWidth(), DimenUtils.getScreenWidth() / 2);
//        path.moveTo(d_width, d_width);
//        path.quadTo(d_width, 0, 0, 0);
        return path;
    }

    public int getViewWidth(View view) {
        view.measure(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        return view.getMeasuredWidth();
    }

    private void startShakeByPropertyAnim(View view, float scaleSmall, float scaleLarge, float shakeDegrees, long duration) {
        if (view == null) {
            return;
        }
        //TODO 验证参数的有效性

        //先变小后变大
        PropertyValuesHolder scaleXValuesHolder = PropertyValuesHolder.ofKeyframe(View.SCALE_X,
                Keyframe.ofFloat(0f, 1.0f),
                Keyframe.ofFloat(0.25f, scaleSmall),
                Keyframe.ofFloat(0.5f, scaleLarge),
                Keyframe.ofFloat(0.75f, scaleLarge),
                Keyframe.ofFloat(1.0f, 1.0f)
        );
        PropertyValuesHolder scaleYValuesHolder = PropertyValuesHolder.ofKeyframe(View.SCALE_Y,
                Keyframe.ofFloat(0f, 1.0f),
                Keyframe.ofFloat(0.25f, scaleSmall),
                Keyframe.ofFloat(0.5f, scaleLarge),
                Keyframe.ofFloat(0.75f, scaleLarge),
                Keyframe.ofFloat(1.0f, 1.0f)
        );

        //先往左再往右
        PropertyValuesHolder rotateValuesHolder = PropertyValuesHolder.ofKeyframe(View.ROTATION,
                Keyframe.ofFloat(0f, 0f),
                Keyframe.ofFloat(0.1f, -shakeDegrees),
                Keyframe.ofFloat(0.2f, shakeDegrees),
                Keyframe.ofFloat(0.3f, -shakeDegrees),
                Keyframe.ofFloat(0.4f, shakeDegrees),
                Keyframe.ofFloat(0.5f, -shakeDegrees),
                Keyframe.ofFloat(0.6f, shakeDegrees),
                Keyframe.ofFloat(0.7f, -shakeDegrees),
                Keyframe.ofFloat(0.8f, shakeDegrees),
                Keyframe.ofFloat(0.9f, -shakeDegrees),
                Keyframe.ofFloat(1.0f, 0f)
        );

        ObjectAnimator objectAnimator = ObjectAnimator.ofPropertyValuesHolder(view, scaleXValuesHolder, scaleYValuesHolder, rotateValuesHolder);
        objectAnimator.setDuration(duration);
        objectAnimator.start();
    }

    private void startIvAnimator() {
        AnimatorSet animatorSet = new AnimatorSet();
        ObjectAnimator ivAnimatorX = ObjectAnimator.ofFloat(imageView, "scaleX", 3.6f, 1.0f);
        ObjectAnimator ivAnimatorY = ObjectAnimator.ofFloat(imageView, "scaleY", 3.6f, 1.0f);
        ObjectAnimator ivAnimatorA = ObjectAnimator.ofFloat(imageView, "alpha", 0f, 1.0f);
        animatorSet.playTogether(ivAnimatorX, ivAnimatorY, ivAnimatorA);
        animatorSet.setDuration(3000);
//        animatorSet.setStartDelay(100);
        animatorSet.setInterpolator(new FastOutSlowInInterpolator());
        animatorSet.start();
//        animatorSet.addListener(new Animator.AnimatorListener() {
//            @Override
//            public void onAnimationStart(Animator animation) {
//
//            }
//
//            @Override
//            public void onAnimationEnd(Animator animation) {
//                dismiss();
//            }
//
//            @Override
//            public void onAnimationCancel(Animator animation) {
//                dismiss();
//            }
//
//            @Override
//            public void onAnimationRepeat(Animator animation) {
//
//            }
//        });

//        AnimatorSet animatorSet2 = new AnimatorSet();
//        ObjectAnimator jzMx2 = ObjectAnimator.ofFloat(imageView, "scaleX", 1f, 0.05f);
//        ObjectAnimator jzMy2 = ObjectAnimator.ofFloat(imageView, "scaleY", 1f, 0.05f);
//        ObjectAnimator jzAlpha2 = ObjectAnimator.ofFloat(imageView, "alpha", 1f, 0.6f);
//        ObjectAnimator jzTx = ObjectAnimator.ofFloat(imageView, "translationX", 0, DimenUtils.getScreenWidth() / 2 - DimenUtils.dp2px(55));
//        ObjectAnimator jzTy = ObjectAnimator.ofFloat(imageView, "translationY", 0, -DimenUtils.getScreenHeight() / 2 + DimenUtils.dp2px(100));
//        animatorSet2.playTogether(jzMx2, jzMy2, jzAlpha2, jzTx, jzTy);
//        animatorSet2.setDuration(3000);
//        animatorSet2.setStartDelay(330);
//        animatorSet2.start();
    }

    //改变背景透明度,windowBackground 从0f -> 0.8f (1s) 1s后 0.8f -> 0f
    private void startBgAnimator() {
        ValueAnimator bgAnimator1 = ValueAnimator.ofFloat(0f, 0.8f);
        bgAnimator1.setDuration(1000);
        bgAnimator1.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                Float value = (Float) animation.getAnimatedValue();
                params.dimAmount = value;
                getWindow().setAttributes(params);
            }
        });
        bgAnimator1.start();

        ValueAnimator bgAnimator2 = ValueAnimator.ofFloat(0.8f, 0f);
        bgAnimator2.setDuration(1000);
        bgAnimator2.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                Float value = (Float) animation.getAnimatedValue();
                params.dimAmount = value;
                getWindow().setAttributes(params);
            }
        });
        bgAnimator2.setStartDelay(1000);
        bgAnimator2.start();
    }
}
