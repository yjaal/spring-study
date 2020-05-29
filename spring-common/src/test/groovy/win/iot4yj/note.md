# Spock 使用

官网：`http://spockframework.org/spock/docs/1.3/index.html`

## 基础

| `Spock`               | `Junit`            | 含义                                             |
| --------------------- | ------------------ | ------------------------------------------------ |
| `Specification`       | `Test class`       | 表示这是一个测试类                               |
| `setup()`             | `@Before`          | 测试方法执行之前执行，每个测试方法执行前都要执行 |
| `cleanup()`           | `@After`           | 测试方法执行之后执行，每个测试方法执行后都要执行 |
| `setupspec()`         | `@BeforeClass`     | 测试类执行之前执行，针对所有的测试方法执行一次   |
| `cleanupsepc()`       | `@AfterClass`      | 测试类执行之后执行，针对所有的测试方法执行一次   |
| `Exception condition` | `@Test(expectd=…)` | 检查被测方法是否抛出xx异常                       |
| `Interaction`         | `Mock expectation` |                                                  |
| `Condition`           | `Assertion`        |                                                  |
| `Data-driven feature` | `Theory`           |                                                  |
| `Feature`             | `Test`             | 一个单元测试方法                                 |
| `Feature method`      | `Test method`      |                                                  |

执行顺序：`@BeforeClass -> @Before -> @Test -> @After -> @AfterClass`。以上是 `Spock` 与 `Junit` 的一些对应关系。



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



### 几种语法组织形式

- given … expect …
- given … when … then …
- when … then …
- given … expect … where …
- expect … where …
- expect



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

    def "spock_test"() {
        given: "给定初始值"
        def a = 1
        def b = 2

        expect:
        a > b

        println "test method finished!"
    }

}
```



```groovy

@Title("测试的标题")
@Narrative("""关于测试的大段文本描述""")
@Subject(Adder)  //标明被测试的类是Adder
@Stepwise  //当测试方法间存在依赖关系时，标明测试方法将严格按照其在源代码中声明的顺序执行
class TestCaseClass extends Specification {  
    
  @Shared //在测试方法之间共享的数据
  SomeClass sharedObj
 
  def setupSpec() {
    //TODO: 设置每个测试类的环境
  }
 
  def setup() {
    //TODO: 设置每个测试方法的环境，每个测试方法执行一次
  }
 
  @Ignore("忽略这个测试方法")
  @Issue(["问题#23","问题#34"])
  def "测试方法1" () {
    given: "给定一个前置条件"
    //TODO: code here
    and: "其他前置条件"
 
 
    expect: "随处可用的断言"
    //TODO: code here
    when: "当发生一个特定的事件"
    //TODO: code here
    and: "其他的触发条件"
 
    then: "产生的后置结果"
    //TODO: code here
    and: "同时产生的其他结果"
 
    where: "不是必需的测试数据"
    input1 | input2 || output
     ...   |   ...  ||   ...   
  }
 
  @IgnoreRest //只测试这个方法，而忽略所有其他方法
  @Timeout(value = 50, unit = TimeUnit.MILLISECONDS)  // 设置测试方法的超时时间，默认单位为秒
  def "测试方法2"() {
    //TODO: code here
  }
 
  def cleanup() {
    //TODO: 清理每个测试方法的环境，每个测试方法执行一次
  }
 
  def cleanupSepc() {
    //TODO: 清理每个测试类的环境
  }
```



```groovy
//数据驱动
package win.iot4yj.service

import spock.lang.Specification
import spock.lang.Unroll


class SpockTest1 extends Specification {

    //数据驱动测试
    @Unroll
    def "数据驱动测试"(int a, int b, int c) {
        expect:
        Math.max(a, b) == c

        where:
        a | b | c
        1 | 3 | 3   //passes
        7 | 4 | 4   //fails
        0 | 0 | 0   //passes
    }
}
```



```groovy
// with，这样就不用每次都写pc.vendor == "Sunny"
def "offered PC matches preferred configuration"() {
  when:
  def pc = shop.buyPc()

  then:
  with(pc) {
    vendor == "Sunny"
    clockRate >= 2333
    ram >= 406
    os == "Linux"
  }
}
```

```groovy
//这里表示start等方法各自调用了一次
def service = Mock(Service) // has start(), stop(), and doWork() methods
def app = new Application(service) // controls the lifecycle of the service

when:
app.run()

then:
with(service) {
  1 * start()
  1 * doWork()
  1 * stop()
}
```



### `verifyAll`

这个和with有点类似

```groovy
def "offered PC matches preferred configuration"() {
  when:
  def pc = shop.buyPc()

  then:
  verifyAll(pc) {
    vendor == "Sunny"
    clockRate >= 2333
    ram >= 406
    os == "Linux"
  }
}

// 或者无参
expect:
    verifyAll {
        2 == 2
        4 == 4
}
```



### `given-and`

```groovy
given: "open a database connection"
// code goes here

and: "seed the customer table"
// code goes here

and: "seed the product table"
// code goes here
```

其实就是给出一些初始条件，通过`and`可以添加多个初始条件。



### 异常

```groovy
/**
* 测试一个方法抛出异常，可以使用try-catch在when语句块中捕获验证，但是写起来比较繁琐
* 所以，Spock测试框架中可以使用thrown()来表示这种现象，并且可以返回异常的实例
*/
def "test Thrown"() {
    when:
        int a = 1
    int b = 0
    int c = 2
    c = (a / b)
    then:
        def ex = thrown(Exception)
        ex instanceof ArithmeticException
    // ArithmeticException 异常时我们预料之中的
}

/**
* notThrown() 表示被测试的方法不会抛出某种异常
*/
def "HashMap accepts null key"() {
    given:
        def map = new HashMap()
        when:
        map.put(null, "elem")
    then:
        notThrown(NullPointerException)
}
```



### Hamcrest Matcher

```groovy
package win.iot4yj.service
import static spock.util.matcher.HamcrestSupport.expect
import static spock.util.matcher.HamcrestMatchers.closeTo

import spock.lang.Specification

class SpockTest1 extends Specification {

    def "test for closeTo()"() {
        given:
        def a = 101256
        when:
        def b = a / 10
        then:"b的值在200到10000之间"
        expect b, closeTo(10000, 200)
    }
}
```







## Data Driven Testing

```groovy
class MathSpec extends Specification {
  def "maximum of two numbers"() {
    expect:
    // exercise math method for a few different inputs
    Math.max(1, 3) == 3
    Math.max(7, 4) == 7
    Math.max(0, 0) == 0
  }
}
```

这里相当于我们有多个数据需要测试同样的逻辑，这样写就比较麻烦了



### Data Tables

```groovy
class MathSpec extends Specification {
  def "maximum of two numbers"(int a, int b, int c) {
    expect:
    Math.max(a, b) == c

    where:
    a | b | c
    1 | 3 | 3
    7 | 4 | 7
    0 | 0 | 0
  }
}
```

某些参数可能需要在多个单元测试，或多个数据测试（如上）中传递，可以使用静态参数，或者使用`@Shared`标注。或者可以简化成这样：

```groovy
class MathSpec extends Specification {
  def "maximum of two numbers"() {
    expect:
    Math.max(a, b) == c

    where:
    a | b || c
    1 | 3 || 3
    7 | 4 || 7
    0 | 0 || 0
  }
}
```

测试方法入参可以省略，然后断言两边可以用`||`分隔。



### @Unroll

上面的测试只会提示错误的测试情况，增加这个注解就会显示其他用例的情况。

```groovy
@Unroll
def "maximum of two numbers"() {
...
    
//结果就是
maximum of two numbers[0]   PASSED
maximum of two numbers[1]   FAILED

Math.max(a, b) == c
    |    |  |  |  |
    |    7  4  |  7
    42         false

maximum of two numbers[2]   PASSED
```

但是自己测试了一下，发现并没有生效，不清楚什么情况。



### Data Pipes

```groovy
...
where:
a << [1, 7, 0]
b << [3, 4, 0]
c << [3, 7, 0]
```

其实就是一种语法糖，这里相当于创建了三个集合。



### Multi-Variable Data Pipes

```groovy
@Shared sql = Sql.newInstance("jdbc:h2:mem:", "org.h2.Driver")

def "maximum of two numbers"() {
  expect:
  Math.max(a, b) == c

  where:
  [a, b, c] << sql.rows("select a, b, c from maxdata")
}

//或者
...
where:
[a, b, _, c] << sql.rows("select * from maxdata")
```

通过这种方式还可以直接给多个参数赋值



### More on Unrolled Method Names

在方法签名上面不能使用`groovy`语法`${}`来获取参数值，可以使用`#`代替，但是不是所有场景都能用

```groovy
def "#person is #person.age years old"() { // property access
def "#person.name.toUpperCase()"() { // zero-arg method call
def "#person.name.split(' ')[1]" {  // cannot have method arguments
def "#person.age / 2" {  // cannot use operators
def "#lastName"() { // zero-arg method call
  ...
  where:
  person << [new Person(age: 14, name: 'Phil Cole')]
  lastName = person.name.split(' ')[1]
}
```



## Interaction Based Testing

​	

















