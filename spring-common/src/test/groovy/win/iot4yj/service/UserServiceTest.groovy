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