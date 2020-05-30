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