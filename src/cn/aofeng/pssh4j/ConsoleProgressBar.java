package cn.aofeng.pssh4j;

import java.text.DecimalFormat;

/**
 * 控制台字符型进度条。
 * 
 * @author <a href="mailto:aofengblog@163.com">聂勇</a>
 */
public class ConsoleProgressBar {

    private long minimum = 0; // 进度条起始值
    
    private long maximum = 100; // 进度条最大值
    
    private long barLen = 100; // 进度条长度
    
    private char showChar = '='; // 用于进度条显示的字符
    
    private DecimalFormat formater = new DecimalFormat("#.##%");
    
    /**
     * 使用系统标准输出，显示字符进度条及其百分比。
     */
    public ConsoleProgressBar() {
    }
    
    /**
     * 使用系统标准输出，显示字符进度条及其百分比。
     * 
     * @param minimum 进度条起始值
     * @param maximum 进度条最大值
     * @param barLen 进度条长度
     */
    public ConsoleProgressBar(long minimum, long maximum, 
            long barLen) {
        this(minimum, maximum, barLen, '=');
    }
    
    /**
     * 使用系统标准输出，显示字符进度条及其百分比。
     * 
     * @param minimum 进度条起始值
     * @param maximum 进度条最大值
     * @param barLen 进度条长度
     * @param showChar 用于进度条显示的字符
     */
    public ConsoleProgressBar(long minimum, long maximum, 
            long barLen, char showChar) {
        this.minimum = minimum;
        this.maximum = maximum;
        this.barLen = barLen;
        this.showChar = showChar;
    }
    
    /**
     * 显示进度条。
     * 
     * @param value 当前进度。进度必须大于或等于起始点且小于等于结束点（start <= current <= end）。
     */
    public void show(long value) {
        if (value < minimum || value > maximum) {
            return;
        }
        
        reset();
        minimum = value;
        float rate = (float) (minimum*1.0 / maximum);
        long len = (long) (rate * barLen);
        draw(len, rate);
        if (minimum == maximum) {
            afterComplete();
        }
    }

    private void draw(long len, float rate) {
        for (int i = 0; i < len; i++) {
            System.out.print(showChar);
        }
        System.out.print(' ');
        System.out.print(format(rate));
        System.out.print(' ');
    }
    
    private void reset() {
        System.out.print('\r');
    }
    
    private void afterComplete() {
        System.out.print('\n');
    }
    
    private String format(float num) {
        return formater.format(num);
    }
    
    public static void main(String[] args) throws InterruptedException {
        ConsoleProgressBar cpb = new ConsoleProgressBar(0, 100, 20, '=');
        for (int i = 1; i <= 100; i++) {
            cpb.show(i);
            Thread.sleep(100);
        }
    }

}
