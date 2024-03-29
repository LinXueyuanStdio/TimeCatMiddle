package com.timecat.middle.block.util;


import com.timecat.middle.block.temp.Def;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by 张启 on 2017/3/31. 某些情况下，app 会出现一些错误，不一定会造成闪退，但我们同样希望获得相关信息。
 * 请注意，所记录的“错误”甚至可能都不是真正的“错误”，比如我们会记录尝试完成一次习惯、结果却无法完成的情况——这有 可能是正常的，比如用户让习惯的提醒通知一直保持到了第二天、再点击了完成这一次按钮，这时的确不能完成那一次；但
 * 有的时候，明明仍然在同一天、其它也都正常，但点击完成这一次按钮后就是无法正常完成，这就是应用的某个地方出错了， 我们需要找到出错的原因，因此需要更多的信息。
 */
public class PossibleMistakeHelper {

    public static final String TAG = "PossibleMistakeHelper";

    private PossibleMistakeHelper() {
    }

    public static void outputNewMistakeInBackground(final Exception e) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                outputNewMistake(e);
            }
        }).start();
    }

    public static void outputNewMistake(Exception exception) {
        File file = createNewLogFile();
        if (file == null) {
            return;
        }

        try {
            PrintWriter writer = new PrintWriter(new FileWriter(file));
            writer.println(System.currentTimeMillis());
            writer.print("APP Version:  ");
            writer.println(DeviceUtil.getDeviceInfo());
            writer.println();
            exception.printStackTrace(writer);
            writer.println();
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static File createNewLogFile() {
        String path = Def.Meta.APP_FILE_DIR + "/log";
        String time = new SimpleDateFormat("yyyyMMddHHmmss").format(new Date());
        String name = "possible_mistake_" + time + ".info";
        return FileUtil.createFile(path, name);
    }

}
