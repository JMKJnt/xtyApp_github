package com.example.aiden.xtapp.db;




import org.xutils.DbManager;
import org.xutils.x;

/**
 * @time 2016/10/31-16:41
 * @desc
 */

public class XDBManager {
    private static final String TAG = "XDBManager";

    private static XDBManager instance;
    private static final int DB_INIT_VERSION = 3;
    public static final String DB_INIT_NAME = "init.db";


    /**
     * 用来存储初始化数据
     */
    private final DbManager initDb;

    private XDBManager() {
        initDb = createInitDb();
    }



    public static synchronized XDBManager getInstance() {
        if (instance == null) {
            instance = new XDBManager();
        }
        return instance;
    }

    private DbManager createInitDb() {
        DbManager.DaoConfig daoConfig = new DbManager.DaoConfig()
                .setDbName(DB_INIT_NAME)
                .setDbVersion(DB_INIT_VERSION)
                .setDbOpenListener(new DbManager.DbOpenListener() {
                    @Override
                    public void onDbOpened(DbManager db) {
                        // 开启WAL, 对写入加速提升巨大
                        db.getDatabase().enableWriteAheadLogging();
                    }
                })
                .setDbUpgradeListener(new InitDbUpdateListener());

        return x.getDb(daoConfig);
    }



    public DbManager getInitDb() {
        return initDb;
    }
}
