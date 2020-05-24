package win.iot4yj.utils.reptile;

import java.net.URL;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.junit.Test;

public class HtmlParse {

    @Test
    public void htmlParse() throws Exception {
        //这里是爬取京东java关键字的网页内容，搜索的时候需要对中文转义
        String url = "https://search.jd.com/Search?keyword=java";
        //这样就获取了一个普通等JS页面
        Document doc = Jsoup.parse(new URL(url), 30000);
        Element ele = doc.getElementById("J_goodsList");
//        System.out.println(ele.html());
        //获取所有的<li>
        Elements liEles = ele.getElementsByTag("li");
        //获取元素中的内容
        liEles.forEach(liEle -> {
            //很多网站的图片是懒加载的，所以这种方式可能一下子获取不到，需要通过其他标签来获取
            String img = ele.getElementsByTag("img").eq(0).attr("src");
            String price = ele.getElementsByClass("p-price").eq(0).text();
            System.out.println("img:" + img + ", price:" + price);
        });
    }

}
