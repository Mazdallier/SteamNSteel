/*
 * Copyright (c) 2014 Rosie Alexander and Scott Killen.
 *
 * This program is free software; you can redistribute it and/or modify it under
 * the terms of the GNU General Public License as published by the Free Software
 * Foundation; either version 3 of the License, or (at your option) any later
 * version.
 *
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY
 * WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A
 * PARTICULAR PURPOSE. See the GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License along with
 * this program; if not, see <http://www.gnu.org/licenses>.
 */

package mod.steamnsteel.configuration;

import com.google.common.base.Optional;
import mod.steamnsteel.reference.Reference;
import mod.steamnsteel.utility.Logger;
import cpw.mods.fml.client.event.ConfigChangedEvent;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.common.config.Configuration;
import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

public enum ConfigurationHandler
{
    INSTANCE;

    private File fileRef;
    private Configuration config;
    private Optional<Configuration> configOld = Optional.absent();

    public Configuration getConfig()
    {
        return config;
    }

    public void setConfig(File configFile)
    {
        fileRef = configFile;

        config = new Configuration(configFile, Reference.CONFIG_VERSION);

        if (!Reference.CONFIG_VERSION.equals(config.getDefinedConfigVersion()))
        {
            final File fileBak = new File(fileRef.getAbsolutePath() + '_' + new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date()) + ".old");
            Logger.warning("Your %s config file is out of date and could cause issues. The existing file will be renamed to %s and a new one will be generated.",
                    Reference.MOD_NAME, fileBak.getName());
            Logger.warning("%s will attempt to copy your old settings, but custom mod/tree settings will have to be migrated manually.", Reference.MOD_NAME);

            boolean success = fileRef.renameTo(fileBak);
            Logger.warning("Rename %s successful.", success ? "was" : "was not");
            configOld = Optional.of(config);
            config = new Configuration(fileRef, Reference.CONFIG_VERSION);
        }

        syncConfig(true);
    }

    public void syncConfig()
    {
        syncConfig(false);
    }

    private void syncConfig(boolean skipLoad)
    {
        if (!skipLoad)
        {
            try
            {
                config.load();
            } catch (final Exception e)
            {
                final File fileBak = new File(fileRef.getAbsolutePath() + '_' + new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date()) + ".errored");
                Logger.severe("An exception occurred while loading your config file. This file will be renamed to %s and a new config file will be generated.", fileBak.getName());
                Logger.severe("Exception encountered: %s", e.getLocalizedMessage());

                boolean success = fileRef.renameTo(fileBak);
                Logger.warning("Rename %s successful.", success ? "was" : "was not");

                config = new Configuration(fileRef, Reference.CONFIG_VERSION);
            }
        }

        Settings.INSTANCE.syncConfig(config);

        convertOldConfig();
        saveConfig();
    }

    private void saveConfig()
    {
        if (config.hasChanged())
        {
            config.save();
        }
    }

    private void convertOldConfig()
    {
        if (configOld.isPresent())
        {
            // Handle old config versions (none yet)

            Settings.INSTANCE.syncConfig(config);
            configOld = Optional.absent();
        }
    }

    @SubscribeEvent
    public void onConfigChanged(ConfigChangedEvent.OnConfigChangedEvent event)
    {
        if (event.modID.equals(Reference.MOD_ID))
        {
            saveConfig();
            syncConfig();
        }
    }
}