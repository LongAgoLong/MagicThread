package com.leo.magic.aspect;

import android.util.Log;

import androidx.lifecycle.LifecycleOwner;

import com.leo.magic.annotation.MagicThread;
import com.leo.magic.thread.LifeCycleController;
import com.leo.magic.thread.MagicRunnable;
import com.leo.magic.thread.ThreadController;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;

import java.lang.reflect.Method;

import com.leo.magic.ThreadMode;

@Aspect
public class ThreadAspect {
    LifeCycleController mLifeCycleController = new LifeCycleController();

    private static final String MAGIC_THREAD =
            "execution(@com.leo.magic.annotation.MagicThread * *(..))";

    @Pointcut(MAGIC_THREAD)
    public void methodMagicThread() {
    }

    @Around("methodMagicThread()")
    public void aroundJoinPointSwitch(final ProceedingJoinPoint joinPoint) {
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Method method = signature.getMethod();
        MagicThread annotation = method.getAnnotation(MagicThread.class);
        ThreadMode threadMode = annotation.threadMode();
        int delayMillisecond = annotation.delayMillisecond();
        if (!Thread.currentThread().isInterrupted()) {
            switch (threadMode) {
                case UI:
                    ThreadController.runOnUIThread(getRunnable(joinPoint), delayMillisecond);
                    break;
                case IO:
                    ThreadController.runOnIOThread(getRunnable(joinPoint), delayMillisecond);
                    break;
                case CALC:
                    ThreadController.runOnCalcThread(getRunnable(joinPoint), delayMillisecond);
                    break;
                case BACKGROUND:
                    ThreadController.runOnBackThread(getRunnable(joinPoint), delayMillisecond);
                    break;
                default:
                    break;
            }
        }
    }

    @AfterReturning(pointcut = "methodMagicThread()",
            returning = "retVal")
    public void afterJoinPointIO(Object retVal) {
        if (retVal != null) {
            Log.e("MagicThread:", "线程转换注解的方法不能有返回值");
        }
    }

    public MagicRunnable getRunnable(final ProceedingJoinPoint joinPoint) {
        final Object target = joinPoint.getTarget();
        MagicRunnable runnable = new MagicRunnable() {
            @Override
            public void run() {
                super.run();
                try {
                    joinPoint.proceed();
                    if (target instanceof LifecycleOwner) {
                        mLifeCycleController.unSubscribe((LifecycleOwner) target, this);
                    }
                } catch (Throwable throwable) {
                    throwable.printStackTrace();
                }
            }
        };
        if (target instanceof LifecycleOwner) {
            mLifeCycleController.subscribe((LifecycleOwner) target, runnable);
        }
        return runnable;
    }

}
