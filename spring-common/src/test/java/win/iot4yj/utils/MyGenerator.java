package win.iot4yj.utils;

import com.baomidou.mybatisplus.annotation.DbType;
import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.generator.AutoGenerator;
import com.baomidou.mybatisplus.generator.InjectionConfig;
import com.baomidou.mybatisplus.generator.config.DataSourceConfig;
import com.baomidou.mybatisplus.generator.config.FileOutConfig;
import com.baomidou.mybatisplus.generator.config.GlobalConfig;
import com.baomidou.mybatisplus.generator.config.PackageConfig;
import com.baomidou.mybatisplus.generator.config.StrategyConfig;
import com.baomidou.mybatisplus.generator.config.TemplateConfig;
import com.baomidou.mybatisplus.generator.config.po.TableFill;
import com.baomidou.mybatisplus.generator.config.po.TableInfo;
import com.baomidou.mybatisplus.generator.config.rules.NamingStrategy;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import org.junit.Test;

public class MyGenerator {

    //作者
    private static final String AUTHOR = "joyang";

    //需要自动填充的字段
    private static final List<TableFill> TABLE_FILLS = Stream.of("create_time", "update_time")
        .map(e -> new TableFill(e, FieldFill.INSERT_UPDATE)).collect(Collectors.toList());

    //要反向生成的表
    private static final String[] TABLE_NAMES = new String[]{"t_user"};
    //反向生成表的前缀，在生成实体类的时候会去掉
    private static final String[] TABLE_PREFIXES = new String[]{"t_"};
    //主键生成策略
    private static final IdType ID_TYPE = IdType.INPUT;

    //模块名，比如用户模块、角色模块
    private static final String MODULE_NAME = null;

    /**
     * 生成文件的存放路径，都是有默认值的
     */
    private static final String PARENT_PACKAGE = "win.iot4yj";
    private static final String CONTROLLER_PACKAGE = "controller";
    private static final String SERVICE_PACKAGE = "service";
    private static final String DAO_PACKAGE = "mapper";
    private static final String MAPPER_PACKAGE = "mapper";
    private static final String ENTITY_PACKAGE = "entity";

    //反向生成文件输出目录
    private static final String OUTPUT_DIR = System.getProperty("user.dir") + "/src/main/java";

    //数据库配置
    private static final String DB_URL = "jdbc:mysql://localhost:13306/spring_study?characterEncoding=utf8";
    private static final String DB_DRIVER = "com.mysql.jdbc.Driver";
    private static final String DB_USERNAME = "root";
    private static final String DB_PASSWORD = "walp1314";


    @Test
    public void testGenerator() {
        //1、全局配置
        GlobalConfig config = new GlobalConfig()
            //开启AR模式
            .setActiveRecord(true)
            //设置实体属性swagger
            .setSwagger2(true)
            //主键策略
            .setIdType(ID_TYPE)
            //设置作者
            .setAuthor(AUTHOR)
            //生成完后不要打开文件夹
            .setOpen(false)
            //生成路径(一般都是生成在此项目的src/main/java下面)
            .setOutputDir(OUTPUT_DIR)
            //第二次生成会把第一次生成的覆盖掉
            .setFileOverride(true)
            //主键策略
            //.setIdType(IdType.AUTO)
            //生成的service接口名字首字母是否为I，这样设置就没有I
            .setServiceName("%sService")
            // 自定义文件命名，注意 %s 会自动填充表实体属性！
            //.setEntityName("%sEntity")
            //生成resultMap
            .setBaseResultMap(true)
            //在xml中生成基础列
            .setBaseColumnList(true);

        //2、数据源配置
        DataSourceConfig dataSourceConfig = new DataSourceConfig()
            //数据库类型
            .setDbType(DbType.MYSQL)
            .setDriverName(DB_DRIVER)
            .setUrl(DB_URL)
            .setUsername(DB_USERNAME)
            .setPassword(DB_PASSWORD);

        //3、策略配置
        StrategyConfig strategyConfig = new StrategyConfig()
            //开启全局大写命名
            .setCapitalMode(true)
            //表名字段名使用下划线
            //.setDbColumnUnderline(true)
            //            //下划线到驼峰的命名方式
            .setNaming(NamingStrategy.underline_to_camel)
            //表名前缀，生成实体的时候会去除
            .setTablePrefix(TABLE_PREFIXES)
            //不使用lombok
            .setEntityLombokModel(false)
            //逆向工程使用的表，可以有多个
            .setInclude(TABLE_NAMES)
            // 自定义实体父类，默认为 com.baomidou.demo.TestEntity
            // .setSuperEntityClass("com.baomidou.demo.TestEntity")
            //controller使用@RestController注解
            .setRestControllerStyle(true)
            //需要自动填充的字段
            .setTableFillList(TABLE_FILLS);

        //4、包名策略配置
        PackageConfig packageConfig = new PackageConfig()
            .setModuleName(MODULE_NAME)
            //设置包名的parent,，但是这样设置之后无法将*mapper.sql添加到resources目录中，但是如果不设置则会使用默认的父类包名
            .setParent(PARENT_PACKAGE)
            //下面这些设置都是有默认值的，如果不想生成，可以设置为null
            .setMapper(DAO_PACKAGE)
            .setService(SERVICE_PACKAGE)
            .setController(CONTROLLER_PACKAGE)
            .setEntity(ENTITY_PACKAGE)
            //设置xml文件的目录
            .setXml(MAPPER_PACKAGE);

        InjectionConfig injectionCfg = new InjectionConfig() {
            @Override
            public void initMap() {
                //自定义
            }
        };
        //自定义输出配置，下面是xml的自定义，这里只是通过这种方式才能将xml输出到resources中去
        List<FileOutConfig> focList = new ArrayList<>();
        focList.add(new FileOutConfig("/templates/mapper.xml.vm") {
            @Override
            public String outputFile(TableInfo tableInfo) {
                // 自定义输出文件名 ， 如果你 Entity 设置了前后缀、此处注意 xml 的名称会跟着发生变化！！
                return System.getProperty("user.dir") + "/src/main/resources/mapper/"
                    + tableInfo.getEntityName() + "Mapper.xml";
            }
        });
        //controller模板配置，添加了swagger注解
        focList.add(new FileOutConfig("/templates/controller.java.vm") {
            @Override
            public String outputFile(TableInfo tableInfo) {
                return System.getProperty("user.dir") + "/src/main/java/win/iot4yj/controller/"
                    + tableInfo.getEntityName() + "Controller.java";
            }
        });
        injectionCfg.setFileOutConfigList(focList);

        //5、整合配置
        AutoGenerator autoGenerator = new AutoGenerator()
            //这里设置了setXml会将之前的覆盖掉
            .setTemplate(new TemplateConfig().setXml(null).setController(null))
            //设置非默认的模板引擎，注意：需要配置相关freemarker配置
            //.setTemplateEngine(new FreemarkerTemplateEngine())
            .setCfg(injectionCfg)
            .setGlobalConfig(config)
            .setDataSource(dataSourceConfig)
            .setStrategy(strategyConfig)
            .setPackageInfo(packageConfig);

        //6、执行
        autoGenerator.execute();
    }
}
