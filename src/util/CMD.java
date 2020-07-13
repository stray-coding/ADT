package util;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * @author: Coding.He
 * @date: 2020/7/13
 * @emil: 229101253@qq.com
 * @des:
 */

public class CMD {

    public static Process CMD(String cmd, OnResultListener resultListener) {
        Process p = null;
        try {
            cmd = "cmd.exe /c " + cmd;
            System.out.println(cmd);
            p = Runtime.getRuntime().exec(cmd);
            new Thread(new cmdResult(p.getInputStream(), resultListener)).start();
            new Thread(new cmdResult(p.getErrorStream(), resultListener)).start();
            p.getOutputStream().close();
        } catch (Exception e) {
            System.out.println("命令行出错！");
            e.printStackTrace();
        }
        return p;
    }

    public static Process CMD(String cmd) {
        return CMD(cmd, null);
    }


/*    public static Process CMD(String cmd, String... args) {
        return CMD(String.format(cmd, args));
    }*/

    private static Process runCMD(String cmd) {
        Process p = null;
        try {
            cmd = "cmd.exe /c start " + cmd;
            System.out.println(cmd);
            p = Runtime.getRuntime().exec(cmd);
            new Thread(new cmdResult(p.getInputStream(), null)).start();
            new Thread(new cmdResult(p.getErrorStream(), null)).start();
            p.getOutputStream().close();
        } catch (Exception e) {
            System.out.println("命令行出错！");
            e.printStackTrace();
        }
        return p;
    }

    private static Process runCMD(String cmd, String... args) {
        return runCMD(String.format(cmd, args));
    }

    static class cmdResult implements Runnable {
        private InputStream ins;
        private OnResultListener resultListener;

        public cmdResult(InputStream ins, OnResultListener resultListener) {
            this.ins = ins;
            this.resultListener = resultListener;
        }

        @Override
        public void run() {
            try {
                BufferedReader reader = new BufferedReader(new InputStreamReader(ins));
                String line;
                while ((line = reader.readLine()) != null) {
                    if (resultListener != null) {
                        resultListener.onContinueResult(line + "\n");
                    }
                    System.out.println(line);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public interface OnResultListener {
        /*每一行返回一次*/
        void onContinueResult(String msg);
    }
}

