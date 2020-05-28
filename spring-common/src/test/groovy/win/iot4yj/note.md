# Spock 使用

## 基础

| Spock               | Junit            |
| ------------------- | ---------------- |
| Specification       | Test class       |
| setup()             | @Before          |
| cleanup()           | @After           |
| setupspec()         | @Beforeclass     |
| cleanupsepc()       | @Afterclass      |
| Exception condition | @Test(expectd=…) |
| Interaction         | Mock expectation |
| Condition           | Assertion        |
| Data-driven feature | Theory           |
| Feature             | Test             |
| Feature method      | Test method      |

以上是 `Spock` 与 `Junit` 的一些对应关系。



首先要说明下依赖和相关问题：

```groovy
//spock测试依赖:使用3.x版本的时候报错，而2.x版本不支持1.8lambda表达式
//    compile 'org.codehaus.groovy:groovy-all:3.0.4'
//    testCompile 'org.spockframework:spock-spring:2.0-M2-groovy-3.0'
//    testCompile "org.spockframework:spock-core:2.0-M2-groovy-3.0"
compile 'org.codehaus.groovy:groovy-all:2.4.15'
testCompile 'org.spockframework:spock-spring:1.0-groovy-2.4'
testCompile "org.spockframework:spock-core:1.0-groovy-2.4"
//enables mocking of classes (in addition to interfaces)
testCompile 'cglib:cglib-nodep:3.3.0'
//enables mocking of classes without default constructor (together with CGLIB)
testCompile 'org.objenesis:objenesis:3.0.1'
```

有人使用`3.x`版本成功了：`https://github.com/spockframework/spock/issues/1067`。可能原因是当我们引入`org.codehaus.groovy:groovy-all:3.0.4`的时候实际上`gradle`导入的却是`2.5`版本，我们可以将低版本剔除，然后分别手动导入，如：

```groovy
dependencies {
    testImplementation('org.spockframework:spock-core:2.0-M2-groovy-3.0') {
        exclude group: 'org.codehaus.groovy'
    }
    testImplementation('org.codehaus.groovy:groovy:3.0.2')
    testImplementation('org.codehaus.groovy:groovy-sql:3.0.2') //if needed
    ...
}
```



同时需要指定包以及添加`groovy`插件：

```groovy
apply plugin: 'groovy'
sourceSets {
    main {
        java {
            srcDir 'src/main/java'
        }

        resources {
            srcDir 'src/main/resources'
        }
    }

    test {
        java {
            srcDir 'src/test/java'
            // 需要指定，否则无法识别
            srcDir 'src/test/groovy'
        }

        resources {
            srcDir 'src/test/resources'
        }
    }
}
```

一个简单的例子：

```groovy
package win.iot4yj.service

import spock.lang.Specification


class UserServiceTest extends Specification {

    // 初始化
    def setupSpec() {
        println ">>>>>>   setupSpec"
    }
    def setup() {
        println ">>>>>>   setup"
    }
    def cleanup() {
        println ">>>>>>   cleanup"
    }
    def cleanupSpec() {
        println ">>>>>>   cleanupSpec"
    }

    // 测试标题不支持中文
    def "spock test"() {
        given:
        def a = 1
        def b = 2

        expect:
        a > b

        println "test method finished!"
    }

}
```



