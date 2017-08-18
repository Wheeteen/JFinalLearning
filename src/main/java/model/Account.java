package model;

import com.jfinal.kit.Prop;
import com.jfinal.kit.PropKit;
import com.jfinal.plugin.activerecord.ActiveRecordPlugin;
import com.jfinal.plugin.activerecord.Model;
import com.jfinal.plugin.druid.DruidPlugin;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

/**
 * @description:
 * @author: youyinnn
 * @date: 2017/8/18
 */
/**
 * Model是ActiveRecord中最重要的组件之一 从当MVC中的Model部分
 * 以下的Account类通过继承Model类 立即拥有众多方便操作数据库的方法
 * 无需定义属性 无需定义getter和setter方法
 * */
public class Account extends Model<Account> {

    /**
     * 声明dao对象是为了方便查询操作 非必须配置
     * 该对象是全局共享的 只能用于数据库查询 不能用于数据承载 如果要使用数据承载 就必须得new对象之后使用set方法
     */
    public static final Account dao = new Account().dao();

    /**
     * 独立启动ActiveRecordPlugin和DruidPlugin
     * */
    @Before
    public void b(){
        Prop dp = PropKit.use("db.properties");
        String jdbc_username = dp.get("jdbc.username");
        String jdbc_url = dp.get("jdbc.url");
        String jdbc_password = dp.get("jdbc.password");
        String jdbc_driver = dp.get("jdbc.driverClass");

        // druid 的数据源插件
        DruidPlugin druidPlugin = new DruidPlugin(jdbc_url,jdbc_username,jdbc_password,jdbc_driver);

        // ActiveRecord 的支持插件
        ActiveRecordPlugin activeRecordPlugin = new ActiveRecordPlugin(druidPlugin);
        activeRecordPlugin.addMapping("account", Account.class);
        activeRecordPlugin.addMapping("book", "book_id", Book.class);

        /*
        * 为了让ActiveRecordPlugin和DruidPlugin独立于Web项目启动 需要手动start插件就可以使用
        * */
        druidPlugin.start();
        activeRecordPlugin.start();
    }

    @Test
    public void testSave(){
        Account account = new Account().set("username", "Jame").set("balance", 500);
        account.save();
    }

    @Test
    public void testDelete(){
        Account.dao.deleteById(2);
    }

    @Test
    public void testFindAndUpdate(){
        Account.dao.findById(1).set("username", "Cate").update();
    }

    @Test
    public void testFind(){
        Account account = Account.dao.findByIdLoadColumns(1, "username, balance");
        Account account1 = Account.dao.findById(1);
        System.out.println(account);
        System.out.println(account1);

        System.out.println(account.getStr("username"));
        System.out.println(account.getInt("balance"));

        List<Account> accountList = Account.dao.find("select * from account");
        System.out.println(accountList);
    }
}
