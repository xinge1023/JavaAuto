#JavaAuto
####主要功能
> **利用java语言的反射机制实现特定业务逻辑功能，包括对j2se和j2ee的对象.属性类型进行填装,数据持久化,并实现简单的增删改查功能，可提高开发效率。**

**使用本util包工具包请按照下面几步用以匹配自己的项目。**

第一步、创建自己的项目并创建项目entity(其他名字也可以,但是需修改SystemVariables类中的Entity_Package属性)包下的实体类(id属性作为主键应该是必须的)。

第二步、把此util工具包放入项目下的src目录以供使用。

第三步、调用util包下的Util.java中的`CreateTable()`或者`DropAndRecreateTable()`根据entity下的实体类生成数据库。

第四步、执行util包中的AutoCreate.java类中的main方法生成BeanManagr.java中的内容以及Methods中的内容。

第五步、若进行servlet开发则servlet最好继承util包下的Util类，以方便使用util包下提供的增删改查方法(不继承则通过类名去调用)，并且servlet中的doGet或者doPost方法第一句应加入`initMethod(request, response)`初始化方法以接受并处理前台的传值。

**servlet使用中说明：**

1. 前台传值若是"对象.属性"类型,在servlet中则通过`initMethod(request, response)`方法自动set属性给对象，后台通过get对象名方法获取对象。示例(前台user.name，后台获取对象则是getUser();)。

2. 前台传值支持"对象.对象.对象...对象.属性"类型。

3. 前台传值若是属性类型在servlet中则按照通用的`requests.getAttribute("属性名")`获取。

**工具包清单:**

AutoCreate:自动生成项目必须的java文件类。

BeanManager:对象管理器(负责创建静态对象方法供全局使用)，由AutoCreate类生成。

DB:数据库连接类 。

Methods:基础的删查方法，由AutoCreate类生成。

SystemVariables:包含关于本项目的一些全局属性，有需要的类可以继承该接口。

Util:提供基础的一些方法。

SqlPropertyMapping.properties:本文件默认使用的是mysql数据库，通过对java语言基本属性和mysql数据库属性的映射完成对数据库的操作(其他数据库可通过修改此配置文件以对应java基本属性)。

##注：此工具仅包含基础的增删改查方法，如有复杂的要求请自行修改代码或扩展。

如使用过程中发现bug请尽快与我联系，邮箱xinge1023@163.com。
