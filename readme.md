之前在看大部分的Android 富文本编辑几乎都是利用webview实现，所以，便有了做一个Android原生的富文本编辑器的主意。

##### Blog

[Android 写一个属于自己的富文本编辑器](https://blog.csdn.net/qq_15893929/article/details/88670198)

##### 样例
照惯例先秀一下图：

<img src=https://img-blog.csdnimg.cn/20190319190538313.png?640 width=32% />
&nbsp&nbsp&nbsp
<img src=https://img-blog.csdnimg.cn/20190319191028673.png width=32% />
&nbsp&nbsp&nbsp
<img src=https://img-blog.csdnimg.cn/20190319192525113.png  width=32% />

该富文本编辑器样式仿照印象笔记的Android版，绘制层实现基于Android的span样式。

##### 目前已经实现的功能：
1. 粗体、斜体、下划线、删除线、上下标、背景色字体样式;
2. 分割线、缩进、有序列表、无序列表、复选框行样式;
3. 支持插入本地图片;
4. 支持插入网络图片;
4. 支持图片预览;
5. 支持撤销和反撤销;
6. 支持本地持久化、支持增删改;
7. 支持编辑模式和预览模式

##### 具体的实现：
主要的实现在于编辑页面，直接是继承自EditText加以改造的（偷懒），但是如果想实现一个商业级别的编辑器，建议使用StaticLayout和自定义View，但是需要考虑的东西比较多，例如输入法和排版布局、选区管理绘制、各类点击事件。

![大致模块](https://img-blog.csdnimg.cn/20190319203551373.png?x-oss-process=image/watermark,type_ZmFuZ3poZW5naGVpdGk,shadow_10,text_aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L3FxXzE1ODkzOTI5,size_16,color_FFFFFF,t_70)
* NoteEditText 继承自 EditText，NoteEditorManager管理基本逻辑；

* NoteEditorRender负责绘制，NoteLineSpanRender是行样式、NoteWordSpanRender是字体样式；

* NoteRevocationManager负责撤销与反撤销；

* NoteImageLoader 是图片加载库，之前想用Glide库，但是Glide不支持直接在UI线程获取缓冲区的Bitmap，所以简单写了一个基于OkHttp的图片加载，内部参照（抄）了Glide的思想，例如ImageView在宽高为0时如何加载图片、图片过大时候怎么处理。Glide太强大了，代码也好复杂；
后续还是要继续替换成Glide，可以通过自定义设置Glide缓冲池，这样外部就可以直接拿到缓冲区数据；

* converter 是简单地将富文本对象转成文本数据，或将文本数转成富文本对象的模块；

* dao 数据库层；

* route 是在利用APT和借鉴OkHttp责任链模式仿写的一个跳转路由的功能；
只是自己学习所写的一个小工具，完全可以去掉。


##### 后续计划:

这个版本更多的是将自己所学的一些知识的运用，只做了小一段时间，所以存留很多了bug和缺陷，后续会继续找时间修补。

想增加的内容：
1. 增加导入导出html
2. 完善图片池
3. 增加桌面小部件
4. 增加保存为图片
5. 支持超链接、引用更多样式


##### 附上
1. BackgroundColorSpan 背景色
2. ForegroundColorSpan 文本颜色（前景色）
3. RasterizerSpan 光栅效果
4. StrikethroughSpan 删除线
5. SuggestionSpan 相当于占位符
6. UnderlineSpan 下划线
7. AbsoluteSizeSpan 绝对大小（文本字体）
8. DynamicDrawableSpan 设置图片，基于文本基线或底部对齐。
9. ImageSpan 图片
10. RelativeSizeSpan 相对大小（文本字体）
11. ReplacementSpan 父类，一般不用
12. URLSpan 文本超链接
13. StyleSpan 字体样式
14. SubscriptSpan 下标
15. SuperscriptSpan 上标
16. TextAppearanceSpan 文本外貌（包括字体、大小、样式和颜色）
17. TypefaceSpan 文本字体
