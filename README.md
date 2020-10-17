# MagicThread

------

## 基于AOP实现通过注解来完成方法运行线程切换

[![](https://jitpack.io/v/LongAgoLong/MagicThread.svg)](https://jitpack.io/#LongAgoLong/MagicThread)

## 1.依赖

### project的gradle依赖

```
allprojects {
	repositories {
		...
		maven { url 'https://jitpack.io' }
	}
}
```

### build.gradle依赖

```
plugins {
    //AOP插件
    id 'android-aspectjx'
}
```

```gradle
dependencies {
	def version = "1.0.1"
	implementation "com.github.LongAgoLong:MagicThread:$version"
}
```

### maven依赖

```xml
    <repositories>
		<repository>
		    <id>jitpack.io</id>
		    <url>https://jitpack.io</url>
		</repository>
	</repositories>
```
```xml
	<dependency>
	    <groupId>com.github.LongAgoLong</groupId>
	    <artifactId>MagicThread</artifactId>
	    <version>$version</version>
	</dependency>
```

## 2.注解

```java
    @MagicThread(threadMode = ThreadMode.MAIN, delayMillisecond = 12000)
    public void delayToast() {
        Toast.makeText(this, "延迟成功", Toast.LENGTH_LONG).show();
    }
```

MagicThread(threadMode = ThreadMode.MAIN, delayMillisecond = 12000)

- threadMode：方法执行的线程

  支持

  MAIN（主线程）、

  IO（子线程，IO密集型，通过线程池实现）、

  CALC（子线程，Cpu调用密集型，如运算，通过线程池实现）、

  BACKGROUND（子线程，通过HandlerThread实现；单一线程，有序执行）

- delayMillisecond ：延时执行时间，毫秒级

PS：方法修饰必须是public！