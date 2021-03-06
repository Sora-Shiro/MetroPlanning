# Metro Planning

# 《地铁规划 Metro Planning》 技术文档

[点我下载APK](https://raw.githubusercontent.com/Sora-Shiro/MetroPlanning/master/extra/Metro%20Planning%20v1.0.apk)

## 程序说明

* **开发环境版本** ： Android Studio 2.2 | Sublime Text 3 | Adobe PhotoShop CC 2014
* **模拟器运行版本** ： MI 5 | Android 6.0.1 | MIUI 8 6.7.21

## 设计思路
现实生活中，人们都离不开交通工具，而交通网络的构建需要 **严谨的设计** 和 **高度的灵活** ，从这个思路出发，我们设计了这一款名为 **《地铁规划 Metro Planning》** 游戏，模拟地铁运输乘客，让用户在规划地铁路线时能体会到游戏的真实感和逻辑的挑战。

* ***游戏核心算法设计***
    * 模式：确定使用 MVC 模式。
    * 显示：地图是均匀分块的，那么首先就要 **有一个基类来存储这些分块的信息** ， BlockBase 类因此创建，它有基本的坐标、层数、类型属性，这些是其他 Model 的基础。
    model 包下的其他类都继承 BlockBase ，他们有共同的属性也有不同的属性，这些 **与 view 包下的 BlockView 相结合** ，将元素显示到地图上。
    * 异步数据处理：在 GameActivity 中，RxJava 除了用来处理比较简单的进度条显示外，还要 **快速计算** 游戏进行时相关的数据， updateMetroPosition 这个方法通过内部执行 moveMetroToPosition(beforeX, beforeY, afterX, afterY)，updateMetroOrientation(afterX, afterY)，updateMetroPassenger(afterX, afterY) 这三个方法来处理核心的算法，具体见代码。

* ***游戏界面的设计***
  * 考虑到屏幕的大小，显示在屏幕上的地图应为 **6\*6** 方形大小，这个大小**刚好能够触摸元素，不会产生误触**，而且作为休闲游戏，要锁定成 **竖直方向** 显示；接着是设计游戏的进度条，进度条应该有两个，一个显示已用时间，一个显示已运输人的总数；然后是按钮的位置，现在由于进度条在上方，游戏主互动区域在中间，那么按钮只能放在下方，这样界面总体显示效果较为平衡。
  * 6\*6 的方格如果利用 LinearLayout 的 layout_weight 属性两层嵌套，会有一定的 **性能问题** ，因此，使用谷歌官方推荐的 **ConstarintLayout** 较为合适。
  * 这些设计好后，进入关卡，游戏结束都要有相应的提示，所以用了属性动画来处理。
  * 游戏主色调应为让人舒适的 **浅色系** ，故使用了浅蓝色作为应用的整体背景色。

* ***主界面的设计***
  * 主界面应该尽量保持简洁，而且步骤要尽量少，所以应该把选关部分直接放到主界面。
  * 接着，既然标题和选关区占了屏幕的中间大部分的空间，其他辅助按钮应该放在四个角落，平衡界面。
  * 这四个按钮对应的功能很规范：设置、帮助、制作人员、退出。这些界面的编写也较为简单，在此不赘述。

## 主要技术

#### **1. 自定义实现**

* **NDK** 
  实现游戏关卡数据的保密和存储

* **util包下的自定义工具类**

| 类名 | 作用 |
|:-------------:|:-------------:|
| AnimationUtil | 自定义属性动画的快捷调用 |
| AppSaveDataSPUtil | SharedPreferences的快捷使用 |
| AppUtil | 判断版本名，判断App是否位于前台 |
| DisplayUtil | dp、dx、sp之间的转换 |
| LogAndToastUtil | Log和Toast的快捷使用 |

* **model包下的自定义Model**

| 类名 | 作用 |
|:-------------:|:-------------:|
| BlockBase | 抽象类基类，是其他model的父类 |
| Metro | 继承BlockBase ，用来存储地铁数据 |
| Station | 继承BlockBase ，用来存储地铁站数据 |
| Turnout | 继承BlockBase ，用来存储分岔道数据 |
| Usable | 继承BlockBase ，用来表示此 BlockBase 块可用 |

* **view包下的自定义View**

| 类名 | 作用 |
|:-------------:|:-------------:|
| BlockView | 与model包下的自定义Model相结合使用，显示对应的元素，重点在于onDraw绘制方法 |

* **jni包下的自定义JNI工具**

| 类名 | 作用 |
|:-------------:|:-------------:|
| CoreData | 核心数据工具类，用于读取游戏关卡数据 |

* **adapter包下的自定义Adapter**

| 类名 | 作用 |
|:-------------:|:-------------:|
| LevelListAdapter | 用于适配主界面的关卡RecyclerView ，通过 CoreData 读取游戏的基本数据 |

* **主包下的自定义类**

| 类名 | 作用 |
|:-------------:|:-------------:|
| BGMPlayer | 内部使用 MediaPlayer 的单例，用于播放自制的游戏背景音乐 |
| ConfigActivity | 实现处理设置音乐开关、语言选项的界面 |
| ConstantValue | 常量类，用于辅助其他类分类数据 |
| ExitDialog | 实现确认退出自定义的对话框 |
| GameActivity | 游戏主界面，处理几乎所有关于游戏的异步数据更新的事务 |
| GameTutorialActivity | 游戏教程主界面，仅在第一关有效 |
| GetResourceUtil | 通过读取当前语言选项使用正确的资源，不用后缀包的原因是要局部语言化 |
| HelpActivity | 实现帮助界面的显示 |
| ListActivity | 实现应用相关信息的显示 |
| MainActivity | 实现主界面的显示 |


#### **2. 开源库**

| 库名称 | 作用 |
|:-------------:|:-------------:|
| ButterKnife | 进行控件绑定和点击事件处理 |
| ConstraintLayout | 实现游戏界面复杂的控件关系 |
| RxJava2 | 异步处理地铁行驶事件 |
| RxJava LifeCycle | 处理RxJava生命周期 |
| RoundCornerProgressBar | 实现游戏主界面上方的ProgressBar控件 |
| AndroidAnimations | 特殊View属性动画处理 |
| RecycleView + CardView + CarouselLayoutManager | 实现主界面选择游戏关卡UI |

## 应用范围

这是一款 **休闲益智游戏** ，适合闲暇之余随手打开玩一玩，放松玩家的心情，也能让有好胜心的玩家面临 **一定难度的挑战** 。


## 优势分析
* 应用较小，不过30M，而且核心基础部分已经编写完毕，即使再增加数十个关卡，应用大小也不会有很大变化，因为每个关卡的数据 **只有几个字符串** 。
* 玩法简单，任何人都能 **轻松上手** ，但是难度却会逐级上升， **简单的规则能有复杂的玩法** 。


## 用户群
所有对休闲游戏有兴趣的玩家，老少皆宜。


## 创意点
* UI 简洁，没有任何让人感觉有累赘的地方，跟很多同类益智休闲游戏相比 **更容易上手** 。
* **自制 BGM** ，更符合游戏环境，让玩家更为投入。
* 一般选择关卡界面是生硬的列表和分页，但这个游戏选关是 **无限滚动立体列表** ，交互更具吸引力和创新性。
* 使用 RxJava 异步处理库，并与 RxJava LifeCycle 相结合，让程序更为
 **高效和可靠** 。
