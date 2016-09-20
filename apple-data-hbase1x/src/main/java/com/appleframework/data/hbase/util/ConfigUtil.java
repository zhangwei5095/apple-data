package com.appleframework.data.hbase.util;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.LineNumberReader;
import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;

import com.appleframework.data.hbase.core.Nullable;

/**
 * ConfigUtil.
 * 
 * @author xinzhi
 * */
public class ConfigUtil {

    /** log. */
    private static Logger log = Logger.getLogger(ConfigUtil.class);

    /**
     * Return positive integer value by parsing the value with key in config,
     * otherwise returns defaultValue.
     * */
    public static int parsePositiveInt(Map<String, String> config, String key, int defaultValue) {
        Util.checkNull(config);
        Util.checkEmptyString(key);
        Util.check(defaultValue > 0);

        try {
            String vaule = config.get(key);
            int result = Integer.parseInt(vaule);
            if (result > 0) {
                return result;
            } else {
                return defaultValue;
            }
        } catch (Exception e) {
            return defaultValue;
        }
    }

    /**
     * Load config file.
     * */
    public static Map<String, String> loadConfigFile(@Nullable InputStream inputStream) throws IOException {

        Map<String, String> result = new HashMap<String, String>();
        if (inputStream == null) {
            return result;
        }

        LineNumberReader lineNumberReader = null;
        try {
            lineNumberReader = new LineNumberReader(new InputStreamReader(inputStream));
            for (String line = lineNumberReader.readLine(); line != null; line = lineNumberReader.readLine()) {

                String[] parts = line.split("=");
                if (parts == null || parts.length != 2) {
                    log.warn("wrong config line.  line=" + line);
                    continue;
                }

                result.put(parts[0], parts[1]);
            }
        } finally {
            if (lineNumberReader != null) {
                lineNumberReader.close();
            }
        }

        return result;
    }

    private ConfigUtil() {
    }
}
