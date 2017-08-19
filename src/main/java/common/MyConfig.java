package common;

import com.jfinal.config.*;
import com.jfinal.kit.Prop;
import com.jfinal.kit.PropKit;
import com.jfinal.kit.StrKit;
import com.jfinal.plugin.activerecord.ActiveRecordPlugin;
import com.jfinal.plugin.activerecord.dialect.MysqlDialect;
import com.jfinal.plugin.druid.DruidPlugin;
import com.jfinal.template.Engine;
import controller.HelloController;
import model.Account;
import model.Book;

/**
 * @description:
 * @author: youyinnn
 * @date: 2017/8/14
 */
public class MyConfig extends JFinalConfig {



    /**
     * 此方法用来配置JFinal常量
     * */
    @Override
    public void configConstant(Constants constants) {
        // 如这个配置会对每次的请求输出请求信息 *开发者模式*
        constants.setDevMode(true);

        /*
        * 这个参数设置url中携带多个参数之间的分隔符 默认是“-”
        * 在Controller中可以通过getPara(int index)分别取出这些值
        * */
        //constants.setUrlParaSeparator("&");

        /*
        * PropKit工具类用来操作外部配置文件
        * 可以在任意时空使用
        * 第一次使用use加在的配置会成为主配置
        * 可以通过PropKit.get(...)直接取值
        * 这个方法加载配置文件内容后会讲数据存在内存里面
        * 可以通过PropKit.useless(...)来清除内容
        * */
        //PropKit.use("firstConfig.txt");
        //
        //constants.setDevMode(PropKit.getBoolean("devMode"));
    }

    /**
     * 此方法用来配置JFinal访问的路由
     * */
    @Override
    public void configRoute(Routes routes) {
        /*
        * 这个方法用于为该Routes内部的所有Controller设置视图渲染时的基础路径
        *   基础路径：
        *       Routes.add(controllerKey, controllerClass, viewPath)方法传入的viewPath
        *       Controller.render(view)方法传入的view
        *   联合组成最终的视图路径
        *   finalView = baseViewPath + viewPath + view
        *
        * 注意：当view以“/”打头时表示绝对路径 baseViewPath 和 viewPath 会被忽略
        * */
        routes.setBaseViewPath("view");

        //routes.addInterceptor(new FrontInterceptor);

        /*
        * Routes add(String controllerKey, Class<? extends Controller> controllerClass, String viewPath)
        * Routes add(String controllerKey, Class<? extends Controller> controllerClass)
        *   这是添加路由的方法
        *       参数1 controllerKey：指访问某个Controller所需要的字符串 唯一对应
        *       参数2 controllerClass：Controller的类
        *       参数3 viewPath：该Controller返回的视图的相对路径 未指定的时候默认是controllerKey
        *
        *   路由规则：
        *           url组成                           访问目标
        *       controllerKey                   Controller.index()
        *       controllerKey/method            Controller.method()
        *       controllerKey/method/v0-v1      Controller.method() 所带参数值为：v0-v1
        *       controllerKey/v0-v1             Controller.index() 所带参数值为：v0-v1
        *
        *   规则指出：JFinal访问一个确切的Action需要使用controllerKey和method来精确定位
        *   urlPara是为了能在url中携带参数
        *
        *   注意：
        *       1、controllerKey、method、urlPara三部分必须使用“/”分隔
        *       2、controllerKey自身也可以包含正斜杠 如“/admin/article” 实质上实现了struts2的命名空间
        * */
        routes.add("/se/hello", HelloController.class, "");

        /*
        * 我们把前后端的路由拆分后在这里合并起来
        * 这样的方式让JFinalConfig文件更加简洁
        * 同时也方便团队开发
        * 每个模块的开发团队都不需要同时修改JFinalConfig文件
        * 避免版本冲突
        * */
        //routes.add(new FrontRoutes());
        //routes.add(new AdminRoutes());
    }

    /**
     * 此方法用来配置模板引擎
     * 这个engine配置是针对render所配置的 和sql的engine无关
     * */
    @Override
    public void configEngine(Engine engine) {
        /*
        * setDevMode相当于热加载功能
        * 该模式下的模板文件的修改都会及时生效
        * 如果配置 则默认采用configConstant中的setDevMode 如果都不配置就是false
        * */
        engine.setDevMode(true);
        engine.addSharedFunction("/common/_layout.html");

        /*
          模板引擎语法
          */

        /*
        * 1、属性访问：user.name
        *
        *   - 假如user.getName()存在 则调用
        *   - 假如user为Model子类 则调用user.get("name")
        *   - 假如user为Record 则调用user.get("name")
        *   - 假如user为Map 则调用user.get("name")
        *   - 假如user具有public修饰过的name 则去user.name
        *
        * 还支持数组长度取值如array.length
        * */

        /*
        * 2、方法调用
        *
        * 模板表达式中可以直接调用对象所有的public方法
        * 方法调用还支持可变参数
        *
        *   #("ABCDEF".substring(0,2))
        *   #(girl.getAge())
        *   #(map.get("key))
        * */

        /*
        * 3、静态属性访问和静态方法调用
        *
        * 类名加双冒号加静态属性名或者方法名就可以访问
        * 这种方式可以避免在模板中使用常量 减少代码重构
        *
        *   #if(x.status == com.jfinal.club.common.model.Account::STATUS_LOCK_ID)
        *       <span>已锁定<span>
        *   #end
        *
        *   #if(com.xxx.xxx.ClassName::staticMethodName(args))
        *       ...
        *   #end
        * */

        /*
        * 4、空合并安全取值操作符
        *
        *   value ?? "哈哈“
        *
        * 如果前面的value值为空 那么整个表达式就取后面的字符串值“哈哈”
        * 该操作符的优先级高于所有数学计算运算符 低于单目运算符
        * */

        /*
        * 5、单引号字符串
        * 模板表达式中可以使用单引号引用表达字符串 可以和双引号协同工作
        * */

        /*
        * 6、逗号表达式
        *
        *   1+2,3*4
        *
        * 整体表达式的值会取逗号后的值 但是逗号前的值也会执行
        * */

        /*
        * 7、Map增强
        *
        * 在模板中 map定义使用大括号 元素定义使用key:value定义 用逗号分隔
        * 其中key只能是string类型 不管有没有引号 所以get方式取值的时候只能用双引号取值
        *
        *   #set(map = {k1 : 123 , 'k2' : "abc" , "k3" : object})
        *   #(map.k1)
        *   #(map.get["k2"])
        * */

        /*
        * 8、注释
        *
        *   ###这是单行注释
        *
        *   #--
        *       这是多行注释
        *   --#
        * */

        /*
         模板引擎指令
         */

        /*
        * 1、输出指令
        *
        * 可以输出前面的任何表达式值
        *
        *   #(...)
        *   #(value)
        *   #(value ??)
        *   #(value ?? "Hello")
        *   #(obj.method(), null)
        *
        *   第二行如果为null会报错 所以有第三行的写法
        *   第五行使用逗号表达式 什么都不输出 仅仅执行前面的方法来完成某些操作
        * */

        /*
        * 2、if指令
        *
        * (1)
        *   #if(condition)
        *       ...
        *   #end
        *
        * (2)
        *   #if(c1)
        *       ...
        *   #elseif(c2)
        *       ...
        *   #elseif(c3)
        *       ...
        *   #else
        *       ...
        *   #end
        * */

        /*
        * 3、for指令
        *
        * (1)
        *   #for(x : list)
        *       #(x.field)
        *   #end
        *
        * (2)
        *   #for(x : map)
        *       #(x.key)
        *       #(x.value)
        *   #end
        *
        * 当被迭代的目标为null的时候 for指令会直接跳过null 不需要做null判断
        *
        * 对for指令的状态进行获取
        *
        *   #for(x : listA)
        *       #(for.index)
        *       #(x.field)
        *
        *       #for(x : listB)
        *           #(for.outer.index)
        *           #(for.index)
        *           #(x.field)
        *       #end
        *   #end
        *
        * 注意：
        *   1、for指令中的x有自己的作用域 和for循环嵌套是一样的
        *   2、for.outer是固定用法 用来获取外层for指令的状态
        *
        * 所有状态如下：
        *   #for(x : list)
        *       #(for.size)     被迭代对象的size值
        *       #(for.index)    从0开始的下标值
        *       #(for.count)    从1开始的记数值
        *       #(for.first)    是否为第一次迭代
        *       #(for.last)     是否为最后一次迭代
        *       #(for.odd)      是否为奇数迭代
        *       #(for.even)     是否为偶数迭代
        *       #(for.outer)    引用上层for指令状态
        *   #end
        *
        * 除了map和list for指令还可以用于Collection、Iterator、Array、普通数组、Enumeration等 用法完全一样
        *
        * for指令还支持else语句 当for指令迭代次数为0的时候 会执行else语句内的语句
        *   #for(x : list)
        *       ...
        *   #else
        *       ...
        *   #end
        *
        * 除了增强型的写法 还可以使用常规写法 和常规不同的是 变量声明不需要类型 但是这种写法不支持for.size和for.last这两个状态
        *   #for(i = 0 ; i < 100 ; i++)
        *       #(i)
        *   #end
        *
        * for指令还支持continue、break指令
        * */

        /*
        * 4、set指令
        *
        * set指令用于声明变量的同时为其赋值 也可以为已存在变量赋值
        * set指令只接收赋值表达式和逗号表达式的赋值
        *
        *   #set(x = 123)
        *   #set(a = 1 , b = 2 , c = a + b)
        *   #set(array[0] = 123)
        * */

        /*
        * 5、include指令
        *
        * 用于将外部模板内容包含进来 被包含的内容会被解析为当前模板的一部分
        *   #include("_sidebar.html")
        *
        * include指令接收一个String型参数 当不以“/”字符打头的时候 包含模板和当前模板同目录
        * 当以“/”打头的时候 包含模板将以baseTemplatePath为相对路径取寻找文件
        * 默认的baseTemplatePath为PathKit.getWebRootPath()
        *
        * include指令还可以无限制传入赋值表达式作为参数 如下有“_hot_list.html"
        *   <div class="hot-list">
        *       <h3>#(title)</h3>
        *       <ul>
        *           #for(x : list)
        *               <li>
        *                   <a href="#(url)/#(x.id)">
        *                       #(x.title)
        *                   </a>
        *               </li>
        *           #end
        *       </ul>
        *   </div>
        *
        * 可以按照以下方式引用
        *   #include("_hot_list.html", title="热门栏目", list=projectList, url="/project")
        *   #include("_hot_list.html", title="热门新闻", list=newsList, url="/news")
        * 分别传入了不同的值生成不同的模板
        * */

        /*
        * 6、render指令
        *
        * 和include指令一样 有两点不同
        *
        *   - render指令支持动态化模板参数
        *       #render(temp)
        *           temp可以是任意表达式 而include只能是字符串
        *   - render指令中#define定义的模板参数只在其子模板中有效 在父模板中无效
        * */

        /*
        * 7、define指令
        *
        * define指令是模板引擎的主要扩展方式之一 define指令可以定义模板函数
        * 通过define指令 可以将需要被重用的模板片段定义成一个个template function
        * 在调用的时候就可以传入参数实现多变功能
        *
        * 具体看_layout.html文件
        * */

        /*
        * Shared Method拓展
        *
        * JF模板引擎可以直接使用java类中的任意public方法 并且被使用的java类不需要继承和实现任何接口 完全无耦合
        *
        * addSharedMethod方法把对象添加进引擎方法共享域中
        * 下面的例子调用StrKit类的静态方法isBlank
        *
        *   #if(isBlank(nickName))
        *       ...
        *   #end
        * */
        engine.addSharedMethod(new StrKit());

        /*
        * Shared Object拓展
        *
        * 添加对象到模板引擎共享域中 可以直接全局使用
        *
        *   #(RESOURCE_HOST)
        *
        *   #if(sk.isBlank(nickName))
        *       ...
        *   #end
        * */
        engine.addSharedObject("RESOURCE_HOST", "http://res.jfinal.com");
        engine.addSharedObject("sk", new StrKit());
    }

    /**
     * 此方法用来配置JFinal的插件
     * 如配置Druid等
     * JFinal插件架构是最主要的拓展方式之一
     * */
    @Override
    public void configPlugin(Plugins plugins) {
        // 非第一次使用PropKit.use(...)加载的配置 需要通过每次使用use类指定配置文件名后再来取值
        String username = PropKit.use("secondConfig.txt").get("username");

        // 使用得到的username

        // 也可以先得到一个Prop对象 再通过该对象取值
        Prop p = PropKit.use("secondConfig.txt");
        String password = p.get("password");

        Prop dp = PropKit.use("db.properties");
        String jdbc_username = dp.get("jdbc.username");
        String jdbc_url = dp.get("jdbc.url");
        String jdbc_password = dp.get("jdbc.password");
        String jdbc_driver = dp.get("jdbc.driverClass");

        // druid 的数据源插件
        DruidPlugin druidPlugin = new DruidPlugin(jdbc_url,jdbc_username,jdbc_password,jdbc_driver);

        // ActiveRecord 的支持插件
        ActiveRecordPlugin activeRecordPlugin = new ActiveRecordPlugin(druidPlugin);

        /*
        * addMapping方法建立了数据库表名到Model的映射关系
        * 第一行代码映射的表 默认主键名字为“id”
        * 第二行代码指定非默认格式的主键
        *
        * ActiveRecordPlugin可以独立于JFinalWeb项目 不过在使用前需要手动start 详情见Account.java
        * */
        activeRecordPlugin.addMapping("account", Account.class);
        activeRecordPlugin.addMapping("book", "book_id", Book.class);

        // 设置方言
        activeRecordPlugin.setDialect(new MysqlDialect());

        // 添加插件
        plugins.add(druidPlugin);
        plugins.add(activeRecordPlugin);
    }

    /**
     * 此方法用来配置项目的全局拦截器
     *      全局拦截所有的action请求
     *      除非使用了@Clear在controller中清除
     * 全局拦截器分为两种：
     *  1、控制层拦截器
     *  2、业务层拦截器
     *
     * 多级别拦截器的执行顺序如下：
     *  Global、Inject、Class、Method
     *
     * Clear注释：
     *  用于清除自身所处层级以上的拦截器
     *
     *  例：
     *      Clear声明在Method层的时候 会清除Global、Inject、CLass级别的拦截器
     *      Clear声明在Class层的时候 会清除Global、Inject级别的拦截器
     *
     *      Clear注释还可以设置参数 用于清除指定的"上层"拦截器 @Clear({AAA.class,BBB.class})
     * */
    @Override
    public void configInterceptor(Interceptors interceptors) {
        //interceptors.add(new AuthInterceptor);

        // 全局的控制层拦截器
        //interceptors.addGlobalActionInterceptor(...);

        // 全局业务层拦截器
        //interceptors.addGlobalServiceInterceptor(...);
    }

    /**
     * 此方法用来配置项目的Handler
     * */
    @Override
    public void configHandler(Handlers handlers) {

    }

    /**
     * JFinal会在系统启动完成之后回调afterJFinalStart方法
     * */
    @Override
    public void afterJFinalStart() {
        super.afterJFinalStart();
        System.out.println("JFinal Started!!*");
        //File file = new File("Start"+System.currentTimeMillis()+".txt");
        //try {
        //    FileOutputStream fo = new FileOutputStream(file);
        //} catch (FileNotFoundException e) {
        //    e.printStackTrace();
        //}

    }

    /**
     * JFinal会在系统关闭前回调beforeJFinalStop方法
     * 但是在Jetty环境下无法运行
     * */
    @Override
    public void beforeJFinalStop() {
        super.beforeJFinalStop();
        System.out.println("JFinal Stop!!***");
        //File file = new File("Stop"+System.currentTimeMillis()+".txt");
        //try {
        //    FileOutputStream fo = new FileOutputStream(file);
        //} catch (Exception e) {
        //    e.printStackTrace();
        //}
    }
}
