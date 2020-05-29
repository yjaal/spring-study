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