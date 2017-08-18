package model;

import com.jfinal.kit.Prop;
import com.jfinal.kit.PropKit;
import com.jfinal.plugin.activerecord.ActiveRecordPlugin;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Record;
import com.jfinal.plugin.druid.DruidPlugin;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

/**
 * @description:
 * @author: youyinnn
 * @date: 2017/8/18
 */
public class DbAndRecord {

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

    /**
     * Db类和配套Record类提供在Model类以外的更丰富的数据库操作功能
     *
     * Record类相当于通用的Model
     * Db相当于表映射和表操作
     *
     * 更为自由 也更为方便
     * */
    @Test
    public void testDbAndRecord(){
        Record account = new Record();
        account.set("username", "Dee")
                .set("balance", 1050);

        //Db.save("account", account);

        // 以下举例一些常用方法

        //Db.deleteById("account", 3);
        account = Db.findById("account", 6);
        System.out.println(account);

        List<Record> records = Db.find("select * from account");
        System.out.println(records);

        //boolean succeed = Db.tx(new IAtom() {
        //    @Override
        //    public boolean run() throws SQLException {
        //        int count1 = Db.update("update account set balance = ? where id = ?", 200, 1);
        //        int count2 = Db.update("update account set balance = ? where id = ?",300,10);
        //        return count1 == 1 && count2 == 2;
        //    }
        //});

        // lambda
        boolean succeed = Db.tx(() -> {
            int count1 = Db.update("update account set balance = ? where id = ?", 1200, 1);
            System.out.println(count1);
            int count2 = Db.update("update account set balance = ? where id = ?",300,3);
            System.out.println(count2);

            // 只有返回true才会做所有事务的提交 不然就不提交或者回滚
            // 注意 即使你给出的count是1 只要你返回0 那就不会提交事务
            return count1 == 1 && count2 == 2;
        });

        System.out.println(succeed);
    }
}
