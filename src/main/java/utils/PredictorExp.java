package utils;

import it.sauronsoftware.cron4j.Predictor;

/**
 * @description:
 * @author: youyinnn
 * @date: 2017/8/21
 */
public class PredictorExp {

    public static void main(String[] args) {
        String pattern = "0 3 * jan-jun,sep-dec mon-fri";
        Predictor p = new Predictor(pattern);
        for (int i = 0; i < 5; i++) {
            System.out.println(p.nextMatchingDate());
        }
    }

}
