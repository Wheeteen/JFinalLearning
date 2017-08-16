package common;

import com.jfinal.core.JFinal;

/**
 * @description:
 * @author: youyinnn
 * @date: 2017/8/16
 */
public class Start {

    public static void main(String[] args) {
        JFinal.start("src/main/webapp", 80 , "/");
    }

}
