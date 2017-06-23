# EnhancedRecyclerView


## Introduction

This is a library for android to swipe base on RecyclerView

![result.gif](https://github.com/SenzerZheng/EnhancedRecyclerView/blob/master/art/result.gif)

# How to use


## 1. Add the dependency：

### Gradle

```java
compile 'com.github.SenzerZheng:EnhancedRecyclerView:1.0'
```

## 2. Add the following code in your Layout：

```xml
<com.senzer.enhancedrecyclerview.EnhancedRecyclerView xmlns:android="http://schemas.android.com/apk/res/android"
    xmlns:app="http://schemas.android.com/apk/res-auto"
    android:id="@+id/enhanced_recycler"
    android:layout_width="match_parent"
    android:layout_height="match_parent"
    android:background="@color/transparent"
    android:minHeight="@dimen/dp_150"
    app:layout_empty="@layout/lay_emptyview_list"
    app:layout_moreProgress="@layout/lay_more_progress"
    app:mainLayoutId="@layout/layout_enhanced_recyclerview"
    app:recyclerClipToPadding="false"
    app:scrollbarStyle="insideOverlay" />
```

## 3.Add the following code in your Activity：

* initialize the instance
```java
enhancedRecyclerView = (EnhancedRecyclerView) findViewById(R.id.enhanced_recycler);
enhancedRecyclerView.setLayoutManager(getLayoutManager());
enhancedRecyclerView.setRefreshListener(this);
enhancedRecyclerView.setupMoreListener(this, 1);
```

* implements OnRefreshListener and OnMoreListener
```java=
@Override
public void onRefresh() {
    enhancedRecyclerView.postDelayed(new Runnable() {
        @Override
        public void run() {
            List<User> users = new ArrayList<>();
            for (int i = 0; i < 20; i++) {
                User user = new User();
                user.setName("name: " + i);
                users.add(user);
            }
            adapter.setDataSource(users, false);

            Toast.makeText(RecycleDemoActivity.this, "refresh ok", Toast.LENGTH_LONG);
            refreshComplete();
        }
    }, 2000);
}
 
@Override
public void onMoreAsked(int overallItemsCount, int itemsBeforeMore, int maxLastVisiblePosition) {
    enhancedRecyclerView.postDelayed(new Runnable() {
        @Override
        public void run() {
            List<User> users = new ArrayList<>();
            for (int i = 20; i < 40; i++) {
                User user = new User();
                user.setName("name: " + i);
                users.add(user);
            }
            adapter.setDataSource(users, true);

            Toast.makeText(RecycleDemoActivity.this, "loading more ok", Toast.LENGTH_LONG);
            refreshComplete();
        }
    }, 2000);
}
```

## For more detail, pelase refer to the Demo code.

###  [Here is demo code](https://github.com/SenzerZheng/EnhancedRecyclerView/blob/master/app/src/main/java/com/senzer/enhancedrecyclerview/RecycleDemoActivity.java)

## Thanks

- [SuperRecyclerView](https://github.com/Malinskiy/SuperRecyclerView)

# License

```
Copyright 2017 Senzer
Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

   http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
```
