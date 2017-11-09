/*
 * Copyright (c) 2016, WSO2 Inc. (http://www.wso2.org) All Rights Reserved.
 *
 * WSO2 Inc. licenses this file to you under the Apache License,
 * Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */

package org.wso2.carbon.identity.policy.password;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.wso2.carbon.utils.CarbonUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;

public class PasswordChangeUtils {
    public static final String IDM_PROPERTIES_FILE = "identity-mgt.properties";
    public static final String PASSWORD_EXP_IN_DAYS = "Authentication.Policy.Password.Reset.Time.In.Days";
    public static final String LAST_PASSWORD_CHANGED_TIMESTAMP_CLAIM = "http://wso2.org/claims/lastPasswordChangedTimestamp";
    public static final int DEFAULT_PASSWORD_EXP_IN_DAYS = 30;
    public static final String PASSWORD_EXP_BYPASS_ROLE = "Authentication.Policy.Password.Reset.Bypass.Role";
    public static final String PASSWORD_EXP_BYPASS_DEFAULT_ROLE = "reserved_role";
    private static Properties properties = new Properties();

    private static final Log log = LogFactory.getLog(PasswordChangeUtils.class);

    static {
        loadProperties();
    }

    private PasswordChangeUtils() {
    }

    /**
     * loading the identity-mgt.properties file.
     */
    public static void loadProperties() {
        FileInputStream fileInputStream = null;
        String configPath = CarbonUtils.getCarbonConfigDirPath() + File.separator + "identity" + File.separator;
        try {
            configPath = configPath + IDM_PROPERTIES_FILE;
            fileInputStream = new FileInputStream(new File(configPath));
            properties.load(fileInputStream);
        } catch (FileNotFoundException e) {
            throw new RuntimeException("identity-mgt.properties file not found in " + configPath, e);
        } catch (IOException e) {
            throw new RuntimeException("identity-mgt.properties file reading error from " + configPath, e);
        } finally {
            if (fileInputStream != null) {
                try {
                    fileInputStream.close();
                } catch (Exception e) {
                    log.error("Error occurred while closing stream :" + e);
                }
            }
        }
    }

    /**
     * Get the password expiration days.
     */
    public static int getPasswordExpirationInDays() {
        if (properties.get(PASSWORD_EXP_IN_DAYS) != null) {
            return Integer.parseInt((String) properties.get(PASSWORD_EXP_IN_DAYS));
        } else {
            return DEFAULT_PASSWORD_EXP_IN_DAYS;
        }
    }

    /**
     * Get the name of the role that password reset enforcement should be bypassed
     * @return
     */
    public static String getPasswordResetBypassRole(){
        if (properties.get(PASSWORD_EXP_BYPASS_ROLE) != null) {
            return (String) properties.get(PASSWORD_EXP_BYPASS_ROLE);
        } else {
            return PASSWORD_EXP_BYPASS_DEFAULT_ROLE;
        }

    }
}