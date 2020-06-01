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



使用数据库中的数据

```groovy
package win.iot4yj.service


import groovy.sql.Sql
import spock.lang.Shared
import spock.lang.Specification

class SpockTest1 extends Specification {

    @Shared
    def sql = Sql.newInstance("jdbc:mysql://localhost:13306/spring_study", "root", "walp1314", "com.mysql.jdbc.Driver")

    def "test_database"() {
        expect:
        rows*.id == ['001', '002', '003', '004']
        where:
        rows = sql.rows("select * from t_user")
    }
}
```





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

### Mocking

```groovy
package win.iot4yj.service

interface Subscriber {
    void receive(String message)
}
```

```groovy
package win.iot4yj.service


class Publisher {
    List<Subscriber> subscribers = []
    int messageCount = 0
    void send(String message){
        subscribers*.receive(message)
        messageCount++
    }
}
```



```groovy
package win.iot4yj.service

import spock.lang.Specification

class PublisherTest extends Specification {
    Publisher publisher = new Publisher()
    Subscriber subscriber = Mock()
    Subscriber subscriber2 = Mock()

    def setup() {
        publisher.subscribers << subscriber // << is a Groovy shorthand for List.add()
        publisher.subscribers << subscriber2
    }

    def "should send messages to all subscribers"() {
        when:
        publisher.send("hello")

        then:
        1 * subscriber.receive("hello")
        1 * subscriber2.receive("hello")
    }
}
```

这里有几点要注意：一个是在`Spock`中`Mock`的方式，一个是这里可以看到两个`mock`对象各自调用了一次。

测试说明如下：

```
1 * subscriber.receive("hello")
|   |          |       |
|   |          |       argument constraint
|   |          method constraint
|   target constraint
cardinality
```

### cardinality

```groovy
1 * subscriber.receive("hello")      // exactly one call
0 * subscriber.receive("hello")      // zero calls
(1..3) * subscriber.receive("hello") // between one and three calls (inclusive)
(1.._) * subscriber.receive("hello") // at least one call
(_..3) * subscriber.receive("hello") // at most three calls
_ * subscriber.receive("hello")      // any number of calls, including zero
                                     // (rarely needed; see 'Strict Mocking')
```



### target constraint

```groovy
1 * subscriber.receive("hello") // a call to 'subscriber'
1 * _.receive("hello")          // a call to any mock object
```



### method constraint

```groovy
1 * subscriber.receive("hello") // a method named 'receive'
1 * subscriber./r.*e/("hello")  // a method whose name matches the given regular expression
                                // (here: method name starts with 'r' and ends in 'e')
1 * subscriber.status // same as: 1 * subscriber.getStatus()
1 * subscriber.setStatus("ok") // NOT: 1 * subscriber.status = "ok"
```

这里就是一些简单的写法



### argument constraints

```groovy
1 * subscriber.receive("hello")        // an argument that is equal to the String "hello"
1 * subscriber.receive(!"hello")       // an argument that is unequal to the String "hello"
1 * subscriber.receive()               // the empty argument list (would never match in our example)
1 * subscriber.receive(_)              // any single argument (including null)
1 * subscriber.receive(*_)             // any argument list (including the empty argument list)
1 * subscriber.receive(!null)          // any non-null argument
1 * subscriber.receive(_ as String)    // any non-null argument that is-a String
1 * subscriber.receive(endsWith("lo")) // any non-null argument that is-a String
1 * subscriber.receive({ it.size() > 3 && it.contains('a') })
// an argument that satisfies the given predicate, meaning that
// code argument constraints need to return true of false
// depending on whether they match or not
// (here: message length is greater than 3 and contains the character a)
```



### Matching Any Method Call

```groovy
1 * subscriber._(*_)     // any method on subscriber, with any argument list
1 * subscriber._         // shortcut for and preferred over the above

1 * _._                  // any method call on any mock object
1 * _                    // shortcut for and preferred over the above
```



### Declaring Interactions at Mock Creation Time

```groovy
Subscriber subscriber = Mock {
   1 * receive("hello")
   1 * receive("goodbye")
}
```

这里就相当于我们`Mock`出来了多个`Subscribe`。那当我们在断言的时候也可以这样

```groovy
with(subscriber) {
    1 * receive("hello")
    1 * receive("goodbye")
}
```

在判断的时候也可以使用相关表达式

```groovy
when:
publisher.send("hello")

then:
def message = "hello"
1 * subscriber.receive(message)
```

更为严谨的方法是

```groovy
when:
publisher.send("hello")

then:
interaction {
  def message = "hello"
  1 * subscriber.receive(message)
}
```

这样变量就只能在当前域中使用



### mocking class

上面都是`mock`相关的接口，如果要`mock class`，则需要`cglib-nodep-2.2` 和 `objenesis-1.2`的支持。如果是`jdk8`，则`cglib-nodep`版本需要在`3.2`以上



## Stubbing

`Stub`对象用来提供测试时所需要的测试数据，可以对各种交互设置相应的回应。

```groovy
subscriber.receive(_) >> "ok"
```

这里表示调用`receive`方法的时候返回值为`'ok'`

```groovy
subscriber.receive(_) >> "ok"
|          |       |     |
|          |       |     response generator
|          |       argument constraint
|          method constraint
target constraint
```

当然也可以设定返回多个值

```groovy
subscriber.receive(_) >>> ["ok", "error", "error", "ok"]
```

也可以设置规则

```groovy
subscriber.receive(_) >> { args -> args[0].size() > 3 ? "ok" : "fail" }
subscriber.receive(_) >> { String message -> message.size() > 3 ? "ok" : "fail" }
```

```groovy
subscriber.receive(_) >>> ["ok", "fail", "ok"] >> { throw new InternalError() } >> "ok"
```

这里就是表示前面三次返回前一个集合中的值，第四次返回异常，最后的情况都返回`ok`



### Other Kinds of Mock Objects

```groovy
Subscriber subscriber = Stub {
    receive("message1") >> "ok"
    receive("message2") >> "fail"
}
```

### spy

A *spy* is created with the `MockingApi.Spy` factory method:

```
SubscriberImpl subscriber = Spy(constructorArgs: ["Fred"])
```

A spy is always based on a real object. Hence you must provide a class type rather than an interface type, along with any constructor arguments for the type. If no constructor arguments are provided, the type’s default constructor will be used.

和在`powermock`中一样，`Spy`模拟出一个对象进行测试，如果测试符合条件，那么就使用模拟对象，如果测试不符合条件，那么就调用真实方法。

当然不仅可以`Spy`出一个对象，还可以`Spy`一个局部行为

```groovy
// this is now the object under specification, not a collaborator
MessagePersister persister = Spy {
  // stub a call on the same object
  isPersistable(_) >> true
}

when:
persister.receive("msg")

then:
// demand a call on the same object
1 * persister.persist("msg")
```





## Goovy Mocks

### Mocking All Instances of a Type

```groovy
def publisher = new Publisher()
publisher << new RealSubscriber() << new RealSubscriber()

RealSubscriber anySubscriber = GroovyMock(global: true)

when:
publisher.publish("message")

then:
2 * anySubscriber.receive("message")
```

这里其实就是使用`GroovyMock mock`出了一个全局的`Subscriber`对象，所有调用的`Subscriber`对象都是`mock`出的，如上面两个`RealSubscriber`对象。



### Mocking Constructors

```groovy
RealSubscriber anySubscriber = GroovySpy(global: true)

1 * new RealSubscriber("Fred")
```

此时如果我们设定

```groovy
new RealSubscriber("Fred") >> new RealSubscriber("Barney")
```

那么就表示如果使用`‘Fred’`来构造一个对象，那么都将被使用`'Barney'`构造出来的对象替代，这里使用了`Spy`。



### [Mocking Static Methods](http://spockframework.org/spock/docs/1.3/interaction_based_testing.html#_mocking_static_methods)

```groovy
GroovySpy(RealSubscriber, global: true)
```



## [Advanced Features](http://spockframework.org/spock/docs/1.3/interaction_based_testing.html#_advanced_features)

```groovy
def person = Mock(name: "Fred", type: Person, defaultResponse: ZeroOrNullResponse, verified: false)
```

这就是`mock`有参数的对象。



```groovy
MockUtil mockUtil = new MockUtil()
List list1 = []
List list2 = Mock()

expect:
!mockUtil.isMock(list1)
mockUtil.isMock(list2)
```

使用`MockUtil`判别一个对象是真实对象还是`mock`对象



## Extensions

```groovy
@Ignore
def "my feature"() { ... }

@Ignore
class MySpec extends Specification { ... }
```

和`junit`中的意思差不多

```groovy
def "I'll be ignored"() { ... }

@IgnoreRest
def "I'll run"() { ... }

def "I'll also be ignored"() { ... }
```

这里的意思是只有这个方法有效，其他方法都忽略

```groovy
@IgnoreIf({ System.getProperty("os.name").contains("windows") })
def "I'll run everywhere but on Windows"() { ... }

@Requires({ os.windows })
def "I'll only run on Windows"() { ... }
```



`PendingFeature`:

- it will mark failing tests as skipped
- if at least one iteration of a data-driven test fails it will be reported as skipped
- if every iteration of a data-driven test passes it will be reported as error

```groovy
@PendingFeature
def "not implemented yet"() { ... }
```



`Stepwise` 表示当测试方法间存在依赖关系时，标明测试方法将严格按照其在源代码中声明的顺序执行

```
@Stepwise
class RunInOrderSpec extends Specification {
  def "I run first"()  { ... }
  def "I run second"() { ... }
}
```



```groovy
@Timeout(5)
def "I fail if I run for more than five seconds"() { ... }

@Timeout(value = 100, unit = TimeUnit.MILLISECONDS)
def "I better be quick" { ... }


@Timeout(10)
class TimedSpec extends Specification {
  def "I fail after ten seconds"() { ... }
  def "Me too"() { ... }

  //将类上面的覆盖掉
  @Timeout(value = 250, unit = MILLISECONDS)
  def "I fail much faster"() { ... }
}
```



`Retry`就是表示可能在比如连接数据库的时候超时了，需要多连接几次这种情况

```groovy
class FlakyIntegrationSpec extends Specification {
  @Retry
  def retry3Times() { ... }

  @Retry(count = 5)
  def retry5Times() { ... }

  @Retry(exceptions=[IOException])
  def onlyRetryIOException() { ... }

  @Retry(condition = { failure.message.contains('foo') })
  def onlyRetryIfConditionOnFailureHolds() { ... }

  @Retry(condition = { instance.field != null })
  def onlyRetryIfConditionOnInstanceHolds() { ... }

  @Retry
  def retryFailingIterations() {
    ...
    where:
    data << sql.select()
  }

  @Retry(mode = Retry.Mode.FEATURE)
  def retryWholeFeature() {
    ...
    where:
    data << sql.select()
  }

  @Retry(delay = 1000)
  def retryAfter1000MsDelay() { ... }
}
```



`Use` 表示在测试类中调用其他类的方法

```groovy
class ListExtensions {
  static avg(List list) { list.sum() / list.size() }
}

class MySpec extends Specification {
  @Use(listExtensions)
  def "can use avg() method"() {
    expect:
    [1, 2, 3].avg() == 2
  }
}
```



```groovy
//加标题
@Title("This is easy to read")
class ThisIsHarderToReadSpec extends Specification {
  ...
}

//加一个基本的注释说明
@Narrative("""
As a user
I want foo
So that bar
""")
class GiveTheUserFooSpec() { ... }
```



### [ Include and Exclude](http://spockframework.org/spock/docs/1.3/extensions.html#_include_and_exclude)

*Include / Exclude Configuration*

```groovy
import some.pkg.Fast
import some.pkg.IntegrationSpec

runner {
  include Fast // could be either an annotation or a (base) class
  exclude {
    annotation some.pkg.Slow
    baseClass IntegrationSpec
  }
}
```



## [ Spring Module](http://spockframework.org/spock/docs/1.3/modules.html#_spring_module)

The Spring module enables integration with [Spring TestContext Framework](http://docs.spring.io/spring/docs/4.1.5.RELEASE/spring-framework-reference/html/testing.html#testcontext-framework). It supports the following spring annotations `@ContextConfiguration` and `@ContextHierarchy`. Furthermore, it supports the meta-annotation `@BootstrapWith` and so any annotation that is annotated with `@BootstrapWith` will also work, such as `@SpringBootTest`, `@WebMvcTest`.



Java Config

```groovy
class DetachedJavaConfig {
  def mockFactory = new DetachedMockFactory()

  @Bean
  GreeterService serviceMock() {
    return mockFactory.Mock(GreeterService)
  }

  @Bean
  GreeterService serviceStub() {
    return mockFactory.Stub(GreeterService)
  }

  @Bean
  GreeterService serviceSpy() {
    return mockFactory.Spy(GreeterServiceImpl)
  }

  @Bean
  FactoryBean<GreeterService> alternativeMock() {
    return new SpockMockFactoryBean(GreeterService)
  }
}
```

使用

```groovy
@Autowired @Named('serviceMock')
GreeterService serviceMock

@Autowired @Named('serviceStub')
GreeterService serviceStub

@Autowired @Named('serviceSpy')
GreeterService serviceSpy

@Autowired @Named('alternativeMock')
GreeterService alternativeMock

def "mock service"() {
  when:
  def result = serviceMock.greeting

  then:
  result == 'mock me'
  1 * serviceMock.getGreeting() >> 'mock me'
}

def "sub service"() {
  given:
  serviceStub.getGreeting() >> 'stub me'

  expect:
  serviceStub.greeting == 'stub me'
}

def "spy service"() {
  when:
  def result = serviceSpy.greeting

  then:
  result == 'Hello World'
  1 * serviceSpy.getGreeting()
}

def "alternatice mock service"() {
  when:
  def result = alternativeMock.greeting

  then:
  result == 'mock me'
  1 * alternativeMock.getGreeting() >> 'mock me'
}
```



**@SpringBean**

Registers mock/stub/spy as a spring bean in the test context. Registers mock/stub/spy as a spring bean in the test context.

```groovy
@SpringBean
Service1 service1 = Mock()

@SpringBean
Service2 service2 = Stub() {
  generateQuickBrownFox() >> "blubb"
}

def "injection with stubbing works"() {
  expect:
  service2.generateQuickBrownFox() == "blubb"
}

def "mocking works was well"() {
  when:
  def result = service1.generateString()

  then:
  result == "Foo"
  1 * service1.generateString() >> "Foo"
}
```



**SpringSpy**

If you want to spy on an existing bean, you can use the `@SpringSpy` annotation to wrap the bean in a spy. As with `@SpringBean` the field must be of the type you want to spy on, however you cannot use an initializer.

```groovy
@SpringSpy
Service2 service2

@Autowired
Service1 service1

def "default implementation is used"() {
  expect:
  service1.generateString() == "The quick brown fox jumps over the lazy dog."
}

def "mocking works was well"() {
  when:
  def result = service1.generateString()

  then:
  result == "Foo"
  1 * service2.generateQuickBrownFox() >> "Foo"
}
```



### [Spring Boot](http://spockframework.org/spock/docs/1.3/modules.html#_spring_boot)

The recommended way to use Spock mocks in `@WebMvcTest` or other `@SpringBootTest`-style tests, is to use the `@SpringBean` and `@SpringSpy` annotations as shown above.

Alternatively you can use an embedded config annotated with `@TestConfiguration` and to create the mocks using the `DetachedMockFactory`.

```groovy
@WebMvcTest
class WebMvcTestIntegrationSpec extends Specification {

  @Autowired
  MockMvc mvc

  @Autowired
  HelloWorldService helloWorldService

  def "spring context loads for web mvc slice"() {
    given:
    helloWorldService.getHelloMessage() >> 'hello world'

    expect: "controller is available"
    mvc.perform(MockMvcRequestBuilders.get("/"))
      .andExpect(status().isOk())
      .andExpect(content().string("hello world"))
  }

  @TestConfiguration
  static class MockConfig {
    def detachedMockFactory = new DetachedMockFactory()

    @Bean
    HelloWorldService helloWorldService() {
      return detachedMockFactory.Stub(HelloWorldService)
    }
  }
}
```



这里在集成的时候需要注意几点：

* 注意依赖包的版本
* `@Autowired`好像不好使

```groovy
package win.iot4yj.service

import org.springframework.boot.test.context.SpringBootTest
import spock.lang.Shared
import spock.lang.Specification
import spock.lang.Unroll

@SpringBootTest
class UserServiceTest extends Specification {

    @Shared
    private UserService userService;

    @Unroll
    def "test_UserService_getUserById"() {
        expect:
        user.name == 'yj'
        user.sex == '1'
        where:
        user = userService.getUserById("001")
    }
}
```

这样写是会报错的，`spock`中的相关语法还需要仔细研究，正确的写法如下

```groovy
package win.iot4yj.service.impl

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import spock.lang.Specification
import win.iot4yj.service.UserService

@SpringBootTest
class SpockTest2 extends Specification {

    @Autowired
    private UserService userService

    def "test_UserService_getUserById"() {
        given:
        def user = userService.getUserById("00424556")
        expect:
        user.name == 'yj'
    }

}
```



```groovy
package win.iot4yj.controller

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders
import org.springframework.test.web.servlet.result.MockMvcResultMatchers
import spock.lang.Specification

@AutoConfigureMockMvc
@WebMvcTest
class UserControllerTest extends Specification {

    @Autowired
    MockMvc mockMvc

    def "test controller"() {
        expect: "Status is 200 and the response is 'Hello world!'"
        mockMvc.perform(MockMvcRequestBuilders.get("/user/hello?msg=world"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn()
                .response
                .contentAsString == "hello world"
    }
}
```

注意：这里不会启动容器，一般用于`controller`层的测试，但是由于这个原因可能有些类无法加载，可能需要注释掉，但是这种方式会快很多


```java
package win.iot4yj.controller;


import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import io.swagger.annotations.Api;
import org.springframework.web.bind.annotation.RestController;

/**
 * <p>
 * 用户表 前端控制器
 * </p>
 *
 * @author joyang
 * @since 2020-05-24
 */
@RestController
@RequestMapping("/user")
@Api(value = "/user", tags = "UserController")
public class UserController {

    @GetMapping("hello")
    public String hello(String msg) {
        return "hello " + msg;
    }
}
```



注意：上面这种方式是不会启动嵌入式容器的。如果按照常规的启动容器的方式测试，则如下

```groovy
@SpringBootTest
class LoadContextTest extends Specification {
 
    @Autowired (required = false)
    private WebController webController
 
    def "when context is loaded then all expected beans are created"() {
        expect: "the WebController is created"
        webController
    }
}
```

其实这是一种集成测试了，我们不需要`MockMvc`对象，只需要

```java
@Autowired
private TestRestTemplate restTemplate;
```

然后使用上面对象发起请求即可测试`controller`类，当然可能还需要对`service`类进行`mock`，可以使用`Mock`。



















