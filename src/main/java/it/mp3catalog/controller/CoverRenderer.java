/*
 *    Copyright 2012 Luca Tagliani
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */
package it.mp3catalog.controller;

import java.text.DecimalFormat;
import javax.swing.SwingConstants;
import javax.swing.table.DefaultTableCellRenderer;

/**
 *
 * @author luca
 */
public class CoverRenderer extends DefaultTableCellRenderer {

    private DecimalFormat df = new DecimalFormat("00");

    public CoverRenderer() {
        super();
        setHorizontalAlignment(SwingConstants.RIGHT);
    }

    @Override
    public void setValue(Object value) {
        // The value is always a long representing the length in seconds of the
        // song
        setText((value == null) ? "00:00" : "" + ((((Long) value) - (((Long) value) % 60)) / 60) + ":" + df.format((((Long) value) % 60)));
    }
}
