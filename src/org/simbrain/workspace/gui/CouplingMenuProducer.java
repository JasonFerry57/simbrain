/*
 * Part of Simbrain--a java-based neural network kit
 * Copyright (C) 2005,2007 The Authors.  See http://www.simbrain.net/credits
 *
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 2 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA  02111-1307, USA.
 */
package org.simbrain.workspace.gui;

import org.simbrain.workspace.Consumer;
import org.simbrain.workspace.Producer;
import org.simbrain.workspace.Workspace;
import org.simbrain.workspace.WorkspaceComponent;

import javax.swing.*;

/**
 * Menu for making a single coupling. This menu is initialized with a potential
 * producer. It then produces a hierarchy of menus: One menu for each component
 * in the workspace. Within each component, a menuitem for each Potential
 * Consumer.
 */
public class CouplingMenuProducer extends JMenu {

    /**
     * Reference to workspace.
     */
    Workspace workspace;

    /**
     * The base attribute for this menu.
     */
    Producer<?> producer;

    /**
     * Construct the menu.
     *
     * @param menuName  the name of the menu
     * @param workspace the workspace
     * @param producer  the target consuming attribute.
     */
    public CouplingMenuProducer(String menuName, Workspace workspace, Producer<?> producer) {
        super(menuName);
        this.workspace = workspace;
        this.producer = producer;
        updateMenu();
    }

    /**
     * Update the menu to reflect current configuration of components in the
     * workspace.
     */
    private void updateMenu() {
        this.removeAll();
        for (WorkspaceComponent component : workspace.getComponentList()) {
            JMenu componentMenu = new JMenu(component.getName());
            for (Consumer<?> consumer : component.getWorkspace().getCouplingFactory().getAllConsumers(component)) {
                if (consumer.getType() == producer.getType()) {
                    CouplingMenuItem menuItem = new CouplingMenuItem(workspace, consumer.toString(), producer, consumer);
                    componentMenu.add(menuItem);
                }
            }
            this.add(componentMenu);
        }
    }

}