package com.cerbon.cerbons_api.api.static_utilities;

import com.cerbon.cerbons_api.platform.Services;

import java.nio.file.Path;

public class MiscUtils {

    /**
     * Gets the name of the current platform
     *
     * @return The name of the current platform.
     */
    public static String getPlatformName() {
        return Services.PLATFORM.getPlatformName();
    }

    /**
     * Get the current directory for game configuration files.
     * @return the configuration directory.
     */
    public static Path getConfigDir() {
        return Services.PLATFORM.getConfigDir();
    }

    /**
     * Checks if a mod with the given id is loaded.
     *
     * @param modId The mod to check if it is loaded.
     * @return True if the mod is loaded, false otherwise.
     */
    public static boolean isModLoaded(String modId) {
        return Services.PLATFORM.isModLoaded(modId);
    }

    /**
     * Check if the game is currently in a development environment.
     *
     * @return True if in a development environment, false otherwise.
     */
    public static boolean isDevelopmentEnvironment() {
        return Services.PLATFORM.isDevelopmentEnvironment();
    }

    /**
     * Check if we are running the client distribution.
     * @return True if in client dist, false otherwise.
     */
    public static boolean isClientDist() {
        return Services.PLATFORM.isClientDist();
    };

    /**
     * Gets the name of the environment type as a string.
     *
     * @return The name of the environment type.
     */
    public static String getEnvironmentName() {
        return Services.PLATFORM.getEnvironmentName();
    }
}
