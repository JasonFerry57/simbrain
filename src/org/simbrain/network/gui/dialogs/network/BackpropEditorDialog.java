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
package org.simbrain.network.gui.dialogs.network;

import org.simbrain.network.core.NetworkUpdateAction;
import org.simbrain.network.gui.NetworkPanel;
import org.simbrain.network.gui.trainer.IterativeControlsPanel;
import org.simbrain.network.subnetworks.BackpropNetwork;
import org.simbrain.network.trainers.BackpropTrainer;
import org.simbrain.network.trainers.BackpropTrainer2;
import org.simbrain.network.trainers.IterableTrainer;
import org.simbrain.network.trainers.Trainer;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

/**
 * <b>BackpropDialog</b> is a dialog box for editing a Backprop network.
 */
public class BackpropEditorDialog extends SupervisedTrainingDialog {

    /**
     * Reference to the backprop network being edited.
     */
    private BackpropNetwork backprop;

    /**
     * An update action to update the backprop trainer when the network is updated.
     */
    private NetworkUpdateAction updater = new NetworkUpdateAction() {
        @Override
        public void invoke() {
            try {
                currentTrainer.apply();
            } catch (Trainer.DataNotInitializedException ex) {
                JOptionPane.showMessageDialog(null, "Unable to apply trainer: data not initialized.");
            }
        }

        @Override
        public String getDescription() {
            return "Apply Backprop Trainer";
        }

        @Override
        public String getLongDescription() {
            return "Applies one training step (usually one epoch) of the currently opened trainer dialog to the" + "associated BackpropNetwork.";
        }
    };

    /**
     * Make it easy to switch between the new, experimental trainer
     * (BackpropTrainer2), and the old one. Once that's stabilized this code can
     * be removed.
     */
    private boolean useExperimentalTrainer = true;
    private IterableTrainer currentTrainer;

    /**
     * Default constructor.
     *
     * @param networkPanel parent panel
     * @param backprop     edited network
     */
    public BackpropEditorDialog(NetworkPanel networkPanel, BackpropNetwork backprop) {
        super((Frame) SwingUtilities.getRoot(networkPanel), networkPanel, backprop);
        this.backprop = backprop;
        init();
        initDefaultTabs();
        updateData();
    }

    /**
     * This method initializes the components on the panel.
     */
    private void init() {
        setTitle("Edit Backprop Network");

        // Trainer tab
        if (useExperimentalTrainer) {
            currentTrainer = new BackpropTrainer2(backprop);
        } else {
            currentTrainer = new BackpropTrainer(backprop);
        }
        networkPanel.getNetwork().getUpdateManager().addAction(updater);
        IterativeControlsPanel iterativeControls = new IterativeControlsPanel(networkPanel, currentTrainer);
        addTab("Train", iterativeControls);
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosed(WindowEvent e) {
                currentTrainer.commitChanges();
                networkPanel.getNetwork().getUpdateManager().removeAction(updater);
            }
        });
    }

    @Override
    protected void stopTrainer() {
        if (currentTrainer != null) {
            currentTrainer.setUpdateCompleted(true);
            currentTrainer.commitChanges();
        }
    }

    @Override
    void updateData() {
        currentTrainer.initData();
    }
}
