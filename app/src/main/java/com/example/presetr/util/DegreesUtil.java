package com.example.presetr.util;

import android.view.MotionEvent;

public class DegreesUtil {

    public static float getDistanceOfFingue(MotionEvent event){
        float dis_x = Math.abs(event.getX(0) - event.getX(1));
        float dis_y = Math.abs(event.getY(0) - event.getY(1));
        float cur_dis = (float) Math.sqrt(dis_x*dis_x+dis_y*dis_y);
        return cur_dis;
    }
    public static boolean getNode(float x1,float y1
                                 ,float x2,float y2
                                 ,float x3,float y3
                                 ,float x4,float y4
                                  ,float[] node){
        //如果没有交点，返回false,两条直线平行，斜率存在且相同；斜率都不存在
        //否则返回true,node存储交点坐标
        if((x1-x2)==0&&(x3-x4)==0) {
            return false;
        }
        if((x1-x2)==0||(x3-x4)==0){
            if(x1-x2!=0){
                node[0] = x3;
                float k1 = (y2-y1)/(x2-x1);
                float b1 = (y1*x2-y2*x1)/(x2-x1);
                float y = k1*x3+b1;
                node[1] = y;
            }
            if(x3-x4!=0){
                node[0] = x1;
                float k2 = (y4-y3)/(x4-x3);
                float b2 = (y3*x4-y4*x3)/(x4-x3);
                float y = k2 * x1 + b2;
                node[1] = y;
            }
            return true;
        }else{
            float k1 = (y2-y1)/(x2-x1);
            float b1 = (y1*x2-y2*x1)/(x2-x1);
            float k2 = (y4-y3)/(x4-x3);
            float b2 = (y3*x4-y4*x3)/(x4-x3);
            //斜率都存在
            //斜率相同
            if(k1==k2){
                return false;
            }
            //斜率不同
            float x = (b1-b2)/(k2 - k1);
            float y = (k2*b1-k1*b2)/(k2-k1);
            node[0] = x;
            node[1] = y;
            return true;
        }
    }
    public static double getRotate(double centerX,double centerY,double x1, double y1, double x2, double y2) {
        double abx = centerX - x1;
        double aby = centerY - y1;
        double acx = centerX - x2;
        double acy = centerY - y2;
        double bcx = x2 - x1;
        double bcy = y2 - y1;

        double c = Math.hypot(abx,aby);
        double b = Math.hypot(acx,acy);
        double a = Math.hypot(bcx,bcy);
        double cos1 = (c*c + b*b - a*a)/(2*b*c);
        if (cos1 >= 1) {
            cos1 = 1f;
        }
        double radian = Math.acos(cos1);
        double degree = Math.toDegrees(radian);

        float tanB = (float) ((y1 - centerY) / (x1 - centerX));
        float tanC = (float) ((y2 - centerY) / (x2 - centerX));
        //同一象限
        if ((x1 > centerX && y1 > centerY && x2 > centerX && y2 > centerY && tanB > tanC)// 第一象限
                || (x1 > centerX && y1 < centerY && x2 > centerX && y2 < centerY && tanB > tanC)// 第四象限
                || (x1 < centerX && y1 < centerY && x2 < centerX && y2 < centerY && tanB > tanC)// 第三象限
                || (x1 < centerX && y1 > centerY && x2 < centerX && y2 > centerY && tanB > tanC))// 第二象限
            return -degree;
        //不同象限
        if(x1>=centerX&&x2>centerX){
            if(y1>centerY&&y2<centerY)
                return -degree;

        }
        if(y1<=centerY&&y2<centerY){
            if(x1>centerX&&x2<centerX)
                return -degree;
        }
        if(x1<centerX&&x2<=centerX){
            if(y1<centerY&&y2>centerY)
                return -degree;
        }
        if(y1>centerY&&y2>=centerY){
            if(x1<centerX&&x2>centerX)
                return -degree;
        }
        return degree;
    }
}
