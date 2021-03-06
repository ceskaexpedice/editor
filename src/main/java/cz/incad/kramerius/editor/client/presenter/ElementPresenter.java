/*
 * Copyright (C) 2010 Jan Pokorsky
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
 */

package cz.incad.kramerius.editor.client.presenter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.google.gwt.core.client.GWT;

import cz.incad.kramerius.editor.client.EditorConstants;
import cz.incad.kramerius.editor.client.view.ElementView;
import cz.incad.kramerius.editor.client.view.ViewUtils;
import cz.incad.kramerius.editor.share.GWTKrameriusObject;
import cz.incad.kramerius.editor.share.GWTKrameriusObject.Kind;

/**
 *
 * @author Jan Pokorsky
 */
public class ElementPresenter implements Presenter, ElementView.Callback {


    public static final Logger LOGGER = Logger.getLogger("cz.incad.kramerius.editor.client.presenter.ElementPresenter");
    
    private ElementView display;
    private GWTKrameriusObject model;
    private final EditorPresenter ebus;
    private boolean isBound = false;

    
    public ElementPresenter(ElementView display, EditorPresenter bus) {
        this.display = display;
        this.ebus = bus;
    }

    public void setModel(GWTKrameriusObject model) {
        this.model = model;
    }
    public void bind() {
        if (isBound) {
            return;
        }
        isBound = true;
        display.setCallback(this);

        display.setLocation(model.getLocation());
        String title = model.getProperties().get("title");

        String sermodel = model.getProperties().toString();
        LOGGER.log(Level.INFO,sermodel );
        
        display.setLabel(ViewUtils.makeLabelVisible(title != null ? title : "notitle", 15));


        display.setModel(ViewUtils.makeLabelVisible(model.getKind().toLocalizedString(), 15));
//        List<String> prohibited = Arrays.asList("title","rootTitle","model","pid","policy");
//        
//        Set<String> keyset = model.getProperties().keySet();
//        List<String> details = new ArrayList<String>();
//        for (String k : keyset) {
//            if (!prohibited.contains(k)) {
//                details.add(model.getProperties().get(k));
//            }
//        }

        String rootTitle = model.getProperties().get("rootTitle");
        display.setRootTitle(ViewUtils.makeLabelVisible(rootTitle != null ? rootTitle : "notitle", 15));
//
//        StringBuilder builder = new StringBuilder();
//        for (int i = 0,ll=details.size(); i < ll; i++) {
//            if (i > 0) builder.append(",");
//            builder.append(details.get(i));
//        }
        
        display.setDetail(model.getProperties().get("details"));
     
        String cTitle = model.getProperties().get("constructedTitle");
        if (cTitle != null) {
            display.setTooltip(model.getKind().toLocalizedString() + " (" + title+") ‣ "+cTitle);
        } else {
            display.setTooltip(model.getKind().toLocalizedString() + " (" + title+")");
        }
        display.setOpenEnabled(model.getKind() != Kind.PAGE);
    }

    @Override
    public Display getDisplay() {
        return display;
    }

    public GWTKrameriusObject getModel() {
        return model;
    }

    @Override
    public void onPreviewClick() {
        display.showPreview(model.getPreviewLocation());
    }

    @Override
    public void onOpenClick() {
        ebus.load(model.getPID());
    }

}